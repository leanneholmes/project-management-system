package controllers;

import org.primefaces.model.StreamedContent;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * This class is the controller for managing database backup file downloads.
 * 
 * @author The Bug Busters
 * @version 1
 */
@Named("fileController")
@SessionScoped
public class FileDownloadController implements Serializable {

    /** Database Backup File. */
    private StreamedContent file;

    /**
     * Default Constructor.
     * Initializes the database backup file to be downloaded.
     * 
     * @throws FileNotFoundException if the file is not found
     */
    public FileDownloadController() {

    }

    /**
     * Prints file's content to console.
     * 
     * @throws FileNotFoundException if the file is not found
     */
    public void printFile() throws FileNotFoundException {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        String imagePath = externalContext.
            getRequestContextPath() + "/resources/dump/backup.sql";

        File dataFile = new File(imagePath);
        Scanner fileScanner = new Scanner(dataFile);

        if (fileScanner.hasNextLine()) {
            System.out.println(fileScanner.nextLine());
        }

        fileScanner.close();
    }

    /**
     * Creates a MySQL dump of a specified database 
     * and writes it to a file at the specified path.
     * 
     * @param dbName the name of the database to be dumped
     * @param dumpPath the path where the dump file should be saved
     * @param mysqlPath the path to the MySQL installation directory
     * @param username the username to connect to the MySQL server
     * @param password the password to connect to the MySQL server
     * @throws IOException if there is an error writing to the file 
     *      or executing the command
     */
    public static void createDump(String dbName, String dumpPath, 
        String mysqlPath, String username, String password)
            throws IOException {
        List<String> commands = new ArrayList<String>();
        commands.add(mysqlPath + "\\resources\\mysqldump");
        commands.add("-u" + username);
        commands.add("-p" + password);
        commands.add(dbName);
        commands.add("-r");
        commands.add(dumpPath);

        ProcessBuilder pb = new ProcessBuilder(commands);
        pb.start();
    }

    /**
     * Gets the database backup file.
     * 
     * @return the database backup file
     */
    public StreamedContent getFile() {
        return file;
    }
}
