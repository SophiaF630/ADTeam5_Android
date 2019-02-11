package com.example.tr.clerk;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class OrdersActivity extends Activity {

    static String dept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        Bundle extras = getIntent().getExtras();
        dept = extras.getString("Dept");

        new AsyncTask<String, Void, List<BriefEmpReq>>() {
            @Override
            protected List<BriefEmpReq> doInBackground(String... params) {
                return BriefEmpReq.ListEmpReq(dept);
            }
            @Override
            protected void onPostExecute(List<BriefEmpReq> result) {
                MyAdapter2 adapter = new MyAdapter2(getApplicationContext(),
                        result);

                ListView list = (ListView) findViewById(R.id.listView1);
                list.setAdapter(adapter);
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        BriefEmpReq selected = (BriefEmpReq)parent.getAdapter().getItem(position);
                        Intent intent = new Intent(getApplicationContext(), RecDetailsActivity.class);
                        intent.putExtra("RRID", selected.get("RRID"));
                        intent.putExtra("Name", selected.get("Name"));
                        intent.putExtra("Dept", dept);
                        startActivityForResult(intent, 123);
                    }
                });
            }
        }.execute();
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
