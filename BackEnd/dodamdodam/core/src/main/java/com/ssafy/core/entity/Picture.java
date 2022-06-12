package com.ssafy.core.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.persistence.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "picture")
public class Picture extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id")
    private Album album;

    @Nullable
    @Column( length = 100)
    private String origin_name;

    @Nullable
    @Column( length = 300)
    private String path_name;

    @Nullable
    @Column
    private boolean is_main;

    public void updateOriginName(String origin_name){
        this.origin_name = origin_name;
    }

    public void updatePathName(String path_name){
        this.path_name = path_name;
    }

    public void updateIsMain(boolean is_main){
        this.is_main = is_main;
    }
}
