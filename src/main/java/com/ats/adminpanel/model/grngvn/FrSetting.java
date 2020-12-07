package com.ats.adminpanel.model.grngvn;

public class FrSetting {

	private int frSettingId;

	private int frId;

	private String frCode;

	private int sellBillNo;

	private int grnGvnNo;

	private int spNo;

	private int count;
	private int posCr;
	private String exVarchar;

	public int getSpNo() {
		return spNo;
	}

	public void setSpNo(int spNo) {
		this.spNo = spNo;
	}

	public int getFrSettingId() {
		return frSettingId;
	}

	public void setFrSettingId(int frSettingId) {
		this.frSettingId = frSettingId;
	}

	public int getFrId() {
		return frId;
	}

	public void setFrId(int frId) {
		this.frId = frId;
	}

	public String getFrCode() {
		return frCode;
	}

	public void setFrCode(String frCode) {
		this.frCode = frCode;
	}

	public int getSellBillNo() {
		return sellBillNo;
	}

	public void setSellBillNo(int sellBillNo) {
		this.sellBillNo = sellBillNo;
	}

	public int getGrnGvnNo() {
		return grnGvnNo;
	}

	public void setGrnGvnNo(int grnGvnNo) {
		this.grnGvnNo = grnGvnNo;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getExVarchar() {
		return exVarchar;
	}

	public void setExVarchar(String exVarchar) {
		this.exVarchar = exVarchar;
	}

	public int getPosCr() {
		return posCr;
	}

	public void setPosCr(int posCr) {
		this.posCr = posCr;
	}

	@Override
	public String toString() {
		return "FrSetting [frSettingId=" + frSettingId + ", frId=" + frId + ", frCode=" + frCode + ", sellBillNo="
				+ sellBillNo + ", grnGvnNo=" + grnGvnNo + ", spNo=" + spNo + ", count=" + count + ", posCr=" + posCr
				+ ", exVarchar=" + exVarchar + "]";
	}

}
