package cn.rexih.android.testjni.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import cn.rexih.android.testjni.MainActivity;


/**
 * @author huangwr
 * @version %I%, %G%
 * @package cn.rexih.android.testjni.service
 * @file ServerSocketService
 * @date 2018/12/5
 */
public class ServerSocketService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate(); new Thread(new Runnable() {
            @Override
            public void run() {
                initServer();
            }
        }).start();
    }

    private void initServer() {
        try {
            ServerSocket serverSocket = new ServerSocket();
            InetSocketAddress inetSocketAddress = new InetSocketAddress
                    ("127.0.0.1", 9912);
            serverSocket.bind(inetSocketAddress); Socket accept; while (true) {
                accept = serverSocket.accept();
                new Thread(new WorkRunnable(accept)).start();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public class WorkRunnable implements Runnable {

        private BufferedReader bufferedReader;
        private Socket         client;

        public WorkRunnable(Socket client) {
            this.client = client; try {
                bufferedReader = new BufferedReader(new InputStreamReader(client
                        .getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            //循环读取内容
            String content = null; try {
                while ((content = bufferedReader.readLine()) != null) {
                    System.out.println("ServerSocketService接收到了客户端：" + content);
                    Intent intent = new Intent(MainActivity
                            .ACTION_SERVER_RECEIVER).putExtra(MainActivity
                            .PARAM_CONTENT, content);
                    getApplicationContext().sendBroadcast(intent);
                } Thread.sleep(1000); client.close(); bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
