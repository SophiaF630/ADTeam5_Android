package com.example.tr.clerk;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DeptRepActivity extends Activity {

    static String dept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dept_rep);

        Bundle extras = getIntent().getExtras();
        dept = extras.getString("Dept");

        new AsyncTask<String, Void, BriefEmp>() {
            @Override
            protected BriefEmp doInBackground(String... params) {
                return BriefEmp.GetDeptRep(dept);
            }

            @Override
            protected void onPostExecute(BriefEmp result) {

                show(result);
            }
        }.execute(dept);

        Button button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EmployeeListActivity.class);
                intent.putExtra("Dept", dept);
                startActivityForResult(intent, 123);
            }
        });

    }
    void show(BriefEmp briefEmp){

        String name = briefEmp.get("Name");
        TextView t = findViewById(R.id.textView2);
        t.setText(name);
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
