
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;




public class Launcher implements ActionListener{
  
	final int WIDTH = 1200;
	final int HEIGHT = 800;
	
	final static String USERSEARCH = "SEARCHUSERNAME";
	final static String CHARSEARCH = "SEARCHCHARACTERNAME";
	final static String NETWORK = "NETWORK";
	final static String SEND = "SENDMESSAGE";
	final static String ACCUSERNAME = "ACCUSERNAME";
	final static String ACCEMAIL = "ACCEMAIL";
	final static String ACCPASSWORD = "ACCPASSWORD";
	final static String SECQUEST1 = "SECQUES1";
	final static String SECQUEST2 = "SECQUES2";
	final static String SECANS1 = "SECANS1";
	final static String SECANS2 = "SECANS2";
	
	//Console Panel
	JPanel consolePanel;
    JTextArea console;
    JScrollPane conScroll;
    
    //Left Panel
    JPanel leftPanel;
	JTextField accountNameSearchText;
    JTextField characterNameSearchText;
    JTextField messageInputText;
    JTextArea messageDisplayArea;
    JScrollPane messageScroll;
    JButton accountNameSearchButton;
    JButton characterNameSearchButton;
    JButton networkMonitorButton;
    JButton sendMessageButton;
    
    //Account Panel
    JPanel accountPanel;
    JTextField accountUserName;
    JTextField accountEmail;
    JTextField accountPassword;
    JTextField accountSecQuestion1; 
    JTextField accountSecQuestion2;
    JTextField accountSecAnswer1;
    JTextField accountSecAnswer2;
    JTextField accountMacAddress;
    JTextField accountLastLoginTime;
    JButton accountUserNameButton;
    JButton accountEmailButton;
    JButton accountPasswordButton;
    JButton accountSecQuestion1Button;
    JButton accountSecQuestion2Button;
    JButton accountSecAnswer1Button;
    JButton accountSecAnswer2Button;
    
	
	
	Font normalFont = new Font("Arial", Font.BOLD, 30);
	Font bigFont = new Font("Arial", Font.BOLD, 60);

	private JFrame frame;

    private JPanel serverMenuPanel;

	
	
	//Each of the elements holds a string with
	//info about each individual character. Each
	//of the strings hold values for the character's
	//stats and stuff, and they're each separated
	//by a space (or some other delimiter)
	ArrayList<String> characters = new ArrayList<String>();


	
	public Launcher(){
		//connectToServer();
        frame = new JFrame();
		serverMenuPanel = initServerMenuPanel();
        
        frame.setTitle("Launcher");
		frame.setSize(WIDTH, HEIGHT);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(serverMenuPanel);
		frame.setVisible(true);
        
	}
	

	
	public JPanel initServerMenuPanel()
	{   
        GridBagConstraints c = new GridBagConstraints();
        
        //Build Console Panel
        consolePanel = new JPanel(new GridBagLayout());
        console = new JTextArea(30,50);
        conScroll = new JScrollPane(console);
        
        console.setEditable(false);
        conScroll.setMinimumSize(new Dimension(WIDTH-60, HEIGHT/3-60));
        
        c.fill = GridBagConstraints.BOTH;
        consolePanel.add(conScroll, c);
        consolePanel.setBorder(BorderFactory.createTitledBorder("Console"));
        
        
        // Build Left Panel
        leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder(""));
        leftPanel.setMinimumSize(new Dimension(WIDTH/2-25, 2*HEIGHT/3-25));
        
		JLabel accountNameLabel = new JLabel("Username: ");
		JLabel characterNameLabel = new JLabel("Character Name: ");
		JLabel messageBoxLabel = new JLabel("Chat Messages");
		
		accountNameSearchText = new JTextField(30);
		accountNameSearchText.setMinimumSize(new Dimension(WIDTH/2-250, 40));
	    characterNameSearchText = new JTextField(30);
	    characterNameSearchText.setMinimumSize(new Dimension(WIDTH/2-250, 40));
	    messageInputText =new JTextField(30);
	    messageInputText.setMinimumSize(new Dimension(WIDTH/2-150, 30));
	    
	    messageDisplayArea = new JTextArea(20, 40);
	    messageDisplayArea.setEditable(false);
	    messageScroll = new JScrollPane(messageDisplayArea);
	    messageScroll.setMinimumSize(new Dimension(WIDTH/2-50, 2*HEIGHT/6-50));

       accountNameSearchButton = new JButton("Search");
       accountNameSearchButton.setActionCommand(USERSEARCH);
       accountNameSearchButton.addActionListener(this);
       
       characterNameSearchButton = new JButton("Search");
       characterNameSearchButton.setActionCommand(CHARSEARCH);
       characterNameSearchButton.addActionListener(this);
       
       networkMonitorButton = new JButton("Network Monitoring");
       networkMonitorButton.setActionCommand(NETWORK);
       networkMonitorButton.addActionListener(this);
       
       sendMessageButton = new JButton("Send");
       sendMessageButton.setActionCommand(SEND);
       sendMessageButton.addActionListener(this);
        
       c.anchor = GridBagConstraints.CENTER;
       c.fill = GridBagConstraints.HORIZONTAL;
        
       c.gridx = 0;
       c.gridy = 0;
       leftPanel.add(accountNameLabel, c);
       c.gridy = 1;
       leftPanel.add(characterNameLabel, c);
      
       c.gridx = 1;
       c.gridy = 0;
       leftPanel.add(accountNameSearchText, c);
       c.gridy = 1;
       leftPanel.add(characterNameSearchText, c);
        
       c.insets = new Insets(5,5,5,5);
       
       c.gridy=0;
       c.gridx = 2;
       leftPanel.add(accountNameSearchButton, c);
       c.gridy = 1;
       leftPanel.add(characterNameSearchButton, c);
       
       c.insets = new Insets(0,0,20,0);
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 3;
        leftPanel.add(networkMonitorButton, c);
        c.insets = new Insets(0,0,0,0);
        
        c.gridx = 0;
        c.gridy = 4;
        leftPanel.add(messageBoxLabel, c);
        
        c.gridy = 5;
        leftPanel.add(messageScroll, c);
        
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 6;
        c.fill = GridBagConstraints.BOTH;
        leftPanel.add(messageInputText, c);
        
       // c.fill = GridBagConstraints.NONE;
        c.gridwidth = 1;
        c.gridx = 2;
        leftPanel.add(sendMessageButton, c);
        
        
        //Build Account Panel
        accountPanel = new JPanel(new GridBagLayout());
        
        accountUserName = new JTextField(30);
        accountUserName.setEnabled(false);
        accountUserName.setMinimumSize(new Dimension(WIDTH/2-250, 30));
        accountEmail = new JTextField(30);
        accountEmail.setEnabled(false);
        accountPassword = new JTextField(30);
        accountPassword.setEnabled(false);
        accountSecQuestion1 = new JTextField(30);
        accountSecQuestion1.setEnabled(false);
        accountSecQuestion2 = new JTextField(30);
        accountSecQuestion2.setEnabled(false);
        accountSecAnswer1 = new JTextField(30);
        accountSecAnswer1.setEnabled(false);
        accountSecAnswer2 = new JTextField(30);
        accountSecAnswer2.setEnabled(false);
        accountMacAddress = new JTextField(30);
        accountMacAddress.setEnabled(false);
        accountLastLoginTime = new JTextField(30);
        accountLastLoginTime.setEnabled(false);
        
        JLabel userName = new JLabel("User Name: ");
        userName.setHorizontalAlignment(JLabel.RIGHT);
        JLabel email = new JLabel("Email: ");
        email.setHorizontalAlignment(JLabel.RIGHT);
        JLabel password = new JLabel("Password: ");
        password.setHorizontalAlignment(JLabel.RIGHT);
        JLabel secQuest1 = new JLabel("Security 1: ");
        secQuest1.setHorizontalAlignment(JLabel.CENTER);
        JLabel secQuest2 = new JLabel("Security 2: ");
        secQuest2.setHorizontalAlignment(JLabel.CENTER);
        JLabel question1 = new JLabel("Question: ");
        question1.setHorizontalAlignment(JLabel.RIGHT);
        JLabel answer1 = new JLabel("Answer: ");
        answer1.setHorizontalAlignment(JLabel.RIGHT);
        JLabel question2 = new JLabel("Question: ");
        question2.setHorizontalAlignment(JLabel.RIGHT);
        JLabel answer2 = new JLabel("Answer: ");
        answer2.setHorizontalAlignment(JLabel.RIGHT);
        JLabel macAdress = new JLabel("Mac Adress: ");
        macAdress.setHorizontalAlignment(JLabel.RIGHT);
        JLabel lastLogin = new JLabel("Last Login Time: ");
        lastLogin.setHorizontalAlignment(JLabel.RIGHT);
    	
        accountUserNameButton = new JButton("Change");
        accountUserNameButton.setActionCommand(ACCUSERNAME);
        accountUserNameButton.addActionListener(this);
        
        accountEmailButton = new JButton("Change");
        accountEmailButton.setActionCommand(ACCEMAIL);
        accountEmailButton.addActionListener(this);
        
        accountPasswordButton = new JButton("Change");
        accountPasswordButton.setActionCommand(ACCPASSWORD);
        accountPasswordButton.addActionListener(this);
        
        accountSecQuestion1Button = new JButton("Change");
        accountSecQuestion1Button.setActionCommand(SECQUEST1);
        accountSecQuestion1Button.addActionListener(this);
        
        accountSecQuestion2Button = new JButton("Change");
        accountSecQuestion2Button.setActionCommand(SECQUEST2);
        accountSecQuestion2Button.addActionListener(this);
        
        accountSecAnswer1Button = new JButton("Change");
        accountSecAnswer1Button.setActionCommand(SECANS1);
        accountSecAnswer1Button.addActionListener(this);
        
        accountSecAnswer2Button = new JButton("Change");
        accountSecAnswer2Button.setActionCommand(SECANS2);
        accountSecAnswer2Button.addActionListener(this);
        
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(2, 0, 2, 0);
        
        c.gridx = 0;
        c.gridy = 0;
        accountPanel.add(userName, c);
        c.gridx = 1;
        accountPanel.add(accountUserName, c);
        c.gridx = 2;
        accountPanel.add(accountUserNameButton, c);
        
        c.gridx = 0;
        c.gridy = 1;
        accountPanel.add(email, c);
        c.gridx = 1;
        accountPanel.add(accountEmail, c);
        c.gridx = 2;
        accountPanel.add(accountEmailButton, c);
        
        c.gridx = 0;
        c.gridy = 2;
        accountPanel.add(password, c);
        c.gridx = 1;
        accountPanel.add(accountPassword, c);
        c.gridx = 2;
        accountPanel.add(accountPasswordButton, c);
        
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 3;
        accountPanel.add(secQuest1, c);
        c.gridy = 4;
        c.gridwidth = 1;
        c.gridx = 0;
        accountPanel.add(question1, c);
        c.gridx = 1;
        accountPanel.add(accountSecQuestion1, c);
        c.gridx = 2;
        accountPanel.add(accountSecQuestion1Button, c);
        c.gridy = 5;
        c.gridx = 0;
        accountPanel.add(answer1, c);
        c.gridx = 1;
        accountPanel.add(accountSecAnswer1, c);
        c.gridx = 2;
        accountPanel.add(accountSecAnswer1Button, c);
        
        c.gridx = 0;
        c.gridy = 6;
        c.gridwidth = 3;
        accountPanel.add(secQuest2, c);
        c.gridy = 7;
        c.gridwidth = 1;
        c.gridx = 0;
        accountPanel.add(question2, c);
        c.gridx = 1;
        accountPanel.add(accountSecQuestion2, c);
        c.gridx = 2;
        accountPanel.add(accountSecQuestion2Button, c);
        c.gridy = 8;
        c.gridx = 0;
        accountPanel.add(answer2, c);
        c.gridx = 1;
        accountPanel.add(accountSecAnswer2, c);
        c.gridx = 2;
        accountPanel.add(accountSecAnswer2Button, c);
        
        c.gridx = 0; c.gridy = 9;
        accountPanel.add(macAdress, c);
        c.gridx = 1; c.gridwidth =2;
        accountPanel.add(accountMacAddress, c);
        
        c.gridwidth = 1;
        c.gridx = 0; c.gridy = 10;
        accountPanel.add(lastLogin, c);
        c.gridx = 1; c.gridwidth =2;
        accountPanel.add(accountLastLoginTime, c);
        
        c.insets = new Insets(0, 0, 0, 0);
        
      //Build Right Panel
        JTabbedPane rightPanel = new JTabbedPane();
        rightPanel.setMinimumSize(new Dimension(WIDTH/2-25, 2*HEIGHT/3-25));
        
        JTextArea networkMonitorText = new JTextArea(30,30);
        JScrollPane networkScroll = new JScrollPane(networkMonitorText);
        
        JTextArea charText = new JTextArea(30,30);
        JScrollPane charScroll = new JScrollPane(charText);
        
        JTextArea userText = new JTextArea(30,30);
        JScrollPane userScroll = new JScrollPane(userText);
        
        rightPanel.addTab("Account Info", accountPanel);
        rightPanel.addTab("Character Info", charScroll);
        rightPanel.addTab("Networking", networkScroll);
        
        
        //Build Main Panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        
        JLabel serverHostnameLabel = new JLabel();
        serverHostnameLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        InetAddress addr;
		try {
			addr = InetAddress.getLocalHost();
			 serverHostnameLabel.setText("Server Hostname : " + addr.getHostName());
			//serverHostnameLabel.setText("test");
		       
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.CENTER;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        mainPanel.add(serverHostnameLabel, c);
        
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        mainPanel.add(leftPanel, c);
        
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 1;
        mainPanel.add(rightPanel, c);
        
        c.gridx=0;
        c.gridy=2;
        c.gridwidth = 2;
        mainPanel.add(consolePanel, c);
        accountUserName.setText("TEst");
        return mainPanel;
        
	}
	
//		get message to send =  messageText.getText();
//		get account name to search = accountNameText.getText();
//		get the idea = you
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getActionCommand() == USERSEARCH){
			String accountInfo = Main.db.getAccountInfo( Integer.toString(Main.db.getAccountID( accountNameSearchText.getText() ) ) );
			String[] tokens = accountInfo.split(":");
			
			accountUserName.setText(tokens[1]);
			accountPassword.setText(tokens[2]);
			accountEmail.setText(tokens[3]);
			accountSecQuestion1.setText(tokens[4]);
			accountSecAnswer1.setText(tokens[5]);
			accountSecQuestion2.setText(tokens[6]);
			accountSecAnswer2.setText(tokens[7]);
			accountMacAddress.setText(tokens[8]);
	        accountLastLoginTime.setText(tokens[9]);
			
			
//			accountUserName = new JTextField(30);

//	        accountEmail = new JTextField(30);
//	        accountEmail.setEnabled(false);
//	        accountPassword = new JTextField(30);
//	        accountPassword.setEnabled(false);
//	        accountSecQuestion1 = new JTextField(30);
//	        accountSecQuestion1.setEnabled(false);
//	        accountSecQuestion2 = new JTextField(30);
//	        accountSecQuestion2.setEnabled(false);
//	        accountSecAnswer1 = new JTextField(30);
//	        accountSecAnswer1.setEnabled(false);
//	        accountSecAnswer2 = new JTextField(30);
//	        accountSecAnswer2.setEnabled(false);
//	        accountMacAddress = new JTextField(30);
//	        accountMacAddress.setEnabled(false);
//	        accountLastLoginTime = new JTextField(30);
//	        accountLastLoginTime.setEnabled(false);
			
		} else if(arg0.getActionCommand() == CHARSEARCH){
			
		}else if(arg0.getActionCommand() == NETWORK){
			
		}else if(arg0.getActionCommand() == SEND){
			//just an exsample definatly need to be replaced
			messageDisplayArea.setText(messageDisplayArea.getText()+"\n"+ "Admin: " + messageInputText.getText());
			messageInputText.setText("");
		}
		else if(arg0.getActionCommand() == ACCUSERNAME)
		{
			
			if(accountUserName.isEditable())
			{
				accountUserName.setEnabled(false);
				accountUserNameButton.setText("Change");
				//Functionality to save the new username
			} 
			else 
			{
				System.out.println("HIT");
				accountUserName.setEnabled(true);
				accountUserNameButton.setText("Save");
			}
		}
		else if(arg0.getActionCommand() == ACCEMAIL){
			
		}else if(arg0.getActionCommand() == ACCPASSWORD){
			
		}else if(arg0.getActionCommand() == SECQUEST1){
			
		}else if(arg0.getActionCommand() == SECQUEST2){
			
		}else if(arg0.getActionCommand() == SECANS1){
			
		}else if(arg0.getActionCommand() == SECANS2){
	
		}
	}
	
//	private void searchChars(ActionEvent evt)
//	{
////		loginNameText.setText("");
////		loginPasswordText.setText("");
////		switchCards("Create Account Panel");
//		//this.placeHolder.setText("Test");
//		
//		placeHolder.setText(Main.db.getCharInfo(characterNameText.getText()) + "\n");
//		//characterNameText.setText(Main.db.getAccountID(accountNameText.getText()) + "\n" );
//	}
//	
//	private void searchAccounts(ActionEvent evt)
//	{
////		loginNameText.setText("");
////		loginPasswordText.setText("");
////		switchCards("Create Account Panel");
//		//this.placeHolder.setText("Test");
//		
//		placeHolder.setText(Main.db.getAccountInfo(accountNameText.getText()) + "\n");
//		//characterNameText.setText(Main.db.getAccountID(accountNameText.getText()) + "\n" );
//	}
//	
//	private void goToCreateAccount(ActionEvent evt)
//	{
////		loginNameText.setText("");
////		loginPasswordText.setText("");
////		switchCards("Create Account Panel");
//	}
//	
//	private void selectChar(ActionEvent evt)
//	{
//		System.out.println("Doesn't do anything yet...");
//	}
//	
//	public void createNewCharacter(ActionEvent evt)
//	{
//		//Create new character...
//	}
//	
////	public void connectToServer()
////	{
////		client = new ChatClient(this);
////        try {
////			client.start();
////		} 
////        catch (Exception e) {
////			e.printStackTrace();
////		}
////	}
//	
//	public void loadCharacterInfo(String[] characterList)
//	{
//		//Each element in the characters array holds a line of information
//		//about a specific character, each of the fields separated
//		//by a space (or some other delimiter)
//		
//		//Starts at 1, because the first element in this list contains the string "loginSuccess"
//		for(int x = 1; x < characterList.length; x++)
//		{
//			characters.add(characterList[x]);
//		}
//	}
	
}
