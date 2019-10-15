package com.production.kalagemet.ftpjar.FolderPackage;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.production.kalagemet.ftpjar.MainActivity;

import org.apache.commons.net.ftp.FTP;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Download_Class extends AsyncTask {

    public static String p;

    public void setSrcFilePath(String srcFilePath) {
        Download_Class.p = srcFilePath;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        SecondActivity.download=true;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        FileOutputStream desFileStream = null;
        try {
            MainActivity.getData().setFileType(FTP.BINARY_FILE_TYPE);
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), p);
            desFileStream = new FileOutputStream(file);
            boolean status = MainActivity.getData().retrieveFile(p,desFileStream);
            desFileStream.close();
            SecondActivity.download=false;
        } catch (FileNotFoundException e) {
            Log.d("DW1",e.getMessage());
        } catch (IOException e) {
            Log.d("DW2",e.getMessage());
        }
        return null;
    }

}
