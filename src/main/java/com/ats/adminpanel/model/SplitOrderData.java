package com.ats.adminpanel.model;

import java.sql.Date;

public class SplitOrderData {
	
	private int orderId;
	
	private int orderQty;
	
	private int menuId;
	
	private Date productionDate;
	
	private Date deliveryDate;

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public int getOrderQty() {
		return orderQty;
	}

	public void setOrderQty(int orderQty) {
		this.orderQty = orderQty;
	}

	public int getMenuId() {
		return menuId;
	}

	public void setMenuId(int menuId) {
		this.menuId = menuId;
	}

	public Date getProductionDate() {
		return productionDate;
	}

	public void setProductionDate(Date productionDate) {
		this.productionDate = productionDate;
	}

	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	@Override
	public String toString() {
		return "SplitOrderData [orderId=" + orderId + ", orderQty=" + orderQty + ", menuId=" + menuId
				+ ", productionDate=" + productionDate + ", deliveryDate=" + deliveryDate + "]";
	}
	
	

}
