package com.example.raul.mychatapp.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raul.mychatapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import Adapters.MyAdapter;

public class rooms_activity extends AppCompatActivity {


    Button createRoom;
    EditText roomName;
    TextView tv;
    ListView rooms;
    //ArrayAdapter<String> arrayAdapter;
    ArrayList<String> listOfRooms = new ArrayList<>();
    MyAdapter myAdapter;

    String room_name, userID;
    FloatingActionButton fab;
    DatabaseReference root = FirebaseDatabase.getInstance().getReference().child("Rooms");
    DatabaseReference getUser;
    AlertDialog.Builder alertDialog;
    String username = "test";
    FirebaseAuth auth;
    Button logoutB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooms);

        roomName = (EditText) findViewById(R.id.roomName);
        rooms = (ListView) findViewById(R.id.listView);
        createRoom = (Button) findViewById(R.id.createRoomB);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        tv = (TextView) findViewById(R.id.textView3);
        logoutB=(Button)findViewById(R.id.logoutB);

        logoutB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth=auth.getInstance();
                auth.signOut();
                Intent i=new Intent(rooms_activity.this,login_activity.class);
                startActivity(i);
                finish();
            }
        });




        //userID = getIntent().getExtras().get("get_uid").toString();
        userID = auth.getInstance().getCurrentUser().getUid();
        getUser = FirebaseDatabase.getInstance().getReference().child("Users");

        getUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot children : dataSnapshot.getChildren()) {
                    if (children.getKey().equals(userID)) {
                        for (DataSnapshot child : children.getChildren()) {
                            if (child.getKey().equals("Username")) {
                                username = child.getValue(String.class);
                                tv.setText("You are logged in as " + username);
                            }
                        }
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        myAdapter=new MyAdapter(this,listOfRooms);
       // arrayAdapter = new ArrayAdapter<String>(rooms_activity.this, android.R.layout.simple_list_item_1, listOfRooms);
        rooms.setAdapter(myAdapter);

        /*createRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put(roomName.getText().toString(), "");
                root.updateChildren(map);
            }
        });*/

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
                myAdapter.notifyDataSetChanged();
                //arrayAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        rooms.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), chatroom_activity.class);
                room_name = rooms.getItemAtPosition(position).toString();
                i.putExtra("room_name", room_name);
                i.putExtra("getUid", userID);
                startActivity(i);
            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog = new AlertDialog.Builder(rooms_activity.this);
                alertDialog.setTitle("Room name");
                alertDialog.setMessage("Please enter room name");

                final EditText input = new EditText(rooms_activity.this);

                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alertDialog.setView(input);

                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(listOfRooms.contains(input.getText().toString())){
                            Toast.makeText(getApplicationContext(), "Room name already exists.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else{
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put(input.getText().toString(), "");
                            root.updateChildren(map);
                        }
                    }
                });
                alertDialog.show();
            }
        });

    }

}

