package com.example.voting_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Voting_Activity extends AppCompatActivity {

    private TextView Title;
    private EditText Name;
    private FirebaseAuth auth;
    private RadioGroup radioGroup;
    private RadioButton radioButton;

    FirebaseFirestore firebaseFirestore;
    SharedPreferences sharedPreferences;
    String title, check, name ;

    public static final String PREFERENCES = "prefKey";
    public static final String TITLE = "titleKey";
    public static final String ITEMS = "itemKey";
    public static final String NAME = "nameKey";

    int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voting);

        Title = findViewById(R.id.title);
        Name = findViewById(R.id.name);
        radioGroup = findViewById(R.id.radioGroup);

        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        sharedPreferences = this.getSharedPreferences(PREFERENCES,MODE_PRIVATE);

        // set the title
        title = sharedPreferences.getString(TITLE,"t");
        Title.setText(title);
        Title.setTextSize(20);

        Set<String> items = sharedPreferences.getStringSet(ITEMS, new HashSet<>());
        Iterator<String> it = items.iterator();
        while(it.hasNext()) {
            radioButton = new RadioButton(this);
            String value = it.next();
            radioButton.setText(value);
            radioButton.setId(i++);
            radioGroup.addView(radioButton);
            System.out.println(value);
        }

        radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checked = ((RadioButton) view).isChecked();
                int j=0;
                for(j=0; j<i; j++)
                {
                    if (checked)
                    {
                        check = radioButton.getText().toString();
                    }
                }
            }
        });
    }

    public void Vote(View view) {
        name = Name.getText().toString();
        String CHECK = check;

        if(name.length() != 0 && CHECK.length() != 0 )
        {
            String uid = auth.getCurrentUser().getUid();
            Map<String, String> map = new HashMap<>();
            map.put("name", name);
            map.put("checked",CHECK);
            map.put("uid", uid);

            firebaseFirestore.collection("Vote").document(uid).set(map)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {
                                    Toast.makeText(Voting_Activity.this,"Voted successfully ",Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(Voting_Activity.this,MainActivity.class));
                                    Log.i("TAG", "*********dataSaved:success", task.getException());
                                    finish();

                                } else {
                                    Toast.makeText(Voting_Activity.this,"Data not storage ",Toast.LENGTH_SHORT).show();
                                    Log.i("TAG", "dataSaved:failure", task.getException());
                                }
                        }
                    });
        }
        else
        {
            Toast.makeText(Voting_Activity.this,"Enter details ",Toast.LENGTH_SHORT).show();
        }
    }
}