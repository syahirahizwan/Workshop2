package my.edu.utem.ftmk.workshop2;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VotingModule extends AppCompatActivity {

    Button vote;
    Dialog dialog;
    RecyclerView recyclerView2;
    ArrayList<Candidates> candidatesArrayList;
    MyAdapter myAdapter;
    private FirebaseFirestore mStore;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar

        setContentView(R.layout.activity_voting_module);

        recyclerView2 = findViewById(R.id.recyclerView2);
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));

        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();

        candidatesArrayList = new ArrayList<Candidates>();
        myAdapter = new MyAdapter(VotingModule.this, candidatesArrayList);
        recyclerView2.setAdapter(myAdapter);

        EventChangeListener();

        dialog = new Dialog(VotingModule.this);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.background));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;

        Button Okay = dialog.findViewById(R.id.btn_okay);
        Button Cancel = dialog.findViewById(R.id.btn_cancel);

        Okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(VotingModule.this, "Vote successful!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                updateVoteCounts();
                updateVoteStatus(mAuth.getCurrentUser().getUid());
                startActivity(new Intent(VotingModule.this, ShareStatus.class));
            }
        });

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(VotingModule.this, "Cancel", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        vote = findViewById(R.id.confirmVote);
        vote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
    }

    private void EventChangeListener() {
        mStore.collection("candidates").orderBy("FullName", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error!=null) {
                    Log.e("Firestore error", error.getMessage());
                    return;
                }
                for (DocumentChange dc : value.getDocumentChanges()) {
                    if (dc.getType() == DocumentChange.Type.ADDED) {
                        candidatesArrayList.add(dc.getDocument().toObject(Candidates.class));
                    }
                }
                myAdapter.notifyDataSetChanged();
            }
        });
    }

    private void updateVoteStatus(String uid) {
        DocumentReference df = mStore.collection("students").document(uid);
        Map<String, Object> map = new HashMap<>();
        map.put("hasVote", "1");
        df.update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("TAG", "onSuccess: Update complete!");
                Toast.makeText(VotingModule.this, "Vote is successful!", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("TAG", "onFailure: ", e);
            }
        });
    }

    private void updateVoteCounts() {
        Intent i = getIntent();
        String name = i.getStringExtra("name");
        DocumentReference df = mStore.collection("voteCounts").document();
        Map<String, Object> map = new HashMap<>();
        map.put("candidateName", name);
        df.set(map);
        updateVoteStatus(mAuth.getCurrentUser().getUid());

    }
}