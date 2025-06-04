const BASE_URL = 'https://localhost:8080/ecommerce/api';

export const api = {
  // User endpoints
  addUser: (userData) => fetch(`${BASE_URL}/user/add`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(userData)
  }),
  
  getUser: (id) => fetch(`${BASE_URL}/user/${id}`),
  
  updateUser: (id, userData) => fetch(`${BASE_URL}/user/${id}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(userData)
  }),
  
  deleteUser: (id) => fetch(`${BASE_URL}/user/${id}`, { method: 'DELETE' }),

  // Product endpoints
  getProducts: () => fetch(`${BASE_URL}/product`),
  
  getProductsByCategory: (categoryId) => fetch(`${BASE_URL}/product/category/${categoryId}`),
  
  addProduct: (productData) => fetch(`${BASE_URL}/product`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(productData)
  }),
  
  getProduct: (id) => fetch(`${BASE_URL}/product/${id}`),
  
  deleteProduct: (id) => fetch(`${BASE_URL}/product/${id}`, { method: 'DELETE' }),

  // Category endpoints
  getCategories: () => fetch(`${BASE_URL}/categories`),
  
  addCategory: (categoryData) => fetch(`${BASE_URL}/categories`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(categoryData)
  }),
  
  updateCategory: (id, categoryData) => fetch(`${BASE_URL}/categories/${id}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(categoryData)
  }),
  
  deleteCategory: (id) => fetch(`${BASE_URL}/categories/${id}`, { method: 'DELETE' }),

  // Cart endpoints
  addToCart: (cartData) => fetch(`${BASE_URL}/cart/add`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(cartData)
  }),
  
  getCart: (userId) => fetch(`${BASE_URL}/cart/${userId}`),
  
  removeFromCart: (productId) => fetch(`${BASE_URL}/cart/${productId}`, { method: 'DELETE' }),
  
  getCartTotal: (userId) => fetch(`${BASE_URL}/cart/${userId}/total`),

  // Auth endpoints
  login: (credentials) => fetch('https://localhost:8080/ecommerce/login', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(credentials)
  }),
  
  signup: (userData) => fetch('https://localhost:8080/ecommerce/api/signup', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(userData)
  })
};