package le.minh.authentication_login_resgiter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    EditText editemail,editpass;
    Button btndky,btndn;
    TextView btnquenmk;
    ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editemail = findViewById(R.id.editEmail);
        editpass = findViewById(R.id.editPass);
        btndky = findViewById(R.id.btnDky);
        btndn = findViewById(R.id.btnDN);
        btnquenmk = findViewById(R.id.textQuenMK);
        progressBar = findViewById(R.id.progressBar);

        firebaseAuth = FirebaseAuth.getInstance();
        /*if(firebaseAuth.getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
         }*/

        btndky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editemail.getText().toString().trim();
                String pass = editpass.getText().toString().trim();
                if(TextUtils.isEmpty(email)){
                    editemail.setError("Email can't Empty");
                    return;
                }
                if(TextUtils.isEmpty(pass)){
                    editpass.setError("Password can't Empty");
                    return;
                }
                if(pass.length()<6){
                    editpass.setError("Password can't length <6");
                    return;

                }
                progressBar.setVisibility(View.VISIBLE);
                firebaseAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            FirebaseUser fuser = firebaseAuth.getCurrentUser();
                            fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getApplicationContext(),"Verification email has been sent",Toast.LENGTH_LONG).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(),"Can not Verification email has been sent",Toast.LENGTH_LONG).show();
                                }
                            });

                            Toast.makeText(getApplicationContext(),"Sign up completed!",Toast.LENGTH_LONG).show();
                            Intent dash = new Intent(MainActivity.this,MainActivity_Dashboard.class);
                            dash.putExtra("mail",email);
                            dash.putExtra("pass",pass);

                            startActivity(dash);
                        }else{
                            Toast.makeText(getApplicationContext(),"Sign up NOT completed!",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
        btndn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editemail.getText().toString().trim();
                String pass = editpass.getText().toString().trim();
                if(TextUtils.isEmpty(email)){
                    editemail.setError("Email can't Empty");
                    return;
                }
                if(TextUtils.isEmpty(pass)){
                    editpass.setError("Password can't Empty");
                    return;
                }
                if(pass.length()<6){
                    editpass.setError("Password can't length <6");
                    return;

                }
                progressBar.setVisibility(View.VISIBLE);
                firebaseAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Sign in completed!",Toast.LENGTH_LONG).show();
                            Intent dash = new Intent(MainActivity.this,MainActivity_Dashboard.class);
                            dash.putExtra("mail",email);
                            dash.putExtra("pass",pass);

                            startActivity(dash);
                        }else{
                            Toast.makeText(getApplicationContext(),"Sign in NOT completed!",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
        btnquenmk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText resetmail = new EditText(v.getContext());
                AlertDialog.Builder passresetDialog = new AlertDialog.Builder(v.getContext());
                passresetDialog.setTitle("Reset Password!");
                passresetDialog.setMessage("Enter your email. you received reset link yourself");
                passresetDialog.setView(resetmail);

                passresetDialog.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String email = resetmail.getText().toString().trim();
                        firebaseAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(),"Reset link sent to your email",Toast.LENGTH_LONG).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(),"Err! Reset link is not sent yourself "+e.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                });
                passresetDialog.setNegativeButton("Unaccept", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                passresetDialog.create().show();
            }
        });
    }
}