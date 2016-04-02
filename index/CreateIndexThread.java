//�˳���ʵ���������ܣ�
//1�������ĵ���
//2������������SQL���ݿ�
//��Ϊһ�������̣߳��𵽵�������

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
        	label.setText("����������ǰ����...");
			return;        	
		}

        IndexMachine indexMachine = new IndexMachine(myDBProperty.getDriverName(),myDBProperty.getUrl(),myDBProperty.getUser(),myDBProperty.getPassword());	
        if(indexMachine.creatIndex(true,"")==0)
        {
        	button.setEnabled(true);
        	label.setText("����������ǰ����...");
			return;
        }
        button.setEnabled(true);
        label.setText("������ϣ���ǰ����...");        
	}
}
