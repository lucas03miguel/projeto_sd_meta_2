document.addEventListener("DOMContentLoaded", function () {
    const searchInput = document.getElementById('searchInput');
    const urlInput = document.getElementById('urlInput');
    const urlSearchInput = document.getElementById('urlSearchInput');

    if (searchInput) {
        searchInput.addEventListener('keydown', function (event) {
            if (event.key === 'Enter') {
                event.preventDefault();
                handleSearch();
            }
        });
    }

    if (urlInput) {
        urlInput.addEventListener('keydown', function (event) {
            if (event.key === 'Enter') {
                event.preventDefault();
                indexUrl();
            }
        });
    }

    if (urlSearchInput) {
        urlSearchInput.addEventListener('keydown', function (event) {
            if (event.key === 'Enter') {
                event.preventDefault();
                searchUrl();
            }
        });
    }

    connect();
});

function handleError() {
    const urlInput = document.getElementById('urlInput');
    shakeElement(urlInput);
    urlInput.style.border = '2px solid red';
}

function shakeElement(element) {
    element.classList.add('shake');
    setTimeout(function () {
        element.classList.remove('shake');
    }, 500);
}

function handleSearch() {
    const query = document.getElementById('searchInput').value;
    if (query.trim().length < 1) {
        shakeElement(document.getElementById('searchInput'));
        return;
    }
    window.location.href = `/search?query=${encodeURIComponent(query)}&page=0`;
    sendMessage();
}

function loadSearchResults(query, page) {
    fetch(`/search-results?query=${encodeURIComponent(query)}&page=${page}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => response.json())
        .then(data => {
            const searchResultList = document.getElementById('searchResultList');
            const paginationControls = document.getElementById('paginationControls');
            searchResultList.innerHTML = '';

            if (data.results && typeof data.results === 'object') {
                for (const [url, details] of Object.entries(data.results)) {
                    const title = details[0] || 'No title available';
                    const snippet = details[1] || 'No snippet available';
                    const li = document.createElement('li');
                    li.innerHTML = `<strong>Title:</strong> ${title}<br>
                                <strong>URL:</strong> <a href="${url}" target="_blank">${url}</a><br>
                                <strong>Snippet:</strong> ${snippet}`;
                    searchResultList.appendChild(li);
                }
            }

            paginationControls.innerHTML = '';
            if (!data.isLastPage) {
                const nextButton = document.createElement('button');
                nextButton.textContent = 'Next';
                nextButton.onclick = () => {
                    updateURL(query, page + 1);
                    loadSearchResults(query, page + 1);
                };
                paginationControls.appendChild(nextButton);
            }
            if (page > 0) {
                const prevButton = document.createElement('button');
                prevButton.textContent = 'Previous';
                prevButton.onclick = () => {
                    updateURL(query, page - 1);
                    loadSearchResults(query, page - 1);
                };
                paginationControls.appendChild(prevButton);
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

function updateURL(query, page) {
    const newURL = `/search?query=${encodeURIComponent(query)}&page=${page}`;
    history.pushState({path: newURL}, '', newURL);
}

function indexUrl() {
    const url = document.getElementById('urlInput').value;
    fetch('/index-url', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({url})
    })
        .then(response => {
            if (response.ok) {
                alert('URL successfully indexed!');
                document.getElementById('urlInput').value = '';
            } else {
                handleError();
            }
        })
        .catch(error => {
            console.error('Error:', error);
            handleError();
        });
}

function searchUrl() {
    const url = document.getElementById('urlSearchInput').value;
    window.location.href = `/search-url?url=${encodeURIComponent(url)}`;
}

function loadURLResults(url) {
    fetch(`/search-url?url=${encodeURIComponent(url)}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => response.json())
        .then(data => {
            const searchResultList = document.getElementById('searchResultList');
            searchResultList.innerHTML = '';

            if (Array.isArray(data)) {
                for (const resultUrl of data) {
                    const li = document.createElement('li');
                    li.innerHTML = `<strong>URL:</strong> <a href="${resultUrl}" target="_blank">${resultUrl}</a>`;
                    searchResultList.appendChild(li);
                }
            } else {
                console.error('Expected array but got:', data);
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

let stompClient = null;
function connect() {
    if (typeof SockJS !== 'undefined' && typeof Stomp !== 'undefined') {
        const socket = new SockJS('/my-websocket');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function (frame) {
            console.log('Connected: ' + frame);
        });
    }
}

function sendMessage() {
    const query = document.getElementById('searchInput').value;
    stompClient.send("/app/message", {}, JSON.stringify({'content': query}));
    $("#message").val("");
    $("#searchInput").val("");
}

function redirectTopSearches() {
    window.location.href = '/top-searches';
}

function redirectBarrels() {
    window.location.href = '/barrels';
}
