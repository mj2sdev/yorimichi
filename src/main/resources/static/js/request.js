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