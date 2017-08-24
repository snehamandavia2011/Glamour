package adapter;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

import entity.BasketItem;
import entity.Order;
import glamour.mafatlal.com.glamour.R;
import glamour.mafatlal.com.glamour.acBasket;
import utility.ConstantVal;
import utility.DataBase;
import utility.DateTimeUtils;
import utility.Helper;
import utility.ServerResponse;

/**
 * Created by SAI on 8/23/2017.
 */

public class OrderAdapter extends BaseAdapter {
    Context mContext;
    ArrayList<Order> arrOrder;

    private class ViewHolder {
        utility.MyListView lvlItems;
        LinearLayout lyContainer, lyPlacedOn;
        ImageButton btnResend;
        TextView txtCreatedOn, txtPlacedOn, txtStatus;
    }

    public OrderAdapter(Context mContext, ArrayList<Order> arrOrder) {
        this.mContext = mContext;
        this.arrOrder = arrOrder;
    }

    @Override
    public int getCount() {
        return arrOrder.size();
    }

    @Override
    public Object getItem(int position) {
        return arrOrder.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        LayoutInflater mInflater = (LayoutInflater) mContext
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.order_list_item, null);
            holder = new ViewHolder();
            holder.lyPlacedOn = (LinearLayout) convertView.findViewById(R.id.lyPlacedOn);
            holder.lyContainer = (LinearLayout) convertView.findViewById(R.id.lyContainer);
            holder.lvlItems = (utility.MyListView) convertView.findViewById(R.id.lvlItems);
            holder.txtCreatedOn = (TextView) convertView.findViewById(R.id.txtCreatedOn);
            holder.txtPlacedOn = (TextView) convertView.findViewById(R.id.txtPlacedOn);
            holder.txtStatus = (TextView) convertView.findViewById(R.id.txtStatus);
            holder.btnResend = (ImageButton) convertView.findViewById(R.id.btnResend);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        new AsyncTask() {
            ArrayList<BasketItem> arrBasketItem;
            Order objOrder;
            ServerResponse sr;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                objOrder = arrOrder.get(position);
                String strCreatedOn = DateTimeUtils.convertDateToString(new Date(Long.parseLong(objOrder.getCreatedOn())), (ConstantVal.DATE_TIME_FORMAT));
                holder.txtCreatedOn.setText(strCreatedOn);
                if (objOrder.getIs_order_place_successfully().equals("N")) {
                    holder.txtStatus.setText(mContext.getString(R.string.strNotSent).toUpperCase());
                    holder.txtStatus.setTextAppearance(mContext, R.style.styDescRedSingleLine);
                    holder.btnResend.setVisibility(View.VISIBLE);
                    holder.lyPlacedOn.setVisibility(View.GONE);
                } else {
                    holder.lyPlacedOn.setVisibility(View.VISIBLE);
                    String strPlacedOn = DateTimeUtils.convertDateToString(new Date(Long.parseLong(objOrder.getPlacedOn())), (ConstantVal.DATE_TIME_FORMAT));
                    holder.txtPlacedOn.setText(strPlacedOn);
                    holder.txtStatus.setText(mContext.getString(R.string.strSent).toUpperCase());
                    holder.txtStatus.setTextAppearance(mContext, R.style.styDescGreenSingleLine);
                    holder.btnResend.setVisibility(View.GONE);
                }
            }

            @Override
            protected Object doInBackground(Object[] params) {
                arrBasketItem = getBasketItem(Integer.parseInt(objOrder.getBasket_id()));
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                showLineItem(arrBasketItem, holder.lvlItems);
                if (holder.btnResend.getVisibility() == View.VISIBLE) {
                    holder.btnResend.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new AsyncTask() {
                                @Override
                                protected Object doInBackground(Object[] params) {
                                    sr = acBasket.sendOrder(mContext, arrBasketItem, Long.parseLong(objOrder.getBasket_id()), objOrder);
                                    return null;
                                }

                                @Override
                                protected void onPostExecute(Object o) {
                                    super.onPostExecute(o);
                                    if (sr != null && sr.getResponseCode().equals(ConstantVal.ServerResponseCode.SUCCESS)) {
                                        notifyDataSetChanged();
                                    } else {
                                        Helper.displaySnackbar((AppCompatActivity) mContext, ConstantVal.ServerResponseCode.getMessage(mContext, sr.getResponseCode()), ConstantVal.ToastBGColor.INFO);
                                    }
                                }
                            }.executeOnExecutor(THREAD_POOL_EXECUTOR);
                        }
                    });
                } else {
                    holder.btnResend.setOnClickListener(null);
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        return convertView;
    }

    private void showLineItem(ArrayList<BasketItem> arrBasketItem, final ListView lvl) {
        if (arrBasketItem != null && arrBasketItem.size() > 0) {
            lvl.setAdapter(new BasketItemAdapter(null, mContext, arrBasketItem, null, null, false));
        }
    }

    private ArrayList<BasketItem> getBasketItem(int basket_id) {
        ArrayList<BasketItem> arrBasketItem = null;
        DataBase db = new DataBase(mContext);
        db.open();
        Cursor curItem = db.fetch(DataBase.basket_items, "basket_id=" + basket_id);
        try {
            if (curItem != null && curItem.getCount() > 0) {
                arrBasketItem = new ArrayList<BasketItem>();
                curItem.moveToFirst();
                do {
                    arrBasketItem.add(new BasketItem(curItem.getString(1), curItem.getString(2), curItem.getString(3), curItem.getString(4), curItem.getString(5), curItem.getString(6), curItem.getString(7), curItem.getString(8), curItem.getString(9)));
                } while (curItem.moveToNext());
                return arrBasketItem;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            curItem.close();
            db.close();
        }
        return arrBasketItem;
    }
}
