package com.ecom.ecommerce.api.v1.model;

import java.util.Map;

public class CartItem {
    private int id;
    private int userId;
    private int productId;
    private int quantity;
    private Long totalPrice;
    public CartItem(){
		super();
	}
    public CartItem(Map<String, Object> cartItems) {
		super();
		this.userId = (int) cartItems.getOrDefault("userId", null);
		this.productId = (int) cartItems.getOrDefault("productId", null);
		this.quantity = (int) cartItems.getOrDefault("quantity", null);
		this.totalPrice = (Long) cartItems.getOrDefault("totalPrice", null);
	}
    
	public Long getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(Long totalPrice) {
		this.totalPrice = totalPrice;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

   
}
