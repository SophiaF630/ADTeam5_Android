package com.example.tr.clerk;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {
    private Button btn_order;
    private Button btn_applyvoucher;
    private Button btn_voucherapproval;
    private Button btn_retrieve;
    private Button btn_myinfo;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences=getSharedPreferences("config",0);
//        AlertDialog.Builder builder  = new AlertDialog.Builder(MenuActivity.this);
//        builder.setTitle("Error" ) ;
//        builder.setMessage("Please input correct email and password"+ sharedPreferences.getInt("workid", 10009)+"1212") ;
//        builder.setPositiveButton("Ok" ,  null );
//        builder.show();
        //todo:after debug this place should release
        final int id = sharedPreferences.getInt("workid", 10004);
        new AsyncTask<Integer, Void, User>() {
            @Override
            protected User doInBackground(Integer... params) {
                return User.ReadUser(params[0]);
            }
            @Override
            protected void onPostExecute(final User result) {
                if (result.get("role").equals("Clerk")){
                    setContentView(R.layout.activity_menu);
                    initView();
                    btn_voucherapproval.setVisibility(View.GONE);
                }
                else if (result.get("role").equals("Superviser")||result.get("role").equals("Manager")){
                    setContentView(R.layout.activity_menu);
                    initView();
                    btn_order.setVisibility(View.GONE);
                    btn_retrieve.setVisibility(View.GONE);
                    btn_applyvoucher.setVisibility(View.GONE);
                    btn_voucherapproval.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            Intent intent = new Intent(MenuActivity.this, IssueVoucherListActivity.class);
                            intent.putExtra("role",result.get("role"));
                            startActivity(intent);
                        }
                    });
                }
                else if (result.get("role").equals("Head")||result.get("role").equals("CoveringHead"))
                {
                    final String dept = result.get("DepartmentCode");
                    setContentView(R.layout.activity_main);
                    Button button1 = (Button) findViewById(R.id.button1);
                    button1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent1 = new Intent(getApplicationContext(), OrdersActivity.class);
                            intent1.putExtra("Dept", dept);
                            startActivity(intent1);
                        }
                    });

                    Button button2 = (Button) findViewById(R.id.button2);
                    button2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent2 = new Intent(getApplicationContext(), DeptRepActivity.class);
                            intent2.putExtra("Dept", dept);
                            startActivity(intent2);
                        }
                    });

//                    Button button3 = (Button) findViewById(R.id.button3);
//                    button3.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                  /*Intent intent3 = new Intent(getApplicationContext(), DeputyHeadActivity.class);
//            intent3.putExtra("Dept", dept);
//            startActivity(intent3);*/
//                        }
//                    });
                    Button button4 = (Button) findViewById(R.id.button4);
                    button4.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            Intent intent = new Intent(MenuActivity.this,InformationActivity.class);
                            intent.putExtra("UserID",sharedPreferences.getInt("workid", 10003));
                            startActivity(intent);
                        }
                    });
                }
                else if (result.get("role").equals("Rep"))
                {
                    setContentView(R.layout.activity_main2);
                    final String dept = result.get("DepartmentCode");
                    Button button1 = (Button) findViewById(R.id.button1);
                    button1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent1 = new Intent(getApplicationContext(), UpcomingCollectionActivity.class);
                            intent1.putExtra("Dept", dept);
                            startActivity(intent1);
                        }
                    });

                    Button button2 = (Button) findViewById(R.id.button2);
                    button2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent2 = new Intent(getApplicationContext(), CollectionPointActivity.class);
                            intent2.putExtra("Dept", dept);
                            startActivity(intent2);
                        }
                    });
                    Button button3 = (Button) findViewById(R.id.button3);
                    button3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent3 = new Intent(getApplicationContext(), CollectionPasswordActivity.class);
                            intent3.putExtra("Dept", dept);
                            startActivity(intent3);
                        }
                    });
                    Button button4 = (Button) findViewById(R.id.button4);
                    button4.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            Intent intent = new Intent(MenuActivity.this,InformationActivity.class);
                            intent.putExtra("UserID",sharedPreferences.getInt("workid", 10003));
                            startActivity(intent);
                        }
                    });
                }
                else {
                    setContentView(R.layout.activity_menu);
                    initView();
                    btn_order.setVisibility(View.GONE);
                    btn_retrieve.setVisibility(View.GONE);
                    btn_applyvoucher.setVisibility(View.GONE);
                    btn_voucherapproval.setVisibility(View.GONE);
                }
            }
        }.execute(id);

    }
    //todo: this part overwrite the back button
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode==KeyEvent.KEYCODE_BACK){
            new AlertDialog.Builder(this).setTitle("Warning")
                    .setMessage("Are you sure you want to exit?" )
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1){
//                            android.os.Process.killProcess(android.os.Process.myPid());
//                            System.exit(0);
                            Intent startMain = new Intent(Intent.ACTION_MAIN);
                            startMain.addCategory(Intent.CATEGORY_HOME);
                            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(startMain);
                            System.exit(0);
                        }
                    }).setNegativeButton("No",null).show();
        }
        return true;
    }
    private void initView() {
        // TODO Auto-generated method stub
        btn_order=(Button)findViewById(R.id.button_menu_order);
        btn_applyvoucher=(Button)findViewById(R.id.button_menu_applyvoucher);
        btn_voucherapproval=(Button)findViewById(R.id.button_menu_voucherapproval);
        btn_retrieve=(Button)findViewById(R.id.button_menu_retrievelist);
        btn_myinfo=(Button)findViewById(R.id.button_menu_myinformation);

        btn_order.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(MenuActivity.this,OrderActivity.class);
                startActivity(intent);
            }
        });

        btn_applyvoucher.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(MenuActivity.this,ApplyVoucherActivity.class);
                startActivity(intent);
            }
        });



        btn_retrieve.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(MenuActivity.this,RetrieveListActivity.class);
                startActivity(intent);
            }
        });

        btn_myinfo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(MenuActivity.this,InformationActivity.class);
                intent.putExtra("UserID",sharedPreferences.getInt("workid", 10003));
                startActivity(intent);
            }
        });
    }
}

