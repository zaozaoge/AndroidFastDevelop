package hu.zaoao.com.myapplication.utils.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.greenrobot.eventbus.EventBus;

import hu.zaoao.com.myapplication.bean.NetWorkChangeEvent;
import hu.zaoao.com.myapplication.utils.LogUtils;

public class NetworkConnectChangedReceiver extends BroadcastReceiver {

    private static final String TAG = NetworkConnectChangedReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isConnected = NetworkUtils.isConnected(context);
        LogUtils.d(TAG,"onReceive:当前网络----->"+isConnected);
        EventBus.getDefault().post(new NetWorkChangeEvent(isConnected));
    }
}
