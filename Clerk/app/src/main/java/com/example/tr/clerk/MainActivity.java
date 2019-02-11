package com.example.tr.clerk;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences=getSharedPreferences("config",0);
        if(sharedPreferences.getString("Status", "").equals("Loggged In"))
        {
            Intent intent = new Intent(MainActivity.this,MenuActivity.class);
            startActivity(intent);
        //this part will add the judge about login status
        }
        else
        {
//            String host = "172.23.212.222/ADTeam5";
//            //Todo:this part have to be replace every time
//            final String baseURL;
//            baseURL = String.format("http://%s/api", host);
            setContentView(R.layout.login);

//            if (android.os.Build.VERSION.SDK_INT > 9) {
//
//                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//
//                StrictMode.setThreadPolicy(policy);
//
//            }


            final EditText et_name=(EditText)findViewById(R.id.editText_login_account);
            final EditText et_pasd=(EditText)findViewById(R.id.editText_login_password);
            Button btn_login=(Button)findViewById(R.id.button_login_login);
            btn_login.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View v) {
                    startLogin(et_name.getText().toString(),et_pasd.getText().toString(),sharedPreferences);
                            // TODO Auto-generated method stub
                            //开启子线程，实现网络请求
//                thread=new Thread(new loginRunnable());
//                thread.start();


                }
            });
        }
    }
    void startLogin(String email, String password, final SharedPreferences sp)
    {
        if (email==null||password==null)
        {
            AlertDialog.Builder builder  = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Error" ) ;
            builder.setMessage("Please input email and password" ) ;
            builder.setPositiveButton("OK" ,  null );
            builder.show();
            return;
        }
        Account user = new Account(email,password);
        new AsyncTask<Account, Void, Void>() {
            @Override
            protected Void doInBackground(Account... params) {
                User.TryLogin(params[0].username,params[0].password,sp);
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                jump(sp);
            }
        }.execute(user);

    }
    void jump(SharedPreferences sp)
    {
        if (sp.getString("Status", "").equals("Loggged Out"))
        {
            AlertDialog.Builder builder  = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Error" ) ;
            builder.setMessage("Please input correct email and password" ) ;
            builder.setPositiveButton("OK" ,  null );
            builder.show();
            return;
        }
        else
        {
            Intent intent = new Intent(MainActivity.this,MenuActivity.class);
            startActivity(intent);
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode==KeyEvent.KEYCODE_BACK){
            new AlertDialog.Builder(this).setTitle("Warning")
                    .setMessage("Are you sure you want to exit?" )
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1){
//                            android.os.Process.killProcess(android.os.Process.myPid());
//                            System.exit(0);
                            Intent startMain = new Intent(Intent.ACTION_MAIN);
                            startMain.addCategory(Intent.CATEGORY_HOME);
                            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(startMain);
                            System.exit(0);
                        }
                    }).setNegativeButton("No",null).show();
        }
        return true;
    }
}
