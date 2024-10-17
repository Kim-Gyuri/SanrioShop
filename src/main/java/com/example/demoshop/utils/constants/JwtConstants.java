package com.example.demoshop.utils.constants;

public class JwtConstants {

    public static final String JWT_AUTH = "Authorization";
    public static final String JWT_REFRESH = "refresh";


    public static final String HttpMethod_POST = "POST";


    public static final long ACCESS_TOKEN_EXPIRATION = 600000L; // 10 minutes in milliseconds
    public static final long REFRESH_TOKEN_EXPIRATION = 86400000L; // 24 hours in milliseconds

    public static final int COOKIE_TIME = 24 * 60 * 60;

    public static final int IMMEDIATE_EXPIRATION = 0;
}
