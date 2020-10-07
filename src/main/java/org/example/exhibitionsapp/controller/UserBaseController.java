package org.example.exhibitionsapp.controller;

import org.example.exhibitionsapp.entity.*;
import org.example.exhibitionsapp.service.ExhibitionService;
import org.example.exhibitionsapp.service.HallService;
import org.example.exhibitionsapp.service.TicketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class UserBaseController {
    Logger logger = LoggerFactory.getLogger(UserBaseController.class);
    @Autowired
    private ExhibitionService exhibitionService;
    @Autowired
    private HallService hallService;
    @Autowired
    private TicketService ticketService;

//    @GetMapping("/user_base")
//    public String userPageGet(Map<String, Object> model) {
//        return ControllerUtil.urlAppendLocale("user_base");
//    }
//
//    @PostMapping("/user_base")
//    public String userPagePost(Map<String, Object> model) {
//        return ControllerUtil.urlAppendLocale("user_base");
//    }


    @GetMapping("/filter_ex_by_name")
    public String filterByName(@RequestParam String filterByName, Map<String, Object> model, @PageableDefault(sort = {"ticketPrice"}, direction = Sort.Direction.ASC) Pageable pageable) {
        Page<Exhibition> exhibitions = exhibitionService.sortByName(filterByName, pageable);
        model.put("url", "/filter_ex_by_name?filterByName="+filterByName );
        model.put("exhibitions", exhibitions);
        if (exhibitions.isEmpty()) {
            model.put("searchResult", "No exhibitions found.");
        }
        return ControllerUtil.urlAppendLocale("user_base");
    }

    @GetMapping("/filter_ex_by_theme")
    public String filterByTheme(@RequestParam String filterByTheme, Map<String, Object> model, @PageableDefault(sort = {"ticketPrice"}, direction = Sort.Direction.ASC) Pageable pageable) {
        Page<Exhibition> exhibitions = exhibitionService.sortByTheme(filterByTheme, pageable);
        model.put("url", "/filter_ex_by_theme?filterByTheme="+filterByTheme );
        model.put("exhibitions", exhibitions);
        if (exhibitions.isEmpty()) {
            model.put("searchResult", "No exhibitions found.");
        }
        return ControllerUtil.urlAppendLocale("user_base");
    }

    @GetMapping("/filter_ex_by_price")
    public String filterByPrice(@RequestParam Long filterByPriceFrom, @RequestParam Long filterByPriceUpTo, Map<String, Object> model, @PageableDefault(sort = {"ticketPrice"}, direction = Sort.Direction.ASC) Pageable pageable) {
        if (filterByPriceFrom < 0 || filterByPriceUpTo < 0) {
            model.put("searchResult", "Price must be positive or 0.");
            return ControllerUtil.urlAppendLocale("user_base");
        }
        if (filterByPriceFrom > filterByPriceUpTo) {
            Long temp = filterByPriceFrom;
            filterByPriceFrom = filterByPriceUpTo;
            filterByPriceUpTo = temp;
        }
        Page<Exhibition> exhibitions = exhibitionService.sortByPrice(filterByPriceFrom, filterByPriceUpTo, pageable);
        model.put("url", "/filter_ex_by_price?filterByPriceFrom="+filterByPriceFrom+"filterByPriceUpTo="+filterByPriceUpTo );
        model.put("exhibitions", exhibitions);
        if (exhibitions.isEmpty()) {
            model.put("searchResult", "No exhibitions found.");
        }
        return ControllerUtil.urlAppendLocale("user_base");

    }

    @GetMapping("/filter_ex_by_date")
    public String filterByDate(@RequestParam String filterByDateStart, @RequestParam String filterByDateEnd, Map<String, Object> model, @PageableDefault(sort = {"ticketPrice"}, direction = Sort.Direction.ASC) Pageable pageable) {
        List<HallSchedule> hallScheduleList = hallService.sortByDate(filterByDateStart, filterByDateEnd, pageable);
        model.put("halls_with_exhibitions", hallScheduleList);
        if (hallScheduleList.isEmpty()) {
            model.put("searchResult", "No exhibitions found.");
        }
        return ControllerUtil.urlAppendLocale("user_base");
    }

    @GetMapping("/filter_ex_by_status")
    public String filterByStatus(@RequestParam String filterByStatus, Map<String, Object> model, @PageableDefault(sort = {"ticketPrice"}, direction = Sort.Direction.ASC) Pageable pageable) {
        List<HallSchedule> hallScheduleList;
        switch (filterByStatus) {
            case ("CANCELED"):
                Page<Exhibition> exhibitions = exhibitionService.findCancelled(pageable);
                model.put("url", "/filter_ex_by_status?filterByStatus="+filterByStatus);
                model.put("exhibitions", exhibitions);
                if (exhibitions.isEmpty()) {
                    model.put("searchResult", "No exhibitions found.");
                }
                return ControllerUtil.urlAppendLocale("user_base");
            case ("ACTIVE"):
                hallScheduleList = hallService.findActiveEvents();
                model.put("halls_with_exhibitions", hallScheduleList);
                if (hallScheduleList.isEmpty()) {
                    model.put("searchResult", "No exhibitions found.");
                }
                return ControllerUtil.urlAppendLocale("user_base");
            case ("ENDED"):
                hallScheduleList = hallService.findEndedEvents();
                model.put("halls_with_exhibitions", hallScheduleList);
                if (hallScheduleList.isEmpty()) {
                    model.put("searchResult", "No exhibitions found.");
                }
                return ControllerUtil.urlAppendLocale("user_base");
        }
        return ControllerUtil.urlAppendLocale("user_base");
    }

    @PostMapping("/buy_ticket")
    public String buyTicket(Map<String, Object> model, @RequestParam int ticketQuantity, @RequestParam Long exhibitionId) {

        if (ticketQuantity < 1) {
            model.put("searchResult", "Quantity must be positive.");
            logger.debug("Attempt to purchase <1 tickets");
            return ControllerUtil.urlAppendLocale("user_base");
        }
        Optional<Exhibition> exhibitionForTicket = exhibitionService.findById(exhibitionId);
        if (exhibitionForTicket.isEmpty()) {
            model.put("searchResult", "Error occured. Try later or purchase via phone.");
            logger.error("Can't find exhibition while purchasing ticket.");
            return ControllerUtil.urlAppendLocale("user_base");
        }
        List<HallSchedule> hallScheduleList = exhibitionForTicket.get().getHallScheduleList();
        boolean exhibitionEndedInAllHalls = true;
        for (HallSchedule hs : hallScheduleList) {
            if (hs.getEndDate().compareTo(Calendar.getInstance()) >= 0) {
                exhibitionEndedInAllHalls = false;
            }
        }
        if (exhibitionEndedInAllHalls) {
            model.put("searchResult", "This exhibition has ended.");
            logger.debug("Attempt to buy ticket to ended exhibition.");
            return ControllerUtil.urlAppendLocale("user_base");
        }

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal == null) {
            model.put("searchResult", "Error occured. Try later or purchase via phone.");
            logger.error("Attempt to buy ticket with unauthorized user.");
            return ControllerUtil.urlAppendLocale("user_base");
        }
        User user = (User) principal;
        Ticket ticket = new Ticket(exhibitionForTicket.get(), user, ticketQuantity);
        ticketService.addNewTicket(ticket);
        model.put("searchResult", "Ticket bought successfully.");
        logger.info("Ticket bought: " + ticket);
        return ControllerUtil.urlAppendLocale("user_base");
    }

    @GetMapping("/find_my_tickets")
    public String findMyTickets(Map<String, Object> model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal == null) {
            model.put("search_ticket_message", "Error occured. Try logging again and repeat operation.");
            logger.error("Trying to search tickets with unauthorized user.");
            return ControllerUtil.urlAppendLocale("user_base");
        }
        User user = (User) principal;
        List<Ticket> ticketList = ticketService.findTicketsByUserId(user.getId());
        model.put("ticketList", ticketList);
        return ControllerUtil.urlAppendLocale("user_base");
    }

}
