#include <jni.h>
#include <string>

#include "XLog.h"

#include "IPlayProxy.h"
#include <android/native_window_jni.h>


#define CLASS com_zaozao_hu_myapplication_utils_JniUtils
#define JNI_METHOD3(CLASS3, FUNC3) Java_##CLASS3##_##FUNC3
#define JNI_METHOD2(CLASS2, FUNC2) JNI_METHOD3(CLASS2, FUNC2)
#define JNI_METHOD(FUNC) JNI_METHOD2(CLASS, FUNC)

extern "C"
JNIEXPORT jint JNI_OnLoad(JavaVM *vm, void *res) {


    IPlayProxy::Get()->Init(vm);
    //http://ivi.bupt.edu.cn/hls/cctv1hd.m3u8
    // IPlayProxy::Get()->Open("http://devimages.apple.com.edgekey.net/streaming/examples/bipbop_4x3/gear2/prog_index.m3u8"); //ios测试
    // IPlayProxy::Get()->Open("http://ivi.bupt.edu.cn/hls/cctv6hd.m3u8"); //cctv6
    // IPlayProxy::Get()->Open("http://ivi.bupt.edu.cn/hls/cctv5phd.m3u8"); //cctv5
    // IPlayProxy::Get()->Open("http://ivi.bupt.edu.cn/hls/cctv3hd.m3u8"); //cctv3
    // IPlayProxy::Get()->Open("http://ivi.bupt.edu.cn/hls/cctv1hd.m3u8"); //cctv1


//    IPlayProxy::Get()->Open("/sdcard/DCIM/Camera/VID_20190503_100122.mp4");
//    IPlayProxy::Get()->Start();


//    IPlayProxy::Get()->Open("/sdcard/tencent/QQfile_recv/v1080.mp4");
//    IPlayProxy::Get()->Start();


    return JNI_VERSION_1_6;
}


extern "C"
JNIEXPORT void JNICALL
JNI_METHOD(initView)(JNIEnv *env, jclass instance, jobject surface) {

    ANativeWindow *nativeWindow = ANativeWindow_fromSurface(env, surface);
    IPlayProxy::Get()->InitView(nativeWindow);

}

extern "C"
JNIEXPORT jboolean JNICALL
JNI_METHOD(isReady)(JNIEnv *env, jclass instance) {
    bool isReady = IPlayProxy::Get()->IsReady();
    return static_cast<jboolean>(isReady);
}

extern "C"
JNIEXPORT void JNICALL
JNI_METHOD(open)(JNIEnv *env, jclass instance, jstring path_) {
    const char *path = env->GetStringUTFChars(path_, nullptr);
    IPlayProxy::Get()->Open(path); //香港卫视
    IPlayProxy::Get()->Start();
    env->ReleaseStringUTFChars(path_, path);
}

extern "C"
JNIEXPORT jdouble JNICALL
JNI_METHOD(getPlayProgress)(JNIEnv *env, jclass instance) {
    return IPlayProxy::Get()->PlayPos();

}

extern "C"
JNIEXPORT void JNICALL
JNI_METHOD(playOrPause)(JNIEnv *env, jclass instance) {
    IPlayProxy::Get()->SetPause(!IPlayProxy::Get()->IsPause());
}

extern "C"
JNIEXPORT void JNICALL
JNI_METHOD(seek)(JNIEnv *env, jclass instance, jdouble pos) {
    IPlayProxy::Get()->Seek(pos);
}

extern "C"
JNIEXPORT void JNICALL
JNI_METHOD(close)(JNIEnv *env, jclass instance) {
    IPlayProxy::Get()->Close();
}
