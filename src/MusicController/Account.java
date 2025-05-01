package src.MusicController;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class Account {
	/*
	Initializing variables used throughout the code.
	 */
	/* 
	public void updateSecurityQuestions();
	public void changePassword();
	public void setPlaybackSettings();
	public void updatePlaylists();
	public void setDefaultPlayback();
	public void setNumSpeakers();
	public void setSavedConfigs();
	public void setDownloadedMusic();
	public void setMusicAccounts();
	public void setSpeakerAreas();
	public void setVolume();
	private boolean checkSecurityQuestions();
	public void getDefaultPlaybackSettings();
	public void getCurrentPlaybackSettings();
	public void getPlaylists();
	public void getNumSpeakers();
	public void getSavedConfigs();
	public void getDownloadedMusic();
	public void getMusicAccounts();
	public void getGuestAccounts();
	public void getSpeakerAreas();
	public float getSpeakerVolume();
	*/
	AccountDatabase database = new AccountDatabase();
	/* 
	private String[] musicAccounts;
	private String[] guestAccount;
	private String[] speakerAreas;
	private float speakerVolume;
	private String[] defaultPlaybackSettings;
	private String[] currentPlaybackSettings;
	private String[] accountPlaylists;
	private int numSpeakers;
	private String[] savedConfigs;
	private String[] downloadedMusic;
	*/

	/*
	 * This method has the user create a username and password.
	 * After creating the username and password, the user must create
	 * security questions for their account.
	 */

	/*
	This method takes in the username text field, password text field, question options and the answers to
	create an account for the user.
	 */
	@SuppressWarnings({"rawtypes"})
	public boolean createAccount(JTextField usernameText, JPasswordField passwordText, JComboBox questionOptions1, JComboBox questionOptions2, JComboBox questionOptions3, JTextField question1Text, JTextField question2Text, JTextField question3Text) {
		/*
		Creates an object of the hash class.
		 */
		HashMap<String, String> securityQuestionsAndAnswers = new HashMap<>();

		/*
		Assigns the global variables to the input provided by the user.
		 */
		String username = usernameText.getText();
		char[] password = passwordText.getPassword();
		securityQuestionsAndAnswers.put(String.valueOf(questionOptions1.getSelectedItem()).toLowerCase(), question1Text.getText().toLowerCase());
		securityQuestionsAndAnswers.put(String.valueOf(questionOptions2.getSelectedItem()), question2Text.getText().toLowerCase());
		securityQuestionsAndAnswers.put(String.valueOf(questionOptions3.getSelectedItem()), question3Text.getText().toLowerCase());
		/*
		Checks if the user did not enter a username, password, or if they did not fill out all of their security questions.
		 */
		if (username.isEmpty() || Arrays.equals(password, "".toCharArray()) || question1Text.getText().isEmpty() || question2Text.getText().isEmpty() || question3Text.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Please fill out all boxes.", "Account Creation", JOptionPane.WARNING_MESSAGE);
			return false;
		}
		/*
		Checks if the selected items in the dropdown boxes are the same. If 2 dropdown boxes are equal, the user must
		try again with unique security questions.
		 */
		else if (Objects.equals(questionOptions1.getSelectedItem(), questionOptions2.getSelectedItem()) || Objects.equals(questionOptions1.getSelectedItem(), questionOptions3.getSelectedItem()) || Objects.equals(questionOptions2.getSelectedItem(), questionOptions3.getSelectedItem())) {
			JOptionPane.showMessageDialog(null, "Please choose 3 unique security questions.", "Account Creation", JOptionPane.WARNING_MESSAGE);
			return false;
		}

		/*
		Resets the input so that the text is not saved in the text boxes.
		 */
		usernameText.setText("");

		passwordText.setText("");

		questionOptions1.setSelectedItem("What is your favorite color?");
		questionOptions2.setSelectedItem("What is your favorite color?");
		questionOptions3.setSelectedItem("What is your favorite color?");

		question1Text.setText("");
		question2Text.setText("");
		question3Text.setText("");

		/*
		Creates 2 files. One for writing to and one for updating. Also creates a boolean to check if the users.txt
		file already exists, otherwise it will create the file.
		 */
		database.createAccount(username, password, securityQuestionsAndAnswers);
		/*
		Informs the user that the account was created successfully. and clears the global variable containing the
		user's security questions and answers.
		 */
		return true;
	}

	/*
	This method gets all the users from the file and stores the different usernames in the global variable
	different accounts.
	 */
	public ArrayList<String> getDifferentAccounts() {
		try {
			return database.getDifferentAccounts();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Error loading accounts: " + e, "Loading Accounts", JOptionPane.ERROR_MESSAGE);
			return null;
		}
	}

	/*
	Compares the inputted password with the password in the text document.
	 */
	public boolean checkPassword(String user, char[] pass) {
		boolean check = false;
		try {
			check = database.checkPassword(user, pass);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Could not check password.", "Check Password", JOptionPane.ERROR_MESSAGE);
		}

		return check;
	}

	/*
	Returns an Array List with the questions from a specified user.
	 */
	public ArrayList<String> getQuestions(String user) throws IOException {
		ArrayList<String> temporaryVar = database.getQuestionsAndAnswers(user);
		/*
		An array list temporary variable is created to store temporary values.
		 */
		ArrayList<String> questions = new ArrayList<>();

		/*
		Iterates through the questions variable to get only the question instead of the question and answer.
		The question is then added to the temporary variable.
		 */
		for (String question : temporaryVar) {
			int len = question.length();
			StringBuilder tmp = new StringBuilder();

			for (int i = 0; i < len; i++) {
				if (question.charAt(i) == ':') {
					i = len;
				} else {
					tmp.append(question.charAt(i));
				}
			}
			questions.add(tmp.toString());
		}

		/*
		Return the questions variable.
		 */
		return questions;
	}

	/*
	This method verifies the user's input is equal to the input of the security question in the "users.txt" file.
	 */
	public boolean verifySecurityQuestion(String user, String securityQuestion, String tmpAnswer) {
		/*
		Creates a hash object. The hash object is then used to hash the user's input to get the user's hashed
		answer. Creates a final int that houses the number of questions the user has.
		 */
		Hash hashObj = new Hash();
		String answer = hashObj.scrambleString(tmpAnswer.toLowerCase().toCharArray());

		/*
		Initializes the final answer string that will be compared to the user's hashed input.
		 */
		String finalAnswer = "";

		/*
		Creates a new file, gets the file size, and gets the number of users.
		 */
		ArrayList<String> questions;
		try {
			questions = database.getQuestionsAndAnswers(user);
			for (String val : questions) {
				if (val.contains(securityQuestion)) {
					String[] temp = val.split(":");
					finalAnswer = temp[1].strip();
				}
			}

		/*
		Check if the user's input is equal to the answer to the security question in the "users.txt" file.
		If it is correct, the user is directed to the forgot password page, otherwise they are informed their
		input is incorrect.
		 */
			if (!answer.equals(finalAnswer)) {
				JOptionPane.showMessageDialog(null, "Incorrect input. Try again.", "Security Question Verification", JOptionPane.WARNING_MESSAGE);
				return false;
			} else {
				return true;
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Error getting questions.", "Security Question Verification", JOptionPane.ERROR_MESSAGE);
		}

		return false;
	}

	public void changePassword(String user, char[] tempPass) throws IOException {
		Hash hashObj = new Hash();
		String password = hashObj.scrambleString(tempPass);

		if (database.setPassword(user, password)) {
			JOptionPane.showMessageDialog(null, "Password changed successfully", "Password Change", JOptionPane.PLAIN_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(null, "Error updating password. Please contact" +
					"your administrator for further assistance.", "Password Change", JOptionPane.ERROR_MESSAGE);
		}
	}

	public boolean deleteUser(String user) {
		try{
			return database.deleteUser(user);
		}catch(IOException e){
			JOptionPane.showMessageDialog(null, "Error deleting user: " + e, "Delete User", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}
}