package src.MusicController;

import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLOutput;
import java.util.Arrays;

public class MusicSourceDatabase {
    private final Path musicSources = Path.of("musicSource.txt");
    private final Path tmp = Path.of("tmp.txt");
    private final int numberLines = 6;
    private final int numberMusicSources = 3;

    public String getLoginInfo(String user, String musicSource) throws FileNotFoundException {
        String usernameAndPassword = "";
        String musicSourceText = "";

        boolean userExists = false;
        try {
            userExists = checkUser(user);
        } catch (IOException e) {
            return null;
        }

        if (!userExists){
            JOptionPane.showMessageDialog(null, "User not found", "Music Sources", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        try {
            musicSourceText = checkMusicSource(user, musicSource);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }

        String[] temp = musicSourceText.split(":");
        usernameAndPassword = temp[1];

        return usernameAndPassword;
    }

    public void createLogin(String user, String musicSource, char[] username, char[] password){
        boolean check;
        try {
            check = checkUser(user);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
            check = false;
        }

        if(check){
            try {
                updateUser(user, musicSource, username, password);
            } catch (IOException e) {
                System.out.println("Unable to update user.");
            }
        }
        else{
            try {
                createUser(user, musicSource, username, password);
            } catch (IOException e) {
                System.out.println("Unable to create user.");
            }
        }
    }

    private void createUser(String user, String musicSource, char[] username, char[] password) throws IOException {
        int fileSize = getFileSize(musicSources);
        int numberUsers = getNumberUsers(fileSize);

        BufferedReader reader = Files.newBufferedReader(musicSources);
        BufferedWriter writer = Files.newBufferedWriter(tmp);

        for (int i = 0; i < numberUsers; i++){
            for (int j = 0; j < numberLines; j++){
                writer.write(reader.readLine() + "\n");
            }
        }

        reader.close();

        writer.write("{" + "\n");
        writer.write("\t" + user + "\n");
        String musicSourceString = "\t" + musicSource + ": " + String.valueOf(username) + "\t" + String.valueOf(password);
        if (musicSource.equals("Spotify")){
            writer.write(musicSourceString + "\n");
        }
        else{
            writer.write("\t" + "Spotify" + ": " + "Placeholder" + "\t" + "Placeholder" + "\n");
        }

        if (musicSource.equals("IHeartRadio")){
            writer.write(musicSourceString + "\n");
        }
        else{
            writer.write("\t" + "IHeartRadio" + ": " + "Placeholder" + "\t" + "Placeholder" + "\n");
        }

        if (musicSource.equals("SoundCloud")){
            writer.write(musicSourceString + "\n");
        }
        else{
            writer.write("\t" + "SoundCloud" + ": " + "Placeholder" + "\t" + "Placeholder" + "\n");
        }
        writer.write("}" + "\n");

        writer.close();

        Files.delete(musicSources);
        Files.move(tmp, musicSources);
    }

    private void updateUser(String user, String musicSource, char[] username, char[] password) throws IOException {
        int fileSize = getFileSize(musicSources);
        int numberUsers = getNumberUsers(fileSize);

        BufferedReader reader = Files.newBufferedReader(musicSources);
        BufferedWriter writer = Files.newBufferedWriter(tmp);

        for (int i = 0; i < numberUsers; i++){
            writer.write(reader.readLine() + "\n");
            String u = reader.readLine();
            writer.write(u + "\n");
            if (u.strip().equals(user)){
                for (int j = 0; j < numberMusicSources; j++){
                    String temp = reader.readLine();
                    if (temp.strip().contains(musicSource)){
                        String concatenated = "\t" + musicSource + ": " + String.valueOf(username) + "\t" + String.valueOf(password);
                        writer.write(concatenated + "\n");
                    }
                    else{
                        writer.write(temp + "\n");
                    }
                    i += 1;
                }
            }
            else{
                writer.write(reader.readLine() + "\n");
                writer.write(reader.readLine() + "\n");
                writer.write(reader.readLine() + "\n");
            }
            writer.write(reader.readLine() + "\n");
        }

        reader.close();
        writer.close();

        Files.delete(musicSources);
        Files.move(tmp, musicSources);
    }

    private boolean checkUser(String user) throws IOException {
        int fileSize = getFileSize(musicSources);
        int numberUsers = getNumberUsers(fileSize);

        BufferedReader reader = Files.newBufferedReader(musicSources);

        for (int i = 0; i < numberUsers; i++){
            reader.readLine();
            if (reader.readLine().strip().equals(user)){
                return true;
            }
            reader.readLine();
            reader.readLine();
            reader.readLine();
            reader.readLine();
        }
        reader.close();

        return false;
    }

    private String checkMusicSource(String user, String musicSource) throws IOException {
        int fileSize = getFileSize(musicSources);
        int numberUsers = getNumberUsers(fileSize);

        BufferedReader reader = Files.newBufferedReader(musicSources);

        for (int i = 0; i < numberUsers; i++){
            reader.readLine();
            if (reader.readLine().strip().equals(user)){
                for (int j = 0; j < numberMusicSources; j++){
                    String temp = reader.readLine();
                    if (temp.contains(musicSource)){
                        return temp;
                    }
                }
                reader.readLine();
            }
            else{
                reader.readLine();
                reader.readLine();
                reader.readLine();
                reader.readLine();
            }
        }

        reader.close();
        return null;
    }

    private int getNumberUsers(int fileSize){
        return fileSize / numberLines;
    }

    private int getFileSize(Path file) throws IOException {
        BufferedReader reader = Files.newBufferedReader(file);
        int fileSize = 0;

        while (reader.readLine() != null){
            fileSize++;
        }
        reader.close();

        return fileSize;
    }
}
