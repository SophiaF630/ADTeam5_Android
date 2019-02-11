package com.example.tr.clerk;

import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class recordDetail extends HashMap<String,String> {
    static Host h = new Host();
    static String host = h.hosturl;
    static String baseURL;
    static {
        baseURL = String.format("http://%s/api/RecordDetails", host);
    }
    public recordDetail(String RDID,String RRID,String ItemName,String Quantity,String Remark)
    {
        put("RDID",RDID);
        put("RRID",RRID);
        put("ItemName",ItemName);
        put("Quantity",Quantity);
        put("Remark",Remark);
    }
    public static List<recordDetail> ReadOrderDetail(String rrid) {
        try {
            JSONObject a = JSONParser.getJSONFromUrl(String.format("%s/%s",baseURL, rrid));
            List<recordDetail> k = (List<recordDetail>)a;
            return k;
        } catch (Exception e) {
            Log.e("User", "JSONArray error");
        }
        return(null);
    }

    public static String PFitem(String amount,String rdid){
        String result = "";
        try {
            result = JSONParser.getStream(String.format("%s/PF/%s/%s",baseURL,amount, rdid));
        }
        catch (Exception e)
        {
            Log.e("RecordDetail", "JSONArray error"+e);
        }
        return result;
    }

    public static String PFitem1(Order source,SharedPreferences sp){
        String result = "";
        if(sp.getString("PF", "not").equals("not")){
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("PF","OK");
            editor.commit();
            try {
                result = JSONParser.postStream(baseURL + "/PFpf", source);
//            result = JSONParser.getStream(String.format("%s/PF1/%s/%s",baseURL,remark, rdid));
            }
            catch (Exception e)
            {
                Log.e("RecordDetail", "JSONArray error"+e);
            }
        }
        else {
            try {
                result = JSONParser.postStream(baseURL + "/PF", source);
//            result = JSONParser.getStream(String.format("%s/PF1/%s/%s",baseURL,remark, rdid));
            }
            catch (Exception e)
            {
                Log.e("RecordDetail", "JSONArray error"+e);
            }
        }
        return result;
    }

    public static String RemoveRd(String rdid){
        String result = "";
        try{
            result= JSONParser.getStream(String.format("%s/Remove/%s",baseURL, rdid));
        }
        catch (Exception e)
        {
            Log.e("RecordDetail remove", "JSONArray error"+e);
        }
        result = result.replace("\n","");
        return result;
    }
}
