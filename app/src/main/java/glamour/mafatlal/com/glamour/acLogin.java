package glamour.mafatlal.com.glamour;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import entity.Category;
import entity.User;
import me.zhanghai.android.materialedittext.MaterialEditText;
import utility.ConstantVal;
import utility.DateTimeUtils;
import utility.DotProgressBar;
import utility.Helper;
import utility.HttpEngine;
import utility.Logger;
import utility.ServerResponse;
import utility.URLMapping;

import static java.security.AccessController.getContext;

public class acLogin extends AppCompatActivity implements View.OnClickListener {
    Button btnRegister, btnForgotPassword, btnLogin;
    MaterialEditText edEmailId, edPassword;
    Context mContext;
    AppCompatActivity ac;
    Handler handler = new Handler();
    DotProgressBar dot_progress_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        ac = this;
        Helper.startFabric(mContext);
        setContentView(R.layout.ac_login);
        dot_progress_bar = (DotProgressBar) findViewById(R.id.dot_progress_bar);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnForgotPassword = (Button) findViewById(R.id.btnForgotPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        edEmailId = (MaterialEditText) findViewById(R.id.edEmailId);
        edPassword = (MaterialEditText) findViewById(R.id.edPassword);
        btnForgotPassword.setPaintFlags(btnForgotPassword.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        btnRegister.setPaintFlags(btnRegister.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        btnForgotPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                loginUser();
                break;
            case R.id.btnForgotPassword:
                forgotPassword();
                break;
            case R.id.btnRegister:
                Intent i = new Intent(getApplicationContext(), acRegistration.class);
                startActivity(i);
                break;
        }
    }

    private void loginUser() {
        boolean isDataEntedProperly = true;
        if (Helper.isFieldBlank(edEmailId.getText().toString())) {
            edEmailId.setError(getString(R.string.strEnterEmailId));
            Helper.requestFocus(ac, edEmailId);
            isDataEntedProperly = false;
        } else if (!Helper.isValidEmailId(edEmailId.getText().toString())) {
            edEmailId.setError(getString(R.string.strEnterValidEmailId));
            Helper.requestFocus(ac, edEmailId);
            isDataEntedProperly = false;
        } else if (Helper.isFieldBlank(edPassword.getText().toString())) {
            edPassword.setError(getString(R.string.msgEnterPassword));
            Helper.requestFocus(ac, edPassword);
            isDataEntedProperly = false;
        }
        if (isDataEntedProperly) {
            btnLogin.setEnabled(false);
            final HttpEngine objHttpEngine = new HttpEngine();

            final String android_id = Settings.Secure.getString(mContext.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            final String deviceName = Build.DEVICE + " " + Build.MODEL;
            final String deviceVersion = Build.VERSION.RELEASE + "(" + Build.VERSION.SDK_INT + ")";
            final String emaiID = edEmailId.getText().toString();
            final String strPassword = edPassword.getText().toString();
            dot_progress_bar.setVisibility(View.VISIBLE);
            new Thread() {
                public void run() {
                    URLMapping um = ConstantVal.customerCredentialVerification();
                    ServerResponse objServerResponse = objHttpEngine.getDataFromWebAPI(mContext, um.getUrl(),
                            um.getParamNames(), new String[]{emaiID, strPassword, android_id, deviceName, deviceVersion, DateTimeUtils.getDate(new Date()), DateTimeUtils.getTime(new Date())});
                    String result = Html.fromHtml(objServerResponse.getResponseString()).toString();
                    if (result != null && !result.equals("")) {
                        try {
                            User objUser = new User();
                            objUser.parseJSON(new JSONObject(result));
                            if (objUser != null && !objUser.getToken().equals("")) {
                                objUser.saveFiledsToPreferences(mContext);
                                try {
                                    Category.loadCategoryFromServer(mContext).join();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Logger.writeToCrashlytics(e);
                                }
                                Intent i1 = new Intent(mContext, acHome.class);
                                startActivity(i1);
                                finish();
                            }
                        } catch (JSONException e) {
                            Helper.displaySnackbar(ac, result, ConstantVal.ToastBGColor.DANGER);
                            Logger.writeToCrashlytics(e);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    btnLogin.setEnabled(true);
                                }
                            });
                        }
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //pd.dismiss();
                            dot_progress_bar.clearAnimation();
                            dot_progress_bar.setVisibility(View.GONE);
                        }
                    });
                }
            }.start();

        }

    }

    private void forgotPassword() {
        LayoutInflater infalInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final Dialog dialog = new Dialog(mContext);
        View view1 = infalInflater.inflate(R.layout.dlg_forgot_password, null, true);
        final LinearLayout lyInput = (LinearLayout) view1.findViewById(R.id.lyInput);
        lyInput.setVisibility(View.VISIBLE);
        final TextView msgError = (TextView) view1.findViewById(R.id.msgError);
        final LinearLayout lyConfirmation = (LinearLayout) view1.findViewById(R.id.lyConfirmation);
        ImageButton btnClose = (ImageButton) view1.findViewById(R.id.btnClose);
        Button btnDone = (Button) view1.findViewById(R.id.btnDone);
        Button btnSendNewPassword = (Button) view1.findViewById(R.id.btnSendNewPassword);
        final DotProgressBar dot_progress_bar = (DotProgressBar) view1.findViewById(R.id.dot_progress_bar);
        final MaterialEditText edUserName = (MaterialEditText) view1.findViewById(R.id.edUserName);
        edUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                msgError.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btnSendNewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendPassoword(dialog, msgError, edUserName, lyInput, lyConfirmation, dot_progress_bar);
            }
        });
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setCancelable(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog.setContentView(view1);
        dialog.show();
    }

    private void sendPassoword(final Dialog d, final TextView msgError, final MaterialEditText edUserName, final LinearLayout lyInput, final LinearLayout lyConfirmation, final DotProgressBar dot_progress_bar) {
        new AsyncTask() {
            boolean isDataEnteredProper = true;
            String email, newPassword;
            ServerResponse sr;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                email = edUserName.getText().toString();
                if (Helper.isFieldBlank(email)) {
                    isDataEnteredProper = false;
                    edUserName.setError(getString(R.string.strEnterEmailId));
                    Helper.requestFocus(ac, edUserName);
                } else if (!Helper.isValidEmailId(email)) {
                    isDataEnteredProper = false;
                    edUserName.setError(getString(R.string.strEnterValidEmailId));
                    Helper.requestFocus(ac, edUserName);
                }
                if (isDataEnteredProper) {
                    dot_progress_bar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            protected Object doInBackground(Object[] params) {
                if (isDataEnteredProper) {
                    URLMapping umForgotPassword = ConstantVal.resetPassword();
                    HttpEngine objHttpEngine = new HttpEngine();
                    String[] data = {email, DateTimeUtils.getDate(new Date()), DateTimeUtils.getTime(new Date())};
                    sr = objHttpEngine.getDataFromWebAPI(mContext, umForgotPassword.getUrl(), umForgotPassword.getParamNames(), data);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                dot_progress_bar.clearAnimation();
                dot_progress_bar.setVisibility(View.GONE);
                if (isDataEnteredProper) {
                    if (sr.getResponseCode().equals(ConstantVal.ServerResponseCode.INVALID_LOGIN)) {
                        msgError.setVisibility(View.VISIBLE);
                        msgError.setText(mContext.getString(R.string.strInvalidUserName));
                    } else if (sr.getResponseCode().equals(ConstantVal.ServerResponseCode.SUCCESS)) {
                        try {
                            JSONObject objJSON = new JSONObject(sr.getResponseString());
                            newPassword = objJSON.getString("password");
                            lyConfirmation.setVisibility(View.VISIBLE);
                            lyInput.setVisibility(View.GONE);
                        } catch (Exception e) {
                            Logger.writeToCrashlytics(e);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    msgError.setVisibility(View.VISIBLE);
                                    msgError.setText(ConstantVal.ServerResponseCode.getMessage(ac, sr.getResponseCode()));
                                }
                            });
                        }
                    } else {
                        msgError.setVisibility(View.VISIBLE);
                        msgError.setText(ConstantVal.ServerResponseCode.getMessage(ac, sr.getResponseCode()));
                    }
                }
            }
        }.execute();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return false;
    }

}
