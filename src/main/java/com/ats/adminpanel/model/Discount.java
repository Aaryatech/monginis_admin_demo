package com.ats.adminpanel.model;

public class Discount {

	private int discId;
	
	private String franchId;
	
	private String itemId;

	private float discPer;
	
	private int isActive;
	
	private int categoryId;
	
	private int delStatus;
	
	private int subCategoryId;
	private String var1;

	private String var2;

	private String var3;
	
	private int int1;
	
	private int int2;
	
	private int int3;
	public int getDiscId() {
		return discId;
	}
	public void setDiscId(int discId) {
		this.discId = discId;
	}
	public String getFranchId() {
		return franchId;
	}
	public void setFranchId(String franchId) {
		this.franchId = franchId;
	}
	
	public float getDiscPer() {
		return discPer;
	}
	public void setDiscPer(float discPer) {
		this.discPer = discPer;
	}
	public int getIsActive() {
		return isActive;
	}
	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}
	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	public int getDelStatus() {
		return delStatus;
	}
	public void setDelStatus(int delStatus) {
		this.delStatus = delStatus;
	}
	
	public String getVar1() {
		return var1;
	}
	public void setVar1(String var1) {
		this.var1 = var1;
	}
	public String getVar2() {
		return var2;
	}
	public void setVar2(String var2) {
		this.var2 = var2;
	}
	public String getVar3() {
		return var3;
	}
	public void setVar3(String var3) {
		this.var3 = var3;
	}
	public int getInt1() {
		return int1;
	}
	public void setInt1(int int1) {
		this.int1 = int1;
	}
	public int getInt2() {
		return int2;
	}
	public void setInt2(int int2) {
		this.int2 = int2;
	}
	public int getInt3() {
		return int3;
	}
	public void setInt3(int int3) {
		this.int3 = int3;
	}
	public int getSubCategoryId() {
		return subCategoryId;
	}
	public void setSubCategoryId(int subCategoryId) {
		this.subCategoryId = subCategoryId;
	}
	
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	@Override
	public String toString() {
		return "Discount [discId=" + discId + ", franchId=" + franchId + ", itemId=" + itemId + ", discPer=" + discPer
				+ ", isActive=" + isActive + ", categoryId=" + categoryId + ", delStatus=" + delStatus
				+ ", subCategoryId=" + subCategoryId + ", var1=" + var1 + ", var2=" + var2 + ", var3=" + var3
				+ ", int1=" + int1 + ", int2=" + int2 + ", int3=" + int3 + "]";
	}
	
	
}
