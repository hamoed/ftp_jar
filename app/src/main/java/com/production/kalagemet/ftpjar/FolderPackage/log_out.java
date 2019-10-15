package com.production.kalagemet.ftpjar.FolderPackage;

import android.os.AsyncTask;
import android.util.Log;

import com.production.kalagemet.ftpjar.Constanta;

import org.apache.commons.net.ftp.FTPClient;

import java.io.IOException;

public class log_out extends AsyncTask {

    FTPClient l;

    public void setL(FTPClient l) {
        this.l = l;
    }


    @Override
    protected Object doInBackground(Object[] objects) {
        try {
            Constanta.mLog.clear();
            l.logout();
            l.disconnect();
        } catch (IOException e) {
            Log.d("LOGOUT", e.getMessage());
        }
        return null;
    }
}
