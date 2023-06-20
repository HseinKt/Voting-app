package com.example.voting_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Administrator_Activity extends AppCompatActivity {

    private EditText titleName, item;
    private Spinner spinnerItem;
    int i=0;
    ArrayList<String> list;

    FirebaseAuth auth;
    FirebaseFirestore firebaseFirestore;
    SharedPreferences sharedPreferences;
    String name;

    public static final String PREFERENCES = "prefKey";
    public static final String TITLE = "titleKey";
    public static final String ITEMS = "itemKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrator);

        titleName = findViewById(R.id.titleName);
        item = findViewById(R.id.item);
        spinnerItem = findViewById(R.id.spinner_item);

        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        sharedPreferences = this.getApplicationContext().getSharedPreferences(PREFERENCES,MODE_PRIVATE);

        list = new ArrayList<>();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line,list);
        spinnerItem.setAdapter(arrayAdapter); //show list in Administrator Activity
    }

    public void AddItem(View view) {

        list.add(item.getText().toString());  //fill the list
        Log.i("  TAG", "list: "+list);
    }

    public void Start_Voting(View view) {

        String title = titleName.getText().toString();  //save title

        if(title.length() != 0 && !spinnerItem.equals("") )
        {
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putString(TITLE,title);
            Set<String> items = new HashSet<>();
            for (i=0; i<list.size(); i++)// Add the new value.
                items.add(list.get(i));
            edit.putStringSet(ITEMS, items);
            edit.commit();


            String uid = auth.getCurrentUser().getUid();
            Map<String, String> map = new HashMap<>();
            map.put("title", title);
            for (i=0; i<list.size(); i++)
                map.put("list "+i, list.get(i));
            map.put("uid", uid);

            firebaseFirestore.collection("Items")
                    .document(uid)
                    .set(map)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Administrator_Activity.this,"data saved and go back to main Activity ",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Administrator_Activity.this,MainActivity.class));
                                Log.i("TAG", "dataSaved:success", task.getException());
                                finish();

                            } else {
                                Toast.makeText(Administrator_Activity.this,"Data not storage ",Toast.LENGTH_SHORT).show();
                                Log.i("TAG", "dataSaved:failure", task.getException());
                            }
                        }
                    });
        }
        else
        {
            Toast.makeText(Administrator_Activity.this,"Enter details ",Toast.LENGTH_SHORT).show();
        }
    }
}