package com.ssafy.core.entity;

import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Alram")
public class Alarm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column
    private long count;

    @Column
    private String content;

    @ManyToOne
    @JoinColumn(name = "profile_id")
    private Profile me;

    @ManyToOne
    @JoinColumn(name = "target_id")
    private Profile target;
}
