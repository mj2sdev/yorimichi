(function (){
     const submitBtn = document.getElementById('myinfosubmit');
     
     submitBtn.addEventListener('click', async function() {
        const data = formData('myinfo');
        const response = await request('PUT', '/mypage', {body: data});
        if(response){
            alert('정보 수정 성공');
            window.location.reload();
        }else{
            alert('정보 수정 실패');
        }
    });
})();