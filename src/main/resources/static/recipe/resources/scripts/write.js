const form = window.document.getElementById('form');
const warning = {
    getElement: () => window.document.getElementById('warning'),
    hide: () => warning.getElement().classList.remove('visible'),
    show: (text) => {
        warning.getElement().innerText = text;
        warning.getElement().classList.add('visible');
    }
};

const noCoverImage = window.document.getElementById('noCoverImage');
noCoverImage.addEventListener('click', () => {
    form['coverImage'].click();
});

const coverImagePreview = window.document.getElementById('coverImagePreview');
coverImagePreview.addEventListener('click', () => {
    form['coverImage'].click();
});
form['coverImage'].addEventListener('input', () => {
    if ((form['coverImage'].files?.length ?? 0) === 0) {
        noCoverImage.classList.add('visible');
        coverImagePreview.removeAttribute('src');
        return;
    }
    coverImagePreview.setAttribute('src', window.URL.createObjectURL(form['coverImage'].files[0]));
    noCoverImage.classList.remove('visible');
});

form['back'].addEventListener('click', () => {
    window.location.href = './';
});

ClassicEditor.create(form['content'], {
    simpleUpload: {
        uploadUrl: 'image'
    }
});

form.onsubmit = e => {
    e.preventDefault();
    warning.hide();

    if ((form['coverImage'].files?.length ?? 0) === 0) {
        warning.show('요리사진을 선택해주세요.');
        return false;
    }

    if (form['title'].value === '') {
        warning.show('제목을 입력해주세요.');
        form['title'].focus();
        return false;
    }

    if (form['content'].value === '') {
        warning.show('내용을 입력해주세요.');
        form['content'].focus();
        return false;
    }

    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('title', form['title'].value);
    formData.append('content', form['content'].value);
    formData.append('coverImageFile', form['coverImage'].files[0])
    xhr.open('POST', './write');
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseJson = JSON.parse(xhr.responseText);
                switch (responseJson['result']) {
                    case 'success':
                        const id = responseJson['id'];
                        window.location.href = `./read/${id}`
                        break;
                    default:
                        warning.show('알 수 없는 이유로 글을 작성하지 못하였습니다. \n 잠시 후 다시 시도해주세요.');
                }
            } else {
                warning.show('서버와 통신하지 못하였습니다. \n 잠시 후 다시 시도해주세요.');
            }
        }
    };
    xhr.send(formData);
};
























