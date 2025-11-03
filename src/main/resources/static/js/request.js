const httpMethods = new Set(['GET', 'POST', 'PUT', 'PATCH', 'DELETE']);
/**
 * @author 3ll3702
 * @version 1.0
 * 
 * TODO: we need description!
 * @param {string} method 
 * @param {string} requestUrl 
 * @param {Object} object 
 * @returns we can't prediction
 */
async function request(method, requestUrl, {params, query, body} = {}){
    
    if(!method || !httpMethods.has(method.toUpperCase())){
        const errorMessage = "유효하지 않거나 지원하지 않는 method입니다.";
        console.error(errorMessage);
        throw new Error(errorMessage);
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
        const contentType = response.headers.get("Content-Type");
        const { status } = response;

        if(!response.ok){
            throw new Error(status);
        }
        if (contentType && contentType.includes("application/json")) {
            return await response.json();
        } else {
            const text = (await response.text()).trim();
            return text || true;
        }
    } catch (error) {
        console.error('데이터 fetch 실패', error);
        return null;
    }
}