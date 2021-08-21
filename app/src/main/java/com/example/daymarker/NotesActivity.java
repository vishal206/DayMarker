package com.example.daymarker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class NotesActivity extends AppCompatActivity {

    private EditText edt_note;
    private Button btn_add,btn_show;

    private List<notes> notesList;

    private RecyclerView recyclerView;

    private FirebaseAuth mAuth;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);


        edt_note=findViewById(R.id.get_note);
        btn_add=findViewById(R.id.btn_add);
        btn_show=findViewById(R.id.btn_show_notes);

        notesList=new ArrayList<notes>();

        recyclerView=findViewById(R.id.my_recyclerview);
        updateList();
        setAdaptor();

        btn_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAdaptor();
            }
        });
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUserInfo();
                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),0);
                setAdaptor();
            }
        });

    }

    private void setAdaptor() {

        notesAdapter adapter=new notesAdapter(notesList);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        adapter.notifyDataSetChanged();
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

    }
    public void updateList(){
        mAuth=FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        uid=user.getUid();
        FirebaseFirestore.getInstance().collection("Users").document(uid).collection("notes").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                notes c1=new notes(document.get("note").toString(),document.get("date").toString());
                                notesList.add(0,c1);
                                Log.d("Log.d",""+document.get("note"));
                            }
                        }else{
                            Log.d("Log.d", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void setUserInfo() {
        Date c = Calendar.getInstance().getTime();
//System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);
        String note=edt_note.getText().toString();

        mAuth=FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        uid=user.getUid();


        if(TextUtils.isEmpty(note))
        {
            Toast.makeText(this, "type the note", Toast.LENGTH_SHORT).show();
        }
        else{
            notesList.add(0,new notes(note,formattedDate));
            DocumentReference ref= FirebaseFirestore.getInstance().collection("Users").document(uid).collection("notes").document(formattedDate+"-"+note);
            HashMap<String,Object> hashMap=new HashMap<>();
            hashMap.put("note",note);
            hashMap.put("date",formattedDate);
            ref.set(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(getApplicationContext(), "added", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }


    }
}