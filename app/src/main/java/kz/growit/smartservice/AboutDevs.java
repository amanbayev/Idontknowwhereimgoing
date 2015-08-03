package kz.growit.smartservice;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.dd.CircularProgressButton;
import com.mikepenz.materialdrawer.Drawer;


public class AboutDevs extends AppCompatActivity {
    private EditText someInfoForUs;
    private com.dd.CircularProgressButton sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_devs);
        Toolbar tolbar = (Toolbar) findViewById(R.id.toolbarAboutDevs);
        setSupportActionBar(tolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Drawer drawer = AppController.getInstance().getDrawer(this, tolbar);
        someInfoForUs = (EditText) findViewById(R.id.contactFormContactDevsEditText);
        sendButton = (CircularProgressButton) findViewById(R.id.contactDevsCircularButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendButton.setIndeterminateProgressMode(true);
                sendButton.setProgress(50);
                Intent sendEmail = new Intent(Intent.ACTION_SEND);
                sendEmail.setData(Uri.parse("mailto:"));
                sendEmail.setType("text/plain");
                String[] TO = {"info@smartservice.kz"};
                String[] CC = {"info@growit.kz"};
                sendEmail.putExtra(Intent.EXTRA_EMAIL, TO);
                sendEmail.putExtra(Intent.EXTRA_CC, CC);
                sendEmail.putExtra(Intent.EXTRA_SUBJECT,"SmartService mobile app Android - from user");
                sendEmail.putExtra(Intent.EXTRA_TEXT, someInfoForUs.getText());
                try {
                    startActivity(Intent.createChooser(sendEmail, "Отправить email..."));
                    sendButton.setProgress(100);
                } catch (android.content.ActivityNotFoundException e) {
                    Snackbar.make(sendButton,"Не удалось отправить сообщение",Snackbar.LENGTH_LONG).show();
                    sendButton.setProgress(-1);
                }
            }
        });
    }

}
