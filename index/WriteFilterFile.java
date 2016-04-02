//此程序的作用是将文档的过滤信息整理并写入filter.dat文件

package index;

import java.sql.*;
import java.io.*;
import tools.DBManager;
import java.util.*;

import structs.FtpInfo;

public class WriteFilterFile
{
	private String docTable = new String("doc"),
	               filePath = new String(),
	               ipListTable = new String("iplist");
	private DBManager myDBManager;
	
	public WriteFilterFile(String dbDriver,String dbUrl1,String dbUser,String dbPassword,String filePath)
	{
	    myDBManager = new DBManager();
		myDBManager.setDriverClassType(dbDriver);
		myDBManager.setDBURL(dbUrl1);
		myDBManager.setUser(dbUser);
		myDBManager.setPassword(dbPassword);
		this.filePath = filePath;
	}
	
	 static byte[] short2bytes(short num)
    {
       byte[] b=new byte[2];
       for(int i=0;i<2;i++)
       {
            b[i]=(byte)(num>>(8-i*8));
       }
      return b;
    }

     static byte[] int2bytes(int num)
    {
       byte[] b=new byte[4];
       for(int i=0;i<4;i++)
       {
            b[i]=(byte)(num>>>(24-i*8));
       }
      return b;
    }

	 //获取文件类型编号
     private byte getByte(String type)
    {
    	if(type.equals("dir"))
    		return 0;
    	else if(type.equals("image"))
    	    return 1;
    	else if(type.equals("video"))
    	    return 2;
    	else if(type.equals("music"))
    	    return 3;
    	else if(type.equals("doc"))
    	    return 4;
    	else if(type.equals("zip"))
    	    return 5;
    	else if(type.equals("program"))
    	    return 6;
    	else if(type.equals("page"))
    	    return 7;
    	else
    	    return 8;
    }
    
	public int execute()
	{
	  FileOutputStream out;
	  
	  //连接到filter文件
	try{
   	  	 out = new FileOutputStream(filePath);
   	   }catch(IOException ee){
   	  	 try{
   	  	    out = new FileOutputStream(filePath);
   	  	 }catch(IOException e){
   	  	 	return 0;
   	  	 }
   	   }
  
  	   ArrayList urlList = new ArrayList();	   
	   
	   //读取url列表
	   String sql = "SELECT IP,urlid,range FROM "+ipListTable;
	   ResultSet rs = myDBManager.executeQuery(sql,false);
	   try
	   {
	      while(rs.next())
	     {
	   	   FtpInfo ftp = new FtpInfo();
	   	  
	   	   ftp.setUrl(rs.getString("IP"));
	   	   ftp.setId(rs.getShort("urlid"));
	   	   ftp.setRange(rs.getByte("range"));
	   	  
	   	   urlList.add(ftp);
	     }
	   }catch(SQLException e){
	   	  myDBManager.closeDB();
	   	
	   	  return 0;
	   }
 	      	   
      //逐个写入
	   sql = "SELECT type,url FROM "+docTable;
System.out.println("doc:"+sql);	   
	   rs = myDBManager.executeQuery(sql,false);
	   
	   if(rs!=null)
	  {
	   	try
	   {
	   	 while(rs.next())
	   	 {
		   FtpInfo ftpInfo;
		   byte _range = 0;
		   short _urlID = 0;
		   
		   for(int k=0;k<urlList.size();k++)
		   {
		   	  ftpInfo = (FtpInfo)(urlList.get(k));
		   	  if(ftpInfo.getUrl().equals(rs.getString("url")))
		   	  {
		   	  	_range = ftpInfo.getRange();
		   	  	_urlID = ftpInfo.getId();
		   	  	break;
		   	  }
		   }
		   
           byte[] b =new byte[4];
           b[0] = getByte(rs.getString("type"));
           b[1] = _range;
           byte[] a = short2bytes(_urlID);
           b[2] = a[0];
           b[3] = a[1];

	   	   try
   	       {  
   	   	 	  out.write(b);
   	       }catch(IOException e){
   	       }
	   	 }
/*	   	 
	   	 if(myDBManager.executeUpdate("UPDATE "+infoTable+" SET num="+indexNum+" WHERE type='indexednum'",false)==0)
	   	 {
             myDBManager.closeDB();
   	   	     return 0;	   	 
   	   	 }*/
	   }catch(SQLException e){
   	   	     myDBManager.closeDB();
   	   	     return 0;   	   	 
   	   }
	  }

   	  //关闭连接
   	  try
   	  {
   	   	  	out.close();
   	  }catch(IOException e){
   	  }
   	   myDBManager.closeDB();
   	   	 
   	   return 1;
   }
	  /*
	public static void main(String args[])
  {
	  WriteFilterFile writeFilterFile = new WriteFilterFile("com.mysql.jdbc.Driver","jdbc:mysql://localhost:3306/ftpinfo?user=root&password=163150","root","163150","F:\\ftp\\filter.dat");
	  
	  int max = 29999;
	  
	  if(writeFilterFile.execute()==1)
	  {
	  	System.out.println("Write Index Finished!");
	  }
	  else
	  {
	  	System.out.println("error!");
	  }
  }	*/
}
