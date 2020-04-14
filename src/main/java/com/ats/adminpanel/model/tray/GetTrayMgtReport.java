package com.ats.adminpanel.model.tray;

public class GetTrayMgtReport {

	private int frId;

	private int outtrayBig;

	private int outtraySmall;

	private int outtrayLead;

	private String outtrayDate;

	private int intrayBig;

	private int intraySmall;

	private int intrayLead;

	private int balanceBig;

	private int balanceSmall;

	private int balanceLead;

	private String frName;
	private String frCode;

	public int getFrId() {
		return frId;
	}

	public void setFrId(int frId) {
		this.frId = frId;
	}

	public int getOuttrayBig() {
		return outtrayBig;
	}

	public void setOuttrayBig(int outtrayBig) {
		this.outtrayBig = outtrayBig;
	}

	public int getOuttraySmall() {
		return outtraySmall;
	}

	public void setOuttraySmall(int outtraySmall) {
		this.outtraySmall = outtraySmall;
	}

	public int getOuttrayLead() {
		return outtrayLead;
	}

	public void setOuttrayLead(int outtrayLead) {
		this.outtrayLead = outtrayLead;
	}

	public String getOuttrayDate() {
		return outtrayDate;
	}

	public void setOuttrayDate(String outtrayDate) {
		this.outtrayDate = outtrayDate;
	}

	public int getIntrayBig() {
		return intrayBig;
	}

	public void setIntrayBig(int intrayBig) {
		this.intrayBig = intrayBig;
	}

	public int getIntraySmall() {
		return intraySmall;
	}

	public void setIntraySmall(int intraySmall) {
		this.intraySmall = intraySmall;
	}

	public int getIntrayLead() {
		return intrayLead;
	}

	public void setIntrayLead(int intrayLead) {
		this.intrayLead = intrayLead;
	}

	public int getBalanceBig() {
		return balanceBig;
	}

	public void setBalanceBig(int balanceBig) {
		this.balanceBig = balanceBig;
	}

	public int getBalanceSmall() {
		return balanceSmall;
	}

	public void setBalanceSmall(int balanceSmall) {
		this.balanceSmall = balanceSmall;
	}

	public int getBalanceLead() {
		return balanceLead;
	}

	public void setBalanceLead(int balanceLead) {
		this.balanceLead = balanceLead;
	}

	public String getFrName() {
		return frName;
	}

	public void setFrName(String frName) {
		this.frName = frName;
	}

	public String getFrCode() {
		return frCode;
	}

	public void setFrCode(String frCode) {
		this.frCode = frCode;
	}

	@Override
	public String toString() {
		return "GetTrayMgtReport [frId=" + frId + ", outtrayBig=" + outtrayBig + ", outtraySmall=" + outtraySmall
				+ ", outtrayLead=" + outtrayLead + ", outtrayDate=" + outtrayDate + ", intrayBig=" + intrayBig
				+ ", intraySmall=" + intraySmall + ", intrayLead=" + intrayLead + ", balanceBig=" + balanceBig
				+ ", balanceSmall=" + balanceSmall + ", balanceLead=" + balanceLead + ", frName=" + frName + ", frCode="
				+ frCode + "]";
	}

}
