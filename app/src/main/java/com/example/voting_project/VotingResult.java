package com.example.voting_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class VotingResult extends AppCompatActivity {

    private RecyclerView  resultRV;
    private List<Votes> list;

    private FirebaseFirestore firebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voting_result);

        resultRV = findViewById(R.id.result);
        firebaseFirestore = FirebaseFirestore.getInstance();

        list = new ArrayList<>();
        //adapter = new RecyclerView.Adapter<Votes>();
        //resultRV.setLayoutManager(new LinearLayoutManager(this));
        //resultRV.setAdapter(adapter);
    }

}