package hu.zaoao.com.myapplication;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;

import java.util.List;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class QRCodeScanActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks, QRCodeView.Delegate {

    private long clickTime = 0; //记录第一次点击的时间
    private ZXingView mQRView;
    private static final int REQUEST_CODE_QRCODE_PERMISSIONS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_scan);
        mQRView = findViewById(R.id.qrView);
        mQRView.setDelegate(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        requestCodeQRCodePermissions();
        mQRView.startCamera();
        mQRView.startSpot();
    }

    @Override
    protected void onResume() {
        mQRView.showScanRect();
        super.onResume();
    }


    @Override
    protected void onStop() {
        mQRView.stopCamera();
        super.onStop();
    }

    @AfterPermissionGranted(REQUEST_CODE_QRCODE_PERMISSIONS)
    private void requestCodeQRCodePermissions() {
        String[] perms = {Manifest.permission.CAMERA};
        if (!EasyPermissions.hasPermissions(this, perms)) {
            EasyPermissions.requestPermissions(this, "", REQUEST_CODE_QRCODE_PERMISSIONS, perms);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if ((System.currentTimeMillis() - clickTime) > 2000) {
                         Toast.makeText(getApplicationContext(), "再次点击退出程序",Toast.LENGTH_SHORT).show();
                         clickTime = System.currentTimeMillis();
                     } else {
                         this.finish();
                     }
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

    @Override
    public void onScanQRCodeSuccess(String result) {

    }

    @Override
    public void onScanQRCodeOpenCameraError() {

    }
}
