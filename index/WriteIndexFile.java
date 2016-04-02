//此程序将SQL数据库中已经生成的索引文件写入文件index.dat和index.head.dat

package index;

import java.sql.*;
import java.io.*;
import tools.DBManager;

public class WriteIndexFile
{
	private String indexTable = new String("docindex"),         
	               filePath = new String(),
	               filePath1 = new String();
	private DBManager myDBManager;
	
	public WriteIndexFile(String dbDriver,String dbUrl1,String dbUser,String dbPassword,String filePath,String filePath1)
	{
	    myDBManager = new DBManager();
		myDBManager.setDriverClassType(dbDriver);
		myDBManager.setDBURL(dbUrl1);
		myDBManager.setUser(dbUser);
		myDBManager.setPassword(dbPassword);
		this.filePath = filePath;
		this.filePath1 = filePath1;
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

	
	public int execute()
	{
	  FileOutputStream out,out1;
	  //连接到索引文件
	try{
   	  	 out = new FileOutputStream(filePath);
   	   }catch(IOException ee){
   	  	 try{
   	  	    out = new FileOutputStream(filePath);
   	  	 }catch(IOException e){
   	  	 	return 0;
   	  	 }
   	   }
   	   
	try{
   	  	 out1 = new FileOutputStream(filePath1);
   	   }catch(IOException ee){
   	  	 try{
   	  	    out1 = new FileOutputStream(filePath1);
   	  	 }catch(IOException e){
   	  	 	return 0;
   	  	 }
   	   }
   	      	   
      //逐个写入
        int start = 0;
        
		for(int i=0;i<65536;i++)
	   {
System.out.println("i:"+i);		 
		 String sql = "SELECT docid,offset FROM "+indexTable+" WHERE wordid="+i+" ORDER BY docid,offset";
   	   	 ResultSet rs = myDBManager.executeQuery(sql,false);
   	   	 int total = 0;
   	   	  
   	   	  if(rs!=null)
   	   	 {
   	   	    try
   	   	   {
   	   	 	 while(rs.next())
   	   	 	 {
   	   	 		total++;
   	   	 		byte[] b1 = int2bytes(rs.getInt("docid"));
   	   	 		byte[] b2 = short2bytes(rs.getShort("offset"));
   	   	 		byte[] b = new byte[4];
   	   	 		for(int k=0;k<3;k++)
   	   	 		{
   	   	 			b[k] = b1[k+1];
   	   	 		} 
   	   	 		b[3] = b2[1];
   	   	 	 try
   	           {
   	   	 		 out.write(b);
   	           }catch(IOException e){
   	           }
   	   	 	 }
   	   	   }catch(SQLException e){
   	   	     myDBManager.closeDB();
   	   	     return 0;   	   	 
   	   	   }
   	   	 }
   	   	 
   	   	 //写入index.head.dat
   	   	 try
   	     {
   	   	    out1.write(int2bytes(total));
   	   	    if(total!=0)
   	   	    {
   	   	    	out1.write(int2bytes(start));
   	   	    }
   	   	    else
   	   	    {
   	   	    	out1.write(int2bytes(0));
   	   	    }
   	   	 }catch(IOException e){
   	     }

   	     //更新start
         start = start + total*4;
	   }
	 
   	   //关闭连接
   	   try
   	   {
   	   	  	out.close();
   	   	  	out1.close();
   	   }catch(IOException e){
   	   }
   	   	 myDBManager.closeDB();
   	   	 
   	   	 return 1;
      }
/*	  
	  public static void main(String args[])
	  {
	  	WriteIndexFile writeIndexFile = new WriteIndexFile("com.mysql.jdbc.Driver","jdbc:mysql://localhost:3306/ftpinfo?user=root&password=163150","root","163150","F:\\ftp\\index.dat","F:\\ftp\\index.head.dat");
	  	if(writeIndexFile.execute()==1)
	  	{
	  		System.out.println("Write Index Finished!");
	  	}
	  	else
	  	{
	  		System.out.println("error!");
	  	}
	  }	*/
	}
 