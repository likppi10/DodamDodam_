package com.ssafy.core.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "family")
public class Family {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private String code;

    @Setter
    @Column
    private String picture;

    @OneToMany(mappedBy = "family", cascade = CascadeType.ALL)
    private List<Album> albums;

    @OneToMany(mappedBy = "family", cascade = CascadeType.ALL)
    private List<Schedule> schedules;

    @OneToMany(mappedBy = "family", cascade = CascadeType.ALL)
    private List<Suggestion> suggestions;

}
