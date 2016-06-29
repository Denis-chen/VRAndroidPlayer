package com.neointernet.onvr.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.login.LoginManager;
import com.neointernet.onvr.R;
import com.neointernet.onvr.fragment.MyPageFragment;
import com.neointernet.onvr.fragment.OnListFragmentInteractionListener;
import com.neointernet.onvr.fragment.TypeViewFragment;
import com.neointernet.onvr.fragment.VideoListFragment;
import com.neointernet.onvr.model.Video;
import com.neointernet.onvr.util.BackPressCloseHandler;
import com.neointernet.onvr.util.MyDownloadManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        OnListFragmentInteractionListener {

    private BackPressCloseHandler backPressCloseHandler;
    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawer;
    private MyDownloadManager myDownloadManager;
    private final static String URL = "http://lifejeju99.cafe24.com/videos/";

    private AccessToken accessToken;
    private String mem_nickname;
    private AccessTokenTracker accessTokenTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //사용자의 닉네임을 받아옴.
        Intent intent = getIntent();
        accessToken = AccessToken.getCurrentAccessToken();
        mem_nickname = intent.getExtras().getString("mem_nickname");
        Toast.makeText(getApplicationContext(), accessToken.getUserId() + " " + mem_nickname, Toast.LENGTH_LONG).show();

        //현재 토큰을 감시하여 로그인 세션이 끊어지면 로그인 페이지로 이동
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                if (currentAccessToken == null) {
                    Intent loginIntent = new Intent(MainActivity.this, LoginSplashActivity.class);
                    startActivity(loginIntent);
                    finish();
                }
            }
        };
        accessTokenTracker.startTracking();

        initView();
    }

    private void initView() {
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        onNavigationItemSelected(navigationView.getMenu().getItem(0));
        backPressCloseHandler = new BackPressCloseHandler(this);

        myDownloadManager = new MyDownloadManager(this);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            backPressCloseHandler.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            LoginManager.getInstance().logOut();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        Fragment fragment = null;
        Bundle bundle = new Bundle();
        ;
        Class fragmentClass;
        switch (item.getItemId()) {
            case R.id.nav_home:
                fragmentClass = VideoListFragment.class;
                break;
            case R.id.nav_mypage:
                fragmentClass = MyPageFragment.class;
                break;
            case R.id.nav_entertainment:
                fragmentClass = TypeViewFragment.class;
                bundle.putSerializable("type", "entertainment");
                break;
            case R.id.nav_event:
                fragmentClass = TypeViewFragment.class;
                bundle.putSerializable("type", "event");
                break;
            case R.id.nav_extreme:
                fragmentClass = TypeViewFragment.class;
                bundle.putSerializable("type", "extreme");
                break;
            case R.id.nav_musicvideo:
                fragmentClass = TypeViewFragment.class;
                bundle.putSerializable("type", "musicvideo");
                break;
            case R.id.nav_game:
                fragmentClass = TypeViewFragment.class;
                bundle.putSerializable("type", "game");
                break;
            case R.id.nav_location:
                fragmentClass = TypeViewFragment.class;
                bundle.putSerializable("type", "location");
                break;
            default:
                fragmentClass = TypeViewFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
            fragment.setArguments(bundle);
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.flContent, fragment, null);
        fragmentTransaction.commit();

        item.setChecked(true);
        setTitle(item.getTitle());
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override
    public void onListFragmentInteraction(Video item) {
        Intent intent = new Intent(MainActivity.this, VideoActivity.class);
        String path;
        if (myDownloadManager.checkExistFile(item.getVideoFilename()))
            path = myDownloadManager.getStoragePath();
        else
            path = URL;
        intent.putExtra("videopath", path + item.getVideoFilename());
        intent.putExtra("videoTitle", item.getVideoTitle());
        sendHits("http://lifejeju99.cafe24.com/video_hit_increment.php", item.getVideoId());
        startActivity(intent);
    }

    private void sendHits(String uri, long videoId) {

        class SendData extends AsyncTask<String, Void, String> {

            private String TAG = "SendDataClass";

            @Override
            protected String doInBackground(String... strings) {

                String uri = strings[0];
                String videoId = strings[1];

                HttpURLConnection conn = null;
                try {
                    java.net.URL url = new java.net.URL(uri + "?video_id=" + videoId);
                    conn = (HttpURLConnection) url.openConnection();

                    int responseCode = conn.getResponseCode();
                    Log.d(TAG, "Response Code : " + responseCode);

                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(conn.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    Log.d(TAG, response.toString());

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (conn != null)
                        try {
                            conn.disconnect();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                }
                return null;
            }
        }

        SendData sendData = new SendData();
        sendData.execute(uri, String.valueOf(videoId));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
    }
}