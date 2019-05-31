package com.zaozao.hu.myapplication.ui;

import android.Manifest;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import com.zaozao.hu.myapplication.R;
import com.zaozao.hu.myapplication.bean.NetWorkChangeEvent;
import com.zaozao.hu.myapplication.utils.Constants;
import com.zaozao.hu.myapplication.utils.LogUtils;
import com.zaozao.hu.myapplication.utils.network.NetworkConnectChangedReceiver;
import com.zaozao.hu.myapplication.utils.network.NetworkUtils;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public abstract class BaseActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    protected String TAG = this.getClass().getSimpleName();
    protected boolean mCheckNetwork = true;//默认检查网络状态
    private View mTipView;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mParams;
    private NetworkConnectChangedReceiver mNetworkChangedReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTipView();
        registerNetworkBroadcast();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        hasNetWork(NetworkUtils.isConnected(this));
    }

    /**
     * 注册网络监听广播
     */
    private void registerNetworkBroadcast() {
        mNetworkChangedReceiver = new NetworkConnectChangedReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mNetworkChangedReceiver, intentFilter);
    }

    /**
     * 注销广播
     */
    private void unRegisterNetWorkBroadcast() {
        if (mNetworkChangedReceiver != null) {
            unregisterReceiver(mNetworkChangedReceiver);
            mNetworkChangedReceiver = null;
        }
    }

    private void hasNetWork(boolean hasNetWork) {
        if (isCheckNetWork()) {
            if (hasNetWork) {
                if (mTipView != null && mTipView.getParent() != null) {
                    mWindowManager.removeView(mTipView);
                }
            } else {
                if (mTipView.getParent() == null) {
                    mWindowManager.addView(mTipView, mParams);
                }
            }
        }
    }


    protected void requestStoragePermission() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (!EasyPermissions.hasPermissions(this, perms)) {
            EasyPermissions.requestPermissions(this, "", Constants.REQUEST_STORAGE_PERMISSIONS, perms);
        } else {
            start();
        }
    }

    @Override
    public void finish() {
        super.finish();
        if (mTipView != null && mTipView.getParent() != null) {
            mWindowManager.removeView(mTipView);
        }
    }

    private void initTipView() {
        mTipView = getLayoutInflater().inflate(R.layout.layout_network_tip, null);
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mParams = new WindowManager.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.TYPE_APPLICATION,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        PixelFormat.TRANSLUCENT);
        mParams.gravity = Gravity.TOP;
        mParams.x = 0;
    }

    /**
     * 设置当前页面是否需要监听网络
     *
     * @param checkNetWork
     */
    public void setCheckNetWork(boolean checkNetWork) {
        mCheckNetwork = checkNetWork;
    }

    public boolean isCheckNetWork() {
        return mCheckNetwork;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNetWorkChangeEvent(NetWorkChangeEvent event) {
        LogUtils.d(TAG, "检测到网络状态发生变化");
        hasNetWork(event.isConnected);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }

    @AfterPermissionGranted(Constants.REQUEST_STORAGE_PERMISSIONS)
    protected void start() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegisterNetWorkBroadcast();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
