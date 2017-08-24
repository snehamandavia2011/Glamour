package glamour.mafatlal.com.glamour;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import adapter.CategoryAdapter;
import adapter.SubCategoryAdapter;
import entity.Category;
import utility.ConstantVal;
import utility.DotProgressBar;
import utility.Helper;
import utility.TabManager;

public class acSubCategory extends AppCompatActivity {
    Helper objHelper = new Helper();
    Context mContext;
    AppCompatActivity ac;
    ListView lvlCategory;
    DotProgressBar dot_progress_bar;
    RelativeLayout lyNoContent, lyMainContent;
    ArrayList<Category> arrCategory;
    int category_id;
    String category_name;
    TabManager objTabManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        ac = this;
        Helper.startFabric(mContext);
        setContentView(R.layout.ac_home);
        if (this.getIntent().getExtras() != null) {
            category_id = this.getIntent().getIntExtra("category_id", 0);
            category_name = this.getIntent().getStringExtra("category_name");
        }
        objHelper.setActionBar(this, category_name, true);
        lyMainContent = (RelativeLayout) findViewById(R.id.lyMainContent);
        lyNoContent = (RelativeLayout) findViewById(R.id.lyNoContent);
        lvlCategory = (ListView) findViewById(R.id.lvlAsset);
        dot_progress_bar = (DotProgressBar) findViewById(R.id.dot_progress_bar);
        objTabManager = new TabManager(TabManager.HOME, ac);
        objTabManager.setCurrentSelection();
        setData();
    }

    private void setData() {
        new AsyncTask() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dot_progress_bar.setVisibility(View.VISIBLE);
            }

            @Override
            protected Object doInBackground(Object[] params) {
                arrCategory = Category.getCategoryFromDatabase(category_id, mContext);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                dot_progress_bar.clearAnimation();
                dot_progress_bar.setVisibility(View.GONE);
                ((ViewGroup) dot_progress_bar.getParent()).removeView(dot_progress_bar);
                if (arrCategory != null && arrCategory.size() > 0) {
                    lyMainContent.setVisibility(View.VISIBLE);
                    lyNoContent.setVisibility(View.GONE);
                    lvlCategory.setAdapter(new SubCategoryAdapter(mContext, arrCategory));
                } else {
                    //lyMainContent.setVisibility(View.GONE);
                    //lyNoContent.setVisibility(View.VISIBLE);
                    Intent i = new Intent(mContext, acProductList.class);
                    i.putExtra("sub_category_id", category_id);
                    i.putExtra("sub_category_name", category_name);
                    mContext.startActivity(i);
                    finish();
                }
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
