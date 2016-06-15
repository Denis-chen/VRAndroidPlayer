package com.neointernet.onvr.activity;

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
import com.neointernet.onvr.R;
import com.neointernet.onvr.listener.AsyncTaskListener;
import com.neointernet.onvr.model.Member;
import com.neointernet.onvr.util.MemberJsonManager;

import java.util.ArrayList;

public class SignUpActivitiy extends AppCompatActivity implements AsyncTaskListener {

    private TextInputLayout textInputLayout;
    private EditText inputNickName;
    private Button submitBtn;
    private MemberJsonManager memberJsonManager;
    private AccessToken accessToken;
    private ArrayList<Member> memberArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //토큰을 가져오고 닉네임을 얻기 위함
        accessToken = AccessToken.getCurrentAccessToken();
        memberJsonManager = new MemberJsonManager();
        String url = "http://lifejeju99.cafe24.com/mem_list.php?mem_id=" + accessToken.getUserId();
        memberJsonManager.makeJsonData(url, this);
        memberArrayList = new ArrayList<>();
    }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    public void asynkTaskFinished() {
        //닉네임을 가져오면 콜백 메소드가 실행됨
        memberArrayList.addAll(memberJsonManager.getMemberArrayList());

        //아이디가 있으면
        if (memberArrayList.size() != 0) {
            String mem_nickname = memberArrayList.get(0).getMemNickname();
            goToMain(mem_nickname);
        } else { //아이디가 없으면 SignUp 화면 생성
            setContentView(R.layout.activity_sign_up_activitiy);
            textInputLayout = (TextInputLayout) findViewById(R.id.text_input_layout);
            inputNickName = (EditText) findViewById(R.id.nick_name);

            inputNickName.addTextChangedListener(new MyTextWatcher()); //닉네임은 4글자 이상이여야함.
            submitBtn = (Button) findViewById(R.id.summit_button);
            submitBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!validNickName()) {
                        return;
                    }
                    Toast.makeText(getApplicationContext(), "Thank you!", Toast.LENGTH_LONG).show();
                    memberJsonManager.sendJson("http://lifejeju99.cafe24.com/insert_mem.php", accessToken.getUserId(), inputNickName.getText().toString().trim()); //아이디 DB에 등록
                    goToMain(inputNickName.getText().toString().trim());
                }
            });
        }

    }

    private void goToMain(String mem_nickname) {
        Intent intent = new Intent(SignUpActivitiy.this, MainActivity.class);
        intent.putExtra("mem_nickname", mem_nickname);
        startActivity(intent);
        finish();
    }

    private class MyTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            validNickName();
        }
    }

    private boolean validNickName() {
        String nickname = inputNickName.getText().toString().trim();
        Log.d("ValidNickName() : ", nickname);
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
}
