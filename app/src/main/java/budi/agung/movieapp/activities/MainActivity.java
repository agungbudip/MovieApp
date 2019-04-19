package budi.agung.movieapp.activities;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import budi.agung.movieapp.R;
import budi.agung.movieapp.adapters.TabAdapter;
import budi.agung.movieapp.fragments.NowPlayingFragment;
import budi.agung.movieapp.fragments.PopularFragment;

public class MainActivity extends AppCompatActivity {

    private Context context;
    private TabAdapter tabAdapter;

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private NowPlayingFragment fNowPlaying;
    private PopularFragment fPopular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initVariable();
        initComponent();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                startActivity(new Intent(context, SearchActivity.class));
                overridePendingTransition(R.anim.in_right_to_left, R.anim.stay);
                break;
        }
        return true;
    }

    private void initVariable() {
        context = this;
        tabAdapter = new TabAdapter(getSupportFragmentManager());

        fNowPlaying = new NowPlayingFragment();
        fPopular = new PopularFragment();

        tabAdapter.addFragment(fNowPlaying, "Now Playing");
        tabAdapter.addFragment(fPopular, "Popular");
    }

    private void initComponent() {
        toolbar = findViewById(R.id.toolbar);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        initToolbar();
        initTabLayout();
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
    }

    private void initTabLayout() {
        viewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                switch (i) {
                    case 0:
                        fNowPlaying.initData();
                        break;
                    case 1:
                        fPopular.initData();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }
}
