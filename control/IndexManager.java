package control;
  
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import structs.DBProperty;
import index.CreateIndexThread;
import index.UpdateIndexThread;
import index.WriteIndexThread;

public class IndexManager extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JPanel  pIndexCon	=new JPanel(),
			pConState	=new JPanel(),
			pIndexPath	=new JPanel(),
			pPathState	=new JPanel();
	CheckboxGroup cbGroup 	= new CheckboxGroup();
  	Checkbox    cbSigIP 	= new Checkbox("基于单IP构建", cbGroup, true),
    			cbAllIP 	= new Checkbox("基于所有IP构建", cbGroup, false);
	JTextField  tfSigIP		=new JTextField(20),
				tfIndexPath =new JTextField(40);

	JButton btIndexCon	=new JButton("开始构建"),
			btPathCh	=new JButton("选择路径"),
			btIndexPath	=new JButton("生成索引");
	JLabel	lbSigIPInst	=new JLabel(new String("将现有索引中与设定IP相关的部分更新：")),
			lbSigIP		=new JLabel(new String("          IP")), 
			lbConState	=new JLabel(new String("空闲")),
			lbAllIPInst	=new JLabel(new String("根据数据库中记录的所有IP，将现有的索引全部重构：")),
			lbIndexPath	=new JLabel(new String("文件路径")),
			lbPSetState	=new JLabel(new String("空闲"));
			
	boolean bIsSigIP	=true;
	DBProperty myDBProperty;
	
	public IndexManager(DBProperty myDBProperty)
	{
		this.setLayout(null);
		this.setBackground(Color.white);
		this.setBorder(new EtchedBorder());
		this.myDBProperty = myDBProperty;
		
		setpIndexCon();
		add(pIndexCon);
		setpIndexPath();
		add(pIndexPath);
	}
	private void setpIndexCon(){
		pIndexCon.setBounds(30,20,360,190);
		pIndexCon.setBorder(new TitledBorder("构建索引"));
		pIndexCon.setBackground(Color.white);
		pIndexCon.setLayout(null);
		
		pConState.setBounds(20,140,330,40);
		pConState.setBorder(new TitledBorder("状态"));
		pConState.setBackground(Color.white);
		pConState.setLayout(null);
		lbConState.setBounds(40,15,120,20);
		pConState.add(lbConState);
		pIndexCon.add(pConState);
		
		cbSigIP.setBounds(20,25,100,20);
		cbSigIP.addItemListener(new CbSigIP());
		cbSigIP.setVisible(false);
		pIndexCon.add(cbSigIP);
		lbSigIPInst.setBounds(35,45,320,20);
		pIndexCon.add(lbSigIPInst);
		lbSigIP.setBounds(20,70,80,20);
		pIndexCon.add(lbSigIP);
		
		tfSigIP.setBounds(80,70,140,20);
		pIndexCon.add(tfSigIP);
		
		cbAllIP.setBounds(20,95,100,20);
		cbAllIP.addItemListener(new CbAllIP());
		cbAllIP.setVisible(false);
		pIndexCon.add(cbAllIP);
		lbAllIPInst.setBounds(35,115,320,20);
		pIndexCon.add(lbAllIPInst);
		
		btIndexCon.setBounds(240,20,100,25);
		btIndexCon.setBackground(new Color(255,255,235));
  		btIndexCon.setBorder(new CompoundBorder(new EtchedBorder(),new LineBorder(Color.red)));
  		btIndexCon.setForeground(Color.red);
		btIndexCon.addActionListener(new BtLStartCon());
		pIndexCon.add(btIndexCon);
	}
	private void setpIndexPath()
	{
		pIndexPath.setBounds(30,215,360,125);
		pIndexPath.setBorder(new TitledBorder("生成索引文件"));
		pIndexPath.setBackground(Color.white);
		pIndexPath.setLayout(null);
		
		pPathState.setBounds(20,75,330,40);
		pPathState.setBorder(new TitledBorder("状态"));
		pPathState.setBackground(Color.white);
		pPathState.setLayout(null);
		lbPSetState.setBounds(40,15,120,20);
		pPathState.add(lbPSetState);
		pIndexPath.add(pPathState);
		
		
		lbIndexPath.setBounds(20,22,80,20);
		pIndexPath.add(lbIndexPath);
		tfIndexPath.setBounds(80,22,160,20);
	//	tfIndexPath.setEditable(false);
		pIndexPath.add(tfIndexPath);
		
		btPathCh.setBounds(250,18,80,25);
		btPathCh.setBackground(new Color(255,255,235));
  		//btPathCh.setBorder(new CompoundBorder(new EtchedBorder(),new LineBorder(Color.blue)));
  		btPathCh.setForeground(Color.blue);
		btPathCh.addActionListener(new BtLPathChoice());
		pIndexPath.add(btPathCh);
		
		btIndexPath.setBounds(230,50,100,25);
		btIndexPath.setBackground(new Color(255,255,235));
  		btIndexPath.setBorder(new CompoundBorder(new EtchedBorder(),new LineBorder(Color.blue)));
  		btIndexPath.setForeground(Color.blue);
		btIndexPath.addActionListener(new BtLPathSave());
		pIndexPath.add(btIndexPath);
		
	}
	//######################################################
	class CbSigIP implements ItemListener {
    	public void itemStateChanged(ItemEvent e) {
    			bIsSigIP=true;
    			tfSigIP.setEditable(true);
			}
    }
    class CbAllIP implements ItemListener {
    	public void itemStateChanged(ItemEvent e) {
    			bIsSigIP=false;
    			tfSigIP.setEditable(false);
			}
    }
	class BtLStartCon implements ActionListener{
		public void actionPerformed(ActionEvent e)
		{
			if(bIsSigIP==false)
			{
System.out.println("all");	
			    lbConState.setText("开始对重新构建索引...");
				btIndexCon.setEnabled(false);
				new CreateIndexThread(btIndexCon,lbConState,myDBProperty).start();
			}
			else if(bIsSigIP)
			{
System.out.println("sig");								
			    if(!(tfSigIP.getText().equals("")))
			    {
			       lbConState.setText("开始对IP"+tfSigIP.getText().trim()+"进行索引...");			    	
				   btIndexCon.setEnabled(false);
				   new UpdateIndexThread(btIndexCon,lbConState,myDBProperty,tfSigIP.getText()).start();
			    }
			}
			else 
			{
			}
		}
	}
	class BtLPathChoice implements ActionListener{
		public void actionPerformed(ActionEvent e)
		{
			FileDialog fd	=new FileDialog(new Frame(),"选择路径",FileDialog.SAVE);
			String s;
			fd.setFile("即将生成的索引文件");
			fd.show();
			s=fd.getDirectory();
			tfIndexPath.setText(s);
		}
	}
	class BtLPathSave implements ActionListener{
		public void actionPerformed(ActionEvent e)
		{
			if(!(tfIndexPath.getText().equals("")))
			{
				btPathCh.setEnabled(false);
				btIndexPath.setEnabled(false);
				lbPSetState.setText("开始生成索引文件...");
				new WriteIndexThread(btPathCh,btIndexPath,lbPSetState,myDBProperty,tfIndexPath.getText().trim()).start();
			}
		}
	}
}