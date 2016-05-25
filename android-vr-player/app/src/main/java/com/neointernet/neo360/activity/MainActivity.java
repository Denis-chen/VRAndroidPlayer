package com.neointernet.neo360.activity;

import android.content.Intent;
import android.net.Uri;
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
import com.neointernet.neo360.R;
import com.neointernet.neo360.fragment.MyPageFragment;
import com.neointernet.neo360.fragment.UploadFragment;
import com.neointernet.neo360.fragment.VideoListFragment;
import com.neointernet.neo360.model.Video;
import com.neointernet.neo360.util.BackPressCloseHandler;
import com.neointernet.neo360.util.MyDownloadManager;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, VideoListFragment.OnListFragmentInteractionListener, UploadFragment.OnFragmentInteractionListener, MyPageFragment.OnFragmentInteractionListener {

    private BackPressCloseHandler backPressCloseHandler;
    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawer;
    private MyDownloadManager myDownloadManager;
    private final static String URL = "http://lifejeju99.cafe24.com/";
    private final static String TAG = "MainActivity";

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
                    Intent intent2 = new Intent(MainActivity.this, LoginSplashActivity.class);
                    startActivity(intent2);
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

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        Fragment fragment = null;
        Class fragmentClass;
        switch (item.getItemId()) {
            case R.id.nav_camera:
                fragmentClass = VideoListFragment.class;
                break;
            case R.id.nav_gallery:
                fragmentClass = UploadFragment.class;
                break;
            case R.id.nav_manage:
                fragmentClass = MyPageFragment.class;
                break;
            default:
                fragmentClass = MyPageFragment.class;
                break;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
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
        Log.i("MainActivity", item.getName());
        Intent intent = new Intent(MainActivity.this, VideoActivity.class);
        String path;
        if (myDownloadManager.checkExistFile(item.getName()))
            path = myDownloadManager.getStoragePath();
        else
            path = URL;
        intent.putExtra("videopath", path + item.getName());
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}