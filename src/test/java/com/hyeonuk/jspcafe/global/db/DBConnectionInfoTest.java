package com.hyeonuk.jspcafe.global.db;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DBConnectionInfo 클래스")
class DBConnectionInfoTest {
    private DBConnectionInfo dbConnectionInfo;

    @Nested
    @DisplayName("기본 생성자는")
    class DefaultConstructorTest{
        @Test
        @DisplayName("username, password, driverClassName, url을 입력받으면 값을 저장할 수 있다.")
        void defaultConstructorTest(){
            //given
            String url = "jdbc:mysql://localhost:3306/test";
            String username = "username";
            String password = "password";
            String driverClassName = "com.mysql.cj.jdbc.Driver";

            //when
            dbConnectionInfo = new DBConnectionInfo(url,username,password,driverClassName);

            //then
            assertAll("default constructor",
                    ()->assertEquals(url,dbConnectionInfo.getUrl()),
                    ()->assertEquals(username,dbConnectionInfo.getUsername()),
                    ()->assertEquals(password,dbConnectionInfo.getPassword()),
                    ()->assertEquals(driverClassName,dbConnectionInfo.getDriverClassName()));
        }

        @Test
        @DisplayName("url null이면 오류를 던진다.")
        void urlNullTest(){
            //given
            String url = null;
            String username = "username";
            String password = "password";
            String driverClassName = "com.mysql.cj.jdbc.Driver";

            //when
            assertThrows(IllegalArgumentException.class,()->{
                dbConnectionInfo = new DBConnectionInfo(url,username,password,driverClassName);
            });
        }

        @Test
        @DisplayName("username이 null이면 오류를 던진다.")
        void usernameNullTest(){
            //given
            String url = "jdbc:mysql://localhost:3306/test";
            String username = null;
            String password = "password";
            String driverClassName = "com.mysql.cj.jdbc.Driver";

            //when
            assertThrows(IllegalArgumentException.class,()->{
                dbConnectionInfo = new DBConnectionInfo(url,username,password,driverClassName);
            });
        }

        @Test
        @DisplayName("password가 null이면 오류를 던진다.")
        void passwordNullTest(){
            //given
            String url = "jdbc:mysql://localhost:3306/test";
            String username = "username";
            String password = null;
            String driverClassName = "com.mysql.cj.jdbc.Driver";

            //when
            assertThrows(IllegalArgumentException.class,()->{
                dbConnectionInfo = new DBConnectionInfo(url,username,password,driverClassName);
            });
        }

        @Test
        @DisplayName("driverClassName이 null이면 오류를 던진다.")
        void driverClassNameNullTest(){
            //given
            String url = "jdbc:mysql://localhost:3306/test";
            String username = "username";
            String password = "password";
            String driverClassName = null;

            //when
            assertThrows(IllegalArgumentException.class,()->{
                dbConnectionInfo = new DBConnectionInfo(url,username,password,driverClassName);
            });
        }
    }

    @Nested
    @DisplayName("yaml파일의 경로를 받는 생성자는")
    class YamlConstructorTest{
        @DisplayName("yaml파일에 모든 값이 존재하면 생성되어야한다.")
        @Test
        void yamlConstructorTest(){
            //given
            String yamlPath = "yaml/db/all.yml";

            //when
            dbConnectionInfo = new DBConnectionInfo(yamlPath);

            //then
            assertAll("default constructor",
                    ()->assertEquals("url",dbConnectionInfo.getUrl()),
                    ()->assertEquals("username",dbConnectionInfo.getUsername()),
                    ()->assertEquals("password",dbConnectionInfo.getPassword()),
                    ()->assertEquals("driverClassName",dbConnectionInfo.getDriverClassName()));
        }

        @Test
        @DisplayName("url null이면 오류를 던진다.")
        void urlNullTest(){
            //given
            String yamlPath = "yaml/db/urlNull.yml";

            //when
            assertThrows(IllegalArgumentException.class,()->{
                dbConnectionInfo = new DBConnectionInfo(yamlPath);
            });
        }

        @Test
        @DisplayName("username이 null이면 오류를 던진다.")
        void usernameNullTest(){
            //given
            String yamlPath = "yaml/db/usernameNull.yml";

            //when
            assertThrows(IllegalArgumentException.class,()->{
                dbConnectionInfo = new DBConnectionInfo(yamlPath);
            });
        }

        @Test
        @DisplayName("password가 null이면 오류를 던진다.")
        void passwordNullTest(){
            //given
            String yamlPath = "yaml/db/passwordNull.yml";

            //when
            assertThrows(IllegalArgumentException.class,()->{
                dbConnectionInfo = new DBConnectionInfo(yamlPath);
            });
        }

        @Test
        @DisplayName("driverClassName이 null이면 오류를 던진다.")
        void driverClassNameNullTest(){
            //given
            String yamlPath = "yaml/db/driverClassNameNull.yml";

            //when
            assertThrows(IllegalArgumentException.class,()->{
                dbConnectionInfo = new DBConnectionInfo(yamlPath);
            });
        }
    }
}