package com.bluetriangle.bluetriangledemo.utils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class ExceptionInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        throw new IOException("ExceptionInterceptor: Request failed due to an exception.");
    }
}