package le.minh.authentication_login_resgiter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity_Dashboard extends AppCompatActivity {
    TextView email,pass,textverify;
    Button btndx,btnVerify;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    String userid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__dashboard);
        email =findViewById(R.id.textEmail);
        pass= findViewById(R.id.textPass);
        btndx= findViewById(R.id.btnDN2);
        textverify= findViewById(R.id.textVerify);
        btnVerify= findViewById(R.id.btnVeri);
        Intent dn = getIntent();
        Bundle bundle = dn.getExtras();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore  = FirebaseFirestore.getInstance();
        userid = firebaseAuth.getCurrentUser().getUid();
        FirebaseUser fuser = firebaseAuth.getCurrentUser();
        if(!fuser.isEmailVerified()){
            textverify.setVisibility(View.VISIBLE);
            btnVerify.setVisibility(View.VISIBLE);

            btnVerify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(v.getContext(),"Verification email has been sent",Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(v.getContext(),"Can not Verification email has been sent",Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
        }


        if(bundle!=null){
            String mail = (String) bundle.get("mail");
            String passw = (String) bundle.get("pass");
            email.setText(mail);
            pass.setText(passw);

        }
        btndx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        });

    }
}