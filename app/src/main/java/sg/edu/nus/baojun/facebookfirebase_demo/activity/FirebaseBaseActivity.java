package sg.edu.nus.baojun.facebookfirebase_demo.activity;

import android.support.v7.app.AppCompatActivity;

import sg.edu.nus.baojun.facebookfirebase_demo.FirebaseApplication;

/**
 * Created by BAOJUN on 12/22/17.
 */

public class FirebaseBaseActivity extends AppCompatActivity {

    public FirebaseApplication getFirebaseApplication() {
        return (FirebaseApplication) getApplication();
    }
}