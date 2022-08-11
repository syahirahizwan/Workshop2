package my.edu.utem.ftmk.workshop2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ShowCandidates extends AppCompatActivity implements View.OnClickListener {

    RecyclerView recyclerView;
    ArrayList<Candidates> candidatesArrayList;
    MyAdapter myAdapter;
    FirebaseFirestore mStore;
    private TextView candidates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar

        setContentView(R.layout.activity_show_candidates);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mStore = FirebaseFirestore.getInstance();
        candidatesArrayList = new ArrayList<Candidates>();
        myAdapter = new MyAdapter(ShowCandidates.this, candidatesArrayList);
        recyclerView.setAdapter(myAdapter);

        EventChangeListener();

        candidates = (TextView) findViewById(R.id.candidates);
        candidates.setOnClickListener(this);
    }

    private void EventChangeListener() {
        mStore.collection("candidates").addSnapshotListener(new EventListener<QuerySnapshot>() {
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

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.title:
                startActivity(new Intent(this, Menu.class));
                break;
        }
    }
}