package com.hyeonuk.jspcafe.global.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("BcryptPasswordEncoder 클래스")
class BcryptPasswordEncoderTest {
    private final PasswordEncoder passwordEncoder = new BcryptPasswordEncoder();

    @Nested
    @DisplayName("encode 메서드")
    class EncodeTest{
        @DisplayName("입력이 들어오면 Bcrypt알고리즘으로 해싱을 한다.")
        @Test
        void encodeTest(){
            //given
            String password = "password";

            //when
            String encode = passwordEncoder.encode(password);

            //then
            assertNotEquals(password,encode);
            assertTrue(encode.indexOf("$") != encode.lastIndexOf("$"));
        }

        @DisplayName("null값이 들어오면 null을 리턴한다.")
        @Test
        void nullTest(){
            //given
            String password = null;
            //when
            String encode = passwordEncoder.encode(password);

            //then
            assertNull(encode);
        }
    }

    @Nested
    @DisplayName("match 메서드")
    class MatchTest{
        @DisplayName("origin과 해싱된 origin hash값을 넣으면 true가 반환된다.")
        @Test
        void matchTest(){
            //given
            String password = "password";
            String encoded = passwordEncoder.encode(password);
            //when
            boolean match = passwordEncoder.match(password, encoded);

            //then
            assertTrue(match);
        }

        @DisplayName("해싱할때 사용된 string이 아닌 다른값이면 false를 반환한다.")
        @Test
        void matchOtherTest(){
            //given
            String password = "password";
            String encoded = passwordEncoder.encode(password);

            //when
            boolean match = passwordEncoder.match("otherPassword", encoded);

            //then
            assertFalse(match);
        }

        @DisplayName("origin값이 null이라면 false를 반환한다.")
        @Test
        void nullOriginTest(){
            //given
            String password = "password";
            String encoded = passwordEncoder.encode(password);

            //when
            boolean match = passwordEncoder.match(null, encoded);

            //then
            assertFalse(match);
        }

        @DisplayName("encrypted값이 null이라면 false를 반환한다.")
        @Test
        void nullEncryptedTest(){
            //given
            String password = "password";
            String encoded = passwordEncoder.encode(password);

            //when
            boolean match = passwordEncoder.match(password, null);

            //then
            assertFalse(match);
        }
    }
}