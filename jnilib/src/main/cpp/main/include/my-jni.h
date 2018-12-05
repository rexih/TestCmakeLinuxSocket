#ifndef TESTJNI_MY_JNI_H
#define TESTJNI_MY_JNI_H
#include <jni.h>
#include <string>
#include <sstream>
#include <sys/types.h>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <my-int.h>
#include <my-long.h>
//#include "../../myint/include/my-int.h"
//#include "../../mylong/include/my-long.h"
#endif //TESTJNI_MY_JNI_H

extern "C"
JNIEXPORT jstring JNICALL
Java_cn_rexih_android_testjni_JniManager_getMessage(JNIEnv *env, jobject instance);

extern "C"
JNIEXPORT void JNICALL
Java_cn_rexih_android_testjni_JniManager_sendToSocket(JNIEnv *env, jobject instance,
                                                     jstring srvIp_, jint port);

