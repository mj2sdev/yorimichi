/**
 * 
 * @param {formId} form 
 * @returns multipart/form-data
 */
function formData(form){
    const formdata = document.getElementById(form);
    return new FormData(formdata);
};