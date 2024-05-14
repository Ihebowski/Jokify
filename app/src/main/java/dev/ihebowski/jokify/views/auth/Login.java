package dev.ihebowski.jokify.views.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import dev.ihebowski.jokify.R;
import dev.ihebowski.jokify.views.main.Home;

public class Login extends AppCompatActivity {
    FirebaseAuth mAuth;
    TextInputEditText emailInput, passwordInput;
    ProgressBar loadingProgress;
    MaterialButton loginButton;
    TextView registerLink;

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), Home.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Firebase
        mAuth = FirebaseAuth.getInstance();

        //Items
        emailInput = findViewById(R.id.email);
        passwordInput = findViewById(R.id.password);
        loadingProgress = findViewById(R.id.loading);
        loginButton = findViewById(R.id.login_btn);
        registerLink = findViewById(R.id.register_link);

        //Register
        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
                finish();
            }
        });

        //Login
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, password;
                email = emailInput.getText().toString().trim();
                password = passwordInput.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(Login.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(Login.this, "Please enter your password", Toast.LENGTH_SHORT).show();
                    return;
                }

                loadingProgress.setVisibility(View.VISIBLE);

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                   @Override
                                                   public void onComplete(@NonNull Task<AuthResult> task) {
                                                       loadingProgress.setVisibility(View.GONE);
                                                       if (task.isSuccessful()) {
                                                           FirebaseUser user = mAuth.getCurrentUser();
                                                           Toast.makeText(Login.this, "Login successful.",
                                                                   Toast.LENGTH_SHORT).show();
                                                           Intent intent = new Intent(getApplicationContext(), Home.class);
                                                           startActivity(intent);
                                                           finish();
                                                       } else {
                                                           Toast.makeText(Login.this, "Login failed.",
                                                                   Toast.LENGTH_SHORT).show();
                                                       }
                                                   }
                                               }
                        );
            }
        });
    }
}