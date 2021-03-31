package com.ncedu.knownetimpl.service;

import com.ncedu.knownetimpl.model.LessonBody;
import com.ncedu.knownetimpl.model.entity.LearnRequest;
import com.ncedu.knownetimpl.model.entity.Lesson;
import com.ncedu.knownetimpl.model.entity.Tag;
import com.ncedu.knownetimpl.model.entity.User;
import com.ncedu.knownetimpl.repository.LearnRequestRepository;
import com.ncedu.knownetimpl.repository.LessonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class LessonService {

    private final UserService userService;
    private final TagService tagService;
    private final LessonRepository lessonRepository;
    private final LearnRequestRepository learnRequestRepository;

    @Autowired
    public LessonService(LessonRepository lessonRepository, UserService userService, TagService tagService, LearnRequestRepository learnRequestRepository) {
        this.lessonRepository = lessonRepository;
        this.userService = userService;
        this.tagService = tagService;
        this.learnRequestRepository = learnRequestRepository;
    }

    public List<Lesson> findAll() {
        return lessonRepository.findAll();
    }

    public Optional<Lesson> findById(Long id) {
        if (id != null) {
            return lessonRepository.findById(id);
        } else {
            log.warn("requested lesson with null id");
            return Optional.empty();
        }
    }

    public List<Lesson> findByName(String name) {
        return lessonRepository.findByName(name);
    }

    public List<Lesson> findByTagId(Long tagId) {
        if (tagId != null) {
            return lessonRepository.findByTagId(tagId);
        } else {
            log.warn("requested lesson with null tagId");
            return new ArrayList<Lesson>(0);
        }
    }

    public List<Lesson> findByTeacherId(Long teacherId) {
        if (teacherId != null) {
            return lessonRepository.findByTeacherId(teacherId);
        } else {
            log.warn("requested lesson with null teacherId");
            return new ArrayList<Lesson>(0);
        }
    }

    public boolean deleteById(Long id) {
        if (id == null) {
            log.warn("deleting lesson with null id");
            return false;
        }
        boolean exists = lessonRepository.existsById(id);
        if (exists) {
            List<LearnRequest> learnRequests = learnRequestRepository.findByLessonId(id);
            for(LearnRequest k : learnRequests) {
              learnRequestRepository.deleteById(k.getId());
            }
            lessonRepository.deleteById(id);
        }
        return exists;
    }

    public boolean create(Lesson lesson) {
        boolean exists = lesson.getId() != null && lessonRepository.existsById(lesson.getId());
        if (!exists) {
            lessonRepository.save(lesson);
        }
        return !exists;
    }

    public boolean update(Lesson lesson) {
        if (lesson.getId() == null) {
            log.warn("updating lesson with null id");
            return false;
        }
        Optional<Lesson> oldLessonOpt = findById(lesson.getId());
        if (oldLessonOpt.isPresent()) {
            Lesson oldLesson = oldLessonOpt.get();
            oldLesson.setName(lesson.getName());
            oldLesson.setTag(lesson.getTag());
            oldLesson.setTopic(lesson.getTopic());
            oldLesson.setPointsToGet(lesson.getPointsToGet());
            oldLesson.setSkillsToComplete(lesson.getSkillsToComplete());
            oldLesson.setDifficulty(lesson.getDifficulty());
            oldLesson.setDescription(lesson.getDescription());
            lessonRepository.save(oldLesson);
        }
        return oldLessonOpt.isPresent();
    }

    public Lesson makeFromBody(LessonBody body) {
        Lesson lesson = new Lesson();
        Optional<Lesson> lessonOpt = findById(body.getId());
        lesson.setId(body.getId());

        Optional<User> teacher;
        if (body.getTeacherId() != null) {
            teacher = userService.findById(body.getTeacherId());
        } else if (lessonOpt.isPresent()) {
            teacher = userService.findById(lessonOpt.get().getTeacher().getId());
        } else {
            teacher = Optional.empty();
        }
        lesson.setTeacher(teacher.orElse(new User()));

        Optional<Tag> tag;
        if (body.getTagId() != null) {
            tag = tagService.findById(body.getTagId());
        } else if (lessonOpt.isPresent()) {
            tag = tagService.findById(lessonOpt.get().getTag().getId());
        } else {
            tag = Optional.empty();
        }
        lesson.setTag(tag.orElse(new Tag()));

        lesson.setSkillsToComplete(body.getSkillsToComplete());
        lesson.setDescription(body.getDescription());
        lesson.setDifficulty(body.getDifficulty());
        lesson.setName(body.getName());
        lesson.setPointsToGet(body.getPointsToGet());
        lesson.setTopic(body.getTopic());

        return lesson;
    }

}
