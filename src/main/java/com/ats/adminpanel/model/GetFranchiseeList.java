package com.ats.adminpanel.model;

public class GetFranchiseeList {

	private int id;
	private int frId;
	private String frName;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getFrId() {
		return frId;
	}

	public void setFrId(int frId) {
		this.frId = frId;
	}

	public String getFrName() {
		return frName;
	}

	public void setFrName(String frName) {
		this.frName = frName;
	}

	@Override
	public String toString() {
		return "GetFranchiseeList [id=" + id + ", frId=" + frId + ", frName=" + frName + "]";
	}

}
