package com.example.tr.clerk;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class OrderDetailActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        sharedPreferences=getSharedPreferences("config",0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("PF","not");
        editor.apply();
        Intent intent = getIntent();
        if (intent.hasExtra("Rrid")) {
            final String id = intent.getStringExtra("Rrid");
            new AsyncTask<String, Void, OrderDetail>() {
                @Override
                protected OrderDetail doInBackground(String... params) {
                    return OrderDetail.ReadOrderDetail(params[0]);
                }
                @Override
                protected void onPostExecute(OrderDetail result) {
                    show(result);
                }
            }.execute(id);


            Button btn3 = (Button)findViewById(R.id.button_order_details_pf);
            btn3.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    //开启子线程，实现网络请求
//                thread=new Thread(new loginRunnable());
//                thread.start();
                    Intent intent1 = new Intent(OrderDetailActivity.this,OrderPFActivity.class);
                    intent1.putExtra("rrID", id);
                    //todo: this part must remove the login status
                    startActivity(intent1);
                }});
            Button btn2 = (Button)findViewById(R.id.button_order_details_delivered);
            btn2.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    //开启子线程，实现网络请求
//                thread=new Thread(new loginRunnable());
//                thread.start();
//                    Intent intent1 = new Intent(OrderDetailActivity.this,OrderPFActivity.class);
//                    intent1.putExtra("rrID", id);
//                    //todo: this part must remove the login status
//                    startActivity(intent1);
                    getdialog();
                }});
            Button btn1 = (Button)findViewById(R.id.button_order_details_noshow);
            btn1.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    //开启子线程，实现网络请求
//                thread=new Thread(new loginRunnable());
//                thread.start();
//                    Intent intent1 = new Intent(OrderDetailActivity.this,OrderPFActivity.class);
//                    intent1.putExtra("rrID", id);
//                    //todo: this part need no show method
//                    startActivity(intent1);
                    new AsyncTask<String, Void, Void>() {
                        @Override
                        protected Void doInBackground(String... params) {
                            Order.Noshow(id);
                            return null;
                        }
                        @Override
                        protected void onPostExecute(Void result) {
                            Intent intent1 = new Intent(OrderDetailActivity.this,OrderActivity.class);
                            startActivity(intent1);
                        }
                    }.execute(id);
                }});

        }
    }
    public void show(OrderDetail source)
    {
        TextView textView1 = findViewById(R.id.textView_order_details_depatrmentname);
        textView1.setText(source.get("DepName"));
        TextView textView2 = findViewById(R.id.textView_order_details_name);
        textView2.setText(source.get("RepName"));
        TextView textView3 = findViewById(R.id.textView_order_details_collectionpoint);
        textView3.setText(source.get("CollectionPointName"));
        TextView textView4 = findViewById(R.id.textView_order_details_orderdetail);
        textView4.setText(source.get("OrderDetails"));
        TextView txt = findViewById(R.id.editText_order_details_remark);
        txt.setText(source.get("Remark"));
    }
    private void getdialog(){
        final EditText et=new EditText(this);

        new AlertDialog.Builder(this).setTitle("Please input password shown on the department representative's phone.").setView(et).
                setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1){
                        String password=et.getText().toString();
                        Intent intent = getIntent();
                        final String id = intent.getStringExtra("Rrid");
                        new AsyncTask<String, Void, Boolean>() {
                            @Override
                            protected Boolean doInBackground(String... params) {
                                return User.CheckCollectPassword(params[0],id);
                            }
                            @Override
                            protected void onPostExecute(Boolean result) {
                                if (result)
                                {
                                    new AsyncTask<String,Void,Void>(){
                                        @Override
                                        protected Void doInBackground(String... params){
                                            if(sharedPreferences.getString("PF", "not").equals("not"))
                                            Order.Delivered(params[0]);
                                            else{
                                                Order.Delivered11(params[0]);
                                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                                editor.putString("PF","not");
                                                editor.commit();
                                            }
                                            return null;
                                        }
                                        @Override
                                        protected void onPostExecute(Void result){
                                            Intent intent1 = new Intent(OrderDetailActivity.this,OrderActivity.class);
                                            startActivity(intent1);
                                        }
                                    }.execute(id);
                                }
                                else
                                {
                                    AlertDialog.Builder builder  = new AlertDialog.Builder(OrderDetailActivity.this);
                                    builder.setTitle("Error" ) ;
                                    builder.setMessage("Incorrect password. Please try again." ) ;
                                    builder.setPositiveButton("OK" ,  null );
                                    builder.show();
                                }
                                return;
                            }
                        }.execute(password);
                    }
                }).setNegativeButton("Cancel",null).show();
    }
}
