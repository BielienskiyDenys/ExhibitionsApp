package org.example.exhibitionsapp.controller;

import org.example.exhibitionsapp.entity.Exhibition;
import org.example.exhibitionsapp.entity.HallSchedule;
import org.example.exhibitionsapp.service.ExhibitionService;
import org.example.exhibitionsapp.service.HallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
public class MainPageController {
    @Autowired
    private ExhibitionService exhibitionService;
    @Autowired
    private HallService hallService;

    @GetMapping("/filter_ex_by_name_guest")
    public String filterByName(@RequestParam String filterByName, Map<String, Object> model, @PageableDefault(sort = {"ticketPrice"}, direction = Sort.Direction.ASC) Pageable pageable) {
        Page<Exhibition> exhibitions = exhibitionService.sortByName(filterByName, pageable);
        model.put("exhibitions", exhibitions);
        model.put("url", "/filter_ex_by_name_guest?filterByName="+filterByName );
        if (exhibitions.isEmpty()) {
            model.put("searchResult", "No exhibitions found.");
        }
        return "main";
    }

    @GetMapping("/filter_ex_by_theme_guest")
    public String filterByTheme(@RequestParam String filterByTheme, Map<String, Object> model, @PageableDefault(sort = {"ticketPrice"}, direction = Sort.Direction.ASC) Pageable pageable) {
        Page<Exhibition> exhibitions = exhibitionService.sortByTheme(filterByTheme, pageable);
        model.put("exhibitions", exhibitions);
        model.put("url", "/filter_ex_by_theme_guest?filterByTheme="+filterByTheme );
        if (exhibitions.isEmpty()) {
            model.put("searchResult", "No exhibitions found.");
        }
        return "main";
    }

    @GetMapping("/filter_ex_by_price_guest")
    public String filterByPrice(@RequestParam Long filterByPriceFrom, @RequestParam Long filterByPriceUpTo, Map<String, Object> model, @PageableDefault(sort = {"ticketPrice"}, direction = Sort.Direction.ASC) Pageable pageable) {
        if (filterByPriceFrom < 0 || filterByPriceUpTo < 0) {
            model.put("searchResult", "Price must be positive or 0.");
            return "main";
        }
        if (filterByPriceFrom > filterByPriceUpTo) {
            Long temp = filterByPriceFrom;
            filterByPriceFrom = filterByPriceUpTo;
            filterByPriceUpTo = temp;
        }
        Page<Exhibition> exhibitions = exhibitionService.sortByPrice(filterByPriceFrom, filterByPriceUpTo, pageable);
        model.put("url", "/filter_ex_by_price_guest?filterByPriceFrom="+filterByPriceFrom+"filterByPriceUpTo="+filterByPriceUpTo );
        model.put("exhibitions", exhibitions);
        if (exhibitions.isEmpty()) {
            model.put("searchResult", "No exhibitions found.");
        }
        return "main";

    }

    @GetMapping("/filter_ex_by_date_guest")
    public String filterByDate(@RequestParam String filterByDateStart, @RequestParam String filterByDateEnd, Map<String, Object> model, @PageableDefault(sort = {"ticketPrice"}, direction = Sort.Direction.ASC) Pageable pageable) {
        List<HallSchedule> hallScheduleList = hallService.sortByDate(filterByDateStart, filterByDateEnd, pageable);
        model.put("halls_with_exhibitions", hallScheduleList);
        if (hallScheduleList.isEmpty()) {
            model.put("searchResult", "No exhibitions found.");
        }
        return "main";
    }

    @GetMapping("/filter_ex_by_status_guest")
    public String filterByStatus(@RequestParam String filterByStatus, Map<String, Object> model, @PageableDefault(sort = {"ticketPrice"}, direction = Sort.Direction.ASC) Pageable pageable) {
        List<HallSchedule> hallScheduleList;
        switch (filterByStatus) {
            case ("CANCELED"):
                Page<Exhibition> exhibitions = exhibitionService.findCancelled(pageable);
                model.put("url", "/filter_ex_by_status_guest?filterByStatus="+filterByStatus);
                model.put("exhibitions", exhibitions);
                if (exhibitions.isEmpty()) {
                    model.put("searchResult", "No exhibitions found.");
                }
                return "main";
            case ("ACTIVE"):
                hallScheduleList = hallService.findActiveEvents();
                model.put("halls_with_exhibitions", hallScheduleList);
                if (hallScheduleList.isEmpty()) {
                    model.put("searchResult", "No exhibitions found.");
                }
                return "main";
            case ("ENDED"):
                hallScheduleList = hallService.findEndedEvents();
                model.put("halls_with_exhibitions", hallScheduleList);
                if (hallScheduleList.isEmpty()) {
                    model.put("searchResult", "No exhibitions found.");
                }
                return "main";
        }
        return "main";
    }
}
