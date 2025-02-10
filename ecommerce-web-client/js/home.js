document.addEventListener("DOMContentLoaded", function() {
    const apiUrl = 'http://localhost:8080'; // Ajusta la URL de la API según sea necesario
    const token = localStorage.getItem('token');

    if (!token) {
        window.location.href = 'login.html'; // Redirige al login si no se encuentra el token
        return;
    }

    const userId = getUserIdFromToken(token);

    // Función para obtener productos
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
                throw new Error('Error al obtener productos');
            }

            const products = await response.json();

            console.log('Productos:', products);

            displayProducts(products);
        } catch (error) {
            console.error('Error al obtener productos:', error);
            alert('Error al obtener productos');
        }
    }

    // Función para mostrar productos en una cuadrícula
    function displayProducts(products) {
        const productGrid = document.getElementById('productGrid');
        productGrid.innerHTML = ''; // Limpia cualquier contenido existente

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

    // Función para mostrar detalles del producto en un modal
    function showProductDetails(product) {
        const modal = document.getElementById('productModal');
        document.getElementById('modalProductName').textContent = product.name;
        document.getElementById('modalProductDescription').textContent = product.description;
        document.getElementById('modalProductPrice').textContent = `Precio: $${product.unitPrice}`;
        document.getElementById('modalProductStock').textContent = `Stock: ${product.stock}`;
        document.getElementById('modalProductCategory').textContent = `Categoría: ${product.category}`;
        const quantityInput = document.getElementById('productQuantity');
        quantityInput.max = product.stock;
        quantityInput.value = 0;
        document.getElementById('addToCartButton').onclick = () => addToCart(product.id, quantityInput.value);
        modal.style.display = 'block';
    }

    // Función para agregar producto al carrito
    async function addToCart(productId, quantity) {
        if (quantity <= 0) {
            alert('La cantidad debe ser mayor que cero');
            return;
        }

        try {
            const response = await fetch(`${apiUrl}/users/cart/add`, {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    userId: userId,
                    productId: productId,
                    quantity: quantity
                })
            });

            if (!response.ok) {
                throw new Error('Error al agregar producto al carrito');
            }

            alert('Producto agregado al carrito');
            document.getElementById('productModal').style.display = 'none';
        } catch (error) {
            console.error('Error al agregar producto al carrito:', error);
            alert('Error al agregar producto al carrito');
        }
    }

    // Función para mostrar detalles del usuario en un modal
    async function showUserDetails() {
        try {
            const response = await fetch(`${apiUrl}/users/${userId}`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                }
            });

            if (!response.ok) {
                throw new Error('Error al obtener detalles del usuario');
            }

            const user = await response.json();

            const modal = document.getElementById('userModal');
            document.getElementById('modalUserName').textContent = `Nombre: ${user.firstName} ${user.lastName}`;
            document.getElementById('modalUserEmail').textContent = `Email: ${user.email}`;
            document.getElementById('modalUserAddress').textContent = `Dirección: ${user.address}`;
            document.getElementById('modalUserPhone').textContent = `Teléfono: ${user.phoneNumber}`;
            modal.style.display = 'block';
        } catch (error) {
            console.error('Error al obtener detalles del usuario:', error);
            alert('Error al obtener detalles del usuario');
        }
    }

    // Función para mostrar detalles del carrito en un modal
    async function showCartDetails() {
        try {
            const response = await fetch(`${apiUrl}/users/cart/user/${userId}`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                }
            });

            if (!response.ok) {
                throw new Error('Error al obtener detalles del carrito');
            }

            const cart = await response.json();

            const modal = document.getElementById('cartModal');
            const cartItemsContainer = document.getElementById('modalCartItems');
            cartItemsContainer.innerHTML = ''; // Limpia cualquier contenido existente

            cart.items.forEach(item => {
                const cartItem = document.createElement('div');
                cartItem.className = 'cart-item';
                cartItem.innerHTML = `
                    <p>${item.name} - Cantidad: ${item.quantity} - Precio Unitario: $${item.unitPrice} - Total: $${item.total}</p>
                `;
                cartItemsContainer.appendChild(cartItem);
            });

            const cartTotal = document.createElement('div');
            cartTotal.className = 'cart-total';
            cartTotal.innerHTML = `<p>Total del Carrito: $${cart.total}</p>`;
            cartItemsContainer.appendChild(cartTotal);

            document.getElementById('clearCartButton').onclick = () => clearCart(cart.id);
            document.getElementById('checkoutButton').onclick = () => checkoutCart(cart.id);

            modal.style.display = 'block';
        } catch (error) {
            console.error('Error al obtener detalles del carrito:', error);
            alert('Error al obtener detalles del carrito');
        }
    }

    // Función para limpiar el carrito
    async function clearCart(cartId) {
        try {
            const response = await fetch(`${apiUrl}/users/cart/clear/${cartId}`, {
                method: 'DELETE',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                }
            });

            if (!response.ok) {
                throw new Error('Error al limpiar el carrito');
            }

            alert('Carrito limpiado');
            document.getElementById('cartModal').style.display = 'none';
        } catch (error) {
            console.error('Error al limpiar el carrito:', error);
            alert('Error al limpiar el carrito');
        }
    }

    // Función para finalizar la compra
    async function checkoutCart(cartId) {
        try {
            const response = await fetch(`${apiUrl}/users/cart/checkout/${cartId}`, {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                }
            });

            if (!response.ok) {
                throw new Error('Error al finalizar la compra');
            }

            alert('Compra finalizada');
            document.getElementById('cartModal').style.display = 'none';
        } catch (error) {
            console.error('Error al finalizar la compra:', error);
            alert('Error al finalizar la compra');
        }
    }

    // Función para mostrar detalles de pedidos en un modal
    async function showOrdersDetails() {
        try {
            const response = await fetch(`${apiUrl}/users/orders/user/${userId}`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                }
            });

            if (response.status === 404) {
                alert('¡Aún no has hecho ningún pedido!');
                return;
            }

            if (!response.ok) {
                throw new Error('Error al obtener detalles de los pedidos');
            }

            const orders = await response.json();

            const modal = document.getElementById('ordersModal');
            const ordersItemsContainer = document.getElementById('modalOrdersItems');
            ordersItemsContainer.innerHTML = ''; // Limpia cualquier contenido existente

            orders.forEach(order => {
                const orderItem = document.createElement('div');
                orderItem.className = 'order-item';
                orderItem.innerHTML = `
                    <p>Pedido ID: ${order.id} - Total: $${order.total} - Estado: ${order.status}</p>
                    <div class="order-items">
                        ${order.items.map(item => `
                            <p>${item.name} - Cantidad: ${item.quantity} - Precio Unitario: $${item.unitPrice} - Total: $${item.total}</p>
                        `).join('')}
                    </div>
                `;
                ordersItemsContainer.appendChild(orderItem);
            });

            modal.style.display = 'block';
        } catch (error) {
            console.error('Error al obtener detalles de los pedidos:', error);
            alert('Error al obtener detalles de los pedidos');
        }
    }

    // Función para obtener el ID del usuario desde el token JWT
    function getUserIdFromToken(token) {
        const payload = JSON.parse(atob(token.split('.')[1]));
        console.log('Token payload:', payload);
        return payload.userId;
    }

    // Cierra el modal cuando el usuario hace clic en <span> (x)
    document.querySelectorAll('.modal .close').forEach(closeButton => {
        closeButton.addEventListener('click', function() {
            closeButton.closest('.modal').style.display = 'none';
        });
    });

    // Cierra el modal cuando el usuario hace clic fuera del modal
    window.addEventListener('click', function(event) {
        document.querySelectorAll('.modal').forEach(modal => {
            if (event.target === modal) {
                modal.style.display = 'none';
            }
        });
    });

    // Obtiene y muestra productos al cargar la página
    fetchProducts();

    // Listeners para los iconos de usuario, carrito y pedidos
    document.getElementById('userIcon').addEventListener('click', showUserDetails);
    document.getElementById('cartIcon').addEventListener('click', showCartDetails);
    document.getElementById('ordersIcon').addEventListener('click', showOrdersDetails);

    // Funcionalidad de cierre de sesión
    document.getElementById('logout').addEventListener('click', function(event) {
        event.preventDefault();
        localStorage.removeItem('token');
        window.location.href = 'login.html';
    });
});