package adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import entity.Category;
import glamour.mafatlal.com.glamour.R;

/**
 * Created by SAI on 8/11/2017.
 */

public class CategoryImageAdapter extends BaseAdapter {
    private class ViewHolder {
        ImageView img;
    }

    Context mContext;
    ArrayList<Bitmap> arrImage;

    public CategoryImageAdapter(Context mContext, ArrayList<Bitmap> arrImage) {
        this.mContext = mContext;
        this.arrImage = arrImage;
    }

    @Override
    public int getCount() {
        return arrImage.size();
    }

    @Override
    public Object getItem(int position) {
        return arrImage.get(position);
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
            convertView = mInflater.inflate(R.layout.category_image_item, null);
            holder = new ViewHolder();
            holder.img = (ImageView) convertView.findViewById(R.id.img);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Bitmap bmp = arrImage.get(position);
        holder.img.setBackgroundResource(R.drawable.temp_img);
        return convertView;
    }
}
