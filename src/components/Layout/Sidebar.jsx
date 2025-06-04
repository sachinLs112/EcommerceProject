import React from 'react';

const Sidebar = ({ categories, selectedCategory, onSelectCategory }) => {
  return (
    <aside className="sidebar">
      <h3>Categories</h3>
      <ul>
        <li 
          className={!selectedCategory ? 'active' : ''}
          onClick={() => onSelectCategory(null)}
        >
          All Products
        </li>
        {categories.map(category => (
          <li 
            key={category.id}
            className={selectedCategory === category.id ? 'active' : ''}
            onClick={() => onSelectCategory(category.id)}
          >
            {category.name}
          </li>
        ))}
      </ul>
    </aside>
  );
};

export default Sidebar;