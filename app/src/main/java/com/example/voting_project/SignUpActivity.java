package com.example.voting_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {

    private EditText userName, userPass, userEmail;
    private TextView haveAcc;
    private FirebaseAuth mAuth;

    public static final String PREFERENCES = "prefKey";
    public static final String NAME = "nameKey";
    public static final String EMAIL = "emailKey";
    public static final String PASSWORD = "passwordKey";

    SharedPreferences sharedPreferences;

    String name, email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        userName = findViewById(R.id.user_name);
        userPass = findViewById(R.id.user_password);
        userEmail = findViewById(R.id.user_email);
        haveAcc = findViewById(R.id.haveAccount);

        sharedPreferences = this.getApplicationContext().getSharedPreferences(PREFERENCES,MODE_PRIVATE);

        haveAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed(); //the code for going back.
            }
        });

        mAuth = FirebaseAuth.getInstance(); //get the shared instance of the FirebaseAuth object

    }

    public void SignUp(View view) {
        name= userName.getText().toString();
        email= userEmail.getText().toString();
        password= userPass.getText().toString();

        if(name.length()==0 && email.length()==0 && password.length()==0) //Check if is empty
        {
            Toast.makeText(SignUpActivity.this,"Please enter your data ",Toast.LENGTH_SHORT).show();
        }
        else
        {
            //Create a new account by passing the new user's email address and password
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(
                    new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(SignUpActivity.this,"User Created ",Toast.LENGTH_SHORT).show();
                        Log.i("TAG", "createUserWithEmail:success ");
                        verifyEmail();

                    } else {
                        Toast.makeText(SignUpActivity.this,"Fail try again ",Toast.LENGTH_SHORT).show();
                        Log.i("TAG", "createUserWithEmail:failure", task.getException());
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SignUpActivity.this,"Something went wrong ",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void verifyEmail() {
        FirebaseUser user = mAuth.getCurrentUser();
        if(user!=null)
        {
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {

                        //cvreate sharedPref
                        SharedPreferences.Editor edit = sharedPreferences.edit();
                        edit.putString(NAME,name);
                        edit.putString(EMAIL,email);
                        edit.putString(PASSWORD,password);
                        edit.commit();

                        //email sent
                        Toast.makeText(SignUpActivity.this,"Email sent ",Toast.LENGTH_SHORT).show();
                        Log.i("TAG", "Email sent:success ");
                        mAuth.signOut();  //To sign out a user, call signOut
                        startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
                        finish();

                    } else {
                        mAuth.signOut();
                        finish();
                    }
                }
            });
        }
    }
}