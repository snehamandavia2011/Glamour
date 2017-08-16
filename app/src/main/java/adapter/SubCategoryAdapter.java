package adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import asyncmanager.asyncLoadCommonData;
import entity.Category;
import glamour.mafatlal.com.glamour.R;
import glamour.mafatlal.com.glamour.acProductList;
import glamour.mafatlal.com.glamour.acSubCategory;

/**
 * Created by SAI on 8/11/2017.
 */

public class SubCategoryAdapter extends BaseAdapter {
    private class ViewHolder {
        TextView txtName;
        ImageView img;
        RelativeLayout rlContainer;
    }

    Context mContext;
    ArrayList<Category> arrCategory;

    public SubCategoryAdapter(Context mContext, ArrayList<Category> arrCategory) {
        this.mContext = mContext;
        this.arrCategory = arrCategory;
    }

    @Override
    public int getCount() {
        return arrCategory.size();
    }

    @Override
    public Object getItem(int position) {
        return arrCategory.get(position);
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
            convertView = mInflater.inflate(R.layout.category_list_item, null);
            holder = new ViewHolder();
            holder.rlContainer = (RelativeLayout) convertView.findViewById(R.id.rlContainer);
            holder.txtName = (TextView) convertView.findViewById(R.id.txtName);
            holder.img = (ImageView) convertView.findViewById(R.id.img);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Category objCategory = arrCategory.get(position);
        holder.txtName.setText(objCategory.getCategory_name());
        holder.rlContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, acProductList.class);
                i.putExtra("sub_category_id", objCategory.getId());
                i.putExtra("sub_category_name", objCategory.getCategory_name());
                mContext.startActivity(i);
            }
        });
        if (!objCategory.isImageLoaded()) {
            new asyncLoadCommonData(mContext).loadCategoryImage(holder.img, objCategory);
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
