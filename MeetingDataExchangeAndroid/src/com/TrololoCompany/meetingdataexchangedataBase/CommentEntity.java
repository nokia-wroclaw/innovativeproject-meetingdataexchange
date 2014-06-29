package com.TrololoCompany.meetingdataexchangedataBase;

public class CommentEntity 
{
	
	private long ID;
	private long fileID;
	private long serverCommentID;
	private String authorName;
	private String addTime;
	private String content;
	public long getID() {
		return ID;
	}
	public void setID(long iD) {
		ID = iD;
	}
	public long getFileID() {
		return fileID;
	}
	public void setFileID(long fileID) {
		this.fileID = fileID;
	}
	public long getServerCommentID() {
		return serverCommentID;
	}
	public void setServerCommentID(long serverCommentID) {
		this.serverCommentID = serverCommentID;
	}
	public String getAuthorName() {
		return authorName;
	}
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	public String getAddTime() {
		return addTime;
	}
	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	

}
