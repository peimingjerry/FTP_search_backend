//控制多IP搜索

package robot;

import javax.swing.*;
import java.util.*;

import structs.DBProperty;

public class	MultiIPThread  	extends Thread{
	static 	int[] 	intIPFrom;
	static 	int[] 	intIPEnd;
	int				threadNum;
	String[]		IPMSG;
	JTextArea		taDetail;
	Vector 			threadVector;
	DBProperty      myDBProperty;
	
	public MultiIPThread(String[] ipMsg,DBProperty myDBProperty,JTextArea taMSG,Vector theVector)
	{
		this.IPMSG=ipMsg;
		this.taDetail=taMSG;
		this.threadVector = theVector;
		this.myDBProperty = myDBProperty;
		
		threadNum=(new Integer(ipMsg[4])).intValue(); 
		initIntIP();
	
	//////////////////??????????????????????????
		for(int i=0;i<4;i++){
		  if(intIPFrom[i]<0||intIPFrom[i]>255)
		 {
		   System.out.println("Wrong From IP!");
		   
		   return;
		 }
		  if(intIPEnd[i]<0||intIPEnd[i]>255)
		 {
		   System.out.println("Wrong End IP!");
					
		   return;
		  }
		}
	}
	
	private void initIntIP()  //put the string data IP in to type  int;
	{
		int[]	sep		=	new	int[4];
		try{
				sep[0]	=IPMSG[0].indexOf('\n');
				String 	strFrom	=new String(IPMSG[0].substring(0,sep[0])+".0.0.0.0");					//防止无效的输入；
				String	strEnd	=new String(IPMSG[0].substring(sep[0]+1,IPMSG[0].length())+".0.0.0.0");
		
				//??????????????????????????????????????????????????????
				if(strFrom.charAt(0)=='.'||strEnd.charAt(0)=='.') 
					return	;

				int i=0,j=0;
				for(;j<strFrom.length()&&i<=3;j++){
					if(strFrom.charAt(j)=='.')
						sep[i++]=j;
				}
				intIPFrom =new int[]{(new Integer(strFrom.substring(0,sep[0]))).intValue(),
									 (new Integer(strFrom.substring(sep[0]+1,sep[1]))).intValue(),
									 (new Integer(strFrom.substring(sep[1]+1,sep[2]))).intValue(),
								  	 (new Integer(strFrom.substring(sep[2]+1,sep[3]))).intValue()
									};
				i=0;
				for(j=0;j<strEnd.length()&&i<=3;j++){
					if(strEnd.charAt(j)=='.')
						sep[i++]=j;
				}		
				intIPEnd=new int[]{	(new Integer(strEnd.substring(0,sep[0]))).intValue(),
									(new Integer(strEnd.substring(sep[0]+1,sep[1]))).intValue(),
									(new Integer(strEnd.substring(sep[1]+1,sep[2]))).intValue(),
									(new Integer(strEnd.substring(sep[2]+1,sep[3]))).intValue()
				};
										
			}
			catch(Exception e){
				
			}
	}
	
	public void run(){
System.out.println("MULT新线程");
			SearchThread thread;
			String IP=getIP();
			
System.out.print(threadNum);			
			for(int i=0;i<threadNum;i++)//初始化多线程 
			{
				
System.out.println("新线程在访问ip："+IP.trim());				
				thread = new SearchThread(IP.trim(),taDetail,IPMSG[1].trim(),IPMSG[2].trim(),myDBProperty.getDriverName(),myDBProperty.getUrl(),myDBProperty.getUser(),myDBProperty.getPassword());
				thread.start();
				threadVector.add(thread);
				
				IP=getIP();
				if(IP==null) 
					break;
			}
			while(IP!=null){	//失败后重试？？？
				
				for(int i=0;i<threadNum;i++)
				{
				   if(!((Thread)(threadVector.elementAt(i))).isAlive())
				   {
				   	 threadVector.remove(i);
				   	 
System.out.println("新线程在访问ip："+IP.trim());				     
				     thread = new SearchThread(IP.trim(),taDetail,IPMSG[1].trim(),IPMSG[2].trim(),myDBProperty.getDriverName(),myDBProperty.getUrl(),myDBProperty.getUser(),myDBProperty.getPassword());
				     thread.start();
				     threadVector.add(thread);
				
				     IP=getIP();
				     if(IP==null) 
					   break;
				   }
				}
				try{
					sleep(10000);
				}catch(InterruptedException e){
				}
			}
	}
	
	private String getIP()//judge if the IP is wrong.
	{
		//????????????
		if(intIPFrom[0]==256)	return	null;
		if(intIPFrom[0]>intIPEnd[0]){System.out.println("IPFrom is larger than IPEnd.");  return null;}
		if(intIPFrom[0]==intIPEnd[0]&&intIPFrom[1]>intIPEnd[1]){System.out.println("IPFrom is larger than IPEnd.");	return null;}
		if(intIPFrom[0]==intIPEnd[0]&&intIPFrom[1]==intIPEnd[1]&&intIPFrom[2]>intIPEnd[2]){System.out.println("IPFrom is larger than IPEnd."); return null;}
		if(intIPFrom[0]==intIPEnd[0]&&intIPFrom[1]==intIPEnd[1]&&intIPFrom[2]==intIPEnd[2]&&intIPFrom[3]>intIPEnd[3]){System.out.println("IPFrom is larger than IPEnd."); return null;}	 

		String	strIP	=	new String(intIPFrom[0]+"."+intIPFrom[1]+"."+intIPFrom[2]+"."+intIPFrom[3]);
		
System.out.println(strIP);
		intIPFrom[3]++;
		
		if(intIPFrom[3]>255) 
		{
			intIPFrom[3]=0;
			intIPFrom[2]++;
		}
		if(intIPFrom[2]>255) 
		{
			intIPFrom[2]=0;
			intIPFrom[1]++;
		}
		if(intIPFrom[1]>255) 
		{
			intIPFrom[1]=0;
			intIPFrom[0]++;
		}
		
		return	strIP;	
	}
}