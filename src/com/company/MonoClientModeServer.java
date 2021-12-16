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
                    String entry = in.readUTF();
                    String [] com = String.valueOf(entry).split(" ");
                    if (com[0].equalsIgnoreCase("/cat")) {
                        Cat cat = new Cat(com[1]);
                        String exitMs = cat.toString();
                        System.out.println(exitMs);
                        out.writeUTF("/cat "+exitMs);
                    }
                    out.writeUTF("Waiting for u... \n" +
                            "[/cat (name)]\n" +
                            "[/all (message)]\n" +
                            "[/direct (id & message)]\n" +
                            "[/exit]\n" +
                            "ur id: " + user.getId());


                    if (com[0].equalsIgnoreCase("/all")) {
                        String entryM="";
                        for (int count=1; count<com.length;count++){
                            entryM+=com[count]+" ";
                        }
                        System.out.println(user.getName() + ": " + entryM);
                        mBuffer.add(mBuffer.size(), user.getName() +", "+user.getId()+ ": " + entryM);
                    }

                    if (com[0].equalsIgnoreCase("/direct")){
                        String entryM="";
                        int id;
                        id = Integer.parseInt(com[1]);
                        for (int count=2; count<com.length;count++){
                            entryM+=com[count]+" ";
                        }
                        direct.put(id,user.getName() +", "+user.getId()+": (direct) "+ entryM);
                    }
                    if (com[0].equalsIgnoreCase("//cat")){
                        int id;
                        id = Integer.parseInt(com[1]);
                        Cat cat = new Cat(com[2]);
                        String exitMs = cat.toString();
                        System.out.println(exitMs);
                        direct.put(id,"//cat "+user.getName() +", "+user.getId()+": (direct cat) "+ exitMs);
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
