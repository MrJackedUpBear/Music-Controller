package src.MusicController;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AccountDatabase {

    private final Path users = Paths.get("users.txt");
    private final Path tmp = Paths.get("tmp.txt");
    private static int fileSize = 0;
    public static int numberUsers;

    public void createAccount(String username, char[] password, HashMap<String, String> securityQuestionsAndAnswers) {
        /*
		Check is true, so the users.txt file does not exist; this code will now execute.
		 */
        try {
            ArrayList<String> differentAccounts = getDifferentAccounts();
            for (String user: differentAccounts){
                if (user.equalsIgnoreCase(username)){
                    JOptionPane.showMessageDialog(null, "Unable to create duplicate account", "Account Creation", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            updateFile(username, password, securityQuestionsAndAnswers);
            JOptionPane.showMessageDialog(null,"User created successfully", "Account Creation", JOptionPane.PLAIN_MESSAGE);
        } catch (IOException e1) {
            try{
                createFile(username, password, securityQuestionsAndAnswers);
                JOptionPane.showMessageDialog(null, "User created successfully", "Account Creation", JOptionPane.PLAIN_MESSAGE);
            }catch(IOException e2){
                JOptionPane.showMessageDialog(null, "Error creating user: " + e2, "Account Creation", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public ArrayList<String> getDifferentAccounts() throws IOException
    {
        return readUsersFromFile();
    }

    public boolean checkPassword(String user, char[] pass) throws IOException {
        Hash hashObj = new Hash();

        String storedPassword = getPassword(user);

		/*
		Hashes the user's input to compare it to the hashed password in the text file.
		 */
        String password = hashObj.scrambleString(pass);

		/*
		Checks if the global password variable from the text file is equal to the hashed user input. Then it displays
		the user's main page if they are successful, otherwise the user is notified that the password they entered
		was incorrect. Then directs the user back to the login page for the user.
		 */

        return storedPassword.equals(password);
    }

    public ArrayList<String> getQuestionsAndAnswers(String user) throws IOException {
        /*
		Initializes an Array list named questions.
		 */
        ArrayList<String> questions = new ArrayList<>();

		/*
		Creates a File variable for the "users.txt" file, then gets the size of the users file with user
		getFileSize method. Afterward, the number of users are found by passing the fileSize into the getNumberUsers()
		method.
		 */
        fileSize = getFileSize(users);
        int numberUsers = getNumberUsers(fileSize);

		/*
		Creates a new reader variable to read through the file
		 */
        BufferedReader reader = Files.newBufferedReader(users);

		/*
		Iterates through the file to find the specified user. Once the user is found, the questions are added to the
		Array List questions variable.
		 */
        for (int i = 0; i < numberUsers; i++){
            reader.readLine();
            if (reader.readLine().strip().equals(user)){
                reader.readLine();
                questions.add(reader.readLine().strip());
                questions.add(reader.readLine().strip());
                questions.add(reader.readLine().strip());
                reader.readLine();
                break;
            }
            else{
                reader.readLine();
                reader.readLine();
                reader.readLine();
                reader.readLine();
                reader.readLine();
            }
        }

        reader.close();

        return questions;
    }

    public boolean setPassword(String user, String password) throws IOException {
        fileSize = getFileSize(users);
        numberUsers = getNumberUsers(fileSize);

        BufferedReader reader = Files.newBufferedReader(users);
        BufferedWriter writer = Files.newBufferedWriter(tmp);

        for (int i = 0; i < fileSize; i++){
            String temp = reader.readLine().strip();

            if (temp.equals(user)){
                writer.write("\t" + temp);
                writer.write("\n");
                reader.readLine();
                i++;
                writer.write("\t" + password);
                writer.write("\n");
            }
            else{
                if (temp.equals("{") || temp.equals("}")){
                    writer.write(temp);
                }
                else{
                    writer.write("\t" + temp);
                }
                writer.write("\n");
            }
        }

        reader.close();
        writer.close();

        Files.delete(users);
        Files.move(tmp, users);


        return true;
    }

    public boolean deleteUser(String user) throws IOException {
        int location = getUserLocation(user);

        if (location == -1){
            JOptionPane.showMessageDialog(null, "Could not find user: " + user);
            return false;
        }

        BufferedReader reader = Files.newBufferedReader(users);
        BufferedWriter writer = Files.newBufferedWriter(tmp);

        for (int i = 0; i < location; i++){
            writer.write(reader.readLine());
            writer.write("\n");
        }

        for (int i = location; i < (location + 7); i++){
            reader.readLine();
        }

        for (int i = (location + 7); i < fileSize; i++){
            writer.write(reader.readLine());
            writer.write("\n");
        }

        reader.close();
        writer.close();

        Files.delete(users);
        Files.move(tmp, users);

        return true;
    }

    public int getNumberUser() throws IOException {
        return getNumberUsers(getFileSize(users));
    }

    private int getUserLocation(String user) throws IOException {
        fileSize = getFileSize(users);

        BufferedReader reader = Files.newBufferedReader(users);

        for (int i = 0; i < fileSize; i++){
            if (reader.readLine().strip().equals(user)){
                return i - 1;
            }
        }
        reader.close();
        return -1;
    }

    private void updateFile(String username, char[] password, HashMap<String, String> securityQuestionsAndAnswers) throws IOException {
        Hash hashObj = new Hash();

        fileSize = getFileSize(users);
        /*
        Creates a reader for the user's file and a writer for the tmp file.
         */
        BufferedReader reader = Files.newBufferedReader(users);
        BufferedWriter writer = Files.newBufferedWriter(tmp);

        /*
        The writer copies and pastes the text in the users.txt file over to the tmp.txt file. Then it closes
        the reader variable.
         */
        for (int i = 0; i < fileSize; i++)
        {
            writer.write(reader.readLine());
            writer.newLine();
        }
        reader.close();

        /*
        This writes the new user's information to the file and closes the writer variable.
         */
        writer.write("{");
        writer.newLine();
        writer.write("\t" + username);
        writer.newLine();
        writer.write("\t");
        String pass = hashObj.scrambleString(password);
        writer.write(pass);
        writer.newLine();

        for (Map.Entry<String, String> entry : securityQuestionsAndAnswers.entrySet())
        {
            writer.write("\t" + entry.getKey() + ": " + hashObj.scrambleString(entry.getValue().toCharArray()));
            writer.newLine();
        }
        writer.write("}");

        writer.close();

        /*
        Ensures that the users file has been deleted and the tmp file has been renamed to "users.txt".
         */
        Files.delete(users);
        Files.move(tmp, users);
    }

    private void createFile(String username, char[] password, HashMap<String, String> securityQuestionsAndAnswers) throws IOException {
        Hash hashObj = new Hash();
        /*
				Creates a writer variable that will create and write to the new "users.txt" file. The file is written
				to with the new user's information. The writer is then closed.
				 */
        BufferedWriter writer = Files.newBufferedWriter(users);
        writer.write("{");
        writer.newLine();
        writer.write("\t" + username);
        writer.newLine();
        writer.write("\t");
        String pass = hashObj.scrambleString(password);
        writer.write(pass);
        writer.newLine();

        for (Map.Entry<String, String> entry : securityQuestionsAndAnswers.entrySet())
        {
            writer.write("\t" + entry.getKey() + ": " + hashObj.scrambleString(entry.getValue().toCharArray()));
            writer.newLine();
        }
        writer.write("}");

        writer.close();
    }

    private ArrayList<String> readUsersFromFile() throws IOException {
        ArrayList<String> differentAccounts = new ArrayList<>();

        fileSize = getFileSize(users);

        numberUsers = getNumberUsers(fileSize);

		/*
		Creates a reader variable and iterates through the document.
		 */
        BufferedReader reader = Files.newBufferedReader(users);

		/*
		Adds the different account usernames and closes the reader variable at the end.
		 */
        for (int i = 0; i < numberUsers; i++)
        {
            reader.readLine();
            differentAccounts.add(reader.readLine().strip());
            reader.readLine();
            reader.readLine();
            reader.readLine();
            reader.readLine();
            reader.readLine();
        }

        reader.close();

        return differentAccounts;
    }

    private String getPassword(String user) throws IOException {
        BufferedReader reader = Files.newBufferedReader(users);

		/*
		Initializes the hashed string variable and gets the file size.
		 */
        String hashedString = "";
        fileSize = getFileSize(users);

		/*
		Iterates through the file to find where the text in the file is equal to the current user.
		After finding the user, the hashed password is saved to the hashed string variable.
		 */
        for (int i = 0; i < fileSize - 1; i++)
        {
            if (reader.readLine().strip().equals(user))
            {
                hashedString = reader.readLine();
                hashedString = hashedString.strip();
                break;
            }
        }

		/*
		Close the reader variable and return the hashed string.
		 */
        reader.close();

        return hashedString;
    }

    private static int getFileSize(Path users) throws IOException {
		/*
		Initializes the fileLength variable to 0
		 */
        int fileLength = 0;

		/*
		Creates a temp buffered reader variable to iterate through the file.
		For each line that is not null, the file length is incremented.
		 */
        BufferedReader temp = Files.newBufferedReader(users);
        while (temp.readLine() != null){
            fileLength++;
        }

        temp.close();

		/*
		Returns the fileLength discovered through iteration.
		 */
        return fileLength;
    }

    private static int getNumberUsers(int fileSize){
		/*
		Creates a final int for the number of lines separating each user.
		 */
        final int numberLines = 7;

		/*
		Divides the total file size by the number of lines separating each user.
		 */
        return fileSize / numberLines;
    }
}
