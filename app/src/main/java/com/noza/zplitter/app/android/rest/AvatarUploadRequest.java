package com.noza.zplitter.app.android.rest;

import android.content.Context;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.noza.zplitter.app.android.R;
import com.noza.zplitter.app.android.constant.HttpStatus;
import com.noza.zplitter.app.android.exception.auth.RequestFailedError;
import com.noza.zplitter.app.android.rest.request.PostRequest;
import com.noza.zplitter.app.android.rest.response.DivisionResponse;
import com.noza.zplitter.app.android.rest.response.SuccessResponse;
import com.orhanobut.logger.Logger;

import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by omiwrench on 2016-03-07.
 */
public class AvatarUploadRequest extends PostRequest{

    private static final String FILE_PART_NAME = "avatar";

    private MultipartEntityBuilder mBuilder = MultipartEntityBuilder.create();
    private final File imageFile;
    protected Map<String, String> headers;

    public AvatarUploadRequest(Context context, String token, File imageFile, Response.Listener<DivisionResponse> listener, Response.ErrorListener errorListener){
        super(getUrl(context, token), listener, errorListener);

        this.imageFile = imageFile;

        buildMultipartEntity();
    }

    @Override
    public Map<String, String> getHeaders(){
        return new HashMap<>();
    }
    private static String getUrl(Context context, String token){
        String url = context.getResources().getString(R.string.url_upload_avatar);
        url += "?token=" + token;
        return url;
    }

    private void buildMultipartEntity(){
        mBuilder.addBinaryBody(FILE_PART_NAME, imageFile, ContentType.create("image/jpeg"), imageFile.getName());
        mBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        mBuilder.setLaxMode().setBoundary("xx").setCharset(Charset.forName("UTF-8"));
    }

    @Override
    public String getBodyContentType(){
        return mBuilder.build().getContentType().getValue();
    }

    @Override
    public byte[] getBody(){
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            mBuilder.build().writeTo(bos);
        } catch (IOException e) {
            VolleyLog.e("IOException writing to ByteArrayOutputStream bos, building the multipart request.");
        }

        return bos.toByteArray();
    }

    @Override
    protected Response<DivisionResponse> parseNetworkResponse(NetworkResponse response) {
        return Response.success((DivisionResponse) new SuccessResponse(), HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected VolleyError parseNetworkError(VolleyError error){
        int status = error.networkResponse.statusCode;
        if(status == HttpStatus.PAYLOAD_TOO_LARGE){
            return new RequestFailedError("PAYLOAD_TOO_LARGE");
        }
        if(error.networkResponse.statusCode == HttpStatus.UNAUTHORIZED ||
           error.networkResponse.statusCode == HttpStatus.BAD_REQUEST){
            return super.requestFailed(error);
        }
        return super.parseNetworkError(error);
    }
}