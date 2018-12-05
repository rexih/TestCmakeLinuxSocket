package cn.rexih.android.testjni;

/**
 * @author huangwr
 * @version %I%, %G%
 * @package cn.rexih.android.testjni
 * @file JniManager
 * @date 2018/11/17
 */
public class JniManager {

    private static class JniManagerHolder {
        private static JniManager instance = new JniManager();
    }

    public static JniManager getInstance() {
        return JniManagerHolder.instance;
    }

    static {
        System.loadLibrary("my-jni");
    }

    public native String getMessage();

    public native void sendToSocket(String srvIp, int port);
}
