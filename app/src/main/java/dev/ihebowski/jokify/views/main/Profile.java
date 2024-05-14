package dev.ihebowski.jokify.views.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import dev.ihebowski.jokify.R;
import dev.ihebowski.jokify.models.Joke;
import dev.ihebowski.jokify.views.auth.Login;

public class Profile extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference databaseReference;
    MaterialButton backButton, logoutButton;
    ProgressBar progressBar;
    TextView emptyTextView, fullnameTextView;
    RecyclerView recyclerView;
    JokeProfileAdapter jokeListAdapter;
    ArrayList<Joke> jokeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Firebase
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        //Items
        backButton = findViewById(R.id.back_btn);
        logoutButton = findViewById(R.id.logout_btn);
        fullnameTextView = findViewById(R.id.fullname_text);
        progressBar = findViewById(R.id.loading);
        emptyTextView = findViewById(R.id.empty_text);

        //Back
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Home.class);
                startActivity(intent);
                finish();
            }
        });

        //Logout
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

        //Load Name
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mUser.getUid());
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String firstName = dataSnapshot.child("firstname").getValue(String.class);
                    String lastName = dataSnapshot.child("lastname").getValue(String.class);
                    String fullName = firstName + " " + lastName;
                    fullnameTextView.setText(fullName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle onCancelled
            }
        });

        //Load Jokes
        recyclerView = findViewById(R.id.joke_list);
        databaseReference = FirebaseDatabase.getInstance().getReference("Jokes");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        jokeList = new ArrayList<>();
        jokeListAdapter = new JokeProfileAdapter(this, jokeList);
        recyclerView.setAdapter(jokeListAdapter);

        Query query = databaseReference.orderByChild("uid").equalTo(mUser.getUid());
        progressBar.setVisibility(View.VISIBLE);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                jokeList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Joke joke = dataSnapshot.getValue(Joke.class);
                    joke.setId(dataSnapshot.getKey());
                    jokeList.add(joke);
                }
                jokeListAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                if (jokeList.isEmpty()) {
                    emptyTextView.setVisibility(View.VISIBLE);
                } else {
                    emptyTextView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });
    }
}