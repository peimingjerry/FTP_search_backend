//此程序实现两个功能：
//1、创建文档库
//2、建立索引至SQL数据库
//作为一个工作线程，起到调度作用

package index;

import javax.swing.JButton;
import javax.swing.JLabel;
import structs.DBProperty;

public class CreateIndexThread extends Thread
{
	DBProperty myDBProperty;
	JButton button;
	JLabel label;
	
	public CreateIndexThread(JButton button,JLabel label,DBProperty myDBProperty)
	{
		this.myDBProperty = myDBProperty;
		this.button = button;
		this.label = label;
	}
	
	public void run()
	{
        CreatDoc creatDoc = new CreatDoc(myDBProperty.getDriverName(),myDBProperty.getUrl(),myDBProperty.getUser(),myDBProperty.getPassword());
		if(creatDoc.execute(true,"")==0)
		{
        	button.setEnabled(true);
        	label.setText("索引出错！当前空闲...");
			return;        	
		}

        IndexMachine indexMachine = new IndexMachine(myDBProperty.getDriverName(),myDBProperty.getUrl(),myDBProperty.getUser(),myDBProperty.getPassword());	
        if(indexMachine.creatIndex(true,"")==0)
        {
        	button.setEnabled(true);
        	label.setText("索引出错！当前空闲...");
			return;
        }
        button.setEnabled(true);
        label.setText("索引完毕！当前空闲...");        
	}
}
