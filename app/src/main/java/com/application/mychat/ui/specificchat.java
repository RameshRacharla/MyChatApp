package com.application.mychat.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.application.mychat.model.Messages;
import com.application.mychat.model.Rateresponse;
import com.application.mychat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class specificchat extends AppCompatActivity {

    EditText mgetmessage;
    ImageButton msendmessagebutton;

    CardView msendmessagecardview;
    androidx.appcompat.widget.Toolbar mtoolbarofspecificchat;
    ImageView mimageviewofspecificuser;
    TextView mnameofspecificuser;

    private String enteredmessage;
    Intent intent;
    String mrecievername, sendername, mrecieveruid, msenderuid;
    private FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    String senderroom, recieverroom;

    ImageButton mbackbuttonofspecificchat;

    RecyclerView mmessagerecyclerview;

    String currenttime;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;

    MessagesAdapter messagesAdapter;
    ArrayList<Messages> messagesArrayList;
    FirebaseFirestore db;
    String snapid;
    int isexist = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specificchat);

        db = FirebaseFirestore.getInstance();

        mgetmessage = findViewById(R.id.getmessage);

        msendmessagecardview = findViewById(R.id.carviewofsendmessage);
        msendmessagebutton = findViewById(R.id.imageviewsendmessage);
        mtoolbarofspecificchat = findViewById(R.id.toolbarofspecificchat);
        mnameofspecificuser = findViewById(R.id.Nameofspecificuser);
        mimageviewofspecificuser = findViewById(R.id.specificuserimageinimageview);
        mbackbuttonofspecificchat = findViewById(R.id.backbuttonofspecificchat);

        messagesArrayList = new ArrayList<>();
        mmessagerecyclerview = findViewById(R.id.recyclerviewofspecific);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        mmessagerecyclerview.setLayoutManager(linearLayoutManager);
        messagesAdapter = new MessagesAdapter(specificchat.this, messagesArrayList);
        mmessagerecyclerview.setAdapter(messagesAdapter);


        intent = getIntent();

        setSupportActionBar(mtoolbarofspecificchat);
        mtoolbarofspecificchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Toolbar is Clicked", Toast.LENGTH_SHORT).show();


            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("hh:mm a");
        FirebaseDatabase.getInstance().purgeOutstandingWrites();

        msenderuid = firebaseAuth.getUid();
        mrecieveruid = getIntent().getStringExtra("receiveruid");
        mrecievername = getIntent().getStringExtra("name");

        senderroom = msenderuid + mrecieveruid;
        recieverroom = mrecieveruid + msenderuid;


        DatabaseReference databaseReference = firebaseDatabase.getReference().
                child("chats").child(senderroom).child("messages");
        databaseReference.keepSynced(true);
        messagesAdapter = new MessagesAdapter(specificchat.this, messagesArrayList);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesArrayList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Messages messages = snapshot1.getValue(Messages.class);
                    messagesArrayList.add(messages);
                }
                messagesAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                DatabaseReference databaseReference = firebaseDatabase.getReference().
                        child("chats").child(senderroom).child("messages");
                databaseReference.keepSynced(true);
                messagesAdapter = new MessagesAdapter(specificchat.this, messagesArrayList);
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messagesArrayList.clear();
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            Messages messages = snapshot1.getValue(Messages.class);
                            messagesArrayList.add(messages);
                        }

                        messagesAdapter = new MessagesAdapter(specificchat.this, messagesArrayList);
                        mmessagerecyclerview.setAdapter(messagesAdapter);
                        messagesAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                DatabaseReference databaseReference = firebaseDatabase.getReference().
                        child("chats").child(senderroom).child("messages");
                databaseReference.keepSynced(true);
                messagesAdapter = new MessagesAdapter(specificchat.this, messagesArrayList);
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messagesArrayList.clear();
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            Messages messages = snapshot1.getValue(Messages.class);
                            messagesArrayList.add(messages);
                        }
                        messagesAdapter = new MessagesAdapter(specificchat.this, messagesArrayList);
                        mmessagerecyclerview.setAdapter(messagesAdapter);
                        messagesAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });


        mbackbuttonofspecificchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Rateyourexperience();

                //finish();
            }
        });


        mnameofspecificuser.setText(mrecievername);

        String uri = intent.getStringExtra("imageuri");
        if (uri.isEmpty()) {
            Toast.makeText(getApplicationContext(), "null is recieved", Toast.LENGTH_SHORT).show();
        } else {
            Picasso.get().load(uri).into(mimageviewofspecificuser);
        }


        msendmessagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                enteredmessage = mgetmessage.getText().toString();
                if (enteredmessage.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Enter message first", Toast.LENGTH_SHORT).show();
                } else {
                    Date date = new Date();
                    currenttime = simpleDateFormat.format(calendar.getTime());
                    Messages messages = new Messages(enteredmessage, firebaseAuth.getUid(), date.getTime(), currenttime);
                    firebaseDatabase = FirebaseDatabase.getInstance();
                    firebaseDatabase.getReference().child("chats")

                            .child(senderroom)
                            .child("messages")
                            .push().setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    firebaseDatabase.getReference()
                                            .child("chats")
                                            .child(recieverroom)
                                            .child("messages")
                                            .push()
                                            .setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                }
                                            });
                                }
                            });

                    mgetmessage.setText(null);


                }


            }
        });


    }

    private void Rateyourexperience() {

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View promptView = layoutInflater.inflate(R.layout.dialogbox_rating, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setCancelable(false);
        Button ranking = (Button) promptView.findViewById(R.id.rank_dialog_button);
        ImageView cancel = (ImageView) promptView.findViewById(R.id.close);
        RatingBar ratingBar = (RatingBar) promptView.findViewById(R.id.dialog_ratingbar);

        ranking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String rate = String.valueOf(ratingBar.getRating());


                db.collection("Rating").get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot documentSnapshots) {
                                if (documentSnapshots.isEmpty()) {
                                    addDataToFirestoreDB(rate);

                                } else {
                                    for (DocumentSnapshot documentSnapshot : documentSnapshots) {

                                        if (documentSnapshot.getData().containsValue(firebaseAuth.getUid())) {
                                            snapid = documentSnapshot.getId();
                                            isexist = 1;
                                        }


                                    }

                                    if (isexist == 1) {
                                        updatedata(rate);

                                    } else {
                                        addDataToFirestoreDB(rate);

                                    }


                                }

                            }

                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(specificchat.this, getResources().getString(R.string.error_getting_data), Toast.LENGTH_LONG).show();
                            }
                        });


                Log.e("Tag", rate);

            }
        });

        alertDialog.setView(promptView);
        alertDialog.show();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                finish();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();


    }


    @Override
    public void onStart() {
        super.onStart();
        messagesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (messagesAdapter != null) {
            messagesAdapter.notifyDataSetChanged();
        }
    }


    private void addDataToFirestoreDB(String rate) {

        // creating a collection reference
        // for our Firebase Firetore database.
        CollectionReference collectionReference = db.collection("Rating");

        // adding our data to our courses object class.
        Rateresponse list = new Rateresponse(firebaseAuth.getUid(), rate);

        // below method is use to add data to Firebase Firestore.
        collectionReference.add(list).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // this method is called when the data addition process is failed.
                // displaying a toast message when data addition is failed.
                Toast.makeText(specificchat.this, "Fail to Register \n" + e, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updatedata(String rate) {

        // creating a collection reference
        // for our Firebase Firetore database.
        CollectionReference collectionReference = db.collection("Rating");

        // adding our data to our courses object class.
        Rateresponse list = new Rateresponse(firebaseAuth.getUid(), rate);

        // below method is use to add data to Firebase Firestore.
        collectionReference.document(snapid).set(list).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                finish();
                //Toast.makeText(specificchat.this, "", Toast.LENGTH_SHORT).show();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(specificchat.this, "Fail to Register \n" + e, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Rateyourexperience();

    }
}