
package glamour.mafatlal.com.glamour;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import entity.User;
import utility.ConstantVal;
import utility.Helper;
import utility.Logger;


public class acSplash extends Activity {
    ImageView imgLogo;
    Context mContext;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        Helper.startFabric(mContext);
        setContentView(R.layout.ac_splash);
        imgLogo = (ImageView) findViewById(R.id.imgLogo);
        new Thread() {
            public void run() {
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Logger.writeToCrashlytics(e);
                }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            final String tokenId = Helper.getStringPreference(mContext, User.Fields.TOKEN, "");
                            if (tokenId.equals("")) {
                                Intent i = new Intent(mContext, acLogin.class);
                                startActivity(i);
                                finish();
                            }else{
                                Intent i = new Intent(mContext, acHome.class);
                                startActivity(i);
                                finish();
                            }
                        }
                    });
                }
            }.start();
    }
}


