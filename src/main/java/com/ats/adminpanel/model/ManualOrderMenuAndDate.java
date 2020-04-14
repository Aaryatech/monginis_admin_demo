package com.ats.adminpanel.model;

import java.util.List;

public class ManualOrderMenuAndDate {
	
	List<Orders> orders;
	String prodDate;
	String deliveryDate;
	public List<Orders> getOrders() {
		return orders;
	}
	public void setOrders(List<Orders> orders) {
		this.orders = orders;
	}
	public String getProdDate() {
		return prodDate;
	}
	public void setProdDate(String prodDate) {
		this.prodDate = prodDate;
	}
	public String getDeliveryDate() {
		return deliveryDate;
	}
	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	@Override
	public String toString() {
		return "ManualOrderMenuAndDate [orders=" + orders + ", prodDate=" + prodDate + ", deliveryDate=" + deliveryDate
				+ "]";
	}
	
	
	

}
