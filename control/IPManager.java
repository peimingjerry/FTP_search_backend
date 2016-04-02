//IPManager的作用在于根据管理员的设置，开启相关线程进行Ftp服务器的搜索，
//显示当前搜索器的工作状态，并将管理员的设置保存；
package control;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.Vector;

import structs.DBProperty;
import structs.ThreadCounter;
import robot.SearchAllThread;
import robot.SigIPThread;
import robot.MultiIPThread;

public class IPManager extends JPanel implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JPanel  pSigIP		=new JPanel(),
			pMulIP		=new JPanel(),
			pDetail		=new JPanel(),
			pUpdate		=new JPanel(),
			pState		=new JPanel();
	JButton btSigIP		=new JButton("搜索"),
			btMulIP		=new JButton("搜索"),
			btDetail	=new JButton("详细信息"),
			btUpdate	=new JButton("立即更新");
	JLabel	lbSigIP		=new JLabel(new String("对某一个单独的IP进行搜索；")), 
			lbMulIP		=new JLabel(new String("对一个连续的IP段进行搜索；")),
			lbUpdate	=new JLabel(new String("根据数据库中记录的IP,将对应站点的数据进行更新；")),
			lbThreadNum	=new JLabel("启用线程数"),
			lbState 	=new JLabel();
	JTextField	tfThreadNum	=new JTextField(10);		   

			
	DlgDetail  detail 	=new DlgDetail(new Frame());
	DBProperty myDBProperty;
		
	public IPManager(DBProperty myDBProperty){
		this.setLayout(null);
		this.setBackground(Color.white);
		this.setBorder(new EtchedBorder());
		this.myDBProperty = myDBProperty;
		
		setpSigIP();
		add(pSigIP);
		setpMulIP();
		add(pMulIP);
		setpDetail();
		add(pDetail);
		setpUpdate();
		add(pUpdate);
		lbState.setText("以上的第三项将删除所有的站点信息，请慎重使用！");
		setpState();
		add(pState);
	}
	private void setpSigIP(){
		pSigIP.setBounds(40,30,340,50);
		pSigIP.setBorder(new EtchedBorder());
		pSigIP.setBackground(Color.white);
		pSigIP.setLayout(null);
		lbSigIP.setBounds(10,10,200,20);
		pSigIP.add(lbSigIP);
		btSigIP.setBounds(220,8,100,25);
		btSigIP.setBackground(new Color(255,255,235));
  		btSigIP.setBorder(new BevelBorder(BevelBorder.RAISED));
  		btSigIP.setForeground(Color.blue);
  		btSigIP.addActionListener(this);
		pSigIP.add(btSigIP);
	}
	private void setpMulIP(){
		pMulIP.setBounds(40,95,340,50);
		pMulIP.setBorder(new EtchedBorder());
		pMulIP.setBackground(Color.white);
		pMulIP.setLayout(null);
		lbMulIP.setBounds(10,10,200,20);
		pMulIP.add(lbMulIP);
		btMulIP.setBounds(220,8,100,25);
		btMulIP.setBackground(new Color(255,255,235));
  		btMulIP.setBorder(new BevelBorder(BevelBorder.RAISED));
  		btMulIP.setForeground(Color.blue);
  		btMulIP.addActionListener(this);
		pMulIP.add(btMulIP);
	}
	
	private void setpUpdate(){
		pUpdate.setBounds(40,160,340,80);
		pUpdate.setBorder(new EtchedBorder());
		pUpdate.setBackground(Color.white);
		pUpdate.setLayout(null);
		lbUpdate.setBounds(10,10,320,20);
		pUpdate.add(lbUpdate);
		lbThreadNum.setBounds(10,25,90,50);
		pUpdate.add(lbThreadNum);
		tfThreadNum.setBounds(80,40,120,20);
		pUpdate.add(tfThreadNum);
		btUpdate.setBounds(220,35,100,25);
		btUpdate.setBackground(new Color(255,255,235));
  		btUpdate.setBorder(new CompoundBorder(new EtchedBorder(),new LineBorder(Color.red)));
  		btUpdate.setForeground(Color.red);
  		btUpdate.addActionListener(this);
		pUpdate.add(btUpdate);
	}
	
	private void setpDetail(){
		pDetail.setBounds(40,240,340,30);
		pDetail.setBackground(Color.white);
		pDetail.setLayout(null);
		btDetail.setBounds(220,5,100,25);
		btDetail.setBackground(new Color(255,255,235));
  		btDetail.setBorder(new BevelBorder(BevelBorder.RAISED));
  		btDetail.setForeground(new Color(255,100,0));
  		btDetail.addActionListener(this);
		pDetail.add(btDetail);
	}
		
	private void setpState(){
		pState.setBounds(40,270,340,60);
		pState.setBorder(new TitledBorder("重要提示！"));
		pState.setBackground(Color.white);
		pState.add(lbState);
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource()==btSigIP)
		{
			new DlgSigIP(new Frame(),myDBProperty,detail.taDetail,"单IP搜索",true);
		}
		else if(e.getSource()==btMulIP)
		{
			new DlgMulIP(new Frame(),myDBProperty,detail.taDetail,"IP段搜索",true);
		}
		else if(e.getSource()==btDetail)
		{
			detail.setVisible(true);
		}
		else if(e.getSource()==btUpdate)
		{
		   if(!(tfThreadNum.getText().equals("")))
		   {
System.out.println("----");	
System.out.println(tfThreadNum.getText());		   	
		   	  int t = (new Integer(tfThreadNum.getText().trim())).intValue();
		   	  if((t>0)&&(t<8))
		   	  {
		   	  	 ThreadCounter threadCounter = new ThreadCounter();
		   	  	 threadCounter.setSize(t);
			     new SearchAllThread(myDBProperty,detail.taDetail,new Vector(),threadCounter).start();
		   	  }
		   }
		   else{
		   }
		}
		else
		{
		}
	}
	class BtLUpdate implements ActionListener{
		public void actionPerformed(ActionEvent e){
			Frame f=new Frame();
			new DlgUpdate(f,"更新设置",true);
		}
	}
}

//#################################################################
class DlgSigIP extends JDialog implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JLabel lbIP			=new JLabel("IP"),
		   lbPort		=new JLabel("端口"),
		   lbName		=new JLabel("用户名"),
		   lbPassword	=new JLabel("密码");
	JTextField tfIP		=new JTextField(15),
			   tfPort	=new JTextField(6),
			   pfPw	    =new JTextField(40),
			   tfName	=new JTextField(20);
	JButton	   btOK		=new JButton("确定"),
			   btCancel	=new JButton("取消");
	JTextArea   textArea;
	DBProperty myDBProperty;
			   
   	public DlgSigIP(Frame owner,DBProperty myDBProperty,JTextArea textArea, String title, boolean modal){
		super(owner,title,modal);
		Container dlgPane=getContentPane();
		
		this.textArea = textArea;
		this.myDBProperty = myDBProperty;
		
		tfPort.setText("21");
	    tfName.setText("anonymous");
		pfPw.setText("zpm@163.com");
		
		this.setBounds(400,250,320,200);
		dlgPane.setBackground(Color.white);
		dlgPane.setLayout(null);
		
		lbIP.setBounds(10,10,40,20);
		dlgPane.add(lbIP);
		tfIP.setBounds(60,10,100,20);
		dlgPane.add(tfIP);
		lbPort.setBounds(180,10,40,20);
		dlgPane.add(lbPort);
		tfPort.setBounds(210,10,50,20);
		dlgPane.add(tfPort);
		lbName.setBounds(10,40,40,20);
		dlgPane.add(lbName);
		tfName.setBounds(60,40,200,20);
		dlgPane.add(tfName);
		lbPassword.setBounds(10,70,100,20);
		dlgPane.add(lbPassword);
		pfPw.setBounds(60,70,200,20);
		dlgPane.add(pfPw);
		
		
		btOK.setBounds(120,120,60,25);
		btOK.setBackground(new Color(255,255,235));
  		btOK.setBorder(new BevelBorder(BevelBorder.RAISED));
  		btOK.setForeground(Color.blue);
  		btOK.addActionListener(this);
		dlgPane.add(btOK);
		btCancel.setBounds(200,120,60,25);
		btCancel.setBackground(new Color(255,255,235));
  		btCancel.setBorder(new BevelBorder(BevelBorder.RAISED));
  		btCancel.setForeground(Color.blue);
  		btCancel.addActionListener(this);
		dlgPane.add(btCancel);
		
		this.show();
	}
	//#############################################################
	public String[] getIPMSG()
	{
		String[]	IPMSG=new String[4];
		IPMSG[0]=new String(tfIP.getText());
		IPMSG[1]=new String(tfName.getText());
		IPMSG[2]=new String(pfPw.getText());
		IPMSG[3]=new String(tfPort.getText());
		
		return	IPMSG;
	}
	//############################################################
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource()==btOK)
		{
			new SigIPThread(getIPMSG(),myDBProperty,textArea).start();
			this.dispose();
		}
		else if(e.getSource()==btCancel)
		{
			this.dispose();
		}
		else
		{
		}
	}	
}

//******************************************************************************
class DlgMulIP extends JDialog implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JLabel lbIP			=new JLabel("IP"),
		   lbTo			=new JLabel("--"),
		   lbThNum		=new JLabel("线程数"),
		   lbPort		=new JLabel("端口"),
		   lbName		=new JLabel("用户名"),
		   lbPassword	=new JLabel("密码");
	JTextField tfIPFrom	=new JTextField(15),
			   tfIPEnd	=new JTextField(15),
			   tfThNum	=new JTextField(10),
			   tfPort	=new JTextField(6),
			   tfName	=new JTextField(20),
			   pfPw     =new JTextField(40);
	JButton	   btOK		=new JButton("确定"),
			   btCancel	=new JButton("取消");
	JTextArea  taDetail;
	DBProperty myDBProperty;
	
	public DlgMulIP(Frame owner,DBProperty myDBProperty, JTextArea taDetail,String title, boolean modal){
		super(owner,title,modal);
		Container dlgPane=getContentPane();
		
		this.taDetail = taDetail;
		this.myDBProperty = myDBProperty;
		
		tfPort.setText("21");
	    tfName.setText("anonymous");
		pfPw.setText("zpm@163.com");
		tfIPFrom.setText("172.16.124.1");
		tfIPEnd.setText("172.16.124.10");

		this.setBounds(400,250,320,200);
		dlgPane.setBackground(Color.white);
		dlgPane.setLayout(null);
		
		lbIP.setBounds(10,10,40,20);
		dlgPane.add(lbIP);
		tfIPFrom.setBounds(60,10,100,20);
		dlgPane.add(tfIPFrom);
		lbTo.setBounds(160,10,10,20);
		dlgPane.add(lbTo);
		tfIPEnd.setBounds(170,10,100,20);
		dlgPane.add(tfIPEnd);
		
		lbThNum.setBounds(10,40,40,20);
		dlgPane.add(lbThNum);
		tfThNum.setBounds(60,40,50,20);
		dlgPane.add(tfThNum);

		lbPort.setBounds(130,40,40,20);
		dlgPane.add(lbPort);
		tfPort.setBounds(170,40,50,20);
		dlgPane.add(tfPort);
		lbName.setBounds(10,70,40,20);
		dlgPane.add(lbName);
		tfName.setBounds(60,70,210,20);
		dlgPane.add(tfName);
		lbPassword.setBounds(10,100,100,20);
		dlgPane.add(lbPassword);
		pfPw.setBounds(60,100,210,20);
		dlgPane.add(pfPw);
		
		
		btOK.setBounds(120,130,60,25);
		btOK.setBackground(new Color(255,255,235));
  		btOK.setBorder(new BevelBorder(BevelBorder.RAISED));
  		btOK.setForeground(Color.blue);
  		btOK.addActionListener(this);
		dlgPane.add(btOK);
		btCancel.setBounds(200,130,60,25);
		btCancel.setBackground(new Color(255,255,235));
  		btCancel.setBorder(new BevelBorder(BevelBorder.RAISED));
  		btCancel.setForeground(Color.blue);
  		btCancel.addActionListener(this);
		dlgPane.add(btCancel);
		
		this.show();
	}
	//############################################################
	
	public String[] getIPMSG()
	{
		String[]	IPMSG=new String[5];
		
		IPMSG[0]=new String(tfIPFrom.getText()+"\n"+tfIPEnd.getText());
		IPMSG[1]=new String(tfName.getText());
		IPMSG[2]=new String(pfPw.getText());
		IPMSG[3]=new String(tfPort.getText());
		IPMSG[4]=new String(tfThNum.getText());
		
		return	IPMSG;
	}
	//############################################################

	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource()==btOK)
		{
			new MultiIPThread(getIPMSG(),myDBProperty,taDetail,new Vector()).start();
			this.dispose();
		}
		else if(e.getSource()==btCancel)
		{
			this.dispose();
		}
		else
		{
		}
	}
}
//******************************************************************************
 
class DlgDetail extends JDialog implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JPanel	   pStatistic 	=new JPanel(),
			   pDetail		=new JPanel();
	JLabel	   lbStatistic 	=new JLabel();
	JTextArea  taDetail		=new JTextArea();
	JButton	   btOK		    =new JButton("关闭面板"); 
	JButton	   btClear		=new JButton("清空面板"); 
	
	public DlgDetail(Frame owner){
		super(owner,"详细信息：");
		Container dlgPane=getContentPane();
		
		this.setBounds(150,200,700,500);
		dlgPane.setBackground(Color.white);
		dlgPane.setLayout(null);
		setVisible(false);
		
		//taDetail.setAutoscrolls(true);
		taDetail.setSize(600,200);
		
		pStatistic.setBounds(10,20,670,60);
		pStatistic.setBorder(new TitledBorder("统计"));
		pStatistic.setBackground(Color.white);
		pStatistic.add(lbStatistic);
		dlgPane.add(pStatistic);
		
		pDetail.setBounds(10,100,670,320);
		pDetail.setBorder(new TitledBorder("详细信息"));
		pDetail.setBackground(Color.white);
		pDetail.setLayout(new BorderLayout());
		pDetail.setAutoscrolls(false);
		pDetail.add("Center",taDetail);
		dlgPane.add(pDetail);
		
		btOK.setBounds(540,430,60,25);
		btOK.setBackground(new Color(255,255,235));
  		btOK.setBorder(new BevelBorder(BevelBorder.RAISED));
  		btOK.setForeground(Color.blue);
  		btOK.addActionListener(this);
		dlgPane.add(btOK);
	}
	
	//############################################################
	public void actionPerformed(ActionEvent e)
	{
			this.dispose();
	}

	}