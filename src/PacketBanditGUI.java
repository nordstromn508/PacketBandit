
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;
import jpcap.NetworkInterfaceAddress;

import java.io.*;

public class PacketBanditGUI {
	
	private NetworkInterface[] networkInterfaces;
	private JpcapCaptor cap;
	private CaptureThread bandit;
	private int index = 0;
	private int counter = 0;
	private boolean captureState = false;
	public static int filterPort = -1;
	
	//GUI components
	private final JFrame mainWindow = new JFrame("Packet Bandit  - BY: Vincent,Nick, and Tyler(Tic-Tac)");
	public static final JTextArea taOutput = new JTextArea();
	private final JScrollPane spOutput = new JScrollPane();
	private final ButtonGroup ports = new ButtonGroup();

	//JButtons
	private final JButton captureButton = new JButton("Capture");
	private final JButton stopButton = new JButton("Stop");
	private final JButton selectButton = new JButton("Select");
	private final JButton listButton = new JButton("List");
	private final JButton factButton = new JButton("Fact");
	private final JButton exitButton = new JButton("Exit");
	
	//All the different ports
	private final JRadioButton httpPort = new JRadioButton("HTTP (80)");
	private final JRadioButton sslPort = new JRadioButton("SSL (443)");
	private final JRadioButton telnetPort = new JRadioButton("Telnet (23)");
	private final JRadioButton smtpPort = new JRadioButton("SMTP (25)");
	private final JRadioButton pop3Port = new JRadioButton("POP3 (110)");
	private final JRadioButton imapPort = new JRadioButton("IMAP (143)");

	private final JLabel categories = new JLabel("Identity     Source     Destination     Protocol     Length     Info");
	private final JLabel title = new JLabel("Packet Bandit \"Banditiering Packets Since 2018\"");
	private final JLabel interfaceLabel = new JLabel("Interface");
	private final JLabel filterPresets = new JLabel("Port Filter Presets");
	private final JTextField selectInterface = new JTextField();
	
	public PacketBanditGUI(){
		BuildGUI();
		disableButton();
	}
	
	private void BuildGUI(){
		Font roman12 = new Font("TimesRoman",0,12);
		Font roman14 = new Font("TimesRoman",0,14);

		mainWindow.setSize(765,500);
		mainWindow.setLocation(200,200);
		mainWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		mainWindow.getContentPane().setLayout(null);
		//mainWindow.setResizable(false);

		// title
		title.setFont(roman14);
		title.setHorizontalAlignment(SwingConstants.CENTER);
		mainWindow.getContentPane().add(title);
		title.setBounds(150,0,440,15);
		taOutput.setEditable(false);
		taOutput.setFont(roman14);
		taOutput.setLineWrap(true);

		//Categories
		categories.setFont(roman14);
		categories.setHorizontalAlignment(SwingConstants.LEFT);
		mainWindow.getContentPane().add(categories);
		categories.setBounds(10, title.getHeight()+1+title.getY(), 440, 15);

		spOutput.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		spOutput.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		spOutput.setViewportView(taOutput);

		mainWindow.getContentPane().add(spOutput);
		spOutput.setBounds(10, categories.getY()+categories.getHeight()+1, 740, 290);

		//capture button details
		captureButton.setBackground(Color.RED);
		captureButton.setForeground(new Color(255,255,255));
		captureButton.setMargin(new Insets(0,0,0,0));
		captureButton.addActionListener(e -> captureAction());
		mainWindow.getContentPane().add(captureButton);
		captureButton.setBounds(10,spOutput.getY()+spOutput.getHeight()+1,130,25);
	    
		//stop button details
		stopButton.setBackground(Color.GRAY);
		stopButton.setForeground(new Color(255,255,255));
		stopButton.setMargin(new Insets(0,0,0,0));
		stopButton.addActionListener(e -> stopAction());
		mainWindow.getContentPane().add(stopButton);
		stopButton.setBounds(145,captureButton.getY(),110,25);

		//filter presets
		filterPresets.setForeground(Color.GREEN);
		filterPresets.setHorizontalAlignment(SwingConstants.CENTER);
		mainWindow.getContentPane().add(filterPresets);
		filterPresets.setBounds(550,stopButton.getY(),110,10);

		//port HTTP button
		ports.add(httpPort);
		httpPort.setFont(roman12);
		httpPort.setToolTipText("HTTP (80)");
		mainWindow.getContentPane().add(httpPort);
		httpPort.setBounds(460,filterPresets.getY()+filterPresets.getHeight()+1,90,23);

		//port SMTP
		ports.add(smtpPort);
		smtpPort.setFont(roman12);
		smtpPort.setToolTipText("SMTP (25)");
		mainWindow.getContentPane().add(smtpPort);
		smtpPort.setBounds(560,httpPort.getY(),90,25);

		//port IMAP
		ports.add(imapPort);
		imapPort.setFont(roman12);
		imapPort.setToolTipText("IMAP (143)");
		mainWindow.getContentPane().add(imapPort);
		imapPort.setBounds(660,httpPort.getY(),90,25);

		//port SSL button
		ports.add(sslPort);
		sslPort.setFont(roman12);
		sslPort.setToolTipText("SSL (443)");
		mainWindow.getContentPane().add(sslPort);
		sslPort.setBounds(460,httpPort.getY()+httpPort.getHeight()+1,100,23);

		//port POP3
		ports.add(pop3Port);
		pop3Port.setFont(roman12);
		pop3Port.setToolTipText("POP3 (110)");
		mainWindow.getContentPane().add(pop3Port);
		pop3Port.setBounds(560,sslPort.getY(),90,25);

		//port telnet
		ports.add(telnetPort);
		telnetPort.setFont(roman12);
		telnetPort.setToolTipText("Telnet (23)");
		mainWindow.getContentPane().add(telnetPort);
		telnetPort.setBounds(660,sslPort.getY(),90,25);

		//interface
		interfaceLabel.setHorizontalAlignment(SwingConstants.CENTER);
		mainWindow.getContentPane().add(interfaceLabel);
		interfaceLabel.setBounds(10,captureButton.getY()+captureButton.getHeight()+1,60,16);

		//select interface
		selectInterface.setForeground(Color.RED);
		selectInterface.setHorizontalAlignment(JTextField.CENTER);
		mainWindow.getContentPane().add(selectInterface);
		selectInterface.setBounds(10,interfaceLabel.getY()+interfaceLabel.getHeight()+1,70,20);

		//select button details
		selectButton.setBackground(Color.GRAY);
		selectButton.setForeground(new Color(255,255,255));
		selectButton.setMargin(new Insets(0,0,0,0));
		selectButton.addActionListener(e -> selectAction());
		mainWindow.getContentPane().add(selectButton);
		selectButton.setBounds(10, selectInterface.getY()+selectInterface.getHeight()+1,75,25);
		
		//list button details
		listButton.setBackground(Color.GRAY);
		listButton.setForeground(new Color(255,255,255));
		listButton.setMargin(new Insets(0,0,0,0));
		listButton.addActionListener(e -> listAction());
		mainWindow.getContentPane().add(listButton);
		listButton.setBounds(10,selectButton.getY()+selectButton.getHeight()+1,75,25);
		
		//exit button details
		exitButton.setBackground(Color.GRAY);
		exitButton.setForeground(new Color(255,255,255));
		exitButton.setMargin(new Insets(0,0,0,0));
		exitButton.addActionListener(e -> exitAction());
		mainWindow.getContentPane().add(exitButton);
		exitButton.setBounds(180,listButton.getY(),75,25);

		//fact button
		factButton.setBackground(Color.GRAY);
		factButton.setForeground(new Color(255,255,255));
		factButton.setMargin(new Insets(0,0,0,0));
		factButton.addActionListener(e -> factAction());
		mainWindow.getContentPane().add(factButton);
		factButton.setBounds(100,listButton.getY(),75,25);


		




		mainWindow.setVisible(true);
	}
	
	private void captureAction(){
		filterAction();
		captureState = true;
		CapturePackets();
	}
	private void stopAction(){
		captureState = false;
		bandit.finished();
		
	}
	public void factAction(){
		JOptionPane.showMessageDialog(null,"Google is better than Bing :P");
	}

	private void selectAction(){
		ChooseInterface();
	}
	private void listAction(){
		ListNetworkInterfaces();
		selectButton.setEnabled(true);
		selectInterface.requestFocus();
	}
	private void filterAction(){
		try{
			if(httpPort.isSelected()){
				filterPort = 8009;
			}
			else if(sslPort.isSelected()){
				filterPort = 443;
			}
			else if(telnetPort.isSelected()){
				filterPort = 23;
			}
			else if(smtpPort.isSelected()){
				filterPort = 25;
			}
			else if(pop3Port.isSelected()){
				filterPort = 110;
			}
			else if(imapPort.isSelected()){
				filterPort = 143;
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	private void exitAction(){
		mainWindow.setVisible(false);
		mainWindow.dispose();
	}
	
	private void disableButton(){
		captureButton.setEnabled(false);
		stopButton.setEnabled(false);
		selectButton.setEnabled(false);
	}
	private void enableButtons(){
		captureButton.setEnabled(true);
		stopButton.setEnabled(true);
		selectButton.setEnabled(true);
	}
		

	private void CapturePackets(){

		bandit = new CaptureThread()
		{
			
			public Object construct()
			{

				try{
					cap = JpcapCaptor.openDevice(networkInterfaces[index],65535,false,20);
						while(captureState){
							cap.processPacket(1, new PacketContents());
						}
					cap.close();
				}
				catch(Exception x){
					x.printStackTrace();
				}
				return 0;
			}
			public void finished()
			{
				this.interrupt();
			}
		};
		bandit.start();
	}
	
	private void ListNetworkInterfaces(){
		networkInterfaces = JpcapCaptor.getDeviceList();
		taOutput.setText("");
			for(int i = 0;i < networkInterfaces.length;i++){
				taOutput.append("\n\n----------------------------Interface" + i + "info-----------------------");
				taOutput.append("\nInterface number " + i);
				taOutput.append("\nInfo: " + networkInterfaces[i].name + " (" + networkInterfaces[i].description + ")");
				taOutput.append("\nDatalink Name: " + networkInterfaces[i].datalink_name + " (" + networkInterfaces[i].datalink_description + ")") ;
				taOutput.append("\n Mac Address: ");

				byte[] R = networkInterfaces[i].mac_address;
				for(int j = 0; j <= networkInterfaces.length; j++) {
					taOutput.append(Integer.toHexString(R[j] & 0xff));
				}
				
				NetworkInterfaceAddress[] networkAddress = networkInterfaces[i].addresses;
				taOutput.append("\nIP address: " + networkAddress[0].address);
				taOutput.append("\nSubnet Mask: " + networkAddress[0].subnet);
				taOutput.append("\nBroadcast Address: " + networkAddress[0].broadcast);
				counter++;
				
			}
	}
	private void ChooseInterface(){
		int temp = Integer.parseInt(selectInterface.getText());
			if(temp > -1 && temp < counter){
				index = temp;
				enableButtons();
			}
			else{
				JOptionPane.showMessageDialog(null,"Outside of range ");
			}
		selectInterface.setText("");
	}
}