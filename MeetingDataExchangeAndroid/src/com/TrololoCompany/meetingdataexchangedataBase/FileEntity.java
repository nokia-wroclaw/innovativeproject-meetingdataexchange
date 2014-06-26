package com.TrololoCompany.meetingdataexchangedataBase;

public class FileEntity 
{
	private long ID;
	private long meetingID;
	private long serverFileId;
	private String fileName;
	private String authorName;
	private String addTime;
	private String hashMD5;
	public long getID() {
		return ID;
	}
	public void setID(long iD) {
		ID = iD;
	}
	public long getMeetingID() {
		return meetingID;
	}
	public void setMeetingID(long meetingID) {
		this.meetingID = meetingID;
	}
	public long getServerFileId() {
		return serverFileId;
	}
	public void setServerFileId(long serverFileId) {
		this.serverFileId = serverFileId;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
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
	public String getHashMD5() {
		return hashMD5;
	}
	public void setHashMD5(String hashMD5) {
		this.hashMD5 = hashMD5;
	}
	

}
