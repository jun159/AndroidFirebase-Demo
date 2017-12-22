package sg.edu.nus.baojun.facebookfirebase_demo;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by BAOJUN on 12/22/17.
 */

public class FirebaseApplication extends Application {

    private static final String CURRENT_USER_KEY = "sg.edu.nus.baojun.CURRENT_USER_KEY";
    private static final String DEFAULT = "DEFAULT";

    private SharedPreferences prefs;

    @Override
    public void onCreate() {
        super.onCreate();
        prefs = this.getSharedPreferences("sg.edu.nus.baojun", Context.MODE_PRIVATE);
    }

//    public User getCurrentUser() {
//        Gson gson = new Gson();
//        String userString = prefs.getString(CURRENT_USER_KEY, DEFAULT);
//
//        if(!userString.equals(DEFAULT)) {
//            currentUser = gson.fromJson(userString, User.class);
//        }
//
//        return currentUser;
//    }
//
//    public void setCurrentUser(User currentUser) {
//        this.currentUser = currentUser;
//        Gson gson = new Gson();
//        prefs.edit().putString(CURRENT_USER_KEY, gson.toJson(currentUser)).apply();
//    }
//
//    public void signOut() {
//        prefs.edit().remove(CURRENT_USER_KEY).apply();
//        currentUser = null;
//    }
}