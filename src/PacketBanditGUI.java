
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;
import jpcap.NetworkInterfaceAddress;

import java.io.*;

public class PacketBanditGUI {
	
	NetworkInterface[] networkInterfaces;
	JpcapCaptor cap;
	CaptureThread bandit;
	int index = 0;
	int counter = 0;
	boolean captureState = false;
	
	//GUI components
	JFrame mainWindow = new JFrame("Packet Bandit  - BY: Vincent,Nick, and Tyler(Tic-Tac)");
	public static JTextArea taOutput = new JTextArea();
	JScrollPane spOutput = new JScrollPane();
	ButtonGroup filterEnableDisable = new ButtonGroup();
	ButtonGroup ports = new ButtonGroup();
	//JButtons
	JButton captureButton = new JButton("Capture");
	JButton stopButton = new JButton("Stop");
	JButton selectButton = new JButton("Select");
	JButton listButton = new JButton("List");
	JButton filterButton = new JButton("Filter");
	JButton infoButton = new JButton("Info");
	JButton saveButton = new JButton("Save");
	JButton loadButton = new JButton("Load");
	JButton aboutButton = new JButton("About");
	JButton helpButton = new JButton("Help");
	JButton exitButton = new JButton("Exit");
	
	//All the different ports(decide which ones are important as group
	JRadioButton filterEnable = new JRadioButton("Enable");
	JRadioButton filterDisable = new JRadioButton("Disable");
	JRadioButton portSpecial = new JRadioButton("Special Port");
	JRadioButton httpPort = new JRadioButton("HTTP (80)");
	JRadioButton sslPort = new JRadioButton("SSL (443)");
	JRadioButton ftpPort = new JRadioButton("FTP (21)");
	JRadioButton sshPort = new JRadioButton("SSH (22)");
	JRadioButton telnetPort = new JRadioButton("Telnet (23)");
	JRadioButton smtpPort = new JRadioButton("SMTP (25)");
	JRadioButton pop3Port = new JRadioButton("POP3 (110)");
	JRadioButton imapPort = new JRadioButton("IMAP (143)");
	JRadioButton imapsPort = new JRadioButton("IMAPS (993)");
	JRadioButton dnsPort = new JRadioButton("DNS (53)");
	JRadioButton netbiosPort = new JRadioButton("netBIOS (139)");
	JRadioButton sambaPort = new JRadioButton("SAMBA (137)");
	JRadioButton adPort = new JRadioButton("AD (445)");
	JRadioButton sqlPort = new JRadioButton("SQL (118)");
	JRadioButton ldapPort = new JRadioButton("LDAP (389)");
	
	JLabel title = new JLabel("Packet Bandit ");
	JLabel interfaceLabel = new JLabel("Interface");
	JLabel filterStatusLabel = new JLabel("Port Filter Status");
	JLabel filterStatusBox = new JLabel("DISABLED (ALL PORTS)");
	JLabel filterPresets = new JLabel("Port Filter Presets");
	JLabel specialPortLabel = new JLabel("Special Port Number");
	
	JTextField selectInterface = new JTextField();
	JTextField specialPortText = new JTextField();
	
	
	public static void main(String args[]){
		new PacketBanditGUI();
	}
	
	public PacketBanditGUI(){
		BuildGUI();
		disableButton();
	}
	
	public void BuildGUI(){
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
					captureAction(x);
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
					stopAction(x);
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
					selectAction(x);
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
					listAction(x);
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
					filterAction(x);
				}
				});
		mainWindow.getContentPane().add(filterButton);
		filterButton.setBounds(360,400,80,20);
		
		//info button details
		infoButton.setBackground(Color.GRAY);
		infoButton.setForeground(new Color(255,255,255));
		infoButton.setMargin(new Insets(0,0,0,0));
		infoButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent x){
					infoAction(x);
				}
				});
		mainWindow.getContentPane().add(infoButton);
		infoButton.setBounds(100,400,75,25);
		
		//save button details
		saveButton.setBackground(Color.GRAY);
		saveButton.setForeground(new Color(255,255,255));
		saveButton.setMargin(new Insets(0,0,0,0));
		saveButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent x){
					saveAction(x);
				}
				});
		mainWindow.getContentPane().add(saveButton);
		saveButton.setBounds(100,340,75,25);
		
		//load button details
		loadButton.setBackground(Color.GRAY);
		loadButton.setForeground(new Color(255,255,255));
		loadButton.setMargin(new Insets(0,0,0,0));
		loadButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent x){
					loadAction(x);
				}
				});
		mainWindow.getContentPane().add(loadButton);
		loadButton.setBounds(180,340,75,25);
		
	    //about button details
		aboutButton.setBackground(Color.GRAY);
		aboutButton.setForeground(new Color(255,255,255));
		aboutButton.setMargin(new Insets(0,0,0,0));
		aboutButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent x){
					aboutAction(x);
				}
				});
		mainWindow.getContentPane().add(aboutButton);
		aboutButton.setBounds(180,370,75,25);
		
		//help button details
		helpButton.setBackground(Color.GRAY);
		helpButton.setForeground(new Color(255,255,255));
		helpButton.setMargin(new Insets(0,0,0,0));
		helpButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent x){
					helpAction(x);
				}
				});
		mainWindow.getContentPane().add(helpButton);
		helpButton.setBounds(180,370,75,25);
		
		//exit button details
		exitButton.setBackground(Color.GRAY);
		exitButton.setForeground(new Color(255,255,255));
		exitButton.setMargin(new Insets(0,0,0,0));
		exitButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent x){
					exitAction(x);
				}
				});
		mainWindow.getContentPane().add(exitButton);
		exitButton.setBounds(180,400,75,25);
		
		//filter enable button
		filterEnableDisable.add(filterEnable);
		filterEnable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent x){
				enableFilterAction(x);
			}
			});
		mainWindow.getContentPane().add(filterEnable);
		filterEnable.setBounds(290,350,70,25);
		
		//filter disable button
		filterEnableDisable.add(filterDisable);
		filterDisable.setSelected(true);
		filterDisable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent x){
				disableFilterAction(x);
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
		
		//port FTP
		ports.add(ftpPort);
		ftpPort.setFont(new Font("TimesRoman",0,12));
		ftpPort.setToolTipText("FTP (21)");
		mainWindow.getContentPane().add(ftpPort);
		ftpPort.setBounds(460,360,90,25);
		
		//port SSH
		ports.add(sshPort);
		sshPort.setFont(new Font("TimesRoman",0,12));
		sshPort.setToolTipText("SSH (22)");
		mainWindow.getContentPane().add(sshPort);
		sshPort.setBounds(460,380,90,25);
		
		//port telnet
		ports.add(telnetPort);
		telnetPort.setFont(new Font("TimesRoman",0,12));
		telnetPort.setToolTipText("Telnet (23)");
		mainWindow.getContentPane().add(telnetPort);
		telnetPort.setBounds(460,400,90,25);
		
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
		imapPort.setBounds(560,360,90,25);
		
		//port IMAPS
		ports.add(imapsPort);
		imapsPort.setFont(new Font("TimesRoman",0,12));
		imapsPort.setToolTipText("IMAPS (993)");
		mainWindow.getContentPane().add(imapsPort);
		imapsPort.setBounds(560,380,90,25);
		
		//port DNS
		ports.add(dnsPort);
		dnsPort.setFont(new Font("TimesRoman",0,12));
		dnsPort.setToolTipText("DNS (53)");
		mainWindow.getContentPane().add(dnsPort);
		dnsPort.setBounds(560,400,90,25);
		
		//port netBios
		ports.add(netbiosPort);
		netbiosPort.setFont(new Font("TimesRoman",0,12));
		netbiosPort.setToolTipText("NetBios (139)");
		mainWindow.getContentPane().add(netbiosPort);
		netbiosPort.setBounds(660,320,90,25);
		
		//port Samba
		ports.add(sambaPort);
		sambaPort.setFont(new Font("TimesRoman",0,12));
		sambaPort.setToolTipText("Samba (137)");
		mainWindow.getContentPane().add(sambaPort);
		sambaPort.setBounds(660,340,90,25);
		
		//port AD
		ports.add(adPort);
		adPort.setFont(new Font("TimesRoman",0,12));
		adPort.setToolTipText("AD (445)");
		mainWindow.getContentPane().add(adPort);
		adPort.setBounds(660,360,90,25);
		
		//port SQL
		ports.add(sqlPort);
		sqlPort.setFont(new Font("TimesRoman",0,12));
		sqlPort.setToolTipText("SQL (118)");
		mainWindow.getContentPane().add(sqlPort);
		sqlPort.setBounds(660,380,90,25);
		
		//port LDAP
		ports.add(ldapPort);
		ldapPort.setFont(new Font("TimesRoman",0,12));
		ldapPort.setToolTipText("LDAP (389)");
		mainWindow.getContentPane().add(ldapPort);
		ldapPort.setBounds(660,400,90,25);
		
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
	
	public void captureAction(ActionEvent x){
		taOutput.setText("");
		captureState = true;
		CapturePackets();
	}
	public void stopAction(ActionEvent x){
		captureState = false;
		bandit.finished();
		
	}
	public void selectAction(ActionEvent x){
		ChooseInterface();
		
	}
	public void listAction(ActionEvent x){
		ListNetworkInterfaces();
		selectButton.setEnabled(true);
		selectInterface.requestFocus();
	}
	public void filterAction(ActionEvent x){
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
				else if(ftpPort.isSelected()){
					cap.setFilter("port 21 ",true);
				}
				else if(sshPort.isSelected()){
					cap.setFilter("port 22 ",true);
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
				else if(dnsPort.isSelected()){
					cap.setFilter("port 53 ",true);
				}
				else if(netbiosPort.isSelected()){
					cap.setFilter("port 139 ",true);
				}
				else if(adPort.isSelected()){
					cap.setFilter("port 445 ",true);
				}
				else if(sqlPort.isSelected()){
					cap.setFilter("port 118 ",true);
				}
				else if(ldapPort.isSelected()){
					cap.setFilter("port 389 ",true);
				}
				else{
					JOptionPane.showMessageDialog(null,"Filtering can not be done at this time");
				}
			}
		}
		catch(Exception e){
			System.out.println(e);
		}
	}
	
	public void infoAction(ActionEvent x){
		//may or may not implement
	}
	public void saveAction(ActionEvent x){
		SaveCaptureData();
	}
	public void loadAction(ActionEvent x){
		LoadCaptureData();
	}
	public void aboutAction(ActionEvent x){
		JOptionPane.showMessageDialog(null,"Packet Bandit: made by Vincent, Nick, and Tyler(Tic Tac)");
	}
	
	public void helpAction(ActionEvent x){
		//may or may not implement
	}
	public void exitAction(ActionEvent x){
		mainWindow.setVisible(false);
		mainWindow.dispose();
	}
	public void enableFilterAction(ActionEvent x){
		filterStatusBox.setText("Enable (selected ports)");
	}
	public void disableFilterAction(ActionEvent x){
		filterStatusBox.setText("disable (all ports)");
	}
	
	public void disableButton(){
		captureButton.setEnabled(false);
		stopButton.setEnabled(false);
		selectButton.setEnabled(false);
		filterButton.setEnabled(false);
		saveButton.setEnabled(false);
	}
	public void enableButtons(){
		captureButton.setEnabled(true);
		stopButton.setEnabled(true);
		selectButton.setEnabled(true);
		filterButton.setEnabled(true);
		saveButton.setEnabled(true);
		loadButton.setEnabled(true);
	}
		

	public void CapturePackets(){

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
					System.out.print(x);
				}
				return 0;
			}
			public void finished(){
				this.interrupt();
			}
		};
		bandit.start();

	}
	
	public void ListNetworkInterfaces(){
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
	public void ChooseInterface(){
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
	public void SaveCaptureData(){
		String captureData = taOutput.getText();
			try{
				File data = new File("CaptureData.txt");
				FileOutputStream outStream = new FileOutputStream(data);
				PrintStream out = new PrintStream(outStream);
				out.print(captureData);
				out.close();
				outStream.close();
				JOptionPane.showMessageDialog(null,"Save successfull");
			}
			catch(Exception e){
				JOptionPane.showMessageDialog(null,"Save unsuccessfull");
			}
	}
	
	public void LoadCaptureData(){
		String captureData = "";
			try{
				File data = new File("CaptureData.txt");
				FileInputStream dataStream = new FileInputStream(data);
				InputStreamReader input = new InputStreamReader(dataStream);
				BufferedReader in = new BufferedReader(input);
					while(in.read() != -1){
						captureData = captureData + in.readLine();
					}
				in.close();
				input.close();
				dataStream.close();
				
				taOutput.setText(captureData);
				JOptionPane.showMessageDialog(null,"Load successfull");
			}
			catch(Exception e){
				JOptionPane.showMessageDialog(null,"Load not successfull");
			}
	}
}