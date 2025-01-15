package com.librarymanagementsystem;

import com.librarymanagementsystem.dto.request.BookRequest;
import com.librarymanagementsystem.mapper.BookMapper;
import com.librarymanagementsystem.model.entity.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class BookMapperTest {

    @Autowired
    private BookMapper bookMapper;

    @Test
    public void testRequestToEntity() {
        BookRequest request = new BookRequest();
        request.setCategoryId(1L);
        request.setAuthorId(List.of(2L, 3L));

        Book book = bookMapper.requestToEntity(request);

        assertNotNull(book);
        assertEquals(1L, book.getCategory().getId());
        assertEquals(2, book.getAuthors().size());
        assertEquals(2L, book.getAuthors().get(0).getId());
    }
}
