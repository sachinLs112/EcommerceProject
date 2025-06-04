package com.ecom.ecommerce.api.v1.model;

import java.util.Map;

public class Product {
	private int id;
	private String name;
	private String description;
	private String imageUrl;
	private long price;
	private long stock;
	private int categoryId;

	public Product() {}

	public Product(int id, String name, String description, String imageUrl, long price, int categoryId) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.imageUrl = imageUrl;
		this.price = price;
		this.categoryId = categoryId;
	}

	public Product(Map<String, Object> product) {
		this.name = (String) product.getOrDefault("name", null);
		this.description = (String) product.getOrDefault("description", null);
		this.imageUrl = (String) product.getOrDefault("imageUrl", null);
		this.price = (long) product.getOrDefault("price", 0);
		this.stock = (long) product.getOrDefault("stock", null);
		this.categoryId = (int) product.getOrDefault("categoryId", 0);
	}

	public long getStock() {
		return stock;
	}

	public void setStock(long stock) {
		this.stock = stock;
	}

	// Getters and Setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public long getPrice() {
		return price;
	}

	public void setPrice(long price) {
		this.price = price;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
}
