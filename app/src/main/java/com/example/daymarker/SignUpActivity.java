package com.example.daymarker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button btn_sendOtp,btn_signUp;
    private EditText edt_name,edt_phone,edt_Otp;
    private String verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth=FirebaseAuth.getInstance();

        btn_sendOtp=findViewById(R.id.btn_sendOtp);
        btn_signUp=findViewById(R.id.btn_signUp2);
        edt_name=findViewById(R.id.edt_name);
        edt_Otp=findViewById(R.id.edt_otp);
        edt_phone=findViewById(R.id.edt_phone);

        btn_sendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(edt_name.getText().toString())){
                    Toast.makeText(SignUpActivity.this, "Enter a username", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(edt_phone.getText().toString())){
                    Toast.makeText(SignUpActivity.this, "Enter you phone number", Toast.LENGTH_SHORT).show();
                }
                else{
                    String phone="+91"+edt_phone.getText().toString();
                    sendVerificationCode(phone);
                }
            }
        });

        btn_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(edt_Otp.getText().toString())){
                    Toast.makeText(SignUpActivity.this, "please enter OTP", Toast.LENGTH_SHORT).show();
                }else{
                    verifyCode(edt_Otp.getText().toString());
                }
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
                        if(task.isSuccessful()){
                            String uPhone="+91"+edt_phone.getText().toString();
                            String uName=edt_name.getText().toString();
                            FirebaseUser user= mAuth.getCurrentUser();
                            String uid=user.getUid();
                            HashMap<String,Object> hashMap=new HashMap<>();
                            hashMap.put("phone",uPhone);
                            hashMap.put("uid",uid);
                            hashMap.put("userName",uName);

                            FirebaseFirestore db=FirebaseFirestore.getInstance();
                            db.collection("Users").document(uid).set(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull  Task<Void> task) {
                                    if (task.isSuccessful())
                                        Toast.makeText(SignUpActivity.this, "added", Toast.LENGTH_SHORT).show();
                                    else
                                        Toast.makeText(SignUpActivity.this, "not added", Toast.LENGTH_SHORT).show();
                                }
                            });

                            Intent i=new Intent(SignUpActivity.this,HomeActivity.class);
                            startActivity(i);
                            finish();
                        }else{
                            Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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
            Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    };
}