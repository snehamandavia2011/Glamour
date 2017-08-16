package asyncmanager;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import org.json.JSONObject;

import entity.Category;
import entity.User;
import glamour.mafatlal.com.glamour.R;
import utility.ConstantVal;
import utility.DataBase;
import utility.Helper;
import utility.HttpEngine;
import utility.Logger;
import utility.ServerResponse;
import utility.URLMapping;

/**
 * Created by SAI on 8/15/2017.
 */

public class asyncLoadCommonData {
    Context mContext;

    public asyncLoadCommonData(Context mContext) {
        this.mContext = mContext;
    }


    public void loadCategoryImage(final ImageView img, final Category objCategory) {
        new AsyncTask() {
            String photo = "";
            String strServerResponse = "";

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                setPreExecutionPhotoToImageView(mContext, objCategory.getImage(), img, null);
            }

            @Override
            protected Object doInBackground(Object[] params) {
                if (objCategory.getImage() == null || objCategory.getImage().length() <= 0) {
                    String id = String.valueOf(objCategory.getId());
                    final HttpEngine objHttpEngine = new HttpEngine();
                    final String tokenId = Helper.getStringPreference(mContext, User.Fields.TOKEN, "");
                    final URLMapping um = ConstantVal.loadPhoto();
                    ServerResponse sr = objHttpEngine.getDataFromWebAPI(mContext, um.getUrl(), um.getParamNames(), new String[]{id, String.valueOf(0), tokenId});
                    strServerResponse = sr.getResponseCode();
                    photo = parsePhoto(sr.getResponseString());
                    //update item_master table
                    if (strServerResponse.equals(ConstantVal.ServerResponseCode.SUCCESS)) {
                        DataBase db = new DataBase(mContext);
                        db.open();
                        ContentValues cv = new ContentValues();
                        cv.put("image", photo);
                        db.update(DataBase.category_master, DataBase.category_master_int, "id='" + id + "'", cv);
                        db.close();
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (objCategory.getImage() == null || objCategory.getImage().length() <= 0) {
                    objCategory.setImageLoaded(setPostExecutionPhotoToImageView(photo, img, strServerResponse, null));
                }
            }
        }.execute();
    }

    public static void setPreExecutionPhotoToImageView(Context ctx, String strBase64, ImageView img, View.OnClickListener imgClick) {
        RelativeLayout.LayoutParams rparam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        rparam.addRule(RelativeLayout.CENTER_IN_PARENT);
        img.setLayoutParams(rparam);
        if (strBase64 != null && strBase64.length() > 0) {
            strBase64 = strBase64.substring(strBase64.indexOf(",") + 1);
            Bitmap bmp = Helper.convertBase64ImageToBitmap(strBase64);
            if (bmp == null) {
                img.setImageResource(R.drawable.ic_nopic);
                img.setBackgroundResource(0);
            } else {
                img.setImageResource(0);
                img.setBackgroundDrawable(new BitmapDrawable(ctx.getResources(), bmp));
                try {
                    img.setScaleType(ImageView.ScaleType.FIT_START);
                } catch (Exception e) {
                    Logger.writeToCrashlytics(e);
                }
                if (imgClick != null) {
                    img.setTag(bmp);
                    img.setOnClickListener(imgClick);
                }
            }
        } else {
            img.setBackgroundResource(0);
            img.setImageResource(R.drawable.ic_awaiting);
        }
    }

    private boolean setPostExecutionPhotoToImageView(String strBase64, ImageView img, String serverResponse, View.OnClickListener imgClick) {
        RelativeLayout.LayoutParams rparam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        rparam.addRule(RelativeLayout.CENTER_IN_PARENT);
        img.setLayoutParams(rparam);
        try {
            if (strBase64.length() > 0) {
                strBase64 = strBase64.substring(strBase64.indexOf(",") + 1);
            }
            try {
                img.setScaleType(ImageView.ScaleType.FIT_START);
            } catch (Exception e) {
                e.printStackTrace();
                Logger.writeToCrashlytics(e);
            }
            Bitmap bmp = Helper.convertBase64ImageToBitmap(strBase64);
            if (strBase64.equals("") || bmp == null) {
                if (serverResponse.equals(ConstantVal.ServerResponseCode.SUCCESS)) {
                    img.setBackgroundResource(0);
                    img.setImageResource(R.drawable.ic_nopic);
                }
            } else {
                img.setImageResource(0);
                img.setBackgroundDrawable(new BitmapDrawable(mContext.getResources(), bmp));
                if (imgClick != null) {
                    img.setTag(bmp);
                    img.setOnClickListener(imgClick);
                }
            }
            return true;
        } catch (Exception e) {
            img.setBackgroundResource(0);
            img.setImageResource(R.drawable.ic_nopic);
            Logger.writeToCrashlytics(e);
            return false;
        }
    }


    public String parsePhoto(String JSONString) {
        String photo = "";
        try {
            JSONObject obj = new JSONObject(JSONString);
            photo = obj.getString("photo").equals("null") ? "" : obj.getString("photo");
        } catch (Exception e) {
            e.printStackTrace();
            Logger.writeToCrashlytics(e);
        }
        return photo;
    }
}
