package com.company;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import static com.company.User.UserDB.direct;
import static com.company.User.UserDB.mBuffer;

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

                DataInputStream in = new DataInputStream(clientDialog.getInputStream());
                logs.add("Server|DataRead|Done");
                User user = new User();
                while (true) {
                    System.out.println("Server reading from channel");

                    if(user.getName().equals("Start")) {
                        out.writeUTF("Ведите желаемое имя>>");
                        String entry = in.readUTF();
                        user.setName(entry);
                        out.writeUTF("Done");
                    }
                    out.writeUTF("...");
                    String entry = in.readUTF();
                    String [] com = String.valueOf(entry).split(" ");

                    if (com[0].equalsIgnoreCase("/all")) {
                        String entryM="";
                        for (int count=1; count<com.length;count++){
                            entryM+=com[count]+" ";
                        }
                        System.out.println(user.getName() + ": " + entryM);
                        mBuffer.add(mBuffer.size(), user.getName() + ": " + entryM);
                    }

                    if (com[0].equalsIgnoreCase("/direct")){
                        out.writeUTF("Кому хотите отправить сообщение [id]?");
                        int id = Integer.parseInt(in.readUTF());
                        out.writeUTF("Кому хотите отправить сообщение [id]?");
                        String message = in.readUTF();
                        direct.put(id,user.getName()+": (direct) "+ message);
                    }

                    if (mBuffer.size()>0) {
                        out.writeUTF(mBuffer.get(0));
                        mBuffer.remove(0);
                    }
                    if (direct.get(user.getId())!=null){
                        out.writeUTF(direct.get(user.getId()));
                        direct.put(user.getId(),null);
                    }

                    if (com[0].equalsIgnoreCase("/exit")) {

                        // если кодовое слово получено то инициализируется закрытие
                        // серверной нити
                        System.out.println("Client initialize connections suicide ...");
                        out.writeUTF("Server reply - " + entry + " - OK");
                        Thread.sleep(6000);
                        break;
                    }
                    if (com[0].equalsIgnoreCase("/cat")) {
                        Cat cat = new Cat(entry);
                        out.writeUTF("Название>>");
                        String exitMs = cat.toString();
                        System.out.println(exitMs);
                        out.writeUTF(exitMs);
                    }

                    // освобождаем буфер сетевых сообщений
                    out.flush();
                }
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
