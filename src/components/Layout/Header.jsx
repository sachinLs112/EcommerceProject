import React from 'react';

const Header = ({ userRole, onAddProduct, onAddCategory, onLogout }) => {
  return (
    <header className="app-header">
      <div className="logo">E-Commerce App</div>
      
      <div className="header-actions">
        {userRole === 'admin' && (
          <>
            <button onClick={onAddProduct}>Add Product</button>
            <button onClick={onAddCategory}>Add Category</button>
          </>
        )}
        <button onClick={onLogout}>Logout</button>
      </div>
    </header>
  );
};

export default Header;