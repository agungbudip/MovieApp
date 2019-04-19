package budi.agung.movieapp.repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import budi.agung.movieapp.models.Movie;
import budi.agung.movieapp.models.MovieGetResponse;
import budi.agung.movieapp.utilities.ApiClient;
import budi.agung.movieapp.utilities.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieNowPlayingRepository {
    private static MovieNowPlayingRepository instances;

    private MutableLiveData<List<Movie>> items;

    public static MovieNowPlayingRepository getInstances() {
        if (instances == null) {
            instances = new MovieNowPlayingRepository();
            instances.items = new MutableLiveData<>();
        }
        return instances;
    }

    public LiveData<List<Movie>> getItems() {
        return items;
    }

    public void loadData(final int page) {
        ApiInterface client = ApiClient.getClient().create(ApiInterface.class);
        client.getNowPlayingMovies(page, "c6a8ab2a50d10a5c5a194ecd0f637f8b").enqueue(new Callback<MovieGetResponse>() {
            @Override
            public void onResponse(Call<MovieGetResponse> call, Response<MovieGetResponse> response) {
                List<Movie> movies = new ArrayList<>();
                for (Movie mov : response.body().getResults()) {
                    if (mov != null) {
                        movies.add(mov);
                    }
                }
                items.postValue(movies);
            }

            @Override
            public void onFailure(Call<MovieGetResponse> call, Throwable t) {
                Log.d("TESTRESULT", t.getMessage() + "");
            }
        });
    }
}
