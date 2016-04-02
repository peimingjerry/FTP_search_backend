package control;

import java.awt.*;
import java.awt.event.*;

import structs.WatcherProperty;

public class WatcherPropertySet extends Frame implements ActionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	WatcherProperty watcherProperty;
	Label watcherPropertyLabel;
	
	Label time_label,threadCount_label;
	Button OKButton,CanselButton,DefaultButton;
	
	Choice timeChoice,threadCountChoice;
	
	Panel timePanel,threadCountPanel,buttonPanel;
	
	private String[] choice = {"һ����","��Сʱ","��Сʱ","��Сʱ","8","9","10","11","12","13"};
	
	public WatcherPropertySet(WatcherProperty watcherProperty,Label watcherPropertyLabel)
	{
		this.watcherProperty = watcherProperty;
		this.watcherPropertyLabel = watcherPropertyLabel;
		
		initGUI();
	}
	
	private void initGUI()
	{
		setTitle("FTP״̬���Ӳ�������");
		setVisible(false);
		setLayout(new BorderLayout());
		setForeground(Color.blue);
		setBounds(400,440,250,140);
		setResizable(false);

		time_label = new Label("  ѡ��ʱ������");
		threadCount_label = new Label("  ѡ���߳�����");
		
		OKButton = new Button("ȷ��");
		OKButton.addActionListener(this);
		CanselButton = new Button("ȡ��");
		CanselButton.addActionListener(this);
		DefaultButton = new Button("�ָ�Ĭ��");
		DefaultButton.addActionListener(this);
		
		timeChoice = new Choice();
		threadCountChoice = new Choice();
		for(int i=0;i<4;i++)
		{
			timeChoice.addItem(choice[i]);
		}
		for(int j=4;j<10;j++)
		{
			threadCountChoice.addItem(choice[j]);
		}
		
		timePanel = new Panel();
		timePanel.add(time_label);
		timePanel.add(timeChoice);
		
		threadCountPanel = new Panel();
		threadCountPanel.add(threadCount_label);
		threadCountPanel.add(threadCountChoice);
		
		buttonPanel = new Panel();
		buttonPanel.add(OKButton);
		buttonPanel.add(CanselButton);
		buttonPanel.add(DefaultButton);
		
		add("North",timePanel);
		add("Center",threadCountPanel);
		add("South",buttonPanel);
		
		addWindowListener(new WindowAdapter()
       { public void windowClosing(WindowEvent e)
         { 
             setVisible(false);
         }
       });
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource()==OKButton)
		{
		  if(timeChoice.getSelectedIndex()==0)
		    watcherProperty.setTime(1);
		  else if(timeChoice.getSelectedIndex()==1)
		    watcherProperty.setTime(120);
		  else if(timeChoice.getSelectedIndex()==2)
		    watcherProperty.setTime(180);
		  else 
		    watcherProperty.setTime(240);
		  
		  watcherProperty.setThreadNum(threadCountChoice.getSelectedIndex()+8);
		    
		  watcherPropertyLabel.setText("   ��ǰ���ԣ����ʱ�䣺"+choice[timeChoice.getSelectedIndex()]+"���߳�����"+watcherProperty.getThreadNum());
		  
		  setVisible(false);
		}
		else if(e.getSource()==CanselButton)
		{
		  setVisible(false);	
		}
		else if(e.getSource()==DefaultButton)
		{
		  watcherProperty.setTime(1);
		  watcherProperty.setThreadNum(10);
		  
		  watcherPropertyLabel.setText("   ��ǰ���ԣ����ʱ�䣺1���ӣ��߳�����10");
		  setVisible(false);
		}
		else
		{
		}
	}
/*
	public static void main(String args[])
	{
		WatcherProperty _watcherProperty = new WatcherProperty();
		
		_watcherProperty.setTime(1);
		_watcherProperty.setThreadNum(5);
		
		new WatcherPropertySet(_watcherProperty,new Label("xxx")).setVisible(true);
	}*/
}