package org.example.exhibitionsapp.entity;

import lombok.ToString;

import javax.persistence.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

@ToString

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"ex_name_eng", "ex_name_native"})})
public class Exhibition {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "ex_name_eng", nullable = false)
    private String exNameEng;
    @Column(name = "ex_name_native", nullable = false)
    private String exNameNative;
    @OneToMany
    @JoinTable
    private List<HallSchedule> hallScheduleList = Collections.EMPTY_LIST;
    private String openTime;
    private String closeTime;
    private String descriptionEng;
    private String descriptionNative;
    private String themesEng;
    private String themesNative;
    private Long ticketPrice;
    @Enumerated(EnumType.STRING)
    private ExhibitionStatus exhibitionStatus;

    public Exhibition() {
    }

    public Exhibition(String exNameEng, String exNameNative, String openTime, String closeTime, String descriptionEng, String descriptionNative, String themesEng, String themesNative, Long ticketPrice) {
        this.exNameEng = exNameEng;
        this.exNameNative = exNameNative;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.descriptionEng = descriptionEng;
        this.descriptionNative = descriptionNative;
        this.themesEng = themesEng;
        this.themesNative = themesNative;
        this.ticketPrice = ticketPrice;
        this.exhibitionStatus = ExhibitionStatus.STATUS_ACTIVE;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExNameEng() {
        return exNameEng;
    }

    public void setExNameEng(String exNameEng) {
        this.exNameEng = exNameEng;
    }

    public String getExNameNative() {
        return exNameNative;
    }

    public void setExNameNative(String exNameNative) {
        this.exNameNative = exNameNative;
    }

    public List<HallSchedule> getHallScheduleList() {
        return hallScheduleList;
    }

    public void setHallScheduleList(List<HallSchedule> hallScheduleList) {
        this.hallScheduleList = hallScheduleList;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    public String getDescriptionEng() {
        return descriptionEng;
    }

    public void setDescriptionEng(String descriptionEng) {
        this.descriptionEng = descriptionEng;
    }

    public String getDescriptionNative() {
        return descriptionNative;
    }

    public void setDescriptionNative(String descriptionNative) {
        this.descriptionNative = descriptionNative;
    }

    public String getThemesEng() {
        return themesEng;
    }

    public void setThemesEng(String themesEng) {
        this.themesEng = themesEng;
    }

    public String getThemesNative() {
        return themesNative;
    }

    public void setThemesNative(String themesNative) {
        this.themesNative = themesNative;
    }

    public Long getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(Long ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public ExhibitionStatus getExhibitionStatus() {
        return exhibitionStatus;
    }

    public void setExhibitionStatus(ExhibitionStatus exhibitionStatus) {
        this.exhibitionStatus = exhibitionStatus;
    }

    @Override
    public String toString() {
        return "Exhibition{" +
                "id=" + id +
                ", exNameEng='" + exNameEng + '\'' +
                ", exNameNative='" + exNameNative + '\'' +
                ", openTime='" + openTime + '\'' +
                ", closeTime='" + closeTime + '\'' +
                ", descriptionEng='" + descriptionEng + '\'' +
                ", descriptionNative='" + descriptionNative + '\'' +
                ", themesEng='" + themesEng + '\'' +
                ", themesNative='" + themesNative + '\'' +
                ", ticketPrice=" + ticketPrice +
                ", exhibitionStatus=" + exhibitionStatus +
                '}';
    }
}