package com.charalambos.pharmaciescy.Support;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.charalambos.pharmaciescy.R;

import javax.mail.MessagingException;

public class ContactUsActivity extends AppCompatActivity {
    EditText nameEditText, emailEditText, messageEditText;
    Button sendButton;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        AsyncTask.execute(this::setThreadPolicy);
        configureToolbar();
        configureViews();
        setEditTextValidators();
    }

    private void setThreadPolicy() {
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    private void configureToolbar() {
        toolbar = findViewById(R.id.contactUsToolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void configureViews() {
        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        messageEditText = findViewById(R.id.messageEditText);
        sendButton = findViewById(R.id.sendButton);
        sendButton.setOnClickListener(this::sendButtonCallback);
    }

    private void setEditTextValidators() {
        nameEditText.addTextChangedListener(new TextValidator(nameEditText) {
            @Override
            public void validate(TextView textView, String text) {
                if (!text.matches(fullNamePattern)) {
                    textView.setError(getString(R.string.full_name_wrong));
                } else {
                    textView.setError(null);
                }
            }
        });
        emailEditText.addTextChangedListener(new TextValidator(emailEditText) {
            @Override
            public void validate(TextView textView, String text) {
                if (!text.matches(emailPattern)) {
                    textView.setError(getString(R.string.email_wrong));
                } else {
                    textView.setError(null);
                }
            }
        });
        messageEditText.addTextChangedListener(new TextValidator(messageEditText) {
            @Override
            public void validate(TextView textView, String text) {
                if (text.length() == 0) {
                    textView.setError(getString(R.string.message_empty));
                } else {
                    textView.setError(null);
                }
            }
        });
    }

    public void sendButtonCallback(View view) {
        if (checkForErrors()) {
            Toast.makeText(this, R.string.fill_form_prompt,Toast.LENGTH_SHORT).show();
            return;
        }
        AsyncTask.execute(this::sendEmail);
    }

    private void sendEmail() {
        try {
            String name = nameEditText.getText().toString();
            String fromEmail = emailEditText.getText().toString();
            String toEmail = "cmaxoutis79@gmail.com";
            String subject = "Αίτημα βοηθείας";
            String message = messageEditText.getText().toString();
            EmailSender emailSender = new EmailSender("cmaxoutis79@gmail.com","tinxbwjtwnnxpadi");
            EmailBody emailBody = new EmailBody.EmailBodyBuilder()
                    .setEmail(fromEmail)
                    .setName(name)
                    .setMessage(message)
                    .build();
            emailSender.sendMail(subject,emailBody.getMessageFinal(),fromEmail,toEmail);
            runOnUiThread(this::toastEmailSentSuccessfully);
        } catch (MessagingException messagingException) {
            runOnUiThread(this::toastEmailSentFailed);
            Log.e("SEND EMAIL", messagingException.toString(), messagingException);
        }
    }

    private void toastEmailSentSuccessfully() {
        Toast.makeText(this, R.string.email_sent_successfully,Toast.LENGTH_SHORT).show();
    }

    private void toastEmailSentFailed() {
        Toast.makeText(this, R.string.email_sent_unsuccessfully,Toast.LENGTH_LONG).show();
    }

    private boolean checkForErrors() {
        boolean isErrors = false;
        if (nameEditText.getError() != null || nameEditText.getText().toString().isEmpty()) {
            nameEditText.setError(getString(R.string.full_name_empty));
            isErrors = true;
        }
        if (emailEditText.getError() != null || emailEditText.getText().toString().isEmpty()) {
            emailEditText.setError(getString(R.string.email_empty));
            isErrors = true;
        }
        if (messageEditText.getError() != null || messageEditText.getText().toString().isEmpty()) {
            messageEditText.setError(getString(R.string.message_empty));
            isErrors = true;
        }
        return isErrors;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}