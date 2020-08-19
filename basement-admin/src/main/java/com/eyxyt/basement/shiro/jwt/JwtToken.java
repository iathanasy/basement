package com.eyxyt.basement.shiro.jwt;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author cd.wang
 * @create 2020-07-24 9:14
 * @since 1.0.0
 */
public class JwtToken  implements AuthenticationToken {

    /**
     * Token
     */
    private String token;

    public JwtToken(String token) {
        this.token = token;
    }


    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
