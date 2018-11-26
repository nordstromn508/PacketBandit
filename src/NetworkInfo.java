import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.*;
import java.util.*;

public class NetworkInfo {
	NetworkInterface netInterface;
	Enumeration<NetworkInterface> allInterfaces;
	Enumeration<InetAddress> allAddresses;
	int interfaceCount = 0;
	int interfaceNumber = 0;
	
	private JFrame mainWindow = new JFrame("Packet Bandit");
	
	private JLabel title = new JLabel("Network Interface Information");
	private JLabel totalInterface = new JLabel("Total # of Interfaces On This Host: ");
	private JLabel totalInterfaceBox = new JLabel("");
	private JLabel interfaceNumberLabel = new JLabel("Interface #: ");
	private JLabel interfaceNumberBox = new JLabel("");
	private JLabel interfaceName = new JLabel("Interface Name: ");
	private JLabel interfaceNameBox = new JLabel("");
	private JLabel interfaceID = new JLabel("Interface ID: ");
	private JLabel interfaceIDBox = new JLabel("");
	private JLabel mac = new JLabel("MAC Address: ");
	private JLabel macBox = new JLabel("");
	private JLabel ip = new JLabel("IP: ");
	private JLabel ipBox = new JLabel("");
	private JLabel hostName = new JLabel("Host Name: ");
	private JLabel hostNameBox = new JLabel("");
	private JLabel mtu = new JLabel("MTU: ");
	private JLabel mtuBox = new JLabel("");
	private JLabel statusLabel = new JLabel("Status: ");
	private JLabel statusBox = new JLabel("");
	private JLabel pointToPointLabel = new JLabel("Point to Point: ");
	private JLabel pointToPointBox = new JLabel("");
	private JLabel multicastLabel = new JLabel("Mutlicast: ");
	private JLabel multicastBox = new JLabel("");
	private JLabel loopbackLabel = new JLabel("Loopback: ");
	private JLabel loopbackBox = new JLabel("");
	private JLabel virtualLabel = new JLabel("Virtual: ");
	private JLabel virtualBox = new JLabel("");
	
	private JButton nextButton = new JButton("Next");
	private JButton previousButton = new JButton("Previous");
	private JButton quitButton = new JButton("Quit");

	public static void main(String[] args) {
		new NetworkInfo();

	}
	
	public NetworkInfo(){
		BuildGUI();
		GetInterface();
		DisplayInterfaceInfo(interfaceNumber);
		
	}
	
	public void GetInterface(){
		try{
			allInterfaces = NetworkInterface.getNetworkInterfaces();
			interfaceCount = Collections.list(allInterfaces).size();
		}
		catch(SocketException e){
			System.out.println(e);
		}
	}
	
	public void DisplayInterfaceInfo(int num){
		try{
			allInterfaces = NetworkInterface.getNetworkInterfaces();
			netInterface = Collections.list(allInterfaces).get(num);
			
			totalInterfaceBox.setText(Integer.toString(interfaceCount));
			interfaceNumberBox.setText(Integer.toString(num + 1));
			interfaceNameBox.setText(netInterface.getDisplayName());
			interfaceIDBox.setText(netInterface.getName());
			macBox.setText(Arrays.toString(netInterface.getHardwareAddress()));
			
			allAddresses = netInterface.getInetAddresses();
				for(InetAddress x: Collections.list(allAddresses)){
					ipBox.setText(x.getHostAddress());
					hostNameBox.setText(x.getHostName());
				}
			mtuBox.setText(Integer.toString(netInterface.getMTU()));
			
			String status;
				if(netInterface.isUp()){
					status = "Up";
				}
				else{
					status = "down";
				}
			statusBox.setText(status);
			
			String pointToPoint;  // could I set this equal to no to make code more efficient
				if(netInterface.isPointToPoint()){
					pointToPoint = "Yes";
				}
				else{
					pointToPoint = "No";
				}
			pointToPointBox.setText(pointToPoint);
			
			String multicast;  // could I set this equal to no to make code more efficient
				if(netInterface.supportsMulticast()){
					multicast = "Yes";
				}
				else{
					multicast = "No";
				}
			multicastBox.setText(multicast);
			
			String loopback;  // could I set this equal to no to make code more efficient
				if(netInterface.isLoopback()){
					loopback = "Yes";
				}
				else{
					loopback = "No";
				}
			loopbackBox.setText(loopback);
			
			String virtual;  // could I set this equal to no to make code more efficient
				if(netInterface.isVirtual()){
					virtual = "Yes";
				}
				else{
					virtual = "No";
				}
			virtualBox.setText(virtual);
		}
		catch(SocketException e){
			System.out.println(e);
		}
	}
	public void BuildGUI(){
		mainWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		mainWindow.setSize(400,400);
		mainWindow.setLayout(null);
		
		mainWindow.add(title);
		title.setBounds(112,0,172,16);
		
		mainWindow.add(totalInterface);
		totalInterface.setBounds(12,23,181,16);
		totalInterfaceBox.setForeground(Color.RED);
		totalInterfaceBox.setHorizontalAlignment(SwingConstants.CENTER);
		totalInterfaceBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		mainWindow.add(totalInterfaceBox);
		totalInterfaceBox.setBounds(224,23,136,16);
		
		mainWindow.add(interfaceNumberLabel);
		interfaceNumberLabel.setBounds(12,46,69,16);
		interfaceNumberBox.setForeground(Color.RED);
		interfaceNumberBox.setHorizontalAlignment(SwingConstants.CENTER);
		interfaceNumberBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		mainWindow.add(interfaceNumberBox);
		interfaceNumberBox.setBounds(112,46,248,16);
		
		mainWindow.add(interfaceName);
		interfaceName.setBounds(12,69,93,16);
		interfaceNameBox.setForeground(Color.RED);
		interfaceNameBox.setHorizontalAlignment(SwingConstants.CENTER);
		interfaceNameBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		mainWindow.add(interfaceNameBox);
		interfaceNameBox.setBounds(112,69,248,16);
		
		mainWindow.add(interfaceID);
		interfaceID.setBounds(12,92,72,16);
		interfaceIDBox.setForeground(Color.RED);
		interfaceIDBox.setHorizontalAlignment(SwingConstants.CENTER);
		interfaceIDBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		mainWindow.add(interfaceIDBox);
		interfaceIDBox.setBounds(112,92,248,16);
		
		mainWindow.add(mac);
		mac.setBounds(12,115,81,16);
		macBox.setForeground(Color.RED);
		macBox.setHorizontalAlignment(SwingConstants.CENTER);
		macBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		mainWindow.add(macBox);
		macBox.setBounds(112,115,248,16);
		
		mainWindow.add(ip);
		ip.setBounds(12,138,82,16);
		ipBox.setForeground(Color.RED);
		ipBox.setHorizontalAlignment(SwingConstants.CENTER);
		ipBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		mainWindow.add(ipBox);
		ipBox.setBounds(112,138,248,16);
		
		mainWindow.add(hostName);
		hostName.setBounds(12,161,82,16);
		hostNameBox.setForeground(Color.RED);
		hostNameBox.setHorizontalAlignment(SwingConstants.CENTER);
		hostNameBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		mainWindow.add(hostNameBox);
		hostNameBox.setBounds(112,161,248,16);
		
		mainWindow.add(mtu);
		mtu.setBounds(12,184,31,16);
		mtuBox.setForeground(Color.RED);
		mtuBox.setHorizontalAlignment(SwingConstants.CENTER);
		mtuBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		mainWindow.add(mtuBox);
		mtuBox.setBounds(112,184,248,16);
		
		//recheck this one *********
		mainWindow.add(statusLabel);
		statusLabel.setBounds(12,207,81,16);
		statusBox.setForeground(Color.RED);
		statusBox.setHorizontalAlignment(SwingConstants.CENTER);
		statusBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		mainWindow.add(statusBox);
		statusBox.setBounds(112,207,248,16);
		
		mainWindow.add(pointToPointLabel);
		pointToPointLabel.setBounds(12,230,100,16);
		pointToPointBox.setForeground(Color.RED);
		pointToPointBox.setHorizontalAlignment(SwingConstants.CENTER);
		pointToPointBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		mainWindow.add(pointToPointBox);
		pointToPointBox.setBounds(112,230,248,16);
		
		mainWindow.add(multicastLabel);
		multicastLabel.setBounds(12,253,100,16);
		multicastBox.setForeground(Color.RED);
		multicastBox.setHorizontalAlignment(SwingConstants.CENTER);
		multicastBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		mainWindow.add(multicastBox);
		multicastBox.setBounds(112,253,248,16);
		
		mainWindow.add(loopbackLabel);
		loopbackLabel.setBounds(12,276,100,16);
		loopbackBox.setForeground(Color.RED);
		loopbackBox.setHorizontalAlignment(SwingConstants.CENTER);
		loopbackBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		mainWindow.add(loopbackBox);
		loopbackBox.setBounds(112,276,248,16);
		
		mainWindow.add(virtualLabel);
		virtualLabel.setBounds(12,299,100,16);
		virtualBox.setForeground(Color.RED);
		virtualBox.setHorizontalAlignment(SwingConstants.CENTER);
		virtualBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		mainWindow.add(virtualBox);
		virtualBox.setBounds(112,299,248,16);
		
		previousButton.setBackground(Color.BLACK);
		previousButton.setForeground(new Color(255,255,255));
		previousButton.setText("Prev");
		previousButton.setToolTipText("Previous");
		mainWindow.add(previousButton);
		previousButton.setBounds(112,322,65,25);
		
		nextButton.setBackground(Color.BLACK);
		nextButton.setForeground(new Color(255,255,255));
		nextButton.setText("Next");
		nextButton.setToolTipText("Next");
		mainWindow.add(nextButton);
		nextButton.setBounds(184,322,65,25);
		
		previousButton.setBackground(Color.BLACK);
		previousButton.setForeground(new Color(255,255,255));
		previousButton.setText("Prev");
		previousButton.setToolTipText("Previous");
		mainWindow.add(previousButton);
		previousButton.setBounds(112,322,65,25);
		
		quitButton.setBackground(Color.BLACK);
		quitButton.setForeground(new Color(255,255,255));
		quitButton.setText("Quit");
		quitButton.setToolTipText("Quit");
		mainWindow.add(quitButton);
		quitButton.setBounds(297,322,63,25);
		
		AddListeners();
		mainWindow.setVisible(true);
	}
	
	public void AddListeners(){
		nextButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent x){
				NextAction();
			}
			});
		
		previousButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent x){
				PreviousAction();
			}
			});
		
		quitButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent x){
				QuitAction();
			}
			});
	}
	
	public void NextAction(){
			if(interfaceNumber < (interfaceCount - 1)){
				interfaceNumber++;
				DisplayInterfaceInfo(interfaceNumber);
			}
	}
	
	public void PreviousAction(){
			if(interfaceNumber > 0){
				interfaceNumber--;
				DisplayInterfaceInfo(interfaceNumber);
			}
	}
	
	public void QuitAction(){
		mainWindow.setVisible(false);
		mainWindow.dispose();
	}
	
}
