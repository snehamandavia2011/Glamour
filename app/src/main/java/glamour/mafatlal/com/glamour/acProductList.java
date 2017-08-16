package glamour.mafatlal.com.glamour;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.androidadvance.topsnackbar.TSnackbar;

import java.util.ArrayList;

import adapter.RefineCriteriaAdapter;
import adapter.SortAdapter;
import entity.Category;
import entity.SizeMaster;
import entity.User;
import me.zhanghai.android.materialedittext.MaterialEditText;
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
    ListView lvlProduct;
    DotProgressBar dot_progress_bar;
    RelativeLayout lyNoContent, lyMainContent;
    int sub_category_id;
    String sub_category_name;
    int currentSortPosition = 0, currentSelectedRefineCriteriaPosition = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_product_list);
        mContext = this;
        ac = this;
        Helper.startFabric(mContext);
        if (this.getIntent().getExtras() != null) {
            sub_category_id = this.getIntent().getIntExtra("sub_category_id", 0);
            sub_category_name = this.getIntent().getStringExtra("sub_category_name");
        }
        objHelper.setActionBar(this, sub_category_name, true);
        lyMainContent = (RelativeLayout) findViewById(R.id.lyMainContent);
        lyNoContent = (RelativeLayout) findViewById(R.id.lyNoContent);
        lvlProduct = (ListView) findViewById(R.id.lvlProduct);
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
        txtRefineVal.setText(getResources().getStringArray(R.array.arrRefine)[currentSelectedRefineCriteriaPosition]);
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
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void loadProductData() {
        new AsyncTask() {
            ServerResponse sr = new ServerResponse("008", "008");

            @Override
            protected Object doInBackground(Object[] params) {
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
                    lyMainContent.setVisibility(View.VISIBLE);
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
                startActivity(i);
                break;
        }
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
            }
        });
        dialog.setCancelable(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog.setContentView(view);
        dialog.show();
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
