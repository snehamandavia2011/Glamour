package glamour.mafatlal.com.glamour;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidadvance.topsnackbar.TSnackbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import adapter.ProductAdapter;
import adapter.SortAdapter;
import entity.ProductMaster;
import entity.SizeMaster;
import entity.User;
import utility.ConstantVal;
import utility.DotProgressBar;
import utility.Helper;
import utility.HttpEngine;
import utility.Logger;
import utility.ServerResponse;
import utility.TabManager;
import utility.URLMapping;

public class acProductList extends AppCompatActivity implements View.OnClickListener {
    LinearLayout lySort, lyRefine;
    TextView txtSortVal, txtRefineVal;
    Helper objHelper = new Helper();
    Context mContext;
    AppCompatActivity ac;
    GridView gvProduct;
    DotProgressBar dot_progress_bar;
    RelativeLayout lyNoContent, lyMainContent;
    int sub_category_id;
    String sub_category_name;
    int currentSortPosition = 0;
    ProductAdapter adpProduct;
    ArrayList<ProductMaster> arrProductRefined;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        ac = this;
        Helper.startFabric(mContext);
        setContentView(R.layout.ac_product_list);
        ConstantVal.arrProduct = null;
        if (this.getIntent().getExtras() != null) {
            sub_category_id = this.getIntent().getIntExtra("sub_category_id", 0);
            sub_category_name = this.getIntent().getStringExtra("sub_category_name");
        }
        objHelper.setActionBar(this, sub_category_name, true);
        lyMainContent = (RelativeLayout) findViewById(R.id.lyMainContent);
        lyNoContent = (RelativeLayout) findViewById(R.id.lyNoContent);
        gvProduct = (GridView) findViewById(R.id.gvProduct);
        dot_progress_bar = (DotProgressBar) findViewById(R.id.dot_progress_bar);
        lySort = (LinearLayout) findViewById(R.id.lySort);
        lyRefine = (LinearLayout) findViewById(R.id.lyRefine);
        txtSortVal = (TextView) findViewById(R.id.txtSortVal);
        txtRefineVal = (TextView) findViewById(R.id.txtRefineVal);
        lySort.setOnClickListener(this);
        lyRefine.setOnClickListener(this);
        TabManager.setCurrentSelection(TabManager.HOME, ac);
        setData();
    }

    private void setData() {
        txtSortVal.setText(getResources().getStringArray(R.array.arrSort)[currentSortPosition]);
        txtRefineVal.setText(getResources().getStringArray(R.array.arrRefine)[0] + ", " + getResources().getStringArray(R.array.arrRefine)[1]);
        getSize();
    }

    private void getSize() {
        new AsyncTask() {
            ServerResponse sr;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dot_progress_bar.setVisibility(View.VISIBLE);
                if (!new HttpEngine().isNetworkAvailable(mContext)) {
                    Helper.displaySnackbar(ac, getResources().getString(R.string.strInternetNotAvaiable), ConstantVal.ToastBGColor.INFO).setCallback(new TSnackbar.Callback() {
                        @Override
                        public void onDismissed(TSnackbar snackbar, int event) {
                            super.onDismissed(snackbar, event);
                            finish();
                        }
                    });
                }
            }

            @Override
            protected Object doInBackground(Object[] params) {
                if (ConstantVal.arrSizeMaster == null) {
                    String token = Helper.getStringPreference(mContext, User.Fields.TOKEN, "");
                    URLMapping umRegister = ConstantVal.getSizeList();
                    HttpEngine objHttpEngine = new HttpEngine();
                    sr = objHttpEngine.getDataFromWebAPI(mContext, umRegister.getUrl(), umRegister.getParamNames(), new String[]{token});
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (ConstantVal.arrSizeMaster == null) {
                    if (sr.getResponseCode().equals(ConstantVal.ServerResponseCode.SUCCESS)) {
                        ConstantVal.arrSizeMaster = SizeMaster.parseArray(sr.getResponseString());
                        loadProductData();
                    } else {
                        dot_progress_bar.clearAnimation();
                        dot_progress_bar.setVisibility(View.GONE);
                        ((ViewGroup) dot_progress_bar.getParent()).removeView(dot_progress_bar);
                        Helper.displaySnackbar(ac, ConstantVal.ServerResponseCode.getMessage(mContext, sr.getResponseCode()), ConstantVal.ToastBGColor.INFO).setCallback(new TSnackbar.Callback() {
                            @Override
                            public void onDismissed(TSnackbar snackbar, int event) {
                                super.onDismissed(snackbar, event);
                                finish();
                            }
                        });
                    }
                } else {
                    loadProductData();
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void loadProductData() {
        new AsyncTask() {
            ServerResponse sr;

            @Override
            protected Object doInBackground(Object[] params) {
                if (ConstantVal.arrProduct == null) {
                    String token = Helper.getStringPreference(mContext, User.Fields.TOKEN, "");
                    URLMapping um = ConstantVal.getProductList();
                    HttpEngine objHttpEngine = new HttpEngine();
                    sr = objHttpEngine.getDataFromWebAPI(mContext, um.getUrl(), um.getParamNames(), new String[]{token, String.valueOf(sub_category_id)});
                    if (sr.getResponseCode().equals(ConstantVal.ServerResponseCode.SUCCESS))
                        ConstantVal.arrProduct = ProductMaster.parseJSON(sr.getResponseString());
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                dot_progress_bar.clearAnimation();
                dot_progress_bar.setVisibility(View.GONE);
                ((ViewGroup) dot_progress_bar.getParent()).removeView(dot_progress_bar);
                if (!sr.getResponseCode().equals(ConstantVal.ServerResponseCode.SUCCESS)) {
                    Helper.displaySnackbar(ac, ConstantVal.ServerResponseCode.getMessage(mContext, sr.getResponseCode()), ConstantVal.ToastBGColor.INFO).setCallback(new TSnackbar.Callback() {
                        @Override
                        public void onDismissed(TSnackbar snackbar, int event) {
                            super.onDismissed(snackbar, event);
                            finish();
                        }
                    });
                } else {
                    if (ConstantVal.arrProduct != null && ConstantVal.arrProduct.size() > 0) {
                        arrProductRefined = new ArrayList<ProductMaster>();
                        for (ProductMaster obj : ConstantVal.arrProduct) {
                            arrProductRefined.add(obj);
                        }
                        lyMainContent.setVisibility(View.VISIBLE);
                        lyNoContent.setVisibility(View.GONE);
                        adpProduct = new ProductAdapter(mContext, arrProductRefined);
                        gvProduct.setAdapter(adpProduct);

                        //default sort order and default min and max price
                        sortPriceHighToLow();
                        ConstantVal.MIN_PRICE = ConstantVal.SELECTED_MIN_PRICE = arrProductRefined.get(arrProductRefined.size() - 1).getPrice();
                        ConstantVal.MAX_PRICE = ConstantVal.SELECTED_MAX_PRICE = arrProductRefined.get(0).getPrice();
                        ConstantVal.ARR_SELECTED_SIZE.clear();
                        for (SizeMaster s : ConstantVal.arrSizeMaster) {
                            s.setSelected(false);
                        }
                    } else {
                        lyNoContent.setVisibility(View.VISIBLE);
                        lyMainContent.setVisibility(View.GONE);
                    }
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lySort:
                showSortDialog();
                break;
            case R.id.lyRefine:
                Intent i = new Intent(mContext, acRefine.class);
                startActivityForResult(i, ConstantVal.REQUEST_REFINE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ConstantVal.REQUEST_REFINE && resultCode == ConstantVal.RESPONSE_REFINE) {
            Logger.debug(ConstantVal.SELECTED_MIN_PRICE + " " + ConstantVal.SELECTED_MAX_PRICE);
            if (ConstantVal.ARR_SELECTED_SIZE.size() > 0) {
                for (SizeMaster obj : ConstantVal.ARR_SELECTED_SIZE) {
                    Logger.debug(obj.getId() + ",");
                }
            }
            new AsyncTask() {
                @Override
                protected Object doInBackground(Object[] params) {
                    arrProductRefined.clear();
                    for (int i = 0; i < ConstantVal.arrProduct.size(); i++) {
                        ProductMaster objProductMaster = ConstantVal.arrProduct.get(i);
                        if (objProductMaster.getPrice() >= ConstantVal.SELECTED_MIN_PRICE && objProductMaster.getPrice() <= ConstantVal.SELECTED_MAX_PRICE) {
                            arrProductRefined.add(objProductMaster);
                        }
                        if (ConstantVal.ARR_SELECTED_SIZE.size() > 0) {//if this array is empty then show all size product
                            for (int j = 0; j < arrProductRefined.size(); j++) {
                                ArrayList<Integer> arrSizeId = arrProductRefined.get(j).getSize_id();
                                //check if product size Id exists in selected size
                                boolean isExists = isSizeExists(arrSizeId);
                                if (!isExists) {
                                    arrProductRefined.remove(j);
                                }
                            }
                        }
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Object o) {
                    super.onPostExecute(o);
                    adpProduct.notifyDataSetChanged();
                    performSorting();
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    private boolean isSizeExists(ArrayList<Integer> arrSizeId) {
        boolean isExists = false;
        for (SizeMaster objSize : ConstantVal.ARR_SELECTED_SIZE) {
            for (Integer size : arrSizeId) {
                if (objSize.getId() == size) {
                    isExists = true;
                    return isExists;
                }
            }
        }
        return isExists;
    }

    private void showSortDialog() {
        LayoutInflater infalInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final Dialog dialog = new Dialog(mContext);
        View view = infalInflater.inflate(R.layout.listview, null, true);
        ListView lvl = (ListView) view.findViewById(R.id.lvl);
        lvl.setAdapter(new SortAdapter(mContext, getResources().getStringArray(R.array.arrSort), currentSortPosition));
        lvl.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentSortPosition = position;
                txtSortVal.setText(getResources().getStringArray(R.array.arrSort)[currentSortPosition]);
                dialog.dismiss();
                performSorting();
            }
        });
        dialog.setCancelable(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog.setContentView(view);
        dialog.show();
    }

    private void performSorting() {
        switch (currentSortPosition) {
            case 0://price high to low
                sortPriceHighToLow();
                break;
            case 1://low to high
                sortPriceLowToHigh();
                break;
            case 2://whats new
                whatsNew();
                break;
            case 3://popularity
                popularity();
                break;
        }
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

    private void sortPriceLowToHigh() {
        Collections.sort(arrProductRefined, new Comparator<ProductMaster>() {
            @Override
            public int compare(ProductMaster p1, ProductMaster p2) {
                return p1.getPrice() - p2.getPrice();
            }
        });
        adpProduct.notifyDataSetChanged();
    }

    private void sortPriceHighToLow() {
        Collections.sort(arrProductRefined, new Comparator<ProductMaster>() {
            @Override
            public int compare(ProductMaster p1, ProductMaster p2) {
                return p2.getPrice() - p1.getPrice();
            }
        });
        adpProduct.notifyDataSetChanged();
    }

    private void whatsNew() {
        Collections.sort(arrProductRefined, new Comparator<ProductMaster>() {
            @Override
            public int compare(ProductMaster p1, ProductMaster p2) {
                return new Date(p2.getDateTime()).compareTo(new Date(p1.getDateTime()));
            }
        });
        adpProduct.notifyDataSetChanged();
    }

    private void popularity(){
        Collections.sort(arrProductRefined, new Comparator<ProductMaster>() {
            @Override
            public int compare(ProductMaster p1, ProductMaster p2) {
                return new Date(p2.getOrder_count()).compareTo(new Date(p1.getOrder_count()));
            }
        });
        adpProduct.notifyDataSetChanged();
    }
}
