package structs;

public class WatcherProperty
{
	private int time;
	private int threadNum;
	
	public WatcherProperty()
	{
		time = 30;
		threadNum = 10;
	}
	
	public long getTime()
	{
		return time*60000;
	}
	public void setTime(int time)
	{
		this.time = time;
	}
	
	public int getThreadNum()
	{
		return threadNum;
	}
	public void setThreadNum(int threadNum)
	{
		this.threadNum = threadNum;
	}
}