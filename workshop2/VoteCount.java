package my.edu.utem.ftmk.workshop2;

import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class VoteCount extends AppCompatActivity {

    private FirebaseFirestore mStore;
    private TextView candidatename, totalCount;
    RecyclerView recyclerView3;
    ArrayList<voteCounts> voteCountsArrayList;
    newAdapter newadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar

        setContentView(R.layout.activity_vote_count);

        mStore = FirebaseFirestore.getInstance();

        recyclerView3 = findViewById(R.id.recyclerView3);
        recyclerView3.setHasFixedSize(true);
        recyclerView3.setLayoutManager(new LinearLayoutManager(this));

        voteCountsArrayList = new ArrayList<voteCounts>();
        newadapter = new newAdapter(VoteCount.this, voteCountsArrayList);
        recyclerView3.setAdapter(newadapter);

        EventChangeListener();
    }

    private void EventChangeListener() {
        mStore.collection("voteCounts").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error!=null) {
                    Log.e("Firestore error", error.getMessage());
                    return;
                }
                for (DocumentChange dc : value.getDocumentChanges()) {
                    if (dc.getType() == DocumentChange.Type.ADDED) {
                        voteCountsArrayList.add(dc.getDocument().toObject(voteCounts.class));
                    }
                }
                newadapter.notifyDataSetChanged();
            }
        });
    }

}