package com.conectatec.data.network;

import com.conectatec.data.model.LoginResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthService {
    @POST("api/auth/google")
    Call<LoginResponse> loginConGoogle(@Body Map<String, String> body);
}
