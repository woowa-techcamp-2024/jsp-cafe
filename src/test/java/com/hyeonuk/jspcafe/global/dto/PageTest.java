package com.hyeonuk.jspcafe.global.dto;

import com.hyeonuk.jspcafe.global.domain.Page;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PageResponseDtoRequest 클래스")
class PageTest {
    private Page<Integer> page;

    @Nested
    @DisplayName("pageList 메서드는")
    class PageListTest {
        int totalPage = 96;
        int size = 10;

        @Test
        @DisplayName("요청한 페이지의 범위가 size개수 만큼 나온다.")
        void requestWithinPageSize(){
            //given
            int page = 2;
            PageTest.this.page = new Page<>(size,page,totalPage,List.of());
            List<Integer> expected = IntStream
                    .rangeClosed(page*size+1,(page+1)*size)
                    .boxed()
                    .toList();

            //when
            List<Integer> list = PageTest.this.page.pageList();

            //then
            assertEquals(expected,list);
        }

        @Test
        @DisplayName("요청한 페이지의 범위가 size개수 미만일 경우에는 남은 개수만큼만 나온다.")
        void requestRemainPage(){
            //given
            int page = 9;
            PageTest.this.page = new Page<>(size,page,totalPage,List.of());
            List<Integer> expected = IntStream
                    .rangeClosed(page*size+1,totalPage)
                    .boxed()
                    .toList();

            //when
            List<Integer> list = PageTest.this.page.pageList();

            //then
            assertEquals(expected,list);
        }
    }

    @Nested
    @DisplayName("hasNextPage 메서드는")
    class HasNextPageTest {
        int totalPage = 96;
        int size = 10;
        @Test
        @DisplayName("다음 페이지가 존재하면 true다")
        void hasNextPageTrue(){
            //given
            int page = totalPage - 1;
            PageTest.this.page = new Page<>(size,page,totalPage,List.of());

            //when & then
            assertTrue(PageTest.this.page.isNextPage());
        }

        @Test
        @DisplayName("다음 페이지가 존재하지 않는다면 false다")
        void hasNextPageFalse(){
            //given
            int page = totalPage;
            PageTest.this.page = new Page<>(size,page,totalPage,List.of());

            //when & then
            assertFalse(PageTest.this.page.isNextPage());
        }
    }

    @Nested
    @DisplayName("hasNextPage 메서드는")
    class HasPreviousPageTest {
        int totalPage = 96;
        int size = 10;
        @Test
        @DisplayName("이전 페이지가 존재하면 true다")
        void hasNextPageTrue(){
            //given
            int page = totalPage - 1;
            PageTest.this.page = new Page<>(size,page,totalPage,List.of());

            //when & then
            assertTrue(PageTest.this.page.isPreviousPage());
        }

        @Test
        @DisplayName("다음 페이지가 존재하지 않는다면 false다")
        void hasNextPageFalse(){
            //given
            int page = 0;
            PageTest.this.page = new Page<>(size,page,totalPage,List.of());

            //when & then
            assertFalse(PageTest.this.page.isPreviousPage());
        }
    }

}