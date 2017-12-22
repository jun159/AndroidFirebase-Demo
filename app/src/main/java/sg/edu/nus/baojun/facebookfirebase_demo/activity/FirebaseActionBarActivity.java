package sg.edu.nus.baojun.facebookfirebase_demo.activity;

import android.view.MenuItem;

import sg.edu.nus.baojun.facebookfirebase_demo.R;

/**
 * Created by BAOJUN on 12/22/17.
 */

public class FirebaseActionBarActivity extends FirebaseBaseActivity {
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_from_right);
    }
}