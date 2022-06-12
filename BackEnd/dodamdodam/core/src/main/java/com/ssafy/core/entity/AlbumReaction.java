package com.ssafy.core.entity;


import lombok.*;
import org.springframework.lang.Nullable;

import javax.persistence.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "album_reaction")
public class AlbumReaction extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id")
    private Album album;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @Nullable
    private String emoticon;

    public void updateEmoticon(String emoticon){
        this.emoticon = emoticon;
    }
}
