document.addEventListener("DOMContentLoaded", function() {
    const searchInput = document.getElementById('searchInput');
    const urlInput = document.getElementById('urlInput');

    if (searchInput) {
        searchInput.addEventListener('keydown', function(event) {
            if (event.key === 'Enter') {
                event.preventDefault(); // Prevent the default form submission
                handleSearch(); // Call the search function
            }
        });
    }

    if (urlInput) {
        urlInput.addEventListener('keydown', function(event) {
            if (event.key === 'Enter') {
                event.preventDefault(); // Prevent the default form submission
                indexUrl(); // Call the URL indexing function
            }
        });
    }

    //if (document.getElementById('searchForm')) {
    //    setupWebSocketHandlers();
    //}
});

function handleError() {
    const urlInput = document.getElementById('urlInput');
    shakeElement(urlInput);
    urlInput.style.border = '2px solid red';
}

function shakeElement(element) {
    element.classList.add('shake');
    setTimeout(function() {
        element.classList.remove('shake');
    }, 500);
}

function submitRegisterForm() {
    document.getElementById('newUsername').value = document.getElementById('username').value;
    document.getElementById('newPassword').value = document.getElementById('password').value;
    document.getElementById('registerForm').submit();
}

function handleSearch() {
    const query = document.getElementById('searchInput').value;
    if (query.trim().length < 1) {
        shakeElement(document.getElementById('searchInput'));
        return;
    }
    window.location.href = `/search?query=${encodeURIComponent(query)}&page=0`;
    console.log('Search query:', query);
    sendMessage();
}

function loadSearchResults(query, page) {
    console.log('Loading search results for query:', query, 'page:', page);
    fetch(`/search-results?query=${encodeURIComponent(query)}&page=${page}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => response.json())
        .then(data => {
            console.log('Data received:', data);
            const searchResultList = document.getElementById('searchResultList');
            const paginationControls = document.getElementById('paginationControls');
            searchResultList.innerHTML = ''; // Clear previous results

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
            } else {
                console.error('Results is not an object:', data.results);
            }

            // Pagination controls
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
    history.pushState({ path: newURL }, '', newURL);
}

function indexUrl() {
    const url = document.getElementById('urlInput').value;
    fetch('/index-url', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ url })
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

function redirectToTopSearches() {
    window.location.href = '/top-searches';
}

let stompClient = null;
function connect() {
    if (typeof SockJS !== 'undefined' && typeof Stomp !== 'undefined') {
        const socket = new SockJS('/my-websocket');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function (frame) {
            console.log('Connected: ' + frame);
            stompClient.subscribe('/topic/messages', function (message) {
                showMessage(JSON.parse(message.body).content);
            });

            // Fetch previous messages
            fetch('/messages')
                .then(response => response.json())
                .then(messages => {
                    messages.forEach(message => showMessage(message.content));
                });
        });
    }
}


function sendMessage() {
    const query = document.getElementById('searchInput').value;
    console.log('Sending message:', query);

    stompClient.send("/app/message", {}, JSON.stringify({'content': query}));
    $("#message").val("");
    $("#searchInput").val("");
}

function showMessage(message) {
    if (typeof $ !== 'undefined') {
        $("#messages").append("<tr><td>" + message + "</td></tr>");
    }
}

function setupWebSocketHandlers() {
    $(function () {
        $("form").on('submit', function (e) {
            e.preventDefault();
        });
        //$("#searchForm").click(function() { sendMessage(); });
    });
}
