package com.fastcampus.faststore.entity;

import com.fastcampus.faststore.type.DiscountType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
/**
 * 책 판매 Entity
 * 책과 할인 정책 정보를 함께 관리한다
 * */
public class BookSale extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    private DiscountPolicy discountPolicy;

    // 완료
    // TODO: 책이 본래 가진 price에 할인 정책을 적용하여 가격을 반환하도록 한다. BookSaleTest를 성공시킨다.
    public Long getPrice() {
        Long price = book.getPrice();
        Long resultPrice = discountPolicy.getDiscountType() == DiscountType.AMOUNT ?
                price - discountPolicy.getAmount() :
                // 기본값 - (기본값 X 퍼센트 ÷ 100)
                price - (price * discountPolicy.getAmount() / 100);
        return resultPrice;
    }
}
