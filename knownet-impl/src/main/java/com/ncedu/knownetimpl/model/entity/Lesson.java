package com.ncedu.knownetimpl.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "lessons")
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "teacher_id", referencedColumnName = "id")
    private User teacher;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "tag_id", referencedColumnName = "id")
    private Tag tag;

    @Column(name = "topic")
    private String topic;

    @Column(name = "difficulty")
    private Double difficulty = 0.0;

    @Column(name = "points_to_get")
    private Integer pointsToGet = 0;

    @Column(name = "description", length = 2000)
    private String description;

    @Column(name = "skills_to_complete", length = 2000)
    private String skillsToComplete;

}
