package com.production.kalagemet.ftpjar.FolderPackage;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

public class goTo_Class extends AsyncTask {

    String l;

    public void setL(String l) {
        this.l = l;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        SecondActivity.changge=false;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        try {
            com.production.kalagemet.ftpjar.MainActivity.getData().changeWorkingDirectory(l);
        } catch (IOException e) {
            Log.d("MOVE",e.getMessage());
        }
        SecondActivity.changge=true;
        return null;
    }
}
