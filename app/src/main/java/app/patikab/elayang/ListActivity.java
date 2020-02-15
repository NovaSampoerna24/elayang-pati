package app.patikab.elayang;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import app.patikab.elayang.fragment.DisInFragment;
import app.patikab.elayang.fragment.DisOutFragment;
import app.patikab.elayang.fragment.InboxFragment;
import app.patikab.elayang.fragment.KonsepFragment;
import app.patikab.elayang.fragment.OutboxFragment;
import app.patikab.elayang.fragment.TteFragment;
import app.patikab.elayang.util.Config;

public class ListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        changeFragment(getIntent().getStringExtra(Config.INTENT_TYPE));
    }

    private void changeFragment(String type) {
        Fragment fragment = null;
        setTitle(type);
        if (type.equals(Config.INBOX)) {
            fragment = new InboxFragment();
        } else if (type.equals(Config.DISIN)) {
            fragment = new DisInFragment();
        } else if (type.equals(Config.DISOUT)) {
            fragment = new DisOutFragment();
        } else if (type.equals(Config.KONSEP)) {
            fragment = new KonsepFragment();
        } else if (type.equals(Config.OUTBOX)) {
            fragment = new OutboxFragment();
        }else if(type.equals(Config.TTE)){
            fragment = new TteFragment();
            Log.d("test","tte fragment");
        }

        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commitNow();
        }
    }


}
