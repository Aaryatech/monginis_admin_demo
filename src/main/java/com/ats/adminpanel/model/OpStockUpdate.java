package com.ats.adminpanel.model;

import java.util.Date;

public class OpStockUpdate {

	private boolean error;

	private String message;

	private int opStockId;

	private int itemId;

	private int catId;

	private int subCatId;

	private int delStatus;

	private float oldQty;

	private float newQty;

	private String reason;

	private String date;

	private String itemName;

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getOpStockId() {
		return opStockId;
	}

	public void setOpStockId(int opStockId) {
		this.opStockId = opStockId;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public int getCatId() {
		return catId;
	}

	public void setCatId(int catId) {
		this.catId = catId;
	}

	public int getSubCatId() {
		return subCatId;
	}

	public void setSubCatId(int subCatId) {
		this.subCatId = subCatId;
	}

	public int getDelStatus() {
		return delStatus;
	}

	public void setDelStatus(int delStatus) {
		this.delStatus = delStatus;
	}

	public float getOldQty() {
		return oldQty;
	}

	public void setOldQty(float oldQty) {
		this.oldQty = oldQty;
	}

	public float getNewQty() {
		return newQty;
	}

	public void setNewQty(float newQty) {
		this.newQty = newQty;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	@Override
	public String toString() {
		return "OpStockUpdate [error=" + error + ", message=" + message + ", opStockId=" + opStockId + ", itemId="
				+ itemId + ", catId=" + catId + ", subCatId=" + subCatId + ", delStatus=" + delStatus + ", oldQty="
				+ oldQty + ", newQty=" + newQty + ", reason=" + reason + ", date=" + date + ", itemName=" + itemName
				+ "]";
	}

}
