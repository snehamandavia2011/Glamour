package adapter;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import entity.BasketItem;
import entity.User;
import glamour.mafatlal.com.glamour.R;
import utility.ConfimationSnackbar;
import utility.ConstantVal;
import utility.DataBase;
import utility.Helper;
import utility.HttpEngine;
import utility.ServerResponse;
import utility.TabManager;
import utility.URLMapping;

/**
 * Created by SAI on 8/21/2017.
 */

public class BasketItemAdapter extends BaseAdapter {
    Context mContext;
    ArrayList<BasketItem> arrBasketItem;
    RelativeLayout lyNoContent, lyMainContent;
    boolean isDeleteNeddtoShow;
    TabManager objTabManager;

    public BasketItemAdapter(TabManager objTabManager, Context mContext, ArrayList<BasketItem> arrBasketItem, RelativeLayout lyNoContent, RelativeLayout lyMainContent, boolean isDeleteNeddtoShow) {
        this.mContext = mContext;
        this.arrBasketItem = arrBasketItem;
        this.lyNoContent = lyNoContent;
        this.lyMainContent = lyMainContent;
        this.isDeleteNeddtoShow = isDeleteNeddtoShow;
        this.objTabManager = objTabManager;
    }

    @Override
    public int getCount() {
        return arrBasketItem.size();
    }

    @Override
    public Object getItem(int position) {
        return arrBasketItem.get(position);
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
            convertView = mInflater.inflate(R.layout.basket_list_item, null);
            holder = new ViewHolder();
            holder.imgProduct = (ImageView) convertView.findViewById(R.id.imgProduct);
            holder.btnDelete = (ImageButton) convertView.findViewById(R.id.btnDelete);
            holder.txtProductQty = (TextView) convertView.findViewById(R.id.txtProductQty);
            holder.txtProductPrice = (TextView) convertView.findViewById(R.id.txtProductPrice);
            holder.txtProductSize = (TextView) convertView.findViewById(R.id.txtProductSize);
            holder.txtProductDesc = (TextView) convertView.findViewById(R.id.txtProductDesc);
            holder.txtProductName = (TextView) convertView.findViewById(R.id.txtProductName);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final BasketItem objBasketItem = arrBasketItem.get(position);
        holder.txtProductQty.setText(mContext.getString(R.string.strQty) + ": " + objBasketItem.getQuantity());
        try {
            holder.txtProductPrice.setText(Helper.getCurrencySymbol() + (Integer.parseInt(objBasketItem.getPrice()) * Integer.parseInt(objBasketItem.getQuantity())));
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.txtProductSize.setText(mContext.getString(R.string.strSize) + ": " + objBasketItem.getSize());
        holder.txtProductName.setText(objBasketItem.getName());
        if (objBasketItem.getDesc().equals("")) {
            holder.txtProductDesc.setText(mContext.getString(R.string.msgDescNotAvail));
        } else {
            holder.txtProductDesc.setText(objBasketItem.getDesc());
        }
        if (objBasketItem.getImage().equals("")) {
            holder.imgProduct.setImageResource(R.drawable.ic_nopic);
        } else {
            holder.imgProduct.setImageBitmap(Helper.convertBase64ImageToBitmap(objBasketItem.getImage()));
        }
        if (isDeleteNeddtoShow) {
            holder.btnDelete.setVisibility(View.VISIBLE);
        } else {
            holder.btnDelete.setVisibility(View.GONE);
        }
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ConfimationSnackbar snackbar = new ConfimationSnackbar((AppCompatActivity) mContext, ConstantVal.ToastBGColor.WARNING);
                snackbar.showSnackBar(mContext.getString(R.string.msgItemRemove), mContext.getString(R.string.strYes), mContext.getString(R.string.strNo), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AsyncTask() {
                            @Override
                            protected void onPreExecute() {
                                super.onPreExecute();
                                snackbar.dismissSnackBar();
                                arrBasketItem.remove(position);
                                notifyDataSetChanged();
                            }

                            @Override
                            protected Object doInBackground(Object[] params) {
                                DataBase db = new DataBase(mContext);
                                db.open();
                                db.delete(DataBase.basket_items, DataBase.basket_items_int, "basket_id=" + objBasketItem.getBasket_id() + " and product_id=" + objBasketItem.getProduct_id() + " and sizeId=" + objBasketItem.getSizeId());
                                db.close();
                                return null;
                            }

                            @Override
                            protected void onPostExecute(Object o) {
                                super.onPostExecute(o);
                                objTabManager.updateBasketItemCounter();
                                if (arrBasketItem.size() <= 0) {
                                    lyNoContent.setVisibility(View.VISIBLE);
                                    lyMainContent.setVisibility(View.GONE);
                                } else {
                                    lyMainContent.setVisibility(View.VISIBLE);
                                    lyNoContent.setVisibility(View.GONE);
                                }
                            }
                        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }
                }, null);
            }
        });
        return convertView;
    }

    private class ViewHolder {
        TextView txtProductQty, txtProductPrice, txtProductSize, txtProductDesc, txtProductName;
        ImageView imgProduct;
        ImageButton btnDelete;
    }
}