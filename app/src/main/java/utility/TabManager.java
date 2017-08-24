package utility;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import entity.User;
import glamour.mafatlal.com.glamour.R;
import glamour.mafatlal.com.glamour.acBasket;
import glamour.mafatlal.com.glamour.acHome;
import glamour.mafatlal.com.glamour.acOrder;
import glamour.mafatlal.com.glamour.acProfile;

import static glamour.mafatlal.com.glamour.R.id.lyHome;

/**
 * Created by SAI on 8/12/2017.
 */

public class TabManager {
    public static final int HOME = 0;
    public static final int ORDER = 1;
    public static final int PROFILE = 2;
    public static final int BASKET = 3;

    TextView itemCountInBasket;
    LinearLayout lyHome, lyOrder, lyProfile;
    RelativeLayout lyBasket;
    ImageView imgHome, imgOrder, imgProfile, imgBasket;
    TextView txtHome, textOrder, textProfile, txtBAsket;
    int intItemCountInBasket;
    int currentTab;
    AppCompatActivity ac;

    public TabManager(int currentTab, AppCompatActivity ac) {
        this.currentTab = currentTab;
        this.ac = ac;

    }

    public void setCurrentSelection() {
        new AsyncTask() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                itemCountInBasket = (TextView) ac.findViewById(R.id.itemCountInBasket);
                lyHome = (LinearLayout) ac.findViewById(R.id.lyHome);
                lyOrder = (LinearLayout) ac.findViewById(R.id.lyOrder);
                lyProfile = (LinearLayout) ac.findViewById(R.id.lyProfile);
                lyBasket = (RelativeLayout) ac.findViewById(R.id.lyBasket);
                imgHome = (ImageView) ac.findViewById(R.id.imgHome);
                imgOrder = (ImageView) ac.findViewById(R.id.imgOrder);
                imgProfile = (ImageView) ac.findViewById(R.id.imgProfile);
                imgBasket = (ImageView) ac.findViewById(R.id.imgBasket);
                txtHome = (TextView) ac.findViewById(R.id.textHome);
                textOrder = (TextView) ac.findViewById(R.id.textOrder);
                textProfile = (TextView) ac.findViewById(R.id.textProfile);
                txtBAsket = (TextView) ac.findViewById(R.id.txtBasket);
            }

            @Override
            protected Object doInBackground(Object[] params) {
                setIntItemCountInBasket();
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                Logger.debug("intItemCountInBasket:" + intItemCountInBasket);
                if (intItemCountInBasket > 0) {
                    itemCountInBasket.setText(intItemCountInBasket + "");
                    itemCountInBasket.setVisibility(View.VISIBLE);
                } else {
                    itemCountInBasket.setVisibility(View.GONE);
                }
                if (currentTab == HOME) {
                    setSelection(ac, lyHome, imgHome, R.drawable.ic_home_red, txtHome);
                    setDeSelection(ac, lyOrder, imgOrder, R.drawable.ic_order_white, textOrder);
                    setDeSelection(ac, lyProfile, imgProfile, R.drawable.ic_profile_white, textProfile);
                    setDeSelection(ac, lyBasket, imgBasket, R.drawable.ic_basket_white, txtBAsket);
                } else if (currentTab == ORDER) {
                    setDeSelection(ac, lyHome, imgHome, R.drawable.ic_home_white, txtHome);
                    setSelection(ac, lyOrder, imgOrder, R.drawable.ic_order_red, textOrder);
                    setDeSelection(ac, lyProfile, imgProfile, R.drawable.ic_profile_white, textProfile);
                    setDeSelection(ac, lyBasket, imgBasket, R.drawable.ic_basket_white, txtBAsket);
                } else if (currentTab == PROFILE) {
                    setDeSelection(ac, lyHome, imgHome, R.drawable.ic_home_white, txtHome);
                    setDeSelection(ac, lyOrder, imgOrder, R.drawable.ic_order_white, textOrder);
                    setSelection(ac, lyProfile, imgProfile, R.drawable.ic_profile_red, textProfile);
                    setDeSelection(ac, lyBasket, imgBasket, R.drawable.ic_basket_white, txtBAsket);
                } else if (currentTab == BASKET) {
                    setDeSelection(ac, lyHome, imgHome, R.drawable.ic_home_white, txtHome);
                    setDeSelection(ac, lyOrder, imgOrder, R.drawable.ic_order_white, textOrder);
                    setDeSelection(ac, lyProfile, imgProfile, R.drawable.ic_profile_white, textProfile);
                    setSelection(ac, lyBasket, imgBasket, R.drawable.ic_basket_red, txtBAsket);
                }
                handleHomeClick(ac, lyHome);
                handleOrderClick(ac, lyOrder);
                handleProfileClick(ac, lyProfile);
                handleBasketClick(ac, lyBasket);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void setIntItemCountInBasket() {
        DataBase db = new DataBase(ac);
        db.open();
        Cursor cur = db.fetch(DataBase.basket, "is_order_place='N' and user_id=" + Helper.getIntPreference(ac, User.Fields.ID, 1), "'desc'");
        try {
            if (cur != null && cur.getCount() > 0) {
                cur.moveToFirst();
                long basket_id = cur.getLong(0);
                this.intItemCountInBasket = db.getCounts(DataBase.basket_items, "basket_id=" + basket_id);
            }
        } catch (Exception e) {
            this.intItemCountInBasket = 0;
        } finally {
            cur.close();
            db.close();
        }

    }

    public void updateBasketItemCounter() {
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                setIntItemCountInBasket();
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (intItemCountInBasket > 0) {
                    itemCountInBasket.setText(intItemCountInBasket + "");
                    itemCountInBasket.setVisibility(View.VISIBLE);
                } else {
                    itemCountInBasket.setVisibility(View.GONE);
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void handleHomeClick(final AppCompatActivity ac, LinearLayout lyHome) {
        if (ac.getClass() == acHome.class) {
            lyHome.setOnClickListener(null);
        } else {
            lyHome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(ac, acHome.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    ac.startActivity(i);
                }
            });
        }
    }

    private void handleOrderClick(final AppCompatActivity ac, LinearLayout lyOrder) {
        if (ac.getClass() == acOrder.class) {
            lyOrder.setOnClickListener(null);
        } else {
            lyOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(ac, acOrder.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    ac.startActivity(i);
                }
            });
        }
    }

    private void handleProfileClick(final AppCompatActivity ac, LinearLayout lyProfile) {
        if (ac.getClass() == acProfile.class) {
            lyProfile.setOnClickListener(null);
        } else {
            lyProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(ac, acProfile.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    ac.startActivity(i);
                }
            });
        }
    }

    private void handleBasketClick(final AppCompatActivity ac, LinearLayout lyBasket) {
        if (ac.getClass() == acBasket.class) {
            lyBasket.setOnClickListener(null);
        } else {
            lyBasket.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(ac, acBasket.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    ac.startActivity(i);
                }
            });
        }
    }

    private void handleBasketClick(final AppCompatActivity ac, RelativeLayout lyBasket) {
        if (ac.getClass() == acBasket.class) {
            lyBasket.setOnClickListener(null);
        } else {
            lyBasket.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(ac, acBasket.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    ac.startActivity(i);
                }
            });
        }
    }

    private void setSelection(AppCompatActivity ac, LinearLayout ly, ImageView img, int imgResource, TextView txt) {
        ly.setBackgroundDrawable(new ColorDrawable(ac.getResources()
                .getColor(R.color.white)));
        txt.setTextColor(ContextCompat.getColor(ac, R.color.red));
        img.setImageResource(imgResource);
        itemCountInBasket.setTextColor(ContextCompat.getColor(ac, R.color.white));
        itemCountInBasket.setBackgroundResource(R.drawable.badge_item_count_red);
    }

    private void setDeSelection(AppCompatActivity ac, LinearLayout ly, ImageView img, int imgResource, TextView txt) {
        ly.setBackgroundDrawable(new ColorDrawable(ac.getResources()
                .getColor(R.color.red)));
        txt.setTextColor(ContextCompat.getColor(ac, R.color.white));
        img.setImageResource(imgResource);
        itemCountInBasket.setTextColor(ContextCompat.getColor(ac, R.color.red));
        itemCountInBasket.setBackgroundResource(R.drawable.badge_item_count_white);
    }

    private void setSelection(AppCompatActivity ac, RelativeLayout ly, ImageView img, int imgResource, TextView txt) {
        ly.setBackgroundDrawable(new ColorDrawable(ac.getResources()
                .getColor(R.color.white)));
        txt.setTextColor(ContextCompat.getColor(ac, R.color.red));
        img.setImageResource(imgResource);
        itemCountInBasket.setTextColor(ContextCompat.getColor(ac, R.color.white));
        itemCountInBasket.setBackgroundResource(R.drawable.badge_item_count_red);
    }

    private void setDeSelection(AppCompatActivity ac, RelativeLayout ly, ImageView img, int imgResource, TextView txt) {
        ly.setBackgroundDrawable(new ColorDrawable(ac.getResources()
                .getColor(R.color.red)));
        txt.setTextColor(ContextCompat.getColor(ac, R.color.white));
        img.setImageResource(imgResource);
        itemCountInBasket.setTextColor(ContextCompat.getColor(ac, R.color.red));
        itemCountInBasket.setBackgroundResource(R.drawable.badge_item_count_white);
    }
}
