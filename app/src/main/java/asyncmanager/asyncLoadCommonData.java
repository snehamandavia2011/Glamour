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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;

import entity.Category;
import entity.ProductImage;
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

    public void setPreExecutionPhotoToImageView(Context ctx, String strBase64, ImageView img, View.OnClickListener imgClick) {
        RelativeLayout.LayoutParams rparam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (int) mContext.getResources().getDimension(R.dimen.heightOfCategoryImage));
        rparam.addRule(RelativeLayout.CENTER_IN_PARENT);
        img.setLayoutParams(rparam);
        if (strBase64 != null && strBase64.length() > 0) {
            strBase64 = strBase64.substring(strBase64.indexOf(",") + 1);
            Bitmap bmp = Helper.convertBase64ImageToBitmap(strBase64);
            if (bmp == null) {
                img.setImageResource(R.drawable.ic_nopic);
                img.setBackgroundResource(0);
            } else {
                img.setImageBitmap(bmp);
                img.setBackgroundResource(R.color.transparent);
                try {
                    img.setScaleType(ImageView.ScaleType.FIT_CENTER);
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
        RelativeLayout.LayoutParams rparam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (int) mContext.getResources().getDimension(R.dimen.heightOfCategoryImage));
        rparam.addRule(RelativeLayout.CENTER_IN_PARENT);
        img.setLayoutParams(rparam);
        try {
            if (strBase64.length() > 0) {
                strBase64 = strBase64.substring(strBase64.indexOf(",") + 1);
            }
            try {
                img.setScaleType(ImageView.ScaleType.FIT_CENTER);
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
                img.setImageBitmap(bmp);
                img.setBackgroundResource(R.color.transparent);
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

    public void loadProductListImage(final ProductImage objProductImage, final ProgressBar pb, final ImageView img) {
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                pb.setVisibility(View.GONE);
                img.setVisibility(View.VISIBLE);
                if (bitmap != null) {
                    img.setImageBitmap(bitmap);
                    objProductImage.setBmpThumb(bitmap);
                } else {
                    img.setImageResource(R.drawable.ic_nopic);
                }
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                pb.setVisibility(View.VISIBLE);
                img.setVisibility(View.GONE);
            }
        };

        img.setTag(target);
        Picasso.with(mContext)
                .load(objProductImage.getImage())
                .resize(200, 200)
                .centerInside()
                .into(target);

    }

    public void loadProductDetailImage(final ProductImage objProductImage, final ProgressBar pb, final ImageView img) {
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                pb.setVisibility(View.GONE);
                img.setVisibility(View.VISIBLE);
                if (bitmap != null) {
                    img.setImageBitmap(bitmap);
                } else {
                    img.setImageResource(R.drawable.ic_nopic);
                }
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                pb.setVisibility(View.VISIBLE);
                img.setVisibility(View.GONE);
            }
        };

        img.setTag(target);
        Picasso.with(mContext)
                .load(objProductImage.getImage())
                .into(target);

    }

    /*public void loadProductImage(final ProductImage objProductImage, final ProgressBar pb, final ImageView img) {
        new AsyncTask() {
            Bitmap mIcon11 = null;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pb.setVisibility(View.VISIBLE);
                img.setVisibility(View.GONE);
            }

            @Override
            protected Object doInBackground(Object[] params) {
                final HttpEngine objHttpEngine = new HttpEngine();
                final String tokenId = Helper.getStringPreference(mContext, User.Fields.TOKEN, "");
                final URLMapping um = ConstantVal.getBase64FromURL();
                ServerResponse sr = objHttpEngine.getDataFromWebAPI(mContext, um.getUrl(), um.getParamNames(), new String[]{objProductImage.getImage(), tokenId});
                if (sr.getResponseCode().equals(ConstantVal.ServerResponseCode.SUCCESS)) {
                    mIcon11 = Helper.convertBase64ImageToBitmap(parsePhoto(sr.getResponseString()));
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                pb.setVisibility(View.GONE);
                img.setVisibility(View.VISIBLE);
                if (mIcon11 != null) {
                    img.setImageBitmap(mIcon11);
                    objProductImage.setBmpThumb(mIcon11);
                } else {
                    img.setImageResource(R.drawable.ic_nopic);
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }*/

    /*public void loadProductImage(final ProductImage objProductImage, final ProgressBar pb, final ImageView img) {
        new AsyncTask() {
            Bitmap mIcon11 = null;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pb.setVisibility(View.VISIBLE);
                img.setVisibility(View.GONE);
            }

            @Override
            protected Object doInBackground(Object[] params) {
                String urldisplay = objProductImage.getImage_thumb();
                try {
                    InputStream in = new java.net.URL(urldisplay).openStream();
                    mIcon11 = BitmapFactory.decodeStream(in);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                pb.setVisibility(View.GONE);
                img.setVisibility(View.VISIBLE);
                if (mIcon11 != null) {
                    img.setImageBitmap(mIcon11);
                    objProductImage.setBmpThumb(mIcon11);
                } else {
                    img.setImageResource(R.drawable.ic_nopic);
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }*/
}
