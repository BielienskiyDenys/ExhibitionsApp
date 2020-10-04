package org.example.exhibitionsapp.service;

import org.example.exhibitionsapp.entity.Exhibition;
import org.example.exhibitionsapp.entity.ExhibitionStatus;
import org.example.exhibitionsapp.entity.HallName;
import org.example.exhibitionsapp.entity.HallSchedule;
import org.example.exhibitionsapp.exceptions.FailedToAddExhibitionException;
import org.example.exhibitionsapp.repository.ExhibitionRepository;
import org.example.exhibitionsapp.repository.HallScheduleRepository;
import org.example.exhibitionsapp.repository.TicketRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.awt.*;
import java.util.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class ExhibitionServiceTest {
    @Autowired
    private ExhibitionService exhibitionService;

    @MockBean
    private ExhibitionRepository exhibitionRepository;
    @MockBean
    private HallScheduleRepository hallScheduleRepository;

    @Test
    public void addNewExhibition() {
        Exhibition exhibition = new Exhibition();
        exhibition.setExNameEng("test");
        exhibition.setExNameNative("тест");
        boolean isExhibitionCreated = false;
        try {
            isExhibitionCreated = exhibitionService.addNewExhibition(exhibition);
        } catch (FailedToAddExhibitionException e) {

        }
        Assert.assertTrue(isExhibitionCreated);
        Mockito.verify(exhibitionRepository, Mockito.times(1)).findByExNameEngOrExNameNative("test", "тест");
        Mockito.verify(exhibitionRepository, Mockito.times(1)).save(exhibition);
    }

    @Test
    public void addNewExhibitionFailTest() {
        Exhibition exhibition = new Exhibition();
        exhibition.setExNameEng("test");
        exhibition.setExNameNative("тест");
        List<Exhibition> exhibitionList = new ArrayList<>();
        exhibitionList.add(exhibition);

        Mockito.doReturn(exhibitionList)
                .when(exhibitionRepository)
                .findByExNameEngOrExNameNative("test", "тест");
        boolean isExhibitionCreated = false;
        try {
            isExhibitionCreated = exhibitionService.addNewExhibition(exhibition);
        } catch (FailedToAddExhibitionException e) {
            e.printStackTrace();
        }

        Assert.assertFalse(isExhibitionCreated);
        Mockito.verify(exhibitionRepository, Mockito.times(1)).findByExNameEngOrExNameNative("test", "тест");
        Mockito.verify(exhibitionRepository, Mockito.times(0)).save(exhibition);
    }

    @Test
    void refreshHalls() {
        Exhibition exhibition = new Exhibition();
        exhibition.setId(1L);
        exhibition.setExNameEng("test");
        exhibition.setExNameNative("тест");

        boolean doesExhibitionHaveHallsOnStart = false;
        if (!exhibition.getHallScheduleList().isEmpty()) {
            doesExhibitionHaveHallsOnStart = true;
        }

        HallSchedule hallSchedule = new HallSchedule();
        hallSchedule.setId(1L);
        hallSchedule.setHallName(HallName.RED_HALL);
        hallSchedule.setExhibition(exhibition);
        List<HallSchedule> hallScheduleList = new ArrayList<>();
        hallScheduleList.add(hallSchedule);

        Mockito.doReturn(hallScheduleList)
                .when(hallScheduleRepository)
                .findAllByExhibitionId(1L);

        boolean methodReturnsExpectedValue = exhibitionService.refreshHalls(exhibition);

        boolean doesExhibitionHaveHallsOnEnd = false;
        if (!exhibition.getHallScheduleList().isEmpty()) {
            doesExhibitionHaveHallsOnEnd = true;
        }

        Assert.assertFalse(doesExhibitionHaveHallsOnStart);
        Assert.assertTrue(methodReturnsExpectedValue);
        Assert.assertTrue(doesExhibitionHaveHallsOnEnd);

        Mockito.verify(hallScheduleRepository, Mockito.times(1)).findAllByExhibitionId(1L);
        Mockito.verify(exhibitionRepository, Mockito.times(1)).save(exhibition);
    }

    @Test
    public void refreshHallsFailure() {
        Exhibition exhibition = new Exhibition();
        exhibition.setId(1L);
        exhibition.setExNameEng("test");
        exhibition.setExNameNative("тест");

        List<HallSchedule> hallScheduleList = new ArrayList<>();

        boolean doesExhibitionHaveHallsOnStart = false;
        if (!exhibition.getHallScheduleList().isEmpty()) {
            doesExhibitionHaveHallsOnStart = true;
        }

        Mockito.doReturn(hallScheduleList)
                .when(hallScheduleRepository)
                .findAllByExhibitionId(1L);

        boolean methodReturnsExpectedValue = exhibitionService.refreshHalls(exhibition);

        boolean doesExhibitionHaveHallsOnEnd = false;
        if (!exhibition.getHallScheduleList().isEmpty()) {
            doesExhibitionHaveHallsOnEnd = true;
        }

        Assert.assertFalse(doesExhibitionHaveHallsOnStart);
        Assert.assertFalse(methodReturnsExpectedValue);
        Assert.assertFalse(doesExhibitionHaveHallsOnEnd);

        Mockito.verify(hallScheduleRepository, Mockito.times(1)).findAllByExhibitionId(1L);
        Mockito.verify(exhibitionRepository, Mockito.times(0)).save(exhibition);
    }

}