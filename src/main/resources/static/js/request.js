const httpMethods = new Set(['GET', 'POST', 'PUT', 'PATCH', 'DELETE']);
async function request(method, endpoint, {params, query, body}={}){
    
    if(!method || !httpMethods.has(method.toUpperCase())){
        console.error("유효하지 않거나 지원하지 않는 method입니다.");
        throw new Error("유효하지 않거나 지원하지 않는 method입니다.");
    }
    const fetchOption = {
        method: method.toUpperCase(),
        headers: {}
    };

    let requestUrl = endpoint;

    if(params && typeof params === 'object' && !Array.isArray(params)){
        for(const key in params){
            requestUrl = requestUrl.replace(`{${key}}`,params[key]);
        }
    }

    if(query && typeof query === 'object' && !Array.isArray(query)){
        const queryString = new URLSearchParams(query).toString();
        if(queryString){
            requestUrl += "?" + queryString; 
        }
    }
    
    if(body){
        if(body instanceof FormData){
            fetchOption.body = body;
        }else{
            fetchOption.headers['Content-Type'] = 'application/json';
            fetchOption.body = JSON.stringify(body);
        }
    }
    
    try {
        const response = await fetch(requestUrl, fetchOption)
        if(!response.ok){
            throw new Error(response.status);
        }
        if(response.status === 204){
            return true;
        }
        return await response.json();
       
    } catch (error) {
        console.error('데이터 fetch 실패',error);
        return null;
    }
}