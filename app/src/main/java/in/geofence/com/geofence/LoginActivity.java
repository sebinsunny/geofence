package in.geofence.com.geofence;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.util.HashMap;

public class LoginActivity extends Activity implements View.OnClickListener {
    final String LOG ="LoginActivity";
    Button btnLogin,b1;
    EditText etUsername,etPassword,user,pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername= (EditText)findViewById(R.id.etUsername);
        etPassword= (EditText) findViewById(R.id.etPassword);
        user= (EditText) findViewById(R.id.user);
        pass= (EditText) findViewById(R.id.pass);
        btnLogin= (Button) findViewById(R.id.btnLogin);
        b1= (Button) findViewById(R.id.b1);
        btnLogin.setOnClickListener(this);
        b1.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnLogin:
                HashMap postData = new HashMap();
                final String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                postData.put("tusername", username);
                postData.put("tpassword", password);
                PostResponseAsyncTask task1 = new PostResponseAsyncTask(LoginActivity.this, postData, new AsyncResponse() {
                    @Override
                    public void processFinish(String s) {
                        //     Log.d(LOG, s);
                        if (s.contains("success")) {
                            Toast.makeText(LoginActivity.this, "successfully login", Toast.LENGTH_LONG).show();
                            Intent in = new Intent(LoginActivity.this, AndroidGPSTrackingActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("s", username);
                            in.putExtras(bundle);


                            startActivity(in);

                        } else {
                            Toast.makeText(LoginActivity.this, "try again", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                task1.execute("http://geofence.in/check/android.php");
                   break;
            case R.id.b1:
                HashMap postDatas = new HashMap();
                String usernames = user.getText().toString();
                String passwords = pass.getText().toString();

                postDatas.put("username", usernames);
                postDatas.put("password", passwords);
                PostResponseAsyncTask task2 = new PostResponseAsyncTask(LoginActivity.this, postDatas, new AsyncResponse() {
                    @Override
                    public void processFinish(String d) {
                        //     Log.d(LOG, s);
                        if (d.contains("success")) {
                           // Toast.makeText(LoginActivity.this, "successfully Registered", Toast.LENGTH_LONG).show();
                            //Intent in = new Intent(LoginActivity.this, MainActivity.class);
                            //startActivity(in);

                        } else {
                            Toast.makeText(LoginActivity.this, "successfully Registered", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                task2.execute("http://geofence.in/check/reg.php");
                break;

        }
    }
}
