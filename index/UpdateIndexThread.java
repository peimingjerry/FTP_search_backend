//更新单个IP的索引，写入SQL数据库；同时更新文档库中的相应记录

package index;

import javax.swing.JButton;
import javax.swing.JLabel;

import java.sql.*;

import structs.DBProperty;
import tools.DBManager;

public class UpdateIndexThread extends Thread
{
	DBProperty myDBProperty;
	JButton button;
	JLabel label;
	String IP;
	private DBManager myDBManager;
	String docTable = new String("doc"),
	       indexTable = new String("docindex");
	
	public UpdateIndexThread(JButton button,JLabel label,DBProperty myDBProperty,String IP)
	{
		this.myDBProperty = myDBProperty;
		this.button = button;
		this.label = label;
		this.IP = IP;
		
		myDBManager = new DBManager();
		myDBManager.setDriverClassType(myDBProperty.getDriverName());
		myDBManager.setDBURL(myDBProperty.getUrl());
		myDBManager.setUser(myDBProperty.getUser());
		myDBManager.setPassword(myDBProperty.getPassword());
	}
	
	public void run()
	{
        String sql = "SELECT MAX(ID),MIN(ID) FROM "+docTable+" WHERE url='"+IP+"'";
        ResultSet rs = myDBManager.executeQuery(sql,false);
        
        if(rs!=null)
        {
	       int start = 0,end = 0;
	       
	       try{
	            while(rs.next())
	            {
	            	end = rs.getInt("MAX(ID)");
	            	start = rs.getInt("MIN(ID)");
System.out.println("start:"+start);	            	
System.out.println("end:"+end);
	            }
	            if(rs!=null)
	               rs.close();
	       }catch(SQLException e){
        	  button.setEnabled(true);
        	  label.setText("Read Error!");
        	  myDBManager.closeDB();
        	  return;
	       } 
	       if((start*end)!=0)
	      {
	         sql = "DELETE FROM "+indexTable+" WHERE docid>="+start+" AND docid<="+end;
             if(myDBManager.execute(sql,false)==0)
	        {
        	  button.setEnabled(true);
        	  label.setText("Delete Index Error!");
	  	      myDBManager.closeDB();
	  	      return;
	        }
	      
	        sql = "DELETE FROM "+docTable+" WHERE ID>="+start+" AND ID<="+end;
            if(myDBManager.execute(sql,false)==0)
	        {
        	 button.setEnabled(true);
        	 label.setText("Delete Doc Error!");
	  	     myDBManager.closeDB();
	  	     return;
	        }
	      }
        }
        
        CreatDoc creatDoc = new CreatDoc(myDBProperty.getDriverName(),myDBProperty.getUrl(),myDBProperty.getUser(),myDBProperty.getPassword());
		if(creatDoc.execute(false," WHERE IP='"+IP+"'")==0)
		{
        	 button.setEnabled(true);
        	 label.setText("Create doc Error!");
        	 myDBManager.closeDB();
	  	     return;
		}

        IndexMachine indexMachine = new IndexMachine(myDBProperty.getDriverName(),myDBProperty.getUrl(),myDBProperty.getUser(),myDBProperty.getPassword());	
        if(indexMachine.creatIndex(false," WHERE url='"+IP+"'")==0)
        {
        	button.setEnabled(true);
        	label.setText("Create index Error!");
        	myDBManager.closeDB();        	
			return;
        }
        
        myDBManager.closeDB();
        button.setEnabled(true);
        label.setText("索引结束！当前空闲...");
	}
}
