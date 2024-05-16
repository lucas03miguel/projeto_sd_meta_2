function submitRegisterForm() {
    document.getElementById('newUsername').value = document.getElementById('username').value;
    document.getElementById('newPassword').value = document.getElementById('password').value;
    document.getElementById('registerForm').submit();
}
