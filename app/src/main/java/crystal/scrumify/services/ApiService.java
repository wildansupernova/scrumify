package crystal.scrumify.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiService {

    // TODO: add base URL
    public static final String BASE_URL = "";

    public static Api getApi() {
        Gson gson = new GsonBuilder().setLenient().create(); //accept malformed json
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit.create(Api.class);
    }

    public interface Api {

    }
}
