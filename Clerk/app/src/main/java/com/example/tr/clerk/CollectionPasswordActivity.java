package com.example.tr.clerk;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.KeyEvent;
import android.widget.TextView;

public class CollectionPasswordActivity extends Activity {

    static String dept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_password);

        Bundle extras = getIntent().getExtras();
        dept = extras.getString("Dept");

        new AsyncTask<String, Void, BriefDept>() {
            @Override
            protected BriefDept doInBackground(String... params) {
                return BriefDept.GetCollectionPassword(dept);
            }

            @Override
            protected void onPostExecute(BriefDept result) {

                show(result);
            }
        }.execute(dept);
    }

    void show(BriefDept briefDept){
        String cp = briefDept.get("CollectionPassword");
        TextView t = findViewById(R.id.textView2);
        t.setText(cp);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode==KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(),MenuActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        return true;
    }
}
