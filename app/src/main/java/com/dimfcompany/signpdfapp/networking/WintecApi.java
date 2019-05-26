package com.dimfcompany.signpdfapp.networking;

import com.dimfcompany.signpdfapp.base.Constants;
import com.dimfcompany.signpdfapp.models.Model_Document;
import com.dimfcompany.signpdfapp.models.Model_User;

import java.util.List;

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
    Call<Model_User> login(@Field("email") String email, @Field("password") String password, @Field("fb_token") String fb_token);

    @Multipart
    @POST(Constants.URL_INSERT_DOCUMENT)
    Call<Model_Document> insertDocument
            (
                    @Part MultipartBody.Part document,
                    @Part MultipartBody.Part pdf_file,
                    @Part MultipartBody.Part check_file,
                    @Part MultipartBody.Part signature_file
            );

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

}
