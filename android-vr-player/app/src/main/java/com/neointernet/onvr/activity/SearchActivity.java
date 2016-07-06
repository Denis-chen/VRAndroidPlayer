package com.neointernet.onvr.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.neointernet.onvr.R;
import com.neointernet.onvr.fragment.SearchRecordListFragment;
import com.neointernet.onvr.fragment.VideoListViewFragment;
import com.neointernet.onvr.listener.SearchWordClickListener;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;

public class SearchActivity extends BaseActivity implements SearchWordClickListener{

    private final static String TAG = "SearchActivity";

    private EditText editText;
    private ArrayList<String> strings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Fragment fragment = null;
        try {
            fragment = SearchRecordListFragment.class.newInstance();
            fragment.setArguments(new Bundle());
        } catch (Exception e) {
            e.printStackTrace();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.search_fragment, fragment).commit();

        Toolbar toolbar = (Toolbar) findViewById(R.id.search_toobar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Log.d(TAG, "strings : " + strings);
        editText = (EditText) findViewById(R.id.search_edit_text);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    searchWordsCheckAndInit();
                    replaceFragmentAndSendSearchWord(editText.getText().toString());
                    putSharedPreference();
                }
                return false;
            }
        });
    }

    private void searchWordsCheckAndInit() {
        String arrayString = getSharedPreferences("search", MODE_PRIVATE).getString("array", "[]");
        if (!arrayString.equals("[]")) {
            strings = new ArrayList<>(Arrays.asList(arrayString.substring(1, arrayString.length() - 1).split(", ")));
            Log.d(TAG, "arrayString is not null");
            Log.d(TAG, arrayString);
        } else {
            strings = new ArrayList<>();
            Log.d(TAG, "arrayString is NULL");
        }
    }

    private void replaceFragmentAndSendSearchWord(String word) {
        Fragment fragment2 = new VideoListViewFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("type", "search");
        String result = "";
        Log.d(TAG, "replaceFragmentAndSendSearchWord : " + word);
        strings.add(word);
        try {
            result = URLEncoder.encode(word, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        bundle.putSerializable("value", result);
        fragment2.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.search_fragment, fragment2).commit();
    }

    private void putSharedPreference() {
        SharedPreferences preferences = getSharedPreferences("search", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("array", strings.toString());
        editor.commit();
    }

    @Override
    public void onClick(String word) {
        Log.d(TAG, "onClick word : " + word);
        searchWordsCheckAndInit();
        replaceFragmentAndSendSearchWord(word);
        editText.setText(word);
    }
}
