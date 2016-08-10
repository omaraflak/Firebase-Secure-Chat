package me.aflak.firebasesecurechat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private ChatAdapter adapter;
    private ListView listView;
    private EditText message;
    private Button logout;

    private FirebaseUser user;
    private DatabaseReference reference;

    private static final String TAG = "Firebase Secure Chat";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.list);
        message = (EditText) findViewById(R.id.message);
        logout = (Button) findViewById(R.id.logout);
        ImageButton send = (ImageButton) findViewById(R.id.send_message);

        adapter = new ChatAdapter(this, R.id.list);
        listView.setAdapter(adapter);

        user = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        reference = database.getReference("chat");

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ChatMessage msg = dataSnapshot.getValue(ChatMessage.class);
                adapter.add(msg);
                scrollToBottom();
            }

            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            public void onChildRemoved(DataSnapshot dataSnapshot) {}
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            public void onCancelled(DatabaseError databaseError) {}
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChatMessage msg = new ChatMessage(user.getEmail(), message.getText().toString());
                reference.push().setValue(msg);
                message.setText("");
                scrollToBottom();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(MainActivity.this, SignIn.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void scrollToBottom() {
        listView.post(new Runnable() {
            @Override
            public void run() {
                listView.setSelection(adapter.getCount() - 1);
            }
        });
    }
}
