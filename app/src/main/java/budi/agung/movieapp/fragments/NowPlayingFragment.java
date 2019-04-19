package budi.agung.movieapp.fragments;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import budi.agung.movieapp.R;
import budi.agung.movieapp.activities.DetailActivity;
import budi.agung.movieapp.adapters.MovieAdapter;
import budi.agung.movieapp.models.LoadingModel;
import budi.agung.movieapp.models.Movie;
import budi.agung.movieapp.models.MovieModel;
import budi.agung.movieapp.viewmodels.MovieViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class NowPlayingFragment extends Fragment {

    private Context context;
    private List<Movie> items;
    private MovieAdapter adapter;

    private View parent;
    private RecyclerView recyclerView;

    private int loadingPosition, nextPage;
    private boolean visible, initialize;
    private MovieViewModel movieViewModel;

    public NowPlayingFragment() {
        // Required empty public constructor
        visible = false;
        initialize = false;
        loadingPosition = 0;
        nextPage = 1;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        parent = inflater.inflate(R.layout.fragment_now_playing, container, false);
        initVariable();
        initComponent();
        if (visible) {
            initData();
        }
        return parent;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        visible = isVisibleToUser;
    }

    private void initVariable() {
        context = getActivity();
        items = new ArrayList<>();

        adapter = new MovieAdapter(context, items, new MovieAdapter.EventListener() {
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
                movieViewModel.loadData(page, MovieModel.CATEGORY_NOW_PLAYING);
            }
        });

        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        movieViewModel.getItems(MovieModel.CATEGORY_NOW_PLAYING).observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                if (movies != null) {
                    if (items.size() > loadingPosition && items.get(loadingPosition) instanceof LoadingModel) {
                        items.remove(loadingPosition);
                    }
                    items.addAll(movies);
                    if (movies.size() >= 20) {
                        items.add(new LoadingModel(nextPage));
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void initComponent() {
        recyclerView = parent.findViewById(R.id.recyclerView);
        initRecyclerView();
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
    }

    public void initData() {
        if (!initialize) {
            initialize = true;
            items.add(new LoadingModel(nextPage));
            adapter.notifyDataSetChanged();
        }
    }
}
