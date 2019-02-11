package com.example.tr.clerk;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.AsyncTask;
import android.view.KeyEvent;
import android.widget.TextView;

public class UpcomingCollectionActivity extends AppCompatActivity {
    static String dept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_collection);

        Bundle extras = getIntent().getExtras();
        dept = extras.getString("Dept");

        new AsyncTask<String, Void, BriefDisbursement>() {
            @Override
            protected BriefDisbursement doInBackground(String... params) {
                return BriefDisbursement.GetDisbursement(dept);
            }

            @Override
            protected void onPostExecute(BriefDisbursement result) {
if (result != null)
                show(result);
            }
        }.execute(dept);
    }
    void show(BriefDisbursement briefD){

        String collectionDate = briefD.get("CollectionDate");
        TextView t2 = findViewById(R.id.textView2);
        t2.setText(collectionDate);

        String collectionPointName = briefD.get("CollectionPointName");
        TextView t4 = findViewById(R.id.textView4);
        t4.setText(collectionPointName);

        String collectionTime = briefD.get("CollectionTime");
        TextView t6 = findViewById(R.id.textView6);
        t6.setText(collectionTime);
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
