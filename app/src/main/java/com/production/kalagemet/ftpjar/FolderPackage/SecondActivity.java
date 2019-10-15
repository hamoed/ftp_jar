package com.production.kalagemet.ftpjar.FolderPackage;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.production.kalagemet.ftpjar.Constanta;
import com.production.kalagemet.ftpjar.MainActivity;
import com.production.kalagemet.ftpjar.R;

import org.apache.commons.net.ftp.FTPFile;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class SecondActivity extends AppCompatActivity {

    public static volatile boolean load, changge, download, upload;

    String host_cd;

    int sum;

    //log
    ArrayAdapter<String> mAdapter;
    ListView mLogList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exsplorer);

        int[] type = new int[1000];
        int total;
        final String[] nama = new String[1000];

        TextView info = findViewById(R.id.info);
        TextView cur_dir = findViewById(R.id.cur_dir);
        Button upload = findViewById(R.id.upload_button);
        Button back = findViewById(R.id.up_button);

        //membuat list log
        mLogList = findViewById(R.id.log_file);

        mAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                Constanta.mLog
        );

        mLogList.setAdapter(mAdapter);

        final ListFille_class list = new ListFille_class();
        list.setmFTPClient(MainActivity.getData());
        list.execute();
        Log.d("STTS",String.valueOf(load));
        FTPFile[] nm = new FTPFile[1000];
        while (load) {
            nm = list.getFtpFiles();
        }
        info.setText("Terhubung dengan : "+MainActivity.server_adress);

        final String cd = list.getCurdir();
        host_cd = cd;
        cur_dir.setText(cd);
        setLog("Mendapatkan Current Direktori ... ");
        setLog(" "+cd+" sebagai currend directori");

        int folder=0;

        if (list.getFtpFiles() != null) {
            int length = nm.length;
            Log.d("leng",String.valueOf(length));
            sum = 0;
            for (int i = 0; i < length; i++) {
                boolean isFile = nm[i].isFile();
                if (!isFile) {
                    nama[sum] = nm[i].getName();
                    type[sum] = R.drawable.ic_folder;
                    sum = sum + 1;
                    folder = folder+1;
                }
            }
            setLog("mendapatkan "+String.valueOf(sum)+" Folder");
            Log.d("FD", String.valueOf(sum));
            int file=0;
            for (int i=0; i < length; i++) {
                boolean isFile = nm[i].isFile();
                if (isFile) {
                    nama[sum] = nm[i].getName();
                    type[sum] = R.drawable.ic_fille;
                    sum = sum + 1;
                    file = file+1;
                }
            }
            setLog("mendapatkan "+String.valueOf(file)+" fille");
        }
        if (sum<1){
            setLog("Total 0 list ditampilkan");
            Log.d("FTP2","0 list");
        }else {
            setLog("Total " + String.valueOf(list.getFtpFiles().length) + " list ditampilkan");
            Log.d("FTP2",String.valueOf(list.getFtpFiles().length)+" list");
        }
        load=true;

        List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();

        for (int i = 0; i < sum; i++) {
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("file_name", nama[i]);
            hm.put("icon", Integer.toString(type[i]));
            aList.add(hm);
        }

        String[] from = {"file_name","icon"};
        int[] to = {R.id.file_name, R.id.fille_icon};

        SimpleAdapter simpleAdapter = new SimpleAdapter(getBaseContext(), aList, R.layout.list_folder, from, to);
        ListView androidListView = (ListView) findViewById(R.id.list_folder);
        androidListView.setAdapter(simpleAdapter);

        final int finalFolder = folder;
        androidListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position< finalFolder){
                    String mvdir=nama[position];
                    Log.d("clik","Membuka DIrektori : "+mvdir);
                    goTo_Class mv = new goTo_Class();
                    mv.setL(mvdir);
                    mv.execute();
                    while (!changge){

                    }
                    Intent i = new Intent(SecondActivity.this, SecondActivity.class);
                    startActivity(i);
                    finish();
                }
                else {
                    String mvdir= nama[position];
                    Log.d("clik", "mendownload file "+mvdir);
                    setLog("Mendownload "+mvdir);
                    Toast.makeText(getApplicationContext(),"mendownload "+mvdir,Toast.LENGTH_LONG).show();
                    Download_Class dw = new Download_Class();
                    dw.setSrcFilePath(mvdir);
                    dw.execute();
                    while (!download){

                    }
                    Toast.makeText(getApplicationContext(),"Download Selesai",Toast.LENGTH_LONG).show();
                    setLog("Download Selesai, File tersimpan di Downloads");
                }
            }
        });

        //set izin
        int acc = ContextCompat.checkSelfPermission(SecondActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (acc==PackageManager.PERMISSION_DENIED){
            Toast.makeText(getApplicationContext(),"Permission ditolak",Toast.LENGTH_LONG).show();
            if (ActivityCompat.shouldShowRequestPermissionRationale(SecondActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            }else {
                ActivityCompat.requestPermissions(SecondActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
            }
        }

        //back / up directori
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!cd.equals("/")) {
                    String mvdir = "/";
                    Log.d("clik", "Membuka DIrektori : " + mvdir);
                    goTo_Class mv = new goTo_Class();
                    mv.setL(mvdir);
                    mv.execute();
                    while (!changge) {

                    }
                    Intent i = new Intent(SecondActivity.this, SecondActivity.class);
                    startActivity(i);
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(),"Sudah pada Direktori terakhir"+cd,Toast.LENGTH_LONG).show();
                }
            }
        });

        //upload
        upload.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String type="*/*";
                Intent i=new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType(type);
                startActivityForResult(Intent.createChooser(i,"Pilih file") ,0);
            }
        });
    }

    public void setLog(String l){
        Constanta.mLog.add(Calendar.getInstance().getTime() +l);
        mAdapter.notifyDataSetChanged();
        mLogList.setSelection(mAdapter.getCount() - 1);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //Handle the back button
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            //Ask the user if they want to quit
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Keluar ?")
                    .setMessage("Lanjutkan untuk mengkahiri sesi FTP")
                    .setPositiveButton("Keluar", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Stop the activity
//                            SecondActivity.this.finish();
                            log_out lo = new log_out();
                            lo.setL(MainActivity.getData());
                            lo.execute();
                            Intent i = new Intent(SecondActivity.this, MainActivity.class);
                            startActivity(i);
                            finish();
                        }

                    })
                    .setNegativeButton("Batal", null)
                    .show();

            return true;
        }
        else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK && data != null) {
            Uri uploadfileuri = data.getData();
            String path = null;
            try {
                path = FileUtil.getPath(this,uploadfileuri);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            File file = new File(path);
            Log.d("upload",file.getName()+" | "+file.getPath()+" | uri : "+uploadfileuri.toString());
            Toast.makeText(getApplicationContext(),"Mengunggah "+file.getName(),Toast.LENGTH_LONG).show();
            setLog("Mengunggah "+file.getName());
            Upload_Class upl = new Upload_Class();
            upl.setUploadedfile(file.getPath());
            upl.setCd(host_cd);
            upl.setFilename(file.getName());
            upl.execute();
            while (upload){

            }
            setLog("File "+file.getName()+" disimpan di "+host_cd);
            Toast.makeText(getApplicationContext(),"Upload berhasil", Toast.LENGTH_LONG).show();
            Intent i = new Intent(SecondActivity.this, SecondActivity.class);
            startActivity(i);
            finish();
        }
    }
}
