package codesquad.javacafe.post.repository;

import codesquad.javacafe.common.db.DBConnection;
import codesquad.javacafe.post.dto.request.PostCreateRequestDto;
import codesquad.javacafe.post.entity.Post;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class PostRepositoryTest {

    private static PostRepository postRepository;
    private static Connection connection;

    @BeforeAll
    static void setUp() throws SQLException {
        postRepository = PostRepository.getInstance();
        DBConnection.setConnectionInfo("jdbc:h2:mem:testdb;MODE=MySQL;DATABASE_TO_LOWER=TRUE", "sa", "");
        connection = DBConnection.getConnection();
        createTable();
    }

    @AfterEach
    void afterEach() throws SQLException {
        clearTable();
    }

    private static void createTable() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:h2:mem:testdb", "sa", "");

        // Create the post table
        String sql = "CREATE TABLE post (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                "post_writer VARCHAR(255), " +
                "post_title VARCHAR(255), " +
                "post_contents TEXT, " +
                "post_create TIMESTAMP" +
                ")";

        try (var statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    private void clearTable() throws SQLException {
        var sql = "DELETE FROM post";
        try (var statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    private PostCreateRequestDto createPostDto(String writer, String title, String contents) {
        Map<String, String[]> body = new HashMap<>();
        body.put("writer", new String[]{writer});
        body.put("title", new String[]{title});
        body.put("contents", new String[]{contents});
        return new PostCreateRequestDto(body);
    }

    @Test
    public void testSave() throws SQLException {
        PostCreateRequestDto postDto = createPostDto("writer", "title", "contents");

        postRepository.save(postDto);

        List<Post> posts = postRepository.findAll();
        assertNotNull(posts);
        assertEquals(1, posts.size());
        assertEquals("writer", posts.get(0).getWriter());
        assertEquals("title", posts.get(0).getTitle());
        assertEquals("contents", posts.get(0).getContents());
    }

    @Test
    public void testFindAll() throws SQLException {
        PostCreateRequestDto postDto1 = createPostDto("writer1", "title1", "contents1");
        PostCreateRequestDto postDto2 = createPostDto("writer2", "title2", "contents2");

        postRepository.save(postDto1);
        postRepository.save(postDto2);

        List<Post> posts = postRepository.findAll();
        assertNotNull(posts);
        assertEquals(2, posts.size());
        assertEquals("writer1", posts.get(0).getWriter());
        assertEquals("title1", posts.get(0).getTitle());
        assertEquals("contents1", posts.get(0).getContents());
        assertEquals("writer2", posts.get(1).getWriter());
        assertEquals("title2", posts.get(1).getTitle());
        assertEquals("contents2", posts.get(1).getContents());
    }

    @Test
    public void testFindById() throws SQLException {
        PostCreateRequestDto postDto = createPostDto("writer", "title", "contents");

        postRepository.save(postDto);

        List<Post> posts = postRepository.findAll();
        assertNotNull(posts);
        assertEquals(1, posts.size());

        Post post = postRepository.findById(posts.get(0).getId());
        assertNotNull(post);
        assertEquals("writer", post.getWriter());
        assertEquals("title", post.getTitle());
        assertEquals("contents", post.getContents());
    }
}