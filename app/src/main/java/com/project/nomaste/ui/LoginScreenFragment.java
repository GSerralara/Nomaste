package com.project.nomaste.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.project.nomaste.Login;
import com.project.nomaste.MainActivity;
import com.project.nomaste.R;
import com.project.nomaste.Register;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class LoginScreenFragment extends Fragment{
    TextInputEditText mail, password;
    TextView back;
    Button loginButton;
    FirebaseAuth auth;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the fragment layout
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        // Get a reference to the ImageView in the fragment Layout
        mail = rootView.findViewById(R.id.email_login_input);
        password = rootView.findViewById(R.id.password_login_input);
        loginButton = rootView.findViewById(R.id.login_button);
        back = rootView.findViewById(R.id.RegisterMenu);
        auth = FirebaseAuth.getInstance();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Login) getActivity()).goToRegister();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_mail = mail.getText().toString().trim();
                String str_pwd = password.getText().toString().trim();

                //check the fields
                if(TextUtils.isEmpty(str_mail)){
                    mail.setError("Email is required");
                    return;
                }
                if(TextUtils.isEmpty(str_pwd)){
                    password.setError("Password is required");
                    return;
                }
                if (str_pwd.length() < 6){
                    password.setError("Password must have minimum of 6 characters");
                    return;
                }

                ((Login) getActivity()).showProgress();
                //login the user

                auth.signInWithEmailAndPassword(str_mail,str_pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            ((Login) getActivity()).hideProgress();
                            Toast.makeText(((Login) getActivity()),"Login Success",Toast.LENGTH_SHORT).show();
                            ((Login) getActivity()).goToMain();
                        }else {
                            Toast.makeText(((Login) getActivity()),"Error: "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                        ((Login) getActivity()).hideProgress();
                    }
                });

            }
        });

        return rootView;
    }

}
