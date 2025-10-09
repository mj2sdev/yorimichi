/**
 * 
 * @param {string} formName
 * @returns {FormData}
 * 리턴타입은 FormData 이며 multipart/form-data 방식으로 전송하기 위해 사용됩니다. 
 */
function formData(formName){
    const formdata = document.forms[formName];
    return new FormData(formdata);
};
