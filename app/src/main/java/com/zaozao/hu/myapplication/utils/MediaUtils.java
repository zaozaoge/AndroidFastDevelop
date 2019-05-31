package com.zaozao.hu.myapplication.utils;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import java.io.File;

public class MediaUtils {


    /**
     * 4.4以下版本
     *
     * @param context context
     * @param uri     uri
     * @return
     */
    public static String getRealPathFromUri(Context context, Uri uri) {
        String result = null;
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            result = cursor.getString(columnIndex);
        }

        if (cursor != null)
            cursor.close();
        return result;
    }

    @SuppressLint("NewApi")
    public static String getPathFromUri(Context context, Uri uri) {
        //是否是4.4以上版本
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        String documentId;
        String documentType;
        Uri contentUri = null;

        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            //外部存储文件
            if (isExternalStorageDocument(uri)) {
                documentId = DocumentsContract.getDocumentId(uri);
                String[] docIdSplit = documentId.split(":");
                if (docIdSplit.length < 2)
                    return null;
                documentType = docIdSplit[0];
                if ("primary".equalsIgnoreCase(documentType)) {
                    return Environment.getExternalStorageDirectory() + File.separator + docIdSplit[1];
                }
            }
            //下载文件
            if (isDownloadsDocument(uri)) {
                documentId = DocumentsContract.getDocumentId(uri);
                String uriString = "content://downloads/public_downloads";
                contentUri = ContentUris.withAppendedId(Uri.parse(uriString), Long.parseLong(documentId));
                return getDataColumn(context, contentUri, null, null);
            }
            //媒体文件
            if (isMediaDocument(uri)) {
                documentId = DocumentsContract.getDocumentId(uri);
                String[] docIdSplit = documentId.split(":");
                if (docIdSplit.length < 2)
                    return null;
                documentType = docIdSplit[0];
                if ("image".equalsIgnoreCase(documentType))
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                if ("video".equalsIgnoreCase(documentType))
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                if ("audio".equalsIgnoreCase(documentType))
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

                final String selection = "_id=?";
                final String[] selectionArgs = {docIdSplit[1]};
                if (contentUri == null)
                    return null;
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }

        if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }

        if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }


    /**
     * 获取数据
     *
     * @param context       上下文
     * @param uri           uri
     * @param selection     selection
     * @param selectionArgs selectionArgs
     * @return
     */
    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor;
        final String column = "_data";
        final String[] projection = {column};
        cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
        if (cursor != null && cursor.moveToFirst()) {
            final int column_index = cursor.getColumnIndexOrThrow(column);
            return cursor.getString(column_index);
        }
        if (cursor != null)
            cursor.close();
        return null;
    }

    /**
     * 是否是外部存储文件
     *
     * @param uri uri
     * @return
     */
    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * 是否是下载文件
     *
     * @param uri uri
     * @return
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * 是否是媒体文件
     *
     * @param uri
     * @return
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
}
