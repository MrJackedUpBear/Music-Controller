package src.MusicController;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.awt.Desktop;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

import javax.swing.*;

public class musicSources {

    private static final MusicSourceDatabase database = new MusicSourceDatabase();
    private static final Encoder encoder = new Encoder();

    /*
    Opens the YouTube music website
     */
    public static void openYoutubeMusic() throws URISyntaxException
    {
        JOptionPane.showMessageDialog(null, "Currently YouTube music does not" +
                "have an option for storing login information, so the login will be based on the" +
                "current browser.", "YouTube", JOptionPane.WARNING_MESSAGE);
        try {
            Desktop.getDesktop().browse(new URI("https://music.youtube.com"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /*
    Opens the Spotify website
     */
    public static void openSpotifyMusic(String user) throws URISyntaxException
    {
        System.setProperty("webdriver.chrome.driver", "C:\\ChromeDriver\\chromedriver.exe");

        String usernameAndPassword = "";
        char[] username = "".toCharArray();
        char[] password = "".toCharArray();

        try {
            usernameAndPassword = database.getLoginInfo(user, "Spotify");
        } catch (FileNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }

        if (usernameAndPassword == null || usernameAndPassword.contains("Placeholder")){
            username = JOptionPane.showInputDialog(null, "It appears you have no login for Spotify. " +
                    "Please enter your username for Spotify.").toCharArray();
            password = Objects.requireNonNull(getPassword()).toCharArray();

            try {
                username = Encoder.encrypt(username);
                password = Encoder.encrypt(password);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error saving to file",
                        "Login", JOptionPane.ERROR_MESSAGE);
            }

            database.createLogin(user, "Spotify", username, password);
        }
        else{
            String[] temp = usernameAndPassword.strip().split("\t");
            username = temp[0].strip().toCharArray();
            password = temp[1].strip().toCharArray();
        }

        try {
            username = Encoder.decrypt(username);
            password = Encoder.decrypt(password);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Unable to load login" +
                    "info", "Login", JOptionPane.ERROR_MESSAGE);
        }

        ChromeOptions options = new ChromeOptions();

        options.addArguments("--profile-directory=Default");
        options.addArguments("--disable-web-security");

        WebDriver driver = new ChromeDriver(options);

        driver.get("https://accounts.spotify.com/en/login?allow_password=1&continue=https%3A%2F%2Fopen.spotify.com%2F%3Fflow_ctx%3D4c1e7424-3027-4feb-a5c2-2237a01122d0%3A1745058016&flow_ctx=4c1e7424-3027-4feb-a5c2-2237a01122d0%3A1745058016");

        WebElement usernameText = driver.findElement(By.id("login-username"));
        WebElement passwordText = driver.findElement(By.id("login-password"));
        WebElement submitButton = driver.findElement(By.id("login-button"));

        usernameText.sendKeys(String.valueOf(username));
        passwordText.sendKeys(String.valueOf(password));
        submitButton.click();
    }

    /*
    Opens the IHeartRadio website
     */
    public static void openIHeartRadio(String user) throws URISyntaxException
    {
        System.setProperty("webdriver.chrome.driver", "C:\\ChromeDriver\\chromedriver.exe");

        String usernameAndPassword = "";
        char[] username = "".toCharArray();
        char[] password = "".toCharArray();

        try {
            usernameAndPassword = database.getLoginInfo(user, "IHeartRadio");
        } catch (FileNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }

        if (usernameAndPassword == null || usernameAndPassword.contains("Placeholder")){
            username = JOptionPane.showInputDialog(null, "It appears you have no login for IHeartRadio. " +
                    "Please enter your username for IHeartRadio.").toCharArray();
            password = Objects.requireNonNull(getPassword()).toCharArray();

            try {
                username = Encoder.encrypt(username);
                password = Encoder.encrypt(password);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error saving to file",
                        "Login", JOptionPane.ERROR_MESSAGE);
            }
            database.createLogin(user, "IHeartRadio", username, password);
        }
        else{
            String[] temp = usernameAndPassword.strip().split("\t");
            username = temp[0].strip().toCharArray();
            password = temp[1].strip().toCharArray();
        }

        try {
            username = Encoder.decrypt(username);
            password = Encoder.decrypt(password);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Unable to load login" +
                    "info", "Login", JOptionPane.ERROR_MESSAGE);
        }

        ChromeOptions options = new ChromeOptions();

        options.addArguments("--profile-directory=Default");
        options.addArguments("--disable-web-security");

        WebDriver driver = new ChromeDriver(options);

        driver.get("https://www.iheart.com");

        WebElement submitButton = driver.findElement(By.cssSelector("button[title=Account]"));
        submitButton.click();

        WebElement userInput = driver.findElement(By.id("username"));
        WebElement passwordInput = driver.findElement(By.id("password"));

        userInput.sendKeys(String.valueOf(username));
        passwordInput.sendKeys(String.valueOf(password));
    }

    /*
    Opens the SoundCloud website
     */
    public static void openSoundCloud(String user) throws URISyntaxException
    {
        System.setProperty("webdriver.chrome.driver", "C:\\ChromeDriver\\chromedriver.exe");

        String usernameAndPassword = "";
        char[] username = "".toCharArray();
        char[] password = "".toCharArray();

        try {
            usernameAndPassword = database.getLoginInfo(user, "SoundCloud");
        } catch (FileNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }

        if (usernameAndPassword == null || usernameAndPassword.contains("Placeholder")){
            username = JOptionPane.showInputDialog(null, "It appears you have no login for SoundCloud. " +
                    "Please enter your username for SoundCloud.").toCharArray();
            password = Objects.requireNonNull(getPassword()).toCharArray();

            try {
                username = Encoder.encrypt(username);
                password = Encoder.encrypt(password);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error saving to file",
                        "Login", JOptionPane.ERROR_MESSAGE);
            }

            database.createLogin(user, "SoundCloud", username, password);
        }
        else{
            String[] temp = usernameAndPassword.strip().split("\t");
            username = temp[0].strip().toCharArray();
            password = temp[1].strip().toCharArray();
        }

        try {
            username = Encoder.decrypt(username);
            password = Encoder.decrypt(password);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Unable to load login" +
                    "info", "Login", JOptionPane.ERROR_MESSAGE);
        }

        ChromeOptions options = new ChromeOptions();

        options.addArguments("--profile-directory=Default");
        options.addArguments("--disable-web-security");

        WebDriver driver = new ChromeDriver(options);

        driver.get("https://soundcloud.com/");
    }

    public static String getPassword() {
        JPasswordField jpf = new JPasswordField(24);
        JLabel jl = new JLabel("Enter Your Password: ");
        Box box = Box.createHorizontalBox();
        box.add(jl);
        box.add(jpf);
        int x = JOptionPane.showConfirmDialog(null, box, "Password Entry", JOptionPane.OK_CANCEL_OPTION);

        if (x == JOptionPane.OK_OPTION) {
            return String.valueOf(jpf.getPassword());
        }
        return null;
    }
}
