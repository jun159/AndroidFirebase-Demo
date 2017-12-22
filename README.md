# FacebookFirebase-Demo
Android Facebook Login with Firebase

## Step 1 - Connect App with Firebase
1. In Android Studio, go to `Tools` -> `Firebase`.
2. Go to `Authentication` -> `Email and password authentication`. 
3. Connect app to Firebase by clicking `Connect to Firebase` button and once dialog opens, click on the blue button below.
4. Once app is connected, add Firebase Authentication to the app.

## Step 2 - Enable Facebook Login
1. Go to [Facebook for Developers](https://developers.facebook.com/).
2. On top right hand corner, go to `Create App` button and enter the App name as the display name and your own contact email.
3. Go to Facebook console page and go to `Dashboard` to retrieve `App ID` and `App Secret`.
4. Go to Firebase console page and go to `Authentication` -> `Sign-in Method` and enable Facebook login. Enter the `App ID` and `App Secret` retrieved from Facebook console page. Copy the OAuth redirect URI as well.
5. Go back to Facebook console page and click on `Add Product`-> under Facebook, click `Setup`. Paste the OAuth redirect URI under the textbox for `Valid OAuth redirect URIs`.

## Step 3 - Setup in Android Studio
1. Go to [Facebook Login](https://developers.facebook.com/docs/facebook-login/android).
2. Select your app in step 1.
3. Link the Facebook SDK by selecting `Maven`. Add the following code in `build.gradle(Project)`
```
buildscript {
    
    repositories {
        maven { url 'https://maven.google.com' }
        mavenCentral()
        jcenter()
    }
    dependencies {
        // Eliminated
    }
}
```
4. Add the following dependencies in `build.gradle(Module)`
```
implementation 'com.facebook.android:facebook-login:[4,5)'
```
5. Add the following in `strings.xml`:
```
<string name="facebook_app_id">201486427066708</string>
<string name="fb_login_protocol_scheme">fb201486427066708</string>    
```
6. In your Manifest, add the following permission:
```
<uses-permission android:name="android.permission.INTERNET" />
```
Also add the following in your application body:
```
<meta-data
    android:name="com.facebook.sdk.ApplicationId"
    android:value="@string/facebook_app_id" />
    
<activity
    android:name="com.facebook.FacebookActivity"
    android:configChanges="keyboard|keyboardHidden|screenLayout|screenSize|orientation"
    android:label="@string/app_name" />
<activity
    android:name="com.facebook.CustomTabActivity"
    android:exported="true">
    <intent-filter>
        <action android:name="android.intent.action.VIEW" />

        <category android:name="android.intent.category.DEFAULT" />
        <category android:name="android.intent.category.BROWSABLE" />

        <data android:scheme="@string/fb_login_protocol_scheme" />
    </intent-filter>
</activity>
```
7. Enter Android package name and default class in [Facebook Login](https://developers.facebook.com/docs/facebook-login/android). Click `Use this Package Name` if package does not exist in Google Play.
8. Retrieve key hashes for the App by running following command on terminal (Note that password is your Facebook account password):
```
keytool -exportcert -alias androiddebugkey -keystore ~/.android/debug.keystore | openssl sha1 -binary | openssl base64
```
Enter the generated key hash in [Facebook Login](https://developers.facebook.com/docs/facebook-login/android). 

## Step 4 - Android Code for Facebook Authentication
1. Add Facebook Login Button in xml layout file:
```
<Button
    android:id="@+id/login_button"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_gravity="center_horizontal"
    android:layout_marginTop="30dp"
    android:layout_marginBottom="30dp" /> 
```
2. Add the following Java code in LoginActivity:
```
public class LoginActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getCanonicalName();

    private CallbackManager mCallbackManager;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        mAuth = FirebaseAuth.getInstance();

        final Button loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(MainActivity.this,
                        Arrays.asList("email", "public_profile"));
                LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d(TAG, "facebook:onSuccess:" + loginResult);
                        // Pass in token result from facebook and sign into Firebase
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        Log.d(TAG, "facebook:onCancel");
                        // ...
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d(TAG, "facebook:onError", error);
                        // ...
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Callback manager asks for result from the new Facebook activity
        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null) {
            updateUI();
        }
    }

    private void updateUI() {
        Toast.makeText(this, "You're logged in", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(MainActivity.this, AccountActivity.class);
        startActivity(intent);
        finish();
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        // Sign in with Facebook with token retrieved from Facebook
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
```
3. Add the following Java code in LogoutActivity:
```
public class LogoutActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        mAuth = FirebaseAuth.getInstance();
        
        Button logoutButton = (Button) findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Logout Firebase
                mAuth.signOut();
                // Logout Facebook
                LoginManager.getInstance().logOut();
                updateUI();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser == null) {
            updateUI();
        }
    }

    private void updateUI() {
        Intent intent = new Intent(AccountActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
```
