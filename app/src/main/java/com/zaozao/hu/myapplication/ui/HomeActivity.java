package com.zaozao.hu.myapplication.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;


import com.zaozao.hu.myapplication.R;
import com.zaozao.hu.myapplication.demo.sqlite.SqliteDemo;
import com.zaozao.hu.myapplication.demo.sqlite.SqliteHelper;
import com.zaozao.hu.myapplication.utils.Constants;
import com.zaozao.hu.myapplication.utils.LogUtils;
import com.zaozao.hu.myapplication.utils.MediaUtils;

public class HomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {


    private String path;
    SqliteDemo sqliteDemo;
    SqliteHelper sqliteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            if (sqliteDemo != null)
                sqliteDemo.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            openVideo("rtmp://live.hkstv.hk.lxdns.com/live/hks2");
        } else if (id == R.id.nav_gallery) {
            openFileExplorer();
        } else if (id == R.id.nav_slideshow) {
            if (sqliteHelper == null)
                sqliteHelper = new SqliteHelper(this, "stu_db", "zaozao", 1);


        } else if (id == R.id.nav_manage) {
        } else if (id == R.id.nav_share) {
        } else if (id == R.id.nav_send) {
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * 浏览手机中的视频文件
     */
    private void openFileExplorer() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("video/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, 1);
    }


    private void openVideo(String path) {
        Intent intent = new Intent(this, PlayActivity.class);
        intent.putExtra(Constants.VIDEO_PATH, path);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            assert data != null;
            Uri uri = data.getData();
            assert uri != null;
            LogUtils.i(TAG, uri.toString());
            LogUtils.i(TAG, uri.getScheme());
            if ("file".equalsIgnoreCase(uri.getScheme())) {
                path = uri.getPath();
                return;
            }
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                //4.4以上
                path = MediaUtils.getPathFromUri(this, uri);
                requestStoragePermission();
            } else {
                // 4.4以下
                path = MediaUtils.getRealPathFromUri(this, uri);
            }

        }
    }


    @Override
    protected void start() {
        super.start();
        //sqliteDemo = new SqliteDemo("zaozao");
        openVideo(path);
    }


}
