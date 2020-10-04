package org.example.exhibitionsapp.service;

import org.example.exhibitionsapp.entity.HallSchedule;
import org.example.exhibitionsapp.repository.ExhibitionRepository;
import org.example.exhibitionsapp.repository.HallScheduleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

@Service
public class HallService {
    Logger logger = LoggerFactory.getLogger(HallService.class);
    @Autowired
    private HallScheduleRepository hallScheduleRepository;
    @Autowired
    private ExhibitionRepository exhibitionRepository;

    @Transactional
    public boolean addNewHallSchedule(HallSchedule hallSchedule) {
        if (hallScheduleRepository.findAllByHallNameAndStartDateIsBeforeAndEndDateIsAfter(hallSchedule.getHallName(), hallSchedule.getStartDate(), hallSchedule.getEndDate()).isEmpty()
                && hallScheduleRepository.findAllByHallNameAndStartDateIsBetweenOrHallNameAndEndDateIsBetween(hallSchedule.getHallName(), hallSchedule.getStartDate(), hallSchedule.getEndDate(), hallSchedule.getHallName(), hallSchedule.getStartDate(), hallSchedule.getEndDate()).isEmpty()) {
            hallScheduleRepository.save(hallSchedule);
            return true;
        }
        logger.error("Erroe during adding new hallSchedule" + hallSchedule);
        return false;
    }

    public List<HallSchedule> sortByDate(String date1, String date2, Pageable pageable) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            c1.setTime(sdf.parse(date1));
            c2.setTime(sdf.parse(date2));
        } catch (ParseException e) {
           logger.error("Error when parsing dates.", e);
            e.printStackTrace();
        }
        return hallScheduleRepository.findAllByStartDateIsBetweenOrEndDateIsBetweenOrStartDateIsBeforeAndEndDateIsAfter(c1, c2, c1, c2, c1, c2);
    }

    public List<HallSchedule> findActiveEvents() {
        return hallScheduleRepository.findAllByEndDateIsAfter(Calendar.getInstance());
    }

    public List<HallSchedule> findEndedEvents() {
        return hallScheduleRepository.findAllByEndDateBefore(Calendar.getInstance());
    }
}
