package com.production.kalagemet.ftpjar.FolderPackage;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

public class ListFille_class extends AsyncTask<Object, Void, String > {

    private FTPClient mFTPClient;
    private static FTPFile[] ftpFiles;
    private static String curdir;

    void setmFTPClient(FTPClient mFTPClient) {
        this.mFTPClient = mFTPClient;
    }

    @Override
    protected void onPreExecute() {
        SecondActivity.load=true;
        Log.d("Load","Memuat List");
        ftpFiles = null;
    }

    @Override
    protected String doInBackground(Object[] objects) {
        FTPFile[] tmp = new FTPFile[0];
        try {
            curdir =mFTPClient.printWorkingDirectory();
            tmp = mFTPClient.listFiles(curdir);
        } catch(Exception e) {
            Log.d("LIST",e.getMessage());
        }
        ftpFiles = tmp;
        SecondActivity.load=false;
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        SecondActivity.load = false;
        Log.d("Load", "Load File Selesai");
        if (ftpFiles == null) {
            Log.d("FTP1","0");
        } else {
            Log.d("FTP1", String.valueOf(ftpFiles.length));
        }
    }
    FTPFile[] getFtpFiles(){return ftpFiles;}

    String getCurdir(){
        return curdir;
    }
}
