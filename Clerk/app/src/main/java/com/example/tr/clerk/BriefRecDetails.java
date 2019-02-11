package com.example.tr.clerk;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BriefRecDetails extends HashMap<String, String> {
    static Host h = new Host();
    static String host = h.hosturl;
    static String baseURL;

    static {
        baseURL = String.format("http://%s/DeptAPI", host);
    }

    public BriefRecDetails(String rrid, String itemNumber, String itemName, Integer quantity){
        put("RRID", rrid);
        put("ItemNumber", itemNumber);
        put("ItemName", itemName);
        put("Quantity", quantity.toString());
    }
    public BriefRecDetails(String rrid, String itemName, Integer quantity){
        put("RRID", rrid);
        put("ItemName", itemName);
        put("Quantity", quantity.toString());
    }
    public BriefRecDetails(String rrid, String itemName, String quantity){
        put("RRID", rrid);
        put("ItemName", itemName);
        put("Quantity", quantity);
    }
    public BriefRecDetails(String rrid, String itemName){
        put("RRID", rrid);
        put("ItemName", itemName);
    }

    public static List<BriefRecDetails> ListRecDetails(String rrid) {

        List<BriefRecDetails> list = new ArrayList<BriefRecDetails>();
        JSONArray a = JSONParser.getJSONArrayFromUrl(baseURL + "/RecordDetails/" + rrid);
        try {
            for (int i = 0; i < a.length(); i++) {
                JSONObject b = a.getJSONObject(i);
                String ItemName = b.getString("itemName");
                Integer Quantity = b.getInt("quantity");
                String sQuantity = Quantity.toString();
                list.add(new BriefRecDetails(b.getString("rrid"),ItemName, sQuantity));
            }
        } catch (Exception e) {
            Log.e("BriefEmpReq", "NOW IS THE JSONArray error DAMN IT");
        }
        return (list);
    }
}
