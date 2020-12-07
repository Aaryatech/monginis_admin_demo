
package com.ats.adminpanel.model.item;


public class SubCategory {

	  private int subCatId;
	    private int catId;
	    private String subCatName;
	    private int delStatus;
		private int seqNo;

		public int getSeqNo() {
			return seqNo;
		}
		public void setSeqNo(int seqNo) {
			this.seqNo = seqNo;
		}
		public int getSubCatId() {
			return subCatId;
		}
		public void setSubCatId(int subCatId) {
			this.subCatId = subCatId;
		}
		public int getCatId() {
			return catId;
		}
		public void setCatId(int catId) {
			this.catId = catId;
		}
		public String getSubCatName() {
			return subCatName;
		}
		public void setSubCatName(String subCatName) {
			this.subCatName = subCatName;
		}
		public int getDelStatus() {
			return delStatus;
		}
		public void setDelStatus(int delStatus) {
			this.delStatus = delStatus;
		}
		@Override
		public String toString() {
			return "SubCategory [subCatId=" + subCatId + ", catId=" + catId + ", subCatName=" + subCatName + ", delStatus="
					+ delStatus + ", seqNo=" + seqNo + "]";
		}
    

}
