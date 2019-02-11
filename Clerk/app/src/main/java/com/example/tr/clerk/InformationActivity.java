package com.example.tr.clerk;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class InformationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        // StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.LAX);

        Button btn_logout = (Button) findViewById(R.id.button_myinformation_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //开启子线程，实现网络请求
//                thread=new Thread(new loginRunnable());
//                thread.start();
                SharedPreferences sp=getSharedPreferences("config",0);
                SharedPreferences.Editor editor = sp.edit();
                //editor.clear();
                editor.putString("Status", "Loggged Out");
                editor.apply();
                Intent intent = new Intent(InformationActivity.this, MainActivity.class);
                //todo: this part must remove the login status
                startActivity(intent);
            }});

        Intent intent = getIntent();
        if (intent.hasExtra("UserID")) {
            final int id = intent.getIntExtra("UserID",10002);
            new AsyncTask<Integer, Void, User>() {
                @Override
                protected User doInBackground(Integer... params) {
                    return User.ReadUser(params[0]);
                }
                @Override
                protected void onPostExecute(User result) {
                    show(result);
                }
            }.execute(id);
        }

    }
    void show(User emp) {
        int []dest = new int[]{R.id.textView_myinformation_userid, R.id.textView_myinformation_department,R.id.emailtextView_myinformation_email};
        String []src = new String[]{"Name","Department", "Email"};
        for (int n=0; n<dest.length; n++) {
            AppCompatTextView txt = findViewById(dest[n]);
            txt.setText(emp.get(src[n]));
        }
    }}
