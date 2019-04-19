package budi.agung.movieapp.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import budi.agung.movieapp.R;
import budi.agung.movieapp.adapters.CastAdapter;
import budi.agung.movieapp.models.Cast;
import budi.agung.movieapp.models.Genre;
import budi.agung.movieapp.models.MovieDetailGetResponse;
import budi.agung.movieapp.models.MovieModel;
import budi.agung.movieapp.viewmodels.MovieDetailViewModel;

public class DetailActivity extends AppCompatActivity {

    private Context context;
    private MovieModel movieModel;
    private List<Genre> genres;
    private List<Cast> casts;
    private CastAdapter adapter;
    private int runTime;
    private MovieDetailViewModel movieDetailViewModel;

    private ProgressBar prgRating;
    private TextView tvTitle, tvRating, tvVoteAverage, tvVoteCount, tvReleaseDate, tvRunningTime, tvGanre, tvOverview;
    private RecyclerView rclActor;
    private ImageView imgBackdrop, imgPoster;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initVariable();
        initComponent();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    private void initVariable() {
        context = this;
        genres = new ArrayList<>();
        casts = new ArrayList<>();
        runTime = 0;
        adapter = new CastAdapter(context, casts);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey("movieModel")) {
            movieModel = (MovieModel) bundle.getSerializable("movieModel");
        } else {
            finish();
        }

        movieDetailViewModel = ViewModelProviders.of(this).get(MovieDetailViewModel.class);
        movieDetailViewModel.getItems().observe(this, new Observer<MovieDetailGetResponse>() {
            @Override
            public void onChanged(@Nullable MovieDetailGetResponse movieDetailGetResponse) {
                runTime = movieDetailGetResponse.getRuntime();
                genres = movieDetailGetResponse.getGenres();
                bindModel();
            }
        });
        movieDetailViewModel.getCast().observe(this, new Observer<List<Cast>>() {
            @Override
            public void onChanged(@Nullable List<Cast> casts) {
                DetailActivity.this.casts.clear();
                for (int i = 0; i < 5; i++) {
                    if (casts.size() > i) {
                        DetailActivity.this.casts.add(casts.get(i));
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void initComponent() {
        prgRating = findViewById(R.id.prgRating);
        tvTitle = findViewById(R.id.tvTitle);
        tvRating = findViewById(R.id.tvRating);
        tvVoteAverage = findViewById(R.id.tvVoteAverage);
        tvVoteCount = findViewById(R.id.tvVoteCount);
        tvReleaseDate = findViewById(R.id.tvReleaseDate);
        tvRunningTime = findViewById(R.id.tvRunningTime);
        tvGanre = findViewById(R.id.tvGanre);
        tvOverview = findViewById(R.id.tvOverview);
        rclActor = findViewById(R.id.rclActor);
        imgBackdrop = findViewById(R.id.imgBackdrop);
        imgPoster = findViewById(R.id.imgPoster);
        toolbar = findViewById(R.id.toolbar);
        initToolbar();
        initRecyclerView();
        bindModel();
        movieDetailViewModel.getDetail(movieModel.getId());
        movieDetailViewModel.getCredit(movieModel.getId());
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(" ");
    }

    private void initRecyclerView() {
        rclActor.setLayoutManager(new LinearLayoutManager(context));
        rclActor.setAdapter(adapter);
    }

    private void bindModel() {
        String backdropUrl = context.getString(R.string.base_backdrop_url) + movieModel.getBackdropPath();
        String posterUrl = context.getString(R.string.base_poster_url) + movieModel.getPosterPath();
        int ratingPercent = (int) (movieModel.getVoteAverage() * 10);
        SimpleDateFormat sdfOri = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        SimpleDateFormat sdfFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.US);
        String releaseDateFormat = "-";
        String runningTimeFormat = "-";
        String genreFormat = "-";

        try {
            Date releaseDateOri = sdfOri.parse(movieModel.getReleaseDate());
            releaseDateFormat = sdfFormat.format(releaseDateOri);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (runTime > 0) {
            runningTimeFormat = runTime + " Minutes";
        }
        if (genres.size() > 0) {
            genreFormat = "";
            for (int i = 0; i < genres.size(); i++) {
                genreFormat += genres.get(i).getName();
                if (i != (genres.size() - 1)) {
                    genreFormat += " | ";
                }
            }
        }

        if (movieModel.getPosterPath() != null && movieModel.getPosterPath().isEmpty()) {
            Picasso.with(context)
                    .load(R.mipmap.image_paceholder)
                    .into(imgPoster);
        } else {
            Picasso.with(context)
                    .load(posterUrl)
                    .placeholder(R.mipmap.image_paceholder)
                    .into(imgPoster);
        }

        if (movieModel.getBackdropPath() != null && movieModel.getBackdropPath().isEmpty()) {
            Picasso.with(context)
                    .load(R.mipmap.backdrop_placeholder)
                    .into(imgBackdrop);
        } else {
            Picasso.with(context)
                    .load(backdropUrl)
                    .placeholder(R.mipmap.backdrop_placeholder)
                    .into(imgBackdrop);
        }
        if (ratingPercent >= 70) {
            prgRating.setProgressDrawable(context.getResources().getDrawable(R.drawable.bg_progress_green));
        } else if (ratingPercent >= 30) {
            prgRating.setProgressDrawable(context.getResources().getDrawable(R.drawable.bg_progress_yellow));
        } else {
            prgRating.setProgressDrawable(context.getResources().getDrawable(R.drawable.bg_progress_red));
        }

        prgRating.setProgress(ratingPercent);
        tvTitle.setText(movieModel.getTitle());
        tvRating.setText(ratingPercent + "%");
        tvVoteAverage.setText(movieModel.getVoteAverage() + "");
        tvVoteCount.setText(movieModel.getVoteCount() + " Votes");
        tvReleaseDate.setText(releaseDateFormat);
        tvRunningTime.setText(runningTimeFormat);
        tvGanre.setText(genreFormat);
        tvOverview.setText(movieModel.getOverview());
    }
}
