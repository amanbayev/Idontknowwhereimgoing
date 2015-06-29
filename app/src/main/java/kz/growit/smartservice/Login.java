package kz.growit.smartservice;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.rey.material.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;


public class Login extends AppCompatActivity {

    private EditText usernameET, passwordET;
    private Button login, register, cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usernameET = (EditText) findViewById(R.id.usernameLoginEditText);
        passwordET = (EditText) findViewById(R.id.passwordLoginEditText);

        login = (Button) findViewById(R.id.loginLoginButton);
        register = (Button) findViewById(R.id.registerLoginButton);
        cancel = (Button) findViewById(R.id.cancelLoginButton);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean ok = true;
                boolean comma = false;
                String errMessage = "";
                String username = usernameET.getText().toString().trim();
                String password = passwordET.getText().toString().trim();
                if (username.length() != 11) {
                    ok = false;
                    comma = true;
                    errMessage = getResources().getString(R.string.invalid_username_error_ru);
                    usernameET.setError(getResources().getString(R.string.invalid_username_error_ru));
                }
                if (password.length() < 6 || password.length() > 30) {
                    ok = false;
                    if (comma) errMessage += getResources().getString(R.string.and_ru);
                    errMessage += getResources().getString(R.string.invalid_password_error_ru);
                    passwordET.setError(getResources().getString(R.string.invalid_password_error_ru));
                }
                if (!ok) {
                    Toast.makeText(Login.this, getResources().getString(R.string.error_ru) + errMessage, Toast.LENGTH_LONG).show();
                    usernameET.setText("");
                    passwordET.setText("");
                    usernameET.requestFocus();
                } else {
                    String url = "http://smartservice.kz/token";
                    JSONObject data = new JSONObject();
                    try {
                        data.put("grant_type", "password");
                        data.put("username", username);
                        data.put("password", password);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                            Request.Method.POST,
                            url,
                            AppController.getInstance().jsonToUrlEncodedString(data),
                            //"grant_type=password&username=77019356386&password=!23Abc",
                            //"{\"grant_type\":\"password\",\"username\":\"77019356386\",\"password\":\"!23Abc\"}",
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    SharedPreferences prefs = getPreferences(MODE_PRIVATE);
                                    if (response != null) {
                                        AppController.getInstance().setToken(response);
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d("login", error.getLocalizedMessage().toString());
                                }
                            }
                    );
                    AppController.getInstance().addToRequestQueue(jsonObjectRequest, "login");
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
