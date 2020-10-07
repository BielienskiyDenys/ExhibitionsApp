package org.example.exhibitionsapp.service;

import org.example.exhibitionsapp.entity.Exhibition;
import org.example.exhibitionsapp.entity.ExhibitionStatus;
import org.example.exhibitionsapp.entity.HallSchedule;
import org.example.exhibitionsapp.repository.ExhibitionRepository;
import org.example.exhibitionsapp.repository.HallScheduleRepository;
import org.example.exhibitionsapp.repository.TicketRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.example.exhibitionsapp.exceptions.FailedToAddExhibitionException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ExhibitionService {
    Logger logger = LoggerFactory.getLogger(ExhibitionService.class);
    @Autowired
    private ExhibitionRepository exhibitionRepository;
    @Autowired
    private HallScheduleRepository hallScheduleRepository;
    @Autowired
    private TicketService ticketService;
    @Autowired
    private TicketRepository ticketRepository;

    public Optional<Exhibition> findById(Long id){
        return exhibitionRepository.findById(id);
    }

    @Transactional
    public boolean addNewExhibition(Exhibition exhibition) throws FailedToAddExhibitionException {
        boolean result;
        if(!exhibitionRepository.findByExNameEngOrExNameNative(exhibition.getExNameEng(), exhibition.getExNameNative()).isEmpty()) {
            return false;
        }
        try {
            exhibitionRepository.save(exhibition);
            result = true;
        } catch (Exception e) {
            logger.error("Failure during adding new exhibition" + exhibition, e);
            throw new FailedToAddExhibitionException(e.getMessage(), e);
        }
        return result;
    }
    @Transactional
    public boolean refreshHalls(Exhibition exhibition) {
        List<HallSchedule>  hallScheduleList = hallScheduleRepository.findAllByExhibitionId(exhibition.getId());
        if(hallScheduleList.isEmpty()) {
            return false;
        }
        exhibition.setHallScheduleList(hallScheduleList);
        exhibitionRepository.save(exhibition);
        return true;
    }

    @Transactional
    public boolean setExhibitonStatusAsCanceledById(Long id) {
        Optional<Exhibition> exhibition = exhibitionRepository.findById(id);
        if(exhibition.isEmpty()) {
            return false;
        }
        Exhibition e = exhibition.get();
        e.setExhibitionStatus(ExhibitionStatus.STATUS_CANCELED);
        exhibitionRepository.save(e);
        ticketService.cancellGroupOfTickets(ticketRepository.findAllByExhibition_Id(e.getId()));
        return true;
    }

    public Page<Exhibition> sortByName(String name, Pageable pageable) {
        return exhibitionRepository.findAllByExNameEngContainingOrExNameNativeContaining(name, name, pageable);
    }

    public Page<Exhibition> sortByTheme(String theme, Pageable pageable) {
        return exhibitionRepository.findAllByThemesEngContainingOrThemesNativeContaining(theme, theme, pageable);
    }

    public Page<Exhibition> sortByPrice(Long min, Long max, Pageable pageable) {
        return exhibitionRepository.findAllByTicketPriceIsBetween(min, max, pageable);
    }


    public Page<Exhibition> findCancelled(Pageable pageable) {
        return exhibitionRepository.findAllByExhibitionStatus(ExhibitionStatus.STATUS_CANCELED, pageable);
    }
}
