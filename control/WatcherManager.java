package control;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import structs.DBProperty;
import structs.WatcherProperty;
import watch.FTPStateWatcher;

public class WatcherManager extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	JPanel  pVigil		=new JPanel(),
			pStatistic	=new JPanel(),
			pState		=new JPanel(),
			pDetail		=new JPanel();
	
	Checkbox   cbIsApply	=new Checkbox("  设置实时更新");
	JTextField tfTimeSpan	=new JTextField(10),
			   tfThreadNum	=new JTextField(10);		   
	JButton	btWatch		=new JButton("立即监测"),
	        btStop		=new JButton("停止监测");
	JLabel	lbVigil		=new JLabel(new String("监测数据库中记载的服务器当前是否在线 ；")),
			lbTimeSpan	=new JLabel("时间间隔(分钟)"),
		    lbThreadNum	=new JLabel("启用线程数(个)"),
			lbState 	=new JLabel(),
			lbStServer	=new JLabel();
	DBProperty myDBProperty;
	WatcherProperty watcherProperty = new WatcherProperty();
	boolean hasStart = false;
	FTPStateWatcher myFTPStateWatcher;
	
	public WatcherManager(DBProperty myDBProperty){
		this.setLayout(null);
		this.setBackground(Color.white);
		this.setBorder(new EtchedBorder());
		this.myDBProperty = myDBProperty;
		
		setpVigil();
		add(pVigil);
		setpStatistic();
		add(pStatistic);
		setpState();
		add(pState);
		
	}
	private void setpVigil(){
		pVigil.setBounds(40,30,340,160);
		pVigil.setBorder(new EtchedBorder());
		pVigil.setBackground(Color.white);
		pVigil.setLayout(null);
		lbVigil.setBounds(10,10,330,20);
		pVigil.add(lbVigil);

		cbIsApply.setBounds(10,30,100,20);
		cbIsApply.addItemListener(new CbLVigil());
		cbIsApply.setVisible(false);
		pVigil.add(cbIsApply);
	
		lbTimeSpan.setBounds(10,60,120,20);
		pVigil.add(lbTimeSpan);
		tfTimeSpan.setBounds(120,60,160,20);
		tfTimeSpan.setEnabled(false);
		pVigil.add(tfTimeSpan);
		lbThreadNum.setBounds(10,90,120,20);
		pVigil.add(lbThreadNum);
		tfThreadNum.setBounds(120,90,160,20);
		tfThreadNum.setEnabled(false);
		pVigil.add(tfThreadNum);
		
		btWatch.setBounds(100,120,100,25);
		btWatch.setEnabled(false);
		btWatch.setBackground(new Color(255,255,235));
  		btWatch.setBorder(new CompoundBorder(new EtchedBorder(),new LineBorder(Color.red)));
  		btWatch.setForeground(Color.red);
  		btWatch.addActionListener(new BtLImApply());
  		
  		btStop.setBounds(220,120,100,25);
  		btStop.setEnabled(false);
		btStop.setBackground(new Color(255,255,235));
  		btStop.setBorder(new CompoundBorder(new EtchedBorder(),new LineBorder(Color.red)));
  		btStop.setForeground(Color.red);
  		btStop.addActionListener(new BtStop());
  		
		pVigil.add(btWatch);
		pVigil.add(btStop);
	}
	private void setpState(){
		pState.setLayout(null);
		pState.setBounds(40,210,340,55);
		pState.setBorder(new TitledBorder("状态"));
		pState.setBackground(Color.white);
		lbState.setText("实时监测已禁用");
		lbState.setBounds(100,20,300,20);
		pState.add(lbState);
	}
	private void setpStatistic(){
		pStatistic.setLayout(null);
		pStatistic.setBounds(40,275,340,55);
		pStatistic.setBorder(new TitledBorder("统计"));
		pStatistic.setBackground(Color.white);
		lbStServer.setBounds(40,20,300,20);
		lbStServer.setText("");
		pStatistic.add(lbStServer);
	}
	//##############################################################
	class CbLVigil implements ItemListener {
    	public void itemStateChanged(ItemEvent e) {
    		if(e.getStateChange()==ItemEvent.SELECTED) {
    			if(hasStart){
    				btWatch.setEnabled(false);
    				btStop.setEnabled(true);    				
    			}
    			else{
    			    btWatch.setEnabled(true);
    			    btStop.setEnabled(false);
    			}
    			tfTimeSpan.setEnabled(true);
    			tfThreadNum.setEnabled(true);
			}
			else{
				tfTimeSpan.setEnabled(false);
    			tfThreadNum.setEnabled(false);
    			btWatch.setEnabled(false);
    			btStop.setEnabled(false);
			}
    	}
    }

	class BtLImApply implements ActionListener{
		public void actionPerformed(ActionEvent e){
			btWatch.setEnabled(false);
			btStop.setEnabled(true);
			hasStart = true;
			lbState.setText("实时监测已启动");
			
			if(!(tfTimeSpan.getText().equals(""))){
			  watcherProperty.setTime((new Integer(tfTimeSpan.getText())).intValue());
			}
			if(!(tfThreadNum.getText().equals(""))){
			  watcherProperty.setThreadNum((new Integer(tfThreadNum.getText())).intValue());
			}
			if(myFTPStateWatcher==null){
			  myFTPStateWatcher = new FTPStateWatcher(myDBProperty,watcherProperty,lbStServer);
			}
			myFTPStateWatcher.start();
		}
	}
	
	class BtStop implements ActionListener{
		public void actionPerformed(ActionEvent e){
			btStop.setEnabled(false);
			btWatch.setEnabled(true);
			hasStart = false;
			lbState.setText("实时监测已禁用");
			if((myFTPStateWatcher!=null)&&(myFTPStateWatcher.isAlive()))
			{
				myFTPStateWatcher.stop();
			}
		}
	}
}


