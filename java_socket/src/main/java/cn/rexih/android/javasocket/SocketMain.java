package cn.rexih.android.javasocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;


public class SocketMain {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket();
            InetSocketAddress inetSocketAddress = new InetSocketAddress("192.168.1.6", 9912);
            serverSocket.bind(inetSocketAddress);
            Socket accept;
            while (true){
                accept = serverSocket.accept();
                new Thread(new WorkRunnable(accept)).start();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class WorkRunnable implements Runnable {

        private BufferedReader bufferedReader;
        private Socket         client;

        public WorkRunnable(Socket client) {
            this.client = client;
            try {
                bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            //循环读取内容
            String content = null;
            while(true){
                try {
                    while((content = bufferedReader.readLine())!=null){
                        System.out.println("接收到了客户端："+content);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
