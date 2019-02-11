package com.example.tr.clerk;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class MyAdapter3 extends ArrayAdapter<BriefRecDetails> {

    private List<BriefRecDetails> items;
    int resource;

    public MyAdapter3(Context context, List<BriefRecDetails> items) {
        super(context, R.layout.row5, items);
        this.resource = R.layout.row5;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        final View v = inflater.inflate(resource, null);
        BriefRecDetails book = items.get(position);
        if (book != null) {
            int[] dest = new int[]{R.id.textView1, R.id.textView2};
            String[] src = new String[]{"ItemName", "Quantity"};
            for (int n = 0; n < dest.length; n++) {
                TextView txt = v.findViewById(dest[n]);
                txt.setText(book.get(src[n]));
            }
        }
        return v;
    }
}