package com.mz.common.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * jwt 认证工具类
 */
@Slf4j
public class JwtUtil {
    /**
     * 过期时间 24小时
     */
    private static final long EXPIRE_TIME = 24 * 60 * 60 * 1000;
//	// 密钥是公众号密钥
//	private static final String TOKEN_SECRET = Constants.APP_SECRET_WX;

    /**
     * 生成XX分钟过期的token
     *
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String sign(String userId, String userName, String appSecret) {
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        Algorithm algorithm = null;
        algorithm = Algorithm.HMAC256(appSecret);
        Map<String, Object> header = new HashMap<String, Object>(2);
        header.put("alg", "h256");
        header.put("type", "jwt");
        return JWT.create().withHeader(header)
                .withClaim("userId", userId)
                .withClaim("userName", userName)
                .withExpiresAt(date).sign(algorithm);
    }

    /**
     * 验证token
     *
     * @param token
     * @return
     */
    public static boolean verify(String token, String appSecretWx) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(appSecretWx);
            JWT.require(algorithm).build().verify(token);
            return true;
        } catch (Exception e) {
            log.error("验证token失败==={}", token);
            return false;
        }
    }

    public static Map<String, String> getUserInfo(String token) {
        Map<String, String> retMap = new HashMap<>();
        try {
            DecodedJWT decodedJWT = JWT.decode(token);
            retMap.put("userId", decodedJWT.getClaim("userId").asString());
            retMap.put("userName", decodedJWT.getClaim("userName").asString());
            return retMap;
        } catch (JWTDecodeException e) {
            log.error("根据token获取用户信息失败{}", e.getMessage());
            return null;
        }
    }
}
