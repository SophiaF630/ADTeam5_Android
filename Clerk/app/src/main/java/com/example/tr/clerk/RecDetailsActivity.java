package com.example.tr.clerk;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class RecDetailsActivity extends Activity {

    static String dept;
    static String RRID;
    static String Name;
    static String remark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rec_details);

        Intent intent = getIntent();
        RRID = intent.getExtras().getString("RRID");
        Name = intent.getExtras().getString("Name");
        dept = intent.getExtras().getString("Dept");


        TextView textView = (TextView)findViewById(R.id.textView1);
        textView.setText("Employee: " +Name);

        Button button2 = (Button) findViewById(R.id.Button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final BriefEmpReq briefEmpReq = new BriefEmpReq(RRID, Name);

                new AsyncTask<BriefEmpReq, Void, Void>(){
                    @Override
                    protected Void doInBackground(BriefEmpReq...params){
                        BriefEmpReq.approveRequest(briefEmpReq);
                        return null;
                    }
                }.execute(briefEmpReq);

                Intent intent = new Intent(getApplicationContext(), OrdersActivity.class);
                intent.putExtra("Dept", dept);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "Request approved successfully", Toast.LENGTH_SHORT).show();
            }
        });

        Button button1 = (Button) findViewById(R.id.Button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCustomDialog();

            }
        });


        new AsyncTask<String, Void, List<BriefRecDetails>>() {
            @Override
            protected List<BriefRecDetails> doInBackground(String... params) {
                return BriefRecDetails.ListRecDetails(RRID);
            }
            @Override
            protected void onPostExecute(List<BriefRecDetails> result) {
                MyAdapter3 adapter = new MyAdapter3(getApplicationContext(),
                        result);

                ListView list = (ListView) findViewById(R.id.listView1);
                list.setAdapter(adapter);
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                });
            }
        }.execute();

    }

    void makeCustomDialog(){
        final EditText et=new EditText(this);
        new android.support.v7.app.AlertDialog.Builder(this).setTitle("Please enter reason for rejection.").setView(et).
                setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1){
                        remark = et.getText().toString();
                        final BriefEmpReq briefEmpReq = new BriefEmpReq(RRID, remark);
                        new AsyncTask<BriefEmpReq, Void, Void>(){
                            @Override
                            protected Void doInBackground(BriefEmpReq...params){
                                BriefEmpReq.rejectRequest(briefEmpReq);
                                return null;
                            }
                            @Override
                            protected void onPostExecute(Void result) {
                                Intent intent = new Intent(getApplicationContext(), OrdersActivity.class);
                                intent.putExtra("Dept", dept);
                                startActivity(intent);
                                Toast.makeText(getApplicationContext(), "Request rejected successfully", Toast.LENGTH_SHORT).show();
                            }
                        }.execute(briefEmpReq);
                    }
                }).setNegativeButton("cancel",null).show();
//        final AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
//        LayoutInflater inflater = this.getLayoutInflater();
//        View dialogView = inflater.inflate(R.layout.custom_dialog, null);
//
//        final EditText editText = (EditText) dialogView.findViewById(R.id.edt_comment);
//        Button button3 = (Button) dialogView.findViewById(R.id.buttonSubmit);
//        Button button4 = (Button) dialogView.findViewById(R.id.buttonCancel);
//
//        button3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                remark = editText.getText().toString();
//                final BriefEmpReq briefEmpReq = new BriefEmpReq(RRID, remark);
//
//                new AsyncTask<BriefEmpReq, Void, Void>(){
//                    @Override
//                    protected Void doInBackground(BriefEmpReq...params){
//                        BriefEmpReq.rejectRequest(briefEmpReq);
//                        return null;
//                    }
//                }.execute(briefEmpReq);
//
//                Intent intent = new Intent(getApplicationContext(), OrdersActivity.class);
//                intent.putExtra("Dept", dept);
//                startActivity(intent);
//                Toast.makeText(getApplicationContext(), "Request rejected successfully", Toast.LENGTH_SHORT).show();
//            }
//        });
//        button4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // DO SOMETHINGS
//                dialogBuilder.dismiss();
//            }
//        });
//
//        dialogBuilder.setView(dialogView);
//        dialogBuilder.show();
    }
}
