package com.example.tr.clerk;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BriefEmp extends HashMap<String, String> {

    static Host h = new Host();
    static String host = h.hosturl;
    static String baseURL;

    static {
        baseURL = String.format("http://%s/DeptAPI", host);
    }

    public BriefEmp(Integer userId, String name) {
        put("UserID", userId.toString());
        put("Name", name);
}
    public BriefEmp(String name, Integer userId) {
        put("UserID", userId.toString());
        put("Name", name);
    }

    public BriefEmp(String userId, String name) { //userid taken from intent as string
        put("UserID", userId.toString());
        put("Name", name);
    }

    public static List<BriefEmp> ListEmp(String dept) {
        List<BriefEmp> list = new ArrayList<BriefEmp>();
        JSONArray a = JSONParser.getJSONArrayFromUrl(baseURL + "/" + dept);
        try {
            for (int i = 0; i < a.length(); i++) {
                JSONObject b = a.getJSONObject(i);

                list.add(new BriefEmp(b.getString("userId"), b.getString("name")));
            }
        } catch (Exception e) {
            Log.e("BriefEmp", "NOW IS THE JSONArray error DAMN IT");
        }
        return (list);
    }

    public static BriefEmp GetDeptRep(String dept) {
        try {
            JSONObject a = JSONParser.getJSONFromUrl(baseURL + "/GetRep/" + dept);
            BriefEmp briefEmp = new BriefEmp(
                    a.getInt("userId"),
                    a.getString("name")
            );
            return briefEmp;
        }catch (Exception e){
            Log.e("BriefEmp", "ERROR IN BRIEFEMP GETDEPTREP AHHHHHH" + e.toString());
        }
        return (null);
    }
}


