import React, { useState } from 'react';

const ProductForm = ({ categories, onSubmit, onClose }) => {
  const [productData, setProductData] = useState({
    name: '',
    description: '',
    price: 0,
    stock: 0,
    categoryId: categories.length > 0 ? categories[0].id : '',
    imageUrl: ''
  });

  const handleSubmit = (e) => {
    e.preventDefault();
    onSubmit(productData);
  };

  return (
    <div className="modal-overlay">
      <div className="modal">
        <h2>Add New Product</h2>
        <form onSubmit={handleSubmit}>
          <div>
            <label>Name:</label>
            <input 
              type="text" 
              value={productData.name}
              onChange={(e) => setProductData({...productData, name: e.target.value})}
              required
            />
          </div>
          <div>
            <label>Description:</label>
            <textarea 
              value={productData.description}
              onChange={(e) => setProductData({...productData, description: e.target.value})}
              required
            />
          </div>
          <div>
            <label>Price:</label>
            <input 
              type="number" 
              min="0" 
              step="0.01"
              value={productData.price}
              onChange={(e) => setProductData({...productData, price: parseFloat(e.target.value)})}
              required
            />
          </div>
          <div>
            <label>Stock:</label>
            <input 
              type="number" 
              min="0"
              value={productData.stock}
              onChange={(e) => setProductData({...productData, stock: parseInt(e.target.value)})}
              required
            />
          </div>
          <div>
            <label>Category:</label>
            <select 
              value={productData.categoryId}
              onChange={(e) => setProductData({...productData, categoryId: e.target.value})}
              required
            >
              {categories.map(category => (
                <option key={category.id} value={category.id}>{category.name}</option>
              ))}
            </select>
          </div>
          <div>
            <label>Image URL:</label>
            <input 
              type="url" 
              value={productData.imageUrl}
              onChange={(e) => setProductData({...productData, imageUrl: e.target.value})}
            />
          </div>
          <div className="form-actions">
            <button type="button" onClick={onClose}>Cancel</button>
            <button type="submit">Add Product</button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default ProductForm;