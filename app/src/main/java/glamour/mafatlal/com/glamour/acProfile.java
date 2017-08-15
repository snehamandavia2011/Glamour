package glamour.mafatlal.com.glamour;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.androidadvance.topsnackbar.TSnackbar;

import org.json.JSONObject;

import entity.User;
import me.zhanghai.android.materialedittext.MaterialEditText;
import utility.ConfimationSnackbar;
import utility.ConstantVal;
import utility.DataBase;
import utility.DotProgressBar;
import utility.Helper;
import utility.HttpEngine;
import utility.Logger;
import utility.ServerResponse;
import utility.TabManager;
import utility.URLMapping;

public class acProfile extends AppCompatActivity implements View.OnClickListener {
    Helper objHelper = new Helper();
    Context mContext;
    AppCompatActivity ac;
    LinearLayout lyPersonalInformation;
    Button btnEditPersonalInfor, btnEditCompanyDetail, btnEditCompanyAddress, btnChangePassword, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        ac = this;
        Helper.startFabric(mContext);
        setContentView(R.layout.ac_profile);
        objHelper.setActionBar(this, getString(R.string.strProfile), false);
        TabManager.setCurrentSelection(TabManager.PROFILE, ac);
        lyPersonalInformation = (LinearLayout) findViewById(R.id.lyPersonalInformation);
        btnEditPersonalInfor = (Button) findViewById(R.id.btnEditPersonalInfor);
        btnEditCompanyDetail = (Button) findViewById(R.id.btnEditCompanyDetail);
        btnEditCompanyAddress = (Button) findViewById(R.id.btnEditCompanyAddress);
        btnChangePassword = (Button) findViewById(R.id.btnChangePassword);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        lyPersonalInformation.setOnClickListener(this);
        btnEditPersonalInfor.setOnClickListener(this);
        btnEditCompanyAddress.setOnClickListener(this);
        btnEditCompanyDetail.setOnClickListener(this);
        btnChangePassword.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lyPersonalInformation:
                Intent i = new Intent(getApplicationContext(), acViewProfile.class);
                startActivity(i);
                break;
            case R.id.btnEditPersonalInfor:
                editPersonalInfo();
                break;
            case R.id.btnEditCompanyDetail:
                editCompanyDetail();
                break;
            case R.id.btnEditCompanyAddress:
                editCompanyAddress();
                break;
            case R.id.btnChangePassword:
                changePassword();
                break;
            case R.id.btnLogout:
                logoutUser();
                break;
        }
    }

    private void changePassword() {
        LayoutInflater infalInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final Dialog dialog = new Dialog(mContext);
        View view = infalInflater.inflate(R.layout.dlg_change_password, null, true);
        final MaterialEditText edOldPassword = (MaterialEditText) view.findViewById(R.id.edOldPassword);
        final MaterialEditText edNewPassword = (MaterialEditText) view.findViewById(R.id.edNewPassword);
        final MaterialEditText edConfirmPassword = (MaterialEditText) view.findViewById(R.id.edConfirmPassword);
        final DotProgressBar dot_progress_bar = (DotProgressBar) view.findViewById(R.id.dot_progress_bar);
        final TextView txtErrorMessage = (TextView) view.findViewById(R.id.txtErrorMessage);
        ImageButton btnClose = (ImageButton) view.findViewById(R.id.btnClose);
        Button btnSave = (Button) view.findViewById(R.id.btnSave);
        Button btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTask() {
                    User objUser;
                    boolean isDataEnteredProper = true;
                    String[] data;
                    ServerResponse sr = null;

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        if (Helper.isFieldBlank(edOldPassword.getText().toString())) {
                            isDataEnteredProper = false;
                            edOldPassword.setError(getString(R.string.strEnterOldPassword));
                            Helper.requestFocus(ac, edOldPassword);
                        } else if (Helper.isFieldBlank(edNewPassword.getText().toString())) {
                            isDataEnteredProper = false;
                            edNewPassword.setError(getString(R.string.strEnterNewPassword));
                            Helper.requestFocus(ac, edNewPassword);
                        } else if (Helper.isFieldBlank(edConfirmPassword.getText().toString())) {
                            isDataEnteredProper = false;
                            edConfirmPassword.setError(getString(R.string.msgEnterConfirmPassword));
                            Helper.requestFocus(ac, edConfirmPassword);
                        } else if (!edNewPassword.getText().toString().equals(edConfirmPassword.getText().toString())) {
                            isDataEnteredProper = false;
                            edConfirmPassword.setError(getString(R.string.msgPasswordConfirmShouldSame));
                            Helper.requestFocus(ac, edConfirmPassword);
                        } else if (!Helper.getStringPreference(mContext, User.Fields.PASSWORD, "").equals(edOldPassword.getText().toString())) {
                            isDataEnteredProper = false;
                            edOldPassword.setError(getString(R.string.msgInvalidOldPassword));
                            Helper.requestFocus(ac, edOldPassword);
                        }
                        if (isDataEnteredProper) {
                            dot_progress_bar.setVisibility(View.VISIBLE);
                            String strEmail = Helper.getStringPreference(mContext, User.Fields.EMAILID, "");
                            String token = Helper.getStringPreference(mContext, User.Fields.TOKEN, "");
                            data = new String[]{strEmail, edNewPassword.getText().toString(), token};
                            objUser = new User(strEmail, edNewPassword.getText().toString());
                        }
                    }

                    @Override
                    protected Object doInBackground(Object[] params) {
                        if (isDataEnteredProper) {
                            URLMapping umRegister = ConstantVal.changePassword();
                            HttpEngine objHttpEngine = new HttpEngine();
                            sr = objHttpEngine.getDataFromWebAPI(mContext, umRegister.getUrl(), umRegister.getParamNames(), data);
                            objUser.savePasswordDetailToPreference(mContext);
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        super.onPostExecute(o);
                        dot_progress_bar.clearAnimation();
                        dot_progress_bar.setVisibility(View.GONE);
                        if (isDataEnteredProper) {
                            if (sr.getResponseCode().equals(ConstantVal.ServerResponseCode.SUCCESS)) {
                                dialog.dismiss();
                            } else {
                                txtErrorMessage.setVisibility(View.VISIBLE);
                                txtErrorMessage.setText(ConstantVal.ServerResponseCode.getMessage(mContext, sr.getResponseCode()));
                            }
                        }
                    }
                }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });
        dialog.setCancelable(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog.setContentView(view);
        dialog.show();
    }

    private void editPersonalInfo() {
        LayoutInflater infalInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final Dialog dialog = new Dialog(mContext);
        View view = infalInflater.inflate(R.layout.dlg_edit_personal_info, null, true);
        final MaterialEditText edFirstName = (MaterialEditText) view.findViewById(R.id.edFirstName);
        final MaterialEditText edLastName = (MaterialEditText) view.findViewById(R.id.edLastName);
        final MaterialEditText edMobileNumber = (MaterialEditText) view.findViewById(R.id.edMobileNumber);
        final RadioButton rdRetailer = (RadioButton) view.findViewById(R.id.rdRetailer);
        final RadioButton rdDealer = (RadioButton) view.findViewById(R.id.rdDealer);
        final DotProgressBar dot_progress_bar = (DotProgressBar) view.findViewById(R.id.dot_progress_bar);
        final TextView txtErrorMessage = (TextView) view.findViewById(R.id.txtErrorMessage);
        edFirstName.setText(Helper.getStringPreference(mContext, User.Fields.FIRST_NAME, ""));
        edLastName.setText(Helper.getStringPreference(mContext, User.Fields.LAST_NAME, ""));
        edMobileNumber.setText(Helper.getStringPreference(mContext, User.Fields.MOBILE_NUMBER, ""));
        String strUserType = Helper.getStringPreference(mContext, User.Fields.USER_TYPE, "");
        if (strUserType.equals(ConstantVal.UserType.DEALER))
            rdDealer.setChecked(true);
        else if (strUserType.equals(ConstantVal.UserType.RETAILER))
            rdRetailer.setChecked(true);
        ImageButton btnClose = (ImageButton) view.findViewById(R.id.btnClose);
        Button btnSave = (Button) view.findViewById(R.id.btnSave);
        Button btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTask() {
                    User objUser;
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
                        } else if (Helper.isFieldBlank(edMobileNumber.getText().toString())) {
                            isDataEnteredProper = false;
                            edMobileNumber.setError(getString(R.string.msgEnterMobileNumber));
                            Helper.requestFocus(ac, edMobileNumber);
                        }
                        if (isDataEnteredProper) {
                            dot_progress_bar.setVisibility(View.VISIBLE);
                            String userType = "";
                            if (rdDealer.isChecked())
                                userType = ConstantVal.UserType.DEALER;
                            else if (rdRetailer.isChecked())
                                userType = ConstantVal.UserType.RETAILER;
                            String strEmail = Helper.getStringPreference(mContext, User.Fields.EMAILID, "");
                            String token = Helper.getStringPreference(mContext, User.Fields.TOKEN, "");
                            data = new String[]{edFirstName.getText().toString(), edLastName.getText().toString(), strEmail,
                                    edMobileNumber.getText().toString(), userType, token};
                            objUser = new User(edFirstName.getText().toString(), edLastName.getText().toString(),
                                    edMobileNumber.getText().toString(), userType);
                        }
                    }

                    @Override
                    protected Object doInBackground(Object[] params) {
                        if (isDataEnteredProper) {
                            URLMapping umRegister = ConstantVal.updatePersonalInfo();
                            HttpEngine objHttpEngine = new HttpEngine();
                            sr = objHttpEngine.getDataFromWebAPI(mContext, umRegister.getUrl(), umRegister.getParamNames(), data);
                            objUser.savePersonalDetailToPreference(mContext);
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        super.onPostExecute(o);
                        dot_progress_bar.clearAnimation();
                        dot_progress_bar.setVisibility(View.GONE);
                        if (isDataEnteredProper) {
                            if (sr.getResponseCode().equals(ConstantVal.ServerResponseCode.SUCCESS)) {
                                dialog.dismiss();
                            } else {
                                txtErrorMessage.setVisibility(View.VISIBLE);
                                txtErrorMessage.setText(ConstantVal.ServerResponseCode.getMessage(mContext, sr.getResponseCode()));
                            }
                        }
                    }
                }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });
        dialog.setCancelable(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog.setContentView(view);
        dialog.show();
    }

    private void editCompanyDetail() {
        LayoutInflater infalInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final Dialog dialog = new Dialog(mContext);
        View view = infalInflater.inflate(R.layout.dlg_edit_company_info, null, true);
        final MaterialEditText edCompanyName = (MaterialEditText) view.findViewById(R.id.edCompanyName);
        final MaterialEditText edCompanyPhoneNumber = (MaterialEditText) view.findViewById(R.id.edCompanyPhoneNumber);
        final SwitchCompat scIsOwner = (SwitchCompat) view.findViewById(R.id.isOwner);
        final SwitchCompat scIsSalesman = (SwitchCompat) view.findViewById(R.id.isSalesman);
        final DotProgressBar dot_progress_bar = (DotProgressBar) view.findViewById(R.id.dot_progress_bar);
        final TextView txtErrorMessage = (TextView) view.findViewById(R.id.txtErrorMessage);
        edCompanyName.setText(Helper.getStringPreference(mContext, User.Fields.COMPANY_NAME, ""));
        edCompanyPhoneNumber.setText(Helper.getStringPreference(mContext, User.Fields.COMPANY_PHONE, ""));
        if (Helper.getStringPreference(mContext, User.Fields.IS_OWNER, "").equals("Y"))
            scIsOwner.setChecked(true);
        else
            scIsOwner.setChecked(false);
        if (Helper.getStringPreference(mContext, User.Fields.IS_SALES_MAN, "").equals("Y"))
            scIsSalesman.setChecked(true);
        else
            scIsSalesman.setChecked(false);
        ImageButton btnClose = (ImageButton) view.findViewById(R.id.btnClose);
        Button btnSave = (Button) view.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTask() {
                    boolean isDataEnteredProper = true;
                    String[] data;
                    ServerResponse sr = null;
                    User objUser;

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        if (Helper.isFieldBlank(edCompanyName.getText().toString())) {
                            isDataEnteredProper = false;
                            edCompanyName.setError(getString(R.string.msgEnterCompanyName));
                            Helper.requestFocus(ac, edCompanyName);
                        } else if (Helper.isFieldBlank(edCompanyPhoneNumber.getText().toString())) {
                            isDataEnteredProper = false;
                            edCompanyPhoneNumber.setError(getString(R.string.msgEnterCompanyPhoneNumber));
                            Helper.requestFocus(ac, edCompanyPhoneNumber);
                        }
                        if (isDataEnteredProper) {
                            dot_progress_bar.setVisibility(View.VISIBLE);
                            String strEmail = Helper.getStringPreference(mContext, User.Fields.EMAILID, "");
                            String isOwner = "", isSalesMan = "";
                            if (scIsSalesman.isChecked())
                                isSalesMan = "Y";
                            else
                                isSalesMan = "N";

                            if (scIsOwner.isChecked())
                                isOwner = "Y";
                            else
                                isOwner = "N";
                            String token = Helper.getStringPreference(mContext, User.Fields.TOKEN, "");
                            data = new String[]{strEmail, edCompanyName.getText().toString(),
                                    edCompanyPhoneNumber.getText().toString(), isOwner, isSalesMan, token};
                            objUser = new User(strEmail, edCompanyName.getText().toString(),
                                    edCompanyPhoneNumber.getText().toString(), isOwner, isSalesMan);
                        }
                    }

                    @Override
                    protected Object doInBackground(Object[] params) {
                        if (isDataEnteredProper) {
                            URLMapping umRegister = ConstantVal.updateCompanyDetail();
                            HttpEngine objHttpEngine = new HttpEngine();
                            sr = objHttpEngine.getDataFromWebAPI(mContext, umRegister.getUrl(), umRegister.getParamNames(), data);
                            objUser.saveCompanyDetailToPreference(mContext);
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        super.onPostExecute(o);
                        dot_progress_bar.clearAnimation();
                        dot_progress_bar.setVisibility(View.GONE);
                        if (isDataEnteredProper) {
                            if (sr.getResponseCode().equals(ConstantVal.ServerResponseCode.SUCCESS)) {
                                dialog.dismiss();
                            } else {
                                txtErrorMessage.setVisibility(View.VISIBLE);
                                txtErrorMessage.setText(ConstantVal.ServerResponseCode.getMessage(mContext, sr.getResponseCode()));
                            }
                        }
                    }
                }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });

        Button btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
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

    private void editCompanyAddress() {
        LayoutInflater infalInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final Dialog dialog = new Dialog(mContext);
        View view = infalInflater.inflate(R.layout.dlg_edit_company_address, null, true);
        final MaterialEditText edCompanyHouseNo = (MaterialEditText) view.findViewById(R.id.edCompanyHouseNo);
        final MaterialEditText edCompanyStreet = (MaterialEditText) view.findViewById(R.id.edCompanyStreet);
        final MaterialEditText edCompanyLandMark = (MaterialEditText) view.findViewById(R.id.edCompanyLandMark);
        final MaterialEditText edCompanyCity = (MaterialEditText) view.findViewById(R.id.edCompanyCity);
        final MaterialEditText edCompanyPostCode = (MaterialEditText) view.findViewById(R.id.edCompanyPostCode);
        final Spinner spnCompanyState = (Spinner) view.findViewById(R.id.spnCompanyState);
        final DotProgressBar dot_progress_bar = (DotProgressBar) view.findViewById(R.id.dot_progress_bar);
        final TextView txtErrorMessage = (TextView) view.findViewById(R.id.txtErrorMessage);
        edCompanyHouseNo.setText(Helper.getStringPreference(mContext, User.Fields.COMPANY_HOUSE_NO, ""));
        edCompanyStreet.setText(Helper.getStringPreference(mContext, User.Fields.COMPANY_STREET, ""));
        edCompanyLandMark.setText(Helper.getStringPreference(mContext, User.Fields.COMPANY_LANDMARK, ""));
        edCompanyCity.setText(Helper.getStringPreference(mContext, User.Fields.COMPANY_CITY, ""));

        ArrayAdapter<String> adpState = new ArrayAdapter<String>(mContext, R.layout.spinner_item_no_padding, mContext.getResources().getStringArray(R.array.arrState));
        adpState.setDropDownViewResource(R.layout.spinner_item);
        spnCompanyState.setAdapter(adpState);
        int spinnerPosition = adpState.getPosition(Helper.getStringPreference(mContext, User.Fields.COMPANY_STATE, ""));//getStatePosition(Helper.getStringPreference(mContext, User.Fields.COMPANY_STATE, ""));
        spnCompanyState.setSelection(spinnerPosition);

        edCompanyPostCode.setText(Helper.getStringPreference(mContext, User.Fields.COMPANY_POST_CODE, ""));
        ImageButton btnClose = (ImageButton) view.findViewById(R.id.btnClose);
        Button btnSave = (Button) view.findViewById(R.id.btnSave);
        Button btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTask() {
                    boolean isDataEnteredProper = true;
                    String[] data;
                    ServerResponse sr = null;
                    User objUser;

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        if (Helper.isFieldBlank(edCompanyHouseNo.getText().toString())) {
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
                            dot_progress_bar.setVisibility(View.VISIBLE);
                            String strEmail = Helper.getStringPreference(mContext, User.Fields.EMAILID, "");
                            String token = Helper.getStringPreference(mContext, User.Fields.TOKEN, "");
                            data = new String[]{strEmail, edCompanyHouseNo.getText().toString(), edCompanyStreet.getText().toString(),
                                    edCompanyLandMark.getText().toString(), edCompanyCity.getText().toString(),
                                    spnCompanyState.getSelectedItem().toString(), "India", edCompanyPostCode.getText().toString(), token};
                            objUser = new User(edCompanyHouseNo.getText().toString(), edCompanyStreet.getText().toString(),
                                    edCompanyLandMark.getText().toString(), edCompanyPostCode.getText().toString(),
                                    edCompanyCity.getText().toString(), spnCompanyState.getSelectedItem().toString(), "India");
                        }
                    }

                    @Override
                    protected Object doInBackground(Object[] params) {
                        if (isDataEnteredProper) {
                            URLMapping umRegister = ConstantVal.updateCompanyAddress();
                            HttpEngine objHttpEngine = new HttpEngine();
                            sr = objHttpEngine.getDataFromWebAPI(mContext, umRegister.getUrl(), umRegister.getParamNames(), data);
                            if (sr.getResponseCode().equals(ConstantVal.ServerResponseCode.SUCCESS)) {
                                objUser.saveCompanyAddressToPreference(mContext);
                            }
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        super.onPostExecute(o);
                        dot_progress_bar.clearAnimation();
                        dot_progress_bar.setVisibility(View.GONE);
                        if (isDataEnteredProper) {
                            if (sr.getResponseCode().equals(ConstantVal.ServerResponseCode.SUCCESS)) {
                                dialog.dismiss();
                            } else {
                                txtErrorMessage.setVisibility(View.VISIBLE);
                                txtErrorMessage.setText(ConstantVal.ServerResponseCode.getMessage(mContext, sr.getResponseCode()));
                            }
                        }
                    }
                }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });
        dialog.setCancelable(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog.setContentView(view);
        dialog.show();
    }

    private void logoutUser() {
        final ConfimationSnackbar snackbar = new ConfimationSnackbar(ac, ConstantVal.ToastBGColor.WARNING);
        snackbar.showSnackBar(ac.getString(R.string.msgLogoutConfirmation), ac.getString(R.string.strLogout), ac.getString(R.string.strCancel), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //expire the token of server
                new AsyncTask() {
                    ServerResponse sr;

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                    }

                    @Override
                    protected Object doInBackground(Object[] params) {
                        String token = Helper.getStringPreference(mContext, User.Fields.TOKEN, "");
                        String userId = String.valueOf(Helper.getIntPreference(mContext, User.Fields.ID, 0));
                        URLMapping objURLMapping = ConstantVal.logoutUser();
                        HttpEngine objHttpEngine = new HttpEngine();
                        sr = objHttpEngine.getDataFromWebAPI(ac, objURLMapping.getUrl(), objURLMapping.getParamNames(), new String[]{userId, token});
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        super.onPostExecute(o);
                        snackbar.dismissSnackBar();
                        if (sr.getResponseCode().equals(ConstantVal.ServerResponseCode.SUCCESS)) {
                            logOutUser();
                        } else {
                            Helper.displaySnackbar(ac, ConstantVal.ServerResponseCode.getMessage(mContext, sr.getResponseCode()), ConstantVal.ToastBGColor.INFO);
                        }
                    }
                }.execute();
            }
        }, null);
    }

    private void logOutUser() {
        User.clearCache(mContext);
        DataBase db = new DataBase(mContext);
        db.open();
        db.cleanAll();
        db.close();
        Intent i = new Intent(ac, acLogin.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        ac.startActivity(i);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Helper.clearAllTable(mContext);
            finish();
        }
        return false;
    }
}
