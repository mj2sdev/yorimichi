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

    const csrfKey = document.querySelector('meta[name="_csrf_header"]').content.trim();
    const csrfVal = document.querySelector('meta[name="_csrf"]').content.trim();

    if(csrfKey && csrfVal){
        fetchOption.headers[csrfKey] = csrfVal;
    }


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
        
        const contentType = response.headers.get("content-type");
        if (contentType && contentType.includes("application/json")) {
            return await response.json();
        } else {
            return await response.text();
        }
    } catch (error) {
        console.error('데이터 fetch 실패',error);
        return null;
    }
}