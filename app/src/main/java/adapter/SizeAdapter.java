package adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import asyncmanager.asyncLoadCommonData;
import entity.Category;
import entity.SizeMaster;
import glamour.mafatlal.com.glamour.R;
import glamour.mafatlal.com.glamour.acSubCategory;

/**
 * Created by SAI on 8/16/2017.
 */

public class SizeAdapter extends BaseAdapter {
    Context mContext;
    ArrayList<SizeMaster> arrSize;

    private class ViewHolder {
        Button btnSize;
    }

    public SizeAdapter(Context mContext, ArrayList<SizeMaster> arrSize) {
        this.mContext = mContext;
        this.arrSize = arrSize;
    }

    @Override
    public int getCount() {
        return arrSize.size();
    }

    @Override
    public Object getItem(int position) {
        return arrSize.get(position);
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
            convertView = mInflater.inflate(R.layout.size_item, null);
            holder = new ViewHolder();
            holder.btnSize = (Button) convertView.findViewById(R.id.btnSize);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final SizeMaster objSizeMaster = arrSize.get(position);
        holder.btnSize.setText(objSizeMaster.getSize());
        if (objSizeMaster.isSelected()) {
            holder.btnSize.setBackgroundResource(R.drawable.ic_filled_grey_circle);
        } else {
            holder.btnSize.setBackgroundResource(R.drawable.ic_unfilled_grey_circle);
        }

        holder.btnSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (objSizeMaster.isSelected())
                    objSizeMaster.setSelected(false);
                else
                    objSizeMaster.setSelected(true);
                notifyDataSetChanged();
            }
        });
        return convertView;
    }
}
