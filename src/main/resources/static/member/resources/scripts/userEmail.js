const emailForm = window.document.getElementById('emailForm');
const emailWarning = {
    getElement: () => window.document.getElementById('emailWarning'),
    hide: () => emailWarning.getElement().classList.remove('visible'),
    show: (text) => {
        emailWarning.getElement().innerText = text;
        emailWarning.getElement().classList.add('visible');
    }
};

emailForm.onsubmit = e => {
    e.preventDefault();
    emailWarning.hide();
    if (emailForm['email'].value !== ''){
        window.location.href = `./userRegister?email=${emailForm['email'].value}`;
        // 보낼 값이 있는 주소를 적어야된다.
    } else {
        emailWarning.show('이메일 주소를 입력해주세요.');
        emailForm['email'].focus();
        return false;
    }
};