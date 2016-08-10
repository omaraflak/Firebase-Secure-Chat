package me.aflak.firebasesecurechat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by root on 10/08/16.
 */

public class SignIn extends Activity {
    private Button signin;
    private Button login;
    private EditText emailSignin;
    private EditText passwordSignin;

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;

    private static final String TAG = "Firebase Secure Chat";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);

        signin = (Button) findViewById(R.id.signin);
        login = (Button) findViewById(R.id.login);
        emailSignin = (EditText) findViewById(R.id.signin_form_email);
        passwordSignin = (EditText) findViewById(R.id.signin_form_password);

        auth = FirebaseAuth.getInstance();
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    Intent i = new Intent(SignIn.this, MainActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = emailSignin.getText().toString();
                final String passwd = passwordSignin.getText().toString();
                auth.createUserWithEmailAndPassword(email, passwd).addOnCompleteListener(SignIn.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            auth.signInWithEmailAndPassword(email, passwd);
                            // authListener will be automatically fired since the user auth state changed, then we'll switch to the chat
                        }
                        else{
                            Toast.makeText(SignIn.this, "Can't create account...", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = emailSignin.getText().toString();
                final String passwd = passwordSignin.getText().toString();
                auth.signInWithEmailAndPassword(email, passwd).addOnFailureListener(SignIn.this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                        Toast.makeText(SignIn.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                // authListener will be automatically fired since the user auth state changed, then we'll switch to the chat
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }
}
