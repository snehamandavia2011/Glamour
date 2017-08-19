package entity;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import utility.Logger;

/**
 * Created by SAI on 8/16/2017.
 */

public class SizeMaster {
    int id;
    String size;
    boolean isSelected;

    public SizeMaster(int id, String size, boolean isSelected) {
        this.id = id;
        this.size = size;
        this.isSelected = isSelected;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public static ArrayList<SizeMaster> parseArray(String strJSON) {
        try {
            JSONArray arrJSON = new JSONArray(strJSON);
            ArrayList<SizeMaster> arrColor = new ArrayList<>();
            for (int i = 0; i < arrJSON.length(); i++) {
                JSONObject objJSON = arrJSON.getJSONObject(i);
                SizeMaster objColorMaster = new SizeMaster(objJSON.getInt("id"), objJSON.getString("size"), false);
                arrColor.add(objColorMaster);
            }
            return arrColor;
        } catch (Exception e) {
            e.printStackTrace();
            Logger.writeToCrashlytics(e);
            return null;
        }
    }
}
