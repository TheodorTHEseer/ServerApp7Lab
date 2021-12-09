package com.company;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class MonoClientModeServer implements Runnable{
    private static Socket clientDialog;

    public MonoClientModeServer(Socket client) {
        MonoClientModeServer.clientDialog = client;
    }

    @Override
    public void run() {

        try {
            // инициируем каналы общения в сокете, для сервера

            // канал записи в сокет следует инициализировать сначала канал чтения для избежания блокировки выполнения программы на ожидании заголовка в сокете
            try (DataOutputStream out = new DataOutputStream(clientDialog.getOutputStream())) {

// канал чтения из сокета
                DataInputStream in = new DataInputStream(clientDialog.getInputStream());
                System.out.println("Канал входа готов.");
                System.out.println("Канал выхода готов.");
                logs.add("Server|DataRead|Done");
                ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                // основная рабочая часть //
                //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

                // начинаем диалог с подключенным клиентом в цикле, пока сокет не
                // закрыт клиентом
                while (!clientDialog.isClosed()) {
                    System.out.println("Server reading from channel");

                    // серверная нить ждёт в канале чтения (inputstream) получения
                    // данных клиента после получения данных считывает их
                    String entry = in.readUTF();

                    // и выводит в консоль
                    System.out.println("Client: " + entry);

                    // инициализация проверки условия продолжения работы с клиентом
                    // по этому сокету по кодовому слову - quit в любом регистре
                    if (entry.equalsIgnoreCase("quit")) {

                        // если кодовое слово получено то инициализируется закрытие
                        // серверной нити
                        System.out.println("Client initialize connections suicide ...");
                        out.writeUTF("Server reply - " + entry + " - OK");
                        Thread.sleep(3000);
                        break;
                    }

                    // если условие окончания работы не верно - продолжаем работу -
                    // отправляем эхо обратно клиенту

                    System.out.println("Server try writing to channel");
                    Cat cat = new Cat(entry);
                    out.writeUTF(cat.toString());
                    System.out.println("Server Wrote message to clientDialog.");

                    // освобождаем буфер сетевых сообщений
                    out.flush();

                    // возвращаемся в началло для считывания нового сообщения
                }

                ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                // основная рабочая часть //
                //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

                // если условие выхода - верно выключаем соединения
                System.out.println("Client disconnected");
                System.out.println("Closing connections & channels.");

                // закрываем сначала каналы сокета !
                in.close();
                out.close();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
            // потом закрываем сокет общения с клиентом в нити моносервера
            clientDialog.close();

            System.out.println("Closing connections & channels - DONE.");
            logs.add("Server|ShutDown|Done");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
