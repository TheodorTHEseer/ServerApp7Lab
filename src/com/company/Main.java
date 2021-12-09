package com.company;

import java.io.File;
import java.util.Scanner;

import static com.company.logs.home;

public class Main {

    public static void main(String[] args) {
        createDir();
        in = Scanner(System.in());

    }
    private static void createDir(){
        File f = new File(home + File.separator + "Desktop" + File.separator +
                "CatsFolder");
        f.mkdir();
    }
    private static void startServer(){

    }
}
