package org.example.exhibitionsapp.repository;

import org.example.exhibitionsapp.entity.Exhibition;
import org.example.exhibitionsapp.entity.ExhibitionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExhibitionRepository extends JpaRepository<Exhibition, Long> {
    List<Exhibition> findByExNameEngOrExNameNative(String exNameEng, String exNameNative);
    Page<Exhibition> findAll(Pageable pageable);
    Page<Exhibition> findAllByExNameEngContainingOrExNameNativeContaining(String exNameEng, String exNameNative, Pageable pageable);
    Page<Exhibition> findAllByExhibitionStatus(ExhibitionStatus exhibStatus, Pageable pageable);
    Page<Exhibition> findAllByThemesEngContainingOrThemesNativeContaining(String theme1, String theme2, Pageable pageable);
    Page<Exhibition> findAllByTicketPriceIsBetween(Long from, Long upTo, Pageable pageable);
}
