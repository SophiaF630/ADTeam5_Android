package com.example.tr.clerk;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RetrieveList extends HashMap<String,String> {
    static Host h = new Host();
    static String host = h.hosturl;
    static String baseURL;

    static {
        baseURL = String.format("http://%s/api/retrieve", host);
    }

    public RetrieveList(String Catagory, String ItemName, String Count,String CountDelivered, String stats) {
        put("Catagory",Catagory);
        put("ItemName",ItemName);
        put("Count",Count);
        put("CountDelivered",CountDelivered);
        put("stats",stats);
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

    public static List<RetrieveList> GetRetrieveList(String Catalog) {
//        List<RetrieveList> result = new ArrayList<RetrieveList>();
//        JSONArray a = JSONParser.getJSONArrayFromUrl(String.format("%s/get", baseURL));
//        try {
//            for (int i = 0; i < a.length(); i++) {
//                JSONObject b = a.getJSONObject(i);
//                result.add(new RetrieveList(b.getString("Catagory"), b.getString("ItemName"), b.getString("Count"),b.getString("CountDelivered"), b.getString("Stats")));
//            }
//        } catch (Exception e) {
//            Log.e("User", "JSONArray error");
//        }
//        return result;
        try {
            List<RetrieveList> result = new ArrayList<>();
            JSONArray a = JSONParser.getJSONArrayFromUrl(String.format("%s/category/%s",baseURL,Catalog));
            if (a == null)
            {
                return null;
            }
            List<String> k = new ArrayList<>();
            for (int i =0;i<a.length();i++)
            {
                k.add(a.getString(i));
            }
            for (String vc:k)
            {
                JSONArray b = JSONParser.getJSONArrayFromUrl(String.format("%s/info/%s",baseURL, vc));
                List<String> temp = new ArrayList<>();
                for (int i =0;i<b.length();i++)
                {
                    temp.add(b.getString(i));
                }
                if (!temp.isEmpty())
                    result.add(new RetrieveList(temp.get(0),temp.get(1),temp.get(2),temp.get(3),temp.get(4)));
            }
            return result;
        }catch (Exception e)
        {
            Log.e("Retrieve", "JSONArray error"+e);
        }
        return null;}
    public static List<RetrieveList> GetRetrieveList() {
//        List<RetrieveList> result = new ArrayList<RetrieveList>();
//        JSONArray a = JSONParser.getJSONArrayFromUrl(String.format("%s/get", baseURL));
//        try {
//            for (int i = 0; i < a.length(); i++) {
//                JSONObject b = a.getJSONObject(i);
//                result.add(new RetrieveList(b.getString("Catagory"), b.getString("ItemName"), b.getString("Count"),b.getString("CountDelivered"), b.getString("Stats")));
//            }
//        } catch (Exception e) {
//            Log.e("User", "JSONArray error");
//        }
//        return result;
        try {
            List<RetrieveList> result = new ArrayList<>();
            JSONArray a = JSONParser.getJSONArrayFromUrl(String.format("%s",baseURL));
            if (a == null)
            {
                return null;
            }
            List<String> k = new ArrayList<>();
            for (int i =0;i<a.length();i++)
            {
                k.add(a.getString(i));
            }
            for (String vc:k)
            {
                JSONArray b = JSONParser.getJSONArrayFromUrl(String.format("%s/info/%s",baseURL, vc));
                List<String> temp = new ArrayList<>();
                for (int i =0;i<b.length();i++)
                {
                    temp.add(b.getString(i));
                }
                if (!temp.isEmpty())
                    result.add(new RetrieveList(temp.get(0),temp.get(1),temp.get(2),temp.get(3),temp.get(4)));
            }
            return result;
    }catch (Exception e)
        {
            Log.e("Retrieve", "JSONArray error"+e);
        }
        return null;}

    public List<String> GetCatagory(List<RetrieveList> source) {
        List<String> result = new ArrayList<String>();
        for (RetrieveList item : source) {
            if (result.contains(item.get("Catagory"))) {
            } else
                result.add(item.get("Catagory"));
        return result;
    }
    return null;
    }

    public List<RetrieveList> GetItemName(String Catagory, List<RetrieveList> source) {
        List<RetrieveList> result = new ArrayList<RetrieveList>();
        for (RetrieveList item : source
                ) {
            if (item.get("Catagory") == Catagory)
                result.add(item);
        }
        return result;
    }

    public static void saveRetrieve(List<RetrieveList> source){
        Map<String,String> jemp = new HashMap<>();
        for (RetrieveList item : source
                ) {
            try {
                jemp.put("Catagory", item.get("Catagory"));
                jemp.put("ItemName", item.get("ItemName"));
                jemp.put("Count", item.get("Count"));
                jemp.put("stats", item.get("stats"));
            } catch (Exception e) {
                Log.e("Book", "JSONArray error");
            }
            JSONParser.postStream(baseURL + "/set", jemp);
        }


    }
}