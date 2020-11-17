package com.example.blacklight;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class FeedbackActivity extends AppCompatActivity {

    private Toolbar feedbackToolbar;
    private TextView feedbackTitle;
    private EditText feedbackEmailFrom, feedbackEmailSubject, feedbackEmailMessage;
    private Button sendFeedbackBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        feedbackToolbar = findViewById(R.id.feedback_toolbar);
        feedbackTitle = findViewById(R.id.feedback_introduction);
        feedbackEmailFrom = findViewById(R.id.feedback_email_from);
        feedbackEmailSubject = findViewById(R.id.feedback_email_subject);
        feedbackEmailMessage = findViewById(R.id.feedback_email_message);
        sendFeedbackBtn = findViewById(R.id.feedback_email_button);

        setSupportActionBar(feedbackToolbar);
    }

    public void redirectToHomeStore(View view) {
        Intent intent = new Intent(FeedbackActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    public void getFeedback(View view) {
    }
}