package dev.ihebowski.jokify.views.main;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import dev.ihebowski.jokify.R;
import dev.ihebowski.jokify.models.Joke;

public class JokeProfileAdapter extends RecyclerView.Adapter<JokeProfileAdapter.ViewModeler> {
    Context context;
    ArrayList<Joke> jokeList;

    public JokeProfileAdapter(Context context, ArrayList<Joke> jokeList) {
        this.context = context;
        this.jokeList = jokeList;
    }

    @NonNull
    @Override
    public ViewModeler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.joke_profile,parent,false);
        return new ViewModeler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewModeler holder, int position) {
        Joke joke = jokeList.get(position);
        holder.SetupTextview.setText(joke.setup);
        holder.PunchlineTextview.setText(joke.punchline);

        holder.deleteButton.setOnClickListener(v -> {
            DatabaseReference jokeRef = FirebaseDatabase.getInstance().getReference()
                    .child("Jokes").child(joke.getId());
            jokeRef.removeValue()
                    .addOnSuccessListener(aVoid -> {
                        int pos = jokeList.indexOf(joke);
                        if (pos != -1) {
                            jokeList.remove(pos);
                            notifyItemRemoved(pos);
                        }
                        Toast.makeText(context, "Joke deleted.", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "Joke Failed.", Toast.LENGTH_SHORT).show();
                    });
        });

        holder.editButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, Modify.class);
            intent.putExtra("joke_id", joke.getId());
            intent.putExtra("joke_setup", joke.getSetup());
            intent.putExtra("joke_punchline", joke.getPunchline());
            try {
                context.startActivity(intent);
            } catch (Exception e) {
                Log.e("JokeProfileAdapter", "Error starting activity: " + e.getMessage());
            }
        });
    }

    @Override
    public int getItemCount() {
        return jokeList.size();
    }

    public static class ViewModeler extends RecyclerView.ViewHolder {

        TextView SetupTextview, PunchlineTextview;
        MaterialButton editButton, deleteButton;

        public ViewModeler(@NonNull View itemView) {
            super(itemView);

            SetupTextview = itemView.findViewById(R.id.setupText);
            PunchlineTextview = itemView.findViewById(R.id.punchlineText);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            editButton = itemView.findViewById(R.id.editButton);
        }
    }
}
