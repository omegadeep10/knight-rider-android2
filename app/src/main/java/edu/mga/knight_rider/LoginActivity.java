package edu.mga.knight_rider;

import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameInput;
    private EditText passwordInput;
    private ProgressBar loginProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameInput = findViewById(R.id.loginUsername);
        passwordInput = findViewById(R.id.loginPassword);
        loginProgressBar = findViewById(R.id.progressBar);
    }

    public void Login(View view) {
        final Button loginButton = (Button) view; // Get our button
        loginButton.setEnabled(false);      // Disable button while loading view/
        loginButton.setText("");
        loginProgressBar.setVisibility(View.VISIBLE);

        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getBaseContext(), "This button should log you in.", Toast.LENGTH_LONG).show();
                loginProgressBar.setVisibility(View.GONE);
                loginButton.setEnabled(true);
                loginButton.setText("LOG IN");
            }
        }, 5000);
    }
}
