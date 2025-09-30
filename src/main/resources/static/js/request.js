async function request(type = 'GET', endpoint, data){

    let requestUrl = endpoint;

    const fetchOption = {
        method: type.toUpperCase()
    };

    if(data instanceof FormData){
        fetchOption.body = data;
    }else if(data && data.constructor === Object){
        fetchOption.headers = {
            'Content-Type': 'application/json'
        };
        fetchOption.body = JSON.stringify(data);
    }else if(data){
        requestUrl += `/${data}`;
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