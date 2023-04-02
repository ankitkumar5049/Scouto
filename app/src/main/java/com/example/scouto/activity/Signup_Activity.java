package com.example.scouto.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
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

public class Signup_Activity extends AppCompatActivity {

    EditText username, mobile, email, password;
    Button signIn, signUp;
    TextView txtSignup;
    Database DB;
    RelativeLayout mainLayout;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        username = findViewById(R.id.username);
        mobile = findViewById(R.id.mobile);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        signUp = findViewById(R.id.singup);
        txtSignup = findViewById(R.id.txtSignUp);
        mainLayout = findViewById(R.id.mainLayout);

        DB = new Database(this);

        txtSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mainLayout.getWindowToken(), 0);
            }
        });


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mainLayout.getWindowToken(), 0);

                String user = username.getText().toString();
                String mob = mobile.getText().toString();
                String mail = email.getText().toString();
                String pass = password.getText().toString();

                if(user.equals("")||mob.equals("")||mail.equals("")||pass.equals("")){
                    Toast.makeText(Signup_Activity.this,"Fill all the details",Toast.LENGTH_SHORT).show();
                }
                else if(mob.length()<10){
                    Toast.makeText(Signup_Activity.this,"Invalid mobile number",Toast.LENGTH_SHORT).show();
                }
                else if(pass.length()<4){
                    Toast.makeText(Signup_Activity.this,"Minimum password length 4 !",Toast.LENGTH_SHORT).show();
                }
                else{
                    Boolean checkEmail = DB.checkUserEmail(mail);
                    Boolean checkMob = DB.checkUserMobile(mob);

                    if(checkEmail == false && checkMob == false){
                        Boolean insert = DB.insertData(user,mob,mail,pass);
                        Toast.makeText(Signup_Activity.this,"Registered Successfully",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(Signup_Activity.this,"Existing User !",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}