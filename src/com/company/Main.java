package com.company;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.company.logs.home;

public class Main {

    public static void main(String[] args) {
        Thread logs = new Thread(new logs(), "LogsThread");
        logs.start();
        startDisplay();
        startServer();
    }

    private static void startDisplay(){
        System.out.println("Первый запуск сервер?\n" +
                " [1]-Да." +
                " [2]-Нет.");
        Scanner in = new Scanner(System.in);
        int key = in.nextInt();
        if (key == 1)
            firstTimeLoad();
    }

    private static void firstTimeLoad(){
        File f = new File(home + File.separator + "Desktop" + File.separator +
                "CatsFolder");
        f.mkdir();
        Cat.loadCats();
    }


    private static void startServer(){
        ExecutorService executeIt = Executors.newFixedThreadPool(4);
        try (ServerSocket server = new ServerSocket(8000);
             BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
             System.out.println("Server socket создан. Консоль готова к использованию.");
             logs.add("Server|Start|Done");
            while (!server.isClosed()) {
                // проверяем поступившие комманды из консоли сервера если такие
                // были
                if (br.ready()) {
                    System.out.println("Server: ");
                    // если команда - !exit то инициализируем закрытие сервера и
                    // выход из цикла раздачии нитей монопоточных серверов
                    String serverCommand = br.readLine();
                    if (serverCommand.equalsIgnoreCase("!exit")) {
                        System.out.println("Main Server initiate exiting...");
                        server.close();
                        logs.add("Сервер заверишл работу!");
                        break;
                    }

                }

                // если комманд от сервера нет то становимся в ожидание
                // подключения к сокету общения под именем - "clientDialog" на
                // серверной стороне
                Socket client = server.accept();
                // после получения запроса на подключение сервер создаёт сокет
                // для общения с клиентом и отправляет его в отдельную нить
                // в Runnable(при необходимости можно создать Callable)
                // монопоточную нить = сервер - MonoThreadClientHandler и тот
                // продолжает общение от лица сервера
                executeIt.execute(new MonoClientModeServer(client));
                System.out.print("Произошло соединение!");
            }
            // закрытие пула нитей после завершения работы всех нитей
            executeIt.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
