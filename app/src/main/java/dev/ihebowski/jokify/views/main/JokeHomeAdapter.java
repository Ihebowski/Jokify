package dev.ihebowski.jokify.views.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import dev.ihebowski.jokify.R;
import dev.ihebowski.jokify.models.Joke;

public class JokeHomeAdapter extends RecyclerView.Adapter<JokeHomeAdapter.ViewModeler> {

    Context context;
    ArrayList<Joke> jokeList;

    public JokeHomeAdapter(Context context, ArrayList<Joke> jokeList) {
        this.context = context;
        this.jokeList = jokeList;
    }

    @NonNull
    @Override
    public ViewModeler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.joke_home,parent,false);
        return new ViewModeler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewModeler holder, int position) {
        Joke joke = jokeList.get(position);
        holder.SetupTextview.setText(joke.setup);
        holder.PunchlineTextview.setText(joke.punchline);
        holder.UserTextview.setText(joke.username);
    }

    @Override
    public int getItemCount() {
        return jokeList.size();
    }

    public static class ViewModeler extends RecyclerView.ViewHolder {
        TextView SetupTextview;
        TextView PunchlineTextview;
        TextView UserTextview;

        public ViewModeler(@NonNull View itemView) {
            super(itemView);

            SetupTextview = itemView.findViewById(R.id.setupText);
            PunchlineTextview = itemView.findViewById(R.id.punchlineText);
            UserTextview = itemView.findViewById(R.id.userText);
        }
    }
}
