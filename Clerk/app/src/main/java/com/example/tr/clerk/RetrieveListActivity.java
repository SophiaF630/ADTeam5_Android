package com.example.tr.clerk;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.sql.BatchUpdateException;
import java.util.List;

public class RetrieveListActivity extends AppCompatActivity {

    Spinner spinner1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve_list);
        LoadPage();
    }
    private void getdialog(final RetrieveList source){
        final EditText et=new EditText(this);

        new AlertDialog.Builder(this).setTitle("Please input the num you retrieve").setView(et).
                setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1){
                        String amount=et.getText().toString();
                        Intent intent = getIntent();
                        final String id = source.get("ItemName");
                        int temp = 0;
                        try{
                            temp = Integer.parseInt(amount);
                        }
                        catch (Exception e)
                        {
                            return;
                        }
                        if (temp<0||temp>Integer.parseInt(source.get("Count")))
                        {
                            AlertDialog.Builder builder  = new AlertDialog.Builder(RetrieveListActivity.this);
                            builder.setTitle("Error" ) ;
                            builder.setMessage("Please input valued amount" ) ;
                            builder.setPositiveButton("ok" ,  null );
                            builder.show();
                            return;
                        }
                        new AsyncTask<String, Void, String>() {
                            @Override
                            protected String doInBackground(String... params) {
                                return RetrieveList.PFitem(params[0],id);
                            }
                            @Override
                            protected void onPostExecute(String result) {
                                LoadPage();
                            }
                        }.execute(amount);
                    }
                }).setNegativeButton("cancel",null).show();
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
    public void LoadPage(){
        spinner1 =(Spinner) findViewById(R.id.spinner);
        new AsyncTask<Void, Void, List<String>>() {
            @Override
            protected List<String> doInBackground(Void...params) {
                return Voucher.GetCatas();
            }

            @Override
            protected void onPostExecute(List<String> result) {
                ArrayAdapter<String> arr_adapter= new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item, result);
                arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner1.setAdapter(arr_adapter);
                spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        String itemname = (String) parent.getAdapter().getItem(position);
                        new AsyncTask<String, Void, List<RetrieveList>>() {
                            @Override
                            protected List<RetrieveList> doInBackground(String... params) {
                                return RetrieveList.GetRetrieveList(params[0]);
                            }
                            @Override
                            protected void onPostExecute(List<RetrieveList> result) {
                                if (result == null) {
                                    return;
                                }
                                RetrieveListAdapter adapter = new RetrieveListAdapter(getApplicationContext(),
                                        result);
                                ListView list = (ListView) findViewById(R.id._dynamic_retrieve_list);
                                list.setAdapter(adapter);
                                list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                                    @Override
                                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                                        RetrieveList selected = (RetrieveList) parent.getAdapter().getItem(position);
                                        getdialog(selected);
                                        return true;
                                    }
                                });
                            }}.execute(itemname);

                    }
                    @Override//什么都没选
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        }.execute();

        Button summary = (Button)findViewById(R.id.button_retrieve_summary);
        summary.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        Intent intent = new Intent(RetrieveListActivity.this, RetrieveListSummaryActivity.class);
                        startActivity(intent);

                    }
                });
        Button retrieve = (Button)findViewById(R.id.button_retrieve_list_apply);
        retrieve.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        Intent intent = new Intent(RetrieveListActivity.this, MenuActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
//todo:retrieve
                    }
                });
        Button cancell = (Button)findViewById(R.id.button_retrieve_list_cancel);
        cancell.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        Intent intent = new Intent(RetrieveListActivity.this, MenuActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    }
                });

    }
}
