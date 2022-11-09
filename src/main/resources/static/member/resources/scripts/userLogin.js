const loginForm = window.document.getElementById('loginForm');
const loginWarning = {
    getElement: () => window.document.getElementById('loginWarning'),
    hide: () => loginWarning.getElement().classList.remove('visible'),
    show: (text) => {
        loginWarning.getElement().innerText = text;
        loginWarning.getElement().classList.add('visible');
    }
};

loginForm.onsubmit = e => {
    e.preventDefault();
    loginWarning.hide();
    if (loginForm['email'].value === '') {
        loginWarning.show('이메일을 입력해주세요.');
        loginForm['email'].focus();
        return false;
    }
    if (loginForm['password'].value === '') {
        loginWarning.show('비밀번호를 입력해주세요.');
        loginForm['password'].focus();
        return false;
    }

    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('email', loginForm['email'].value);
    formData.append('password', loginForm['password'].value);
    xhr.open('POST', './userLogin');
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseJson = JSON.parse(xhr.responseText);
                switch (responseJson['result']) {
                    case 'success':
                        window.location.href = '/';
                        break;
                    default:
                        loginWarning.show('이메일 혹은 비밀번호가 올바르지 않습니다.');
                        loginForm['email'].focus();
                }
            } else {
                loginWarning.show('서버와 통신하지 못하였습니다. 잠시 후 다시 시도해주세요.');
            }
        }
    };
    xhr.send(formData);
};














