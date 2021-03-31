package com.ncedu.knownetweb;

import com.ncedu.knownetimpl.model.LessonBody;
import com.ncedu.knownetimpl.model.entity.Lesson;
import com.ncedu.knownetimpl.model.entity.Role;
import com.ncedu.knownetimpl.model.entity.User;
import com.ncedu.knownetimpl.service.LessonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/lessons")
public class LessonController {
    private final LessonService lessonService;

    @Autowired
    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Lesson>> findAll() {
        log.debug("requested: lessons get    (all)");
        return ResponseEntity.ok().body(lessonService.findAll());
    }

    @GetMapping(value = "byId/{id}")
    public ResponseEntity<Lesson> findById(@PathVariable("id") Long id) {
        log.debug("requested: lesson  get    (id = {})", id);
        Optional<Lesson> lesson = lessonService.findById(id);
        return ResponseEntity.of(lesson);
    }

    @GetMapping("byName/{name}")
    public ResponseEntity<List<Lesson>> findByName(@PathVariable(name = "name") String name) {
        log.debug("requested: lesson get    (name = {})", name);
        return ResponseEntity.ok().body(lessonService.findByName(name));
    }

    @GetMapping("byTagId/{tagId}")
    public ResponseEntity<List<Lesson>> findByTagId(@PathVariable(name = "tagId") Long tagId) {
        log.debug("requested: lesson get    (tagId = {})", tagId);
        return ResponseEntity.ok().body(lessonService.findByTagId(tagId));
    }

    @GetMapping("byTeacherId/{teacherId}")
    public ResponseEntity<List<Lesson>> findByTeacherId(@PathVariable(name = "teacherId") Long teacherId) {
        log.debug("requested: learnRequests get    (teacherId = {})", teacherId);
        return ResponseEntity.ok().body(lessonService.findByTeacherId(teacherId));
    }

    @DeleteMapping(value = "byId/{id}")
    public ResponseEntity<String> deleteById(@PathVariable("id") Long id, Authentication authentication) {
        log.debug("requested: lesson  delete (id = {})", id);

        Optional<Lesson> lesson = lessonService.findById(id);
        if (!lesson.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("lesson with id = " + id + " does not exist");
        }

        User user = (User)authentication.getPrincipal();
        if (user.getRole() == Role.ADMIN || lesson.get().getTeacher().getLogin().equals(user.getLogin())) {
            lessonService.deleteById(id);
            return ResponseEntity.ok().body("lesson with id = " + id + " was deleted");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("");
        }
    }

    @PreAuthorize("#lessonBody.teacherId == authentication.principal.id or hasRole('ADMIN')")
    @PostMapping(value = "")
    public ResponseEntity<String> create(@RequestBody LessonBody lessonBody) {
        Lesson lesson = lessonService.makeFromBody(lessonBody);
        log.debug("requested: lesson  create (teacher_id = {}, tagId = {}, lessonName = {})",
                lesson.getTeacher().getId(), lesson.getTag().getId(), lesson.getName());

        boolean created = lessonService.create(lesson);
        if (created) {
            return ResponseEntity.ok().body("lesson with id = " + lesson.getId() + " was created");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("lesson with id = " + lesson.getId() + " already exists");
        }
    }

    @PutMapping(value = "")
    public ResponseEntity<String> update(@RequestBody LessonBody lessonBody, Authentication authentication) {
        Lesson lesson = lessonService.makeFromBody(lessonBody);

        User user = (User)authentication.getPrincipal();
        if (!(user.getRole() == Role.ADMIN) && !lesson.getTeacher().getLogin().equals(user.getLogin())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("");
        }

        Long id = lesson.getId();
        log.debug("requested: lesson  update (id = {})", id);
        boolean updated = lessonService.update(lesson);
        if (updated) {
            return ResponseEntity.ok().body("lesson with id = " + id + " was updated");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("lesson with id = " + id + " does not exist");
        }
    }
    
}
