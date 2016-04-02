package structs;

public class DBProperty
{
	private String driver,url,user,password;
	
	public DBProperty()
	{
	}
	
	public DBProperty(String driver,String url,String user,String password)
	{
		this.driver = driver;
		this.url = url;
		this.user = user;
		this.password = password;
	}
	
	public String getDriverName()
	{
		return driver;
	}
	public void setDriverName(String driver)
	{
		this.driver = driver;
	}
	
	public String getUrl()
	{
		return url;
	}
	public void setUrl(String url)
	{
		this.url = url;
	}
	
	public String getUser()
	{
		return user;
	}
	public void setUser(String user)
	{
		this.user = user;
	}
	
	public String getPassword()
	{
		return password;
	}
	public void setPassword(String password)
	{
		this.password = password;
	}
}