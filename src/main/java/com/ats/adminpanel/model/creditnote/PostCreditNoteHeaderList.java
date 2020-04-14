package com.ats.adminpanel.model.creditnote;

import java.util.List;

public class PostCreditNoteHeaderList {
	
	List<PostCreditNoteHeader> postCreditNoteHeader;

	public List<PostCreditNoteHeader> getPostCreditNoteHeader() {
		return postCreditNoteHeader;
	}

	public void setPostCreditNoteHeader(List<PostCreditNoteHeader> postCreditNoteHeader) {
		this.postCreditNoteHeader = postCreditNoteHeader;
	}

	@Override
	public String toString() {
		return "PostCreditNoteHeaderList [postCreditNoteHeader=" + postCreditNoteHeader + "]";
	}
	
	
	

}
