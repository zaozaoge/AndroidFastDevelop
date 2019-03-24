package hu.zaoao.com.myapplication.bean;

public class NetWorkChangeEvent {

    public boolean isConnected;//是否存在网络

    public NetWorkChangeEvent(boolean isConnected) {
        this.isConnected = isConnected;
    }
}
