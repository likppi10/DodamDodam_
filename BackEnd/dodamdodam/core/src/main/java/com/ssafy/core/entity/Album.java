package com.ssafy.core.entity;


import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "album")
public class Album extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "family_id")
    private Family family;

    @Nullable
    @Setter
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate date;

    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL)
    private List<HashTag> hashTags;

    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL)
    private List<AlbumReaction> albumReactions;

    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL)
    private List<Picture> pictures;

    public void updateLocalDate(LocalDate date){
        this.date = date;
    }
}
