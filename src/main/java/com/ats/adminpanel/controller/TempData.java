package com.ats.adminpanel.controller;

import java.util.List;

import com.ats.adminpanel.model.flavours.Flavour;
import com.ats.adminpanel.model.manspbill.SpecialCake;

public class TempData {

	float sprRate;
	float spBackendRate;
	
	List<Flavour> filterFlavoursList;
	
	List<Float> weightList;
	
	int billBy;
	
	String spNo;
	int spId;
	SpecialCake specialCake;
	List<TempSpOrder> tempSpOrdList;

	public List<TempSpOrder> getTempSpOrdList() {
		return tempSpOrdList;
	}

	public void setTempSpOrdList(List<TempSpOrder> tempSpOrdList) {
		this.tempSpOrdList = tempSpOrdList;
	}

	public SpecialCake getSpecialCake() {
		return specialCake;
	}

	public void setSpecialCake(SpecialCake specialCake) {
		this.specialCake = specialCake;
	}

	public int getSpId() {
		return spId;
	}

	public void setSpId(int spId) {
		this.spId = spId;
	}

	public float getSprRate() {
		return sprRate;
	}

	public void setSprRate(float sprRate) {
		this.sprRate = sprRate;
	}

	public float getSpBackendRate() {
		return spBackendRate;
	}

	public void setSpBackendRate(float spBackendRate) {
		this.spBackendRate = spBackendRate;
	}

	public List<Flavour> getFilterFlavoursList() {
		return filterFlavoursList;
	}

	public void setFilterFlavoursList(List<Flavour> filterFlavoursList) {
		this.filterFlavoursList = filterFlavoursList;
	}

	public List<Float> getWeightList() {
		return weightList;
	}

	public void setWeightList(List<Float> weightList) {
		this.weightList = weightList;
	}

	public int getBillBy() {
		return billBy;
	}

	public void setBillBy(int billBy) {
		this.billBy = billBy;
	}

	public String getSpNo() {
		return spNo;
	}

	public void setSpNo(String spNo) {
		this.spNo = spNo;
	}

	@Override
	public String toString() {
		return "TempData [sprRate=" + sprRate + ", spBackendRate=" + spBackendRate + ", filterFlavoursList="
				+ filterFlavoursList + ", weightList=" + weightList + ", billBy=" + billBy + ", spNo=" + spNo
				+ ", spId=" + spId + ", specialCake=" + specialCake + ", tempSpOrdList=" + tempSpOrdList + "]";
	}
	
}
