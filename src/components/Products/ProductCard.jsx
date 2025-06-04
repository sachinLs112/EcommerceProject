import React, { useState } from 'react';
import { api } from '../../services/api';

const ProductCard = ({ product }) => {
  const [quantity, setQuantity] = useState(1);

  const handleAddToCart = async () => {
    try {
      const response = await api.addToCart({
        productId: product.id,
        quantity: quantity
      });
      
      if (response.ok) {
        alert('Product added to cart');
      } else {
        alert('Failed to add product to cart');
      }
    } catch (err) {
      console.error('Error adding to cart:', err);
    }
  };

  return (
    <div className="product-card">
      <img src={product.imageUrl || 'https://via.placeholder.com/150'} alt={product.name} />
      <h3>{product.name}</h3>
      <p>{product.description}</p>
      <p className="price">₹{product.price}</p>
      
      <div className="quantity-control">
        <button onClick={() => setQuantity(Math.max(1, quantity - 1))}>-</button>
        <span>{quantity}</span>
        <button onClick={() => setQuantity(quantity + 1)}>+</button>
      </div>
      
      <button onClick={handleAddToCart}>Add to Cart</button>
    </div>
  );
};

export default ProductCard;