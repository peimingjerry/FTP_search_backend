//此程序的作用是DB属性操作界面

package	control;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

import structs.DBProperty;

public class DBManagerPanel extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JPanel  pDBSet		=new JPanel(),
			pStatistic	=new JPanel();
	JButton btApply		=new JButton("应用"),
			btCancel	=new JButton("取消");
	JLabel	lbName		=new JLabel(new String("用户名")),
			lbPassWord	=new JLabel(new String("密码")),
			lbURLDoc	=new JLabel(new String("文档数据库URL")),
			lbURLSort	=new JLabel(new String("索引数据库URL")),
			lbStRecord	=new JLabel("当前数据库记录文件数为 0"),
			lbStIP		=new JLabel("当前数据库记录IP地址数为0"),
			lbStUpdate	=new JLabel("上次更新日期");
	JTextField	tfName	=new JTextField(40),
				tfURLDoc=new JTextField(80),
	            pfPw	=new JTextField(40);
	DBProperty myDBProperty;
	
	public DBManagerPanel(DBProperty myDBProperty){
		this.setLayout(null);
		this.setBackground(Color.white);
		this.setBorder(new EtchedBorder());
		this.myDBProperty = myDBProperty;
		
		tfName.setText(myDBProperty.getUser());
		tfURLDoc.setText(myDBProperty.getUrl());
		pfPw.setText(myDBProperty.getPassword());
		
		setpDBSet();
		add(pDBSet);
		setpStatistic();
		add(pStatistic);
		
	}
	private void setpDBSet(){
		pDBSet.setBounds(40,30,340,150);
		pDBSet.setBorder(new EtchedBorder());
		pDBSet.setBackground(Color.white);
		pDBSet.setLayout(null);
		lbName.setBounds(10,20,40,20);
		pDBSet.add(lbName);
		tfName.setBounds(60,20,200,20);
		//tfName.requestFocus(true);
		pDBSet.add(tfName);
		lbPassWord.setBounds(10,50,40,20);
		pDBSet.add(lbPassWord);
		pfPw.setBounds(60,50,200,20);
		pDBSet.add(pfPw);
		lbURLDoc.setBounds(10,85,100,20);
		pDBSet.add(lbURLDoc);
		tfURLDoc.setBounds(105,85,200,20);
		pDBSet.add(tfURLDoc);
		
		btApply.setBounds(165,115,60,25);
		btApply.setBackground(new Color(255,255,235));
  		btApply.setBorder(new BevelBorder(BevelBorder.RAISED));
  		btApply.setForeground(Color.blue);
		pDBSet.add(btApply);
		btCancel.setBounds(245,115,60,25);
		btCancel.setBackground(new Color(255,255,235));
  		btCancel.setBorder(new BevelBorder(BevelBorder.RAISED));
  		btCancel.setForeground(Color.blue);
		pDBSet.add(btCancel);
		
	}
	private void setpStatistic(){
		pStatistic.setBounds(40,190,340,140);
		pStatistic.setLayout(null);
		pStatistic.setBorder(new TitledBorder("统计"));
		pStatistic.setBackground(Color.white);
		lbStRecord.setBounds(50,20,200,30);
		pStatistic.add(lbStRecord);
		lbStIP.setBounds(50,60,200,30);
		pStatistic.add(lbStIP);	
		lbStUpdate.setBounds(50,100,200,30);
		pStatistic.add(lbStUpdate);	
	}
}