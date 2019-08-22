package com.caiusf.ratemydriving.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.caiusf.ratemydriving.R;
import com.caiusf.ratemydriving.utils.toast.ToastDisplayer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class NewPasswordActivity extends AppCompatActivity {

    EditText emailField;
    Button btnNewPass;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        emailField = (EditText) findViewById(R.id.resetPasswordEmailField);
        btnNewPass = (Button) findViewById(R.id.resetPasswordButton);

        firebaseAuth = FirebaseAuth.getInstance();

        btnNewPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = emailField.getText().toString();

                if(TextUtils.isEmpty(email)){
                    ToastDisplayer.displayShortToast(getApplicationContext(), getResources().getString(R.string.resetPassword_pleaseFillEmail));
                    return;
                }

                firebaseAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    ToastDisplayer.displayLongToast(getApplicationContext(), getResources().getString(R.string.resetPassword_successful) + email);
                                    finish();
                                }
                                else{
                                    ToastDisplayer.displayShortToast(getApplicationContext(), getResources().getString(R.string.resetPassword_fail));
                                }
                            }
                        });
            }
        });


    }
}
