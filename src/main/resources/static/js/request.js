const request = {
    requestUrl: '',
    fetchOption: {
        method: '',

    },

    get: function (endpoint, data){
        this.requestUrl = endpoint;
        this.requestUrl += '?' + new URLSearchParams(data).toString();
        this.fetchOption.method = 'GET';
        return this.send();
    },

    delete: function (endpoint, data){
        this.requestUrl = endpoint;
        this.requestUrl += '?' + new URLSearchParams(data).toString();
        this.fetchOption.method = 'DELETE';
        return this.send();
    },

    post: function (endpoint, data){
        this.requestUrl = endpoint;
        this.fetchOption.method = 'POST';
        if(data instanceof HTMLFormElement){
            this.fetchOption.body = new FormData(data);
        }else if(data&&data.constructor === Object){
            this.fetchOption.headers = {
                'Content-Type': 'application/json'
            };
            this.fetchOption.body = JSON.stringify(data);
        }
        return this.send();
    },

    put: function (endpoint, data){
        this.requestUrl = endpoint;
        this.fetchOption.method = 'PUT';
        if(data instanceof HTMLFormElement){
            this.fetchOption.body = new FormData(data);
        }else if(data&&data.constructor === Object){
            this.fetchOption.headers = {
                'Content-Type': 'application/json'
            };
            this.fetchOption.body = JSON.stringify(data);
        }
        return this.send();
    },

    patch: function (endpoint, data){
        this.requestUrl = endpoint;
        this.fetchOption.method = 'PATCH';
        if(data instanceof HTMLFormElement){
            this.fetchOption.body = new FormData(data);
        }else if(data&&data.constructor === Object){
            this.fetchOption.headers = {
                'Content-Type': 'application/json'
            };
            this.fetchOption.body = JSON.stringify(data);
        }
        return this.send();
    },

    send: async function (){
        const url = this.requestUrl;
        const options = this.fetchOption;

        this.clear();

        try{
            const response = await fetch(url, options)
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
    },

    clear: function(){
        this.requestUrl = '';
        this.fetchOption = {
             method: '',
        };
    }
}
request.clear();