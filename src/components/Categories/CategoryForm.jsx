import React, { useState } from 'react';

const CategoryForm = ({ onSubmit, onClose }) => {
  const [categoryData, setCategoryData] = useState({
    name: '',
    description: ''
  });

  const handleSubmit = (e) => {
    e.preventDefault();
    onSubmit(categoryData);
  };

  return (
    <div className="modal-overlay">
      <div className="modal">
        <h2>Add New Category</h2>
        <form onSubmit={handleSubmit}>
          <div>
            <label>Name:</label>
            <input 
              type="text" 
              value={categoryData.name}
              onChange={(e) => setCategoryData({...categoryData, name: e.target.value})}
              required
            />
          </div>
          <div>
            <label>Description:</label>
            <textarea 
              value={categoryData.description}
              onChange={(e) => setCategoryData({...categoryData, description: e.target.value})}
              required
            />
          </div>
          <div className="form-actions">
            <button type="button" onClick={onClose}>Cancel</button>
            <button type="submit">Add Category</button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default CategoryForm;