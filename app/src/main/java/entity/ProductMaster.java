package entity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import utility.ConstantVal;
import utility.Helper;
import utility.Logger;

/**
 * Created by SAI on 8/17/2017.
 */

public class ProductMaster {
    int id, price, order_count;
    String gender, product_id, product_name, product_description, cost, available_stock;
    long dateTime;
    ArrayList<Integer> size_id;
    ArrayList<String> feature_name;
    ArrayList<ProductImage> productImage;

    public ProductMaster(int id, String gender, String product_id, String product_name, String product_description, String cost, int price, String available_stock, long dateTime, ArrayList<Integer> size_id, ArrayList<String> feature_name, ArrayList<ProductImage> productImage, int order_count) {
        this.id = id;
        this.gender = gender;
        this.product_id = product_id;
        this.product_name = product_name;
        this.product_description = product_description;
        this.cost = cost;
        this.price = price;
        this.available_stock = available_stock;
        this.dateTime = dateTime;
        this.size_id = size_id;
        this.feature_name = feature_name;
        this.productImage = productImage;
        this.order_count = order_count;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_description() {
        return product_description;
    }

    public void setProduct_description(String product_description) {
        this.product_description = product_description;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getAvailable_stock() {
        return available_stock;
    }

    public void setAvailable_stock(String available_stock) {
        this.available_stock = available_stock;
    }

    public ArrayList<Integer> getSize_id() {
        return size_id;
    }

    public void setSize_id(ArrayList<Integer> size_id) {
        this.size_id = size_id;
    }

    public ArrayList<String> getFeature_name() {
        return feature_name;
    }

    public void setFeature_name(ArrayList<String> feature_name) {
        this.feature_name = feature_name;
    }

    public ArrayList<ProductImage> getProductImage() {
        return productImage;
    }

    public void setProductImage(ArrayList<ProductImage> productImage) {
        this.productImage = productImage;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    public int getOrder_count() {
        return order_count;
    }

    public void setOrder_count(int order_count) {
        this.order_count = order_count;
    }

    public static ArrayList<ProductMaster> parseJSON(String strJSON) {
        ArrayList<ProductMaster> arrProductMaster = null;
        try {
            JSONArray arrJSON = new JSONArray(strJSON);
            arrProductMaster = new ArrayList<>();
            for (int i = 0; i < arrJSON.length(); i++) {
                JSONObject objJSON = arrJSON.getJSONObject(i);
                int id = objJSON.getString("id").equals("null") ? 0 : objJSON.getInt("id");
                String product_id = objJSON.getString("product_id").equals("null") ? "" : objJSON.getString("product_id");
                String gender = objJSON.getString("gender").equals("null") ? "" : objJSON.getString("gender");
                String product_name = objJSON.getString("product_name").equals("null") ? "" : objJSON.getString("product_name");
                String product_description = objJSON.getString("product_description").equals("null") ? "" : objJSON.getString("product_description");
                String cost = objJSON.getString("cost").equals("null") ? "" : objJSON.getString("cost");
                int price = objJSON.getString("price").equals("null") ? 0 : objJSON.getInt("price");
                String available_stock = objJSON.getString("available_stock").equals("null") ? "" : objJSON.getString("available_stock");
                String date = objJSON.getString("date").equals("null") ? "" : objJSON.getString("date");
                String time = objJSON.getString("time").equals("null") ? "" : objJSON.getString("time");
                int order_count = objJSON.getString("order_count").equals("null") ? 0 : objJSON.getInt("order_count");
                long datetime = Helper.convertStringToDate(date + " " + time, ConstantVal.DATE_FORMAT + " " + ConstantVal.TIME_FORMAT).getTime();
                JSONArray JSONarrSize = objJSON.getJSONArray("size");
                ArrayList<Integer> arrSize = new ArrayList<>();
                for (int intSize = 0; intSize < JSONarrSize.length(); intSize++) {
                    arrSize.add(JSONarrSize.getJSONObject(intSize).getInt("size_id"));
                }
                JSONArray JSONarrFeature = objJSON.getJSONArray("feature");
                ArrayList<String> arrFeature = new ArrayList<>();
                for (int intFeature = 0; intFeature < JSONarrFeature.length(); intFeature++) {
                    arrFeature.add(JSONarrFeature.getJSONObject(intFeature).getString("feature_name"));
                }
                JSONArray JSONarrImage = objJSON.getJSONArray("image");
                ArrayList<ProductImage> arrImage = new ArrayList<>();
                for (int intImage = 0; intImage < JSONarrImage.length(); intImage++) {
                    JSONObject objImage = JSONarrImage.getJSONObject(intImage);
                    arrImage.add(new ProductImage(objImage.getString("image_thumb"), objImage.getString("image")));
                }
                arrProductMaster.add(new ProductMaster(id, gender, product_id, product_name, product_description, cost, price, available_stock, datetime, arrSize, arrFeature, arrImage, order_count));
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.writeToCrashlytics(e);
        }
        return arrProductMaster;
    }
}
