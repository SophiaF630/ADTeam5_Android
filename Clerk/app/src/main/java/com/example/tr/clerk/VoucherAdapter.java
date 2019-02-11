package com.example.tr.clerk;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class VoucherAdapter extends ArrayAdapter<Voucher> {
    private List<Voucher> items;
    int resource;
    public VoucherAdapter(Context context, List<Voucher> items) {
        super(context, R.layout.voucherstartrow, items);
        this.resource = R.layout.voucherstartrow;
        this.items = items;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        final View v = inflater.inflate(resource, null);
        Voucher emp = items.get(position);
        if (emp != null) {
            int []dest = new int[]{R.id.textView1,R.id.textView2,R.id.textView3};
            String []src = new String[]{"IssueDate", "Price","status"};
            for (int n=0; n<dest.length; n++) {
                TextView txt = v.findViewById(dest[n]);
                txt.setText(emp.get(src[n]));
                //txt.setText(emp.get(src[n]));
            }
//todo: this part maybe need to fix one day but not today
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
        return v;
    }
}
