package com.example.tr.clerk;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Order extends HashMap<String,String> {
    static Host h = new Host();
    static String host = h.hosturl;
    static String baseURL;
    static {
        baseURL = String.format("http://%s/api/order", host);
    }
    public Order(String DepName,String RRID,String DepRep, String status) {
        put("DepName", DepName);
        put("RRID", RRID);
        put("DepRep", DepRep);
        put("status", status);
    }
    public static List<String> ReadCollectionPoints(){
        try {
            List<String> result = new ArrayList<>();
            JSONArray a = JSONParser.getJSONArrayFromUrl(String.format("%s/collection",baseURL));
            for (int i =0;i<a.length();i++)
            {
                result.add(a.getString(i));
            }
            return result;
        } catch (Exception e) {
            Log.e("collection", "JSONArray error"+e);
        }
        return (null);
    }
    public static List<Order> ReadOrders(String CollectionID) {
        try {
            List<Order> result = new ArrayList<>();
            JSONArray a = JSONParser.getJSONArrayFromUrl(String.format("%s/%s",baseURL, CollectionID));
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
                JSONArray b = JSONParser.getJSONArrayFromUrl(String.format("%s/orders/%s",baseURL, dep));
                List<String> temp = new ArrayList<>();
                for (int i =0;i<b.length();i++)
                {
                    temp.add(b.getString(i));
                }
                if (!temp.isEmpty())
                result.add(new Order(temp.get(0),temp.get(1),temp.get(2),temp.get(3)));
            }
//            Order e = new Order(k.get(0),k.get(1),k.get(2),k.get(3)
//            );
            return result;
        } catch (Exception e) {
            Log.e("Order", "JSONArray error"+e);
        }
        return(null);
    }
    public static List<Order> ReadOrderPFs(String rrID) {
        try {
            List<Order> result = new ArrayList<>();
            JSONArray a = JSONParser.getJSONArrayFromUrl(String.format("%s/pfpart/%s",baseURL, rrID));
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
                JSONArray b = JSONParser.getJSONArrayFromUrl(String.format("%s/pfparts/%s",baseURL, dep));
                List<String> temp = new ArrayList<>();
                for (int i =0;i<b.length();i++)
                {
                    temp.add(b.getString(i));
                }
                if (!temp.isEmpty())
                    result.add(new Order(temp.get(0),temp.get(1),temp.get(2),temp.get(3)));
            }
//            Order e = new Order(k.get(0),k.get(1),k.get(2),k.get(3)
//            );
            return result;
        } catch (Exception e) {
            Log.e("PFOrder", "JSONArray error"+e);
        }
        return(null);
    }
    public static void Noshow(String dlID){
        JSONParser.getStream(String.format("%s/NoShow/%s",baseURL, dlID));
    }
    public static void Delivered(String dlID){
        JSONParser.getStream(String.format("%s/Delivered/%s",baseURL, dlID));
    }
    public static void Delivered11(String dlID){
        JSONParser.getStream(String.format("%s/Deliveredpf/%s",baseURL, dlID));
    }
}
