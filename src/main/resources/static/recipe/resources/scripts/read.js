const id = parseInt(window.location.href.split('/').at(-1).split('?')[0]);
const commentId = parseInt(window.location.href.split('/').at(-1).split('?')[0]);

const coverImage = window.document.getElementById('coverImage');
const title = window.document.getElementById('title');
const content = window.document.getElementById('content');
const createdAt = window.document.getElementById('createdAt');
const name = window.document.getElementById('name');
const back = window.document.getElementById('back');
const modifyButton = window.document.getElementById('modifyButton');
const deleteButton = window.document.getElementById('deleteButton');
const firstForm = window.document.getElementById('firstForm');
const secondForm = window.document.getElementById('secondForm');
const writeName = window.document.getElementById('writeName');
const commentWriteButton = window.document.getElementById('commentWriteButton');
const commentContent = window.document.getElementById('commentContent');
const commentModifyButton = window.document.getElementById('commentModifyButton');
const modifyContentButton = window.document.getElementById('modifyContentButton');

back.addEventListener('click', () => {
    window.location.href = '/recipe/';
});

const xhr = new XMLHttpRequest();
// const formData = new FormData();
// formData.append('commentWrite', secondForm['commentWrite'].value);
xhr.open('POST', window.location.href);
xhr.onreadystatechange = () => {
    if (xhr.readyState === XMLHttpRequest.DONE) {
        if (xhr.status >= 200 && xhr.status < 300) {
            const responseJson = JSON.parse(xhr.responseText);
            const createdAtObj = new Date(responseJson['createdAt']);
            window.document.title = `${responseJson['title']} :: 이지푸드`;
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
            alert('존재하지 않는 게시글입니다.');
            if (window.history.length > 0) {
                window.history.back();
            } else {
                alert('서버와 통신하지 못하였습니다.');
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
    if (!confirm('정말로 게시글을 삭제할까요?')) {
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
                        alert('게시글을 성공적으로 삭제하였습니다.');
                        window.location.href = '../';
                        break;
                    case 'warn':
                        alert('게시글 작성자의 이메일과 일치하지 않습니다.');
                        break;
                    default:
                        alert('알 수 없는 이유로 게시글을 삭제하지 못하였습니다. 잠시 후 다시 시도해주세요.');
                }
            } else {
                alert('서버와 통신하지 못하였습니다. 잠시 후 다시 시도해주세요.');
            }
        }
    };
    xhr.send();
});

// replyButton.addEventListener('click', () => {
//     if (!replyVisible.classList.contains('visible')) {
//         replyVisible.classList.add('visible');
//     } else {
//         replyVisible.classList.remove('visible');
//     }
// });

// commentModifyButton.addEventListener('click', () => {
//    if(!commentsContainer.classList.contains('visible')){
//        commentsContainer.classList.add('visible');
//    } else {
//        commentsContainer.classList.remove('visible');
//    }
// });

commentWriteButton.addEventListener('click', () => {
    const xhr = new XMLHttpRequest();
    // const formData = new FormData();
    // formData.append('content', secondForm['content'].value);
    xhr.open('GET', `../comment/${id}?content=${secondForm['content'].value}`);
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseJson = JSON.parse(xhr.responseText);
                switch (responseJson['result']) {
                    case 'success':
                        const commentId = responseJson['commentId'];
                        // window.location.href = `../comment/${commentId}`
                        break;
                    default :
                        alert('알 수 없는 이유로 댓글 작성에 실패했습니다.');
                }
            } else {
                alert('서버와 통신하지 못하였습니다.');
            }
        }
    };
    xhr.send();
});

const commentContainer = window.document.getElementById('commentContainer');
const modifyContainer = window.document.getElementById('comment-modify-container');
const indexPage = () => {
    const xhr = new XMLHttpRequest();
    // const formData = new FormData();
    // formData.append('commentId', commentId);
    xhr.open('POST', `../comment/${id}`);
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseJson = JSON.parse(xhr.responseText);
                const createdAtObj = new Date(responseJson['createdAt']);

                for (let comment of responseJson['recipeComments']) {
                    if (id == comment['boardIndex']) {

                        const commentsElement = window.document.createElement('div');
                        commentsElement.classList.add('commentsContainer');
                        commentsElement.classList.add('visible');
                        commentsElement.dataset.commentId = comment['index'];

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
                        modifyElement.classList.add('visible');
                        modifyElement.setAttribute('value', '수정');
                        modifyElement.id = 'commentModifyButton';

                        const deleteElement = window.document.createElement('input');
                        deleteElement.classList.add('commentDeleteButton');
                        deleteElement.classList.add('visible');
                        deleteElement.setAttribute('value', '삭제');
                        deleteElement.id = 'commentDeleteButton';

                        const hr = window.document.createElement('hr');
                        hr.classList.add('line');

                        // for(let comments of responseJson['comments']) {
                        //     if ((responseJson['mine'] ?? false) === true) {
                        //         modifyElement.classList.add('visible');
                        //         deleteElement.classList.add('visible');
                        //     }
                        // }


                        buttonElement.append(modifyElement, deleteElement);
                        commentsElement.append(contentElement, nameElement, dateElement, buttonElement);
                        commentContainer.append(commentsElement, hr);

                        // commentId = comment['index'];

                        const commentDeleteButton = window.document.getElementById('commentDeleteButton');
                        commentDeleteButton.addEventListener('click', () => {
                            deleteComment();
                        });

                        // modifyContentButton.addEventListener('click', () => {
                        //     const modifyNameElement = window.document.createElement('span');
                        //     modifyNameElement.classList.add('modifyName');
                        //     nameElement.innerText = comment['userName'];
                        //
                        //     const modifyContentElement = window.document.createElement('input');
                        //     modifyContentElement.classList.add('object-input');
                        //
                        //     const commentModifyElement = window.document.createElement('button');
                        //     commentModifyElement.classList.add('object-button');
                        //     commentModifyElement.setAttribute('value', '수정하기');
                        //
                        //     modifyContainer.append(modifyNameElement, modifyContentElement, commentModifyElement);
                        // });
                    }
                }

            } else {
                alert('서버와 통신하지 못하였습니다.');
            }
        }
    };
    xhr.send();
};

// let commentId = -1;
indexPage();

const deleteComment = () => {
    // if (!confirm('정말로 댓글을 삭제할까요?')) {
    //     return;
    // }
    const xhr = new XMLHttpRequest();
    // const formData = new FormData();
    // formData.append('commentId', commentId);
    xhr.open('DELETE', `../comment/${commentId}`);
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status >= 200 && xhr.status < 300) {
                // commentId = comment['index'];
                const responseJson = JSON.parse(xhr.responseText);
                switch (responseJson['result']) {
                    case 'success':
                        alert('댓글을 성공적으로 삭제하였습니다.');
                        // window.location.href = '../';
                        break;
                    case 'warn':
                        alert('댓글 작성자의 이메일과 일치하지 않습니다.');
                        break;
                    default:
                        alert('알 수 없는 이유로 댓글을 삭제하지 못하였습니다. 잠시 후 다시 시도해주세요.');
                }
            } else {
                alert('서버와 통신하지 못하였습니다. 잠시 후 다시 시도해주세요.');
            }
        }
    };
    xhr.send();
};

// let commentId = -1;


























