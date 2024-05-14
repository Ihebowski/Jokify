package dev.ihebowski.jokify.views.main;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import dev.ihebowski.jokify.R;
import dev.ihebowski.jokify.models.Joke;

public class Add extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;
    TextInputEditText setupInput;
    TextInputEditText punchlineInput;
    ProgressBar loadingProgress;
    MaterialButton saveButton, backButton, moreButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add);
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
                Intent intent = new Intent(getApplicationContext(), Home.class);
                startActivity(intent);
                finish();
            }
        });

        //More
        moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                invalidateOptionsMenu(); // This line invalidates the options menu
                PopupMenu popupMenu = new PopupMenu(Add.this, v);
                popupMenu.getMenuInflater().inflate(R.menu.main_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        // Handle menu item clicks here
                        return true;
                    }
                });
                popupMenu.show();
            }
        });

        //Add
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser == null) {
                    Toast.makeText(Add.this, "User not authenticated", Toast.LENGTH_SHORT).show();
                    return;
                }
                String userId = currentUser.getUid();

                String setup, punchline;
                setup = setupInput.getText().toString().trim();
                punchline = punchlineInput.getText().toString().trim();
                loadingProgress.setVisibility(View.VISIBLE);

                if (TextUtils.isEmpty(setup)) {
                    Toast.makeText(Add.this, "Please enter your Setup", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(punchline)) {
                    Toast.makeText(Add.this, "Please enter your Punchline", Toast.LENGTH_SHORT).show();
                    return;
                }

                mDatabase = FirebaseDatabase.getInstance();
                String key = mDatabase.getReference().push().getKey();
                Joke joke = new Joke(setup, punchline, userId);
                mDatabase.getReference().child("Jokes").child(key).setValue(joke)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(Add.this, "Joke added.", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), Home.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(Add.this, "Joke failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_report) {
            // Handle report action
            Toast.makeText(Add.this, "Report not available right now!", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.nav_dev) {
            // Handle developer action
            Toast.makeText(Add.this, "Made by Ihebowski, ArcherBladeWork, Manita!", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}