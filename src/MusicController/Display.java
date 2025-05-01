package src.MusicController;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import javax.swing.*;

public class Display{

	private static final Dimension BUTTON_SIZE = new Dimension(100, 75);
	private static final Dimension DEFAULT_SIZE = new Dimension(400, 50);
	private static final Dimension LARGE_SIZE = new Dimension(400, 200);

	private static final String BACK_BUTTON_NAME = "Back";

	private static final Font DEFAULT_FONT = new Font("Serif", Font.PLAIN, 25);
	private static final Font TITLE_FONT = new Font("Serif", Font.BOLD, 100);

	/*
	 * This includes the different security questions allowed in the account creation screen.
	 */
	private static final String[] securityQuestions = {"What is your favorite color?",
												"What was your first pet's name?",
												"Where were you born?",
												"What is your home address?",
												"What is your mother's maiden name?",
												"How many siblings do you have?"};
	private static String currentUser;

	/*
	 * This is the button for the Music Controller Page
	 */
	static JButton musicControllerButton = new JButton("Continue");
	static JButton backToMainButton = new JButton(BACK_BUTTON_NAME);

	/*
	 * These three lines include the main frame used, the buttons and panels used in mainPage()
	 */
	private static final JFrame frame = new JFrame("Music Controller");
	private static final JButton createAccountButton = new JButton("Create account");
	private static final JPanel createAccountPanel = new JPanel();
	private static final JPanel userPanel = new JPanel();
	private static final JLabel userPanelLabel = new JLabel("Users");

	/*
	 * This code creates a static guest and user account
	 */
	public static Account user = new Account();

	/*
	 * This code creates labels for the account creation screen including the username,
	 * password, and question labels.
	 */
	static JLabel usernameLabel = new JLabel("Enter your username: ");
	static JLabel passwordLabel = new JLabel("Enter your password: ");
	static JLabel questionLabels = new JLabel("Enter your security questions and answers: ");
	
	/*
	 * This code includes the text fields for the account creation screen.
	 */
	public static JTextField usernameText = new JTextField(16);
	public static JPasswordField passwordText = new JPasswordField(16);
	private static final JTextField question1Text = new JTextField(16);
	private static final JTextField question2Text = new JTextField(16);
	private static final JTextField question3Text = new JTextField(16);
	
	/*
	 * This code includes the dropdown boxes for the security question part of the account creation
	 * screen.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static final JComboBox questionOptions1 = new JComboBox(securityQuestions);
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static final JComboBox questionOptions2 = new JComboBox(securityQuestions);
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static final JComboBox questionOptions3 = new JComboBox(securityQuestions);
	
	/*
	 * This creates a submit button and back button for the account creation screen.
	 */
	static JButton submitButton = new JButton("Submit");
	static JButton backButton = new JButton(BACK_BUTTON_NAME);
	static JButton forgotPasswordButton = new JButton("Forgot Password?");
	
	/*
	 * This creates the panels for the buttons, text fields, and labels created above
	 */
	static JPanel usernamePanel = new JPanel();
	static JPanel passwordPanel = new JPanel();
	static JPanel securityQuestionsPanel = new JPanel();
	static JPanel submitPanel = new JPanel();
	static JPanel finalPanel = new JPanel(new BorderLayout());
	static JPanel backPanel = new JPanel(new BorderLayout());

	/*
	 * Submit button for the login screen.
	 */
	static JButton loginSubmitButton = new JButton("Submit");
	static JPasswordField loginPasswordField = new JPasswordField();

	/*
	 * Music Sources buttons for user accounts
	 */
	static JButton youtubeButton = new JButton("Youtube Music");
	static JButton spotifyButton = new JButton("Spotify");
	static JButton iHeartButton = new JButton("IHeartRadio");
	static JButton soundcloudButton = new JButton("SoundCloud");

	static JButton deleteUserButton = new JButton("Delete Account?");

	/*
	Submit button and questions for forgot password page.
	 */
	static JButton submitForgotPasswordSecurityQuestion = new JButton("Submit");
	static JComboBox<String> forgotPasswordSecurityQuestions = new JComboBox<>();
	static JTextField passwordResetInput = new JTextField();
	static JButton backToUserLoginPage = new JButton(BACK_BUTTON_NAME);

	/*
	JComponents for password reset screen.
	 */
	static JPasswordField passwordChangeText = new JPasswordField();
	static JButton passwordChangeSubmitButton = new JButton("Submit");

	/*
	The first setup method initializes a lot of the main focuses of this code. It sets the default close operation of the
	JFrame, makes it visible and full screens it. Below are also where the buttons get their action listeners on first
	startup. At the bottom, it calls the music controller page after getting creating buttons for each user in the system.
	 */
	public Display(){
		/*
		Sets the default values for the JFrame. Makes sure that the frame will exit on close, is visible, is in fullscreen,
		and cannot be shrunk to teeny tiny sizes
		 */
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setSize(1100, 800);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setLayout(new BorderLayout());
		frame.setMinimumSize(new Dimension(500, 500));

		/*
		All of these initialize the JButton action listeners to actually make the buttons work.
		 */
		backToUserLoginPage.addActionListener(e1 -> loginScreen(currentUser));

		deleteUserButton.addActionListener(e1 ->{
			int confirmed = JOptionPane.showConfirmDialog(frame, "Are you sure?", "Delete User", JOptionPane.YES_NO_OPTION);

			if (confirmed == 0){
				boolean check;
                check = user.deleteUser(currentUser);

                if (check){
					JOptionPane.showMessageDialog(null, "User successfully deleted.");
                    loadAccounts();
                    mainPage();
                }
			}
		});

		passwordChangeSubmitButton.addActionListener(e1 ->{
			try {
				user.changePassword(currentUser, passwordChangeText.getPassword());
				passwordChangeText.setText("");
				loginScreen(currentUser);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		});

		submitForgotPasswordSecurityQuestion.addActionListener(e1 -> {
            boolean check = user.verifySecurityQuestion(currentUser, String.valueOf(forgotPasswordSecurityQuestions.getSelectedItem()), passwordResetInput.getText());
            forgotPasswordSecurityQuestions.setSelectedIndex(0);
            passwordResetInput.setText("");
            if (check) {
                forgotPasswordPage();
            }
        });

		forgotPasswordButton.addActionListener(e1 ->{
			try{
				forgotPasswordPageConfirmation();
			} catch (IOException e){
				JOptionPane.showMessageDialog(null, "Error bringing up forgot password page. Error: " + e.getMessage(), "Forgot Password Page", JOptionPane.ERROR_MESSAGE);
			}
		});

		createAccountButton.addActionListener(e1 -> accountCreationScreen());
		submitButton.addActionListener(e1 -> {
			boolean check = user.createAccount(usernameText, passwordText, questionOptions1, questionOptions2, questionOptions3, question1Text, question2Text, question3Text);

			if (check){
				loadAccounts();
				mainPage();
			}
			else{
				accountCreationScreen();
			}
		});
		backButton.addActionListener(e1 -> mainPage());

		loginSubmitButton.addActionListener(e1 -> {
            boolean check = user.checkPassword(currentUser, loginPasswordField.getPassword());
            loginPasswordField.setText("");

            if (check){
                mainUserPage(currentUser);
            }
            else{
                JOptionPane.showMessageDialog(null, "Password incorrect. Try again.");
                loginScreen(currentUser);
            }
        });

		youtubeButton.addActionListener(e1 -> {
			try {
				musicSources.openYoutubeMusic();
			} catch (URISyntaxException e) {
				JOptionPane.showMessageDialog(null, "Could not load YouTube Music. Error: " + e.getMessage());
			}
		});

		spotifyButton.addActionListener(e1 -> {
			try {
				musicSources.openSpotifyMusic(currentUser);
			} catch (URISyntaxException e) {
				JOptionPane.showMessageDialog(null, "Could not load Spotify. Error: " + e.getMessage());
			}
		});

		iHeartButton.addActionListener(e1 -> {
			try {
				musicSources.openIHeartRadio(currentUser);
			} catch (URISyntaxException e) {
				JOptionPane.showMessageDialog(null, "Could not load IHeartRadio. Error: " + e.getMessage());
			}
		});

		soundcloudButton.addActionListener(e1 -> {
			try {
				musicSources.openSoundCloud(currentUser);
			} catch (URISyntaxException e) {
				JOptionPane.showMessageDialog(null, "Could not load SoundCloud. Error: " + e.getMessage());
			}
		});

		musicControllerButton.addActionListener(e1 -> mainPage());

		backToMainButton.addActionListener(e1 -> musicControllerPage());

		/*
		Updates the layout for the panel that displays the users as buttons This also updates the buttons for returning
		to the main page and the button to create an account
		 */
		userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));

		userPanelLabel.setPreferredSize(new Dimension(50, 25));
		userPanelLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		userPanelLabel.setFont(DEFAULT_FONT);

		createAccountButton.setPreferredSize(new Dimension(200, 25));

		backToMainButton.setPreferredSize(new Dimension(100, 25));
		backToMainButton.setAlignmentX(JButton.CENTER_ALIGNMENT);

		/*
		This is where the method loadAccounts() is called to create the buttons for the users. More will be explained
		in that method
		 */
        loadAccounts();

		/*
		Continues the code on to the main page.
		 */
		musicControllerPage();
	}

	/*
	This method houses the main page of this project. This page describes what the intentions for this project are, as
	well as just being a main page with the name of the application.
	 */
	public static void musicControllerPage()
	{
		/*
		If the method is called outside of this point, it just removes anything that was on the JFrame to begin with.
		 */
		frame.getContentPane().removeAll();

		/*
		Description of what this project aims to do and other miscellaneous information.
		 */
		String description = "<html>Welcome to my music controller application. This application will allow" +
		" you, the user, to create an account, log in and play from a variety of music sources." +
		" This is my third version and may be the last of my Music Controller applications." +
		" I will be adding API calls or call your default browser to play selected music." +
		" I will also allow you to save your logins for each account, and control how the music" +
		" is played throughout your space. I hope you enjoy and let me know if there are any issues.<html>";

		/*
		Displays the music controller title displayed at the beginning with the description text above. This is arranged
		with a box layout to ensure they are centered vertically.
		 */
		JPanel musicControllerPanel = new JPanel(new BorderLayout());
		JPanel topPanel = new JPanel();
		JLabel musicControllerTitle = new JLabel("Music Controller Application");
		JLabel musicControllerDescription = new JLabel(description);

		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

		musicControllerDescription.setFont(DEFAULT_FONT);
		musicControllerTitle.setFont(TITLE_FONT);
		musicControllerButton.setFont(DEFAULT_FONT);

		musicControllerButton.setPreferredSize(new Dimension(200, 50));

		/*
		Ensures that the tile, button, and description are all centered.
		 */
		musicControllerTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
		musicControllerButton.setAlignmentX(Component.CENTER_ALIGNMENT);

		musicControllerDescription.setAlignmentX(Component.CENTER_ALIGNMENT);
		musicControllerDescription.setPreferredSize(LARGE_SIZE);

		topPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

		/*
		Adds the title and music controller button to the topPanel JPanel to separate the top and bottom panels
		 */
		topPanel.add(musicControllerTitle);
		topPanel.add(Box.createVerticalStrut(300));
		topPanel.add(musicControllerButton);

		/*
		Adds the top panel and the description to a singular panel and separates them to the top and bottom respectively.
		After doing so, the background color is changed with the music controller panel and the top panel gets changed
		to the color that the background color is set to.
		 */
		musicControllerPanel.add(topPanel, BorderLayout.NORTH);
		musicControllerPanel.add(musicControllerDescription, BorderLayout.SOUTH);

		musicControllerPanel.setBackground(Color.gray);
		topPanel.setBackground(musicControllerPanel.getBackground());

		JScrollPane scrollPane = new JScrollPane(musicControllerPanel);

		/*
		Adds the music controller panel to the center of the frame, and then reloads the page so that the new frame will
		load properly.
		 */
		frame.add(scrollPane, BorderLayout.CENTER);

		frame.revalidate();
		frame.repaint();
	}

	/*
	This page is where the real application starts. This method creates the main page that displays all the users as buttons,
	the create user button, and a back button. I know! A back button!
	 */
	public static void mainPage(){
		/*
		The frame's content pane is cleared to ensure no other remnants from anywhere else are showing.
		 */
        frame.getContentPane().removeAll();

		/*
		Adds the button to create accounts to its own panel. Creates a new panel that allows the user to go back to the
		previous page.
		 */
		createAccountButton.setFont(DEFAULT_FONT);

		createAccountPanel.add(createAccountButton);
		JPanel backToMainPanel = new JPanel(new BorderLayout());

		backToMainButton.setPreferredSize(BUTTON_SIZE);
		backToMainButton.setFont(DEFAULT_FONT);

		backToMainPanel.add(backToMainButton, BorderLayout.WEST);
		backToMainPanel.setPreferredSize(BUTTON_SIZE);

		/*
		Creates a panel that will hold all the panels for this page
		 */
		JPanel allPanels = new JPanel(new BorderLayout());

		JScrollPane scrollPane = new JScrollPane(userPanel);

		allPanels.add(scrollPane, BorderLayout.CENTER);
		allPanels.add(createAccountPanel, BorderLayout.SOUTH);

		/*
		Adds the panel to the center of the frame and reloads the page
		 */
		frame.add(allPanels, BorderLayout.CENTER);
		frame.add(backToMainPanel, BorderLayout.NORTH);

		frame.revalidate();
		frame.repaint();
    }

	/*
	This method loads all the accounts and creates buttons for each account.
	 */
	public static void loadAccounts() {
		/*
		Removes all buttons from the userPanel and adds the label for the user panel. Afterward, the panel adds a
		vertical strut to separate the label from the user buttons.
		 */
		userPanel.removeAll();
		userPanelLabel.setFont(TITLE_FONT);
		userPanelLabel.setMinimumSize(new Dimension(250, 250));
		userPanelLabel.setPreferredSize(new Dimension(250, 250));
		userPanel.add(userPanelLabel);
		userPanel.add(Box.createVerticalStrut(100));

		/*
		Calls the guest account method that creates a button for the guest user and adds it to the user panel.
		Gets all the accounts available on the text document and calls the createButton method to create a button
		for the user.
		 */
		guestAccount();

		ArrayList<String> differentAccounts = user.getDifferentAccounts();

		for (String user : differentAccounts)
		{
			createButton(user);
		}
	}

	/*
	This method creates a guest account button and adds it to the user panel.
	 */
	public static void guestAccount()
	{
		/*
		Sets the username to guest and assigns the username to the string guestUsername
		 */
		String guestUsername = "Guest";

		/*
		Creates a button for the Guest user, aligns the button to the center and adds the action listener to lead the
		user to the guest's main user page. Afterward, the button is added to the user panel.
		 */
		JButton accountButton = new JButton(guestUsername);
		accountButton.setFont(DEFAULT_FONT);
		accountButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
		accountButton.setAlignmentY(JButton.CENTER_ALIGNMENT);
		accountButton.addActionListener(e1 -> mainUserPage(guestUsername));
		accountButton.setPreferredSize(new Dimension(50, 50));
		accountButton.setMaximumSize(new Dimension(300, 50));
		userPanel.add(accountButton);
	}

	/*
	This method creates buttons to display.
	 */
	public static void createButton(String text)
	{
		/*
		Creates a button with the label text. Centers the button and adds an action listener to send the user to the
		login screen pertaining to their username. The User panel then adds the button to the center of the panel.
		 */
		JButton accountButton = new JButton(text);
		accountButton.setFont(DEFAULT_FONT);
		accountButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		accountButton.setAlignmentY(Component.CENTER_ALIGNMENT);
		accountButton.addActionListener(e1 -> loginScreen(text));
		accountButton.setPreferredSize(new Dimension(50, 50));
		accountButton.setMaximumSize(new Dimension(300,50));
		userPanel.add(accountButton);
	}

	/*
	This method displays the screen that allows the user to create a new account. This will require the user to
	create a username, password, and select 3 security questions.
	 */
    public static void accountCreationScreen(){
		/*
		Currently only allows less than the number of max users.

		if (user.getNumberUsers() >= MAX_USERS)
		{
			JOptionPane.showMessageDialog(null, "You can only create 5 accounts");
			return;
		}


		Clears the frame, adds username label and text to the username panel, adds password label and text to the password
		panel, adds question labels and options to the security questions panel, adds submit button to the submit panel,
		and adds the back button to the back panel.
		 */
		frame.getContentPane().removeAll();

		usernameLabel.setFont(DEFAULT_FONT);
		usernameText.setFont(DEFAULT_FONT);
		usernameText.setPreferredSize(DEFAULT_SIZE);

		usernamePanel.add(usernameLabel);
		usernamePanel.add(usernameText);

		passwordLabel.setFont(DEFAULT_FONT);
		passwordText.setFont(DEFAULT_FONT);
		passwordText.setPreferredSize(DEFAULT_SIZE);

		passwordPanel.add(passwordLabel);
		passwordPanel.add(passwordText);

		questionLabels.setFont(DEFAULT_FONT);

		questionOptions1.setPreferredSize(DEFAULT_SIZE);
		questionOptions1.setFont(DEFAULT_FONT);

		question1Text.setFont(DEFAULT_FONT);
		question1Text.setPreferredSize(DEFAULT_SIZE);

		questionOptions2.setPreferredSize(DEFAULT_SIZE);
		questionOptions2.setFont(DEFAULT_FONT);

		question2Text.setPreferredSize(DEFAULT_SIZE);
		question2Text.setFont(DEFAULT_FONT);

		questionOptions3.setPreferredSize(DEFAULT_SIZE);
		questionOptions3.setFont(DEFAULT_FONT);

		question3Text.setPreferredSize(DEFAULT_SIZE);
		question3Text.setFont(DEFAULT_FONT);

		securityQuestionsPanel.add(questionLabels);
		securityQuestionsPanel.add(questionOptions1);
		securityQuestionsPanel.add(question1Text);
		securityQuestionsPanel.add(questionOptions2);
		securityQuestionsPanel.add(question2Text);
		securityQuestionsPanel.add(questionOptions3);
		securityQuestionsPanel.add(question3Text);

		submitButton.setFont(DEFAULT_FONT);

		submitPanel.add(submitButton);

		backButton.setPreferredSize(BUTTON_SIZE);
		backButton.setFont(DEFAULT_FONT);

		backPanel.add(backButton, BorderLayout.WEST);
		backPanel.setPreferredSize(BUTTON_SIZE);

		/*
		Sets the layout for the final panel to a box layout so that the components are stacked on top of each other.
		 */
		finalPanel.setLayout(new BoxLayout(finalPanel, BoxLayout.Y_AXIS));

		/*
		Ensures that the panels are all properly sized and then adds them to the final panel.
		 */
		usernamePanel.setMaximumSize(new Dimension(400, 100));
		finalPanel.add(usernamePanel);
		passwordPanel.setMaximumSize(new Dimension(400, 100));
		finalPanel.add(passwordPanel);
		securityQuestionsPanel.setMaximumSize(new Dimension(600, 500));
		finalPanel.add(securityQuestionsPanel);
		submitPanel.setMaximumSize(new Dimension(300, 100));
		finalPanel.add(submitPanel);
		finalPanel.setPreferredSize(new Dimension(300, 700));


		JPanel finalAndBackPanel = new JPanel(new BorderLayout());
		finalAndBackPanel.add(finalPanel, BorderLayout.CENTER);
		finalAndBackPanel.add(backPanel, BorderLayout.NORTH);

		JScrollPane scrollPane = new JScrollPane(finalAndBackPanel);

		/*
		Adds the final panel and the back panel to the frame, and then it reloads the frame to display the new screen.
		 */
		frame.add(scrollPane);
		frame.revalidate();
		frame.repaint();
    }

	/*
	This is the screen that allows the user to log in to their account.
	 */
	public static void loginScreen(String user)
	{
		frame.getContentPane().removeAll();

		/*
		Assigns the current user to the user provided from the login screen variables. This is usually called when
		the user logs in successfully. Creates a login panel and a login label
		 */
		currentUser = user;
		JPanel loginPanel = new JPanel();
		JLabel loginLabel = new JLabel("Enter the password for " + currentUser);

		/*
		Centers the login label, login password field, login submit button, and login panel.
		 */
		loginLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		loginPasswordField.setAlignmentX(JPasswordField.CENTER_ALIGNMENT);
		loginSubmitButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
		loginPanel.setAlignmentX(JPanel.CENTER_ALIGNMENT);

		loginLabel.setFont(DEFAULT_FONT);
		loginLabel.setPreferredSize(DEFAULT_SIZE);
		loginPasswordField.setFont(DEFAULT_FONT);
		loginPasswordField.setPreferredSize(DEFAULT_SIZE);
		loginSubmitButton.setFont(DEFAULT_FONT);
		loginSubmitButton.setPreferredSize(DEFAULT_SIZE);

		/*
		Sets the maximum size for the login text field.
		 */
		loginPasswordField.setMaximumSize(DEFAULT_SIZE);

		/*
		Sets the login panel layout to use boxlayout so that the login panels are stacked on top of each other.
		 */
		loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));

		/*
		Creates a forgot password panel that holds the forgot password button, so the user can change their password
		if need be.
		 */
		forgotPasswordButton.setPreferredSize(DEFAULT_SIZE);
		forgotPasswordButton.setFont(DEFAULT_FONT);

		JPanel forgotPasswordPanel = new JPanel();
		forgotPasswordPanel.add(forgotPasswordButton);

		backButton.setPreferredSize(BUTTON_SIZE);
		backButton.setFont(DEFAULT_FONT);
		/*
		Login panel adds the login label, password field, and submit button.
		 */
		loginPanel.add(loginLabel);
		loginPanel.add(loginPasswordField);
		loginPanel.add(loginSubmitButton);

		/*
		The back panel adds the back button and sticks it to the far left side of the screen
		 */
		backPanel.add(backButton, BorderLayout.WEST);
		backPanel.setPreferredSize(BUTTON_SIZE);

		/*
		Adds the back panel to the top of the screen, so that the back button is in the top left hand corner of the
		screen. Adds the forgot password panel to the bottom of the page. Adds the loginPanel to the page. Then
		reloads the page, so it shows the proper page.
		 */
		frame.add(backPanel, BorderLayout.NORTH);
		frame.add(forgotPasswordPanel, BorderLayout.SOUTH);
		frame.add(loginPanel);

		frame.revalidate();
		frame.repaint();
	}

	/*
	This displays the main user page for the user that is logged in.
	 */
	public static void mainUserPage(String user)
	{
		frame.getContentPane().removeAll();

		/*
		Creates a welcome panel, music sources panel, and final panel labeled total panel. Sets the layout
		for the music sources panel to box layout so that the components in the music source layout are stacked
		on top of each other. Sets the layout for the entire panel to be a box layout so that all the panels
		within the final panel stack on top of each other.
		 */
		JPanel welcomePanel = new JPanel();
		JPanel musicSourcesPanel = new JPanel(new BorderLayout());
		JPanel totalPanel = new JPanel(new BorderLayout());
		musicSourcesPanel.setLayout(new BoxLayout(musicSourcesPanel, BoxLayout.Y_AXIS));
		totalPanel.setLayout(new BorderLayout());

		/*
		Creates a welcome label to welcome the user after they log in and sets the welcome panel's layout to use
		BorderLayout to control what part of the screen the components of the welcome panel are placed.
		 */
		JLabel welcomeLabel = new JLabel("Welcome " + user + "!");
		welcomePanel.setLayout(new BorderLayout());

		/*
		General formatting for the font and size of the welcome label
		 */
		welcomeLabel.setPreferredSize(LARGE_SIZE);
		welcomeLabel.setFont(TITLE_FONT);

		/*
		Centers the welcome label and panel.
		 */
		welcomeLabel.setHorizontalAlignment(JLabel.CENTER);
		welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		/*
		Sets the maximum size of the music source buttons so that they are not huge.
		 */
		spotifyButton.setMaximumSize(DEFAULT_SIZE);
		spotifyButton.setFont(DEFAULT_FONT);
		spotifyButton.setAlignmentX(Component.CENTER_ALIGNMENT);

		youtubeButton.setMaximumSize(DEFAULT_SIZE);
		youtubeButton.setFont(DEFAULT_FONT);
		youtubeButton.setAlignmentX(Component.CENTER_ALIGNMENT);

		iHeartButton.setMaximumSize(DEFAULT_SIZE);
		iHeartButton.setFont(DEFAULT_FONT);
		iHeartButton.setAlignmentX(Component.CENTER_ALIGNMENT);

		soundcloudButton.setMaximumSize(DEFAULT_SIZE);
		soundcloudButton.setFont(DEFAULT_FONT);
		soundcloudButton.setAlignmentX(Component.CENTER_ALIGNMENT);

		/*
		Adds the back button to the far left hand side of the back panel. Adds the welcome label to the top of the
		welcome panel. Adds all the music source buttons to the panel at the top.
		 */
		backButton.setPreferredSize(BUTTON_SIZE);
		backButton.setFont(DEFAULT_FONT);
		backPanel.setPreferredSize(BUTTON_SIZE);
		backPanel.add(backButton, BorderLayout.WEST);
		welcomePanel.add(welcomeLabel, BorderLayout.NORTH);
		musicSourcesPanel.add(youtubeButton);
		musicSourcesPanel.add(spotifyButton);
		musicSourcesPanel.add(iHeartButton);
		musicSourcesPanel.add(soundcloudButton);

		/*
		Centers the music sources panel to the complete center of the page.
		 */
		musicSourcesPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		musicSourcesPanel.setAlignmentY(Component.CENTER_ALIGNMENT);

		/*
		Creates a delete user JPanel to prompt the user to delete their account.
		 */
		JPanel deleteUserPanel = new JPanel(new BorderLayout());
		deleteUserButton.setFont(DEFAULT_FONT);
		deleteUserButton.setForeground(Color.red);
		deleteUserButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		deleteUserButton.setMaximumSize(BUTTON_SIZE);
		deleteUserPanel.setMaximumSize(new Dimension(200, 40));
		deleteUserPanel.add(deleteUserButton, BorderLayout.EAST);
		/*
		Adds the back panel, welcome panel, and music sources panel to the final panel.
		 */
		totalPanel.add(backPanel, BorderLayout.NORTH);
		totalPanel.add(welcomePanel, BorderLayout.CENTER);
		totalPanel.add(musicSourcesPanel, BorderLayout.SOUTH);

		JPanel totalPanelAndDeleteUserPanel = new JPanel(new BorderLayout());
		/*
		Adds the final panel to the frame at the top of the frame. Reloads the page so the page loads properly.
		 */
		totalPanelAndDeleteUserPanel.add(totalPanel, BorderLayout.NORTH);
		if (!user.equals("Guest")){
			totalPanelAndDeleteUserPanel.add(deleteUserPanel, BorderLayout.SOUTH);
		}

		JScrollPane scrollPane = new JScrollPane(totalPanelAndDeleteUserPanel);

		frame.add(scrollPane);
		frame.revalidate();
		frame.repaint();
	}

	/*
	This displays the forgot password verification screen. This allows the user to choose a security question
	and provide an answer to the question to verify their identity.
	 */
	private static void forgotPasswordPageConfirmation() throws IOException {
		frame.getContentPane().removeAll();

		/*
		Creates a temporary Array List variable that will hold the values of the questions the user set up. The
		display class is called to get the questions that the user set up.
		 */
		ArrayList<String> tmp;
		tmp = user.getQuestions(currentUser);

		/*
		Converts the Array List to a String array so that the questions can be put into the JComboBox variable
		 */
		String[] questions = tmp.toArray(new String[0]);

		/*
		Creates a combo box with the array full of the user's questions. Sets the max size of the combo box and
		password text box so that they are not huge. Centers the password text box. Creates a password reset label
		to inform the user of what to do and centers the label, so it is in the middle of the screen. Sets the max
		size for the submit button so that it is not huge.
		 */
		forgotPasswordSecurityQuestions = new JComboBox<>(questions);
		forgotPasswordSecurityQuestions.setMaximumSize(DEFAULT_SIZE);
		forgotPasswordSecurityQuestions.setAlignmentX(Component.CENTER_ALIGNMENT);
		forgotPasswordSecurityQuestions.setFont(DEFAULT_FONT);
		passwordResetInput.setMaximumSize(DEFAULT_SIZE);
		passwordResetInput.setAlignmentX(Component.CENTER_ALIGNMENT);
		passwordResetInput.setFont(DEFAULT_FONT);
		JLabel passwordResetLabel = new JLabel("Select a security question and answer it below.");
		passwordResetLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		passwordResetLabel.setPreferredSize(DEFAULT_SIZE);
		passwordResetLabel.setFont(DEFAULT_FONT);
		submitForgotPasswordSecurityQuestion.setMaximumSize(DEFAULT_SIZE);
		submitForgotPasswordSecurityQuestion.setFont(DEFAULT_FONT);

		/*
		Creates the password panel that holds the verification information
		 */
		JPanel passwordPanel = new JPanel();

		/*
		Sets the layout manager to border layout. Adds the submit button, updates the preferred and max size of
		the submit panel, so that it is not huge, and aligns the submit panel to the center of the x-axis.
		 */
		JPanel passwordSubmitPanel = new JPanel(new BorderLayout());
		passwordSubmitPanel.add(submitForgotPasswordSecurityQuestion);
		passwordSubmitPanel.setPreferredSize(new Dimension(300, 20));
		passwordSubmitPanel.setMaximumSize(DEFAULT_SIZE);
		passwordSubmitPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

		/*
		Sets the password panel's layout manager to use box layout so that the components stack on top of each
		other. Adds the password reset label, creates a vertical structure, adds the security questions, creates
		another vertical structure, adds the verification input, creates another vertical structure, and adds the
		password submit panel. The structures were added to increase the space between the components.
		 */
		passwordPanel.setLayout(new BoxLayout(passwordPanel, BoxLayout.Y_AXIS));
		passwordPanel.add(passwordResetLabel);
		passwordPanel.add(Box.createVerticalStrut(100));
		passwordPanel.add(forgotPasswordSecurityQuestions);
		passwordPanel.add(Box.createVerticalStrut(100));
		passwordPanel.add(passwordResetInput);
		passwordPanel.add(Box.createVerticalStrut(100));
		passwordPanel.add(passwordSubmitPanel);

		/*
		Initializes the final panel and sets the layout manager to use border layout. Adds the password panel to the
		center of the final panel
		 */
		JPanel completePanel = new JPanel(new BorderLayout());
		completePanel.add(passwordPanel, BorderLayout.CENTER);

		backToUserLoginPage.setAlignmentX(Component.CENTER_ALIGNMENT);
		backToUserLoginPage.setPreferredSize(BUTTON_SIZE);
		backToUserLoginPage.setFont(DEFAULT_FONT);

		JPanel backPanel = new JPanel(new BorderLayout());
		backPanel.add(backToUserLoginPage, BorderLayout.WEST);
		backPanel.setPreferredSize(BUTTON_SIZE);

		JPanel completePanelAndBackPanel = new JPanel(new BorderLayout());
		completePanelAndBackPanel.add(completePanel, BorderLayout.CENTER);
		completePanelAndBackPanel.add(backPanel, BorderLayout.NORTH);

		JScrollPane scrollPane = new JScrollPane(completePanelAndBackPanel);
		/*
		The frame adds the final panel to the center of the frame. Reloads the page so that the page shows properly.
		 */
		frame.add(scrollPane);
		frame.revalidate();
		frame.repaint();
	}

	/*
	This displays the screen where the user can change their password.
	 */
	public static void forgotPasswordPage(){
		frame.getContentPane().removeAll();

		/*
		Creates a label prompting the user to enter a new password and centers the label.
		 */
		JLabel passwordResetLabel = new JLabel("Enter a new password.");
		passwordResetLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		passwordResetLabel.setPreferredSize(DEFAULT_SIZE);
		passwordResetLabel.setFont(DEFAULT_FONT);

		/*
		Centers the password change input text and sets the maximum size so that it is not huge.
		 */
		passwordChangeText.setAlignmentX(Component.CENTER_ALIGNMENT);
		passwordChangeText.setMaximumSize(DEFAULT_SIZE);
		passwordChangeText.setFont(DEFAULT_FONT);

		/*
		Centers the submit button and sets the maximum size so that it is not huge.
		 */
		passwordChangeSubmitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		passwordChangeSubmitButton.setMaximumSize(DEFAULT_SIZE);
		passwordChangeSubmitButton.setFont(DEFAULT_FONT);

		/*
		Creates a password change panel and sets the layout manager to use the box layout so that the components
		within are stacked on top of each other.
		 */
		JPanel passwordChangePanel = new JPanel();
		passwordChangePanel.setLayout(new BoxLayout(passwordChangePanel, BoxLayout.Y_AXIS));

		/*
		Adds the password reset label, password change input text, and submit button to the password change
		panel.
		 */
		passwordChangePanel.add(passwordResetLabel);
		passwordChangePanel.add(passwordChangeText);
		passwordChangePanel.add(passwordChangeSubmitButton);

		backToUserLoginPage.setAlignmentX(Component.CENTER_ALIGNMENT);
		backToUserLoginPage.setPreferredSize(BUTTON_SIZE);
		backToUserLoginPage.setFont(DEFAULT_FONT);

		JPanel backButton = new JPanel(new BorderLayout());
		backButton.add(backToUserLoginPage, BorderLayout.WEST);
		backButton.setPreferredSize(BUTTON_SIZE);

		/*
		Initializes a final panel that uses the BorderLayout layout manager, and adds the password change
		panel to the final panel.
		 */
		JPanel finalPanel = new JPanel();

		finalPanel.setLayout(new BorderLayout());

		finalPanel.add(passwordChangePanel);

		/*
		Adds the final panel to the frame and reloads the page, so it shows the new page properly.
		 */
		frame.add(finalPanel, BorderLayout.CENTER);
		frame.add(backButton, BorderLayout.NORTH);
		frame.revalidate();
		frame.repaint();
	}
}
