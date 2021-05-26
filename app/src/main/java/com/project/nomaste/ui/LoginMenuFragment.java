package com.project.nomaste.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

// import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuth;
import com.project.nomaste.Login;
import com.project.nomaste.MainActivity;
import com.project.nomaste.R;
import com.project.nomaste.Register;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class LoginMenuFragment extends Fragment {
    FirebaseAuth auth;
    Button normalLoginButton;
    TextView signUpText;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the fragment layout
        View rootView = inflater.inflate(R.layout.fragment_login_menu, container, false);

        auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser() != null){
            startActivity(new Intent(((Login) getActivity()).getApplicationContext(), MainActivity.class));
            ((Login) getActivity()).finish();
        }

        normalLoginButton = rootView.findViewById(R.id.login);
        signUpText = rootView.findViewById(R.id.signUp);

        normalLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Login) getActivity()).loadLoginScreen();
            }
        });

        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Login) getActivity()).goToRegister();
            }
        });

        return rootView;
    }
}
