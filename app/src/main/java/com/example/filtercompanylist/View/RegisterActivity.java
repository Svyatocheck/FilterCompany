package com.example.filtercompanylist.View;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.filtercompanylist.Controller.DatabaseAdapter;
import com.example.filtercompanylist.Model.GMailSender;
import com.example.filtercompanylist.R;

public class RegisterActivity extends AppCompatActivity {

    Button registerBTN;
    Button backBTN;

    EditText login;
    EditText mail;
    EditText pwd;

    DatabaseAdapter dbAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        dbAdapter = new DatabaseAdapter(RegisterActivity.this);
        registerBTN = (Button) findViewById(R.id.register);

        login = (EditText)findViewById(R.id.LoginRegister);
        mail = (EditText) findViewById(R.id.emailRegister);
        pwd = (EditText) findViewById(R.id.pwdRegister);

        backBTN = (Button) findViewById(R.id.backToLogin);

        registerBTN.setOnClickListener(v ->{

            if ((!login.getText().toString().equals("")) &&
                    !(mail.getText().toString().equals("")) &&
                    (!pwd.getText().toString().equals("")))
            {
                if (pwd.length() > 8) {
                    dbAdapter.open();
                    dbAdapter.insertLoginUser(
                            login.getText().toString(),
                            mail.getText().toString(),
                            pwd.getText().toString());

                    dbAdapter.close();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                GMailSender sender = new GMailSender("svyatwork2@gmail.com",
                                        "*");
                                sender.sendMail("Registration in application", "Dear user! Your registration has been completed. Here is your registration data:\n"
                                                + "Login: " + login.getText().toString() + "\nPassword: " + pwd.getText().toString(),
                                        "svyatwork2@gmail.com", mail.getText().toString());

                            } catch (Exception e) {
                                Log.e("SendMail", e.getMessage(), e);
                                Toast.makeText(getApplicationContext(), "Your mail does not exist!", Toast.LENGTH_LONG).show();
                            }
                        }

                    }).start();

                    Toast.makeText(getApplicationContext(), "Your account added successfully!", Toast.LENGTH_LONG).show();
                    finish();

                } else {
                    Toast.makeText(getApplicationContext(), "Your password's length must be >= 5!", Toast.LENGTH_LONG).show();
                }
            }

        });

        backBTN.setOnClickListener(v ->{
            finish();
        });

    }
}
