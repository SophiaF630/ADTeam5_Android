package com.example.tr.clerk;

import android.util.Log;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VoucherDetail extends HashMap<String,String> {
    static Host h = new Host();
    static String host = h.hosturl;
    static String baseURL;
    static {
        baseURL = String.format("http://%s/api/voucher", host);
    }
    public VoucherDetail(String ItemName,String VoNo,String Quantity,String Remark){
        put("ItemName",ItemName);
        put("VoNo",VoNo);
        put("Quantity",Quantity);
        put("Remark",Remark);
    }

    public static List<VoucherDetail> ReadVoucherDetails(String VoNo) {
        List<VoucherDetail> result = new ArrayList<>();
        try {

            JSONArray a = JSONParser.getJSONArrayFromUrl(String.format("%s/details/%s",baseURL, VoNo));
            if (a == null)
            {
                return null;
            }
            List<String> k = new ArrayList<>();
            for (int i =0;i<a.length();i++)
            {
                k.add(a.getString(i));
            }
            for (String dep:k)
            {
                JSONArray b = JSONParser.getJSONArrayFromUrl(String.format("%s/detailsinfo/%s",baseURL, dep));
                List<String> temp = new ArrayList<>();
                for (int i =0;i<b.length();i++)
                {
                    temp.add(b.getString(i));
                }
                if (!temp.isEmpty())
                    result.add(new VoucherDetail(temp.get(0),temp.get(1),temp.get(2),temp.get(3)));
            }
            return result;
        } catch (Exception e) {
            Log.e("voucherdetail", "JSONArray error"+e);
        }
        return(null);
    }
}
