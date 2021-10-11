package com.fastcampus.faststore.service;

import com.fastcampus.faststore.entity.Book;
import com.fastcampus.faststore.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    @Transactional(readOnly = true)
    public Book getOrThrow(String title) {
        return bookRepository.findByTitle(title)
                .orElseThrow(() -> new RuntimeException("해당 제목의 책이 존재하지 않습니다."));
    }

    // 완료
    // TODO: 동일한 책의 제목이 존재하면 RunTimeException을 발생시키고, 그렇지 않으면 책을 저장하도록 구현하세요. 제목으로 책을 조회해오는 쿼리는 이미 BookRepository에 등록되어있습니다.
    @Transactional
    public void registerBook(String title, String author, Long price) {
        Optional<Book> findBook = bookRepository.findByTitle(title);
        if(findBook.isPresent()) {
            // 동일한 책 존재할 경우
            throw new RuntimeException("해당 제목의 책이 이미 존재 합니다.");
        } else {
            // 동일한 책 존재하지 않을 경우
            Book book = Book.builder()
                    .title(title)
                    .author(author)
                    .price(price)
                    .build();
            Book newBook = bookRepository.save(book);
        }
    }
}
