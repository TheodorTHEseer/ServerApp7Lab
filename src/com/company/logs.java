package com.company;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class logs implements Runnable{
    public static String home = System.getProperty("user.home");
    public static ArrayList<String> logs;

    @Override
    public void run() {
        try {
            Thread.sleep(5000);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void add(String str) {
        logs.add(logs.size(), str);
    }
    public static void uploadLogs(){
        try {
            FileWriter fileWriter = new FileWriter(home + File.separator + "Desktop" + File.separator +
                    "CatsFolder" + File.separator +"logs.txt", false);
            fileWriter.write(logs.toString());
            fileWriter.close();
            logs.add("Logs|upload|Done");
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
            logs.add("Logs|upload|Failed");
        }
    }
}
