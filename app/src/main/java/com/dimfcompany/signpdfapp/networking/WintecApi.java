package com.dimfcompany.signpdfapp.networking;

import androidx.annotation.Nullable;

import com.dimfcompany.signpdfapp.base.Constants;
import com.dimfcompany.signpdfapp.models.Model_Document;
import com.dimfcompany.signpdfapp.models.Model_User;

import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface WintecApi
{
    @POST(Constants.URL_REGISTER)
    Call<String> registerNewUser
            (
                    @Query("first_name") String first_name,
                    @Query("last_name") String second_name,
                    @Query("email") String email,
                    @Query("password") String password,
                    @Query("fb_token") String fb_token
            );

    @POST(Constants.URL_RESETPASS)
    Call<String> resetPassword(@Query("email") String email);

    @FormUrlEncoded
    @POST(Constants.URL_LOGIN)
    Call<String> login
            (
                    @Field("email") String email,
                    @Field("password") String password,
                    @Field("fb_token") String fb_token,
                    @Field("app_version") String app_version
            );

    @Multipart
    @POST(Constants.URL_INSERT_DOCUMENT)
    Call<Model_Document> insertDocument
            (
                    @Part MultipartBody.Part document,
                    @Part MultipartBody.Part pdf_file,
                    @Part MultipartBody.Part check_file,
                    @Part MultipartBody.Part signature_file,
                    @Part MultipartBody.Part vaucher_file
            );

    @GET(Constants.URL_GET_APP_LAST_VERSION)
    Call<String> getAppLastVersion();

    @GET(Constants.URL_USER_ROLE_NAME)
    Call<String> getUserRoleName(@Query("id") int user_id);

    @GET(Constants.URL_USER_DOCS_COUNT)
    Call<Integer> getUserDocsCount(@Query("id") int user_id);

    @GET(Constants.URL_GET_ALL_DOCS)
    Call<List<Model_Document>> getAllDocuments(@Query("id") int user_id);

    @GET(Constants.URL_GET_ALL_USERS_ALL_DOCS)
    Call<List<Model_Document>> getAllUSersAllDocs();

    @GET(Constants.URL_GET_DOCUMETS_WITH_FULL_INFO)
    Call<Model_Document> getDocumentWithFullInfo(@Query("document_id") long document_id);

    @POST(Constants.URL_DELETE_DOCUMENT_ON_SERVER)
    Call<String> deleteDocumentOnServer(@Query("document_id") long document_id);

    @GET(Constants.URL_GET_ALL_USERS)
    Call<List<Model_User>> getUsers(@Query("search") String search, @Query("sort") String sort);

    @POST(Constants.URL_CHANGE_ROLE)
    Call<String> changeRole(@Query("user_id") int user_id, @Query("role_id") int role_id);

    @GET(Constants.URL_GET_USER_WITH_DOCS)
    Call<Model_User> getUserFull(@Query("user_id") int user_id);

    @POST(Constants.URL_TOGGLE_APPROVED)
    Call<String> toggleUserApproved(@Query("user_id") int user_id, @Query("approved_value") Integer approved_value);

    @POST(Constants.URL_INSERT_UPDATE_USER_NEW)
    Call<String> insertOrUpdateUserNew(@Query("user_id") Integer user_id,
                                       @Query("first_name") String first_name,
                                       @Query("last_name") String last_name,
                                       @Query("email") String email,
                                       @Query("password") String password,
                                       @Query("verified") int verified,
                                       @Query("admin_approved") int admin_approved,
                                       @Query("role_id") int role_id);

    @GET(Constants.URL_GET_DOCUMENTS_SEARCH)
    Observable<List<Model_Document>> getSearchDocuments(
            @Query("search") String search,
            @Query("date_min") String date_min,
            @Query("date_max") String date_max,
            @Query("sum_min") Integer sum_min,
            @Query("sum_max") Integer sum_max
    );
}
