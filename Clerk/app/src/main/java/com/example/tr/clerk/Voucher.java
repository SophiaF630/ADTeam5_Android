package com.example.tr.clerk;

import android.util.Log;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Voucher extends HashMap<String,String>{
    static Host h = new Host();
    static String host = h.hosturl;
    static String baseURL;
    static {
        baseURL = String.format("http://%s/api/voucher", host);
    }


    public Voucher(String VoNo,String IssueDate,String Price,String status){
//todo:this part need finish voucher(ask yuxi for logic of voucher)
        put("VoNo",VoNo);
        put("IssueDate",IssueDate);
        put("Price",Price);
        put("status",status);
    }

    public static List<Voucher> ReadVouchers(String UserID){
        try {
            List<Voucher> result = new ArrayList<>();
            JSONArray a = JSONParser.getJSONArrayFromUrl(String.format("%s/%s",baseURL, UserID));
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
                    result.add(new Voucher(temp.get(0),temp.get(1),temp.get(2),temp.get(3)));
            }
            return result;
        }
        catch (Exception e)
        {
            Log.e("Voucher", "JSONArray error"+e);
        }
        return null;
    }
    public static List<Voucher> ReadVouchers1(String UserID){
        try {
            List<Voucher> result = new ArrayList<>();
            JSONArray a = JSONParser.getJSONArrayFromUrl(String.format("%s/manager/%s",baseURL, UserID));
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
                JSONArray b = JSONParser.getJSONArrayFromUrl(String.format("%s/manager/info/%s",baseURL, vc));
                List<String> temp = new ArrayList<>();
                for (int i =0;i<b.length();i++)
                {
                    temp.add(b.getString(i));
                }
                if (!temp.isEmpty())
                    result.add(new Voucher(temp.get(0),temp.get(1),temp.get(2),temp.get(3)));
            }
            return result;
        }
        catch (Exception e)
        {
            Log.e("Voucher", "JSONArray error"+e);
        }
        return null;
    }
    public static List<String> GetCatas(){
        List<String> result = new ArrayList<>();
        try {
            JSONArray b = JSONParser.getJSONArrayFromUrl(String.format("%s/Cata/List",baseURL));
            for (int i =0;i<b.length();i++)
            {
                result.add(b.getString(i));
            }
        }
        catch (Exception e)
        {
            Log.e("Cata", "JSONArray error"+e);
        }
        return result;
    }
    public static List<String> GetItems(String id){
        List<String> result = new ArrayList<>();
        try {
            JSONArray b = JSONParser.getJSONArrayFromUrl(String.format("%s/Cata/List/%s",baseURL,id));
            for (int i =0;i<b.length();i++)
            {
                result.add(b.getString(i));
            }
        }
        catch (Exception e)
        {
            Log.e("ItemNames", "JSONArray error"+e);
        }
        return result;
    }
    public static String SaveVoucher(details source){
        Log.e("ItemNames", "JSONArray error"+source.toString());
        Map<String,String> bDept = new HashMap<>();
        try{
            bDept.put("VoNo", source.get("VoNo"));
            bDept.put("ItemName", source.get("ItemName"));
            bDept.put("Remark", source.get("Remark"));
            bDept.put("Quantity", source.get("Quantity"));
        }catch(Exception e){
            Log.e("BriefDept", "ERROR IN SAVING" + e.toString());
        }
        Log.e("BriefDept", "ERROR IN SAVING" + bDept.toString());
        JSONParser.postStream(baseURL+"/save", bDept);
        return "ok";
    }
    public static void ApplyVoucher(int ClarkID){
        JSONParser.getStream(String.format("%s/ApplyVoucher/%s",baseURL, ClarkID));
    }
    public static void IssueVoucher(String VoNo,String UserID){
        JSONParser.getStream(String.format("%s/IssueVoucher/%s/%s",baseURL, VoNo,UserID));
    }
    public static void RejectIssueVoucher(String VoNo,String UserID){
        JSONParser.getStream(String.format("%s/RejectIssueVoucher/%s/%s",baseURL, VoNo,UserID));
    }
}
class details extends HashMap<String,String>{
    public details(String VoNo,String ItemName,String Quantity,String Remark){
        put("VoNo",VoNo);
        put("ItemName",ItemName);
        put("Quantity",Quantity);
        put("Remark",Remark);
    }
}
