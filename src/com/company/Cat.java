package com.company;

import com.company.logs;

import java.io.*;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

import static com.company.logs.home;

public class Cat {
    private static final Map <String, String> nameRace = new HashMap<>();
    private static final Map <String, Double> nameParams = new HashMap<>();
    private static final String [] names = {"лютик", "барсик", "мурка", "тигра", "летиция", "аэлита"};
    private static final double [] params = {1.2, 2.3, 4.3, 1.7, 2.3, 3.3};
    private static void load(){
        nameRace.put(names[0], "Abyssinian");
        nameRace.put(names[1], "Persian");
        nameRace.put(names[2], "Brazilian Shorthair");
        nameRace.put(names[3], "Burmilla");
        nameRace.put(names[4], "York");
        nameRace.put(names[5], "British Longhair");
        for (int count = 0; count<names.length; count++){
            nameParams.put(names[count], params[count]);
        }

    }
    public static void loadCats(){
        load();
    }
    private String name;
    private String race;
    private double weight;

    public Cat (int num){
        load();
        name = names[num];
        race = nameRace.get(name);
        weight = params[num];
    }

    public Cat (String name){
            load();
            name.toLowerCase(Locale.ROOT);
            try {
                this.name=name;
                this.race = nameRace.get(name);
                this.weight = nameParams.get(name);
            }
            catch (Exception e){
                System.out.println("Кошки нет" + e.getMessage());
            }
    }

    public String toString() {
        return "name," + name + ",race," + race +
                ",weight," +weight;
    }
    public void fromString(String catString){
        String [] CatMas = catString.split(",");
        this.name=CatMas[1];
        this.race=CatMas[3];
        this.weight=Double.valueOf(CatMas[5]);
    }

    public void upload(){
        try {
            FileWriter fileWriter = new FileWriter(home + File.separator + "Desktop" + File.separator +
                    "CatsFolder" + File.separator + name +".txt", false);
            fileWriter.write(this.toString());
            fileWriter.close();
            logs.add("Cat|upload|Done");
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
            logs.add("Cat|upload|Failed");
        }
    }
    public void download(){
        String catString=null;
        try {
            FileReader fileReader = new FileReader(home + File.separator + "Desktop" + File.separator +
                    "testGameFolder" + File.separator + "SettlementBuildings.txt");
            Scanner scanner = new Scanner(fileReader);
            catString=scanner.nextLine();
            fileReader.close();
            logs.add("Cat|download|1|Done");
        }
        catch (Exception exception){
            logs.add("Cat|download|1|Failed");
        }
        try {
            fromString(catString);
            logs.add("Cat|download|2|Done");
        }
        catch (Exception e) {
            logs.add("Cat|download|2|Failed");
        }
    }
}
