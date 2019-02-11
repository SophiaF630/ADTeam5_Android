package com.example.tr.clerk;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class OrderSecondActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_second);
        Intent intent = getIntent();
        if (intent.hasExtra("CollectionPointID"))
        {
            String id = intent.getStringExtra("CollectionPointID");
            new AsyncTask<String, Void, List<Order>>() {
                @Override
                protected List<Order> doInBackground(String... params) {
                    return Order.ReadOrders(params[0]);
                }

                @Override
                protected void onPostExecute(List<Order> result) {
                    if (result == null)
                    {
                        return;
                    }
                    OrderAdapter2 adapter = new OrderAdapter2(getApplicationContext(),
                            result);
                    ListView list = (ListView) findViewById(R.id.listview_order_second);
                    list.setAdapter(adapter);
                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Order selected = (Order) parent.getAdapter().getItem(position);
                            Intent intent = new Intent(getApplicationContext(), OrderDetailActivity.class);
                            intent.putExtra("Rrid", selected.get("RRID"));
                            startActivityForResult(intent, 123);
                        }
                    });
                }
            }.execute(id);
        }
    }
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event){
//        if(keyCode==KeyEvent.KEYCODE_BACK){
//            Intent intent = new Intent();
//            intent.setClass(getApplicationContext(),OrderActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
//        }
//        return true;
//    }
}
