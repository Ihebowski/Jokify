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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import dev.ihebowski.jokify.R;
import dev.ihebowski.jokify.models.Joke;
import dev.ihebowski.jokify.views.auth.Login;

public class Home extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference databaseReference;
    MaterialButton profileButton;
    FloatingActionButton addButton;
    ProgressBar progressBar;
    TextView emptyTextView;
    RecyclerView recyclerView;
    JokeHomeAdapter jokeListAdapter;
    ArrayList<Joke> jokeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Firebase
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        //Items
        profileButton = findViewById(R.id.profile_btn);
        addButton = findViewById(R.id.addjoke_btn);
        progressBar = findViewById(R.id.loading);
        emptyTextView = findViewById(R.id.empty_text);

        //Logic
        if (mUser == null) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }

        //Logout
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Profile.class);
                startActivity(intent);
                finish();
            }
        });

        //Add Joke
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Add.class);
                startActivity(intent);
                finish();
            }
        });

        //Load Jokes
        recyclerView = findViewById(R.id.joke_list);
        databaseReference = FirebaseDatabase.getInstance().getReference("Jokes");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        jokeList = new ArrayList<>();
        jokeListAdapter = new JokeHomeAdapter(this, jokeList);
        recyclerView.setAdapter(jokeListAdapter);
        progressBar.setVisibility(View.VISIBLE);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                jokeList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Joke joke = dataSnapshot.getValue(Joke.class);
                    String uid = joke.getUid();
                    DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users").child(uid);
                    usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                String firstName = dataSnapshot.child("firstname").getValue(String.class);
                                String lastName = dataSnapshot.child("lastname").getValue(String.class);
                                joke.setUsername(firstName + " " + lastName);
                                jokeList.add(joke);
                                jokeListAdapter.notifyDataSetChanged();
                            }
                            if (jokeList.isEmpty()) {
                                emptyTextView.setVisibility(View.VISIBLE);
                            } else {
                                emptyTextView.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}