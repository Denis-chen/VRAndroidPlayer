package com.neointernet.neo360.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.neointernet.neo360.R;

public class SignUpActivitiy extends AppCompatActivity {

    private TextInputLayout textInputLayout;
    private EditText inputNickName;
    private Button submitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_activitiy);

        textInputLayout = (TextInputLayout) findViewById(R.id.text_input_layout);
        inputNickName = (EditText) findViewById(R.id.nick_name);

        inputNickName.addTextChangedListener(new MyTextWatcher(inputNickName));

        submitBtn = (Button) findViewById(R.id.summit_button);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validNickName()) {
                    return;
                }
                Toast.makeText(getApplicationContext(), "Thank you!", Toast.LENGTH_LONG).show();

                Intent getIntent = getIntent();
                AccessToken accessToken = (AccessToken) getIntent.getExtras().get("mem_token");

                Intent intent = new Intent(SignUpActivitiy.this, MainActivity.class);
                intent.putExtra("mem_token", accessToken);
                intent.putExtra("mem_nick_name", inputNickName.getText().toString().trim());
                startActivity(intent);
                finish();
            }
        });
    }

    private boolean validNickName() {
        String nickname = inputNickName.getText().toString().trim();
        Log.d("ValidNickName() : " , nickname);
        if (nickname.length() < 4) {
            textInputLayout.setError(getString(R.string.err_msg_name));
            requestFocus(inputNickName);
            return false;
        } else {
            textInputLayout.setError(null);
            Log.d("Valid!!", nickname);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class MyTextWatcher implements TextWatcher {
        private final View view;

        public MyTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            validNickName();
            Log.d("afterTextChanged", inputNickName.getText().toString().trim());
        }
    }
}
