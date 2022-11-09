const secessionForm = window.document.getElementById('secessionForm');
const secessionWarning = {
    getElement: () => window.document.getElementById('secessionWarning'),
    hide: () => secessionWarning.getElement().classList.remove('visible'),
    show: (text) => {
        secessionWarning.getElement().innerText = text;
        secessionWarning.getElement().classList.add('visible');
    }
};

secessionForm.onsubmit = e => {
    e.preventDefault();
    secessionWarning.hide();

    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('password', secessionForm['password'].value);
    xhr.open('POST', './userSecession');
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseJson = JSON.parse(xhr.responseText);
                switch (responseJson['result']) {
                    case 'success':
                        if (!confirm('정말로 탈퇴하시겠습니까?')) {
                            return;
                        }
                        alert('회원탈퇴가 성공적으로 처리되었습니다.');
                        window.location.href = './userLogout';
                        break;
                    case 'warn':
                        secessionWarning.show('비밀번호가 일치하지 않습니다.\n다시 시도해주세요.');
                        break;
                    default:
                        secessionWarning.show('알 수 없는 이유로 회원탈퇴가 처리되지 못하였습니다.\n잠시 후 다시 시도해주세요.');
                }
            } else {
                secessionWarning.show('서버와 통신하지 못하였습니다. 잠시 후 다시 시도해주세요.');
            }
        }
    };
    xhr.send(formData);
};
