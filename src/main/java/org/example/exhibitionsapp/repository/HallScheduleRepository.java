package org.example.exhibitionsapp.repository;

import org.example.exhibitionsapp.entity.HallName;
import org.example.exhibitionsapp.entity.HallSchedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Calendar;
import java.util.List;

public interface HallScheduleRepository extends JpaRepository<HallSchedule, Long> {
    List<HallSchedule> findAll();
    List<HallSchedule> findAllByExhibitionId(Long id);
    List<HallSchedule> findAllByHallNameAndStartDateIsBetweenOrHallNameAndEndDateIsBetween(HallName hallName1, Calendar startDate1, Calendar endDate1, HallName hallName2, Calendar startDate2, Calendar endDate2);
    List<HallSchedule> findAllByHallNameAndStartDateIsBeforeAndEndDateIsAfter(HallName hallName, Calendar startDate, Calendar endDate);
    List<HallSchedule> findAllByStartDateIsBetweenOrEndDateIsBetweenOrStartDateIsBeforeAndEndDateIsAfter(Calendar startDate1, Calendar endDate1, Calendar startDate2, Calendar endDate2, Calendar startDate3, Calendar endDate3);
    List<HallSchedule> findAllByEndDateIsAfter(Calendar today);
    List<HallSchedule> findAllByEndDateBefore (Calendar today);
}
