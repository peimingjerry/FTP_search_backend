package tools;

import java.sql.*;

public class DBManager
{
	private Connection connection;
	private String driverClassType,DBURL,user,password,errorMessage;
	private Statement stmt;
	private boolean isConnected;
	
	public DBManager()
	{
		errorMessage = new String();
		isConnected = false;
	}
	
	public String getErrorMessage()
	{
		return errorMessage;
	}
	
	public boolean isConnected()
	{
		return isConnected;
	}
	
	public void setDriverClassType(String driverClassType)
	{
		this.driverClassType = driverClassType;
	}
		
	public void setDBURL(String url)
	{
		this.DBURL = url;
	}

	public void setUser(String user)
	{
		this.user = user;
	}
	
	public void setPassword(String password)
	{
		this.password = password;
	}
		
	
	public String getDriverClassType()
	{
		return driverClassType;
	}
	
	public String getDBURL()
	{
		return DBURL;
	}
	
	public String getUser()
	{
		return user;
	}
	
	public String getPassword()
	{
		return password;
	}
	
	public int connect()
	{
	  if(isConnected)
	  {
	  	return 1;
	  }
   try{
	  	Class.forName(driverClassType);
	  }catch(ClassNotFoundException e){
	  	errorMessage = e.toString();
System.out.println(errorMessage);
	  	return 0;
	  }
	  try{    //("jdbc:mysql://localhost/test?user=monty&password=greatsqldb");
	  	connection = DriverManager.getConnection(DBURL);//.getConnection(DBURL,user,password);
	  	isConnected = true;
        return 1;
	  }catch(SQLException e1){
	  	errorMessage = e1.toString();
	  	return 0;
	  }
	}
	
	public int closeDB()
	{
	 if(connection!=null) 
	 {
	 try{
	 	 if(stmt!=null)
	 	 {
	 	 	stmt.close();
	 	 }  
	 	   
	  	 connection.close();	
	  	 isConnected = false;
	  	
	  	 return 1;
	    }catch(SQLException e){
	  	 errorMessage = e.toString();
	  	 isConnected = false;
	  	 return 0;
	    }
	 }
	 return 1;
	}
	
	public ResultSet executeQuery(String que,boolean autoClose)
	{
	  //get to connect
		if(connect()==0)
		{
		  return null;				
		}
      //execute
	try{
		 stmt = connection.createStatement();
		 ResultSet rs = stmt.executeQuery(que);
		 //close
		 if(!autoClose)
		 {
		 	return rs;
		 }
		 else
		 {
		 	closeDB();
		 	return rs;
		 }	 			
	   }catch(SQLException e){
		 errorMessage = e.toString();
		 return null;
	   }
	 }
	 
	public int executeUpdate(String upd,boolean autoClose)
	{
	  //get the connection
	   if(connect()==0)
	   {
		 return 0;				
	   }
      //execute
	try{
		 stmt = connection.createStatement();
		 stmt.executeUpdate(upd);
		 //close
		 if(!autoClose)
		 {
		 	return 1;
		 }
		 else
		 {
		 	closeDB();
		 	return 1;
		 }	 			
	   }catch(SQLException e){
		 errorMessage = e.toString();
		 return 0;
	   }
	 }
	 
	 public int execute(String sql,boolean autoClose)
	 {
	  //get the connection
	   if(connect()==0)
	   {
		 return 0;				
	   }
      //execute
	try{
		 stmt = connection.createStatement();
		 stmt.execute(sql);
		 //close
		 if(!autoClose)
		 {
		 	return 1;
		 }
		 else
		 {
		 	closeDB();
		 	return 1;
		 }	 			
	   }catch(SQLException e){
		 errorMessage = e.toString();
		 return 0;
	   }	 	
	 }
}
