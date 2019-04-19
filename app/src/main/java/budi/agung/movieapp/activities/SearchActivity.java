package budi.agung.movieapp.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import budi.agung.movieapp.R;
import budi.agung.movieapp.adapters.MovieSearchAdapter;
import budi.agung.movieapp.models.LoadingModel;
import budi.agung.movieapp.models.Movie;
import budi.agung.movieapp.models.MovieModel;
import budi.agung.movieapp.viewmodels.MovieSearchViewModel;

public class SearchActivity extends AppCompatActivity {

    private Context context;
    private List<Movie> items;
    private MovieSearchAdapter adapter;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private SearchView searchView;
    private SearchView.OnQueryTextListener queryTextListener;
    private MovieSearchViewModel movieSearchViewModel;
    private boolean initialize;
    private int loadingPosition, nextPage, currentPage;
    private String currentQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initVariable();
        initComponent();
        initData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchItem.expandActionView();
        searchView.setOnQueryTextListener(queryTextListener);
        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                return false;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                finish();
                overridePendingTransition(R.anim.stay, R.anim.out_left_to_right);
                return false;
            }
        });
        ImageView closeButton = searchView.findViewById(R.id.search_close_btn);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et = findViewById(R.id.search_src_text);
                et.setText("");
                searchView.setQuery("", true);

                adapter.setShowHeader(true);
                movieSearchViewModel.searchMovie("", 1);
            }
        });
        return true;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.stay, R.anim.out_left_to_right);
    }

    private void initVariable() {
        initialize = false;
        loadingPosition = 0;
        nextPage = 1;
        currentPage = 1;
        currentQuery = "";
        context = this;
        items = new ArrayList<>();
        adapter = new MovieSearchAdapter(context, items, new MovieSearchAdapter.EventListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("movieModel", (MovieModel) items.get(position));
                startActivity(intent);
            }

            @Override
            public void onReachBottom(int position) {
                int page = ((LoadingModel) items.get(position)).getPage();
                loadingPosition = position;
                nextPage = page + 1;
                movieSearchViewModel.searchMovie(currentQuery, page);
            }
        });

        queryTextListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                currentQuery = s;
                nextPage = 2;
                adapter.setShowHeader(s.isEmpty());
                movieSearchViewModel.searchMovie(currentQuery, 1);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        };
        movieSearchViewModel = ViewModelProviders.of(this).get(MovieSearchViewModel.class);
        movieSearchViewModel.getItems().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                if (currentPage == 1) {
                    items.clear();
                }
                items.addAll(movies);
                if (items.size() > loadingPosition && items.get(loadingPosition) instanceof LoadingModel) {
                    items.remove(loadingPosition);
                }
                if (movies.size() >= 20 && !adapter.isShowHeader()) {
                    items.add(new LoadingModel(nextPage));
                }
                adapter.notifyDataSetChanged();
            }
        });
        movieSearchViewModel.getPage().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                currentPage = integer;
            }
        });
    }

    private void initComponent() {
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerView);
        initToolbar();
        initRecyclerView();
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
    }

    public void initData() {
        if (!initialize) {
            currentQuery = "";
            nextPage = 2;
            initialize = true;
            movieSearchViewModel.searchMovie(currentQuery, 1);
        }
    }
}
