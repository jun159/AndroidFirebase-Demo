package sg.edu.nus.baojun.facebookfirebase_demo.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

import sg.edu.nus.baojun.facebookfirebase_demo.R;

public class LoginActivity extends FirebaseBaseActivity implements View.OnClickListener {

    public static final String KEY_EMAIL = "EMAIL";

    private static final String TAG = LoginActivity.class.getCanonicalName();

    private CallbackManager callbackManager;
    private FirebaseAuth auth;

    private EditText editEmail;
    private EditText editPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize Facebook Login button
        callbackManager = CallbackManager.Factory.create();
        auth = FirebaseAuth.getInstance();

        TextView loginFacebookButton = findViewById(R.id.button_facebook_login);
        TextView loginEmailButton = findViewById(R.id.button_email_login);
        LinearLayout registerButton = findViewById(R.id.button_register);
        editEmail = findViewById(R.id.edit_email);
        editPassword = findViewById(R.id.edit_password);

        loginFacebookButton.setOnClickListener(this);
        loginEmailButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Callback manager asks for result from the new Facebook activity
        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_email_login : {
                String textEmailAddress = editEmail.getText().toString();
                String textPassword = editPassword.getText().toString();

                if(!textEmailAddress.isEmpty() && !textPassword.isEmpty()) {
                    final ProgressDialog progressDialog = ProgressDialog.show(this,
                            "Please wait...", "Processing...", true);
                    auth.signInWithEmailAndPassword(textEmailAddress, textPassword)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressDialog.dismiss();

                                    if (task.isSuccessful()) {
                                        updateUI();
                                    } else {
                                        Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                } else {
                    Snackbar snackbar = Snackbar.make(editEmail, "Please do not leave the fields empty.", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }

                break;
            }

            case R.id.button_facebook_login : {
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this,
                        Arrays.asList("email", "public_profile"));
                LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
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

                break;
            }

            case R.id.button_register : {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
                break;
            }

            default: {
                break;
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = auth.getCurrentUser();

        // TODO
//        if(currentUser != null) {
//            updateUI();
//        }
    }

    private void updateUI() {
        Snackbar snackbar = Snackbar.make(editEmail, "You have logged in successfully.", Snackbar.LENGTH_LONG);
        snackbar.show();

        Intent intent = new Intent(LoginActivity.this, UserActivity.class);
        startActivity(intent);
        finish();
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        // Sign in with Facebook with token retrieved from Facebook
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = auth.getCurrentUser();
                            updateUI();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
