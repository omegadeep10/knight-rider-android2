package edu.mga.knight_rider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.Button;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameInput;
    private EditText passwordInput;
    private ProgressBar loginProgressBar;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameInput = findViewById(R.id.loginUsername);
        passwordInput = findViewById(R.id.loginPassword);
        loginProgressBar = findViewById(R.id.progressBar);
        prefs = this.getSharedPreferences("edu.mga.knightrider", Context.MODE_PRIVATE);
    }

    private Boolean validLoginForm() {
        String usernameVal = usernameInput.getText().toString();
        String passwordVal = passwordInput.getText().toString();

        Boolean isValid = true;

        if (usernameVal == null || usernameVal.equals("")) {
            usernameInput.setError("Username is required.");
            isValid = false;
        }

        if (passwordVal == null || passwordVal.equals("")) {
            passwordInput.setError("Password is required.");
            isValid = false;
        }

        return isValid;
    }

    public void Login(View view) {
        if (!validLoginForm()) {
            return;
        }

        final Button loginButton = (Button) view; // Get our button
        loginButton.setEnabled(false);      // Disable button while loading view/
        loginButton.setText("");
        loginProgressBar.setVisibility(View.VISIBLE);

        JsonObject loginParams = new JsonObject();
        loginParams.addProperty("username", usernameInput.getText().toString());
        loginParams.addProperty("password", passwordInput.getText().toString());

        Ion.with(view.getContext())
            .load("http://knightrider.mgaitec.net:8080/ridesharing/auth/login")
            .setJsonObjectBody(loginParams)
            .asJsonObject()
            .withResponse()
            .setCallback(new FutureCallback<Response<JsonObject>>() {
                @Override
                public void onCompleted(Exception e, Response<JsonObject> result) {
                    if (e != null) Toast.makeText(getBaseContext(), "Unable to access server. Try again later.", Toast.LENGTH_LONG).show();

                    if (result.getHeaders().code() == 401) {
                        Toast.makeText(getBaseContext(), "Invalid username or password.", Toast.LENGTH_LONG).show();
                    }

                    if (result.getHeaders().code() == 200) {
                        String token = result.getResult().get("token").toString();
                        String userId = result.getResult().get("user_id").toString();
                        prefs.edit().putString("knight-rider-token", token).apply();
                        prefs.edit().putString("knight-rider-userid", userId).apply();

                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }

                    loginProgressBar.setVisibility(View.GONE);
                    loginButton.setEnabled(true);
                    loginButton.setText("LOG IN");
                }
            });
    }
}
