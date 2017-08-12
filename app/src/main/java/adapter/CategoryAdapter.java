package adapter;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import entity.Category;
import glamour.mafatlal.com.glamour.R;
import glamour.mafatlal.com.glamour.acHome;
import utility.Logger;

/**
 * Created by SAI on 8/11/2017.
 */

public class CategoryAdapter extends BaseAdapter {
    private class ViewHolder {
        TextView txtName;
        GridView gvItems;
        RelativeLayout rlContainer;
    }

    Context mContext;
    ArrayList<Category> arrCategory;

    public CategoryAdapter(Context mContext, ArrayList<Category> arrCategory) {
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
            holder.gvItems = (GridView) convertView.findViewById(R.id.gvItems);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Category objCategory = arrCategory.get(position);
        holder.txtName.setText(objCategory.getCategoryName());
        ArrayList<Bitmap> arr = new ArrayList<>();
        arr.add(null);
        arr.add(null);
        arr.add(null);
        holder.gvItems.setAdapter(new CategoryImageAdapter(mContext, arr));
        holder.rlContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return convertView;
    }
}
