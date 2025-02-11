
document.addEventListener("DOMContentLoaded", function() {
    const loginForm = document.getElementById("loginForm");
    const registerForm = document.getElementById("registerForm");

    if (loginForm) {
        loginForm.addEventListener("submit", function(event) {
            event.preventDefault();
        });
    }

    if (registerForm) {
        registerForm.addEventListener("submit", function(event) {
            event.preventDefault();
        });
    }
});