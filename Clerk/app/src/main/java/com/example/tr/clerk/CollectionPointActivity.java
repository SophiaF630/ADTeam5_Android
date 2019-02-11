package com.example.tr.clerk;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CollectionPointActivity extends Activity {

    static String dept;
    static String newCollectionPointName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_point);

        Bundle extras = getIntent().getExtras();
        dept = extras.getString("Dept");

        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                intent.putExtra("Dept", dept);
                startActivity(intent);
            }
        });

        //Get current collection point
        new AsyncTask<String, Void, BriefDept2>() {
            @Override
            protected BriefDept2 doInBackground(String... params) {
                return BriefDept2.GetCurrentCollectionPoint(dept);
            }

            @Override
            protected void onPostExecute(BriefDept2 result) {
                show(result);
            }
        }.execute(dept);

        //Populate dropdownlist
        new AsyncTask<Void, Void, List<BriefDept2>>() {
            @Override
            protected List<BriefDept2> doInBackground(Void... params) {
                return BriefDept2.ListCollectionPoints();
            }

            @Override
            protected void onPostExecute(List<BriefDept2> result) {
                List<String> list = new ArrayList<String>();
                for (BriefDept2 current : result) {
                    list.add(current.get("CollectionPointName"));
                }
                Spinner spinner = (Spinner) findViewById(R.id.spinner);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, list);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
            }
        }.execute();

        Button button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                makeAlertDialog();
            }
        });

    }

    void show(BriefDept2 briefDept2) {

        String name = briefDept2.get("CollectionPointName");
        TextView t = findViewById(R.id.textView2);
        t.setText(name);
    }

    void makeAlertDialog(){
        new AlertDialog.Builder(this)
                .setTitle("Confirm Changes?")
                .setMessage("Changes made from Friday 5.30pm to Monday 11.59pm will only take effect after the upcoming collection." + '\n' + '\n'+ "Do you wish to continue?")
                .setCancelable(false)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        Spinner spinner = (Spinner) findViewById(R.id.spinner);
                        newCollectionPointName = spinner.getSelectedItem().toString();
                        final BriefDept2 briefDept2 = new BriefDept2(dept, newCollectionPointName);

                        new AsyncTask<BriefDept2, Void, Void>(){
                            @Override
                            protected Void doInBackground(BriefDept2...params){
                                BriefDept2.saveBriefDept2(briefDept2);
                                return null;
                            }
                        }.execute(briefDept2);

                        Intent intent = new Intent(getApplicationContext(), CollectionPointActivity.class);
                        intent.putExtra("Dept", dept);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), "Collection point changed successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(CollectionPointActivity.this, "Changes were not saved", Toast.LENGTH_SHORT).show();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

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


