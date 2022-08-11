package my.edu.utem.ftmk.workshop2;

import android.content.Context;
import android.content.Intent;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    ArrayList<Candidates> candidatesArrayList;
    FirebaseFirestore mStore;


    public MyAdapter(Context context, ArrayList<Candidates> candidatesArrayList) {
        this.context = context;
        this.candidatesArrayList = candidatesArrayList;
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Candidates candidates = candidatesArrayList.get(position);
        holder.name.setText(candidates.FullName);
        holder.showFaculty.setText(candidates.Faculty);
        holder.showManifesto.setText(candidates.Manifesto);
        holder.showUrl.setText(candidates.Url);
        Glide.with(holder.displayImage.getContext()).load(candidates.getImage()).into(holder.displayImage);

    }

    @Override
    public int getItemCount() {
        return candidatesArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name, showFaculty, showManifesto, showUrl;
        ImageView displayImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(v.getContext(), VotingModule.class);
                    i.putExtra("name", name.getText().toString());
                    v.getContext().startActivity(i);
                }
            });
            displayImage = itemView.findViewById(R.id.displayImage);
            name = itemView.findViewById(R.id.name);
            showFaculty = itemView.findViewById(R.id.showFaculty);
            showManifesto = itemView.findViewById(R.id.showManifesto);
            showUrl = itemView.findViewById(R.id.showUrl);
            showUrl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showUrl.setMovementMethod(LinkMovementMethod.getInstance());
                }
            });

        }


    }
}
