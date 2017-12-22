package sg.edu.nus.baojun.facebookfirebase_demo.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import sg.edu.nus.baojun.facebookfirebase_demo.R;

public class RegistrationActivity extends FirebaseBaseActivity implements View.OnClickListener {

    private FirebaseAuth auth;
    private EditText editEmail;
    private EditText editPassword;

    private String textEmailAddress;
    private String textPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        auth = FirebaseAuth.getInstance();

        editEmail = findViewById(R.id.edit_email);
        editPassword = findViewById(R.id.edit_password);

        TextView buttonRegister = findViewById(R.id.button_register);
        buttonRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_register : {
                textEmailAddress = editEmail.getText().toString();
                textPassword = editPassword.getText().toString();

                if(!textEmailAddress.isEmpty() && !textPassword.isEmpty()) {
                    final ProgressDialog progressDialog = ProgressDialog.show(this, "Please wait...",
                            "Processing...", true);

                    auth.createUserWithEmailAndPassword(textEmailAddress, textPassword)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressDialog.dismiss();

                                    if (task.isSuccessful()) {
                                        updateUI();
                                    } else {
                                        Toast.makeText(RegistrationActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                } else {
                    Snackbar snackbar = Snackbar.make(editEmail, "Please do not leave the fields empty.", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }

                break;
            }

            default: {
                break;
            }
        }
    }

    private void updateUI() {
        Snackbar snackbar = Snackbar.make(editEmail, "You have registered successfully.", Snackbar.LENGTH_LONG);
        snackbar.show();

        Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
        intent.putExtra(LoginActivity.KEY_EMAIL, textEmailAddress);
        startActivity(intent);
        finish();
    }
}
