package glamour.mafatlal.com.glamour;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import adapter.OrderAdapter;
import entity.Order;
import entity.User;
import utility.DataBase;
import utility.Helper;
import utility.TabManager;

public class acOrder extends AppCompatActivity {
    Helper objHelper = new Helper();
    Context mContext;
    AppCompatActivity ac;
    RelativeLayout lyNoContent, lyMainContent;
    ListView lvlOrder;
    ArrayList<Order> arrOrder;
    OrderAdapter adpOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        ac = this;
        Helper.startFabric(mContext);
        setContentView(R.layout.ac_order);
        objHelper.setActionBar(this, getString(R.string.strOrder), false);
        lyMainContent = (RelativeLayout) findViewById(R.id.lyMainContent);
        lyNoContent = (RelativeLayout) findViewById(R.id.lyNoContent);
        lvlOrder = (ListView) findViewById(R.id.lvlOrder);
        TabManager.setCurrentSelection(TabManager.ORDER, ac);
        setData();
    }

    private void setData() {
        new AsyncTask() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Object doInBackground(Object[] params) {
                DataBase db = new DataBase(mContext);
                db.open();
                Cursor cur = db.fetch(DataBase.basket, "is_order_place='Y' and user_id=" + Helper.getIntPreference(mContext, User.Fields.ID, 0));
                if (cur != null && cur.getCount() > 0) {
                    cur.moveToFirst();
                    arrOrder = new ArrayList<Order>();
                    do {
                        int basketItemCount = db.getCounts(DataBase.basket_items, "basket_id=" + cur.getInt(0));
                        arrOrder.add(new Order(cur.getString(0), cur.getString(1), cur.getString(2), cur.getString(3), cur.getString(4), cur.getString(5), String.valueOf(basketItemCount)));
                    } while (cur.moveToNext());
                }
                cur.close();
                db.close();
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (arrOrder != null && arrOrder.size() > 0) {
                    lyMainContent.setVisibility(View.VISIBLE);
                    lyNoContent.setVisibility(View.GONE);
                    adpOrder = new OrderAdapter(mContext, arrOrder);
                    lvlOrder.setAdapter(adpOrder);
                } else {
                    lyMainContent.setVisibility(View.GONE);
                    lyNoContent.setVisibility(View.VISIBLE);
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Helper.clearOnCloseApp(mContext);
            finish();
        }
        return false;
    }
}
