package com.example.tr.clerk;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class ApplyVoucherCUActivity extends AppCompatActivity {

    Spinner spinner1;
    Spinner spinner2;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_voucher_cu);
        sharedPreferences=getSharedPreferences("config",0);
        final int ClerkID = sharedPreferences.getInt("workid", -300);
        Intent intent = getIntent();
        spinner1 =(Spinner) findViewById(R.id.spinner_applyvoucher_additem_itemcategory);
        spinner2 = (Spinner) findViewById(R.id.spinner_applyvoucher_additem_itemname);
        if (intent.hasExtra("CorU"))
        {
            setSpinner();
            final EditText edittext = findViewById(R.id.applyvoucher_quantity);
            edittext.setInputType( InputType.TYPE_CLASS_NUMBER);
            final EditText edittext2 = findViewById(R.id.editText_applyvoucher_additem_remark);
            //arr_adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data_list);
            List<String> temp = new ArrayList<>();
            temp.add("Inbound");
            temp.add("Outbound");
            ArrayAdapter<String> quantity_adapter= new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item, temp);
            final Spinner spinner = (Spinner)findViewById(R.id.spinner_applyvoucher_additem_quantity);
            spinner.setAdapter(quantity_adapter);
            Button btn_cancell = (Button)findViewById(R.id.button_applyvoucher_additem_cancel);
            btn_cancell.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ApplyVoucherCUActivity.this,ApplyVoucherListActivity.class);
                    intent.putExtra("VoNo",("VTemp"+ClerkID));
                    intent.putExtra("Mode","Edit");
                    startActivity(intent);
                }});
            Button btn_retrive = (Button)findViewById(R.id.button_applyvoucher_additem_apply);
            btn_retrive.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    SharedPreferences sharedPreferences;
                    sharedPreferences=getSharedPreferences("config",0);
                    final int id = sharedPreferences.getInt("workid", 10004);
//                    source.add("VTemp"+id);
//                    source.add(spinner2.getSelectedItem().toString());
//                    if (spinner.getSelectedItem().toString() == "Inbound")
//                    source.add(edittext.getText().toString());
//                    else
//                        source.add((Integer.parseInt(edittext.getText().toString())*-1)+"");
//                    source.add(edittext2.getText().toString());
                    details source = new details("VTemp"+id,spinner2.getSelectedItem().toString(),spinner.getSelectedItem().toString() == "Inbound"?edittext.getText().toString():((Integer.parseInt(edittext.getText().toString())*-1)+""),edittext2.getText().toString());
                    new AsyncTask<details, Void, Void>() {
                        @Override
                        protected Void doInBackground(details...params) {
                            Voucher.SaveVoucher(params[0]);
                            Intent intent = new Intent(ApplyVoucherCUActivity.this,ApplyVoucherListActivity.class);
                            intent.putExtra("VoNo",("VTemp"+ClerkID));
                            intent.putExtra("Mode","Edit");
                            startActivity(intent);
                            return null;
                        }
                    }.execute(source);
                }});
        }
        //todo: this part receive the voucher number and come out with enough problems
    }
    public void setSpinner(){
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
                        new AsyncTask<String, Void, List<String>>() {
                            @Override
                            protected List<String> doInBackground(String...params) {
                                return Voucher.GetItems(params[0]);
                            }

                            @Override
                            protected void onPostExecute(List<String> result) {
                                ArrayAdapter<String> arr_adapter= new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item, result);
                                arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner2.setAdapter(arr_adapter);
                            }
                        }.execute(itemname);

                    }
                    @Override//什么都没选
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        }.execute();
    };
}
