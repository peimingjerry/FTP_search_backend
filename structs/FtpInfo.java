package structs;

public class FtpInfo
{
	private String url;
	private byte range;
	private short id;
	
	public FtpInfo()
	{
	}
	
	public String getUrl()
	{
		return url;
	}
	public void setUrl(String url)
	{
		this.url = url;
	}
	
	public short getId()
	{
		return id;
	}
	public void setId(short id)
	{
		this.id = id;
	}
	
	public byte getRange()
	{
		return range;
	}
	public void setRange(byte range)
	{
		this.range = range;
	}}
