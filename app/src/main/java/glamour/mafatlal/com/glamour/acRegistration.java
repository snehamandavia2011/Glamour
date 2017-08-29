package glamour.mafatlal.com.glamour;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.androidadvance.topsnackbar.TSnackbar;
import com.github.silvestrpredko.dotprogressbar.DotProgressBar;

import java.util.Date;

import me.zhanghai.android.materialedittext.MaterialEditText;
import utility.ConstantVal;
import utility.DateTimeUtils;
import utility.Helper;
import utility.HttpEngine;
import utility.Logger;
import utility.ServerResponse;
import utility.URLMapping;

public class acRegistration extends AppCompatActivity implements View.OnClickListener {
    Helper objHelper = new Helper();
    Context mContext;
    AppCompatActivity ac;
    MaterialEditText edFirstName, edLastName, edEmailId, edMobileNumber, edCompanyName, edCompanyPhoneNumber, edCompanyHouseNo, edCompanyStreet, edCompanyLandMark, edCompanyCity, edCompanyPostCode;
    Spinner spnCompanyState;
    RadioButton rdRetailer, rdDealer;
    Button btnCancel, btnSave;
    RelativeLayout rlDotProgress, rlMainContent;
    DotProgressBar dot_progress_bar;
    SwitchCompat scIsOwner, scIsSalesman;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        ac = this;
        Helper.startFabric(mContext);
        setContentView(R.layout.ac_registration);
        objHelper.setActionBar(this, getString(R.string.strRegistration), true);
        intializeControl();
    }

    private void intializeControl() {
        dot_progress_bar = (DotProgressBar) findViewById(R.id.dot_progress_bar);
        rlDotProgress = (RelativeLayout) findViewById(R.id.rlDotProgress);
        rlMainContent = (RelativeLayout) findViewById(R.id.rlMainContent);
        edFirstName = (MaterialEditText) findViewById(R.id.edFirstName);
        edLastName = (MaterialEditText) findViewById(R.id.edLastName);
        edEmailId = (MaterialEditText) findViewById(R.id.edEmailId);
        edMobileNumber = (MaterialEditText) findViewById(R.id.edMobileNumber);
        edCompanyName = (MaterialEditText) findViewById(R.id.edCompanyName);
        edCompanyPhoneNumber = (MaterialEditText) findViewById(R.id.edCompanyPhoneNumber);
        edCompanyHouseNo = (MaterialEditText) findViewById(R.id.edCompanyHouseNo);
        edCompanyStreet = (MaterialEditText) findViewById(R.id.edCompanyStreet);
        edCompanyLandMark = (MaterialEditText) findViewById(R.id.edCompanyLandMark);
        edCompanyCity = (MaterialEditText) findViewById(R.id.edCompanyCity);
        edCompanyPostCode = (MaterialEditText) findViewById(R.id.edCompanyPostCode);
        spnCompanyState = (Spinner) findViewById(R.id.spnCompanyState);
        rdRetailer = (RadioButton) findViewById(R.id.rdRetailer);
        rdDealer = (RadioButton) findViewById(R.id.rdDealer);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnSave = (Button) findViewById(R.id.btnSave);
        scIsOwner = (SwitchCompat) findViewById(R.id.isOwner);
        scIsSalesman = (SwitchCompat) findViewById(R.id.isSalesman);
        btnSave.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        ArrayAdapter<String> adpState = new ArrayAdapter<String>(mContext, R.layout.spinner_item_no_padding, mContext.getResources().getStringArray(R.array.arrState));
        adpState.setDropDownViewResource(R.layout.spinner_item);
        spnCompanyState.setAdapter(adpState);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSave:
                registerUser();
                break;
            case R.id.btnCancel:
                finish();
                break;
        }
    }

    private void registerUser() {
        new AsyncTask() {
            boolean isDataEnteredProper = true;
            String[] data;
            ServerResponse sr = null;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if (Helper.isFieldBlank(edFirstName.getText().toString())) {
                    isDataEnteredProper = false;
                    edFirstName.setError(getString(R.string.msgEnterFirstName));
                    Helper.requestFocus(ac, edFirstName);
                } else if (Helper.isFieldBlank(edLastName.getText().toString())) {
                    isDataEnteredProper = false;
                    edLastName.setError(getString(R.string.msgEnterLastName));
                    Helper.requestFocus(ac, edLastName);
                } else if (Helper.isFieldBlank(edEmailId.getText().toString())) {
                    isDataEnteredProper = false;
                    edEmailId.setError(getString(R.string.strEnterEmailId));
                    Helper.requestFocus(ac, edEmailId);
                } else if (!Helper.isValidEmailId(edEmailId.getText().toString())) {
                    isDataEnteredProper = false;
                    edEmailId.setError(getString(R.string.strEnterValidEmailId));
                    Helper.requestFocus(ac, edEmailId);
                } else if (Helper.isFieldBlank(edMobileNumber.getText().toString())) {
                    isDataEnteredProper = false;
                    edMobileNumber.setError(getString(R.string.msgEnterMobileNumber));
                    Helper.requestFocus(ac, edMobileNumber);
                } else if (Helper.isFieldBlank(edCompanyName.getText().toString())) {
                    isDataEnteredProper = false;
                    edCompanyName.setError(getString(R.string.msgEnterCompanyName));
                    Helper.requestFocus(ac, edCompanyName);
                } else if (Helper.isFieldBlank(edCompanyPhoneNumber.getText().toString())) {
                    isDataEnteredProper = false;
                    edCompanyPhoneNumber.setError(getString(R.string.msgEnterCompanyPhoneNumber));
                    Helper.requestFocus(ac, edCompanyPhoneNumber);
                } else if (Helper.isFieldBlank(edCompanyHouseNo.getText().toString())) {
                    isDataEnteredProper = false;
                    edCompanyHouseNo.setError(getString(R.string.msgEnterCompanyHouseNo));
                    Helper.requestFocus(ac, edCompanyHouseNo);
                } else if (Helper.isFieldBlank(edCompanyStreet.getText().toString())) {
                    isDataEnteredProper = false;
                    edCompanyStreet.setError(getString(R.string.msgEnterCompanyStreet));
                    Helper.requestFocus(ac, edCompanyStreet);
                } else if (Helper.isFieldBlank(edCompanyLandMark.getText().toString())) {
                    isDataEnteredProper = false;
                    edCompanyLandMark.setError(getString(R.string.msgEnterCompanyLandmark));
                    Helper.requestFocus(ac, edCompanyLandMark);
                } else if (Helper.isFieldBlank(edCompanyCity.getText().toString())) {
                    isDataEnteredProper = false;
                    edCompanyCity.setError(getString(R.string.msgEnterCompanyCity));
                    Helper.requestFocus(ac, edCompanyCity);
                } else if (Helper.isFieldBlank(edCompanyPostCode.getText().toString())) {
                    isDataEnteredProper = false;
                    edCompanyPostCode.setError(getString(R.string.msgEnterCompanyPostCode));
                    Helper.requestFocus(ac, edCompanyPostCode);
                }
                if (isDataEnteredProper) {
                    rlDotProgress.setVisibility(View.VISIBLE);
                    rlMainContent.setVisibility(View.GONE);
                    String userType = "", isOwner = "", isSalesMan = "";
                    if (rdDealer.isChecked())
                        userType = ConstantVal.UserType.DEALER;
                    else if (rdRetailer.isChecked())
                        userType = ConstantVal.UserType.RETAILER;
                    if (scIsSalesman.isChecked())
                        isSalesMan = "Y";
                    else
                        isSalesMan = "N";

                    if (scIsOwner.isChecked())
                        isOwner = "Y";
                    else
                        isOwner = "N";
                    data = new String[]{edFirstName.getText().toString(), edLastName.getText().toString(), edEmailId.getText().toString(),
                            edMobileNumber.getText().toString(), userType, edCompanyName.getText().toString(),
                            edCompanyPhoneNumber.getText().toString(), edCompanyHouseNo.getText().toString(), edCompanyStreet.getText().toString(),
                            edCompanyLandMark.getText().toString(), edCompanyCity.getText().toString(), spnCompanyState.getSelectedItem().toString(),
                            "India", edCompanyPostCode.getText().toString(), isOwner, isSalesMan, DateTimeUtils.getDate(new Date()), DateTimeUtils.getTime(new Date())};
                }
            }

            @Override
            protected Object doInBackground(Object[] params) {
                if (isDataEnteredProper) {
                    URLMapping umRegister = ConstantVal.registerUser();
                    HttpEngine objHttpEngine = new HttpEngine();
                    sr = objHttpEngine.getDataFromWebAPI(mContext, umRegister.getUrl(), umRegister.getParamNames(), data);
                    Logger.debug("response code:" + sr.getResponseCode() + " " + sr.getResponseString());
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                dot_progress_bar.clearAnimation();
                rlDotProgress.setVisibility(View.GONE);
                rlMainContent.setVisibility(View.VISIBLE);
                if (isDataEnteredProper) {
                    if (sr.getResponseCode().equals(ConstantVal.ServerResponseCode.SUCCESS)) {
                        Helper.displaySnackbar((AppCompatActivity) mContext, mContext.getString(R.string.msgPasswordSend), ConstantVal.ToastBGColor.SUCCESS).setCallback(new TSnackbar.Callback() {
                            @Override
                            public void onDismissed(TSnackbar snackbar, int event) {
                                super.onDismissed(snackbar, event);
                                ((AppCompatActivity) mContext).finish();
                            }
                        });
                    } else {
                        Helper.displaySnackbar((AppCompatActivity) mContext, ConstantVal.ServerResponseCode.getMessage(mContext, sr.getResponseCode()), ConstantVal.ToastBGColor.INFO).setCallback(new TSnackbar.Callback() {
                            @Override
                            public void onDismissed(TSnackbar snackbar, int event) {
                                super.onDismissed(snackbar, event);
                                //((AppCompatActivity) mContext).finish();
                            }
                        });
                    }
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
