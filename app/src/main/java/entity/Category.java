package entity;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import utility.ConstantVal;
import utility.DataBase;
import utility.Helper;
import utility.HttpEngine;
import utility.Logger;
import utility.ServerResponse;
import utility.URLMapping;

/**
 * Created by SAI on 8/11/2017.
 */

public class Category {
    int id, parent_id;
    String category_name, category_description, category_for, image;
    boolean isImageLoaded;

    public Category(int id, int parent_id, String category_name, String category_description, String category_for, String image) {
        this.id = id;
        this.parent_id = parent_id;
        this.category_name = category_name;
        this.category_description = category_description;
        this.category_for = category_for;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParent_id() {
        return parent_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getCategory_description() {
        return category_description;
    }

    public void setCategory_description(String category_description) {
        this.category_description = category_description;
    }

    public String getCategory_for() {
        return category_for;
    }

    public void setCategory_for(String category_for) {
        this.category_for = category_for;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isImageLoaded() {
        return isImageLoaded;
    }

    public void setImageLoaded(boolean imageLoaded) {
        isImageLoaded = imageLoaded;
    }

    public static ArrayList<Category> parseData(String strJSON) {
        try {
            ArrayList<Category> arrCategory = new ArrayList<>();
            JSONArray arrJSON = new JSONArray(strJSON);
            for (int i = 0; i < arrJSON.length(); i++) {
                JSONObject objJSON = arrJSON.getJSONObject(i);
                Category objCategory = parseObject(objJSON);
                arrCategory.add(objCategory);
            }
            return arrCategory;
        } catch (Exception e) {
            e.printStackTrace();
            Logger.writeToCrashlytics(e);
            return null;
        }
    }

    private static void saveDataToLocalDatabase(ArrayList<Category> arrCategory, Context mContext) {
        DataBase db = new DataBase(mContext);
        db.open();
        db.cleanTable(DataBase.category_master_int);
        for (int i = 0; i < arrCategory.size(); i++) {
            Category objCategory = arrCategory.get(i);
            db.insert(DataBase.category_master, DataBase.category_master_int, new String[]{String.valueOf(objCategory.getId()),
                    String.valueOf(objCategory.getParent_id()), objCategory.getCategory_name(), objCategory.getCategory_description(),
                    objCategory.getCategory_for(), objCategory.getImage()});
        }
        db.close();
    }

    private static Category parseObject(JSONObject objJSON) {
        try {
            int id = objJSON.getString("id").equals("null") ? 0 : objJSON.getInt("id");
            int parent_id = objJSON.getString("parent_id").equals("null") ? 0 : objJSON.getInt("parent_id");
            String category_name = objJSON.getString("category_name").equals("null") ? "" : objJSON.getString("category_name");
            String category_description = objJSON.getString("category_description").equals("null") ? "" : objJSON.getString("category_description");
            String category_for = objJSON.getString("category_for").equals("null") ? "" : objJSON.getString("category_for");
            String image_url = "";//objJSON.getString("image").equals("null") ? "" : objJSON.getString("image");
            return new Category(id, parent_id, category_name, category_description, category_for, image_url);
        } catch (Exception e) {
            e.printStackTrace();
            Logger.writeToCrashlytics(e);
            return null;
        }
    }

    public static Thread loadCategoryFromServer(final Context mContext) {
        Thread t = new Thread() {
            public void run() {
                try {
                    ServerResponse sr;
                    String token = Helper.getStringPreference(mContext, User.Fields.TOKEN, "");
                    URLMapping objURLMapping = ConstantVal.getProductCategory();
                    HttpEngine objHttpEngine = new HttpEngine();
                    sr = objHttpEngine.getDataFromWebAPI(mContext, objURLMapping.getUrl(), objURLMapping.getParamNames(), new String[]{token});
                    if (sr.getResponseCode().equals(ConstantVal.ServerResponseCode.SUCCESS)) {
                        ArrayList<Category> arrCategory = parseData(sr.getResponseString());
                        if (arrCategory != null)
                            saveDataToLocalDatabase(arrCategory, mContext);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
        return t;
    }

    public static ArrayList<Category> getCategoryFromDatabase(int parentCategoryId, Context mContext) {
        DataBase db = new DataBase(mContext);
        db.open();
        ArrayList<Category> arrCategory = null;
        Cursor cur = db.fetch(DataBase.category_master, DataBase.category_master_int, "parent_id=" + parentCategoryId);
        if (cur != null && cur.getCount() > 0) {
            arrCategory = new ArrayList<>();
            cur.moveToFirst();
            do {
                arrCategory.add(new Category(cur.getInt(1), cur.getInt(2), cur.getString(3), cur.getString(4), cur.getString(5), cur.getString(6)));
            } while (cur.moveToNext());
        }
        cur.close();
        db.close();
        return arrCategory;
    }
}
