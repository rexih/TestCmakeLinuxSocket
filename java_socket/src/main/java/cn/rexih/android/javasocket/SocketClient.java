package cn.rexih.android.javasocket;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;


/**
 * @author huangwr
 * @version %I%, %G%
 * @package cn.rexih.android.javasocket
 * @file SocketClient
 * @date 2018/12/5
 */
public class SocketClient {
    public static void main(String[] args){
        try {
            Socket socket = new Socket("192.168.1.6", 9912);
            OutputStream os = socket.getOutputStream();
            PrintWriter pw =new PrintWriter(os);//将输出流包装成打印流
            pw.write("用户名：admin；密码：123");
            pw.flush();
            socket.shutdownOutput();
            pw.close();
            os.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
