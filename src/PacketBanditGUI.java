
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
	
	//GUI components
	private final JFrame mainWindow = new JFrame("Packet Bandit  - BY: Vincent,Nick, and Tyler(Tic-Tac)");
	public static final JTextArea taOutput = new JTextArea();
	private final JScrollPane spOutput = new JScrollPane();
	private final ButtonGroup filterEnableDisable = new ButtonGroup();
	private final ButtonGroup ports = new ButtonGroup();
	//JButtons
	private final JButton captureButton = new JButton("Capture");
	private final JButton stopButton = new JButton("Stop");
	private final JButton selectButton = new JButton("Select");
	private final JButton listButton = new JButton("List");
	private final JButton filterButton = new JButton("Filter");
	private final JButton factButton = new JButton("Fact");
	private final JButton exitButton = new JButton("Exit");
	
	//All the different ports(decide which ones are important as group
	private final JRadioButton filterEnable = new JRadioButton("Enable");
	private final JRadioButton filterDisable = new JRadioButton("Disable");
	private final JRadioButton portSpecial = new JRadioButton("Special Port");
	private final JRadioButton httpPort = new JRadioButton("HTTP (80)");
	private final JRadioButton sslPort = new JRadioButton("SSL (443)");
	private final JRadioButton telnetPort = new JRadioButton("Telnet (23)");
	private final JRadioButton smtpPort = new JRadioButton("SMTP (25)");
	private final JRadioButton pop3Port = new JRadioButton("POP3 (110)");
	private final JRadioButton imapPort = new JRadioButton("IMAP (143)");
	private final JRadioButton imapsPort = new JRadioButton("IMAPS (993)");
	
	private final JLabel title = new JLabel("Packet Bandit ");
	private final JLabel interfaceLabel = new JLabel("Interface");
	private final JLabel filterStatusLabel = new JLabel("Port Filter Status");
	private final JLabel filterStatusBox = new JLabel("DISABLED (ALL PORTS)");
	private final JLabel filterPresets = new JLabel("Port Filter Presets");
	private final JLabel specialPortLabel = new JLabel("Special Port Number");
	
	private final JTextField selectInterface = new JTextField();
	private final JTextField specialPortText = new JTextField();
	
	
	public static void main(String args[]){
		new PacketBanditGUI();
	}
	
	private PacketBanditGUI(){
		BuildGUI();
		disableButton();
	}
	
	private void BuildGUI(){
		mainWindow.setSize(765,480);
		mainWindow.setLocation(200,200);
		mainWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		mainWindow.getContentPane().setLayout(null);
		
		taOutput.setEditable(false);
		taOutput.setFont(new Font("TimesRoman",0,14));
		//taOutput.setForeground(Color.WHIT
		taOutput.setLineWrap(true);
		
		spOutput.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		spOutput.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		spOutput.setViewportView(taOutput);

		mainWindow.getContentPane().add(spOutput);
		spOutput.setBounds(10, 16, 740, 290);

		//capture button details
		captureButton.setBackground(Color.RED);
		captureButton.setForeground(new Color(255,255,255));
		captureButton.setMargin(new Insets(0,0,0,0));
		captureButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent x){
					captureAction();
				}
				});
		mainWindow.getContentPane().add(captureButton);
		captureButton.setBounds(10,310,130,25);
	    
		//stop button details
		stopButton.setBackground(Color.GRAY);
		stopButton.setForeground(new Color(255,255,255));
		stopButton.setMargin(new Insets(0,0,0,0));
		stopButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent x){
					stopAction();
				}
				});
		mainWindow.getContentPane().add(stopButton);
		stopButton.setBounds(145,310,110,25);
		
		//select button details
		selectButton.setBackground(Color.GRAY);
		selectButton.setForeground(new Color(255,255,255));
		selectButton.setMargin(new Insets(0,0,0,0));
		selectButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent x){
					selectAction();
				}
				});
		mainWindow.getContentPane().add(selectButton);
		selectButton.setBounds(0,388,75,20);
		
		//list button details
		listButton.setBackground(Color.GRAY);
		listButton.setForeground(new Color(255,255,255));
		listButton.setMargin(new Insets(0,0,0,0));
		listButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent x){
					listAction();
				}
				});
		mainWindow.getContentPane().add(listButton);
		listButton.setBounds(0,410,75,20);
		
		//filter button details
		filterButton.setBackground(Color.GRAY);
		filterButton.setForeground(new Color(255,255,255));
		filterButton.setMargin(new Insets(0,0,0,0));
		filterButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent x){
					filterAction();
				}
				});
		mainWindow.getContentPane().add(filterButton);
		filterButton.setBounds(360,400,80,20);
		
		//exit button details
		exitButton.setBackground(Color.GRAY);
		exitButton.setForeground(new Color(255,255,255));
		exitButton.setMargin(new Insets(0,0,0,0));
		exitButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent x){
					exitAction();
				}
				});
		mainWindow.getContentPane().add(exitButton);
		exitButton.setBounds(180,400,75,25);

		//fact button
		factButton.setBackground(Color.GRAY);
		factButton.setForeground(new Color(255,255,255));
		factButton.setMargin(new Insets(0,0,0,0));
		factButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent x){
				factAction(x);
			}
		});
		mainWindow.getContentPane().add(factButton);
		factButton.setBounds(100,400,75,25);
		
		//filter enable button
		filterEnableDisable.add(filterEnable);
		filterEnable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent x){
				enableFilterAction();
			}
			});
		mainWindow.getContentPane().add(filterEnable);
		filterEnable.setBounds(290,350,70,25);
		
		//filter disable button
		filterEnableDisable.add(filterDisable);
		filterDisable.setSelected(true);
		filterDisable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent x){
				disableFilterAction();
			}
			});
		mainWindow.getContentPane().add(filterDisable);
		filterDisable.setBounds(360,350,70,25);
		
		//port special button
		ports.add(portSpecial);
		portSpecial.setFont(new Font("TimesRoman",0,12));
		mainWindow.getContentPane().add(portSpecial);
		portSpecial.setBounds(360,380,90,20);
		
		//port HTTP button
		ports.add(httpPort);
		httpPort.setFont(new Font("TimesRoman",0,12));
		httpPort.setToolTipText("HTTP (80)");
		mainWindow.getContentPane().add(httpPort);
		httpPort.setBounds(460,320,90,23);
		
		//port SSL button
		ports.add(sslPort);
		sslPort.setFont(new Font("TimesRoman",0,12));
		sslPort.setToolTipText("SSL (443)");
		mainWindow.getContentPane().add(sslPort);
		sslPort.setBounds(460,340,100,23);
		
		//port telnet
		ports.add(telnetPort);
		telnetPort.setFont(new Font("TimesRoman",0,12));
		telnetPort.setToolTipText("Telnet (23)");
		mainWindow.getContentPane().add(telnetPort);
		telnetPort.setBounds(660,340,90,25);
		
		//port SMTP
		ports.add(smtpPort);
		smtpPort.setFont(new Font("TimesRoman",0,12));
		smtpPort.setToolTipText("SMTP (25)");
		mainWindow.getContentPane().add(smtpPort);
		smtpPort.setBounds(560,320,90,25);
		
		//port POP3
		ports.add(pop3Port);
		pop3Port.setFont(new Font("TimesRoman",0,12));
		pop3Port.setToolTipText("POP3 (110)");
		mainWindow.getContentPane().add(pop3Port);
		pop3Port.setBounds(560,340,90,25);
		
		//port IMAP
		ports.add(imapPort);
		imapPort.setFont(new Font("TimesRoman",0,12));
		imapPort.setToolTipText("IMAP (143)");
		mainWindow.getContentPane().add(imapPort);
		imapPort.setBounds(660,320,90,25);
		
		//title
		title.setFont(new Font("TimesRoman",0,14));
		title.setHorizontalAlignment(SwingConstants.CENTER);
		mainWindow.getContentPane().add(title);
		title.setBounds(150,0,440,15);
		
		//interface
		interfaceLabel.setHorizontalAlignment(SwingConstants.CENTER);
		mainWindow.getContentPane().add(interfaceLabel);
		interfaceLabel.setBounds(10,344,60,16);
		
		//filter status
		filterStatusLabel.setHorizontalAlignment(SwingConstants.CENTER);
		mainWindow.getContentPane().add(filterStatusLabel);
		filterStatusLabel.setBounds(300,310,110,16);
		
		//filter StatusBox
		filterStatusBox.setHorizontalAlignment(SwingConstants.CENTER);
		filterStatusBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		mainWindow.getContentPane().add(filterStatusBox);
		filterStatusBox.setBounds(270,330,170,20);
		
		//filter presets
		filterPresets.setForeground(Color.GREEN);
		filterPresets.setHorizontalAlignment(SwingConstants.CENTER);
		mainWindow.getContentPane().add(filterPresets);
		filterPresets.setBounds(550,310,110,10);
		
		//special port
		specialPortLabel.setFont(new Font("TimesRoman",0,14));
		specialPortLabel.setHorizontalAlignment(SwingConstants.CENTER);
		mainWindow.getContentPane().add(specialPortLabel);
		specialPortLabel.setBounds(270,380,80,14);
		
		//select interface
		selectInterface.setForeground(Color.RED);
		selectInterface.setHorizontalAlignment(JTextField.CENTER);
		mainWindow.getContentPane().add(selectInterface);
		selectInterface.setBounds(3,364,70,20);
		
		//Special port text
		specialPortText.setForeground(Color.RED);
		specialPortText.setHorizontalAlignment(JTextField.CENTER);
		mainWindow.getContentPane().add(specialPortText);
		specialPortText.setBounds(270,400,80,20);
		
		
		
		mainWindow.setVisible(true);
	}
	
	private void captureAction(){
		taOutput.setText("");
		captureState = true;
		CapturePackets();
	}
	private void stopAction(){
		captureState = false;
		bandit.finished();
		
	}
	public void factAction(ActionEvent x){
		JOptionPane.showMessageDialog(null,"Google is better than Bing");
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
			if(filterEnable.isSelected()){
				if(portSpecial.isSelected()){
					String port = specialPortText.getText();
					cap.setFilter("port " + port,true);
				}
					
				else if(httpPort.isSelected()){
					cap.setFilter("port 80 ",true);
				}
				else if(sslPort.isSelected()){
					cap.setFilter("port 443 ",true);
				}
				else if(telnetPort.isSelected()){
					cap.setFilter("port 23 ",true);
				}
				else if(smtpPort.isSelected()){
					cap.setFilter("port 25 ",true);
				}
				else if(pop3Port.isSelected()){
					cap.setFilter("port 110 ",true);
				}
				else if(imapPort.isSelected()){
					cap.setFilter("port 143 ",true);
				}
				else if(imapsPort.isSelected()){
					cap.setFilter("port 993 ",true);
				}
				else{
					JOptionPane.showMessageDialog(null,"Filtering can not be done at this time");
				}
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
	private void enableFilterAction(){
		filterStatusBox.setText("Enable (selected ports)");
	}
	private void disableFilterAction(){
		filterStatusBox.setText("disable (all ports)");
	}
	
	private void disableButton(){
		captureButton.setEnabled(false);
		stopButton.setEnabled(false);
		selectButton.setEnabled(false);
		filterButton.setEnabled(false);
	}
	private void enableButtons(){
		captureButton.setEnabled(true);
		stopButton.setEnabled(true);
		selectButton.setEnabled(true);
		filterButton.setEnabled(true);
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