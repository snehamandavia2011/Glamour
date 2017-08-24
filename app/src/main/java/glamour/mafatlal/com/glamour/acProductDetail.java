package glamour.mafatlal.com.glamour;

import android.app.Dialog;
import android.content.ContentValues;
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
import java.util.Date;

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
import utility.MyGridView;
import utility.TabManager;

public class acProductDetail extends AppCompatActivity {
    ProductMaster objProduct;
    Context mContext;
    AppCompatActivity ac;
    Button btnAddtoBag;
    com.travijuu.numberpicker.library.NumberPicker qtyPicker;
    TextView txtStockAvail, txtSizeChart, txtProductPrice, txtProductName, txtProductDesc;
    utility.MyGridView gvSize;
    ImageView imgProduct;
    ProgressBar pb;
    ArrayList<SizeMaster> arrProductSize = new ArrayList<>();
    ScrollView lyMainContent;
    DotProgressBar dot_progress_bar;
    TabManager objTabManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        ac = this;
        Helper.startFabric(mContext);
        setContentView(R.layout.ac_product_detail);
        objTabManager = new TabManager(TabManager.HOME, ac);
        objTabManager.setCurrentSelection();
        new Helper().setActionBar(ac, this.getIntent().getStringExtra("productName"), true);
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
                txtProductDesc = (TextView) findViewById(R.id.txtProductDesc);
                btnAddtoBag = (Button) findViewById(R.id.btnAddtoBag);
                gvSize = (utility.MyGridView) findViewById(R.id.gvSize);
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
                                    arrProductSize.add(new SizeMaster(objSizeMaster.getId(), objSizeMaster.getSize(), false));
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
                    String strProductId = objProduct.getProduct_id().equals("") ? mContext.getString(R.string.strNA) : objProduct.getProduct_id();
                    txtProductName.setText(objProduct.getProduct_name() + " (" + strProductId + ")");
                    if (objProduct.getProduct_description().equals("")) {
                        txtProductDesc.setText(mContext.getString(R.string.msgDescNotAvail));
                    } else {
                        txtProductDesc.setText(objProduct.getProduct_description());
                    }
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
                            int customer_id = Helper.getIntPreference(mContext, User.Fields.ID, 0);
                            Cursor cur = db.fetch(DataBase.basket, "is_order_place='N' and user_id=" + customer_id, "'desc'");
                            if (cur != null && cur.getCount() > 0) {
                                cur.moveToFirst();
                                basket_id = cur.getLong(0);
                            } else {
                                basket_id = db.insert(DataBase.basket, DataBase.basket_int, new String[]{"N", "N", String.valueOf(Helper.getIntPreference(mContext, User.Fields.ID, 0)), String.valueOf(new Date().getTime()), ""});
                            }
                            cur.close();
                            ProductImage objImage = objProduct.getProductImage().get(0);
                            String base64Image = "";
                            if (objImage.getBmpThumb() != null) {
                                base64Image = Helper.getEncoded64ImageStringFromBitmap(objImage.getBmpThumb());
                            }
                            String where = "basket_id=" + basket_id + " and sizeId=" + objSelectedSize.getId() + " and product_id=" + objProduct.getId();
                            Cursor curBaseketItem = db.fetch(DataBase.basket_items, where);
                            if (curBaseketItem != null && curBaseketItem.getCount() > 0) {
                                curBaseketItem.moveToFirst();
                                int currentQuantity = curBaseketItem.getInt(7);
                                ContentValues cv = new ContentValues();
                                cv.put("quantity", (currentQuantity + 1));
                                db.update(DataBase.basket_items, DataBase.basket_items_int, where, cv);
                            } else {
                                db.insert(DataBase.basket_items, DataBase.basket_items_int, new String[]{String.valueOf(basket_id), String.valueOf(objProduct.getId()),
                                        objProduct.getProduct_name(), objProduct.getProduct_description(), String.valueOf(objSelectedSize.getId()), objSelectedSize.getSize(), String.valueOf(qty), String.valueOf(objProduct.getPrice()),
                                        base64Image});
                            }
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
                            objTabManager.updateBasketItemCounter();
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
