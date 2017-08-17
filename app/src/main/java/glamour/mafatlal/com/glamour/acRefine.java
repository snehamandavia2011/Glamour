package glamour.mafatlal.com.glamour;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.yahoo.mobile.client.android.util.rangeseekbar.RangeSeekBar;

import java.util.ArrayList;

import adapter.RefineCriteriaAdapter;
import adapter.SizeAdapter;
import utility.ConstantVal;
import utility.Helper;
import utility.Logger;

public class acRefine extends AppCompatActivity {
    TextView txtMinMaxPrice;
    AppCompatActivity ac;
    Button btnApply;
    ListView lvlRefineCriteria;
    int currentSelectedRefineCriteriaPosition = 0;
    Context mContext;
    Toolbar toolbar;
    GridView gvSize;
    LinearLayout lySize, lyPrice;
    com.yahoo.mobile.client.android.util.rangeseekbar.RangeSeekBar<Integer> rangPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_refine);
        mContext = this;
        ac = this;
        Helper.startFabric(mContext);
        lvlRefineCriteria = (ListView) findViewById(R.id.lvlRefineCriteria);
        lySize = (LinearLayout) findViewById(R.id.lySize);
        lyPrice = (LinearLayout) findViewById(R.id.lyPrice);
        gvSize = (GridView) findViewById(R.id.gvSize);
        btnApply = (Button) findViewById(R.id.btnApply);
        rangPrice = (com.yahoo.mobile.client.android.util.rangeseekbar.RangeSeekBar) findViewById(R.id.rangPrice);
        txtMinMaxPrice = (TextView) findViewById(R.id.txtMinMaxPrice);
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(ConstantVal.RESPONSE_REFINE);
                finish();
            }
        });
        setActionBar();
        setData();
    }

    private void setData() {
        setRefineCriteria();
        setPriceVal();
        setSizeVal();
    }

    private void setRefineCriteria() {
        lvlRefineCriteria.setAdapter(new RefineCriteriaAdapter(mContext, getResources().getStringArray(R.array.arrRefine), currentSelectedRefineCriteriaPosition));
        lvlRefineCriteria.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentSelectedRefineCriteriaPosition = position;
                for (int i = 0; i < lvlRefineCriteria.getChildCount(); i++) {
                    TextView txt = (TextView) lvlRefineCriteria.getChildAt(i);
                    if (position == i) {
                        txt.setBackgroundDrawable(new ColorDrawable(mContext.getResources()
                                .getColor(android.R.color.darker_gray)));
                        txt.setTextAppearance(mContext, R.style.styDescBlackSingleLineBold);
                    } else {
                        txt.setBackgroundDrawable(new ColorDrawable(mContext.getResources()
                                .getColor(R.color.white)));
                        txt.setTextAppearance(mContext, R.style.styDescBlackSingleLine);
                    }

                    if (position == 0) {
                        lyPrice.setVisibility(View.VISIBLE);
                        lySize.setVisibility(View.GONE);
                    } else {
                        lyPrice.setVisibility(View.GONE);
                        lySize.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    private void setPriceVal() {
        txtMinMaxPrice.setText(ConstantVal.SELECTED_MIN_PRICE + " - " + ConstantVal.SELECTED_MAX_PRICE);
        rangPrice.setRangeValues(ConstantVal.MIN_PRICE, ConstantVal.MAX_PRICE);
        rangPrice.setSelectedMinValue(ConstantVal.SELECTED_MIN_PRICE);
        rangPrice.setSelectedMaxValue(ConstantVal.SELECTED_MAX_PRICE);
        rangPrice.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                ConstantVal.SELECTED_MIN_PRICE = minValue;
                ConstantVal.SELECTED_MAX_PRICE = maxValue;
                txtMinMaxPrice.setText(minValue + " - " + maxValue);
            }
        });
    }

    private void setSizeVal() {
        gvSize.setAdapter(new SizeAdapter(mContext, ConstantVal.arrSizeMaster));
    }

    public void setActionBar() {
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
                View v = mInflater.inflate(R.layout.refine_action, null);
                actionBar.setCustomView(v);
                actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
                actionBar.setDisplayHomeAsUpEnabled(false);
                ((Button) v.findViewById(R.id.btnReset)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

                ((ImageButton) v.findViewById(R.id.btnClose)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
}
