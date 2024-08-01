package codesquad.article.service;

import codesquad.common.exception.NoSuchElementException;
import codesquad.common.exception.UnauthorizedRequestException;

/**
 * <h3>Article 삭제를 위한 서비스</h3>
 * <h4>요구사항</h4>
 * <ol>
 *     <li>Article(이하 게시글)은 Comment(이하 댓글)이 없는 경우 삭제가 가능하다.</li>
 *     <li>게시글 작성자와 댓글 작성자가 모두 같은 경우 삭제가 가능하다.</li>
 *     <li>게시글 작성자와 댓글 작성자가 다를 경우 삭제는 불가능하다.</li>
 *     <li>게시글을 삭제할 때 댓글 또한 삭제해야 한다. 댓글의 삭제 또한 삭제 상태를 변경한다.</li>
 * </ol>
 *
 * <h4>트랜젝션 및 동시성 제어 전략</h4>
 * 위의 요구사항은 두 단계로 나눌 수 있습니다.
 * <ol>
 *     <li>검증: 댓글이 있는지 확인합니다.</li>
 *     <li>실행: 검증이 성공하면 게시글을 삭제합니다.</li>
 * </ol>
 * 검증 및 실행을 하나의 쿼리로 수행할 수도 있습니다. 이 경우 특별히 트랜젝션이 필요하지 않습니다. 하지만 동시성 제어를 할 방법이 없습니다.
 * 즉, 동시성 제어(게시글 삭제 쿼리 vs 댓글 생성 쿼리)를 위해 위 요구사항은 두 개의 쿼리로 분리하여 수행해야 합니다.
 * 또한 댓글을 생성하는 도중에 게시글이 삭제되는 문제를 방지하기 위해 게시글에 S-lock을 걸고, 댓글을 생성해야 합니다.
 * 정리하면,
 * <table>
 *     <thead>
 *         <tr>
 *             <td>전략</td>
 *             <td>장점</td>
 *             <td>단점</td>
 *         </tr>
 *     </thead>
 *     <tbody>
 *         <tr>
 *             <td>한방쿼리</td>
 *             <td>lock없이 처리되기 때문에 Connection이 빠르게 반환되고 처리속도가 빠르다.</td>
 *             <td>동시성 제어를 할 수 없다. 그로 인해 비활성상태의 게시글에 활성상태의 댓글이 달릴 수 있습니다.</td>
 *         </tr>
 *         <tr>
 *             <td>두방쿼리</td>
 *             <td>동세성 제어를 할 수 있다.</td>
 *             <td>lock 경합으로 인해 Connection을 점유한 상태로 대기하게되고, 처리속도가 느리다.</td>
 *         </tr>
 *     </tbody>
 * </table>
 *
 * <h4>결론</h4>
 * 한방쿼리를 이용하는 경우 고객들은 댓글을 작성했는데 게시글이 삭제되어있는 상황을 겪을 수 있습니다.
 * 이로 인해 고객이 게시판을 이용하는데에 큰 불편을 겪지는 않겠지만 데이터 정합성이 깨지는 문제가 있습니다.
 * 따라서 두방쿼리로 처리하는 것이 장기적으로 보았을 때 바람직하다고 결론내렸습니다.
 */
public interface DeleteArticleService {
    void delete(Command cmd) throws NoSuchElementException, UnauthorizedRequestException;

    record Command(
            long articleId,
            String userId
    ) {
    }
}



