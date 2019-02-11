package com.example.tr.clerk;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class RetrieveListAdapter extends ArrayAdapter<RetrieveList> {
private List<RetrieveList> items;
        int resource;
        ImageView imageView;
public RetrieveListAdapter(Context context, List<RetrieveList> items) {
        super(context, R.layout.retrieverow, items);
        this.resource = R.layout.retrieverow;
        this.items = items;
        }
@Override
public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext()
        .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
final View v = inflater.inflate(resource, null);
        RetrieveList emp = items.get(position);
        if (emp != null) {
        int []dest = new int[]{R.id.textView1,R.id.textView4,R.id.textView2};
        String []src = new String[]{"ItemName","Count","CountDelivered"};
        //this part i reuse the order stuct,nobody should do in this way
        for (int n=0; n<dest.length; n++) {
        TextView txt = v.findViewById(dest[n]);
        txt.setText(emp.get(src[n]));

//todo: this part need to fix
//            new AsyncTask<String, Void, Bitmap>() {
//                @Override
//                protected Bitmap doInBackground(String... params) {
//                    return Book.getPhoto(params[0], true);
//                }
//                @Override
//                protected void onPostExecute(Bitmap result) {
//                    ImageView image = v.findViewById(R.id.imageView2);
//                    image.setImageBitmap(result);
//                }
//            }.execute(emp.get("EmployeeID"));
        }
                if (emp.get("stats").equals("finished")) {
                        imageView = v.findViewById(R.id.imageView2);
                        imageView.setImageResource(R.drawable.ic_star_on);
                }else
                {
                        Log.e("test",emp.get("stats"));
                        imageView = v.findViewById(R.id.imageView2);
                        imageView.setImageResource(R.drawable.ic_star_off);
                }
                //txt.setText(emp.get(src[n]));
        }
        return v;
        }
        }