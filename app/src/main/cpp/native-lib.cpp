#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_cn_rexih_android_testjni_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from app Module Shared Lib";
    return env->NewStringUTF(hello.c_str());
}

