package com.example.tr.clerk;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BriefDept2 extends HashMap<String, String> {
    static Host h = new Host();
    static String host = h.hosturl;
    static String baseURL;
    static{
        baseURL = String.format("http://%s/DeptAPI", host);
    }
    public BriefDept2(String deptCode, Integer collectionPointId, String collectionPointName){
        put("DeptCode", deptCode);
        put("CollectionPointID", collectionPointId.toString());
        put("CollectionPointName", collectionPointName);
    }
    public BriefDept2(String deptCode, String collectionPointName){
        put("DeptCode", deptCode);
        put("CollectionPointName", collectionPointName);
    }
    public BriefDept2(Integer collectionPointId, String collectionPointName){
        put("CollectionPointID", collectionPointId.toString());
        put("CollectionPointName", collectionPointName);
    }

    public static BriefDept2 GetCurrentCollectionPoint(String dept) {
        try {
            JSONObject a = JSONParser.getJSONFromUrl(baseURL + "/CurrentCollectionPoint/" + dept);
            BriefDept2 briefDept2 = new BriefDept2(
                    a.getString("deptCode"),
                    a.getInt("collectionPointId"),
                    a.getString("collectionPointName")
            );
            return briefDept2;
        }catch (Exception e){
            Log.e("BriefDep2", "ERROR IN BRIEFDEPT2 GETCOLLECTIONPOINT AHHHHHH" + e.toString());
        }
        return (null);
    }

    public static List<BriefDept2> ListCollectionPoints() {
        List<BriefDept2> list = new ArrayList<BriefDept2>();
        JSONArray a = JSONParser.getJSONArrayFromUrl(baseURL + "/AllCollectionPoints");
        try {
            for (int i = 0; i < a.length(); i++) {
                JSONObject b = a.getJSONObject(i);

                list.add(new BriefDept2(b.getInt("collectionPointId"), b.getString("collectionPointName")));
            }
        } catch (Exception e) {
            Log.e("BriefDept2", "NOW IS THE JSONArray error DAMN IT");
        }
        return (list);
    }

    public static void saveBriefDept2(BriefDept2 briefDept2){
        Log.e("BriefDept2", "ERROR IN SAVING" + briefDept2.toString());
        Map<String,String> bDept = new HashMap<>();
        try{
            bDept.put("DeptCode", briefDept2.get("DeptCode"));
            bDept.put("CollectionPointName", briefDept2.get("CollectionPointName"));
        }catch(Exception e){
            Log.e("BriefDept2", "ERROR IN SAVING" + e.toString());
        }
        JSONParser.postStream(baseURL + "/ChangeCollectionPoint", bDept);
    }

}
