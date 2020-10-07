package org.example.exhibitionsapp.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.exhibitionsapp.entity.*;
import org.example.exhibitionsapp.exceptions.FailedToAddExhibitionException;
import org.example.exhibitionsapp.service.ExhibitionService;
import org.example.exhibitionsapp.service.HallService;
import org.example.exhibitionsapp.service.TicketService;
import org.example.exhibitionsapp.service.UserService;

import org.hibernate.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
            ControllerUtil.addValueToModelDependsOnLocale(model, "errormessage",
                    "Price has to be positive or 0.",
                    "Вартість має бути більше, або дорінювати нулю.");
            logger.debug("Attempt to enter exhibition.price < 0");
            return ControllerUtil.urlAppendLocale("admin_base");

        }
        Exhibition exhibition = new Exhibition(exNameEng, exNameNative, openTime, closeTime, descriptionEng, descriptionNative, themesEng, themesNative, ticketPrice);
        try {
            exhibitionService.addNewExhibition(exhibition);
        } catch (FailedToAddExhibitionException ex) {
            ControllerUtil.addValueToModelDependsOnLocale(model, "errormessage",
                    "Failed to add an exhibition! Check if data is correct!",
                    "Не вдалося створити виставку. Перевірте чи данні корректні!");
            logger.error("Error during adding new exhibition: " + exhibition, ex);
        }
        model.put("exhibition_added", exhibition);
        logger.info("Exhibition added: " + exhibition );
        return ControllerUtil.urlAppendLocale("admin_base");
    }

    @PostMapping("/cancel_exhibition")
    public String cancelExhibition(Map<String, Object> model, @RequestParam Long exhibitionId) {
        if (exhibitionService.setExhibitonStatusAsCanceledById(exhibitionId)) {
            ControllerUtil.addValueToModelDependsOnLocale(model, "cancel_exhibition_message",
                    "Exhibition cancelled, tickets set as waiting to refund.",
                    "Виставку скасовано. Всі білети промарковані як ті, що чекають на відшкодження грошей (STATUS WAITING_REFUND).");
            logger.info("Canceled exhibition by ID: "+ exhibitionId);
            return ControllerUtil.urlAppendLocale("admin_base");
        } else {
            ControllerUtil.addValueToModelDependsOnLocale(model, "cancel_exhibition_message",
                    "Failed to cancel exhibition",
                    "Не вдалося відмінити виставку.");
            logger.error("Error during cancelling exhibition on ID: " + exhibitionId);
            return ControllerUtil.urlAppendLocale("admin_base");
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
            ControllerUtil.addValueToModelDependsOnLocale(model, "hall_error",
                    "Exhibition not found.",
                    "Не вдалося знайти виставку.");
            return ControllerUtil.urlAppendLocale("admin_base");
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
            ControllerUtil.addValueToModelDependsOnLocale(model, "hall_error",
                    "End date has to be equal or greater than Start date of the event.",
                    "Дата закінчення має йти після дати початку.");
            logger.debug("Attempt to set startDate>endDate while /add_new_exhibition_to_hall.");
            return ControllerUtil.urlAppendLocale("admin_base");
        }
        if (c1.compareTo(Calendar.getInstance()) < 0) {
            ControllerUtil.addValueToModelDependsOnLocale(model, "hall_error",
                    "Exhibition can't be assigned to past dates.",
                    "Не можна призначати виставку у минулому.");
            logger.debug("Attempt to set startDate in past /add_new_exhibition_to_hall.");
            return ControllerUtil.urlAppendLocale("admin_base");
        }
        HallSchedule hallSchedule = new HallSchedule(HallName.values()[hallName], exhibition.get(), c1, c2);
        if (hallService.addNewHallSchedule(hallSchedule) == true) {
            exhibitionService.refreshHalls(exhibition.get());
            ControllerUtil.addValueToModelDependsOnLocale(model, "hall_added",
                    "Exhibition successfully assigned to the hall!",
                    "Виставку успішно призначено!");
            logger.info("Exhibition: " + exhibition + "assigned to new hallSchedule" + hallSchedule);
        } else {
            ControllerUtil.addValueToModelDependsOnLocale(model, "hall_error",
                    "Hall is taken for theese dates. Please check schedule.",
                    "Цей зал зайнятий на зазначені дати. Будь ласка, перевірте росклад.");
            logger.error("Failed to assign exhibition: " + exhibition + "for: " + hallSchedule);
        }

        return ControllerUtil.urlAppendLocale("admin_base");
    }

    @GetMapping("/filter_ex_by_name_admin")
    public String filterByName(@RequestParam String filterByName, Map<String, Object> model, @PageableDefault(sort = {"exNameEng"}, direction = Sort.Direction.ASC) Pageable pageable) {
        Page<Exhibition> exhibitions = exhibitionService.sortByName(filterByName, pageable);
        model.put("exhibitions", exhibitions);
        model.put("url", "/filter_ex_by_name_admin?filterByName="+filterByName );
        if (exhibitions.isEmpty()) {
            ControllerUtil.addValueToModelDependsOnLocale(model, "searchResult",
                    "No exhibitions found.",
                    "Жодної виставки не знайдено.");
        }
        return ControllerUtil.urlAppendLocale("admin_base");
    }

    @GetMapping("/filter_ex_by_theme_admin")
    public String filterByTheme(@RequestParam String filterByTheme, Map<String, Object> model, @PageableDefault(sort = {"themesEng"}, direction = Sort.Direction.ASC) Pageable pageable) {
        Page<Exhibition> exhibitions = exhibitionService.sortByTheme(filterByTheme, pageable);
        model.put("exhibitions", exhibitions);
        model.put("url", "/filter_ex_by_theme_admin?filterByTheme="+filterByTheme );
        if (exhibitions.isEmpty()) {
            ControllerUtil.addValueToModelDependsOnLocale(model, "searchResult",
                    "No exhibitions found.",
                    "Жодної виставки не знайдено.");
        }
        return ControllerUtil.urlAppendLocale("admin_base");
    }

    @GetMapping("/filter_ex_by_price_admin")
    public String filterByPrice(@RequestParam Long filterByPriceFrom, @RequestParam Long filterByPriceUpTo, Map<String, Object> model, @PageableDefault(sort = {"ticketPrice"}, direction = Sort.Direction.ASC) Pageable pageable) {
        if (filterByPriceFrom < 0 || filterByPriceUpTo < 0) {
            ControllerUtil.addValueToModelDependsOnLocale(model, "searchResult",
                    "Price must be positive or 0.",
                    "Вартість має бути більшою, чи дорівнювати нулю.");
            return ControllerUtil.urlAppendLocale("admin_base");
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
            ControllerUtil.addValueToModelDependsOnLocale(model, "searchResult",
                    "No exhibitions found.",
                    "Жодної виставки не знайдено.");
        }
        return ControllerUtil.urlAppendLocale("admin_base");

    }

    @GetMapping("/filter_ex_by_date_admin")
    public String filterByDate(@RequestParam String filterByDateStart, @RequestParam String filterByDateEnd, Map<String, Object> model, @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) {
        List<HallSchedule> hallScheduleList = hallService.sortByDate(filterByDateStart, filterByDateEnd, pageable);
        model.put("halls_with_exhibitions", hallScheduleList);
        if (hallScheduleList.isEmpty()) {
            ControllerUtil.addValueToModelDependsOnLocale(model, "searchResult",
                    "No exhibitions found.",
                    "Жодної виставки не знайдено.");
        }
        return ControllerUtil.urlAppendLocale("admin_base");
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
                    ControllerUtil.addValueToModelDependsOnLocale(model, "searchResult",
                            "No exhibitions found.",
                            "Жодної виставки не знайдено.");
                }
                return ControllerUtil.urlAppendLocale("admin_base");
            case ("ACTIVE"):
                hallScheduleList = hallService.findActiveEvents();
                model.put("url", "/filter_ex_by_status_admin?filterByStatus="+filterByStatus);
                model.put("halls_with_exhibitions", hallScheduleList);
                if (hallScheduleList.isEmpty()) {
                    ControllerUtil.addValueToModelDependsOnLocale(model, "searchResult",
                            "No exhibitions found.",
                            "Жодної виставки не знайдено.");
                }
                return ControllerUtil.urlAppendLocale("admin_base");
            case ("ENDED"):
                hallScheduleList = hallService.findEndedEvents();
                model.put("url", "/filter_ex_by_status_admin?filterByStatus="+filterByStatus);
                model.put("halls_with_exhibitions", hallScheduleList);
                if (hallScheduleList.isEmpty()) {
                    ControllerUtil.addValueToModelDependsOnLocale(model, "searchResult",
                            "No exhibitions found.",
                            "Жодної виставки не знайдено.");
                }
                return ControllerUtil.urlAppendLocale("admin_base");
        }
        return "admin_base";
    }

    @PostMapping("/buy_ticket_admin")
    public String buyTicket(Map<String, Object> model, @RequestParam int ticketQuantity, @RequestParam Long exhibitionId) {
        if(ticketQuantity<1) {
            ControllerUtil.addValueToModelDependsOnLocale(model, "searchResult",
                    "Quantity must be positive.",
                    "Кількість не може бути меншою за одиницю.");
            logger.debug("Attempt to purchase <1 tickets");
            return ControllerUtil.urlAppendLocale("admin_base");
        }
        Optional<Exhibition> exhibitionForTicket = exhibitionService.findById(exhibitionId);
        if(exhibitionForTicket.isEmpty()) {
            ControllerUtil.addValueToModelDependsOnLocale(model, "searchResult",
                    "Error occured. Try later or purchase via phone.",
                    "Помилка. Спробуйте через де-який час, або замовте квіток за телефоном.");
            logger.error("Can't find exhibition while purchasing ticket.");
            return ControllerUtil.urlAppendLocale("admin_base");
        }

        List<HallSchedule> hallScheduleList = exhibitionForTicket.get().getHallScheduleList();
        boolean exhibitionEndedInAllHalls = true;
        for (HallSchedule hs: hallScheduleList) {
            if (hs.getEndDate().compareTo(Calendar.getInstance())>=0) {
                exhibitionEndedInAllHalls = false;
            }
        }
        if(exhibitionEndedInAllHalls){
            ControllerUtil.addValueToModelDependsOnLocale(model, "searchResult",
                    "This exhibition has ended.",
                    "Ця виставка вже закінчилася.");
            logger.debug("Attempt to buy ticket to ended exhibition.");
            return ControllerUtil.urlAppendLocale("admin_base");
        }

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal == null) {
            ControllerUtil.addValueToModelDependsOnLocale(model, "searchResult",
                    "Error occured. Try later or purchase via phone.",
                    "Помилка. Спробуйте через де-який час, або замовте квіток за телефоном.");
            logger.error("Attempt to buy ticket with unauthorized user.");
            return ControllerUtil.urlAppendLocale("admin_base");
        }
        User user = (User)principal;
        Ticket ticket = new Ticket(exhibitionForTicket.get(), user, ticketQuantity);
        ticketService.addNewTicket(ticket);
        ControllerUtil.addValueToModelDependsOnLocale(model, "searchResult",
                "Ticket bought successfully.",
                "Квіток придбано.");
        model.put("searchResult", "Ticket bought successfully.");
        logger.info("Ticket bought: " + ticket);
        return ControllerUtil.urlAppendLocale("admin_base");
    }

    @GetMapping("/find_my_tickets_admin")
    public String findMyTickets(Map<String, Object> model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal == null) {
            ControllerUtil.addValueToModelDependsOnLocale(model, "search_ticket_message",
                    "Error occured. Try logging again and repeat operation.",
                    "Помилка. Спробуйте перезайти на сторінку та купити квіток.");
            logger.error("Trying to search tickets with unauthorized user.");
            return ControllerUtil.urlAppendLocale("admin_base");
        }
        User user = (User)principal;
        List<Ticket> ticketList = ticketService.findTicketsByUserId(user.getId());
        model.put("ticketList",ticketList);
        return ControllerUtil.urlAppendLocale("admin_base");
    }

    @GetMapping("/search_tickets_by_user_id_admin")
    public String findTicketsByUserId(Map<String, Object> model, @RequestParam Long userId) {
        List<Ticket> ticketList = ticketService.findTicketsByUserId(userId);
        if(ticketList.isEmpty()) {
            ControllerUtil.addValueToModelDependsOnLocale(model, "search_ticket_message",
                    "No tickets for this user.",
                    "Цей користувач не придбав жодного квітка.");
            return ControllerUtil.urlAppendLocale("admin_base");
        }
        model.put("ticketList", ticketList);
        return ControllerUtil.urlAppendLocale("admin_base");
    }

    @GetMapping("/search_tickets_by_exhibition_id_admin")
    public String findTicketsByExhibitionId(Map<String, Object> model, @RequestParam Long exhibitionId) {
        List<Ticket> ticketList = ticketService.findTicketsByExhibitionId(exhibitionId);
        if(ticketList.isEmpty()) {
            ControllerUtil.addValueToModelDependsOnLocale(model, "search_ticket_message",
                    "No tickets for this exhibition.",
                    "На цю виставку не продано жодного квітка.");
            return ControllerUtil.urlAppendLocale("admin_base");
        }
        model.put("ticketList", ticketList);
        return ControllerUtil.urlAppendLocale("admin_base");
    }

    @GetMapping("/search_tickets_by_status_admin")
    public String findTicketsByExhibitionId(Map<String, Object> model, @RequestParam String ticketStatus) {
        List<Ticket> ticketList = ticketService.findTicketsByStatus(ticketStatus);
        if(ticketList.isEmpty()) {
            ControllerUtil.addValueToModelDependsOnLocale(model, "search_ticket_message",
                    "No tickets with such status.",
                    "Немає квітків з таким статусом.");
            return ControllerUtil.urlAppendLocale("admin_base");
        }
        model.put("ticketList", ticketList);
        return ControllerUtil.urlAppendLocale("admin_base");
    }

    @GetMapping("/find_all_users_admin")
    public String findAllUsers(Map<String, Object> model) {
        List<User> userList = userService.findAllUsers();
        if(userList.isEmpty()) {
            ControllerUtil.addValueToModelDependsOnLocale(model, "userSearchMessage",
                    "No users yet.",
                    "Користувачів ще не зареєстровано.");
            return ControllerUtil.urlAppendLocale("admin_base");
        }
        model.put("userList", userList);
        return ControllerUtil.urlAppendLocale("admin_base");
    }

    @GetMapping("/find_user_by_email_admin")
    public String findUserByEmail(Map<String, Object> model, @RequestParam String userEmail) {
        Optional<User> userOptional= userService.findUserByEmail(userEmail);
        if(userOptional.isEmpty()) {
            ControllerUtil.addValueToModelDependsOnLocale(model, "userSearchMessage",
                    "No such user.",
                    "Такого користувача не знайдено.");
            return ControllerUtil.urlAppendLocale("admin_base");
        }
        model.put("url", "/find_user_by_email_admin?userEmail="+userEmail);
        model.put("userList", userOptional.get());
        return ControllerUtil.urlAppendLocale("admin_base");
    }


    @PostMapping("/cancel_ticket")
    public String cancelTicket(Map<String, Object> model, @RequestParam Long ticketId) {
        if (ticketService.setTicketInactive(ticketId)) {
            ControllerUtil.addValueToModelDependsOnLocale(model, "cancel_ticket_message",
                    "Ticket inactivated.",
                    "Квіток марковано як недійсний");
            model.put("cancel_ticket_message", "Ticket inactivated.");
            logger.info("Inactivated ticket: " + ticketId);
            return ControllerUtil.urlAppendLocale("admin_base");
        }
        ControllerUtil.addValueToModelDependsOnLocale(model, "cancel_ticket_message",
                "Error. Invalid ticket.",
                "Помилка. Квіток не є валідним.");
        logger.debug("Attemp to inactivate invalid ticket" + ticketId);
        return ControllerUtil.urlAppendLocale("admin_base");

    }



}
