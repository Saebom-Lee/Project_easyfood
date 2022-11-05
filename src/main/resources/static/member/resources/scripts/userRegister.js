const registerForm = window.document.getElementById('registerForm');
const registerWarning = {
    getElement: () => window.document.getElementById('registerWarning'),
    hide: () => registerWarning.getElement().classList.remove('visible'),
    show: (text) => {
        registerWarning.getElement().innerText = text;
        registerWarning.getElement().classList.add('visible');
    }
};

registerForm.onsubmit = e => {
    e.preventDefault();
    registerWarning.hide();
    if (registerForm['email'].value === '') {
        registerWarning.show('이메일 주소를 입력해주세요.');
        registerForm['email'].focus();
        return false;
    }
    if (registerForm['password'].value === '') {
        registerWarning.show('비밀번호를 입력해주세요.');
        registerForm['password'].focus();
        return false;
    }
    if (registerForm['passwordCheck'].value === '') {
        registerWarning.show('비밀번호를 다시 입력해주세요.');
        registerForm['passwordCheck'].focus();
        return false;
    }
    if (registerForm['password'].value !== registerForm['passwordCheck'].value){
        registerWarning.show('입력한 비밀번호가 서로 다릅니다. 다시 확인해주세요.');
        registerForm['passwordCheck'].focus();
        return false;
    }
    if (registerForm['name'].value === '') {
        registerWarning.show('이름을 입력해주세요.');
        registerForm['name'].focus();
        return false;
    }
    if (registerForm['contact'].value === '') {
        registerWarning.show('연락처를 입력해주세요.');
        registerForm['contact'].focus();
        return false;
    }
    if (!registerForm['policyTerms'].checked) {
        registerWarning.show('서비스 이용약관을 읽고 동의해주세요.');
        registerForm['policyTerms'].focus();
        return false;
    }
    if (!registerForm['policyPrivacy'].checked) {
        registerWarning.show('개인정보 처리방침을 읽고 동의해주세요.');
        registerForm['policyPrivacy'].focus();
        return false;
    }

    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('email', registerForm['email'].value);
    formData.append('password', registerForm['password'].value);
    formData.append('name', registerForm['name'].value);
    formData.append('contact', registerForm['contact'].value);
    formData.append('policyMarketing', registerForm['policyMarketing'].checked);
    xhr.open('POST', './userRegister');
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseJson = JSON.parse(xhr.responseText);
                switch (responseJson['result']) {
                    case 'success':
                        window.location.href = '/';
                        break;
                    // case 'duplicate':
                    //     registerWarning.show('입력하신 이메일을 사용할 수 없습니다. 다른 이메일을 입력해 주세요.');
                    //     break;
                    default:
                        registerWarning.show('알 수 없는 이유로 회원가입하지 못 하였습니다.\n잠시 후 다시 시도해주세요.');
                }
            } else {
                // registerWarning.show('서버와 통신하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
                registerWarning.show('입력하신 이메일을 사용할 수 없습니다.\n다른 이메일을 입력해주세요.');
            }
        }
    };
    xhr.send(formData);
};
