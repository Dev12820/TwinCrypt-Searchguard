/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DualServer;

/**
 *
 * @author java1
 */
import java.io.File;
import java.io.FileInputStream;
import org.apache.commons.net.ftp.FTPClient;

public class FTPcon {

    FTPClient client = new FTPClient();
    FileInputStream fis = null;
    boolean status;

    /**
     *
     * @param file
     * @return
     */
  public boolean upload(File file) {
    try {
        System.out.println("Check------------------------------------->1");
        client.connect("ftp.drivehq.com");
        System.out.println("Connected to the server: " + client.isConnected());

        if (client.login("devansh128", "Saanvime10@")) {
            System.out.println("Login successful");
        } else {
            System.out.println("Login failed");
            return false;
        }

        client.enterLocalActiveMode(); // Use active mode

        // Check if the file exists
        if (!file.exists()) {
            System.out.println("File does not exist: " + file.getAbsolutePath());
            return false;
        }

        System.out.println("Uploading file: " + file.getAbsolutePath());
        try (FileInputStream fis = new FileInputStream(file)) {
            System.out.println("Check------------------------------------->2");
            status = client.storeFile("/" + file.getName(), fis);
        }

        if (!status) {
            System.out.println("File upload failed.");
            System.out.println("Error: " + client.getReplyString());
        } else {
            System.out.println("File upload successful.");
        }

        client.logout();
        client.disconnect();

    } catch (Exception e) {
        e.printStackTrace(); // Print full stack trace for debugging
    }

    return status; // Return the status of the upload
}



}
