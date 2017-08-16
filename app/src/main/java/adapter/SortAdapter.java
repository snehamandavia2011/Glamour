package adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import asyncmanager.asyncLoadCommonData;
import entity.Category;
import glamour.mafatlal.com.glamour.R;
import glamour.mafatlal.com.glamour.acSubCategory;

/**
 * Created by SAI on 8/16/2017.
 */

public class SortAdapter extends BaseAdapter {
    Context ctx;
    String[] arrSort;
    int currentSelection;

    public SortAdapter(Context ctx, String[] arrSort, int currentSelection) {
        this.ctx = ctx;
        this.arrSort = arrSort;
        this.currentSelection = currentSelection;
    }

    @Override
    public int getCount() {
        return arrSort.length;
    }

    @Override
    public Object getItem(int position) {
        return arrSort[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView txt = new TextView(ctx);
        if (position == currentSelection) {
            txt.setTextAppearance(ctx, R.style.styTitleRedSingleLine);
        } else {
            txt.setTextAppearance(ctx, R.style.stySpinnerStyle);
        }
        txt.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
        txt.setPadding((int) ctx.getResources().getDimension(R.dimen.paddingBig), (int) ctx.getResources().getDimension(R.dimen.paddingBig), (int) ctx.getResources().getDimension(R.dimen.paddingBig), (int) ctx.getResources().getDimension(R.dimen.paddingBig));
        txt.setText(arrSort[position]);
        return txt;
    }
}
