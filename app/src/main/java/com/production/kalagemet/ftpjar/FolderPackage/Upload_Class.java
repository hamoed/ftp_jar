package com.production.kalagemet.ftpjar.FolderPackage;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.production.kalagemet.ftpjar.MainActivity;

import org.apache.commons.net.ftp.FTP;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Upload_Class extends AsyncTask {

    public static String uploadedfile, cd, filename, status;

    public static void setUploadedfile(String l) {
        Upload_Class.uploadedfile = l;
    }

    public static void setCd(String cd) {
        Upload_Class.cd = cd;
    }

    public static void setFilename(String filename) {
        Upload_Class.filename = filename;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        SecondActivity.upload=true;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        try {
            MainActivity.getData().setFileType(FTP.BINARY_FILE_TYPE);
            FileInputStream srcFileStream = new FileInputStream(new File(uploadedfile));
            String descFile = cd+File.separator+filename;
            boolean status = MainActivity.getData().storeFile(descFile, srcFileStream);
            srcFileStream.close();
            SecondActivity.upload=false;
            Log.d("USuk","sukses"+descFile+" |  "+status);
        } catch (IOException e) {
            Log.d("Upoad",e.getMessage());
        }
        return null;
    }
}