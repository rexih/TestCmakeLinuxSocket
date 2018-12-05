#include "include/my-jni.h"
void JNICALL connectSocket(const char *ip, jint port);

extern "C"
JNIEXPORT jstring JNICALL
Java_cn_rexih_android_testjni_JniManager_getMessage(JNIEnv *env, jobject instance) {
    std::string hello = "call sub lib function:\ngetInt(): ";
    std::stringstream ss;
    ss << hello << getInt() << "\ngetLong(): " << getLong();
    return env->NewStringUTF(ss.str().c_str());
}

extern "C"
JNIEXPORT void JNICALL
Java_cn_rexih_android_testjni_JniManager_sendToSocket(JNIEnv *env, jobject instance,
                                                     jstring srvIp_, jint port) {
    const char *srvIp = env->GetStringUTFChars(srvIp_, 0);
    connectSocket(srvIp, port);
    env->ReleaseStringUTFChars(srvIp_, srvIp);
}



void  JNICALL connectSocket(const char *srvIp, int port) {
    // 1. 创建服务端地址的struct
    // sockaddr_in 在 <linux/in.h>
    struct sockaddr_in srvAddr;

    memset(&srvAddr, 0, sizeof(srvAddr));
    srvAddr.sin_family = PF_INET;
    // inet_addr 在 <arpa/inet.h>
    srvAddr.sin_addr.s_addr = inet_addr(srvIp);
    // htons 在 <endian.h>
    srvAddr.sin_port = htons(port);
    // 2. 创建客户端的socket套接字，获取到socket描述符
    // 0表示根据第二个参数自动设置第三个参数协议
    int socketDescriptor = socket(PF_INET, SOCK_STREAM, 0);
    if (0 > socketDescriptor) {
        printf("create socket error: %s(errno: %d)\n", strerror(errno), errno);
        return;
    }
    // 3. setsockopt设置此描述符对应的socket的属性
    struct timeval timeout = {1, 0};
    // 设置发送超时
    setsockopt(socketDescriptor, SOL_SOCKET, SO_SNDTIMEO, (char *) &timeout,
               sizeof(struct timeval));
    // 设置接收超时
    setsockopt(socketDescriptor, SOL_SOCKET, SO_RCVTIMEO, (char *) &timeout,
               sizeof(struct timeval));
    // 接收缓冲区设置为32K
    int nRecvBuf = 32 * 1024;
    setsockopt(socketDescriptor, SOL_SOCKET, SO_RCVBUF, (const char *) &nRecvBuf,
               sizeof(int));
    // 发送缓冲区设置为32K
    int nSendBuf = 32 * 1024;
    setsockopt(socketDescriptor, SOL_SOCKET, SO_SNDBUF, (const char *) &nSendBuf,
               sizeof(int));
    // 4. connect连接服务端地址
    int ret = connect(socketDescriptor, (struct sockaddr *) &srvAddr,
                      sizeof(struct sockaddr));
    if (0 > ret) {
        printf("connect error: %s(errno: %d)\n", strerror(errno), errno);
        return;
    }
    // 5. c/s发送和接收数据
    char buf[BUFSIZ];
    sprintf(buf, "from client:socket descriptor: %d ;random: %d", socketDescriptor, getInt());
    // send 数据
    if (0 > send(socketDescriptor, buf, strlen(buf), 0)) {
        perror("send msg error");
        return;
    }
    // 6. 关闭socket结束通讯
    shutdown(socketDescriptor, SHUT_RDWR);
}