package glamour.mafatlal.com.glamour;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import adapter.CategoryAdapter;
import entity.Category;
import utility.DotProgressBar;
import utility.Helper;
import utility.TabManager;

public class acHome extends AppCompatActivity {
    Helper objHelper = new Helper();
    Context mContext;
    AppCompatActivity ac;
    ListView lvlCategory;
    DotProgressBar dot_progress_bar;
    RelativeLayout lyNoContent, lyMainContent;
    ArrayList<Category> arrCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        ac = this;
        Helper.startFabric(mContext);
        setContentView(R.layout.ac_home);
        objHelper.setActionBar(this, getString(R.string.strHome), false);
        lyMainContent = (RelativeLayout) findViewById(R.id.lyMainContent);
        lyNoContent = (RelativeLayout) findViewById(R.id.lyNoContent);
        lvlCategory = (ListView) findViewById(R.id.lvlAsset);
        dot_progress_bar = (DotProgressBar) findViewById(R.id.dot_progress_bar);
        TabManager.setCurrentSelection(TabManager.HOME, ac);
        setData();
    }

    private void setData() {
        fetchDataFromServer();
    }

    private void fetchDataFromServer() {
        new AsyncTask() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dot_progress_bar.setVisibility(View.VISIBLE);
            }

            @Override
            protected Object doInBackground(Object[] params) {
                arrCategory = new ArrayList<Category>();
                arrCategory.add(new Category(1, "Casual Wear", "Glamour"));
                arrCategory.add(new Category(1, "Denims", "Glamour"));
                arrCategory.add(new Category(1, "Ethnic", "Glamour"));
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                dot_progress_bar.setVisibility(View.GONE);
                if (arrCategory != null && arrCategory.size() > 0) {
                    lyMainContent.setVisibility(View.VISIBLE);
                    lyNoContent.setVisibility(View.GONE);
                    lvlCategory.setAdapter(new CategoryAdapter(mContext, arrCategory));
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
            finish();
        }
        return false;
    }
}
