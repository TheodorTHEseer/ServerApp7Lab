package com.company;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static com.company.logs.home;

public class User {
    public int getId() {
        return id;
    }

    private int id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;
    private int accessLvl;
    private ArrayList<String> MessageBuffer = new ArrayList<>();
    public int MsCount = MessageBuffer.size();
    static public Map<Integer, String> UserDB = new HashMap<>();
    public User (){
        this.name = "Start";
    }
    public User(String name) {
        this.name = name;
        this.id = UserDB.size();
    }
    private String toSring(){
        String str = id+","+name+":";
        return str;
    }
    public void upload() throws IOException {
        logs.add("User|upload|Запись бд начата!");
        FileWriter fileWriter = new FileWriter(home + File.separator + "Desktop" + File.separator +
                "catsFolder" + File.separator + "UsersDB.txt", true);
        fileWriter.write(this.toString());
    }
    public void addMessage(String message){
        MessageBuffer.add(MessageBuffer.size(), message);
    }

    public static class UserDB {
        public static void download() {
            logs.add("User|download|Загрузка бд пользователей начата!");
            try {
                FileReader fileReader = new FileReader(home + File.separator + "Desktop" + File.separator +
                        "catsFolder" + File.separator + "UsersDB.txt");
                Scanner scanner = new Scanner(fileReader);
                try {
                    String[] users = scanner.nextLine().split(":");
                    logs.add("User|download|Массив разбит!");
                    for (int count = 0; count < users.length; count++) {
                        String[] paramsMas = users[count].split(",");
                        Integer id = Integer.parseInt(paramsMas[0]);
                        String name = String.valueOf(paramsMas[1]);
                        UserDB.put(id, name);
                    }
                    logs.add("User|download|Загрузка успешна");
                } catch (Exception exception) {
                    logs.add("User|download|Fail: " + exception.getMessage());
                }
                fileReader.close();
            } catch (Exception exception) {
                logs.add("User|download|Fail: " + exception.getMessage());
            }
        }
        public static ArrayList <String> mBuffer = new ArrayList<>(100);
        public static Map <Integer, String> direct = new HashMap<>(100);
    }
}
