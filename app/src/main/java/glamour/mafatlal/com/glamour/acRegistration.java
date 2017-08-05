package glamour.mafatlal.com.glamour;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;

import me.zhanghai.android.materialedittext.MaterialEditText;
import utility.Helper;

public class acRegistration extends AppCompatActivity {
    Helper objHelper = new Helper();
    Context mContext;
    MaterialEditText edFirstName, edLastName, edEmailId, edPassword, edMobileNumber, edCompanyName, edCompanyPhoneNumber, edComapnyHouseNo, edComapnyStreet, edCompanyLandMark, edCompanyCity, edCompanyPostCode;
    Spinner spnCompanyState;
    RadioButton rdRetailer, rdDealer;
    Button btnCancel, btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        Helper.startFabric(mContext);
        setContentView(R.layout.ac_registration);
        objHelper.setActionBar(this, getString(R.string.strRegistration));
    }

}
