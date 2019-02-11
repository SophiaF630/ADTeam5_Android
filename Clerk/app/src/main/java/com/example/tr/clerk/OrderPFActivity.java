package com.example.tr.clerk;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.List;

public class OrderPFActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_pf);
        sharedPreferences=getSharedPreferences("config",0);
        LoadPage();

    }
    private void LoadPage(){
        Intent intent = getIntent();
        if (intent.hasExtra("rrID"))
        {
            final String id = intent.getStringExtra("rrID");
            new AsyncTask<String, Void, List<Order>>() {
                @Override
                protected List<Order> doInBackground(String... params) {
                    return Order.ReadOrderPFs(params[0]);
                }

                @Override
                protected void onPostExecute(List<Order> result) {
                    if (result == null)
                    {
                        return;
                    }
                    OrderAdapter3 adapter = new OrderAdapter3(getApplicationContext(),
                            result);
                    ListView list = (ListView) findViewById(R.id._dynamic_order_pf);
                    list.setAdapter(adapter);
                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                            Order selected = (Order) parent.getAdapter().getItem(position);
//                            Intent intent = new Intent(getApplicationContext(), OrderDetailActivity.class);
//                            intent.putExtra("Rrid", selected.get("RRID"));
//                            startActivityForResult(intent, 123);
                            Order selected = (Order) parent.getAdapter().getItem(position);
//                            String Rdid = selected.get("DepName");
                            getdialog(selected);
                        }
                    });
                }
            }.execute(id);
            Button btn = (Button)findViewById(R.id.button_order_pf_confirm);
            btn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub

//                    //todo: this part must remove the login status
                    Intent intent = new Intent(OrderPFActivity.this,OrderDetailActivity.class);
                    intent.putExtra("Rrid",id);
                    startActivity(intent);
                }});
            Button btn1 = (Button)findViewById(R.id.button_order_pf_cancel);
            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(OrderPFActivity.this,OrderDetailActivity.class);
                    intent.putExtra("Rrid",id);
                    startActivity(intent);
                }});
        }

    }
    private void getdialog(final Order source){
        final EditText et=new EditText(this);
        et.setInputType( InputType.TYPE_CLASS_NUMBER);
        new AlertDialog.Builder(this).setTitle("Please enter the delivered quantity of this item.").setView(et).
                setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1){
                        String amount=et.getText().toString();
                        Intent intent = getIntent();
                        final String id = source.get("DepName");
                        int temp = 0;
                        try{
                            temp = Integer.parseInt(amount);
                        }
                        catch (Exception e)
                        {
                            return;
                        }
                        if (temp<0||temp>Integer.parseInt(source.get("DepRep")))
                        {
                            AlertDialog.Builder builder  = new AlertDialog.Builder(OrderPFActivity.this);
                            builder.setTitle("Error" ) ;
                            builder.setMessage("Please input valued amount" ) ;
                            builder.setPositiveButton("ok" ,  null );
                            builder.show();
                            return;
                        }
                        setRemark(source);
                        new AsyncTask<String, Void, String>() {
                            @Override
                            protected String doInBackground(String... params) {
                                return recordDetail.PFitem(params[0],id);
                            }
                            @Override
                            protected void onPostExecute(String result) {
                                LoadPage();
                            }
                        }.execute(amount);
                    }
                }).setNegativeButton("cancel",null).show();
    }
    public void setRemark(final Order source){
        final EditText et_remark = new EditText(this);
        new AlertDialog.Builder(this).setTitle("Please input remark").setView(et_remark).
                setPositiveButton("OK", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface arg0, int arg1){
                        String remark=et_remark.getText().toString();
                        final String id = source.get("DepName");
                        if (remark.equals(""))
                            return;
                        source.put("status",remark);
                        new AsyncTask<Order, Void, String>() {
                            @Override
                            protected String doInBackground(Order... params) {
                                return recordDetail.PFitem1(params[0],sharedPreferences);
                            }
                            @Override
                            protected void onPostExecute(String result) {
                                LoadPage();
                            }
                        }.execute(source);
                    }
                }).setNegativeButton("Cancel",null).show();
    }
}
