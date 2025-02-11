document.addEventListener("DOMContentLoaded", function() {
    const apiUrl = 'http://localhost:8080'; // Ajusta la URL de la API según sea necesario
    const token = localStorage.getItem('token');
    let products = []; // Variable global para almacenar los productos
    let currentPage = 1;
    const itemsPerPage = 9;

    if (!token) {
        window.location.href = 'login.html'; // Redirige al login si no se encuentra el token
        return;
    }

    const userId = getUserIdFromToken(token);

    async function fetchCategories() {
        try {
            const response = await fetch(`${apiUrl}/products/categories`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                }
            });

            if (!response.ok) {
                throw new Error('Error al obtener categorías');
            }

            const categories = await response.json();
            const categoryFilter = document.getElementById('categoryFilter');
            categories.forEach(category => {
                const option = document.createElement('option');
                option.value = category.id;
                option.textContent = category.name;
                categoryFilter.appendChild(option);
            });
        } catch (error) {
            console.error('Error al obtener categorías:', error);
        }
    }

    // Función para obtener productos con filtros y paginación
    async function fetchProducts() {
        const searchQuery = document.getElementById('searchQuery').value;
        const categoryFilter = document.getElementById('categoryFilter').value;
        const minPrice = document.getElementById('minPrice').value;
        const maxPrice = document.getElementById('maxPrice').value;
        const sortBy = 'name'; // Puedes cambiar esto según el criterio de ordenamiento deseado
        const order = 'asc'; // Puedes cambiar esto a 'desc' para orden descendente

        try {
            const response = await fetch(`${apiUrl}/products?page=${currentPage}&limit=${itemsPerPage}&q=${searchQuery}&categoryId=${categoryFilter}&minPrice=${minPrice}&maxPrice=${maxPrice}&inStock=true&active=true&sortBy=${sortBy}&order=${order}`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                }
            });

            if (!response.ok) {
                throw new Error('Error al obtener productos');
            }

            const data = await response.json();
            products = data; // Almacena los productos en la variable global
            displayProducts(products);
            updatePagination(products.length);
        } catch (error) {
            console.error('Error al obtener productos:', error);
        }
    }

    // Función para mostrar productos en una cuadrícula
    function displayProducts(products) {
        const productGrid = document.getElementById('productGrid');
        productGrid.innerHTML = ''; // Limpia cualquier contenido existente
    
        products.forEach(product => {
            const productCard = document.createElement('div');
            productCard.className = 'product-card';
    
            // Genera un color pastel aleatorio
            const pastelColor = `hsl(${Math.random() * 360}, 100%, 85%)`;
    
            productCard.innerHTML = `
                <div class="product-image" style="background-color: ${pastelColor};"></div> <!-- Simulación de imagen del producto -->
                <h3>${product.name}</h3>
                <p>${product.description}</p>
                <p>Precio: $${product.unitPrice}</p>
                <p>Categoría: ${product.category}</p>
            `;
            productCard.addEventListener('click', () => showProductDetails(product));
            productGrid.appendChild(productCard);
        });
    }

    // Función para actualizar la paginación
    function updatePagination(productsCount) {
        document.getElementById('prevPage').disabled = currentPage === 1;
        document.getElementById('nextPage').disabled = productsCount < itemsPerPage;
    }

    // Event listeners para los botones de paginación
    document.getElementById('prevPage').addEventListener('click', () => {
        if (currentPage > 1) {
            currentPage--;
            fetchProducts();
        }
    });

    document.getElementById('nextPage').addEventListener('click', () => {
        currentPage++;
        fetchProducts();
    });

    // Event listener para el botón de aplicar filtros
    document.getElementById('applyFilters').addEventListener('click', () => {
        currentPage = 1; // Reinicia a la primera página al aplicar filtros
        fetchProducts();
    });

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
        quantityInput.min = 1; // Establece el límite inferior a 1
        quantityInput.value = 1; // Inicializa el valor en 1
        document.getElementById('addToCartButton').onclick = () => addToCart(product.id, quantityInput.value);
        modal.style.display = 'block';
    }

    // Función para agregar producto al carrito
    async function addToCart(productId, quantity) {
        if (quantity <= 0) {
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

            document.getElementById('productModal').style.display = 'none';
        } catch (error) {
            console.error('Error al agregar producto al carrito:', error);
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
                    <button class="decrement" data-id="${item.id}" data-quantity="${item.quantity}">-</button>
                    <button class="increment" data-id="${item.id}" data-quantity="${item.quantity}">+</button>
                    <button class="remove" data-id="${item.id}">Eliminar</button>
                `;
                cartItemsContainer.appendChild(cartItem);
            });
    
            const cartTotal = document.createElement('div');
            cartTotal.className = 'cart-total';
            cartTotal.innerHTML = `<p>Total del Carrito: $${cart.total}</p>`;
            cartItemsContainer.appendChild(cartTotal);
    
            document.getElementById('clearCartButton').onclick = () => clearCart(cart.id);
            document.getElementById('checkoutButton').onclick = () => {
                if (cart.items.length === 0) {
                    alert('El carrito está vacío. No puedes finalizar la compra.');
                } else if (confirm('¿Estás seguro de que deseas finalizar la compra?')) {
                    checkoutCart(cart.id);
                }
            };
    
            modal.style.display = 'block';
    
            // Event listeners for increment, decrement, and remove buttons
            document.querySelectorAll('.increment').forEach(button => {
                button.addEventListener('click', () => {
                    const productId = parseInt(button.dataset.id); // Asegúrate de que productId sea un número
                    const product = products.find(p => p.id === productId);
                    if (product) {
                        const newQuantity = parseInt(button.dataset.quantity) + 1;
                        if (newQuantity <= product.stock) {
                            updateCartItemQuantity(productId, newQuantity);
                            button.dataset.quantity = newQuantity; // Actualiza el atributo data-quantity
                            if (newQuantity === product.stock) {
                                button.disabled = true; // Deshabilita el botón si se alcanza el stock
                            }
                        }
                    } else {
                        console.error('Producto no encontrado:', productId);
                    }
                });
            });
    
            document.querySelectorAll('.decrement').forEach(button => {
                button.addEventListener('click', () => {
                    const newQuantity = parseInt(button.dataset.quantity) - 1;
                    if (newQuantity > 0) {
                        updateCartItemQuantity(parseInt(button.dataset.id), newQuantity);
                        button.dataset.quantity = newQuantity; // Actualiza el atributo data-quantity
                        const incrementButton = button.nextElementSibling;
                        incrementButton.disabled = false; // Habilita el botón de incremento
                    } else {
                        removeCartItem(parseInt(button.dataset.id));
                    }
                });
            });
    
            document.querySelectorAll('.remove').forEach(button => {
                button.addEventListener('click', () => removeCartItem(parseInt(button.dataset.id)));
            });
    
        } catch (error) {
            console.error('Error al obtener detalles del carrito:', error);
        }
    }
    
    // Función para actualizar la cantidad de un item en el carrito
    async function updateCartItemQuantity(productId, quantity) {
        try {
            const response = await fetch(`${apiUrl}/users/cart/update`, {
                method: 'PUT',
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
                throw new Error('Error al actualizar la cantidad del item');
            }
    
            showCartDetails(); // Refresh cart details
        } catch (error) {
            console.error('Error al actualizar la cantidad del item:', error);
        }
    }
    
    // Función para eliminar un item del carrito
    async function removeCartItem(productId) {
        try {
            const response = await fetch(`${apiUrl}/users/cart/remove`, {
                method: 'DELETE',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    userId: userId,
                    productId: productId
                })
            });
    
            if (!response.ok) {
                throw new Error('Error al eliminar el item del carrito');
            }
    
            showCartDetails(); // Refresh cart details
        } catch (error) {
            console.error('Error al eliminar el item del carrito:', error);
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

            document.getElementById('cartModal').style.display = 'none';
        } catch (error) {
            console.error('Error al finalizar la compra:', error);
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

            document.getElementById('cartModal').style.display = 'none';
        } catch (error) {
            console.error('Error al limpiar el carrito:', error);
        }
    }

    // Función para obtener el ID del usuario desde el token JWT
    function getUserIdFromToken(token) {
        const payload = JSON.parse(atob(token.split('.')[1]));
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
    fetchCategories();

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