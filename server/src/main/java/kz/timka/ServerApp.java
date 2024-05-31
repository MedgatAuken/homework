package kz.timka;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class ServerApp {

    // 2. Пусть сервер подсчитывает количество сообщений от клиента
    // 3. Если клиент отправит команду /stat, то сервер должен выслать клиенту
    // сообщение 'Количество сообщений - n'
    public static void main( String[] args ) {
        try(ServerSocket serverSocket = new ServerSocket(8189)) {
            System.out.println("Сервер запущен на порту 8189. Ожидаем подключение клиента...");
            Socket socket = serverSocket.accept();
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            System.out.println("Клиент подключился");


            int messagesCount = 0;
            String msg;
            while (true) {
                msg = in.readUTF();

                if(msg.equals("/stat")) {
                    out.writeUTF("Количество сообщений - " + messagesCount);
                    continue;
                } else {
                    System.out.print(msg + "\n");
                    out.writeUTF("ECHO: " + msg);
                    messagesCount++;
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }

    }
}
