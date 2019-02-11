package com.example.tr.clerk;

import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.HashMap;
import java.util.List;

public class User extends HashMap<String, String> {
    static Host h = new Host();
    static String host = h.hosturl;
    static String baseURL;
    static {
        baseURL = String.format("http://%s/api/user", host);
    }
    public User(String Name,String UserID,String Department,String DepartmentCode,String Email, String role) {
        put("Name", Name);
        put("UserID", UserID);
        put("Department", Department);
        put("DepartmentCode",DepartmentCode);
        put("Email", Email);
        put("role", role);
    }
    public static boolean TryLogin(String username, String password, SharedPreferences sp){
            String result= JSONParser.getStream(String.format("%s/%s/%s",baseURL, username,password));
             result = result.replaceFirst("\n","");
        if (result.equals("")){
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("Status", "Loggged Out");
                editor.apply();
                return false;
            }
            else
            {
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("Status", "Loggged In");
                editor.putInt("workid", Integer.parseInt(result));
                editor.apply();
                return true;
            }
//        Account account = new Account(username,password);
//        JSONObject jemp = new JSONObject();
//        try {
//            //jemp.put("Category", Category.ReadCategory(emp.get("CategoryID")));
//            jemp.put("", account.username);
//            jemp.put("", account.password);
//        } catch (Exception e) {
//            Log.e("login", "JSONArray error");
//        }
//            String result = JSONParser.postStream(baseURL,jemp.toString());
//            if (result.equals("no")||result.equals("")){
//                SharedPreferences.Editor editor = sp.edit();
//                editor.putString("Status", "Loggged Out");
//                editor.apply();
//                return false;
//            }
//            else
//            {
//                SharedPreferences.Editor editor = sp.edit();
//                editor.putString("Status", "Loggged In");
//                editor.putString("workid", result);
//                editor.commit();
//                return true;
//            }
    }
    public static User ReadUser(int id) {
        try {
            JSONArray a = JSONParser.getJSONArrayFromUrl(String.format("%s/%s",baseURL, id));
            User e = new User(a.getString(0),a.getString(1),a.getString(2),a.getString(3),a.getString(4),a.getString(5));
            return e;
        } catch (Exception e) {
            Log.e("User", "JSONArray error");
        }
        return(null);
    }
    public static boolean CheckCollectPassword(String passowrd,String rrid){
        String result = JSONParser.getStream(String.format("%s/checkpass/%s/%s",baseURL,rrid,passowrd));
        Log.e("User", "JSONArray error"+result+"123");
        result = result.replace("\n","");
        if (result.equals("true") )
            return true;
        else
            return false;
    }


}
class Account{
    String username;
    String password;
    public Account(String username,String password){
        this.username = username;
        this.password = password;
    }
}
