package com.example.tr.clerk;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

public class ApplyVoucherActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_voucher);
        sharedPreferences=getSharedPreferences("config",0);
        final int ClerkID = sharedPreferences.getInt("workid", -300);
        if (ClerkID != -300) {
            String id = String.valueOf(ClerkID);
            new AsyncTask<String, Void, List<Voucher>>() {
                @Override
                protected List<Voucher> doInBackground(String... params) {
                    return Voucher.ReadVouchers(params[0]);
                }

                @Override
                protected void onPostExecute(List<Voucher> result) {
                    if (result == null) {
                        return;
                    }
                    VoucherAdapter adapter = new VoucherAdapter(getApplicationContext(),
                            result);
                    ListView list = (ListView) findViewById(R.id.listView_voucherlist);
                    list.setAdapter(adapter);
                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Voucher selected = (Voucher) parent.getAdapter().getItem(position);
                            Intent intent = new Intent(getApplicationContext(), ApplyVoucherListActivity.class);
                            intent.putExtra("VoNo", selected.get("VoNo"));
                            startActivityForResult(intent, 123);
                        }
                    });
                }
            }.execute(id);
        }
        Button btn = (Button)findViewById(R.id.button_applyvoucher_list_addlist);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(ApplyVoucherActivity.this,ApplyVoucherListActivity.class);
                intent.putExtra("VoNo",("VTemp"+ClerkID));
                intent.putExtra("Mode","Edit");
                startActivity(intent);
            }
        });
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
