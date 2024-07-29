package com.codesquad.cafe.db;

import static com.codesquad.cafe.TestDataSource.createTable;
import static com.codesquad.cafe.TestDataSource.dataSource;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.codesquad.cafe.db.entity.Post;
import com.codesquad.cafe.db.entity.PostDetailsDto;
import com.codesquad.cafe.db.entity.User;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PostDaoTest {

    private static PostDao postDao;

    private static UserDao userDao;

    private static User user;

    private Post post;


    @BeforeAll
    static void beforeAll() {
        DataSource ds = dataSource();
        postDao = new PostDao(new JdbcTemplate(ds));
        userDao = new UserDao(new JdbcTemplate(ds));
        createTable(ds);
        user = userDao.save(User.of("javajigi", "test1234", "박재성", "javajihi@gmail.com"));
    }

    public void createPost() {
        post = postDao.save(Post.of(user.getId(), "test-title", "test-content", "test-filename.jpg"));
    }

    @AfterEach
    void tearDown() {
        postDao.deleteAll();
    }

    @DisplayName("게시글 저장")
    @Test
    void save() {
        //given
        Post post = Post.of(1L, "test-title", "test-content", "test-filename.jpg");

        //when
        postDao.save(post);

        //then
        Post savedPost = postDao.findById(post.getId()).get();
        assertEquals("test-title", savedPost.getTitle());
        assertEquals("test-content", savedPost.getContent());
        assertEquals("test-filename.jpg", savedPost.getFileName());
        assertEquals(0, savedPost.getView());
        assertNotNull(post.getCreatedAt());
        assertNotNull(post.getUpdatedAt());
        assertFalse(post.isDeleted());
    }

    @DisplayName("게시글 조회 - findById")
    @Test
    void findById() {
        //given
        createPost();

        //when
        Optional<Post> found = postDao.findById(post.getId());

        //then
        assertTrue(found.isPresent());
        Post foundPost = found.get();
        assertEquals(post.getId(), foundPost.getId());
        assertEquals(post.getTitle(), foundPost.getTitle());
        assertEquals(post.getContent(), foundPost.getContent());
        assertEquals(post.getFileName(), foundPost.getFileName());
        assertEquals(post.getAuthorId(), foundPost.getAuthorId());
        assertEquals(post.getView(), foundPost.getView());
        assertEquals(post.getCreatedAt(), foundPost.getCreatedAt());
        assertEquals(post.getUpdatedAt(), foundPost.getUpdatedAt());
        assertEquals(post.isDeleted(), foundPost.isDeleted());
    }

    @DisplayName("게시글 존재 조회")
    @Test
    void existsById() {
        //given
        createPost();

        //when
        boolean exists = postDao.existsById(post.getId());

        //then
        assertTrue(exists);
        boolean noExists = postDao.existsById(1000L);
        assertFalse(noExists);
    }

    @DisplayName("게시글 전체 조회")
    @Test
    void findAll() {
        //given
        postDao.deleteAll();
        postDao.save(Post.of(user.getId(), "first-title", "first-content", "first-filename"));
        postDao.save(Post.of(user.getId(), "second-title", "second-content", "second-filename"));
        postDao.save(Post.of(user.getId(), "third-title", "third-content", "third-filename"));
        postDao.save(Post.of(user.getId(), "fourth-title", "fourth-content", "fourth-filename"));

        //when
        List<Post> allPosts = postDao.findAll();
        int total = postDao.countAll();

        //then
        assertEquals(4, allPosts.size());
        assertEquals(total, allPosts.size());
    }

    @DisplayName("게시글 페이지 조회- 첫번째 페이지")
    @Test
    void findPostWithAuthorByPageSortByCreatedAtDescFirstPage() {
        //given
        postDao.deleteAll();
        postDao.save(Post.of(user.getId(), "first-title", "first-content", "first-filename"));
        postDao.save(Post.of(user.getId(), "second-title", "second-content", "second-filename"));
        postDao.save(Post.of(user.getId(), "third-title", "third-content", "third-filename"));
        postDao.save(Post.of(user.getId(), "fourth-title", "fourth-content", "fourth-filename"));
        postDao.save(Post.of(user.getId(), "fifth-title", "fifth-content", "fifth-filename"));

        //when
        Page<PostDetailsDto> page = postDao.findPostWithAuthorByPageSortByCreatedAtDesc(1, 2);

        //then
        assertEquals(2, page.getActualSize());
        assertEquals(1, page.getPageNumber());
        assertEquals(2, page.getPageSize());
        assertTrue(page.getIsFirstPage());
        assertFalse(page.getIsLastPage());
        PostDetailsDto firstPost = page.getContent().get(0);
        PostDetailsDto secondPost = page.getContent().get(1);
        assertTrue(firstPost.getCreatedAt().isAfter(secondPost.getCreatedAt()));
        assertEquals("fifth-title", firstPost.getTitle());
        assertEquals("fourth-title", secondPost.getTitle());
    }

    @DisplayName("게시글 페이지 조회- 마지막 페이지")
    @Test
    void findPostWithAuthorByPageSortByCreatedAtDescLastPage() {
        //given
        postDao.deleteAll();
        postDao.save(Post.of(user.getId(), "first-title", "first-content", "first-filename"));
        postDao.save(Post.of(user.getId(), "second-title", "second-content", "second-filename"));
        postDao.save(Post.of(user.getId(), "third-title", "third-content", "third-filename"));
        postDao.save(Post.of(user.getId(), "fourth-title", "fourth-content", "fourth-filename"));
        postDao.save(Post.of(user.getId(), "fifth-title", "fifth-content", "fifth-filename"));

        //when
        Page<PostDetailsDto> page = postDao.findPostWithAuthorByPageSortByCreatedAtDesc(3, 2);

        //then
        assertEquals(1, page.getActualSize());
        assertEquals(3, page.getPageNumber());
        assertEquals(2, page.getPageSize());
        assertFalse(page.getIsFirstPage());
        assertTrue(page.getIsLastPage());
        PostDetailsDto post = page.getContent().get(0);
        assertEquals("first-title", post.getTitle());
    }

    @DisplayName("게시글 페이지 조회- 없는 페이지")
    @Test
    void findPostWithAuthorByPageSortByCreatedAtDescNonExistPage() {
        //given
        postDao.deleteAll();
        postDao.save(Post.of(user.getId(), "first-title", "first-content", "first-filename"));
        postDao.save(Post.of(user.getId(), "second-title", "second-content", "second-filename"));
        postDao.save(Post.of(user.getId(), "third-title", "third-content", "third-filename"));
        postDao.save(Post.of(user.getId(), "fourth-title", "fourth-content", "fourth-filename"));
        postDao.save(Post.of(user.getId(), "fifth-title", "fifth-content", "fifth-filename"));

        //when & then
        assertThrows(IllegalArgumentException.class, () ->
                postDao.findPostWithAuthorByPageSortByCreatedAtDesc(4, 2));
    }

    @DisplayName("게시글 삭제")
    @Test
    void deleteById() {
        //given
        createPost();

        //when
        postDao.deleteById(post.getId());

        //then
        Optional<Post> found = postDao.findById(post.getId());
        assertTrue(found.isEmpty());
    }

    @DisplayName("게시글 전체 삭제")
    @Test
    void deleteAll() {
        //given
        Post firstPost = postDao.save(Post.of(user.getId(), "first-title", "first-content", "first-filename"));
        Post secondPost = postDao.save(Post.of(user.getId(), "second-title", "second-content", "second-filename"));

        //when
        postDao.deleteAll();

        //then
        List<Post> posts = postDao.findAll();
        assertEquals(0, posts.size());
    }

    @DisplayName("게시글 카운트")
    @Test
    void countAll() {
        //given
        Post firstPost = postDao.save(Post.of(user.getId(), "first-title", "first-content", "first-filename"));
        Post secondPost = postDao.save(Post.of(user.getId(), "second-title", "second-content", "second-filename"));

        //when
        int count = postDao.countAll();

        //then
        assertEquals(2, count);
    }

    @DisplayName("게시글 카운트 - 삭제된 게시글")
    @Test
    void countAllContainsDeletedPost() {
        //given
        Post firstPost = postDao.save(Post.of(user.getId(), "first-title", "first-content", "first-filename"));
        Post secondPost = postDao.save(Post.of(user.getId(), "second-title", "second-content", "second-filename"));
        Post thirdPost = postDao.save(Post.of(user.getId(), "third-title", "third-content", "third-filename"));
        postDao.deleteById(thirdPost.getId());

        //when
        int count = postDao.countAll();

        //then
        assertEquals(2, count);
    }

    @DisplayName("게시글 조회 - findPostWithAuthorById")
    @Test
    void findPostWithAuthorById() {
        //given
        createPost();

        //when
        Optional<PostDetailsDto> optionalPost = postDao.findPostWithAuthorById(post.getId());

        //then
        assertTrue(optionalPost.isPresent());
        PostDetailsDto postWithDetail = optionalPost.get();
        assertNotNull(postWithDetail.getPostId());
        assertEquals(user.getId(), postWithDetail.getAuthorId());
        assertEquals(user.getUsername(), postWithDetail.getAuthorUsername());
        assertEquals(post.getCreatedAt(), postWithDetail.getCreatedAt());
    }

    @DisplayName("게시글 수정")
    @Test
    void update() {
        //given
        createPost();
        post.update("updated-content", "updated-title", "updated-filename");

        //when
        Post updatedPost = postDao.save(post);

        //then
        assertEquals(post.getId(), updatedPost.getId());
        assertEquals(post.getAuthorId(), updatedPost.getAuthorId());
        assertEquals("updated-content", updatedPost.getContent());
        assertEquals("updated-title", updatedPost.getTitle());
        assertEquals("updated-filename", updatedPost.getFileName());
        assertEquals(post.getCreatedAt(), updatedPost.getCreatedAt());
        assertEquals(post.getView(), updatedPost.getView());
        assertEquals(post.getCreatedAt(), updatedPost.getCreatedAt());
        assertEquals(post.getUpdatedAt(), updatedPost.getUpdatedAt());
        assertTrue(post.getCreatedAt().isBefore(post.getUpdatedAt()));
        assertEquals(post.isDeleted(), updatedPost.isDeleted());
    }

    @DisplayName("게시글 조회수 증가")
    @Test
    void addView() {
        //given
        createPost();

        //when
        postDao.addView(post);
        postDao.addView(post);
        postDao.addView(post);

        //then
        assertEquals(3, post.getView());
    }

    @DisplayName("게시글 조회수 증가 실패 - 없는 글")
    @Test
    void addViewNonExistPost() {
        //given unsaved post
        Post post = Post.of(user.getId(), "test-title", "test-content", "test-filename");
        int originalView = post.getView();
        //when & then
        postDao.addView(post);
        assertEquals(originalView, post.getView());
    }
}
