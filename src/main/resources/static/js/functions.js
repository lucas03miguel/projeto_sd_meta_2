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

let currentPage = 0;

function fetchSearchResults(query) {
    fetch(`/search?page=${currentPage}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ query })
    })
        .then(response => response.json())
        .then(data => {
            displaySearchResults(data);
        })
        .catch(error => {
            console.error('Error:', error);
            alert('An error occurred while searching.');
        });
}

function displaySearchResults(results) {
    const searchList = document.getElementById('searchResultList');
    searchList.innerHTML = '';
    if (results.error) {
        alert(results.error[0]);
        return;
    }
    for (const [url, details] of Object.entries(results)) {
        const li = document.createElement('li');
        li.innerHTML = `<strong>Title:</strong> ${details[0]}<br><strong>URL:</strong> <a href="${url}" target="_blank">${url}</a><br><strong>Citation:</strong> ${details[1]}`;
        searchList.appendChild(li);
    }

    // Add pagination controls
    const paginationControls = document.getElementById('paginationControls');
    paginationControls.innerHTML = '';
    const prevButton = document.createElement('button');
    prevButton.textContent = 'Previous';
    prevButton.disabled = currentPage === 0;
    prevButton.onclick = () => {
        if (currentPage > 0) {
            currentPage--;
            fetchSearchResults(query);
        }
    };
    paginationControls.appendChild(prevButton);

    const nextButton = document.createElement('button');
    nextButton.textContent = 'Next';
    nextButton.disabled = Object.keys(results).length < 10;
    nextButton.onclick = () => {
        if (Object.keys(results).length === 10) {
            currentPage++;
            fetchSearchResults(query);
        }
    };
    paginationControls.appendChild(nextButton);
}

// Fetch search results on page load
document.addEventListener('DOMContentLoaded', () => {
    const query = new URLSearchParams(window.location.search).get('query');
    if (query) {
        fetchSearchResults(query);
    }
});
