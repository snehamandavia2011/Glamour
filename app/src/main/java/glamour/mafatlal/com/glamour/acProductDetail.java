package glamour.mafatlal.com.glamour;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

import adapter.SizeAdapter;
import adapter.SizeAdapterForProductDetail;
import asyncmanager.asyncLoadCommonData;
import entity.ProductImage;
import entity.ProductMaster;
import entity.SizeMaster;
import entity.User;
import utility.ConstantVal;
import utility.DataBase;
import utility.DotProgressBar;
import utility.Helper;
import utility.Logger;
import utility.TabManager;

public class acProductDetail extends AppCompatActivity {
    Toolbar toolbar;
    ProductMaster objProduct;
    Context mContext;
    AppCompatActivity ac;
    Button btnAddtoBag;
    com.travijuu.numberpicker.library.NumberPicker qtyPicker;
    TextView txtStockAvail, txtSizeChart, txtProductPrice, txtProductName;
    GridView gvSize;
    ImageView imgProduct;
    ProgressBar pb;
    ArrayList<SizeMaster> arrProductSize = new ArrayList<>();
    ScrollView lyMainContent;
    DotProgressBar dot_progress_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        ac = this;
        Helper.startFabric(mContext);
        setContentView(R.layout.ac_product_detail);
        TabManager.setCurrentSelection(TabManager.HOME, ac);
        setActionBar();
        setData();
    }

    private void setData() {
        new AsyncTask() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                lyMainContent = (ScrollView) findViewById(R.id.lyMainContent);
                dot_progress_bar = (DotProgressBar) findViewById(R.id.dot_progress_bar);
                txtStockAvail = (TextView) findViewById(R.id.txtStockAvail);
                txtSizeChart = (TextView) findViewById(R.id.txtSizeChart);
                txtSizeChart.setPaintFlags(txtSizeChart.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                txtProductPrice = (TextView) findViewById(R.id.txtProductPrice);
                txtProductName = (TextView) findViewById(R.id.txtProductName);
                btnAddtoBag = (Button) findViewById(R.id.btnAddtoBag);
                gvSize = (GridView) findViewById(R.id.gvSize);
                qtyPicker = (com.travijuu.numberpicker.library.NumberPicker) findViewById(R.id.qtyPicker);
                imgProduct = (ImageView) findViewById(R.id.imgProduct);
                pb = (ProgressBar) findViewById(R.id.pb);
                dot_progress_bar.setVisibility(View.VISIBLE);
            }

            @Override
            protected Object doInBackground(Object[] params) {
                try {
                    if (ac.getIntent().getExtras() != null) {
                        int productId = ac.getIntent().getIntExtra("productId", 0);
                        for (ProductMaster obj : ConstantVal.arrProduct) {
                            if (obj.getId() == productId) {
                                objProduct = obj;
                                break;
                            }
                        }

                        for (int sizeId : objProduct.getSize_id()) {
                            for (SizeMaster objSizeMaster : ConstantVal.arrSizeMaster) {
                                if (sizeId == objSizeMaster.getId()) {
                                    arrProductSize.add(objSizeMaster);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Logger.writeToCrashlytics(e);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                dot_progress_bar.clearAnimation();
                dot_progress_bar.setVisibility(View.GONE);
                lyMainContent.setVisibility(View.VISIBLE);
                if (objProduct != null) {
                    ProductImage objImage = objProduct.getProductImage().get(0);
                    if (objImage.getBmpThumb() != null) {
                        imgProduct.setVisibility(View.VISIBLE);
                        imgProduct.setImageBitmap(objImage.getBmpThumb());
                    } else {
                        new asyncLoadCommonData(mContext).loadProductImage(objImage, pb, imgProduct);
                    }
                    txtProductName.setText(objProduct.getProduct_name());
                    txtProductPrice.setText(Helper.getCurrencySymbol() + "" + objProduct.getPrice());
                    try {
                        if (Integer.parseInt(objProduct.getAvailable_stock()) > 0) {
                            txtStockAvail.setText(mContext.getText(R.string.strYes));
                        } else {
                            txtStockAvail.setText(mContext.getText(R.string.strNo));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    gvSize.setAdapter(new SizeAdapterForProductDetail(mContext, arrProductSize));
                    txtSizeChart.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LayoutInflater infalInflater = (LayoutInflater) mContext
                                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            final Dialog dialog = new Dialog(mContext);
                            View view = infalInflater.inflate(R.layout.dlg_size_chart, null, true);
                            ((ImageButton) view.findViewById(R.id.btnClose)).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                            dialog.setCancelable(false);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
                            dialog.setContentView(view);
                            dialog.show();
                        }
                    });

                    btnAddtoBag.setOnClickListener(addToBagClcik);
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private View.OnClickListener addToBagClcik = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new AsyncTask() {
                SizeMaster objSelectedSize;
                int qty;
                boolean isItemAdded = false;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    qty = qtyPicker.getValue();
                }

                @Override
                protected Object doInBackground(Object[] params) {
                    objSelectedSize = getSelectedSize();
                    if (objSelectedSize != null) {
                        long basket_id;
                        DataBase db = new DataBase(mContext);
                        db.open();
                        try {
                            Cursor cur = db.fetch(DataBase.basket, "is_order_place='N'", "'desc'");
                            if (cur != null && cur.getCount() > 0) {
                                cur.moveToFirst();
                                basket_id = cur.getLong(0);
                            } else {
                                basket_id = db.insert(DataBase.basket, DataBase.basket_int, new String[]{"N", "N", String.valueOf(Helper.getIntPreference(mContext, User.Fields.ID, 0))});
                            }
                            cur.close();
                            ProductImage objImage = objProduct.getProductImage().get(0);
                            String base64Image = "";
                            if (objImage.getBmpThumb() != null) {
                                base64Image = Helper.getEncoded64ImageStringFromBitmap(objImage.getBmpThumb());
                            }
                            db.insert(DataBase.basket_items, DataBase.basket_items_int, new String[]{String.valueOf(basket_id), String.valueOf(objProduct.getId()),
                                    objProduct.getProduct_name(), String.valueOf(objSelectedSize.getId()), objSelectedSize.getSize(), String.valueOf(qty), String.valueOf(objProduct.getPrice()),
                                    base64Image});
                            isItemAdded = true;
                        } catch (Exception e) {
                            e.printStackTrace();
                            Logger.writeToCrashlytics(e);
                        } finally {
                            db.close();
                        }
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Object o) {
                    super.onPostExecute(o);
                    if (objSelectedSize != null) {
                        if (isItemAdded) {
                            Helper.displaySnackbar(ac, getString(R.string.msgItemAddedToBag), ConstantVal.ToastBGColor.INFO);
                        }
                    } else {
                        Helper.displaySnackbar(ac, getString(R.string.msgSelectSize), ConstantVal.ToastBGColor.INFO);
                    }
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    };

    private SizeMaster getSelectedSize() {
        for (SizeMaster s : arrProductSize) {
            if (s.isSelected()) {
                return s;
            }
        }
        return null;
    }

    private void setActionBar() {
        new AsyncTask() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if (toolbar == null) {
                    toolbar = (Toolbar) ac.findViewById(R.id.toolbar);
                    ac.setSupportActionBar(toolbar);
                    ac.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ac.getResources()
                            .getColor(R.color.red)));
                }
            }

            @Override
            protected Object doInBackground(Object[] params) {
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                ac.invalidateOptionsMenu();
                ActionBar actionBar = ac.getSupportActionBar();
                actionBar.setDisplayShowHomeEnabled(true);
                actionBar.setDisplayShowCustomEnabled(true);
                actionBar.setBackgroundDrawable(new ColorDrawable(ac.getResources()
                        .getColor(R.color.red)));
                LayoutInflater mInflater = (LayoutInflater) ac.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v = mInflater.inflate(R.layout.product_detail_action, null);
                actionBar.setCustomView(v);
                actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
                actionBar.setDisplayHomeAsUpEnabled(true);
                ((ImageButton) v.findViewById(R.id.btnShare)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                ((ImageButton) v.findViewById(R.id.btnAddToCart)).setOnClickListener(addToBagClcik);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
