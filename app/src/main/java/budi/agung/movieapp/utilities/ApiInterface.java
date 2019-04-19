package budi.agung.movieapp.utilities;

import budi.agung.movieapp.models.Cast;
import budi.agung.movieapp.models.CreditGetResponse;
import budi.agung.movieapp.models.MovieDetailGetResponse;
import budi.agung.movieapp.models.MovieGetResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("movie/popular")
    Call<MovieGetResponse> getPopularMovies(
            @Query("page") int page,
            @Query("api_key") String api_key
    );

    @GET("movie/now_playing")
    Call<MovieGetResponse> getNowPlayingMovies(
            @Query("page") int page,
            @Query("api_key") String api_key
    );

    @GET("trending/movie/week")
    Call<MovieGetResponse> getTrending(
            @Query("api_key") String api_key
    );

    @GET("search/movie")
    Call<MovieGetResponse> getSearchMovie(
            @Query("query") String query,
            @Query("page") int page,
            @Query("api_key") String api_key
    );

    @GET("movie/{movie_id}")
    Call<MovieDetailGetResponse> getMovieDetail(
            @Path("movie_id") int movie_id,
            @Query("api_key") String api_key
    );

    @GET("movie/{movie_id}/credits")
    Call<CreditGetResponse> getMovieCredits(
            @Path("movie_id") int movie_id,
            @Query("api_key") String api_key
    );
}
