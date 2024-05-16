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
    }
}

function shakeElement(element) {
    element.classList.add('shake');
    setTimeout(function() {
        element.classList.remove('shake');
    }, 500);
}
