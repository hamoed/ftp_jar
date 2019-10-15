package com.production.kalagemet.ftpjar;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

class Koneksi_Class extends AsyncTask <Object, Void, String> {

    FTPClient mFTPClient;
    static Server_class server = new Server_class();
    static User_Class user = new User_Class();
    @SuppressLint("StaticFieldLeak")
    private static Button tombol;

    void setUp(Server_class a, User_Class b, Button d) {
        tombol = d;
        server = a;
        user = b;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onPreExecute() {
        MainActivity.waiting=true;
        tombol.setText("Mengakses...");
        tombol.setEnabled(false);
    }

    @Override
    protected String doInBackground(Object[] objects) {
        mFTPClient = null;
        try {
            mFTPClient = new FTPClient();
            // koneksi ke host
            mFTPClient.connect(server.getIp(),server.getPort());
            // tes koneksi ip server
            if (FTPReply.isPositiveCompletion(mFTPClient.getReplyCode())) {
                //login_error username & password
                if(mFTPClient.login(user.getUsername(), user.getPassword())){
                    Log.d("Auth", "Login Berhasil");
                    Log.d("COBA", mFTPClient.getReplyString());
                    MainActivity.waiting=false;
                }else{
                    Log.d("Auth", "Login Gagal");
                    MainActivity.waiting=false;
                }
                //set Mode
                mFTPClient.setFileType(FTP.ASCII_FILE_TYPE);
                mFTPClient.enterLocalPassiveMode();
                mFTPClient.changeWorkingDirectory("/");
            }else {
                Log.d("Conn", "Server not found");
            }
        } catch (Exception e) {
            Log.d("KONEKSI", e.getMessage());
            MainActivity.waiting=false;
        }
        return null;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        MainActivity.waiting=false;
        tombol.setText("Connect");
        tombol.setEnabled(true);
        Log.d("oPE","selesai ");
    }

    FTPClient getFtp() {
        Log.d("OgFTP",String.valueOf(MainActivity.waiting) );
        return mFTPClient;
    }
}