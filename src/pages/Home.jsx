import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import Sidebar from '../components/Layout/Sidebar';
import Header from '../components/Layout/Header';
import ProductList from '../components/Products/ProductList';
import ProductForm from '../components/Products/ProductForm';
import CategoryForm from '../components/Categories/CategoryForm';
import { api } from '../services/api';

const Home = ({ userRole }) => {
  const [products, setProducts] = useState([]);
  const [categories, setCategories] = useState([]);
  const [selectedCategory, setSelectedCategory] = useState(null);
  const [showProductForm, setShowProductForm] = useState(false);
  const [showCategoryForm, setShowCategoryForm] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    fetchCategories();
    fetchProducts();
  }, []);

  const fetchProducts = async (categoryId = null) => {
    try {
      const response = categoryId 
        ? await api.getProductsByCategory(categoryId)
        : await api.getProducts();
      const data = await response.json();
      setProducts(data);
      setSelectedCategory(categoryId);
    } catch (err) {
      console.error('Error fetching products:', err);
    }
  };

  const fetchCategories = async () => {
    try {
      const response = await api.getCategories();
      const data = await response.json();
      setCategories(data);
    } catch (err) {
      console.error('Error fetching categories:', err);
    }
  };

  const handleAddProduct = async (productData) => {
    try {
      const response = await api.addProduct(productData);
      if (response.ok) {
        fetchProducts(selectedCategory);
        setShowProductForm(false);
      }
    } catch (err) {
      console.error('Error adding product:', err);
    }
  };

  const handleAddCategory = async (categoryData) => {
    try {
      const response = await api.addCategory(categoryData);
      if (response.ok) {
        fetchCategories();
        setShowCategoryForm(false);
      }
    } catch (err) {
      console.error('Error adding category:', err);
    }
  };

  const handleLogout = () => {
    localStorage.removeItem('token');
    navigate('/login');
  };

  return (
    <div className="home-container">
      <Header 
        userRole={userRole} 
        onAddProduct={() => setShowProductForm(true)} 
        onAddCategory={() => setShowCategoryForm(true)}
        onLogout={handleLogout}
      />
      
      <div className="main-content">
        <Sidebar 
          categories={categories} 
          selectedCategory={selectedCategory}
          onSelectCategory={fetchProducts}
        />
        
        <div className="product-section">
          <ProductList products={products} />
        </div>
      </div>

      {showProductForm && (
        <ProductForm 
          categories={categories}
          onSubmit={handleAddProduct}
          onClose={() => setShowProductForm(false)}
        />
      )}

      {showCategoryForm && (
        <CategoryForm 
          onSubmit={handleAddCategory}
          onClose={() => setShowCategoryForm(false)}
        />
      )}
    </div>
  );
};

export default Home;