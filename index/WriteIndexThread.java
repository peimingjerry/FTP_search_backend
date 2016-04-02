//调度程序，控制写索引文件

package index;

import javax.swing.JButton;
import javax.swing.JLabel;

import structs.DBProperty;

public class WriteIndexThread extends Thread
{
	DBProperty myDBProperty;
	String root = new String();
	JButton button1,button2;
	JLabel label;
	
	public WriteIndexThread(JButton button1,JButton button2,JLabel label,DBProperty myDBProperty,String root)
	{
		this.myDBProperty = myDBProperty;
		this.root = root;
		this.button1 = button1;
		this.button2 = button2;
		this.label = label;
	}
	
	public void run()
	{
		WriteIndexFile writeIndexFile = new WriteIndexFile(myDBProperty.getDriverName(),myDBProperty.getUrl(),myDBProperty.getUser(),
		                                                  myDBProperty.getPassword(),root+"\\index.dat",root+"\\index.head.dat");
		if(writeIndexFile.execute()==0)
		{
			button1.setEnabled(true);
			button2.setEnabled(true);
			label.setText("生成索引文件出错！");
			return;
		}
		
		WriteFilterFile writeFilterFile = new WriteFilterFile(myDBProperty.getDriverName(),myDBProperty.getUrl(),myDBProperty.getUser(),
		                                                  myDBProperty.getPassword(),root+"\\filter.dat");
		if(writeFilterFile.execute()==0)
		{
			button1.setEnabled(true);
			button2.setEnabled(true);
			label.setText("生成索引文件出错！");
			return;
		}
			button1.setEnabled(true);
			button2.setEnabled(true);	
			label.setText("生成索引文件完毕，当前空闲！");	 		
	}
}
