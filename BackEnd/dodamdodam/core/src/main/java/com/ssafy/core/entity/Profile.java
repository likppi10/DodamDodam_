package com.ssafy.core.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DynamicUpdate
@Table(name = "profile")
public class Profile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Nullable
    @Column(length = 10)
    private String emotion;

    @Nullable
    @Column(length = 20)
    private String comment;

    @Column(nullable = false, length = 10)
    private String role;

    @Column(nullable = false, length = 10)
    private String nickname;

    @Nullable
    @Column(length = 100)
    private String mission_content;

    @Nullable
    @Column(length = 20)
    private String mission_target;

    @Nullable
    @Column(length = 200)
    private String imagePath;

    @Nullable
    @Column(length = 200)
    private String imageName;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_pk")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "family_id")
    private Family family;

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL)
    private List<AlbumReaction> albumReactions;

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL)
    private List<SuggestionReaction> suggestionReactions;

    @OneToMany(mappedBy = "me", cascade = CascadeType.ALL)
    private List<Alarm> alarms;

//
//
//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "id")
//    private Profile profileParent;

    public void updateEmotion(String emotion) {
        this.emotion = emotion;
    }

    public void updateComment(String comment) {
        this.comment = comment;
    }

    public void updateRole(String role) {
        this.role = role;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void updateImageName(String imageName) {
        this.imageName = imageName;
    }

    public void updateMissionContent(String mission_content) {
        this.mission_content = mission_content;
    }

    public void updateMissionTarget(String mission_target) {
        this.mission_target = mission_target;
    }
}
