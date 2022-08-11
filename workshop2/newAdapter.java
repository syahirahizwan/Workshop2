package my.edu.utem.ftmk.workshop2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class newAdapter extends RecyclerView.Adapter<newAdapter.MyViewHolder> {

    Context context;
    ArrayList<voteCounts> voteCountsArrayList;
    FirebaseFirestore mStore;

    public newAdapter(Context context, ArrayList<voteCounts> voteCountsArrayList) {
        this.context = context;
        this.voteCountsArrayList = voteCountsArrayList;
    }

    @NonNull
    @Override
    public newAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.vote_item, parent, false);

        return new newAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull newAdapter.MyViewHolder holder, int position) {
        voteCounts vote = voteCountsArrayList.get(position);
        holder.name.setText(vote.candidateName);
    }

    @Override
    public int getItemCount() {
        return voteCountsArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            //voteCount = itemView.findViewById(R.id.voteCounts);

        }


    }
}
