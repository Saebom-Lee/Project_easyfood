const recipeContainer = window.document.getElementById('recipeContainer');
const searchButton = window.document.getElementById('searchButton');

const indexPage = () => {

    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('lastArticleId', lastArticleId);
    xhr.open('POST', './');
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseJson = JSON.parse(xhr.responseText);

                for (let article of responseJson['recipeArticles']) {

                    const recipeElement = window.document.createElement('a');
                    recipeElement.classList.add('recipe');
                    recipeElement.dataset.id = article['index'];
                    recipeElement.setAttribute('href', `./read/${article['index']}`);

                    const imgElement = window.document.createElement('img');
                    imgElement.classList.add('cover-image');
                    imgElement.setAttribute('alt', '');
                    imgElement.setAttribute('src', `./cover-image/${article['index']}`);

                    const titleElement = window.document.createElement('span');
                    titleElement.classList.add('title');
                    titleElement.innerText = article['title'];

                    const contentElement = window.document.createElement('span');
                    contentElement.classList.add('content');
                    contentElement.innerText = article['content'];

                    recipeElement.append(imgElement, titleElement, contentElement);
                    recipeContainer.append(recipeElement);

                    lastArticleId = article['index'];

                    if (responseJson['recipeArticles'].length === 8) {
                        more.classList.add('visible');
                    } else {
                        more.classList.remove('visible');
                    }
                }
            } else {
                alert('서버와 통신하지 못하였습니다. 잠시만 기다려주세요.');
            }
        }
    };
    xhr.send(formData);
};

let lastArticleId = -1;
indexPage();

// const more = window.document.getElementById('more');
// more.addEventListener('click', () => {
//     indexPage();
// });

const searchPage = () => {

    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('lastArticleId', lastArticleId);
    formData.append('keyword', form['keyword'].value);
    xhr.open('PATCH', './');
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseJson = JSON.parse(xhr.responseText);

                for (let article of responseJson['recipeArticles']) {

                    const recipeElement = window.document.createElement('a');
                    recipeElement.classList.add('recipe');
                    recipeElement.dataset.id = article['index'];
                    recipeElement.setAttribute('href', `./read/${article['index']}`);

                    const imgElement = window.document.createElement('img');
                    imgElement.classList.add('cover-image');
                    imgElement.setAttribute('alt', '');
                    imgElement.setAttribute('src', `./cover-image/${article['index']}`);

                    const titleElement = window.document.createElement('span');
                    titleElement.classList.add('title');
                    titleElement.innerText = article['title'];

                    const contentElement = window.document.createElement('span');
                    contentElement.classList.add('content');
                    contentElement.innerText = article['content'];

                    recipeElement.append(imgElement, titleElement, contentElement);
                    recipeContainer.append(recipeElement);

                    lastArticleId = article['index'];

                    if (responseJson['recipeArticles'].length === 8) {
                        more.classList.add('visible');
                    } else {
                        more.classList.remove('visible');
                    }
                }
            } else {
                alert('서버와 통신하지 못하였습니다. 잠시만 기다려주세요.');
            }
        }
    };
    xhr.send(formData);
};

const form = window.document.getElementById('form');
form.onsubmit = e => {
    e.preventDefault();
    lastArticleId = -1;
    recipeContainer.innerText = '';
    searchPage();
};

const more = window.document.getElementById('more');
more.addEventListener('click', () => {
    indexPage();
});


