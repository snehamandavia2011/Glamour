package glamour.mafatlal.com.glamour;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import utility.Helper;
import utility.TabManager;

public class acProfile extends AppCompatActivity {
    Helper objHelper = new Helper();
    Context mContext;
    AppCompatActivity ac;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        ac = this;
        Helper.startFabric(mContext);
        setContentView(R.layout.ac_profile);
        objHelper.setActionBar(this, getString(R.string.strProfile), false);
        TabManager.setCurrentSelection(TabManager.PROFILE, ac);

    }
}
