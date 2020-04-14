package com.ats.adminpanel.model;
 

public class RouteMaster {
	 
	private int routeId; 
	private String routeName; 
    private int delStatus; 
    private int abcType; 
    private int seqNo;
	public int getRouteId() {
		return routeId;
	}
	public void setRouteId(int routeId) {
		this.routeId = routeId;
	}
	public String getRouteName() {
		return routeName;
	}
	public void setRouteName(String routeName) {
		this.routeName = routeName;
	}
	public int getDelStatus() {
		return delStatus;
	}
	public void setDelStatus(int delStatus) {
		this.delStatus = delStatus;
	}
	public int getAbcType() {
		return abcType;
	}
	public void setAbcType(int abcType) {
		this.abcType = abcType;
	}
	public int getSeqNo() {
		return seqNo;
	}
	public void setSeqNo(int seqNo) {
		this.seqNo = seqNo;
	}
	@Override
	public String toString() {
		return "RouteMaster [routeId=" + routeId + ", routeName=" + routeName + ", delStatus=" + delStatus
				+ ", abcType=" + abcType + ", seqNo=" + seqNo + "]";
	}
    
    

}
