this.clear;
const request = {
    requestUrl: '',
    fetchOption: {
        method: '',

    },

    get: function (endpoint, data){
        this.requestUrl = endpoint;
        this.requestUrl += '?' + new URLSearchParams(data).toString();
        this.fetchOption.method = 'GET';
        this.send();
    },

    delete: function (endpoint, data){
        this.requestUrl = endpoint;
        this.requestUrl += '?' + new URLSearchParams(data).toString();
        this.fetchOption.method = 'DELETE';
        this.send();
    },

    post: function (endpoint, data){
        this.requestUrl = endpoint;
        this.fetchOption.method = 'POST';
        if(data instanceof HTMLFormElement){
            this.fetchOption.headers = {
                'Contetn-Type': 'multipart/form-data'
            };
            this.fetchOption.body = new FormData(data);
        }else if(data&&data.constructor === Object){
            this.fetchOption.headers = {
                'Content-Type': 'application/json'
            };
            this.sfetchOption.body = JSON.stringify(data);
        }
        this.send();
    },

    put: function (endpoint, data){
        this.requestUrl = endpoint;
        this.fetchOption.method = 'PUT';
        this.fetchOption.headers = 'PUT';
        if(data instanceof HTMLFormElement){
            this.fetchOption.headers = {
                'Contetn-Type': 'multipart/form-data'
            };
            this.fetchOption.body = new FormData(data);
        }else if(data&&data.constructor === Object){
            this.fetchOption.headers = {
                'Content-Type': 'application/json'
            };
            this.sfetchOption.body = JSON.stringify(data);
        }
        this.send();
    },

    patch: function (endpoint, data){
        this.requestUrl = endpoint;
        this.fetchOption.method = 'PATCH';
        this.fetchOption.headers = 'PATCH';
        if(data instanceof HTMLFormElement){
            this.fetchOption.headers = {
                'Contetn-Type': 'multipart/form-data'
            };
            this.fetchOption.body = new FormData(data);
        }else if(data&&data.constructor === Object){
            this.fetchOption.headers = {
                'Content-Type': 'application/json'
            };
            this.sfetchOption.body = JSON.stringify(data);
        }
        this.send();
    },

    send: async function (){
        try{
            const response = await fetch(this.requestUrl, this.fetchOption)
            if(!response.ok){
                throw new Error(response.status);
            }
            if(response.status === 204){
                this.clear;
                return true;
            }
            this.clear;
            return await response.json();     
        } catch (error) {
            console.error('데이터 fetch 실패',error);
            this.clear;
            return null;
        }        
    },

    clear: function(){
        this.requestUrl = null;
        this.fetchOption = {};
    }
}


/*
async function request(method = 'GET', endpoint, data){

    let requestUrl = endpoint;

    const fetchOption = {
        method: method.toUpperCase()
    };

    if(method == 'GET' || method == 'DELETE'){
        requestUrl += '?' + new URLSearchParams(data).toString();
    }
    if(method == 'POST' || method == 'PUT' || method== 'PATCH'){

    }

    if(data instanceof FormData){
        fetchOption.body = data;
    }else if(data && data.constructor === Object){
        fetchOption.headers = {
            'Content-Type': 'application/json'
        };
        fetchOption.body = JSON.stringify(data);
    }else if(data){
        requestUrl += `/${data}`;
    }else {
        console.error('데이터 형식이 맞지 않습니다.',error);
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
*/