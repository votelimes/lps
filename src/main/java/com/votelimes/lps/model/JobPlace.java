package com.votelimes.lps.model;

import lombok.Data;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "job_place")
@Data
public class JobPlace {
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name = "company_name")
    String companyName;

    @Column(name = "job_title")
    String jobTitle;

    @Column(name = "period_start")
    Date dateStart;

    @Column(name = "period_end")
    Date dateEnd;

    @Column(name = "salary")
    int salary;

    @Column(name = "user_id")
    int user;
}
