package com.zaozao.hu.myapplication.utils;

import android.view.Surface;

public class JniUtils {


    public static native void open(String path);

    public static native boolean isReady();

    public static native double getPlayProgress();

    public static native void seek(double pos);

    public static native void close();

    public static native void initView(Surface surface);

    public static native void playOrPause();
}
