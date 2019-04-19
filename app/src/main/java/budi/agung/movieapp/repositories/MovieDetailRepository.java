package budi.agung.movieapp.repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import budi.agung.movieapp.models.Movie;
import budi.agung.movieapp.models.MovieDetailGetResponse;
import budi.agung.movieapp.models.MovieGetResponse;
import budi.agung.movieapp.utilities.ApiClient;
import budi.agung.movieapp.utilities.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailRepository {
    private static MovieDetailRepository instances;

    private MutableLiveData<MovieDetailGetResponse> items;

    public static MovieDetailRepository getInstances() {
        if (instances == null) {
            instances = new MovieDetailRepository();
            instances.items = new MutableLiveData<>();
        }
        return instances;
    }

    public LiveData<MovieDetailGetResponse> getItems() {
        return items;
    }

    public void getDetail(final int movie_id) {
        ApiInterface client = ApiClient.getClient().create(ApiInterface.class);
        client.getMovieDetail(movie_id, "c6a8ab2a50d10a5c5a194ecd0f637f8b").enqueue(new Callback<MovieDetailGetResponse>() {
            @Override
            public void onResponse(Call<MovieDetailGetResponse> call, Response<MovieDetailGetResponse> response) {
                items.setValue(response.body());
            }

            @Override
            public void onFailure(Call<MovieDetailGetResponse> call, Throwable t) {
                Log.d("TESTRESULT", t.getMessage() + "");
            }
        });
    }
}
