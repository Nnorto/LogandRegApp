package com.example.logandregapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.snackbar.Snackbar;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.logandregapp.models.users;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    Button btnsigin, btnreg;
    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference user;

    RelativeLayout root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        btnreg = findViewById(R.id.btnreg);
        btnsigin = findViewById(R.id.btnsigin);
        root = findViewById(R.id.root_element);
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        user = db.getReference("Users");
        btnreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRegisterWindow();
            }
        });
        btnsigin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSiginWindow();
            }
        });
    }
    private void showSiginWindow(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Войти.");
        dialog.setMessage("Пожалуйста, заполните данные для входа.");
        LayoutInflater inflater = LayoutInflater.from(this);
        View sig_in_w = inflater.inflate(R.layout.sigin_window, null);
        dialog.setView(sig_in_w);
        final EditText email = sig_in_w.findViewById(R.id.email);
        final EditText password = sig_in_w.findViewById(R.id.passf);


        dialog.setNegativeButton("Отменить.", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        });
        dialog.setPositiveButton("Войти.", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {

                if (TextUtils.isEmpty(email.getText().toString())) {
                    Snackbar.make(root, "Введите почту.", Snackbar.LENGTH_SHORT).show();
                    return;
                }


                if (password.getText().toString().length() < 8) {
                    Snackbar.make(root, "Введите пароль.", Snackbar.LENGTH_SHORT).show();
                    return;
                }

            auth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            startActivity(new Intent(MainActivity.this, MapActivity.class));
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Snackbar.make(root, "Ошибка авторизации."+e.getMessage(), Snackbar.LENGTH_SHORT).show();
                        }
                    });

            }
        });

        dialog.show();

    }

    private void showRegisterWindow() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Зарегистрироваться.");
        dialog.setMessage("Пожалуйста, заполните данные.");
        LayoutInflater inflater = LayoutInflater.from(this);
        View reg_w = inflater.inflate(R.layout.register_window, null);
        dialog.setView(reg_w);
        final EditText email = reg_w.findViewById(R.id.email);
        final EditText password = reg_w.findViewById(R.id.passf);
        final EditText name = reg_w.findViewById(R.id.name);
        final EditText phone = reg_w.findViewById(R.id.phonef);

        dialog.setNegativeButton("Отменить.", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        });
        dialog.setPositiveButton("Добавить.", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {

                if (TextUtils.isEmpty(email.getText().toString())) {
                    Snackbar.make(root, "Введите почту.", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(name.getText().toString())) {
                    Snackbar.make(root, "Введите имя.", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(phone.getText().toString())) {
                    Snackbar.make(root, "Введите телефон.", Snackbar.LENGTH_SHORT).show();
                    return;
                }


                if (password.getText().toString().length() < 8) {
                    Snackbar.make(root, "Введите пароль.", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                auth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                users Userr = new users();
                                Userr.setEmail(email.getText().toString());
                                Userr.setName(name.getText().toString());
                                Userr.setPass(password.getText().toString());
                                Userr.setPhone(phone.getText().toString());
                                user.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(Userr)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Snackbar.make(root, "Пользователь добавлен!", Snackbar.LENGTH_SHORT)
                                                        .show();
                                            }
                                        });

                            }
                        });

            }
        });

        dialog.show();
    }
}
