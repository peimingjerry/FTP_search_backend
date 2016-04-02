//此程序工作在SQL数据库中，将各个ftp站点的文件信息统一编号，形成初始文档

package index;

import java.sql.*;
import tools.DBManager;
import java.util.ArrayList;

public class CreatDoc
{
	
	private String docTable = new String("doc"),
	               ipTable = new String("iplist");
	private DBManager myDBManager;
	
	public CreatDoc(String dbDriver,String dbUrl1,String dbUser,String dbPassword)
	{
		myDBManager = new DBManager();
		myDBManager.setDriverClassType(dbDriver);
		myDBManager.setDBURL(dbUrl1);
		myDBManager.setUser(dbUser);
		myDBManager.setPassword(dbPassword);
	}
	
	public int execute(boolean updateAll,String _sql)
	{
	   ResultSet rs;
	   String sql;
	   ArrayList ipList = new ArrayList(),tableList = new ArrayList();
       
       if(updateAll)
      {
	   	 //删除原有索引数据库,新建立表 
	     sql = "DROP TABLE "+docTable;
	  
	     myDBManager.execute(sql,false);//need modificton

 	     sql = "CREATE TABLE "+docTable+"(ID int(11) unsigned not null auto_increment primary key, layer int(3) NOT NULL,url varchar(15) NOT NULL,name varchar(255) NOT NULL,dir varchar(255) NOT NULL,property varchar(2),size int(11),date varchar(15),type varchar(10) NOT NULL,_ID int(11)) ENGINE=InnoDB DEFAULT CHARSET=gbk";
	  
	     if(myDBManager.execute(sql,false)==0)
	    {
	  	   System.out.println("Error occurs when creating table doc:"+myDBManager.getErrorMessage());
	  	   myDBManager.closeDB();
	   	   
	  	   return 0;
	    }
      }  

	  //新建索引
	    //添加信息至doc
	  if(updateAll)
	  {
	     sql = "SELECT * FROM "+ipTable+" WHERE valid=1";
	  }
	  else
	  {
	     sql = "SELECT * FROM "+ipTable+" "+_sql;	  	 
	  }
System.out.println(sql);	  
	  rs = myDBManager.executeQuery(sql,false);
	   
	   if(rs==null)
	  {
	   	  System.out.println(myDBManager.getErrorMessage());
	   	  myDBManager.closeDB();
	   	  
	   	  return 0;
	  }
	   
	  try
	  {
	       while(rs.next())
	      {
	   	    ipList.add(rs.getString("IP"));
	   	    tableList.add("f"+rs.getLong("ID")+"th");
	      }
	  }catch(SQLException er){
	   	    System.out.println("error:"+myDBManager.getErrorMessage());
	   	    myDBManager.closeDB();

		    return 0;
	  }
	   
	     for(int i=0;i<ipList.size();i++)
	  {
	      String url = ipList.get(i).toString();
//need 修改	      
	      sql = "SELECT * FROM "+tableList.get(i).toString();
	      rs = myDBManager.executeQuery(sql,false);
	      
	      if(rs!=null)
	      {
	      	try
	      	{
	      	   while(rs.next())
	      	  {
	      	    sql = "INSERT INTO "+docTable+"(layer,url,name,dir,property,size,date,type,_ID) VALUES ("+rs.getInt("layer")+",'"+url+"','"+rs.getString("name")+"','"+rs.getString("dir")
	      	          +"','"+rs.getString("property")+"',"+rs.getString("size")+",'"+rs.getString("date")+"','"+rs.getString("type")+"',"+rs.getLong("ID")+")";

	      	    if(myDBManager.executeUpdate(sql,false)==0)
	      	    {
	      	   	  System.out.println(myDBManager.getErrorMessage());
	      	    }
	      	  }
	      	}catch(SQLException e){
	   	      System.out.println(myDBManager.getErrorMessage());
	      	}
	      }
	   }
	   
	   //关闭数据库连接
	   try
	   {
	   	  if(rs!=null)
	   	   rs.close();
	   }catch(SQLException e){
	   }
	   
	   myDBManager.closeDB();
	    
	   return 1;
	}
	/*
	public static void main(String args[])
	{
		CreatDoc creatDoc = new CreatDoc("com.mysql.jdbc.Driver","jdbc:mysql://localhost:3306/ftpinfo?user=root&password=163150","jdbc:mysql://localhost:3306/ftpindex?user=root&password=163150","root","163150");
		long startTime = System.currentTimeMillis();
		if(creatDoc.execute(false,"WHERE urlID =8")==0)
		{
			System.out.println("error occurs!");
		}
		else
		{
			System.out.println("Creat doc finished;Cost Time:"+(System.currentTimeMillis()-startTime)/1000+"s.");
		}
	}*/
}