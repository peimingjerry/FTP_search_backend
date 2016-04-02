package structs;

public class ThreadCounter
{
	private int maxThreadCount;
	
	public ThreadCounter()
	{
		maxThreadCount = 3;
	}
	
	public int getSize()
	{
		return maxThreadCount;
	}
	
	public void setSize(int maxThreadCount)
	{
		this.maxThreadCount = maxThreadCount;
	}
}