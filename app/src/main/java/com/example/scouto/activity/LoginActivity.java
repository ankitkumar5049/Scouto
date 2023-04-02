package com.example.scouto.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scouto.R;
import com.example.scouto.database.Database;

public class LoginActivity extends AppCompatActivity {

    EditText email_mob, password;
    Button signIn;
    Database DB;
    TextView signUpText;
    RelativeLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email_mob = findViewById(R.id.email_Mob);
        password = findViewById(R.id.password1);
        signIn = findViewById(R.id.singin1);
        signUpText = findViewById(R.id.txtSignUpText);
        mainLayout = findViewById(R.id.mainLayout);

        DB = new Database(this);

        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Signup_Activity.class);
                startActivity(intent);
            }
        });




        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mainLayout.getWindowToken(), 0);

                String mail_mob = email_mob.getText().toString();
                String pass = password.getText().toString();

                if(mail_mob.equals("") || pass.equals("")){
                    Toast.makeText(LoginActivity.this,"Please enter all the details !",Toast.LENGTH_SHORT).show();
                }
                else if (mail_mob.length()<10){
                    Toast.makeText(LoginActivity.this,"Incorrect mobile number !",Toast.LENGTH_SHORT).show();
                }
                else if (pass.length()<4){
                    Toast.makeText(LoginActivity.this,"Minimum password length 4 !",Toast.LENGTH_SHORT).show();
                }
                else{

                    Boolean checkPass1 = DB.checkUserPass1(mail_mob,pass);
                    //Boolean checkPass2 = DB.checkUserPass2(mail_mob,pass);

                    if(checkPass1 == true){
                        Toast.makeText(LoginActivity.this,"Login Successful",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        intent.putExtra("mobile",mail_mob);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(LoginActivity.this,"Invalid Credential",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}