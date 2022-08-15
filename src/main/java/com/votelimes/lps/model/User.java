package com.votelimes.lps.model;

import com.votelimes.lps.model.enums.UserRole;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "user_data")
@Data
public class User {
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    UserRole role;

    @Column(name = "active")
    boolean active;

    @Column(name = "comment")
    String comment;

    public User(int id, UserRole role) {
        this.id = id;
        this.role = role;
    }

    public User() {
        id = -1;
        role = UserRole.client;
        comment = "";
    }
}
