package com.example.daymarker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button btn_sendOtp,btn_login,btn_signUp;
    private EditText edt_phone,edt_Otp;
    private String verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth=FirebaseAuth.getInstance();

        btn_sendOtp=findViewById(R.id.btn_signUp_sendOtp);
        btn_login=findViewById(R.id.btn_signUp_login);
        edt_Otp=findViewById(R.id.edt_signUp_otp);
        edt_phone=findViewById(R.id.edt_signUp_phone);

        btn_sendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),0);
                if(TextUtils.isEmpty(edt_phone.getText().toString())){
                    Toast.makeText(LoginActivity.this, "Enter you phone number", Toast.LENGTH_SHORT).show();
                }
                else{
                    String phone="+91"+edt_phone.getText().toString();
                    sendVerificationCode(phone);
                }
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),0);
                if(TextUtils.isEmpty(edt_Otp.getText().toString())){
                    Toast.makeText(LoginActivity.this, "please enter OTP", Toast.LENGTH_SHORT).show();
                }else{
                    verifyCode(edt_Otp.getText().toString());
                }
            }
        });

        btn_signUp=findViewById(R.id.btn_signUp_signup);
        btn_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    private void verifyCode(String code) {
        PhoneAuthCredential credential= PhoneAuthProvider.getCredential(verificationId,code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Intent i=new Intent(LoginActivity.this,HomeActivity.class);
                            startActivity(i);
                            finish();
                        }else{
                            Log.d("output",task.getException().getMessage());
                        }
                    }
                });
    }
    private void sendVerificationCode(String phone) {
        PhoneAuthOptions options=PhoneAuthOptions.newBuilder(mAuth).setPhoneNumber(phone)
                .setTimeout(60L, TimeUnit.SECONDS).setActivity(this)
                .setCallbacks(mCallBack).build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    private  PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId=s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            final String code=phoneAuthCredential.getSmsCode();
            if(code!=null){
                edt_Otp.setText(code);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
//            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("output",e.getMessage());
        }
    };
}