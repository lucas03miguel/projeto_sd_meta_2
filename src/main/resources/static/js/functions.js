window.onload = function () {
    const urlParams = new URLSearchParams(window.location.search);
    const error = urlParams.get('error');

    if (error) {
        console.log(error);
        const usernameInput = document.getElementById('username');
        const passwordInput = document.getElementById('password');
        usernameInput.classList.add('error');
        passwordInput.classList.add('error');
        shakeElement(usernameInput);
        shakeElement(passwordInput);
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
