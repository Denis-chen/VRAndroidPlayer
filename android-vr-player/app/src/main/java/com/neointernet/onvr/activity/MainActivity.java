package com.neointernet.onvr.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.login.LoginManager;
import com.neointernet.onvr.R;
import com.neointernet.onvr.fragment.HomeFragment;
import com.neointernet.onvr.fragment.MyPageFragment;
import com.neointernet.onvr.fragment.VideoListViewFragment;
import com.neointernet.onvr.util.BackPressCloseHandler;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener{

    private BackPressCloseHandler backPressCloseHandler;
    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawer;

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
            LoginManager.getInstance().logOut();
            return true;
        } else if (id == R.id.action_search) {
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        Fragment fragment = null;
        Bundle bundle = new Bundle();
        bundle.putSerializable("type", "type");
        Class fragmentClass;
        switch (item.getItemId()) {
            case R.id.nav_home:
                fragmentClass = HomeFragment.class;
                break;
            case R.id.nav_mypage:
                fragmentClass = MyPageFragment.class;
                break;
            case R.id.nav_entertainment:
                fragmentClass = VideoListViewFragment.class;
                bundle.putSerializable("value", "entertainment");
                break;
            case R.id.nav_event:
                fragmentClass = VideoListViewFragment.class;
                bundle.putSerializable("value", "event");
                break;
            case R.id.nav_extreme:
                fragmentClass = VideoListViewFragment.class;
                bundle.putSerializable("value", "extreme");
                break;
            case R.id.nav_musicvideo:
                fragmentClass = VideoListViewFragment.class;
                bundle.putSerializable("value", "musicvideo");
                break;
            case R.id.nav_game:
                fragmentClass = VideoListViewFragment.class;
                bundle.putSerializable("value", "game");
                break;
            case R.id.nav_location:
                fragmentClass = VideoListViewFragment.class;
                bundle.putSerializable("value", "location");
                break;
            default:
                fragmentClass = VideoListViewFragment.class;
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
    protected void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
    }
}