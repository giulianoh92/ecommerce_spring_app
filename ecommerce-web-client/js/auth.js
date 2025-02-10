document.addEventListener("DOMContentLoaded", function() {
    const apiUrl = 'http://localhost:8080'; // Adjust the API URL as needed

    // Function to handle user registration
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

            alert('Registration successful! You can now log in.');
            window.location.href = 'login.html'; // Redirect to login page
        } catch (error) {
            displayErrorMessage(error.message);
        }
    }

    // Function to handle user login
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
    
            const token = await response.text(); // Read response as text
            localStorage.setItem('token', token); // Store JWT token
            window.location.href = 'home.html'; // Redirect to home page
        } catch (error) {
            displayErrorMessage(error.message);
        }
    }

    // Function to display error messages
    function displayErrorMessage(message) {
        const errorContainer = document.getElementById('errorContainer');
        if (errorContainer) {
            errorContainer.textContent = message;
            errorContainer.style.display = 'block';
        } else {
            alert(message);
        }
    }

    // Event listeners for the forms
    const registerForm = document.getElementById('registerForm');
    if (registerForm) {
        registerForm.addEventListener('submit', registerUser);
    }

    const loginForm = document.getElementById('loginForm');
    if (loginForm) {
        loginForm.addEventListener('submit', loginUser);
    }
});