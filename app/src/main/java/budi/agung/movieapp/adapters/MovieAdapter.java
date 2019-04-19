package budi.agung.movieapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import budi.agung.movieapp.R;
import budi.agung.movieapp.holders.LoadingHolder;
import budi.agung.movieapp.models.LoadingModel;
import budi.agung.movieapp.models.Movie;
import budi.agung.movieapp.models.MovieModel;

public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<Movie> items;
    private EventListener eventListener;
    private LayoutInflater inflater;

    private static final int MOVIE = 1;
    private static final int LOADING = 2;

    public MovieAdapter(Context context, List<Movie> items, EventListener eventListener) {
        this.context = context;
        this.items = items;
        this.eventListener = eventListener;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == MOVIE) {
            View view = inflater.inflate(R.layout.row_movie, viewGroup, false);
            return new MovieHolder(view);
        } else if (viewType == LOADING) {
            View view = inflater.inflate(R.layout.row_loading, viewGroup, false);
            return new LoadingHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof MovieHolder) {
            MovieHolder mHolder = (MovieHolder) viewHolder;
            MovieModel item = (MovieModel) items.get(position);

            String posterUrl = context.getString(R.string.base_poster_url) + item.getPosterPath();
            int ratingPercent = (int) (item.getVoteAverage() * 10);
            SimpleDateFormat sdfOri = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            SimpleDateFormat sdfFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.US);
            String releaseDateFormat = "-";
            try {
                Date releaseDateOri = sdfOri.parse(item.getReleaseDate());
                releaseDateFormat = sdfFormat.format(releaseDateOri);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (item.getPosterPath() != null && item.getPosterPath().isEmpty()) {
                Picasso.with(context)
                        .load(R.mipmap.image_paceholder)
                        .into(mHolder.imgPoster);
            } else {
                Picasso.with(context)
                        .load(posterUrl)
                        .placeholder(R.mipmap.image_paceholder)
                        .into(mHolder.imgPoster);
            }
            if (ratingPercent >= 70) {
                mHolder.prgRating.setProgressDrawable(context.getResources().getDrawable(R.drawable.bg_progress_green));
            } else if (ratingPercent >= 40) {
                mHolder.prgRating.setProgressDrawable(context.getResources().getDrawable(R.drawable.bg_progress_yellow));
            } else {
                mHolder.prgRating.setProgressDrawable(context.getResources().getDrawable(R.drawable.bg_progress_red));
            }
            mHolder.prgRating.setProgress(ratingPercent);
            mHolder.tvRating.setText(ratingPercent + "%");
            mHolder.tvTitle.setText(item.getTitle());
            mHolder.tvReleaseDate.setText(releaseDateFormat);
            mHolder.tvOverview.setText(item.getOverview());

        } else if (viewHolder instanceof LoadingHolder) {
            eventListener.onReachBottom(position);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position) instanceof MovieModel) {
            return MOVIE;
        } else if (items.get(position) instanceof LoadingModel) {
            return LOADING;
        }
        return -1;
    }

    public class MovieHolder extends RecyclerView.ViewHolder {
        ImageView imgPoster;
        ProgressBar prgRating;
        TextView tvRating, tvTitle, tvReleaseDate, tvOverview;

        public MovieHolder(@NonNull View itemView) {
            super(itemView);
            imgPoster = itemView.findViewById(R.id.imgPoster);
            prgRating = itemView.findViewById(R.id.prgRating);
            tvRating = itemView.findViewById(R.id.tvRating);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvReleaseDate = itemView.findViewById(R.id.tvReleaseDate);
            tvOverview = itemView.findViewById(R.id.tvOverview);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    eventListener.onItemClick(position);
                }
            });
        }
    }

    public interface EventListener {
        void onItemClick(int position);

        void onReachBottom(int position);
    }
}
