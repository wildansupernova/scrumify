package crystal.scrumify.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import crystal.scrumify.responses.ApiResponse;
import crystal.scrumify.responses.GroupListResponse;
import crystal.scrumify.responses.LoginResponse;
import crystal.scrumify.responses.TaskResponse;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

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

        @GET("user/{userId}/groups")
        Call<ApiResponse<List<GroupListResponse>>> getUserGroups(
                @Path("userId") int userId
        );

        @FormUrlEncoded
        @POST("group")
        Call<ApiResponse<String>> createGroup(
                @Field("group_name") String groupName,
                @Field("description") String groupDesc,
                @Field("user_id") int user_id
        );

        @GET("group/{group_id}/tasks")
        Call<ApiResponse<List<TaskResponse>>> getTasks(
                @Path("group_id") int groupId,
                @Query("status_kanban") String statusKanban
        );
    }
}
