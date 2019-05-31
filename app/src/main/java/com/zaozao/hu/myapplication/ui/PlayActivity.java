package com.zaozao.hu.myapplication.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.Log;
import android.widget.SeekBar;


import com.zaozao.hu.myapplication.R;
import com.zaozao.hu.myapplication.utils.Constants;
import com.zaozao.hu.myapplication.utils.JniUtils;

public class PlayActivity extends Activity implements Runnable {


    private ContentLoadingProgressBar progressBar;
    private Thread thread;
    private AppCompatSeekBar seekBar;
    private boolean flag = true;
    private boolean isTracking = false;
    private String mVideoPath;

    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        mVideoPath = getIntent().getStringExtra(Constants.VIDEO_PATH);
        progressBar = findViewById(R.id.contentLoading);
        seekBar = findViewById(R.id.seekBar);
        seekBar.setMax(100);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isTracking = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isTracking = false;
                Log.e("ZP", "onStopTrackingTouch:" + (double) seekBar.getProgress() / (double) seekBar.getMax());
                JniUtils.seek((double) seekBar.getProgress() / (double) seekBar.getMax());
            }
        });
        progressBar.hide();
        openResource();
    }




    public void openResource() {
        progressBar.show();
        if (thread != null && thread.isAlive()) {
            thread.interrupt();
            thread = null;
        }
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        JniUtils.open(mVideoPath);
        while (flag) {
            double progress = JniUtils.getPlayProgress();
            if (!isTracking)
                seekBar.setProgress((int) (progress * 100));
            if (JniUtils.isReady()) {
                Log.e("PlayActivity", "开始出图像了:" + progress);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.hide();
                    }
                });
            }
            try {
                Thread.sleep(40);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        flag = false;
        JniUtils.close();
    }
}
