//���Ĺ����̣߳�����FTPվ��������Ѽ���Ϣ����

package robot;

import java.util.*;
import javax.swing.JTextArea;
import java.sql.*; 

import com.jscape.inet.ftp.*;
import tools.DBManager;

public class SearchThread extends Thread
{
   private Ftp ftpClient;
   private String hostName,curDir,userName,passWord,
                  IPlistTable = new String("iplist"),
                  tableIdTable = new String("info");
   private Vector list,directoryList,dirInfoList;         //tools
   private JTextArea textArea;                             //show
   private DBManager myDBManager;                         //DB tool
   private String tableName;
   
   public SearchThread(String hostName,JTextArea textArea,String userName,String passWord,
                       String DBDriver,String ftpListUrl,String DBUser,String DBPassword)	
   {
   	 this.hostName = new String(hostName);
   	 this.textArea = textArea;
   	 this.userName = userName;
   	 this.passWord = passWord;
   	 
   	 list = new Vector();                  //��ʱ���list����õ�����Ϣ
   	 directoryList = new Vector();         //��ΪĿ¼�Ķ�ջ������·����
   	 dirInfoList = new Vector();   	       //���Ŀ¼��Ӧ����ϸ��Ϣ��ֱ����list�õ��ģ� 
   	 curDir = new String();                //��ŵ�ǰĿ¼����ʱ����
   	 
   	 //��ʼ�����ݿ������
   	 myDBManager = new DBManager();
   	 myDBManager.setDriverClassType(DBDriver);
   	 myDBManager.setDBURL(ftpListUrl);
   	 myDBManager.setUser(DBUser);
   	 myDBManager.setPassword(DBPassword);
   }
   
   //������FTP
   private int connect()
   {
   	 try
   	 {
   	 	ftpClient = new Ftp(hostName,userName,passWord);
   	 	ftpClient.connect();
   	 	textArea.append("�̵߳�¼"+hostName+"�ɹ���\n");

   	 	return 1;
   	 }catch(FtpException e){
   	 	textArea.append("�̵߳�½FTP:"+hostName+"ʧ�ܣ�\n");
   	 	return 0;
   	 }
   }
      
   //�ر����ݿ�����
   private int closeDBManager()
   {
   	 if(myDBManager.closeDB()==0)
   	 {
   	 	System.out.println(myDBManager.getErrorMessage());
   	 	return 0;
   	 }
   	 return 1;
   }   
   
      
   //��database����ȡtable ID
   synchronized private long gettableID(DBManager dBManager)
   {
   	 long i = 0;
   	 
   	 try{
   	 	  ResultSet res = dBManager.executeQuery("SELECT * FROM "+tableIdTable+" WHERE type='ftp'",false);
   	 	  while(res.next())
   	 	  {
   	 	  	i = res.getLong("num");
   	 	  }
   	 	  res.close();
   	    }catch(SQLException e){
   	 	 textArea.append("SQLException occurs when reading 'tableID'!\n");
   	 	 return -1;
   	    }
   	    
   	  if(settableID(dBManager,i)==0)
   	    {
   	   	 return -1;
   	    }
   	   
         return i;
   }
   
   //����table ID
   synchronized private int settableID(DBManager dBManager,long i)
   {
   	 if(dBManager.executeUpdate("UPDATE "+tableIdTable+" SET num="+(i+1)+" WHERE type='ftp'",false)==0)	
   	 {
   	 	textArea.append("SQLException occurs when writing 'tableID'!\n");
   	 	return 0;
   	 }  
   	 return 1;
   }
    
   //�г���ǰĿ¼���б�
   private int listDir()
   {
   	 list.setSize(0);
   	 try
   	  {
		// get directory listing 
        Enumeration listing = ftpClient.getDirListing(); 

        // enumerate thru listing printing filename for each entry 
        while(listing.hasMoreElements()) 
        { 
         FtpFile file = (FtpFile)listing.nextElement(); 
         list.add(file);
        }
   	    
   	    return 1; 	       	    
   	  }catch(FtpException e){
   	 	textArea.append("��ȡĿ¼ʧ�ܣ�"+e.toString()+"\n");
   	 	return 0;
   	  }
   }

   //ȷ��IP��Χ
   private int getRange(String IP)
   {
   	 if(IP.startsWith("202.112.")||IP.startsWith("210.31.")||IP.startsWith("219.224.")||IP.startsWith("59.64.")||IP.startsWith("172.16."))
   	 {
   	 	return 1;
   	 }
   	 else
   	    return 0;
   }
   
   //��Ӽ�¼�����ݿ�
   private void addIntoDB(String dir,FtpFile file)
   {
   	 String ddir = "'" + dir + "'";
   	    	    
   	 String name = "'" + file.getFilename() + "'";
   	 String size = "" + file.getFilesize();
   	 String date = "'" + file.getDate()+"'";

   	 String property,type;
   	 
   	 if(file.isDirectory())
   	 {
   	 	property = "'d'";
   	 	type = "dir";
   	 } 
   	 else
   	 {
   	    property = "'-d'";
   	    
   	    int i = file.getFilename().lastIndexOf('.');
   	    String suffix = file.getFilename().substring(i+1);
   	    
		if(suffix.equalsIgnoreCase("jpg")||suffix.equalsIgnoreCase("gif")||suffix.equalsIgnoreCase("bmp")||
			       suffix.equalsIgnoreCase("ico")||suffix.equalsIgnoreCase("jpeg"))
		{type = "image";}
		
		else if(suffix.equalsIgnoreCase("rm")||suffix.equalsIgnoreCase("asf")||suffix.equalsIgnoreCase("rmvb")
			       ||suffix.equalsIgnoreCase("wmv")||suffix.equalsIgnoreCase("wma")||suffix.equalsIgnoreCase("avi")
			       ||suffix.equalsIgnoreCase("mp3")||suffix.equalsIgnoreCase("wav"))
		{
		  if(file.getFilesize()>8000000)
		    type = "video";
		  else
		    type = "music";
		}
		
		else if(suffix.equalsIgnoreCase("doc")||suffix.equalsIgnoreCase("txt")||suffix.equalsIgnoreCase("exl")
		       ||suffix.equalsIgnoreCase("pdf")||suffix.equalsIgnoreCase("ppt")||suffix.equalsIgnoreCase("pps")
		       ||suffix.equalsIgnoreCase("c")||suffix.equalsIgnoreCase("c++")||suffix.equalsIgnoreCase("java"))
		{type = "doc";}
		
		else if(suffix.equalsIgnoreCase("zip")||suffix.equalsIgnoreCase("rar")||suffix.equalsIgnoreCase("jar"))
		{type = "zip";}
		
		else if(suffix.equalsIgnoreCase("exe"))
		{type = "program";}
		
		else if(suffix.equalsIgnoreCase("html")||suffix.equalsIgnoreCase("htm")||suffix.equalsIgnoreCase("php")||suffix.equalsIgnoreCase("asp")||suffix.equalsIgnoreCase("aspx")||suffix.equalsIgnoreCase("jsp"))
		{type = "page";}
		
		else
		{type = "others";}
   	 }
   	 
   	 type = "'" + type + "'";
   	 
   	 int num = 0;
   	 
   	 for(int j = 0;j<dir.length();j++)
   	 {
   	   if(dir.charAt(j)=='/')	
   	 	 num += 1; 
   	 }
   	 
     String layer = "" + num;
     layer = "'" + layer + "'";
     
   	 if(myDBManager.executeUpdate("INSERT INTO "+tableName+"(layer,name,dir,property,size,date,type) VALUES("
   	          +layer+","+name+","+ddir+","+property+","+size+","+date+","+type+")",false)==0)
   	 {
   	 }
   	  //textArea.append("�������ʱ"+tableName+"��"+myDBManager.getErrorMessage()+"����SQL�쳣��\n");
   	   
   }
   
//����������   
   public void run()
   {
   	 long startTime = System.currentTimeMillis();
   	 long endTime = startTime;
   	 long total = 0 ,tableID;
   	 boolean isSurvived=false;
   	 
   	 if(connect() == 1)
   	 {
   	   //���������ݿ�,������
   	    if(myDBManager.connect()==1)
   	   {
   	   	  String s = "SELECT * FROM "+IPlistTable+" WHERE IP='" + hostName + "'";
   	   	  ResultSet rs = myDBManager.executeQuery(s,false);
   	   	  
   	   	  if(rs!=null)
   	   	  {
   	     try{
   	   	  	  while(rs.next())
   	   	  	  {
   	   	  	  	if(rs.getString("IP").equals(hostName))
   	   	  	  	{
   	   	  	  	  tableName = rs.getString("tablename");
   	   	  	  	  isSurvived = true;
   	   	  	  	}
   	   	  	  }
   	   	  	  
   	   	  	  if(isSurvived&&myDBManager.execute("DROP TABLE "+tableName+";",false)==0)
   	   	  	  {
   	   	  	  	textArea.append("DROP TABLE "+tableName+";ʧ�ܣ�\n"+myDBManager.getErrorMessage()+"\n");
   	   	  	  	return;
   	   	  	  }
   	   	  	  
   	   	    }catch(SQLException e){
   	   	      textArea.append("SQLException occurs when searching for the IP in IPlist;\n"+e.toString()+"\n");
   	   	    }
   	   	  }
   	   	  else
   	   	  {
   	   	  	isSurvived = false;
   	   	  }
   	   	  
   	   	   if(!isSurvived)
   	   	  {
   	   	   //textArea.append("new IP:"+hostName+"/n");
   	   	    
   	   	   tableID = gettableID(myDBManager);
   	   	   
   	   	   if(tableID==-1)
   	   	      return;           
  	   	   
  	   	   tableName = "f"+tableID+"th";
  	   	   
   	   	   String s1 = "INSERT INTO "+IPlistTable+"(IP,urlID,tablename,range,user,password) VALUES('"+hostName+"',"+tableID+",'"+tableName+"',"+getRange(hostName)+",'"+userName+"','"+passWord+"')";
   	   	   if(myDBManager.execute(s1,false)==0)
   	   	   { textArea.append("!"+myDBManager.getErrorMessage()+"\n");closeDBManager();return;}
   	   	  }
   	   	   
   	   	   String s2 = "CREATE TABLE "+tableName+"(ID int(11) unsigned not null auto_increment primary key, layer int(3) NOT NULL,name varchar(255) NOT NULL,dir varchar(255) NOT NULL,property varchar(2),size int(11),date varchar(15),type varchar(10)) ENGINE=InnoDB DEFAULT CHARSET=gbk";

   	   	   if(myDBManager.executeUpdate(s2,false)==0)  
   	   	   { textArea.append(myDBManager.getErrorMessage()+"\n");closeDBManager();return;}
   	   }
   	    else
       {
   	   	    textArea.append("!"+myDBManager.getErrorMessage()+"\n"); 
       	    return;
       }
       
   	   boolean isRoot = true;
   	   
   	 do{ 
   	 /*
   	     System.out.println("��ջ��Ϣ");
   	     for(int u=0;u<directoryList.size();u++)
   	     {
   	     	System.out.println(u+":"+directoryList.get(u).toString());
   	     }*/
   	      if(!isRoot)
   	     {
   	      boolean findNotNullDir = false;
   	      
   	      while(!findNotNullDir)
   	      {
   	      try{
  	       	  if(directoryList.isEmpty())
   	       	     return;
   	       	  
   	       	  String s = directoryList.elementAt(0).toString();  
   	       	  //д�����ݿ�
   	       	  addIntoDB(s,(FtpFile)(dirInfoList.elementAt(0)));
   	       	  
   	       	  //textArea.append("<<<<<Ŀ¼����"+s+"\n"+"     --"+"������"+dirInfoList.elementAt(0)+"\n"); 
   	       	  total++;  

   	       	  directoryList.remove(0);                 
   	       	  dirInfoList.remove(0); 	     	   
	       	   
   	       	  ftpClient.setDir(s);
               
              curDir = ftpClient.getDir();  
   	       	  
   	       	  findNotNullDir = true;	            //�ų��˿�Ŀ¼    	 
  	         }catch(FtpException er){
//System.out.println("error:"+er.toString());
  	          findNotNullDir = false;
  	         }
   	      }//end while
   	     }//end if   	      
   	     
   	      if(listDir() == 1)
   	     {
   	   	    int j = list.size();
   	   	    //����Ŀ¼���ļ�
   	   	    for(int i = 0;i<j;i++)
   	   	   {
   	   	   	 FtpFile file = (FtpFile)(list.elementAt(i));
   	   	      //�����ļ�
   	   	     if(!file.isDirectory())
   	   	    {
   	   	   	  String fileName = file.getFilename();
   	   	   	  //textArea.append("*****�ļ�����"+curDir+"/"+fileName+"\n"+"     --"+"������"+file.toString()+"\n");  
   	   	   	  //д�����ݿ�
   	   	   	  addIntoDB(curDir+"/"+fileName,file);
   	   	   	  total++;
   	   	    }
   	   	     //����Ŀ¼
   	   	     else	  
   	   	    {   	   	   	  
   	   	  	  String fileDir = file.getFilename();
   	   	  	  if(!fileDir.equals(".")&&!fileDir.equals(".."))
   	   	  	  {
   	   	  	  	String s = curDir+"/"+fileDir;   	   	  	  		
   	   	  	  	  
   	   	  	  	dirInfoList.add(0,file);
   	   	  	  	directoryList.add(0,s);
   	   	  	  }
   	   	    } 	   	   
   	   	  }//end for
   	    } //end if 
   	    //ȥ����Ŀ¼��־
   	    isRoot = false;                       
   	   }while(directoryList.isEmpty()!=true);
   	   
   	   //�ر�FTP���� 
   	   ftpClient.disconnect();   	   	 	
   	   	  
   	   //ͳ����Ϣ
   	   java.util.Date curDate = new java.util.Date(System.currentTimeMillis());
   	   if(myDBManager.executeUpdate("UPDATE IPlist SET listnum="+total+",updatetime='"+curDate.toLocaleString()+"' WHERE IP='"+hostName+"'",false)==0)
   	   {
   	   	  textArea.append("UPDATE IPlist SET user='"+userName+"',password='"+passWord+"',listnum="+total+",updatetime='"+curDate.toLocaleString()+"' WHERE IP='"+hostName+"'ʧ��\n"+myDBManager.getErrorMessage()+"\n");
   	   }


   	   endTime = System.currentTimeMillis();
   	   textArea.append("IP:"+hostName+":ͳ�ƣ��Ѿ��ҵ� "+ total +" ����¼;��ʱ: "+ (endTime-startTime)/1000 +" �롣\n"); 

   	   //�ر����ݿ����� 
   	   closeDBManager();
   	   
   	 }//end if 
   	 
   }//end run
   
 }