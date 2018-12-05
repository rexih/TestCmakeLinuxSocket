package cn.rexih.android.testjni;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import cn.rexih.android.testjni.service.ServerSocketService;


public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    public static final String ACTION_SERVER_RECEIVER = "serverReceiver";
    public static final String PARAM_CONTENT = "content";

    private TextView tv_main_app;
    private TextView tv_main_sub_lib;
    private Button   btn_main_request_socket;
    private EditText et_main_ip;
    private EditText et_main_port;
    private ServerReceiver serverReceiver;
    private TextView tv_main_receive;
    private ScrollView sv_main_scroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); setContentView(R.layout.activity_main);

        serverReceiver = new ServerReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_SERVER_RECEIVER);
        registerReceiver(serverReceiver, intentFilter);

        startService(new Intent(this, ServerSocketService.class));

        tv_main_app = (TextView) findViewById(R.id.tv_main_app);
        tv_main_sub_lib = (TextView) findViewById(R.id.tv_main_sub_lib);
        sv_main_scroll = (ScrollView) findViewById(R.id.sv_main_scroll);
        tv_main_receive = (TextView) findViewById(R.id.tv_main_receive);
        btn_main_request_socket = (Button) findViewById(R.id.btn_main_request_socket);
        et_main_ip = (EditText) findViewById(R.id.et_main_ip);
        et_main_port = (EditText) findViewById(R.id.et_main_port);
        tv_main_app.setText(stringFromJNI()); tv_main_sub_lib.setText(JniManager
                .getInstance()
                .getMessage());
        btn_main_request_socket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String strIp = et_main_ip
                        .getText()
                        .toString()
                        .trim();

                final String ip = TextUtils.isEmpty(strIp) ?
                        "127.0.0.1" :
                        strIp;

                String strPort = et_main_port
                        .getText()
                        .toString()
                        .trim();

                int tmpPort = 0;
                try {
                    tmpPort = Integer.parseInt(strPort);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                if (0 == tmpPort) {
                    tmpPort = 9912;
                }

                final int port = tmpPort;

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        JniManager
                                .getInstance()
                                .sendToSocket(ip, port);
                    }
                }).start();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(serverReceiver);
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
    
    
    private class ServerReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String content = intent.getStringExtra(PARAM_CONTENT);
            System.out.println("ServerReceiver:" + content);
            tv_main_receive.append("\n"+content);
            sv_main_scroll.post(new Runnable() {
                @Override
                public void run() {
                    sv_main_scroll.fullScroll(ScrollView.FOCUS_DOWN);
                }
            });

        }
    }
}
