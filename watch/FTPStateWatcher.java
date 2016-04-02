//###########################################################################
//读取数据库中已有的IP，监测各个IP对应的服务器是否打开,将结果写入数据库中的IsAlive字段；
package watch;

import java.sql.*;
import com.jscape.inet.ftp.*;
import javax.swing.JLabel;

import tools.DBManager;
import structs.DBProperty;
import structs.WatcherProperty;

public class FTPStateWatcher extends Thread
{
	String	hostName=new String(),
			userName=new String(),
			passWord=new String();
	private DBManager dBManager=new DBManager();
	private ResultSet rsIPInfo;
	private int 		 threadCount;
	private long		 time;
	private SigWatcher[]   	 theGroup;		
	public  boolean	 isFinished=false;
	private JLabel result;
	
	public FTPStateWatcher(DBProperty dBProperty,WatcherProperty watcherProperty,JLabel result)
	{
		dBManager.setDriverClassType(dBProperty.getDriverName());
		dBManager.setDBURL(dBProperty.getUrl());
		dBManager.setUser(dBProperty.getUser());
		dBManager.setPassword(dBProperty.getPassword());
		
		this.result = result;
		
		threadCount = watcherProperty.getThreadNum();
		time = watcherProperty.getTime();
	}
	//###############################################
   private  void initIPInfo()
   {
   		String strCmd="SELECT IP,user,password FROM iplist";
   		rsIPInfo=dBManager.executeQuery(strCmd,false);
   }
    
      private  int getOnlineNum()
   {
   		String s="SELECT count(*) FROM iplist WHERE isconnected=1";
   		ResultSet rs=dBManager.executeQuery(s,false);
   		
   		if(rs==null)
   		{
            return 0;    		
        }
        
        int n = 0;
   		try{
   			while(rs.next())
   			{
   				n = rs.getInt("count(*)");
   			}
   			rs.close();
   		}catch(SQLException e){
   		}
   		
   		return n;
    }
    
	//##############################################
	public void run()
	{	
		try{
			 while(true)
			 {
			 	result.setText("启动一轮监视.....");

				initIPInfo();
				theGroup=new SigWatcher[threadCount];
				
				int size = 0;
				int total = 0;				
				isFinished=false;
				
				//选择前threadCount个进行连接
				for(int i=0;i<threadCount;i++)
				{
					if(rsIPInfo.next())
					{
					   theGroup[i] = new SigWatcher(rsIPInfo.getString("IP"),rsIPInfo.getString("user"),rsIPInfo.getString("password"));
					   size++;
					   total++;
					}
					else
					    break;
				}//end for
				
                //不断查看各个线程状况，选择空闲线程继续进行连接
				while(true)
				{
					for(int i=0;i<size;i++)
					{ 
						if(!(theGroup[i].isAlive()))
						{
							if(rsIPInfo.next()){
								total++;
								theGroup[i]=new SigWatcher(rsIPInfo.getString("IP"),rsIPInfo.getString("user"),rsIPInfo.getString("password"));
							}
							else
							{
								isFinished = true;
								break;	//一次监测完成；正常退出
							}
						}
					}
					
					if(isFinished)
					   break;
					
				 try{
						sleep(1000);
					}catch(InterruptedException e){
						e.printStackTrace();
					}			
				}
				
				while(true)
				{
				   int finishNum = 0;
					
				   for(int i=0;i<size;i++)
				  {
					if(theGroup[i]==null)
					    finishNum++;
					else if(!(theGroup[i].isAlive()))
						finishNum++;
				  }

				   if(finishNum==size)
				   {
				      break;
				   }
				   else
				   {
				   try{
						sleep(1000);
					  }catch(InterruptedException e){
						e.printStackTrace();
					  }			
				   }
				}

System.out.println("*********");				
				result.setText("此次监视结束，共有FTP站点"+total+"个，在线站点"+getOnlineNum()+"个!");
				
                dBManager.closeDB();

			    try
				{
					sleep(time);
				}catch(InterruptedException e){
					e.printStackTrace();
				}
			 }//end while				
				
		}catch(SQLException e){
			dBManager.closeDB();
			isFinished=true;
			return;			//异常退出；
		}
	}
 
 
	//#############################################
	class SigWatcher extends Thread
	{
		private String	hostName;
		private String	user;
		private String	pw;				
		private Ftp 	ftpClient;
		public  boolean isFinished=false;
		
		public SigWatcher(String hostName,String user,String pw)
		{
            this.hostName = hostName;
            this.user = user;
            this.pw = pw;
			start();
		}
		
		public void run()//给予每个IP最多10秒钟的连接时间；
	  	{
	  	 	 if(connect()==1) 
	  	 	{
	  	 	  System.out.println(hostName+"连接成功");
	          dBManager.executeUpdate("UPDATE iplist SET isconnected=1 WHERE IP='"+hostName+"'",false);
	  	 	}
             else
            {
              System.out.println(hostName+"连接失败");
	          dBManager.executeUpdate("UPDATE iplist SET isconnected=0 WHERE IP='"+hostName+"'",false);
            }
	  	}
	  	
	  	private int connect()
   		{
   		    try
   	 		{
   	 			ftpClient = new Ftp(hostName,user,pw);
   	 			ftpClient.connect();
   	 			ftpClient.disconnect();
   	 			return 1;
   			 }catch(FtpException e){
   			 	return 0;
  		 	 }
	  	}
	}
}