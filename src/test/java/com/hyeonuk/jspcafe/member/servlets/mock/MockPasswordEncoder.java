package com.hyeonuk.jspcafe.member.servlets.mock;

import com.hyeonuk.jspcafe.global.utils.PasswordEncoder;

public class MockPasswordEncoder implements PasswordEncoder {
    public String encode(String password) {
        if(password == null) return null;
        return new StringBuilder().append(password).reverse().toString();
    }

    @Override
    public boolean match(String str, String encoded) {
        return new StringBuilder().append(str).reverse().toString().equals(encoded);
    }
}
