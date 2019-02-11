package com.example.tr.clerk;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class EmployeeListActivity extends Activity{

    static String dept;
    static String repIdName;
    static List<BriefEmp> result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_list);

        Bundle extras = getIntent().getExtras();
        dept = extras.getString("Dept");

        new AsyncTask<String, Void, List<BriefEmp>>() {
            @Override
            protected List<BriefEmp> doInBackground(String... params) {
                result = BriefEmp.ListEmp(dept);
                return result;


            }
            @Override
            protected void onPostExecute(List<BriefEmp> result) {
                MyAdapter adapter = new MyAdapter(getApplicationContext(),
                        result);

                ListView list = (ListView) findViewById(R.id.listView1);
                list.setAdapter(adapter);


                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        BriefEmp selected = (BriefEmp) parent.getAdapter().getItem(position);
                        repIdName = selected.get("UserID");
                        makeAlertDialog();

                    }
                });
            }
        }.execute();


    }

    void makeAlertDialog(){
        new AlertDialog.Builder(this)
                .setTitle("Change Department Representative")
                .setMessage("Assign employee as department rep?")
                .setCancelable(false)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        final BriefDept briefDept = new BriefDept(dept, repIdName);

                        new AsyncTask<BriefDept, Void, Void>(){
                            @Override
                            protected Void doInBackground(BriefDept...params){
                                BriefDept.saveBriefDept(briefDept);
                                return null;
                            }
                        }.execute(briefDept);

                        Intent intent = new Intent(getApplicationContext(), DeptRepActivity.class);
                        intent.putExtra("Dept", dept);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), "Department rep changed successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(EmployeeListActivity.this, "Changes are not saved", Toast.LENGTH_SHORT).show();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }




}


