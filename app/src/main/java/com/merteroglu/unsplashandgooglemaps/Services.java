package com.merteroglu.unsplashandgooglemaps;

import com.merteroglu.unsplashandgooglemaps.Models.Images;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Mert on 4.12.2017.
 */

public interface Services {

    String clientID = "108634c558e0100e3d958f253cbb532a3d12089297b1849742f6afe084487074";

    @GET("photos/random/?client_id=" + clientID)
    Call<List<Images>> getImagesList(@Query("count") int count);

}
