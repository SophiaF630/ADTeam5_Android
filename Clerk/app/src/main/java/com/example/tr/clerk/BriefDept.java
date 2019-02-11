package com.example.tr.clerk;

import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class BriefDept extends HashMap<String, String> {
    static Host h = new Host();
    static String host = h.hosturl;
    static String baseURL;
    static{
        baseURL = String.format("http://%s/DeptAPI", host);
    }
    public BriefDept(String deptCode, Integer repId, String collectionPassword){
        put("DeptCode", deptCode);
        put("RepID", repId.toString());
        put("CollectionPassword", collectionPassword);
    }
    public BriefDept(String deptCode, String repId, String collectionPassword){
        put("DeptCode", deptCode);
        put("RepID", repId);
        put("CollectionPassword", collectionPassword);
    }
    public BriefDept(String deptCode, String repId){
        put("DeptCode", deptCode);
        put("RepID", repId.toString());
    }
    public BriefDept(String collectionPassword){
        put("CollectionPassword", collectionPassword);
    }

    public static void saveBriefDept(BriefDept briefDept){
        Map<String,String> bDept = new HashMap<>();
        try{
            bDept.put("DeptCode", briefDept.get("DeptCode"));
            bDept.put("RepID", briefDept.get("RepID"));
        }catch(Exception e){
            Log.e("BriefDept", "ERROR IN SAVING" + e.toString());
        }
        Log.e("BriefDept", "ERROR IN SAVING" + bDept.toString());
        JSONParser.postStream(baseURL + "/SaveBriefEmp", bDept);
    }

    public static BriefDept GetCollectionPassword(String dept) {
        try {
            JSONObject a = JSONParser.getJSONFromUrl(baseURL + "/CollectionPassword/" + dept);
            BriefDept briefDept = new BriefDept(
                    a.getString("deptCode"),
                    a.getInt("repId"),
                    a.getString("collectionPassword")
            );
            return briefDept;
        }catch (Exception e){
            Log.e("BriefDept", "ERROR IN BRIEFDEPT COLLECTION AHHHHHH" + e.toString());
        }
        return (null);
    }

}
