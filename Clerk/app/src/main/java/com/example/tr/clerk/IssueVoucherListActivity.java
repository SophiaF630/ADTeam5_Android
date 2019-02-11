package com.example.tr.clerk;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

public class IssueVoucherListActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_voucher_list);
        final ListView listView = findViewById(R.id.issue_voucher_list);
        sharedPreferences=getSharedPreferences("config",0);
        final int ClerkID = sharedPreferences.getInt("workid", -300);
        if (ClerkID != -300) {
            String id = String.valueOf(ClerkID);
            new AsyncTask<String, Void, List<Voucher>>() {
                @Override
                protected List<Voucher> doInBackground(String... params) {
                    return Voucher.ReadVouchers1(params[0]);
                }

                @Override
                protected void onPostExecute(List<Voucher> result) {
                    if (result == null) {
                        return;
                    }
                    VoucherAdapter3 adapter = new VoucherAdapter3(getApplicationContext(),
                            result);
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Voucher selected = (Voucher) parent.getAdapter().getItem(position);
                            Intent intent = new Intent(getApplicationContext(), IssueVoucherApproveActivity.class);
                            intent.putExtra("VoNo", selected.get("VoNo"));
                            intent.putExtra("Name",selected.get("IssueDate"));
                            intent.putExtra("Price",selected.get("Price"));
                            startActivityForResult(intent, 123);
                        }
                    });
                }
            }.execute(id);
    }
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
