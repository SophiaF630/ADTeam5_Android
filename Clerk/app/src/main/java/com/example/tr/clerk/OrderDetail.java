package com.example.tr.clerk;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class OrderDetail extends HashMap<String,String> {
    static Host h = new Host();
    static String host = h.hosturl;
    static String baseURL;
    static {
        baseURL = String.format("http://%s/api/orderdetail", host);
    }
    public OrderDetail(String DepName,String RepName,String CollectionPointName,String OrderDetails,String Remark)
    {
        put("DepName",DepName);
        put("RepName",RepName);
        put("CollectionPointName",CollectionPointName);
        put("OrderDetails",OrderDetails);
        put("Remark",Remark);
    }
    public static OrderDetail ReadOrderDetail(String rrid) {
        try {
            JSONArray a = JSONParser.getJSONArrayFromUrl(String.format("%s/%s",baseURL, rrid));
            OrderDetail e = new OrderDetail(a.get(0).toString(),a.get(1).toString(),a.get(2).toString(),a.get(3).toString(),a.get(4).toString());
            return e;
        } catch (Exception e) {
            Log.e("User", "JSONArray error");
        }
        return(null);
    }
}
