package org.example.exhibitionsapp.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.exhibitionsapp.entity.*;
import org.example.exhibitionsapp.exceptions.FailedToAddExhibitionException;
import org.example.exhibitionsapp.service.ExhibitionService;
import org.example.exhibitionsapp.service.HallService;
import org.example.exhibitionsapp.service.TicketService;
import org.example.exhibitionsapp.service.UserService;

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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Controller
public class AdminBaseController {
    Logger logger = LoggerFactory.getLogger(AdminBaseController.class);
    @Autowired
    private ExhibitionService exhibitionService;
    @Autowired
    private HallService hallService;
    @Autowired
    private TicketService ticketService;
    @Autowired
    private UserService userService;

    @GetMapping("/admin_base")
    public String adminPageGet(Map<String, Object> model) {
        return "admin_base";
    }

    @PostMapping("/admin_base")
    public String adminPagePost(Map<String, Object> model) {
        return "admin_base";
    }

    @PostMapping("/add_exhibition")
    public String addExhibition(
            Map<String, Object> model,
            @RequestParam String exNameEng,
            @RequestParam String exNameNative,
            @RequestParam String openTime,
            @RequestParam String closeTime,
            @RequestParam String descriptionEng,
            @RequestParam String descriptionNative,
            @RequestParam String themesEng,
            @RequestParam String themesNative,
            @RequestParam Long ticketPrice
    ) {
        if (ticketPrice < 0) {
            model.put("errormessage", "Price has to be positive or 0");
            logger.debug("Attempt to enter exhibition.price < 0");
            return "admin_base";

        }
        Exhibition exhibition = new Exhibition(exNameEng, exNameNative, openTime, closeTime, descriptionEng, descriptionNative, themesEng, themesNative, ticketPrice);
        try {
            exhibitionService.addNewExhibition(exhibition);
        } catch (FailedToAddExhibitionException ex) {
            model.put("errormessage", "Failed to add an exhibition! Check if data is correct!");
            logger.error("Error during adding new exhibition: " + exhibition, ex);
        }
        model.put("exhibition_added", exhibition);
        logger.info("Exhibition added: " + exhibition );
        return "admin_base";
    }

    @PostMapping("/cancel_exhibition")
    public String cancelExhibition(Map<String, Object> model, @RequestParam Long exhibitionId) {
        if (exhibitionService.setExhibitonStatusAsCanceledById(exhibitionId)) {
            model.put("cancel_exhibition_message", "Exhibition cancelled, tickets set as waiting to refund.");
            logger.info("Canceled exhibition by ID: "+ exhibitionId);
            return "admin_base";
        } else {
            model.put("cancel_exhibition_message", "Failed to cancel exhibition");
            logger.error("Error during cancelling exhibition on ID: " + exhibitionId);
            return "admin_base";
        }
    }

    @PostMapping("/add_exhibition_to_hall")
    public String assignExhibitionToHall(
            Map<String, Object> model,
            @RequestParam int hallName,
            @RequestParam Long exhibitionId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        Optional<Exhibition> exhibition = exhibitionService.findById(exhibitionId);

        if (exhibition.isEmpty()) {
            model.put("hall_error", "Exhibition not found.");
            return "admin_base";
        }
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            c1.setTime(sdf.parse(startDate));
            c2.setTime(sdf.parse(endDate));
        } catch (ParseException e) {
            logger.error("Error during parsing dates (/add_exhibition_to_hall).", e);
            e.printStackTrace();
        }
        if (c1.compareTo(c2) > 0) {
            model.put("hall_error", "End date has to be equal or greater than Start date of the event.");
            logger.debug("Attempt to set startDate>endDate while /add_new_exhibition_to_hall.");
            return "admin_base";
        }
        if (c1.compareTo(Calendar.getInstance()) < 0) {
            model.put("hall_error", "Exhibition can't be assigned to past dates.");
            logger.debug("Attempt to set startDate in past /add_new_exhibition_to_hall.");
            return "admin_base";
        }
        HallSchedule hallSchedule = new HallSchedule(HallName.values()[hallName], exhibition.get(), c1, c2);
        if (hallService.addNewHallSchedule(hallSchedule) == true) {
            exhibitionService.refreshHalls(exhibition.get());
            model.put("hall_added", "Exhibition successfully assigned to the hall!");
            logger.info("Exhibition: " + exhibition + "assigned to new hallSchedule" + hallSchedule);
        } else {
            model.put("hall_error", "Hall is taken for theese dates. Please check schedule.");
            logger.error("Failed to assign exhibition: " + exhibition + "for: " + hallSchedule);
        }

        return "admin_base";
    }

    @GetMapping("/filter_ex_by_name_admin")
    public String filterByName(@RequestParam String filterByName, Map<String, Object> model, @PageableDefault(sort = {"exNameEng"}, direction = Sort.Direction.ASC) Pageable pageable) {
        Page<Exhibition> exhibitions = exhibitionService.sortByName(filterByName, pageable);
        model.put("exhibitions", exhibitions);
        model.put("url", "/filter_ex_by_name_admin?filterByName="+filterByName );
        if (exhibitions.isEmpty()) {
            model.put("searchResult", "No exhibitions found.");
        }
        return "admin_base";
    }

    @GetMapping("/filter_ex_by_theme_admin")
    public String filterByTheme(@RequestParam String filterByTheme, Map<String, Object> model, @PageableDefault(sort = {"themesEng"}, direction = Sort.Direction.ASC) Pageable pageable) {
        Page<Exhibition> exhibitions = exhibitionService.sortByTheme(filterByTheme, pageable);
        model.put("exhibitions", exhibitions);
        model.put("url", "/filter_ex_by_theme_admin?filterByTheme="+filterByTheme );
        if (exhibitions.isEmpty()) {
            model.put("searchResult", "No exhibitions found.");
        }
        return "admin_base";
    }

    @GetMapping("/filter_ex_by_price_admin")
    public String filterByPrice(@RequestParam Long filterByPriceFrom, @RequestParam Long filterByPriceUpTo, Map<String, Object> model, @PageableDefault(sort = {"ticketPrice"}, direction = Sort.Direction.ASC) Pageable pageable) {
        if (filterByPriceFrom < 0 || filterByPriceUpTo < 0) {
            model.put("searchResult", "Price must be positive or 0.");
            return "admin_base";
        }
        if (filterByPriceFrom > filterByPriceUpTo) {
            Long temp = filterByPriceFrom;
            filterByPriceFrom = filterByPriceUpTo;
            filterByPriceUpTo = temp;
        }
        Page<Exhibition> exhibitions = exhibitionService.sortByPrice(filterByPriceFrom, filterByPriceUpTo, pageable);
        model.put("url", "/filter_ex_by_price_admin?filterByPriceFrom="+filterByPriceFrom+"filterByPriceUpTo="+filterByPriceUpTo );
        model.put("exhibitions", exhibitions);
        if (exhibitions.isEmpty()) {
            model.put("searchResult", "No exhibitions found.");
        }
        return "admin_base";

    }

    @GetMapping("/filter_ex_by_date_admin")
    public String filterByDate(@RequestParam String filterByDateStart, @RequestParam String filterByDateEnd, Map<String, Object> model, @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) {
        List<HallSchedule> hallScheduleList = hallService.sortByDate(filterByDateStart, filterByDateEnd, pageable);
        model.put("halls_with_exhibitions", hallScheduleList);
        if (hallScheduleList.isEmpty()) {
            model.put("searchResult", "No exhibitions found.");
        }
        return "admin_base";
    }

    @GetMapping("/filter_ex_by_status_admin")
    public String filterByStatus(@RequestParam String filterByStatus, Map<String, Object> model, @PageableDefault(sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        List<HallSchedule> hallScheduleList;
        switch (filterByStatus) {
            case ("CANCELED"):
                Page<Exhibition> exhibitions = exhibitionService.findCancelled(pageable);
                model.put("url", "/filter_ex_by_status_admin?filterByStatus="+filterByStatus);
                model.put("exhibitions", exhibitions);
                if (exhibitions.isEmpty()) {
                    model.put("searchResult", "No exhibitions found.");
                }
                return "admin_base";
            case ("ACTIVE"):
                hallScheduleList = hallService.findActiveEvents();
                model.put("url", "/filter_ex_by_status_admin?filterByStatus="+filterByStatus);
                model.put("halls_with_exhibitions", hallScheduleList);
                if (hallScheduleList.isEmpty()) {
                    model.put("searchResult", "No exhibitions found.");
                }
                return "admin_base";
            case ("ENDED"):
                hallScheduleList = hallService.findEndedEvents();
                model.put("url", "/filter_ex_by_status_admin?filterByStatus="+filterByStatus);
                model.put("halls_with_exhibitions", hallScheduleList);
                if (hallScheduleList.isEmpty()) {
                    model.put("searchResult", "No exhibitions found.");
                }
                return "admin_base";
        }
        return "admin_base";
    }

    @PostMapping("/buy_ticket_admin")
    public String buyTicket(Map<String, Object> model, @RequestParam int ticketQuantity, @RequestParam Long exhibitionId) {
        if(ticketQuantity<1) {
            model.put("searchResult", "Quantity must be positive.");
            logger.debug("Attempt to purchase <1 tickets");
            return "admin_base";
        }
        Optional<Exhibition> exhibitionForTicket = exhibitionService.findById(exhibitionId);
        if(exhibitionForTicket.isEmpty()) {
            model.put("searchResult", "Error occured. Try later or purchase via phone.");
            logger.error("Can't find exhibition while purchasing ticket.");
            return "admin_base";
        }

        List<HallSchedule> hallScheduleList = exhibitionForTicket.get().getHallScheduleList();
        boolean exhibitionEndedInAllHalls = true;
        for (HallSchedule hs: hallScheduleList) {
            if (hs.getEndDate().compareTo(Calendar.getInstance())>=0) {
                exhibitionEndedInAllHalls = false;
            }
        }
        if(exhibitionEndedInAllHalls){
            model.put("searchResult", "This exhibition has ended.");
            logger.debug("Attempt to buy ticket to ended exhibition.");
            return "admin_base";
        }

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal == null) {
            model.put("searchResult", "Error occured. Try later or purchase via phone.");
            logger.error("Attempt to buy ticket with unauthorized user.");
            return "admin_base";
        }
        User user = (User)principal;
        Ticket ticket = new Ticket(exhibitionForTicket.get(), user, ticketQuantity);
        ticketService.addNewTicket(ticket);
        model.put("searchResult", "Ticket bought successfully.");
        logger.info("Ticket bought: " + ticket);
        return "admin_base";
    }

    @GetMapping("/find_my_tickets_admin")
    public String findMyTickets(Map<String, Object> model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal == null) {
            model.put("search_ticket_message", "Error occured. Try logging again and repeat operation.");
            logger.error("Trying to search tickets with unauthorized user.");
            return "admin_base";
        }
        User user = (User)principal;
        List<Ticket> ticketList = ticketService.findTicketsByUserId(user.getId());
        model.put("ticketList",ticketList);
        return "admin_base";
    }

    @GetMapping("/search_tickets_by_user_id_admin")
    public String findTicketsByUserId(Map<String, Object> model, @RequestParam Long userId) {
        List<Ticket> ticketList = ticketService.findTicketsByUserId(userId);
        if(ticketList.isEmpty()) {
            model.put("search_ticket_message", "No tickets for this user.");
            return "admin_base";
        }
        model.put("ticketList", ticketList);
        return "admin_base";
    }

    @GetMapping("/search_tickets_by_exhibition_id_admin")
    public String findTicketsByExhibitionId(Map<String, Object> model, @RequestParam Long exhibitionId) {
        List<Ticket> ticketList = ticketService.findTicketsByExhibitionId(exhibitionId);
        if(ticketList.isEmpty()) {
            model.put("search_ticket_message", "No tickets for this exhibition.");
            return "admin_base";
        }
        model.put("ticketList", ticketList);
        return "admin_base";
    }

    @GetMapping("/search_tickets_by_status_admin")
    public String findTicketsByExhibitionId(Map<String, Object> model, @RequestParam String ticketStatus) {
        List<Ticket> ticketList = ticketService.findTicketsByStatus(ticketStatus);
        if(ticketList.isEmpty()) {
            model.put("search_ticket_message", "No tickets with such status.");
            return "admin_base";
        }
        model.put("ticketList", ticketList);
        return "admin_base";
    }

    @GetMapping("/find_all_users_admin")
    public String findAllUsers(Map<String, Object> model) {
        List<User> userList = userService.findAllUsers();
        if(userList.isEmpty()) {
            model.put("userSearchMessage", "No users yet.");
            return "admin_base";
        }
        model.put("userList", userList);
        return "admin_base";
    }

    @GetMapping("/find_user_by_email_admin")
    public String findUserByEmail(Map<String, Object> model, @RequestParam String userEmail) {
        Optional<User> userOptional= userService.findUserByEmail(userEmail);
        if(userOptional.isEmpty()) {
            model.put("userSearchMessage", "No such user.");
            return "admin_base";
        }
        model.put("url", "/find_user_by_email_admin?userEmail="+userEmail);
        model.put("userList", userOptional.get());
        return "admin_base";
    }


    @PostMapping("/cancel_ticket")
    public String cancelTicket(Map<String, Object> model, @RequestParam Long ticketId) {
        if (ticketService.setTicketInactive(ticketId)) {
            model.put("cancel_ticket_message", "Ticket inactivated.");
            logger.info("Inactivated ticket: " + ticketId);
            return "admin_base";
        }
        model.put("cancel_ticket_message", "Error. Invalid ticket.");
        logger.debug("Attemp to inactivate invalid ticket" + ticketId);
        return "admin_base";

    }

}
