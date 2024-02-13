package com.shop.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.constant.ItemSellStatus;
import com.shop.dto.ItemSearchDto;
import com.shop.dto.MainItemDto;
import com.shop.dto.QMainItemDto;
import com.shop.entity.*;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ItemRepositoryCustomImpl implements ItemRepositoryCustom{
    private JPAQueryFactory queryFactory; // 동적쿼리 사용하기 위해 JPAQueryFactory 변수 선언
    // 생성자
    public ItemRepositoryCustomImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em); // JPAQueryFactory 실질적인 객체가 만들어 집니다.
    }

    private BooleanExpression searchByCategoryId(Long categoryId) {
        return categoryId != null ? QItem.item.category.id.eq(categoryId) : null;
    }
    private BooleanExpression searchSellStatusEq(ItemSellStatus searchSellStatus){
        return searchSellStatus == null ?
                null : QItem.item.itemSellStatus.eq(searchSellStatus);
        //ItemSellStatus null이면 null 리턴 null 아니면 SELL, SOLD 둘중 하나 리턴
    }
    private  BooleanExpression regDtsAfter(String searchDateType){ // all, 1d, 1w, 1m 6m
        LocalDateTime dateTime = LocalDateTime.now(); // 현재시간을 추출해서 변수에 대입

        if(StringUtils.equals("all",searchDateType) || searchDateType == null){
            return null;
        }else if(StringUtils.equals("1d",searchDateType)){
            dateTime = dateTime.minusDays(1);
        }else if(StringUtils.equals("1w",searchDateType)){
            dateTime = dateTime.minusWeeks(1);
        }else if(StringUtils.equals("1m",searchDateType)){
            dateTime = dateTime.minusMonths(1);
        }else if(StringUtils.equals("6m",searchDateType)){
            dateTime = dateTime.minusMonths(6);
        }
        return QItem.item.regTime.after(dateTime);
        //dateTime을 시간에 맞게 세팅 후 시간에 맞는 등록된 상품이 조회하도록 조건값 반환
    }
    private BooleanExpression searchByLike(String searchBy, String searchQuery){
        if(StringUtils.equals("itemNm",searchBy)){ // 상품명
            return QItem.item.itemNm.like("%"+searchQuery+"%");
        }else if(StringUtils.equals("createdBy",searchBy)){ // 작성자
            return QItem.item.createdBy.like("%"+searchQuery+"%");
        }
        return null;
    }
    @Override
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        QueryResults<Item> results = queryFactory.selectFrom(QItem.item).
                join(QItem.item.category).fetchJoin().
                where(regDtsAfter(itemSearchDto.getSearchDateType()),
                        searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
                        searchByLike(itemSearchDto.getSearchBy(),itemSearchDto.getSearchQuery()))
                .orderBy(QItem.item.id.desc())
                .offset(pageable.getOffset()).limit(pageable.getPageSize()).fetchResults();
        List<Item> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content,pageable,total);
    }
    private BooleanExpression itemNmLike(String searchQuery){
        return StringUtils.isEmpty(searchQuery) ? null : QItem.item.itemNm.like("%"+searchQuery+"%");
    }
    @Override
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable, Long categoryId, Long memberId) {
        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;
        QLike like = QLike.like; // 추가: Like 엔티티

        QueryResults<MainItemDto> results = queryFactory
                .select(new QMainItemDto(item.id, item.itemNm, itemImg.imgUrl, item.price,
                                JPAExpressions.select(like.id)
                                        .from(like)
                                        .where(like.item.id.eq(item.id).and(like.member.id.eq(memberId))).exists()
                        )
                )
                .from(itemImg).join(itemImg.item, item)
                .join(item.category)
                .where(itemImg.repImgYn.eq("Y"), categoryEq(categoryId), itemNmLike(itemSearchDto.getSearchQuery()))
                .orderBy(item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<MainItemDto> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);
    }
    @Override
    public Page<MainItemDto> getMainItemPageCategorys(ItemSearchDto itemSearchDto, Pageable pageable, List<Category> categoryList, Long memberId) {
        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;
        QLike like = QLike.like; // 추가: Like 엔티티

        QueryResults<MainItemDto> results = queryFactory
                .select(new QMainItemDto(item.id, item.itemNm, itemImg.imgUrl, item.price,
                                JPAExpressions.select(like.id)
                                        .from(like)
                                        .where(like.item.id.eq(item.id).and(like.member.id.eq(memberId))).exists()
                        )
                )
                .from(itemImg).join(itemImg.item, item)
                .join(item.category)
                .where(itemImg.repImgYn.eq("Y"), categoryIn(categoryList), itemNmLike(itemSearchDto.getSearchQuery()))
                .orderBy(item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<MainItemDto> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);
    }
    private BooleanExpression categoryEq(Long categoryId) {
        return categoryId != null ? QItem.item.category.id.eq(categoryId) : null;
    }
    private BooleanExpression categoryIn(List<Category> categories) {
        if (!categories.isEmpty()) {
            // 스트림과 람다식을 사용해서 리스트의 카테고리 id만 추출해 List<Long>으로 만듦
            // categories.stream() : list -> stream 으로 변환
            // Category::getId: 람다식 표현을 사용한 메소드 참조, Category 클래스의 getId 메소드를 참조하여 각 요소에 대해 해당 메소드를 호출함
            List<Long> categoryIds = categories.stream().map(Category::getId).collect(Collectors.toList());
            // 쿼리 생성
            // QCategory.category.id.in(categoryIds):  id는 QCategory 클래스에서 정의된 카테고리 엔터티의 ID를 나타냅니다.
            // .in(categoryIds)는 주어진 리스트(categoryIds)에 속하는 ID를 가진 엔터티를 선택하는 조건을 생성합니다. 예를 들어, ID가 리스트에 포함된 값 중 하나와 일치하는 경우 해당 엔터티를 선택
            return QCategory.category.id.in(categoryIds).or(QCategory.category.parentCategoryId.in(categoryIds));
        } else {
            return null;
        }
    }

    @Override
    public Page<MainItemDto> getAllItems(ItemSearchDto itemSearchDto, Pageable pageable, Long memberId) {
        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;
        QLike like = QLike.like;

        QueryResults<MainItemDto> results = queryFactory
                .select(new QMainItemDto(item.id, item.itemNm, itemImg.imgUrl, item.price, JPAExpressions.select(like.id)
                        .from(like)
                        .where(like.item.id.eq(item.id).and(like.member.id.eq(memberId))).exists()))
                .from(itemImg).join(itemImg.item, item)
                .join(item.category)
                .where(itemImg.repImgYn.eq("Y"), itemNmLike(itemSearchDto.getSearchQuery()))
                .orderBy(item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<MainItemDto> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);
    }
    @Override
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable, Long categoryId) {
        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;

        // QMainItemDto @QueryProjection을 활용하면 DTO로 바로 조회 가능
        QueryResults<MainItemDto> results = queryFactory.select(new QMainItemDto(item.id, item.itemNm, itemImg.imgUrl, item.price))
                // join 내부조인 .repImgYn.eq("Y") 대표이미지만 가져온다.
                .from(itemImg).join(itemImg.item, item)
                .join(item.category) // 추가: 아이템의 카테고리와 조인
                .where(itemImg.repImgYn.eq("Y"), categoryEq(categoryId), itemNmLike(itemSearchDto.getSearchQuery()))
                .orderBy(item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<MainItemDto> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<MainItemDto> getMainItemPageCategorys(ItemSearchDto itemSearchDto, Pageable pageable, List<Category> categoryList) {
        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;

        // QMainItemDto @QueryProjection을 활용하면 DTO로 바로 조회 가능
        QueryResults<MainItemDto> results = queryFactory.select(new QMainItemDto(item.id, item.itemNm, itemImg.imgUrl, item.price))
                // join 내부조인 .repImgYn.eq("Y") 대표이미지만 가져온다.
                .from(itemImg).join(itemImg.item, item)
                .join(item.category) // 추가: 아이템의 카테고리와 조인
                .where(itemImg.repImgYn.eq("Y"), categoryIn(categoryList), itemNmLike(itemSearchDto.getSearchQuery()))
                .orderBy(item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<MainItemDto> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<MainItemDto> getAllItems(ItemSearchDto itemSearchDto, Pageable pageable) {
        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;

        // QMainItemDto @QueryProjection을 활용하면 DTO로 바로 조회 가능
        QueryResults<MainItemDto> results = queryFactory.select(new QMainItemDto(item.id, item.itemNm, itemImg.imgUrl, item.price))
                // join 내부조인 .repImgYn.eq("Y") 대표이미지만 가져온다.
                .from(itemImg).join(itemImg.item, item)
                .join(item.category) // 추가: 아이템의 카테고리와 조인
                .where(itemImg.repImgYn.eq("Y"), itemNmLike(itemSearchDto.getSearchQuery()))
                .orderBy(item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<MainItemDto> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);
    }
}