package com.ncedu.knownetimpl.repository;

import com.ncedu.knownetimpl.model.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {

    List<Lesson> findByName(String name);

    List<Lesson> findByTagId(Long tagId);

    List<Lesson> findByTeacherId(Long teacherId);

}

