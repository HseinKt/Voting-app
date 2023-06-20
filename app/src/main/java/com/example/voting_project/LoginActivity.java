package com.example.voting_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LoginActivity extends AppCompatActivity {

    private EditText userPass, userEmail;
    private TextView haveNoAcc;
    private FirebaseAuth mAuth;

    public static final String PREFERENCES = "prefKey";
    public static final String NAME = "nameKey";
    public static final String EMAIL = "emailKey";
    public static final String PASSWORD = "passwordKey";

    SharedPreferences sharedPreferences;
    FirebaseFirestore firebaseFirestore;

    String Email, Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userPass = findViewById(R.id.user_password);
        userEmail = findViewById(R.id.user_email);
        haveNoAcc = findViewById(R.id.haveNoAcc);
        mAuth = FirebaseAuth.getInstance();

        sharedPreferences = this.getSharedPreferences(PREFERENCES,MODE_PRIVATE);
        firebaseFirestore = FirebaseFirestore.getInstance();

        haveNoAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    public void login(View view) {
        Email= userEmail.getText().toString();
        Password= userPass.getText().toString();

        mAuth.signInWithEmailAndPassword(Email, Password)
                .addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(LoginActivity.this,"User found ",Toast.LENGTH_SHORT).show();
                            Log.i("TAG", "signInWithEmail:success ");
                            verifyEmail();

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this,"User not found ",Toast.LENGTH_SHORT).show();
                            Log.i("TAG", "signInWithEmail:failure", task.getException());
                        }
                    }
                });
    }

    private void verifyEmail() {

        FirebaseUser user = mAuth.getCurrentUser();
        String name = sharedPreferences.getString(NAME,"Name");
        String email = sharedPreferences.getString(EMAIL,"E "+Email);
        String password = sharedPreferences.getString(PASSWORD,"P "+Password);

        //first we sent email for  verification
        //Store data in sharedPreferences if user verify email
        //and login then we upload data to FireStore

        if(name !=null && email !=null && password !=null  )
        {
            String uid = mAuth.getUid();

            Map<String, String> map = new HashMap<>();
            map.put("name", name);
            map.put("email", email);
            map.put("password", password);
            map.put("uid", uid);

            firebaseFirestore.collection("Users")
                    .document(uid)
                    .set(map)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this,"Data storage ",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this,Administrator_Activity.class));
                                finish();

                            } else {
                                Toast.makeText(LoginActivity.this,"Data not storage ",Toast.LENGTH_SHORT).show();
                                Log.i("TAG", "createUserWithEmail:failure", task.getException());
                            }
                        }
                    });
        }
    }
}