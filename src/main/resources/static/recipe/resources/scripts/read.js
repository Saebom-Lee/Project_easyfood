const id = parseInt(window.location.href.split('/').at(-1).split('?')[0]);

const coverImage = window.document.getElementById('coverImage');
const title = window.document.getElementById('title');
const content = window.document.getElementById('content');
const createdAt = window.document.getElementById('createdAt');
const name = window.document.getElementById('name');
const back = window.document.getElementById('back');
const modifyButton = window.document.getElementById('modifyButton');
const deleteButton = window.document.getElementById('deleteButton');
const thirdForm = window.document.getElementById('thirdForm');
const secondForm = window.document.getElementById('secondForm');
const writeName = window.document.getElementById('writeName');
const commentWriteButton = window.document.getElementById('commentWriteButton');
const commentContent = window.document.getElementById('commentContent');
const commentModifyButton = window.document.getElementById('commentModifyButton');
const changeContainer = window.document.getElementById('change-container');
const commentBackButton = window.document.getElementById('commentBackButton');

back.addEventListener('click', () => {
    window.location.href = '/recipe/';
});

const xhr = new XMLHttpRequest();
xhr.open('POST', window.location.href);
xhr.onreadystatechange = () => {
    if (xhr.readyState === XMLHttpRequest.DONE) {
        if (xhr.status >= 200 && xhr.status < 300) {
            const responseJson = JSON.parse(xhr.responseText);
            const createdAtObj = new Date(responseJson['createdAt']);
            window.document.title = `EASYFOOD | ${responseJson['title']}`;
            coverImage.setAttribute('src', `../cover-image/${id}`);
            title.innerText = responseJson['title'];
            content.innerHTML = responseJson['content'];
            writeName.innerText = responseJson['writeName'];
            name.innerText = responseJson['userName'];
            if ((responseJson['mine'] ?? false) === true) {
                modifyButton.classList.add('visible');
                deleteButton.classList.add('visible');
            }
            createdAt.innerText = `${createdAtObj.getFullYear()}-${createdAtObj.getMonth() + 1}-${createdAtObj.getDate()} ${createdAtObj.getHours()}:${createdAtObj.getMinutes()}`;
        } else if (xhr.status === 404) {
            alert('???????????? ?????? ??????????????????.');
            if (window.history.length > 0) {
                window.history.back();
            } else {
                alert('????????? ???????????? ??????????????????.');
                window.history.back();
            }
        } else {
        }
    }
};
xhr.send();

modifyButton.addEventListener('click', () => {
    window.location.href = `../modify/${id}`;
});

deleteButton.addEventListener('click', () => {
    if (!confirm('????????? ???????????? ?????????????????????????')) {
        return;
    }
    const xhr = new XMLHttpRequest();
    xhr.open('DELETE', window.location.href);
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseJson = JSON.parse(xhr.responseText);
                switch (responseJson['result']) {
                    case 'success':
                        alert('???????????? ??????????????? ?????????????????????.');
                        window.location.href = '../';
                        break;
                    case 'warn':
                        alert('????????? ???????????? ???????????? ???????????? ????????????.');
                        break;
                    default:
                        alert('??? ??? ?????? ????????? ???????????? ???????????? ??????????????????.\n?????? ??? ?????? ??????????????????.');
                }
            } else {
                alert('????????? ???????????? ??????????????????. ?????? ??? ?????? ??????????????????.');
            }
        }
    };
    xhr.send();
});

commentWriteButton.addEventListener('click', () => {
    const xhr = new XMLHttpRequest();
    xhr.open('GET', `../comment/${id}?content=${secondForm['content'].value}`);
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseJson = JSON.parse(xhr.responseText);
                switch (responseJson['result']) {
                    case 'success':
                        break;
                    default :
                        alert('??? ??? ?????? ????????? ?????? ????????? ??????????????????.');
                }
            } else {
                alert('????????? ???????????? ??????????????????. ?????? ??? ?????? ??????????????????.');
            }
        }
    };
    xhr.send();
});

const commentContainer = window.document.getElementById('commentContainer');
const indexPage = () => {
    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('commentId', commentId);
    xhr.open('POST', `../comment/${id}`);
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseJson = JSON.parse(xhr.responseText);

                for (let comment of responseJson['recipeComments']) {
                    if (id == comment['boardIndex']) {

                        const commentsElement = window.document.createElement('div');
                        commentsElement.classList.add('commentsContainer');
                        commentsElement.classList.add('visible');
                        commentsElement.dataset.id = comment['index'];

                        const contentElement = window.document.createElement('div');
                        contentElement.classList.add('commentContent');
                        contentElement.innerText = comment['content'];

                        const nameElement = window.document.createElement('span');
                        nameElement.classList.add('commentContent');
                        nameElement.innerText = comment['userName'];

                        const dateElement = window.document.createElement('span');
                        dateElement.classList.add('commentCreatedAt');
                        const date = new Date(comment['createdAt']);
                        const dateText = `${(date.getFullYear() + '.' + (date.getMonth() + 1) + '.' + date.getDate() + ' ' + date.getHours() + ':' + date.getMinutes())}`
                        dateElement.innerText = `${dateText}`;

                        const buttonElement = window.document.createElement('div');
                        buttonElement.classList.add('buttonContainer');

                        const modifyElement = window.document.createElement('input');
                        modifyElement.classList.add('commentModifyButton');
                        // modifyElement.classList.add('visible');
                        modifyElement.setAttribute('value', '??????');

                        const deleteElement = window.document.createElement('input');
                        deleteElement.classList.add('commentDeleteButton');
                        // deleteElement.classList.add('visible');
                        deleteElement.setAttribute('value', '??????');

                        const hr = window.document.createElement('hr');
                        hr.classList.add('line');

                        buttonElement.append(modifyElement, deleteElement);
                        commentsElement.append(contentElement, nameElement, dateElement, buttonElement);
                        commentContainer.append(commentsElement, hr);

                        if (responseJson['writeName'] == comment['userName']) {
                            modifyElement.classList.add('visible');
                            deleteElement.classList.add('visible');
                        }

                        deleteElement.addEventListener('click', () => {
                            deleteComment(comment['index']);
                        });

                        modifyElement.addEventListener('click', () => {
                            changeContainer.classList.add('visible');
                            modifyComment(comment['index']);
                        });
                    }
                }

            } else {
                alert('????????? ???????????? ??????????????????. ?????? ??? ?????? ??????????????????.');
            }
        }
    };
    xhr.send(formData);
};

let commentId = -1;
indexPage();

const deleteComment = (commentId) => {
    if (!confirm('????????? ????????? ?????????????????????????')) {
        return;
    }
    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('commentId', commentId);
    xhr.open('DELETE', '../comment/');
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status >= 200 && xhr.status < 300) {
                // commentId = comment['index'];
                const responseJson = JSON.parse(xhr.responseText);
                switch (responseJson['result']) {
                    case 'success':
                        alert('????????? ??????????????? ?????????????????????.');
                        window.location.href = `../read/${id}`
                        break;

                    default:
                        alert('??? ??? ?????? ????????? ????????? ???????????? ??????????????????.\n?????? ??? ?????? ??????????????????.');
                }
            } else {
                alert('????????? ???????????? ??????????????????. ?????? ??? ?????? ??????????????????.');
            }
        }
    };
    xhr.send(formData);
};

commentBackButton.addEventListener('click', () => {
    window.location.href = `../read/${id}`;
});

const modifyComment = (commentId) => {
    thirdForm.onsubmit = e => {
        e.preventDefault();
        const xhr = new XMLHttpRequest();
        const formData = new FormData();
        formData.append('commentId', commentId);
        formData.append('content', thirdForm['content'].value);
        xhr.open('PATCH', `../comment/${id}`);
        xhr.onreadystatechange = () => {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status >= 200 && xhr.status < 300) {
                    const responseJson = JSON.parse(xhr.responseText);
                    switch (responseJson['result']) {
                        case 'success':
                            alert('????????? ??????????????? ?????????????????????.');
                            window.location.href = `../read/${id}`;
                            break;
                        default :
                            alert('??? ??? ?????? ????????? ?????? ????????? ??????????????????.\n?????? ??? ?????? ??????????????????.');
                    }
                } else {
                    alert('????????? ???????????? ??????????????????. ?????? ??? ?????? ??????????????????.');
                }
            }
        };
        xhr.send(formData);
    }
};
