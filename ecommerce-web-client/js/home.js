document.addEventListener("DOMContentLoaded", function() {
    const apiUrl = 'http://localhost:8080'; // Adjust the API URL as needed
    const token = localStorage.getItem('token');

    if (!token) {
        window.location.href = 'login.html'; // Redirect to login if no token is found
        return;
    }

    // Function to fetch products
    async function fetchProducts() {
        try {
            const response = await fetch(`${apiUrl}/products`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                }
            });

            if (!response.ok) {
                throw new Error('Failed to fetch products');
            }

            const products = await response.json();

            console.log('Products:', products);

            displayProducts(products);
        } catch (error) {
            console.error('Error fetching products:', error);
            alert('Error fetching products');
        }
    }

    // Function to display products in a grid
    function displayProducts(products) {
        const productGrid = document.getElementById('productGrid');
        productGrid.innerHTML = ''; // Clear any existing content

        products.forEach(product => {
            const productCard = document.createElement('div');
            productCard.className = 'product-card';
            productCard.innerHTML = `
                <h3>${product.name}</h3>
                <p>${product.description}</p>
                <p>Precio: $${product.unitPrice}</p>
            `;
            productCard.addEventListener('click', () => showProductDetails(product));
            productGrid.appendChild(productCard);
        });
    }

    // Function to show product details in a modal
    function showProductDetails(product) {
        const modal = document.getElementById('productModal');
        document.getElementById('modalProductName').textContent = product.name;
        document.getElementById('modalProductDescription').textContent = product.description;
        document.getElementById('modalProductPrice').textContent = `Precio: $${product.unitPrice}`;
        document.getElementById('modalProductStock').textContent = `Stock: ${product.stock}`;
        document.getElementById('modalProductCategory').textContent = `Categoria: ${product.category}`;
        modal.style.display = 'block';
    }

    // Close the modal when the user clicks on <span> (x)
    document.querySelector('.modal .close').addEventListener('click', function() {
        document.getElementById('productModal').style.display = 'none';
    });

    // Close the modal when the user clicks anywhere outside of the modal
    window.addEventListener('click', function(event) {
        const modal = document.getElementById('productModal');
        if (event.target === modal) {
            modal.style.display = 'none';
        }
    });

    // Fetch and display products on page load
    fetchProducts();

    // Logout functionality
    document.getElementById('logout').addEventListener('click', function(event) {
        event.preventDefault();
        localStorage.removeItem('token');
        window.location.href = 'login.html';
    });
});