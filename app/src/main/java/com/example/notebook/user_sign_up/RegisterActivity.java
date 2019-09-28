package com.example.notebook.user_sign_up;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.notebook.MainActivity;
import com.example.notebook.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword;

    private FirebaseAuth fAuth;
    private Button btnReg;
    private TextInputLayout inName, inEmail, inPass;
    private DatabaseReference fUsersDatabase;
    private ProgressDialog PD;



    @Override    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //Нахожу поля и кнопку для регистрации
        btnReg = (Button)findViewById(R.id.btn_reg);
        inName = (TextInputLayout) findViewById(R.id.input_reg_name);
        inEmail = (TextInputLayout) findViewById(R.id.input_reg_email);
        inPass = (TextInputLayout) findViewById(R.id.input_reg_pass);


        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);

//        btnSignUp.setOnClickListener(new View.OnClickListener() {
//            @Override            public void onClick(View view) {
//                final String email = inputEmail.getText().toString();
//                final String password = inputPassword.getText().toString();
//
//                try {
//                    if (password.length() > 0 && email.length() > 0) {
//                        PD.show();
//                        auth.createUserWithEmailAndPassword(email, password)
//                                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<AuthResult> task) {
//                                        if (!task.isSuccessful()) {
//                                            Toast.makeText(
//                                                    RegisterActivity.this,
//                                                    "Authentication Failed",
//                                                    Toast.LENGTH_LONG).show();
//                                            Log.v("error", task.getResult().toString());
//                                        } else {
//                                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
//                                            startActivity(intent);
//                                            finish();
//                                        }
//                                        PD.dismiss();
//                                    }
//                                });
//                    } else {
//                        Toast.makeText(
//                                RegisterActivity.this,
//                                "Fill All Fields",
//                                Toast.LENGTH_LONG).show();
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });

//        btnLogin.setOnClickListener(new View.OnClickListener() {
//            @Override            public void onClick(View view) {
//                finish();
//            }
//        });

        fAuth = FirebaseAuth.getInstance();
        fUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");



        // Назначаю на кнопку слушатель, если кнопка нажмется выполниться операция регистрации пользователя
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Получаем значения из полей в виде строк - имя, пароль, емейл
                String uname = inName.getEditText().getText().toString().trim();
                String uemail = inEmail.getEditText().getText().toString().trim();
                String upass = inPass.getEditText().getText().toString().trim();

                registerUser( uname, uemail, upass);
            }
        });
    }

    //метод для регистрации пользователя, параметры имя пользователя, его емейл и пароль
    private void registerUser(final String name, String email, String password){
        PD = new ProgressDialog(this);
        PD.setMessage("Processing your request, please wait ...");
        PD.show();
        PD.setCancelable(true);
        PD.setCanceledOnTouchOutside(false);
        /*создаем Юзера и назначаем на экземпляре объекта FirebaseAuth  слушатель OnCompleteListener
       у которого параметр типа Task<AuthResult>. По результатам работы слушателя, если результат положительный
       то
         */
        fAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener( this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user = fAuth.getCurrentUser();
                            fUsersDatabase.child(fAuth.getCurrentUser().getUid())
                                    .child("basic").child("name").setValue(name)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                PD.dismiss();
                                                Intent mainActivityIntent = new Intent(RegisterActivity.this, MainActivity.class);
                                                startActivity(mainActivityIntent);
                                                finish();
                                                Toast.makeText(RegisterActivity.this, "User created!", Toast.LENGTH_SHORT).show();
                                            }else{
                                                PD.dismiss();
                                                Toast.makeText(RegisterActivity.this,
                                                        "ERROR: "+ task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                        }else{
                            PD.dismiss();
                            Toast.makeText(RegisterActivity.this,
                                    "ERROR: "+ task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }
}
