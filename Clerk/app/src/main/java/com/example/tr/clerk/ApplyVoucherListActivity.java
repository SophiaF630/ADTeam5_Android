package com.example.tr.clerk;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.List;

public class ApplyVoucherListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent.hasExtra("Mode")){
        setContentView(R.layout.activity_apply_voucher_list);
        }
        else
        setContentView(R.layout.activity_apply_voucher_list_1);
        show(intent);
    }
    private void show(Intent intent)
    {
        if (intent.hasExtra("VoNo")) {
            final String id = intent.getStringExtra("VoNo");
            if (intent.hasExtra("Mode")){
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
                        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                            @Override
                            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                                VoucherDetail selected = (VoucherDetail) parent.getAdapter().getItem(position);
                                getdialog(selected);
                                return true;
                            }
                        });
                    }}.execute(id);
                FloatingActionButton btn1 = (FloatingActionButton)findViewById(R.id.floatingActionButton_applyvoucher_create_add);
                btn1.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub

//                    //todo: this part must remove the login status
                        Intent intent1 = new Intent(getApplicationContext(), ApplyVoucherCUActivity.class);
                        intent1.putExtra("CorU","Create");
                        startActivity(intent1);
                    }});
                Button btn_cancell = (Button)findViewById(R.id.button_applyvoucher_create_cancel);
                btn_cancell.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ApplyVoucherListActivity.this,ApplyVoucherActivity.class);
                        startActivity(intent);
                    }});
                Button btn_confirm = (Button)findViewById(R.id.button_applyvoucher_create_submit);
                btn_confirm.setOnClickListener(new View.OnClickListener() {
                    SharedPreferences sharedPreferences=getSharedPreferences("config",0);
                    final int k = sharedPreferences.getInt("workid", 10004);
                    @Override
                    public void onClick(View v) {
                        new AsyncTask<Integer, Void, Void>() {
                            @Override
                            protected Void doInBackground(Integer... params) {
                                Voucher.ApplyVoucher(params[0]);
                                return null;
                            }
                            @Override
                            protected void onPostExecute(Void result) {
                                Intent intent = new Intent(ApplyVoucherListActivity.this,ApplyVoucherActivity.class);
                                startActivity(intent);
                            }}.execute(k);

                    }});
            }
            else {
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
                        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            }
                        });
                    }}.execute(id);
            }

        }
    }
    private void getdialog(final VoucherDetail source){
        new AlertDialog.Builder(this).setTitle("Are you sure you want to delete this item?").
                setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1){
                        String rdid=source.get("VoNo");
                        final Intent intent = getIntent();
                        final String id = intent.getStringExtra("Rrid");
                        new AsyncTask<String, Void, String>() {
                            @Override
                            protected String doInBackground(String... params) {
                                return recordDetail.RemoveRd(params[0]);
                            }
                            @Override
                            protected void onPostExecute(String result) {
//                                if (result=="ok")
//                                {
//                                    AlertDialog.Builder builder  = new AlertDialog.Builder(getApplicationContext());
//                                    builder.setTitle("ok" ) ;
//                                    builder.setMessage("remove successfully" ) ;
//                                    builder.setPositiveButton("ok" ,  null );
//                                    builder.show();
//                                }
//                                else
//                                {
//                                    AlertDialog.Builder builder  = new AlertDialog.Builder(getApplicationContext());
//                                    builder.setTitle("error" ) ;
//                                    builder.setMessage("error" ) ;
//                                    builder.setPositiveButton("ok" ,  null );
//                                    builder.show();
//                                }
                                show(intent);
                            }
                        }.execute(rdid);
                    }
                }).setNegativeButton("No",null).show();
    }
}
