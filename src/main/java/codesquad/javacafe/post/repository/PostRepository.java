package codesquad.javacafe.post.repository;

import codesquad.javacafe.member.repository.MemberRepository;
import codesquad.javacafe.post.dto.request.PostRequestDto;
import codesquad.javacafe.post.entity.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static codesquad.javacafe.common.db.DBConnection.close;
import static codesquad.javacafe.common.db.DBConnection.getConnection;

public class PostRepository {
    private static final Logger log = LoggerFactory.getLogger(PostRepository.class);
    private static final Map<Long, Post> map = new ConcurrentHashMap<>();
    private static final PostRepository instance = new PostRepository();

    private PostRepository() {
    }

    public static PostRepository getInstance() {
        return instance;
    }


    public Post save(PostRequestDto postDto) {
        Post post = postDto.toEntity();
        var sql = "insert into post(post_writer, post_title, post_contents, post_create, member_id)\n" +
                "values (?,?,?,?,?)";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, post.getWriter());
            ps.setString(2, post.getTitle());
            ps.setString(3, post.getContents());
            ps.setTimestamp(4, Timestamp.valueOf(post.getCreatedAt()));
            ps.setLong(5, post.getMemberId());
            ps.executeUpdate();

            var generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                long pk = generatedKeys.getLong(1);
                log.debug("[Post PK] {}", pk);
                post.setId(pk);
            }

            log.debug("[PostRepository] created post: {}", post);

            return post;
        } catch (SQLException exception) {
            log.error("[SQLException] throw error when member save, Class Info = {}", MemberRepository.class);
            throw new RuntimeException(exception);
        } finally {
            close(con, ps, null);

        }
    }

    public List<Post> findAll() {
        var sql = " select * from post";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            ps = con.prepareStatement(sql);

            rs = ps.executeQuery();
            if (rs.next()) {
                var postList = new ArrayList<Post>();
                do {
                    var post = new Post();
                    post.setId(rs.getLong("id"));
                    post.setWriter(rs.getString("post_writer"));
                    post.setTitle(rs.getString("post_title"));
                    post.setContents(rs.getString("post_contents"));
                    post.setCreatedAt(rs.getTimestamp("post_create").toLocalDateTime());
                    postList.add(post);
                } while (rs.next());

                return postList;
            } else {
                log.info("[PostReository] 게시글 정보가 없습니다.");
                return null;
            }

        } catch (SQLException exception) {
            log.error("[SQLException] throw error when findById, Class info = {}", MemberRepository.class);
            throw new RuntimeException(exception);
        } finally {
            close(con, ps, rs);
        }
    }

    public Post findById(long id) {
        log.debug("[Post Find] pk = {}", id);
        var sql = " select * from post where id = ?";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            ps = con.prepareStatement(sql);
            ps.setLong(1, id);

            rs = ps.executeQuery();
            if (rs.next()) {
                var post = new Post();
                post.setId(rs.getLong("id"));
                post.setWriter(rs.getString("post_writer"));
                post.setTitle(rs.getString("post_title"));
                post.setContents(rs.getString("post_contents"));
                post.setCreatedAt(rs.getTimestamp("post_create").toLocalDateTime());
                post.setMemberId(rs.getLong("member_id"));

                return post;
            } else {
                log.info("[PostReository] 게시글 정보가 없습니다.");
                return null;
            }

        } catch (SQLException exception) {
            log.error("[SQLException] throw error when findById, Class info = {}", MemberRepository.class);
            throw new RuntimeException(exception);
        } finally {
            close(con, ps, rs);
        }
    }

    public int update(PostRequestDto postDto) {
        Post post = postDto.toEntity();
        var sql = "update post set post_title = ?, post_contents =?\n" +
                "where id = ? and member_id = ?";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, post.getTitle());
            ps.setString(2, post.getContents());
            ps.setLong(3, post.getId());
            ps.setLong(4, post.getMemberId());
            int result = ps.executeUpdate();


            log.debug("[PostRepository] updated post: {}", post);

            return result;
        } catch (SQLException exception) {
            log.error("[SQLException] throw error when member save, Class Info = {}", MemberRepository.class);
            throw new RuntimeException(exception);
        } finally {
            close(con, ps, null);

        }
    }

    public int delete(long postId) {
        var sql = "delete from post where id = ?";

        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = getConnection();
            ps = con.prepareStatement(sql);
            ps.setLong(1, postId);
            int result = ps.executeUpdate();

            return result;
        } catch (SQLException exception) {
            log.error("[SQLException] throw error when member save, Class Info = {}", MemberRepository.class);
            throw new RuntimeException(exception);
        } finally {
            close(con, ps, null);
        }
    }
}
