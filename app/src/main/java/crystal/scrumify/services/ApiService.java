package crystal.scrumify.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import crystal.scrumify.models.ApiResponse;
import crystal.scrumify.models.LoginResponse;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class ApiService {

    public static final String BASE_URL = "http://192.168.0.139:8000/api/";

    public static Api getApi() {
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit.create(Api.class);
    }

    public interface Api {
        @FormUrlEncoded
        @POST("login")
        Call<LoginResponse> login(
                @Field("tokenGoogle") String tokenGoogle
        );

    }
}
