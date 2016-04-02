//控制对所有IP进行重新搜索

package robot;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Vector;
import java.sql.*;

import structs.DBProperty;
import structs.ThreadCounter;
import tools.DBManager;

public class SearchAllThread extends Thread
{
	static 	int[] 	intIPFrom;
	static 	int[] 	intIPEnd;
	String[]		IPMSG;
	JTextArea		textArea;
	Vector 			threadVector;
	ThreadCounter 	threadCounter;
	DBProperty      myDBProperty;
	ArrayList       ipList = new ArrayList();
	ArrayList       userList = new ArrayList();
	ArrayList       pwList = new ArrayList();
	DBManager       myDBManager;
	String          ipListTable = new String("iplist");

	
	public SearchAllThread(DBProperty myDBProperty,JTextArea taMSG,Vector theVector,ThreadCounter theCounter)
	{
		this.textArea=taMSG;
		this.threadVector = theVector;
		this.threadCounter = theCounter;
		this.myDBProperty = myDBProperty;
		myDBManager = new DBManager();
		myDBManager.setDriverClassType(myDBProperty.getDriverName());
		myDBManager.setDBURL(myDBProperty.getUrl());
		myDBManager.setUser(myDBProperty.getUser());
		myDBManager.setPassword(myDBProperty.getPassword());
	}
	
	public void run()
	{
		String sql = "SELECT IP,user,password FROM "+ipListTable;
        ResultSet rs = myDBManager.executeQuery(sql,false);
        
        if(rs!=null)
        {
	       try{
	            while(rs.next())
	            {
	            	ipList.add(rs.getString("IP"));
	            	userList.add(rs.getString("user"));
	            	pwList.add(rs.getString("password"));
	            }
	       }catch(SQLException e){
        	 textArea.append(myDBManager.getErrorMessage());
	       } 
        }
        else
        {
        	 textArea.append("No IP Found!");
        }

        int ftpNum = ipList.size();
        int i;
        
System.out.println("threadCounter.getSize():"+threadCounter.getSize());			
		for(i=0;i<threadCounter.getSize();i++)//初始化多线程 
		{
			if(i<ftpNum)
			{
System.out.println("新线程在访问ip："+ipList.get(i).toString());				
			  SearchThread thread = new SearchThread(ipList.get(i).toString(),textArea,userList.get(i).toString(),pwList.get(i).toString(),
			                        myDBProperty.getDriverName(),myDBProperty.getUrl(),myDBProperty.getUser(),myDBProperty.getPassword());
			  thread.start();
			  threadVector.add(thread);
			}
		}
		
		while(i<ftpNum)
		{	
			for(int j=0;j<threadVector.size();j++)
			{
				 if(!((Thread)(threadVector.elementAt(j))).isAlive())
				{
				   threadVector.remove(j);
				   	 
System.out.println("新线程在访问ip："+ipList.get(i).toString());				     
			       SearchThread thread = new SearchThread(ipList.get(i).toString(),textArea,userList.get(i).toString(),pwList.get(i).toString(),
			                        myDBProperty.getDriverName(),myDBProperty.getUrl(),myDBProperty.getUser(),myDBProperty.getPassword());
				   thread.start();
				   threadVector.add(thread);
				   i++;
				}
			}
		try{
			   sleep(60000);
		    }catch(InterruptedException e){
			}
		}
	}
}