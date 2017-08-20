package adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;

import asyncmanager.asyncLoadCommonData;
import entity.ProductImage;
import entity.ProductMaster;
import glamour.mafatlal.com.glamour.R;
import glamour.mafatlal.com.glamour.acProductDetail;
import utility.Helper;

/**
 * Created by SAI on 8/17/2017.
 */

public class ProductAdapter extends BaseAdapter {
    Context mContext;
    ArrayList<ProductMaster> arrProduct;

    private class ViewHolder {
        TextView txtProductName, txtProductPrice;
        ImageView img;
        LinearLayout lyContainer;
        ProgressBar pb;
    }

    public ProductAdapter(Context mContext, ArrayList<ProductMaster> arrProduct) {
        this.mContext = mContext;
        this.arrProduct = arrProduct;
    }

    @Override
    public int getCount() {
        return arrProduct.size();
    }

    @Override
    public Object getItem(int position) {
        return arrProduct.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        LayoutInflater mInflater = (LayoutInflater) mContext
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.product_list_item, null);
            holder = new ViewHolder();
            holder.pb = (ProgressBar) convertView.findViewById(R.id.pb);
            holder.lyContainer = (LinearLayout) convertView.findViewById(R.id.lyContainer);
            holder.txtProductName = (TextView) convertView.findViewById(R.id.txtProductName);
            holder.txtProductPrice = (TextView) convertView.findViewById(R.id.txtProductPrice);
            holder.img = (ImageView) convertView.findViewById(R.id.img);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final ProductMaster objProductMaster = arrProduct.get(position);
        holder.txtProductName.setText(objProductMaster.getProduct_name());
        holder.txtProductPrice.setText(Helper.getCurrencySymbol() + objProductMaster.getPrice());
        holder.lyContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, acProductDetail.class);
                i.putExtra("productId", objProductMaster.getId());
                mContext.startActivity(i);
            }
        });
        try {
            ProductImage objImage = objProductMaster.getProductImage().get(0);
            if (objImage.getBmpThumb() != null) {
                holder.img.setImageBitmap(objImage.getBmpThumb());
            } else {
                new asyncLoadCommonData(mContext).loadProductImage(objImage, holder.pb, holder.img);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }

}
