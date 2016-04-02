//�˳��������ǽ�������Χ�ڵ�ȫ���ĵ�����������д��SQL���ݿ�

package index;

import java.sql.*;
import tools.DBManager;

public class IndexMachine
{
	private String indexTable = new String("docindex"),         
	               docTable = new String("doc");
	private DBManager ftpListManager;
	
	public IndexMachine(String dbDriver,String dbUrl1,String dbUser,String dbPassword)
	{
	    ftpListManager = new DBManager();
		ftpListManager.setDriverClassType(dbDriver);
		ftpListManager.setDBURL(dbUrl1);
		ftpListManager.setUser(dbUser);
		ftpListManager.setPassword(dbPassword);
	}
	
	
	//���ֳɴ�
	public int[] cutAndCode(String key)
	{
		key = key.toLowerCase();
		byte[] b = key.getBytes();
		int[] shorts = new int[b.length-1];
		
		for(int i=0;i<b.length-1;i++)
		{
			int k = 0;
			k = (int)(k + ((int)(b[i])+256)%256 + ((((int)(b[i+1])+256)%256)<<8));
			shorts[i] = k;
		}

		return shorts;
	}	
			
	public int creatIndex(boolean all,String docSql)
	{
	   ResultSet rs;
	   String sql; 
	   
       if(all)
       {
          if(ftpListManager.execute("DELETE FROM "+indexTable,false)==0)
	     {
System.out.println("error:"+ftpListManager.getErrorMessage());
		     ftpListManager.closeDB();
	   	             
		     return 0;		        	
	     } 
       }
		 
//�ִʡ���������
 	   sql = "SELECT ID,name FROM "+docTable+docSql;
System.out.println("doc:"+sql);	   
	   rs = ftpListManager.executeQuery(sql,false);
	   
	   long docId;
	   String name;

	try{
	   	 while(rs.next())
	     {
	       docId = rs.getLong("ID");
		   name = rs.getString("name");
System.out.println("-"+docId);	   

      //Ϊ�ļ�����������	
		  //�ִ�
		  int[] seg = cutAndCode(name);
		   
		  //�������
		  if(seg.length<=0)
		  {
		  	continue;
		  }
		  
		  sql = "INSERT INTO "+indexTable+" VALUES ("+seg[0]+","+docId+","+"0"+")";
          for(int i=1;i<seg.length;i++)
          {
          	 sql = sql + ",("+seg[i]+","+docId+","+i+")";
          }
//System.out.println("sql:"+sql);		
          if(ftpListManager.execute(sql,false)==0)
		  {
System.out.println("error:"+ftpListManager.getErrorMessage());
		     ftpListManager.closeDB();
	   	             
		     return 0;		        	
		  } 	
	     }//end while
	   }catch(SQLException e){
	   	ftpListManager.closeDB();
	   	
	   	return 0;
	   } 
		
		 //�ر����ݿ�����
		 if(rs!=null)
		{
		try{
			 rs.close();
		   }catch(SQLException e1){
		   	 System.out.println("error:"+e1.toString());
		     return 0;
		   }
		}
		ftpListManager.closeDB();
System.out.println("Indexes finishied!");
		
		return 1;
	}
/*
	public static void main(String args[])
	{
	   IndexMachine indexMachine = new IndexMachine("com.mysql.jdbc.Driver","jdbc:mysql://localhost:3306/ftpinfo?user=root&password=163150","root","163150");

       long beginIndex = System.currentTimeMillis();
       
   	   if(indexMachine.creatIndex(" WHERE ID<30000")==0)
   	  {
   	  	System.out.println("error happened when creating indexes!");
   	  }
   	   else
   	  {
   	  	System.out.println("indexes finished,��ʱ��"+(System.currentTimeMillis()-beginIndex)/1000+"�룡");
   	  }
   	  System.exit(0);
	}*/
}
