package com.production.kalagemet.ftpjar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.production.kalagemet.ftpjar.FolderPackage.SecondActivity;

import org.apache.commons.net.ftp.FTPClient;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static FTPClient data;
    protected static volatile  boolean waiting;
    public static String server_adress;

    ArrayAdapter<String> mAdapter;
    ListView mlistView;

    public static FTPClient getData() {
        return data;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button login = findViewById(R.id.login);
        final EditText server = findViewById(R.id.ip);
        final EditText no_port = findViewById(R.id.port);
        final EditText user = findViewById(R.id.user);
        final EditText password = findViewById(R.id.password);
        TextView tekt = findViewById(R.id.consol);
        tekt.setText("Aktivitas Log");
        //log
        mlistView= findViewById(R.id.log);
        //membuat class server
        final Server_class serverFtp = new Server_class();
        final User_Class auth = new User_Class();
        Constanta.mLog = new ArrayList<>();

        mAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                Constanta.mLog
        );

        mlistView.setAdapter(mAdapter);


        setLog(" - Aplikasi Dimulai...");

//        //dummy
        server.setText("192.168.43.198");
        no_port.setText("21");
        user.setText("ftp");
        password.setText("123");

        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if (String.valueOf(server.getText().toString()).equals("")) {
                    server.setError("Harus di isi !!!");
                }
                else if (String.valueOf(no_port.getText().toString()).equals("")) {
                    no_port.setError("Harus di isi !!!");
                }
                else if (String.valueOf(user.getText().toString()).equals("")) {
                    user.setError("Harus di isi !!!");
                }
                else if (String.valueOf(password.getText().toString()).equals("")) {
                    password.setError("Harus di isi !!!");
                }else{
                    serverFtp.setIp(server.getText().toString());
                    serverFtp.setPort(Integer.parseInt(no_port.getText().toString()));
                    server_adress = server.getText()+" : "+no_port.getText().toString();

                    setLog(" - Menghubungi"+serverFtp.getIp()+" di Port : "+serverFtp.getPort());

                    auth.setUsername(user.getText().toString());
                    auth.setPassword(password.getText().toString());

                    //koneksi
                    Koneksi_Class conn = new Koneksi_Class();
                    conn.setUp(serverFtp,auth,login);
                    //mengambil FTPCLient
                    conn.execute();
                    FTPClient ftp = new FTPClient();
                    while (waiting){
                        ftp = conn.getFtp();
                    }
                    if (ftp.getReplyString() != null) {
                        if (ftp.getReplyCode()==230 || ftp.getReplyCode()==200 ||ftp.getReplyCode()==250 ) {
                            //login sukses
                            data = ftp;

                            setLog(" - Terhubung" + serverFtp.getIp() + " di Port : " + serverFtp.getPort());
                            setLog(" - Setting setFileType(FTP.ASCII_FILE_TYPE)");
                            setLog(" - enterLocalPassiveMode");

                            Intent i = new Intent(MainActivity.this, SecondActivity.class);
                            startActivity(i);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), ftp.getReplyString(), Toast.LENGTH_LONG).show();
                            Log.i("FTP", ftp.getReplyString());

                            setLog(" - "+ftp.getReplyString()+" Tidak ada FTP | code : "+ftp.getReplyCode());

                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Server Tidak Merespon", Toast.LENGTH_LONG).show();
                        setLog(" - Tidak dapat menghubungi " + serverFtp.getIp() + " di Port : " + serverFtp.getPort());
                    }
                }
                Log.d("FNS",  "selesai");
            }
        });
    }


    public void setLog(String l){
        Constanta.mLog.add(Calendar.getInstance().getTime() +l);
        mAdapter.notifyDataSetChanged();
        mlistView.setSelection(mAdapter.getCount() - 1);
    }

    public String getServer(){
        return server_adress;
    }
}
