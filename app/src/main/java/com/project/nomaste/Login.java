package com.project.nomaste;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.android.material.textfield.TextInputEditText;
import com.project.nomaste.Network.HyperTextRequester;
import com.project.nomaste.R;
import com.project.nomaste.ui.LoginMenuFragment;
import com.project.nomaste.ui.LoginScreenFragment;


import java.io.IOException;
import java.net.URL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class Login extends AppCompatActivity {
    LoginScreenFragment loginScreenFragment;
    LoginMenuFragment loginMenuFragment;
    ProgressBar pb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        this.loginMenuFragment = new LoginMenuFragment();
        this.loginScreenFragment = new LoginScreenFragment();

        pb = findViewById(R.id.progressBar_login);
        loadFragment(loginMenuFragment);

    }
    private void loadFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.login_container,fragment);
        transaction.commit();
    }
    public void loadLoginMenu(){
        loadFragment(loginMenuFragment);
    }
    public void loadLoginScreen(){
        loadFragment(loginScreenFragment);
    }
    public void showProgress(){
        pb.setVisibility(View.VISIBLE);
    }
    public void hideProgress(){
        pb.setVisibility(View.INVISIBLE);
    }
    public void goToMain(){
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
    }
    public void goToRegister(){
        startActivity(new Intent(getApplicationContext(),Register.class));
    }

}
