package com.fastcampus.faststore.service;

import com.fastcampus.faststore.entity.Book;
import com.fastcampus.faststore.entity.BookInventory;
import com.fastcampus.faststore.entity.BookSale;
import com.fastcampus.faststore.entity.DiscountPolicy;
import com.fastcampus.faststore.repository.BookInventoryRepository;
import com.fastcampus.faststore.repository.BookRepository;
import com.fastcampus.faststore.repository.BookSaleRepository;
import com.fastcampus.faststore.type.DiscountType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@SpringBootTest
public class BookInventoryServiceTest {

    @Autowired
    private BookInventoryRepository bookInventoryRepository;

    @Autowired
    private BookRepository bookRepository;

    @Mock
    private BookService bookService;

    @Mock
    private BookSaleService bookSaleService;

    private BookInventoryService bookInventoryService;

    @BeforeEach
    public void setup() {
        bookInventoryService = new BookInventoryService(bookInventoryRepository, bookService, bookSaleService);
    }

    @AfterEach
    public void tearDown() {
        bookInventoryRepository.deleteAll();
        bookRepository.deleteAll();
    }

    // 완료
    // TODO: BookInventoryService의 storeBook의 테스트 코드를 작성하세요.
    @Test
    @Transactional
    public void storeBook() {
        // book 생성하여 저장
        Book book = new Book("토지", "박경리", 30000L);
        Book newBook = bookRepository.save(book);
        // bookSale 생성하여 저장
        DiscountPolicy discountPolicy = new DiscountPolicy(DiscountType.PERCENT, 10L);
        BookSale bookSale = new BookSale(book, discountPolicy);

        given(bookService.getOrThrow("토지")).willReturn(book);
        given(bookSaleService.getOrThrow(book)).willReturn(bookSale);

        // storeBook 호출
        bookInventoryService.storeBook("토지");

        // 테스트 코드
        Optional<BookInventory> findBookInventory = bookInventoryRepository.findByBook(newBook);
        Assertions.assertNotNull(findBookInventory.isPresent());

    }

    @Test
    @Transactional
    public void sellBook() {
        Book book = new Book("자바의 정석", "남궁성", 30000L);
        bookRepository.save(book);

        bookInventoryRepository.save(new BookInventory(book, 1L, 27000L));

        DiscountPolicy discountPolicy = new DiscountPolicy(DiscountType.PERCENT, 10L);
        BookSale bookSale = new BookSale(book, discountPolicy);
        given(bookService.getOrThrow("자바의 정석")).willReturn(book);
        given(bookSaleService.getOrThrow(book)).willReturn(bookSale);

        bookInventoryService.sellBook("자바의 정석");

        assertThat(bookInventoryRepository.count()).isEqualTo(1L);
        BookInventory result = bookInventoryRepository.findAll().get(0);
        assertThat(result.getBook().getTitle()).isEqualTo(book.getTitle());
        assertThat(result.getBook().getAuthor()).isEqualTo(book.getAuthor());
        assertThat(result.getBook().getPrice()).isEqualTo(book.getPrice());
        assertThat(result.getCount()).isEqualTo(0L);
        assertThat(result.getIncome()).isEqualTo(0L);
    }
}
