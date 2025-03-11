document.addEventListener("DOMContentLoaded", function() {
    const apiUrl = 'http://localhost:8080';

    // Funcion para manejar el registro de usuarios
    async function registerUser(event) {
        event.preventDefault();
        
        const formData = new FormData(event.target);
        const userData = {
            email: formData.get('email'),
            password: formData.get('password'),
            firstName: formData.get('firstName'),
            lastName: formData.get('lastName'),
            address: formData.get('address'),
            phoneNumber: formData.get('phoneNumber')
        };

        try {
            const response = await fetch(`${apiUrl}/users/register`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(userData)
            });

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.detail || 'Registration failed');
            }

            window.location.href = 'login.html'; // redirigir a la página de inicio de sesión
        } catch (error) {
            displayErrorMessage(error.message);
        }
    }

    // funcion para manejar el inicio de sesión de usuarios
    async function loginUser(event) {
        event.preventDefault();
        
        const formData = new FormData(event.target);
        const loginData = {
            email: formData.get('email'),
            password: formData.get('password')
        };
    
        try {
            const response = await fetch(`${apiUrl}/users/login`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(loginData)
            });
    
            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.detail || 'Login failed');
            }
    
            const token = await response.text(); // leer el token JWT de la respuesta
            localStorage.setItem('token', token); // guardar el token en el almacenamiento local
            window.location.href = 'home.html'; // redirigir a la página de inicio
        } catch (error) {
            displayErrorMessage(error.message);
        }
    }

    // funcion para mostrar mensajes de error
    function displayErrorMessage(message) {
        const errorContainer = document.getElementById('errorContainer');
        if (errorContainer) {
            errorContainer.textContent = message;
            errorContainer.style.display = 'block';
        } else {
            alert(message);
        }
    }

    // Event listeners para los formularios de registro e inicio de sesión
    const registerForm = document.getElementById('registerForm');
    if (registerForm) {
        registerForm.addEventListener('submit', registerUser);
    }

    const loginForm = document.getElementById('loginForm');
    if (loginForm) {
        loginForm.addEventListener('submit', loginUser);
    }
});