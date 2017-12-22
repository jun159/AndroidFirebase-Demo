package sg.edu.nus.baojun.facebookfirebase_demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import sg.edu.nus.baojun.facebookfirebase_demo.R;

public class UserActivity extends FirebaseBaseActivity implements View.OnClickListener {

    private FirebaseAuth auth;
    private FirebaseAnalytics analytics;

    private TextView logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        auth = FirebaseAuth.getInstance();
        analytics = FirebaseAnalytics.getInstance(this);

        logoutButton = findViewById(R.id.button_logout);
        TextView clickMeButton = findViewById(R.id.button_click_me);
        TextView doNotClickMeButton = findViewById(R.id.button_do_not_click_me);

        logoutButton.setOnClickListener(this);
        clickMeButton.setOnClickListener(this);
        doNotClickMeButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_logout : {
                // Logout Firebase
                auth.signOut();
                // Logout Facebook
                LoginManager.getInstance().logOut();
                updateUI();

                break;
            }

            case R.id.button_click_me : {
                // Log button click
                Snackbar snackbar = Snackbar.make(logoutButton, "Click Me triggered", Snackbar.LENGTH_LONG);
                snackbar.show();

                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "button_click_me");
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Click Me!");
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Button");
                analytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
                break;
            }

            case R.id.button_do_not_click_me : {
                // Log button click
                Snackbar snackbar = Snackbar.make(logoutButton, "Do NOT Click Me triggered", Snackbar.LENGTH_LONG);
                snackbar.show();

                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "button_do_not_click_me");
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Do NOT Click Me!");
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Button");
                analytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
                break;
            }

            default: {
                break;
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = auth.getCurrentUser();

        if(currentUser == null) {
            updateUI();
        }
    }

    private void updateUI() {
        Intent intent = new Intent(UserActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
