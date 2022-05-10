package com.ssafy.core.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "suggestion")
public class Suggestion extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "family_id")
    private Family family;

    private Long likeCount;

    private Long dislikeCount;

    @OneToMany(mappedBy = "suggestion", cascade = CascadeType.ALL)
    private List<SuggestionReaction> suggestionReaction;

    @PrePersist
    public void prePersist() {
        this.likeCount = this.likeCount == null ? 0 : this.likeCount;
        this.dislikeCount = this.dislikeCount == null ? 0 : this.dislikeCount;
    }

    public void updateLikeCount(int updateCount) {
        this.likeCount = this.likeCount + updateCount;
    }

    public void updateDislikeCount(int updateCount) {
        this.dislikeCount = this.dislikeCount + updateCount;
    }

}
