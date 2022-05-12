package com.ssafy.core.entity;

import com.ssafy.core.dto.res.AlbumPictureListResDto;
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
    @Column(name = "album_id")
    private List<Album> albums;

    @OneToMany(mappedBy = "family", cascade = CascadeType.ALL)
    @Column(name = "schedule_id")
    private List<Schedule> schedules;

    @OneToMany(mappedBy = "family", cascade = CascadeType.ALL)
    @Column(name = "suggestion")
    private List<Suggestion> suggestions;

}
