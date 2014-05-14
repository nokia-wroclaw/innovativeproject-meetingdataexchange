package dataBase;

public class MettingEntity 
{
	private long ID;
	private long serverId;
	private long serverMeetingID;
	private String title;
	private String topic;
	private String hostName;
	private String startTime;
	private String endTime;
	private String code;
	private int numberOfMembers;
	private int permission;
	public long getID() {
		return ID;
	}
	public void setID(long iD) {
		ID = iD;
	}
	public long getServerId() {
		return serverId;
	}
	public void setServerId(long serverId) {
		this.serverId = serverId;
	}
	public long getServerMeetingID() {
		return serverMeetingID;
	}
	public void setServerMeetingID(long serverMeetingID) {
		this.serverMeetingID = serverMeetingID;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public int getNumberOfMembers() {
		return numberOfMembers;
	}
	public void setNumberOfMembers(int numberOfMembers) {
		this.numberOfMembers = numberOfMembers;
	}
	public int getPermission() {
		return permission;
	}
	public void setPermission(int permission) {
		this.permission = permission;
	}
	

}
