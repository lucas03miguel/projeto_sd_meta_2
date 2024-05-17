window.onload = function () {
    const urlParams = new URLSearchParams(window.location.search);
    const error = urlParams.get('error');

    if (error) {
        const usernameInput = document.getElementById('username');
        const passwordInput = document.getElementById('password');
        usernameInput.classList.add('error');
        passwordInput.classList.add('error');
        shakeElement(usernameInput);
        shakeElement(passwordInput);

        alert('Crendiciais inválidas!');
    }
}

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
        })
        .catch(error => {
            console.error('Error:', error);
            alert('An error occurred while indexing the URL.');
        });
}


//verificar
function submitSearch() {
    const searchInput = document.getElementById('searchInput').value;
    if (searchInput) {
        window.location.href = '/search?query=' + searchInput;
    }
}
