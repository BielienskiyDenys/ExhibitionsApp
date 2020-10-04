package org.example.exhibitionsapp.entity;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@Entity
public class HallSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Enumerated(EnumType.STRING)
    private HallName hallName;
    @ManyToOne(fetch = FetchType.LAZY)
    private Exhibition exhibition;
    private Calendar startDate;
    private Calendar endDate;
    private String startDateAsString;
    private String endDateAsString;

    public HallSchedule() {
    }

    public HallSchedule(HallName hallName, Exhibition exhibition, Calendar startDate, Calendar endDate) {
        this.hallName = hallName;
        this.exhibition = exhibition;
        this.startDate = startDate;
        this.endDate = endDate;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        this.startDateAsString = sdf.format(startDate.getTime());
        this.endDateAsString = sdf.format(endDate.getTime());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public HallName getHallName() {
        return hallName;
    }

    public void setHallName(HallName hallName) {
        this.hallName = hallName;
    }

    public Exhibition getExhibition() {
        return exhibition;
    }

    public void setExhibition(Exhibition exhibition) {
        this.exhibition = exhibition;
    }

    public Calendar getStartDate() {
        return startDate;
    }

    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }

    public Calendar getEndDate() {
        return endDate;
    }

    public void setEndDate(Calendar endDate) {
        this.endDate = endDate;
    }

    public String getStartDateAsString() {
        return startDateAsString;
    }

    public String getEndDateAsString() {
        return endDateAsString;
    }

    @Override
    public String toString() {
        return "HallSchedule{" +
                "id=" + id +
                ", hallName=" + hallName +
                ", exhibition=" + exhibition.getId() +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
