package glamour.mafatlal.com.glamour;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import entity.User;
import utility.Helper;
import utility.TabManager;

public class acViewProfile extends AppCompatActivity {
    Helper objHelper = new Helper();
    Context mContext;
    AppCompatActivity ac;
    TextView txtUserType, txtMobileNumber, txtEmailId, txtName, txtCompanyName, txtCompanyPhone, isOwner, txtCompanyNumber, txtCompanyStreet, txtCompanyLandmark, txtCity, txtState, txtPostCode, isSalesman;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_view_profile);
        mContext = this;
        ac = this;
        Helper.startFabric(mContext);
        setContentView(R.layout.ac_view_profile);
        objHelper.setActionBar(this, getString(R.string.strProfile), true);
        TabManager.setCurrentSelection(TabManager.PROFILE, ac);
        setData();
    }

    private void setData() {
        new AsyncTask() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                txtUserType = (TextView) findViewById(R.id.txtUserType);
                txtMobileNumber = (TextView) findViewById(R.id.txtMobileNumber);
                txtEmailId = (TextView) findViewById(R.id.txtEmailId);
                txtName = (TextView) findViewById(R.id.txtName);
                txtCompanyName = (TextView) findViewById(R.id.txtCompanyName);
                txtCompanyPhone = (TextView) findViewById(R.id.txtCompanyPhone);
                isOwner = (TextView) findViewById(R.id.isOwner);
                isSalesman = (TextView) findViewById(R.id.isSalesman);
                txtCompanyNumber = (TextView) findViewById(R.id.txtCompanyNumber);
                txtCompanyStreet = (TextView) findViewById(R.id.txtCompanyStreet);
                txtCompanyLandmark = (TextView) findViewById(R.id.txtCompanyLandmark);
                txtCity = (TextView) findViewById(R.id.txtCity);
                txtState = (TextView) findViewById(R.id.txtState);
                txtPostCode = (TextView) findViewById(R.id.txtPostCode);
            }

            @Override
            protected Object doInBackground(Object[] params) {
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                txtName.setText(Helper.getStringPreference(mContext, User.Fields.FIRST_NAME, "") + " " + Helper.getStringPreference(mContext, User.Fields.LAST_NAME, ""));
                txtEmailId.setText(Helper.getStringPreference(mContext, User.Fields.EMAILID, ""));
                txtMobileNumber.setText(Helper.getStringPreference(mContext, User.Fields.MOBILE_NUMBER, ""));
                txtUserType.setText(Helper.getStringPreference(mContext, User.Fields.USER_TYPE, ""));
                txtCompanyName.setText(Helper.getStringPreference(mContext, User.Fields.COMPANY_NAME, ""));
                txtCompanyPhone.setText(Helper.getStringPreference(mContext, User.Fields.COMPANY_PHONE, ""));
                isOwner.setText(Helper.getStringPreference(mContext, User.Fields.IS_OWNER, ""));
                isSalesman.setText(Helper.getStringPreference(mContext, User.Fields.IS_SALES_MAN, ""));
                txtCompanyNumber.setText(Helper.getStringPreference(mContext, User.Fields.COMPANY_HOUSE_NO, ""));
                txtCompanyStreet.setText(Helper.getStringPreference(mContext, User.Fields.COMPANY_STREET, ""));
                txtCompanyLandmark.setText(Helper.getStringPreference(mContext, User.Fields.COMPANY_LANDMARK, ""));
                txtCity.setText(Helper.getStringPreference(mContext, User.Fields.COMPANY_CITY, ""));
                txtState.setText(Helper.getStringPreference(mContext, User.Fields.COMPANY_STATE, ""));
                txtPostCode.setText(Helper.getStringPreference(mContext, User.Fields.COMPANY_POST_CODE, ""));
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
