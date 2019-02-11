package com.example.tr.clerk;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.List;

public class RetrieveListSummaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve_list_summary);
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
                            AlertDialog.Builder builder  = new AlertDialog.Builder(RetrieveListSummaryActivity.this);
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
    private void LoadPage(){
        new AsyncTask<Void, Void, List<RetrieveList>>() {
            @Override
            protected List<RetrieveList> doInBackground(Void... params) {
                return RetrieveList.GetRetrieveList();
            }
            @Override
            protected void onPostExecute(List<RetrieveList> result) {
                if (result == null) {
                    return;
                }
                RetrieveListAdapter adapter = new RetrieveListAdapter(getApplicationContext(),
                        result);
                ListView list = (ListView) findViewById(R.id._dynamic_retrieve_summary);
                list.setAdapter(adapter);
                                list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                                    @Override
                                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                                        RetrieveList selected = (RetrieveList) parent.getAdapter().getItem(position);
                                        getdialog(selected);
                                        return true;
                                    }
                                });
            }}.execute();
        Button back = (Button)findViewById(R.id.button_retrieve_summary_cancel);
        back.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        Intent intent = new Intent(RetrieveListSummaryActivity.this,RetrieveListActivity .class);
                        startActivity(intent);
                    }
                });
    }
}
