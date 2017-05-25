package com.example.raul.mychatapp.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raul.mychatapp.Client;
import com.example.raul.mychatapp.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class mainChat_activity extends AppCompatActivity {


    Button createRoom;
    EditText roomName;
    ListView rooms;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> listOfRooms = new ArrayList<>();
    String email, room_name,userID;
    DatabaseReference root = FirebaseDatabase.getInstance().getReference().child("Rooms");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_chat_activity);
        roomName = (EditText) findViewById(R.id.roomName);
        rooms = (ListView) findViewById(R.id.listView);
        createRoom = (Button) findViewById(R.id.createRoomB);

        //email = getIntent().getExtras().get("get_email").toString();
        userID=getIntent().getExtras().get("get_uid").toString();

        arrayAdapter = new ArrayAdapter<String>(mainChat_activity.this, android.R.layout.simple_list_item_1, listOfRooms);
        rooms.setAdapter(arrayAdapter);


        createRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
          /* Client c = new Client("10.0.2.2", 10000, et.getText().toString());
                c.execute();
                //c.sendMsg("Mesaj");*/

                Map<String, Object> map = new HashMap<String, Object>();
                map.put(roomName.getText().toString(), "");
                root.updateChildren(map);
            }
        });

        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Set<String> set = new HashSet<String>();

                Iterator i = dataSnapshot.getChildren().iterator();
                while (i.hasNext()) {
                    set.add(((DataSnapshot) i.next()).getKey());
                }
                listOfRooms.clear();
                listOfRooms.addAll(set);
                arrayAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        rooms.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), ChatRoomActivity.class);
                room_name = rooms.getItemAtPosition(position).toString();
                i.putExtra("room_name", room_name);
              //  i.putExtra("gett_email", email);
                i.putExtra("getUid",userID);
                startActivity(i);
            }
        });

    }

}

