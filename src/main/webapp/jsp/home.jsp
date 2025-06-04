<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>E-commerce Home</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            display: flex;
        }

        .sidebar {
            width: 200px;
            background: #f4f4f4;
            padding: 20px;
            height: 100vh;
        }

        .main {
            flex: 1;
            padding: 20px;
        }

        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
        }

        .product-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
            gap: 15px;
        }

        .product-card {
            border: 1px solid #ccc;
            padding: 10px;
            border-radius: 4px;
            background-color: #fff;
        }

        .category {
            cursor: pointer;
            padding: 5px 0;
        }

        .category:hover {
            text-decoration: underline;
        }

        .modal {
            display: none;
            position: fixed;
            z-index: 999;
            padding-top: 80px;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            overflow: auto;
            background-color: rgba(0,0,0,0.4);
        }

        .modal-content {
            background-color: #fefefe;
            margin: auto;
            padding: 20px;
            border-radius: 10px;
            border: 1px solid #888;
            width: 400px;
            max-width: 90%;
            position: relative;
        }

        .close {
            color: #aaa;
            float: right;
            font-size: 28px;
            font-weight: bold;
            position: absolute;
            right: 20px;
            top: 10px;
            cursor: pointer;
        }

        .modal-content h2 {
            margin-top: 0;
        }

        .modal-content input, .modal-content select {
            width: 100%;
            padding: 10px;
            margin: 8px 0;
            box-sizing: border-box;
        }

        .modal-content button {
            padding: 10px 20px;
            width: 100%;
            background-color: #4CAF50;
            color: white;
            border: none;
            border-radius: 6px;
            cursor: pointer;
        }
    </style>
</head>
<body>
<div class="sidebar">
    <h3>Categories</h3>
    <div id="categories"></div>
</div>

<div class="main">
    <div class="header">
        <h2>Products</h2>
        <div id="adminControls" style="text-align: right; padding: 10px;">
            <button onclick="openModal('categoryModal')">Add Category</button>
            <button onclick="loadCategoriesForProductForm(); openModal('productModal')">Add Product</button>
        </div>
    </div>
    <div class="product-grid" id="productGrid"></div>
</div>

<!-- Add Category Modal -->
<div id="categoryModal" class="modal">
    <div class="modal-content">
        <span class="close" onclick="closeModal('categoryModal')">&times;</span>
        <h2>Add Category</h2>
        <form id="addCategoryForm">
            <input type="text" placeholder="Category Name" name="category_name" required />
            <button type="submit">Add Category</button>
        </form>
    </div>
</div>

<!-- Add Product Modal -->
<div id="productModal" class="modal">
    <div class="modal-content">
        <span class="close" onclick="closeModal('productModal')">&times;</span>
        <h2>Add Product</h2>
        <form id="addProductForm">
            <input type="text" placeholder="Product Name" name="name" required />
            <input type="text" placeholder="Description" name="description" required />
            <input type="number" placeholder="Price" name="price" step="0.01" required />
            <input type="number" placeholder="Stock" name="stock" required />
            <input type="text" placeholder="Image URL" name="imageUrl" required />
            <select name="categoryId" required id="productCategorySelect"></select>
            <button type="submit">Add Product</button>
        </form>
    </div>
</div>

<script>

    document.addEventListener("DOMContentLoaded", function () {
        const userRole = 'admin'; // update based on actual session/user

        if (userRole === 'admin') {
            document.getElementById('adminControls').style.display = 'block';
        }

        function fetchCategories() {
            fetch(`/ecommerce/api/categories`)
                .then(res => res.json())
                .then(data => {
                    const container = document.getElementById('categories');
                    container.innerHTML = '';

                    // Add "All Products" option
                    const allDiv = document.createElement('div');
                    allDiv.className = 'category';
                    allDiv.textContent = 'All Products';
                    allDiv.onclick = () => fetchProducts();
                    container.appendChild(allDiv);

                    // Add each actual category
                    data.forEach(cat => {
                        const div = document.createElement('div');
                        div.className = 'category';
                        div.textContent = cat.name;
                        div.onclick = () => fetchProducts(cat.id);
                        container.appendChild(div);
                    });

                    // Populate category select in the form
                    const select = document.getElementById('productCategorySelect');
                    if (select) {
                        select.innerHTML = '';
                        data.forEach(cat => {
                            const opt = document.createElement('option');
                            opt.value = cat.id;
                            opt.textContent = cat.name;
                            select.appendChild(opt);
                        });
                    }
                })
                .catch(err => console.error("Error fetching categories:", err));
        }


        function fetchProducts(categoryId = null) {
            let url = `/ecommerce/api/products`;
            if (categoryId !== null) url += `/category/${categoryId}`;

            fetch(url)
                .then(res => res.json())
                .then(products => {
                    const grid = document.getElementById('productGrid');
                    grid.innerHTML = '';
                    products.forEach(p => {
                        const card = document.createElement('div');
                        card.className = 'product-card';

                        console.log("Rendering product:", p); // Debug line

                        const image = document.createElement('img');
                        image.src = p.imageUrl;
                        image.alt = p.name;
                        image.style.width = '100%';
                        image.style.height = '150px';
                        image.style.objectFit = 'cover';
                        image.style.borderRadius = '4px';

                        const title = document.createElement('h4');
                        title.textContent = p.name;

                        const desc = document.createElement('p');
                        desc.textContent = p.description;

                        const price = document.createElement('p');
                        price.textContent = `Price: ₹${p.price}`;

                        card.appendChild(image);
                        card.appendChild(title);
                        card.appendChild(desc);
                        card.appendChild(price);

                        grid.appendChild(card);
                    });


                })
                .catch(err => console.error("Error fetching products:", err));
        }


        function openModal(id) {
            document.getElementById(id).style.display = 'block';
        }

        function closeModal(id) {
            document.getElementById(id).style.display = 'none';
        }

        window.openModal = openModal;
        window.closeModal = closeModal;
        window.loadCategoriesForProductForm = fetchCategories;

        document.getElementById('addCategoryForm').addEventListener('submit', function (e) {
            e.preventDefault();
            const formData = new FormData(this);
            const json = {};
            formData.forEach((v, k) => json[k] = v);

            fetch(`/ecommerce/api/categories`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(json)
            })
                .then(res => {
                    if (res.ok) {
                        alert("Category added successfully");
                        closeModal('categoryModal');
                        fetchCategories();
                    } else {
                        alert("Failed to add category");
                    }
                })
                .catch(err => {
                    console.error("Error adding category", err);
                    alert("Error adding category");
                });
        });

        document.getElementById('addProductForm').addEventListener('submit', function (e) {
            e.preventDefault();
            const formData = new FormData(this);
            const json = {};
            formData.forEach((v, k) => {
                json[k] = ["price", "stock", "categoryId"].includes(k) ? Number(v) : v;
            });

            fetch(`/ecommerce/api/products`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(json)
            })
                .then(res => {
                    if (res.ok) {
                        alert("Product added successfully");
                        closeModal('productModal');
                        fetchProducts();
                    } else {
                        alert("Failed to add product");
                    }
                })
                .catch(err => {
                    console.error("Error adding product", err);
                    alert("Error adding product");
                });
        });

        fetchCategories();
        fetchProducts();
    });
</script>

</body>
</html>
