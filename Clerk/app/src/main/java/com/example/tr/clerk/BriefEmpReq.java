package com.example.tr.clerk;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BriefEmpReq extends HashMap<String, String> {

    static Host h = new Host();
    static String host = h.hosturl;
    static String baseURL;

    static {
        baseURL = String.format("http://%s/DeptAPI", host);
    }

    public BriefEmpReq(String rrid, LocalDateTime requestDate, Integer userid, String name, String status) {
        put("RRID", rrid);
        put("RequestDate", requestDate.toString());
        put("UserID", userid.toString());
        put("Name", name);
        put("Status", status);
    }
    public BriefEmpReq(String rrid, String requestDate, String userid, String name, String status) {
        put("RRID", rrid);
        put("RequestDate", requestDate);
        put("UserID", userid);
        put("Name", name);
        put("Status", status);
    }

    public BriefEmpReq( String rrid, String name, String requestDate) {
        put("RRID", rrid);
        put("Name", name);
        put("RequestDate", requestDate);
    }
    public BriefEmpReq(String rrid) {
        put("RRID", rrid);
    }

    public BriefEmpReq( String rrid,String remark) {
        put("RRID", rrid);
        put("Remark", remark);
    }

    public static List<BriefEmpReq> ListEmpReq(String dept) {

        List<BriefEmpReq> list = new ArrayList<BriefEmpReq>();
        JSONArray a = JSONParser.getJSONArrayFromUrl(baseURL + "/OutstandingOrders/" + dept);
        try {
            for (int i = 0; i < a.length(); i++) {
                JSONObject b = a.getJSONObject(i);
                String bName = b.getString("name");
                String bDate = b.getString("requestDate");
                String newBDate = bDate.replace("T00:00:00", "");
                list.add(new BriefEmpReq(b.getString("rrid"), bName,newBDate));
            }
        } catch (Exception e) {
            Log.e("BriefEmpReq", "NOW IS THE JSONArray error DAMN IT");
        }
        return (list);
    }

    public static void approveRequest(BriefEmpReq briefEmpReq){
        Map<String,String> bEmpReq = new HashMap<>();
        try{
            bEmpReq.put("RRID", briefEmpReq.get("RRID"));
            bEmpReq.put("Status", "Approved");
        }catch(Exception e){
            Log.e("BriefEmpReq", "ERROR IN SAVING" + e.toString());
        }
        JSONParser.postStream(baseURL + "/ApproveRequest", bEmpReq);
    }

    public static void rejectRequest(BriefEmpReq briefEmpReq){
        Map<String,String> bEmpReq = new HashMap<>();
        try{
            bEmpReq.put("RRID", briefEmpReq.get("RRID"));
            bEmpReq.put("Status", "Rejected");
            bEmpReq.put("Remark", briefEmpReq.get("Remark"));
        }catch(Exception e){
            Log.e("BriefEmpReq", "ERROR IN SAVING" + e.toString());
        }
        JSONParser.postStream(baseURL + "/RejectRequest", bEmpReq);
    }



}
