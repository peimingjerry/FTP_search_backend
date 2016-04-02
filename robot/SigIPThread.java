//单IP搜索

package robot;

import javax.swing.JTextArea;

import structs.DBProperty;

public class	SigIPThread  extends Thread{
	static 	int[] 	intIP;
	String[]		IPMSG;
	JTextArea		taDetail;
	DBProperty      myDBProperty;
	
	public SigIPThread(String[] ipMsg,DBProperty myDBProperty,JTextArea taMSG)
	{
		this.IPMSG=ipMsg;
		this.taDetail=taMSG	;
	    this.myDBProperty = myDBProperty;		
	}
	
	private void initIntIP()  //put the string data IP in to type  int;
	{
		int[]	sep		=	new	int[4];
		try{
				String 	strIP =new String(IPMSG[0]+".0.0.0.0");	//防止无效的输入；
				//??????????????????????????????????????
				if(strIP.charAt(0)=='.') 
					return	;

				int i=0,j=0;
				for(;j<strIP.length()&&i<=3;j++){
					if(strIP.charAt(j)=='.')
						sep[i++]=j;
				}
				intIP=new int[]{(new Integer(strIP.substring(0,sep[0]))).intValue(),
								(new Integer(strIP.substring(sep[0]+1,sep[1]))).intValue(),
								(new Integer(strIP.substring(sep[1]+1,sep[2]))).intValue(),
								(new Integer(strIP.substring(sep[2]+1,sep[3]))).intValue()
							};
			}
			catch(Exception e){
				
			}
	}
	
	public void run(){
//System.out.println("Sig线程");
			SearchThread thread;
			
			initIntIP();
			String IP=getIP();
			
			if(IP==null)
			{//????????????????????????????????????????????????????????????????????????????????
			}
			thread = new SearchThread(IP.trim(),taDetail,IPMSG[1].trim(),IPMSG[2].trim(),myDBProperty.getDriverName(),myDBProperty.getUrl(),myDBProperty.getUser(),myDBProperty.getPassword());
			thread.start();
				
			try{
					sleep(10000);
				}catch(InterruptedException e){
	
			}
	}
	
	private String getIP()//judge if the IP is wrong.
	{
		
		if(intIP[0]>=256||intIP[1]>=256||intIP[2]>=256||intIP[3]>=256)	return	null;
		String	strIP	=	new String(intIP[0]+"."+intIP[1]+"."+intIP[2]+"."+intIP[3]);
		
		return strIP;
	}
		
}