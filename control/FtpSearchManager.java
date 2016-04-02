//总控制台

package	control;
 
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import structs.DBProperty;;

public class FtpSearchManager extends JFrame implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JPanel pProductMsg	=new JPanel();
	JPanel pButtonCtr	=new JPanel();
	JPanel pManageMsg	=new JPanel();
	JPanel pGenMsg		=new JPanel();
	
	JButton	btHelp		=new JButton("帮助");
	JLabel	lbTitle		=new JLabel("Ftp搜索引擎"),
			lbIconTt	=new JLabel(new ImageIcon("./Icon./title.jpg"));
	
	DBProperty	 	myDBProperty;
	IPManager		pIPManager; 
	IndexManager	pIndexManager; 
	WatcherManager	pWatcherManager;
	DBManagerPanel  pDBManager;
	
	JButton btIpSet		=new JButton("服务器搜索"),
			btIndexSet	=new JButton("更新索引"),
			btService	=new JButton("FTP状态监视"),
			btDBSet		=new JButton("数据库管理");
	  	
   	public FtpSearchManager(){
   		super("Ftp搜索引擎");
   		this.setBounds(200,100,650,480);//固定主窗口大小;
   		this.setResizable(false);
   	
   		myDBProperty = new DBProperty();
   		myDBProperty.setDriverName("com.mysql.jdbc.Driver");
   		myDBProperty.setUrl("jdbc:mysql://localhost:3306/ftpinfo?user=root&password=163150");
   		myDBProperty.setUser("root");
   		myDBProperty.setPassword("163150");
   		
	 	pIPManager		=new IPManager(myDBProperty);
		pWatcherManager	=new WatcherManager(myDBProperty);
		pDBManager		=new DBManagerPanel(myDBProperty);
		pIndexManager	=new IndexManager(myDBProperty);

   				
  		addComponent();
  		  		
  		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});
  	}
  	private void addComponent(){	//向主窗口添加组件;
  		Container  contentPane=getContentPane();
  		contentPane.setLayout(null);
  		
  		contentPane.setBackground(new Color(255,255,235));
  			
  		setpPrMsg();
  		contentPane.add(pProductMsg);
  		setpButtonCtr();
  		contentPane.add(pButtonCtr);
  		setpManMsg();
  		contentPane.add(pManageMsg);
  		setpGenMsg();
  		contentPane.add(pGenMsg);
  	}
  	private void setpPrMsg(){		//设定信息栏;
  		pProductMsg.setBounds(10,10,625,50);
  		pProductMsg.setLayout(null);
  		pProductMsg.setBorder(new EtchedBorder());
  		pProductMsg.setBackground(Color.white);
  		btHelp.setBounds(550,10,60,25);
  		
  		btHelp.setBackground(new Color(255,255,235));
  		btHelp.setBorder(new BevelBorder(BevelBorder.RAISED));
  		btHelp.setForeground(Color.blue);
  		pProductMsg.add(btHelp);
  		
  		lbTitle.setBounds(300,10,200,25);
  		pProductMsg.add(lbTitle);
  		
  		lbIconTt.setBounds(-150,3,450,45);
  		pProductMsg.add(lbIconTt);
  	}
  	private void setpButtonCtr(){	//设定按钮栏；
  		pButtonCtr.setBounds(10,70,200,210);
  		pButtonCtr.setBorder(new EtchedBorder());
  		pButtonCtr.setBackground(Color.white);
  		
  		pButtonCtr.setLayout(null);
  		
  		btIpSet.setBounds(20,30,160,30);
  		btIpSet.setBackground(new Color(255,255,235));
  		btIpSet.setBorder(new BevelBorder(BevelBorder.RAISED));
  		btIpSet.setForeground(Color.blue);
  		btIpSet.addActionListener(this);
  		pButtonCtr.add(btIpSet);
  		btIndexSet.setBounds(20,70,160,30);
  		btIndexSet.setBackground(new Color(255,255,235));
  		btIndexSet.setBorder(new BevelBorder(BevelBorder.RAISED));
  		btIndexSet.setForeground(Color.blue);
  		btIndexSet.addActionListener(this);
  		pButtonCtr.add(btIndexSet);
  		btService.setBounds(20,110,160,30);
  		btService.setBackground(new Color(255,255,235));
  		btService.setBorder(new BevelBorder(BevelBorder.RAISED));
  		btService.setForeground(Color.blue);
  		btService.addActionListener(this);
  		pButtonCtr.add(btService);
  		btDBSet.setBounds(20,150,160,30);
  		btDBSet.setBackground(new Color(255,255,235));
  		btDBSet.setBorder(new BevelBorder(BevelBorder.RAISED));
  		btDBSet.setForeground(Color.blue);
  		btDBSet.addActionListener(this);
  		pButtonCtr.add(btDBSet);
  	}
  	private void setpManMsg(){		//设定信息栏；
  		pManageMsg.setBounds(220,70,415,350);
  		
  		pManageMsg.setLayout(new CardLayout());
  		pManageMsg.add("IP",pIPManager);
  		pManageMsg.add("ID",pIndexManager);
  		pManageMsg.add("WT",pWatcherManager);
  		pManageMsg.add("DB",pDBManager);
  		((CardLayout)pManageMsg.getLayout()).show(pManageMsg,"IP");
  	}
  	private void setpGenMsg(){
  		pGenMsg.setBounds(10,290,200,130);
  		pGenMsg.setBorder(new EtchedBorder());
  		pGenMsg.setBackground(Color.white);
  	}
  	//############################################################
  	public void actionPerformed(ActionEvent e){
  		if(e.getSource()==btIpSet)
  		{  			
  			((CardLayout)pManageMsg.getLayout()).show(pManageMsg,"IP");
  		}
  		else if(e.getSource()==btIndexSet)
  		{
  			((CardLayout)pManageMsg.getLayout()).show(pManageMsg,"ID");
  			pIndexManager.cbAllIP.setVisible(true);
  			pIndexManager.cbSigIP.setVisible(true);
  		}
  		else if(e.getSource()==btService)
  		{
  			((CardLayout)pManageMsg.getLayout()).show(pManageMsg,"WT");
  			pWatcherManager.cbIsApply.setVisible(true);
  		}
  		else if(e.getSource()==btDBSet)
  		{
  			((CardLayout)pManageMsg.getLayout()).show(pManageMsg,"DB");
  		}
  		else
  		{
  		}
  	}
  	//############################################################
 /* 	public static void main(String[] arg){
  		FtpSearchManager fsm=new FtpSearchManager();
  		fsm.show();
  	}*/
}