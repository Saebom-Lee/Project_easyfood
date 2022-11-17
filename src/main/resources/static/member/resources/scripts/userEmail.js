const emailForm = window.document.getElementById('emailForm');
const emailWarning = {
    getElement: () => window.document.getElementById('emailWarning'),
    hide: () => emailWarning.getElement().classList.remove('visible'),
    show: (text) => {
        emailWarning.getElement().innerText = text;
        emailWarning.getElement().classList.add('visible');
    }
};

// 이메일이 중복되면 중복메시지 표시
// 중복되지 않으면 회원가입 페이지의 이메일 폼으로 입력한 이메일을 넘긴다.
emailForm.onsubmit = e => {
    e.preventDefault();
    emailWarning.hide();

    if (emailForm['email'].value == '') {
        emailWarning.show('이메일 주소를 입력해주세요.');
        emailForm['email'].focus();
        return false;
    }

    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('email', emailForm['email'].value);
    xhr.open('POST', './userEmail');
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseJson = JSON.parse(xhr.responseText);
                switch (responseJson['result']) {
                    case 'success':
                        window.location.href = `./userRegister?email=${emailForm['email'].value}`;
                        break;
                    case 'duplicate':
                        emailWarning.show('입력하신 이메일을 사용할 수 없습니다.\n다른 이메일을 입력해주세요.');
                        break;
                    default:
                        emailWarning.show('알 수 없는 이유로 회원가입하지 못하였습니다.\n잠시 후 다시 시도해주세요.');
                }
            } else {
                emailWarning.show('서버와 통신하지 못하였습니다. 잠시 후 다시 시도해주세요.');
            }
        }
    };
    xhr.send(formData);
};