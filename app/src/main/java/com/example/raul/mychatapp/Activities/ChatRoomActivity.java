package com.example.raul.mychatapp.Activities;

import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raul.mychatapp.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.OnDisconnect;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

public class ChatRoomActivity extends AppCompatActivity {

    Button sendMessage;
    EditText message;
    TextView conversation;

    String username = "test";
    String roomName;
    String userID;

    DatabaseReference root;
    DatabaseReference getUser;

    private String temp_key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);


        sendMessage = (Button) findViewById(R.id.sendMessage);
        message = (EditText) findViewById(R.id.message);
        conversation = (TextView) findViewById(R.id.textView2);

        roomName = getIntent().getExtras().get("room_name").toString();
        userID = getIntent().getExtras().get("getUid").toString();


        setTitle(" Room - " + roomName);

        root = FirebaseDatabase.getInstance().getReference().child("Rooms").child(roomName);
        getUser = FirebaseDatabase.getInstance().getReference().child("Users");

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                temp_key = root.push().getKey();

                DatabaseReference message_root = root.child(temp_key); //inside message
                Map<String, Object> map2 = new HashMap<String, Object>();
                map2.put("username", username);
                map2.put("msg", message.getText().toString());
                message_root.updateChildren(map2);
                message.setText("");

            }
        });
        getUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot children : dataSnapshot.getChildren()) {
                    if (children.getKey().equals(userID)) {
                        for (DataSnapshot child : children.getChildren()) {
                            if (child.getKey().equals("Username")) {
                                // Log.d("YOOO", child.getValue(String.class));
                                username = child.getValue(String.class);
                            }
                        }
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        root.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                append_chat_conv(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                append_chat_conv(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    String chat_email, chat_msg;

    private void append_chat_conv(DataSnapshot dataSnapshot) {

        Iterator i = dataSnapshot.getChildren().iterator();

        while (i.hasNext()) {
            chat_msg = (String) ((DataSnapshot) i.next()).getValue();
            chat_email = (String) ((DataSnapshot) i.next()).getValue();
            conversation.append(chat_email + " : " + chat_msg + "\n");
        }
    }
}
