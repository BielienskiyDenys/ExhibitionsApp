package database;


import org.example.exhibitionsapp.entity.Exhibition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface ExhibitionRepository extends JpaRepository<Exhibition, Long> {
    List<Exhibition> findByExNameEng(String exNameEng);
    List<Exhibition> findAllByExNameEngContainingOrExNameNativeContaining(String exNameEng, String exNameNative);
    List<Exhibition> findAllByExhibStatus(ExhibStatus exhibStatus);
    List<Exhibition> findAllByEventTheme1ContainingOrEventTheme2ContainingOrEventTheme3Containing(String theme1, String theme2, String theme3);
    List<Exhibition> findAllByTicketPriceIsBetween(Long from, Long upTo);
    List<Exhibition> findAllByEndDateIsAfterAndStartDateIsBefore(Date startDate, Date endDate);
}
