package dev.ihebowski.jokify.views.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import dev.ihebowski.jokify.R;

public class Modify extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;
    TextInputEditText setupInput, punchlineInput;
    ProgressBar loadingProgress;
    MaterialButton saveButton, backButton, moreButton;
    String jokeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_modify);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //firebase
        mAuth = FirebaseAuth.getInstance();

        //Inputs
        backButton = findViewById(R.id.back_btn);
        moreButton = findViewById(R.id.more_btn);
        setupInput = findViewById(R.id.setup);
        punchlineInput = findViewById(R.id.punchline);
        loadingProgress = findViewById(R.id.loading);
        saveButton = findViewById(R.id.save_btn);

        //Back
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Profile.class);
                startActivity(intent);
                finish();
            }
        });

        //Fill Inputs
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            jokeId = extras.getString("joke_id");
            String setup = extras.getString("joke_setup");
            String punchline = extras.getString("joke_punchline");

            setupInput.setText(setup);
            punchlineInput.setText(punchline);
        }

        //Save
        saveButton.setOnClickListener(v -> {
            String modifiedSetup = setupInput.getText().toString().trim();
            String modifiedPunchline = punchlineInput.getText().toString().trim();
            DatabaseReference jokeRef = FirebaseDatabase.getInstance().getReference()
                    .child("Jokes").child(jokeId);
            jokeRef.child("setup").setValue(modifiedSetup);
            jokeRef.child("punchline").setValue(modifiedPunchline);
            finish();
        });
    }
}