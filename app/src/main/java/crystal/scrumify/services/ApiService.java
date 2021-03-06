package crystal.scrumify.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import crystal.scrumify.models.Comment;
import crystal.scrumify.models.Event;
import crystal.scrumify.models.Group;
import crystal.scrumify.models.User;
import crystal.scrumify.responses.ApiResponse;
import crystal.scrumify.responses.ChartValue;
import crystal.scrumify.responses.GroupListResponse;
import crystal.scrumify.responses.LoginResponse;
import crystal.scrumify.responses.TaskResponse;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class ApiService {

    public static final String BASE_URL = "http://192.168.43.22:8000/api/";

    public static Api getApi() {
        Gson gson = new GsonBuilder().setLenient().create();
        HttpLoggingInterceptor loggingApi = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder builderApi = new OkHttpClient.Builder().addInterceptor(loggingApi);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(builderApi.build())
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

        @GET("user/email/{userEmail}")
        Call<ApiResponse<User>> getUser(
                @Path("userEmail") String userEmail
        );

        @GET("user/{userId}/groups")
        Call<ApiResponse<List<GroupListResponse>>> getUserGroups(
                @Path("userId") int userId
        );

        @FormUrlEncoded
        @POST("group")
        Call<ApiResponse<Group>> createGroup(
                @Field("group_name") String groupName,
                @Field("description") String groupDesc,
                @Field("user_id") int userId
        );

        @FormUrlEncoded
        @POST("group/member")
        Call<ApiResponse<String>> createGroupMember(
                @Field("group_id") int groupId,
                @Field("email") String emailReceiver
        );

        @GET("group/{group_id}/tasks")
        Call<ApiResponse<List<TaskResponse>>> getTasks(
                @Path("group_id") int groupId,
                @Query("kanban_status") String kanbanStatus
        );

        @GET("group/{group_id}/history")
        Call<ApiResponse<List<ChartValue>>> getChartValue(
                @Path("group_id") int groupId
        );

        @FormUrlEncoded
        @POST("tasks")
        Call<ApiResponse<String>> createTask(
                @Field("group_id") int groupId,
                @Field("task_name") String taskName,
                @Field("description") String taskDesc,
                @Field("kanban_status") String kanbanStatus,
                @Field("work_hour") int workHour
        );

        @PUT("task/move/{task_id}")
        Call<ApiResponse<String>> moveTask(
                @Path("task_id") int taskId
        );

        @GET("task/{task_id}/comment")
        Call<ApiResponse<List<Comment>>> getComments(
                @Path("task_id") int taskId
        );

        @FormUrlEncoded
        @POST("user/{user_id}/task/{task_id}/comment")
        Call<ApiResponse<String>> createComment(
                @Path("user_id") int userId,
                @Path("task_id") int taskId,
                @Field("comment") String comment
        );
    }
}
