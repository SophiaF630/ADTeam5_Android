package com.example.tr.clerk;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class IssueVoucherApproveActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private String exe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_voucher_list_detail);
        sharedPreferences=getSharedPreferences("config",0);
        final int id1 = sharedPreferences.getInt("workid", 10004);
        exe = id1+"";
        Intent intent = getIntent();
        if (intent.hasExtra("VoNo")){
            final String id = intent.getStringExtra("VoNo");
            TextView txt1 = findViewById(R.id.textView15);
            txt1.setText(intent.getStringExtra("Name"));
            TextView txt2 = findViewById(R.id.textView17);
            txt2.setText(intent.getStringExtra("Price"));
            Button btn_cancell = (Button)findViewById(R.id.button_applyvoucher_create_cancel);
            btn_cancell.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    getdia1(id);
                }});
            Button btn_confirm = (Button)findViewById(R.id.button_applyvoucher_create_submit);
            btn_confirm.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    getdia(id);
                }});
            //todo: here need to update logic
            new AsyncTask<String, Void, List<VoucherDetail>>() {
                @Override
                protected List<VoucherDetail> doInBackground(String... params) {
                    return VoucherDetail.ReadVoucherDetails(params[0]);
                }
                @Override
                protected void onPostExecute(List<VoucherDetail> result) {
                    if (result == null) {
                        return;
                    }
                    VoucherAdapter2 adapter = new VoucherAdapter2(getApplicationContext(),
                            result);
                    ListView list = (ListView) findViewById(R.id.listView_voucherlist);
                    list.setAdapter(adapter);
//                    list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//                        @Override
//                        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                            VoucherDetail selected = (VoucherDetail) parent.getAdapter().getItem(position);
//                            getdialog(selected);
//                            return true;
//                        }
//                    });
                }}.execute(id);
        }
    }
    private void getdia(final String id){
        new AlertDialog.Builder(this)
                .setTitle("Important" )
                .setMessage("You have selected Approve.Do you want to proceed?" )
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1){
                        new AsyncTask<String, Void, Void>() {
                            @Override
                            protected Void doInBackground(String... params) {
                                Voucher.IssueVoucher(params[0],exe);
                                return null;
                            }
                            @Override
                            protected void onPostExecute(Void result) {
                                Intent intent = new Intent(IssueVoucherApproveActivity.this,IssueVoucherListActivity.class);
                                startActivity(intent);}
                        }.execute(id);
                    }
                }).setNegativeButton("Cancel",null).show()
                .show();
    }
    private void getdia1(final String id){
        new AlertDialog.Builder(this)
                .setTitle("Important" )
                .setMessage("You have selected Reject.Do you want to proceed?" )
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1){
                        new AsyncTask<String, Void, Void>() {
                            @Override
                            protected Void doInBackground(String... params) {
                                Voucher.RejectIssueVoucher(params[0],exe);
                                return null;
                            }
                            @Override
                            protected void onPostExecute(Void result) {
                                Intent intent = new Intent(IssueVoucherApproveActivity.this,IssueVoucherListActivity.class);
                                startActivity(intent);}
                        }.execute(id);
                    }
                }).setNegativeButton("Cancel",null).show()
                .show();
    }
}
