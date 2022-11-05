const id = parseInt(window.location.href.split('/').at(-1).split('?')[0]);

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
    window.location.href = `../read/${id}`;
});

form.onsubmit = e => {
    e.preventDefault();
    warning.hide();

    if (form['title'].value === '') {
        warning.show('제목을 입력해주세요.');
        form['title'].focus();
        return false;
    }

    if (editor.getData() === '') {
        warning.show('내용을 입력해주세요.');
        form['content'].focus();
        return false;
    }

    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('title', form['title'].value);
    formData.append('content', editor.getData());
    formData.append('coverImageFile', form['coverImage'].files[0])
    xhr.open('POST', window.location.href);
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseJson = JSON.parse(xhr.responseText);
                switch (responseJson['result']) {
                    case 'success':
                        window.location.href = `../read/${id}`;
                        break;
                    default:
                        warning.show('알 수 없는 이유로 게시글을 수정하지 못하였습니다. \n 잠시 후 다시 시도해주세요.');
                }
            } else {
                warning.show('서버와 통신하지 못하였습니다. \n 잠시 후 다시 시도해주세요.');
            }
        }
    };
    xhr.send(formData);
};

let editor;
const loadArticle = () => {
    const xhr = new XMLHttpRequest();
    xhr.open('PATCH', window.location.href);
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseJson = JSON.parse(xhr.responseText);
                coverImagePreview.setAttribute('src', `../cover-image/${responseJson['index']}`);
                form['title'].value = responseJson['title'];
                form['content'].value = responseJson['content'];
                ClassicEditor.create(form['content'], {
                    simpleUpload: {
                        uploadUrl: '../image'
                    }
                }).then(x => {
                    editor = x;
                });
            } else if (xhr.status === 403) {
                alert('게시글 작성자의 이메일과 일치하지 않습니다.');
            } else if (xhr.status === 404) {
                alert('존재하지 않는 게시글입니다.');
                if (window.history.length > 0) {
                    window.history.back();
                } else {
                    window.close();
                }
            } else {
                alert('서버와 통신하지 못하였습니다.');
                if (window.history.length > 0) {
                    window.history.back();
                }
            }
        }
    };
    xhr.send();
};

loadArticle();






















