package com.creativemonkeyz.robotzi;

import android.content.Intent;
import android.os.Bundle;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.bugsense.trace.BugSenseHandler;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.PushService;
import de.keyboardsurfer.android.widget.crouton.Crouton;

public class MainActivity extends SherlockFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        BugSenseHandler.initAndStartSession(getApplicationContext(), "bac2edd2");

        Parse.initialize(this, "MQUbUNm75g6r6Xw3EE8GGpYeOwImP9fFc7HD5bcn", "SqkkRPf5tDiqlIOLGbibxowVBt4V7hEr7GQG2W51");
        PushService.setDefaultPushCallback(this, MainActivity.class);
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }

    @Override
    protected void onStart() {
        super.onStart();
        BugSenseHandler.startSession(getApplicationContext());
    }

    @Override
    protected void onStop() {
        super.onStop();
        BugSenseHandler.closeSession(getApplicationContext());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Crouton.cancelAllCroutons();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.main, menu);
        return true;

        // TODO: Search episodes
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_rate:
                RateMeMaybe rateDialog = new RateMeMaybe(MainActivity.this);
                rateDialog.setAdditionalListener(new RateMeMaybe.OnRMMUserChoiceListener() {
                    @Override
                    public void handlePositive() {
                        Utils.launchPlayStoreAppPage(MainActivity.this);
                    }

                    @Override
                    public void handleNeutral() {}

                    @Override
                    public void handleNegative() {}
                });

                rateDialog.forceShow();
                return true;

            case R.id.action_settings:
                startActivity(new Intent(this, Settings.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}