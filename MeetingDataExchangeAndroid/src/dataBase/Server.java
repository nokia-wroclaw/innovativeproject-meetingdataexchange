package dataBase;

public class Server 
{
	public Server(String name,String introduceName)
	{
		this.name=name;
		this.introduceName=introduceName;
	}
	private String name;
	private String introduceName;
	public String getIntroduceName() {
		return introduceName;
	}
	public void setIntroduceName(String introduceName) {
		this.introduceName = introduceName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	

}
