package adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import glamour.mafatlal.com.glamour.R;

/**
 * Created by SAI on 8/16/2017.
 */

public class RefineCriteriaAdapter extends BaseAdapter {
    Context ctx;
    String[] arrRefine;
    int currentSelectedRefineCriteriaPosition;

    public RefineCriteriaAdapter(Context ctx, String[] arrRefine, int currentSelectedRefineCriteriaPosition) {
        this.ctx = ctx;
        this.arrRefine = arrRefine;
        this.currentSelectedRefineCriteriaPosition = currentSelectedRefineCriteriaPosition;
    }

    @Override
    public int getCount() {
        return arrRefine.length;
    }

    @Override
    public Object getItem(int position) {
        return arrRefine[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView txt = new TextView(ctx);
        if (position == currentSelectedRefineCriteriaPosition) {
            txt.setBackgroundDrawable(new ColorDrawable(ctx.getResources()
                    .getColor(android.R.color.darker_gray)));
            txt.setTextAppearance(ctx, R.style.styDescBlackSingleLineBold);
        } else {
            txt.setBackgroundDrawable(new ColorDrawable(ctx.getResources()
                    .getColor(R.color.white)));
            txt.setTextAppearance(ctx, R.style.styDescBlackSingleLine);
        }
        txt.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
        txt.setPadding((int) ctx.getResources().getDimension(R.dimen.paddingBig), (int) ctx.getResources().getDimension(R.dimen.paddingBig), (int) ctx.getResources().getDimension(R.dimen.paddingBig), (int) ctx.getResources().getDimension(R.dimen.paddingBig));
        txt.setText(arrRefine[position]);
        return txt;
    }
}
