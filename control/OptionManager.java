package control;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import structs.DBProperty;

public class OptionManager extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JPanel  pUpdate		=new JPanel(),
			pVigil		=new JPanel(),
			pStatistic	=new JPanel(),
			pState		=new JPanel(),
			pDetail		=new JPanel();
	JButton btUpdate	=new JButton("设置"),
			btVigil		=new JButton("设置"),
			btDetail	=new JButton("详细信息");
	JLabel	lbUpdate	=new JLabel(new String("根据数据库中记录的IP，将数据")),
			lbUpdateB	=new JLabel(new String("库中记录的内容进行更新；")),
			lbVigil		=new JLabel(new String("监测数据库中记载的服务器当前")),
			lbVigilB	=new JLabel(new String("是否在线 ；")),
			lbState 	=new JLabel(),
			lbStateB 	=new JLabel(),
			lbStRecord	=new JLabel(),
			lbStServer	=new JLabel();
	DBProperty myDBProperty;
	
	public OptionManager(DBProperty myDBProperty){
		this.setLayout(null);
		this.setBackground(Color.white);
		this.setBorder(new EtchedBorder());
		this.myDBProperty = myDBProperty;
		
		setpUpdate();
		add(pUpdate);
		setpVigil();
		add(pVigil);
		setpDetail();
		add(pDetail);
		setpStatistic();
		add(pStatistic);
		setpState();
		add(pState);
		
	}
	private void setpUpdate(){
		pUpdate.setBounds(40,30,340,60);
		pUpdate.setBorder(new EtchedBorder());
		pUpdate.setBackground(Color.white);
		pUpdate.setLayout(null);
		lbUpdate.setBounds(10,10,200,20);
		pUpdate.add(lbUpdate);
		lbUpdateB.setBounds(10,30,200,20);
		pUpdate.add(lbUpdateB);
		btUpdate.setBounds(220,8,100,25);
		btUpdate.setBackground(new Color(255,255,235));
  		btUpdate.setBorder(new BevelBorder(BevelBorder.RAISED));
  		btUpdate.setForeground(Color.blue);
  		btUpdate.addActionListener(new BtLUpdate());
		pUpdate.add(btUpdate);
	}
	private void setpVigil(){
		pVigil.setBounds(40,100,340,60);
		pVigil.setBorder(new EtchedBorder());
		pVigil.setBackground(Color.white);
		pVigil.setLayout(null);
		lbVigil.setBounds(10,10,200,20);
		pVigil.add(lbVigil);
		lbVigilB.setBounds(10,30,200,20);
		pVigil.add(lbVigilB);
		btVigil.setBounds(220,8,100,25);
		btVigil.setBackground(new Color(255,255,235));
  		btVigil.setBorder(new BevelBorder(BevelBorder.RAISED));
  		btVigil.setForeground(Color.blue);
  		btVigil.addActionListener(new BtLVigil());
		pVigil.add(btVigil);
	}
	private void setpDetail(){
		pDetail.setBounds(40,160,340,30);
		pDetail.setBackground(Color.white);
		pDetail.setLayout(null);
		btDetail.setBounds(220,5,100,20);
		btDetail.setBackground(new Color(255,255,235));
  		btDetail.setBorder(new BevelBorder(BevelBorder.RAISED));
  		btDetail.setForeground(new Color(255,100,0));
  		btDetail.addActionListener(new BtLDetail());
		pDetail.add(btDetail);
	}
	private void setpState(){
		pState.setLayout(null);
		pState.setBounds(40,190,340,70);
		pState.setBorder(new TitledBorder("状态"));
		pState.setBackground(Color.white);
		lbState.setText("当前未进行更新");
		lbState.setBounds(100,15,300,20);
		pState.add(lbState);
		lbStateB.setText("实时监测已禁用");
		lbStateB.setBounds(100,40,300,20);
		pState.add(lbStateB);
	}
	private void setpStatistic(){
		pStatistic.setLayout(null);
		pStatistic.setBounds(40,270,340,70);
		pStatistic.setBorder(new TitledBorder("统计"));
		pStatistic.setBackground(Color.white);
		lbStRecord.setBounds(10,15,200,20);
		lbStRecord.setText("当前共记录文件数");
		pStatistic.add(lbStRecord);
		lbStServer.setBounds(10,40,200,20);
		lbStServer.setText("当前在线服务器数");
		pStatistic.add(lbStServer);
	}
	//##############################################################
	class BtLUpdate implements ActionListener{
		public void actionPerformed(ActionEvent e){
			Frame f=new Frame();
			new DlgUpdate(f,"更新设置",true);
		}
	}
	class BtLVigil implements ActionListener{
		public void actionPerformed(ActionEvent e){
			Frame f=new Frame();
			new DlgVigil(f,"实时监测",true);
		}
	}
	class BtLDetail implements ActionListener{
		public void actionPerformed(ActionEvent e){
			Frame f=new Frame();
			new DlgDetail1(f,"详细信息",true);
		}
	}
	//##############################################################
}


class DlgUpdate extends JDialog{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Checkbox	cbIsAuto	=new Checkbox("  启用自动更新");
	JLabel lbTimeSpan		=new JLabel("时间间隔"),
		   lbThreadNum		=new JLabel("启用线程数");
	JTextField tfTimeSpan	=new JTextField(10),
			   tfThreadNum	=new JTextField(10);		   
	JButton	   btUpdate		=new JButton("立即更新"),
			   btOK			=new JButton("确定"),
			   btCancel		=new JButton("取消");
	
	public DlgUpdate(Frame owner, String title, boolean modal){
		super(owner,title,modal);
		Container dlgPane=getContentPane();
		
		this.setBounds(400,250,320,200);
		dlgPane.setBackground(Color.white);
		dlgPane.setLayout(null);
		
		cbIsAuto.setBounds(10,10,100,20);
		cbIsAuto.addItemListener(new CbLUpdate());
		dlgPane.add(cbIsAuto);
	
		lbTimeSpan.setBounds(10,40,60,20);
		dlgPane.add(lbTimeSpan);
		tfTimeSpan.setBounds(80,40,200,20);
		tfTimeSpan.setEnabled(false);
		dlgPane.add(tfTimeSpan);
		lbThreadNum.setBounds(10,70,100,20);
		dlgPane.add(lbThreadNum);
		tfThreadNum.setBounds(80,70,200,20);
		tfThreadNum.setEnabled(false);
		dlgPane.add(tfThreadNum);
		
		btUpdate.setBounds(180,10,100,25);
		btUpdate.setBackground(new Color(255,255,235));
  		btUpdate.setBorder(new CompoundBorder(new EtchedBorder(),new LineBorder(Color.red)));
  		btUpdate.setForeground(Color.red);
  		btUpdate.addActionListener(new BtLImUpdate());
		dlgPane.add(btUpdate);
		btOK.setBounds(120,130,60,25);
		btOK.setBackground(new Color(255,255,235));
  		btOK.setBorder(new BevelBorder(BevelBorder.RAISED));
  		btOK.setForeground(Color.blue);
  		btOK.addActionListener(new BtLOK());
		dlgPane.add(btOK);
		btCancel.setBounds(200,130,60,25);
		btCancel.setBackground(new Color(255,255,235));
  		btCancel.setBorder(new BevelBorder(BevelBorder.RAISED));
  		btCancel.setForeground(Color.blue);
  		btCancel.addActionListener(new BtLCancel(this));
		dlgPane.add(btCancel);
		
		this.show();
	}
	//############################################################
	class CbLUpdate implements ItemListener {
    	public void itemStateChanged(ItemEvent e) {
    		if(e.getStateChange()==ItemEvent.SELECTED) {
    			tfTimeSpan.setEnabled(true);
    			tfThreadNum.setEnabled(true);
			}
			else{
				tfTimeSpan.setEnabled(false);
    			tfThreadNum.setEnabled(false);
			}
    	}
    }

	class BtLImUpdate implements ActionListener{
		public void actionPerformed(ActionEvent e){
		}
	}
	class BtLOK implements ActionListener{
		public void actionPerformed(ActionEvent e){
		}
	}
	class BtLCancel implements ActionListener{
		JDialog dlg;
		public BtLCancel(JDialog jdlg){
			dlg=jdlg;
		}
		public void actionPerformed(ActionEvent e){
			dlg.dispose();
		}
	}
	//############################################################	
}


class DlgVigil extends JDialog{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Checkbox	cbIsApply	=new Checkbox("  启用实时更新");
	JLabel lbTimeSpan		=new JLabel("时间间隔"),
		   lbThreadNum		=new JLabel("启用线程数");
	JTextField tfTimeSpan	=new JTextField(10),
			   tfThreadNum	=new JTextField(10);		   
	JButton	   btUpdate		=new JButton("立即监测"),
			   btOK			=new JButton("确定"),
			   btCancel		=new JButton("取消");
	
	public DlgVigil(Frame owner, String title, boolean modal){
		super(owner,title,modal);
		Container dlgPane=getContentPane();
		
		this.setBounds(400,250,320,200);
		dlgPane.setBackground(Color.white);
		dlgPane.setLayout(null);
		
		cbIsApply.setBounds(10,10,100,20);
		cbIsApply.addItemListener(new CbLVigil());
		dlgPane.add(cbIsApply);
	
		lbTimeSpan.setBounds(10,40,60,20);
		dlgPane.add(lbTimeSpan);
		tfTimeSpan.setBounds(80,40,200,20);
		tfTimeSpan.setEnabled(false);
		dlgPane.add(tfTimeSpan);
		lbThreadNum.setBounds(10,70,100,20);
		dlgPane.add(lbThreadNum);
		tfThreadNum.setBounds(80,70,200,20);
		tfThreadNum.setEnabled(false);
		dlgPane.add(tfThreadNum);
		
		btUpdate.setBounds(180,10,100,25);
		btUpdate.setBackground(new Color(255,255,235));
  		btUpdate.setBorder(new CompoundBorder(new EtchedBorder(),new LineBorder(Color.red)));
  		btUpdate.setForeground(Color.red);
  		btUpdate.addActionListener(new BtLImApply());
		dlgPane.add(btUpdate);
		btOK.setBounds(120,130,60,25);
		btOK.setBackground(new Color(255,255,235));
  		btOK.setBorder(new BevelBorder(BevelBorder.RAISED));
  		btOK.setForeground(Color.blue);
  		btOK.addActionListener(new BtLOK());
		dlgPane.add(btOK);
		btCancel.setBounds(200,130,60,25);
		btCancel.setBackground(new Color(255,255,235));
  		btCancel.setBorder(new BevelBorder(BevelBorder.RAISED));
  		btCancel.setForeground(Color.blue);
  		btCancel.addActionListener(new BtLCancel(this));
		dlgPane.add(btCancel);
		
		this.show();
	}
	//############################################################
	class CbLVigil implements ItemListener {
    	public void itemStateChanged(ItemEvent e) {
    		if(e.getStateChange()==ItemEvent.SELECTED) {
    			tfTimeSpan.setEnabled(true);
    			tfThreadNum.setEnabled(true);
			}
			else{
				tfTimeSpan.setEnabled(false);
    			tfThreadNum.setEnabled(false);
			}
    	}
    }

	class BtLImApply implements ActionListener{
		public void actionPerformed(ActionEvent e){
		}
	}
	class BtLOK implements ActionListener{
		public void actionPerformed(ActionEvent e){
		}
	}
	class BtLCancel implements ActionListener{
		JDialog dlg;
		public BtLCancel(JDialog jdlg){
			dlg=jdlg;
		}
		public void actionPerformed(ActionEvent e){
			dlg.dispose();
		}
	}
	//############################################################
}
   

class DlgDetail1 extends JDialog{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JPanel	   pStatistic 	=new JPanel(),
			   pUpDetail	=new JPanel(),
			   pWtDetail	=new JPanel();
	JLabel	   lbStatistic 	=new JLabel();
	JTextArea  taUpDetail	=new JTextArea(),
			   taWtDetail	=new JTextArea();
	JButton	   btOK		=new JButton("确定"),
			   btCancel	=new JButton("取消");
	JTabbedPane tabbed 	=new JTabbedPane();
	
	public DlgDetail1(Frame owner, String title, boolean modal){
		super(owner,title,modal);
		Container dlgPane=getContentPane();
		
		this.setBounds(150,200,700,500);
		dlgPane.setBackground(Color.white);
		dlgPane.setLayout(null);
		
		pStatistic.setBounds(10,20,670,60);
		pStatistic.setBorder(new TitledBorder("统计"));
		pStatistic.setBackground(Color.white);
		pStatistic.add(lbStatistic);
		dlgPane.add(pStatistic);
		
		tabbed.add("更新信息",taUpDetail);
		tabbed.setBounds(10,100,670,320);
		tabbed.add("监测信息",taWtDetail);
		dlgPane.add(tabbed);
		
		btOK.setBounds(540,430,60,25);
		btOK.setBackground(new Color(255,255,235));
  		btOK.setBorder(new BevelBorder(BevelBorder.RAISED));
  		btOK.setForeground(Color.blue);
  		btOK.addActionListener(new BtLOK());
		dlgPane.add(btOK);
		btCancel.setBounds(600,430,60,25);
		btCancel.setBackground(new Color(255,255,235));
  		btCancel.setBorder(new BevelBorder(BevelBorder.RAISED));
  		btCancel.setForeground(Color.blue);
  		btCancel.addActionListener(new BtLCancel(this));
		dlgPane.add(btCancel);
		
		this.show();
	}
	//############################################################
	class BtLOK implements ActionListener{
		public void actionPerformed(ActionEvent e){
		}
	}
	class BtLCancel implements ActionListener{
		JDialog dlg;
		public BtLCancel(JDialog jdlg){
			dlg=jdlg;
		}
		public void actionPerformed(ActionEvent e){
			dlg.dispose();
		}
	}
	//############################################################
} 