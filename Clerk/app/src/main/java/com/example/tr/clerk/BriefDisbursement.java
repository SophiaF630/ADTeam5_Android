package com.example.tr.clerk;

import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

public class BriefDisbursement extends HashMap<String, String> {

    static Host h = new Host();
    static String host = h.hosturl;
    static String baseURL;
    static{
        baseURL = String.format("http://%s/DeptAPI", host);
    }
    public BriefDisbursement(String collectionDate, String collectionPointName, String collectionTime){
        put("CollectionDate", collectionDate);
        put("CollectionPointName", collectionPointName);
        put("CollectionTime", collectionTime);
    }

    public static BriefDisbursement GetDisbursement(String dept) {
        try {
            JSONObject a = JSONParser.getJSONFromUrl(baseURL + "/GetDisbursement/" + dept);
            BriefDisbursement briefD = new BriefDisbursement(

                    a.getString("collectionDate").replace("T00:00:00", ""),
                    a.getString("collectionPointName"),
                    a.getString("collectionTime").replace("1900-01-01T", "")
            );
            return briefD;
        }catch (Exception e){
            Log.e("BriefDisbursement", "ERROR IN BRIEFDISBURSEMENT AHHHHHH" + e.toString());
        }
        return (null);
    }
}
