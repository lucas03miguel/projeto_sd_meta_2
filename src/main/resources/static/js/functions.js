document.addEventListener("DOMContentLoaded", function() {
    const searchInput = document.getElementById('searchInput');

    searchInput.addEventListener('keydown', function(event) {
        if (event.key === 'Enter') {
            event.preventDefault(); // Prevent the default form submission
            handleSearch(); // Call the search function
        }
    });
});

function submitRegisterForm() {
    document.getElementById('newUsername').value = document.getElementById('username').value;
    document.getElementById('newPassword').value = document.getElementById('password').value;
    document.getElementById('registerForm').submit();
}

function shakeElement(element) {
    element.classList.add('shake');
    setTimeout(function() {
        element.classList.remove('shake');
    }, 500);
}

function submitURL() {
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
            } else {
                alert('Failed to index URL.');
            }
            document.getElementById('urlInput').value = ''; // Clear the input field
        })
        .catch(error => {
            console.error('Error:', error);
            alert('An error occurred while indexing the URL.');
        });
}

function handleSearch() {
    const query = document.getElementById('searchInput').value;
    if (query.length < 1) {
        alert('Pesquisa inválida (1+ caracteres)');
        shakeElement(document.getElementById('searchInput'));
        return;
    }

    // Redirect to search results page with the query as a URL parameter
    window.location.href = `/search?query=${encodeURIComponent(query)}&page=0`;
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
                alert('Unexpected data format received. Please try again later.');
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
            alert('An error occurred while fetching search results.');
        });
}

function updateURL(query, page) {
    const newURL = `/search?query=${encodeURIComponent(query)}&page=${page}`;
    history.pushState({ path: newURL }, '', newURL);
}

// Initialize search results page
window.onload = function() {
    const params = new URLSearchParams(window.location.search);
    const query = params.get('query');
    const page = parseInt(params.get('page'), 10) || 0;
    console.log('Initializing search results page with query:', query, 'page:', page);
    if (query) {
        loadSearchResults(query, page);
    }
};
