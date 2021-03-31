package com.ncedu.knownetimpl.service;

import com.ncedu.knownetimpl.model.LearnRequestBody;
import com.ncedu.knownetimpl.model.entity.LearnRequest;
import com.ncedu.knownetimpl.model.entity.LearnRequest.Status;
import com.ncedu.knownetimpl.model.entity.Lesson;
import com.ncedu.knownetimpl.model.entity.User;
import com.ncedu.knownetimpl.repository.LearnRequestRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class LearnRequestService {
    private final LearnRequestRepository learnRequestRepository;
    private final UserService userService;
    private final LessonService lessonService;

    @Autowired
    public LearnRequestService(LearnRequestRepository learnRequestRepository, UserService userService, LessonService lessonService) {
        this.learnRequestRepository = learnRequestRepository;
        this.userService = userService;
        this.lessonService = lessonService;
    }

    public List<LearnRequest> findAll() {
        return learnRequestRepository.findAll();
    }

    public List<LearnRequest> findByLessonId(Long lessonId) {
      if (lessonId != null) {
        return learnRequestRepository.findByLessonId(lessonId);
      } else {
        log.warn("requested learnRequest with null lessonId");
        return new ArrayList<>();
      }
    }

    public Optional<LearnRequest> findById(Long id) {
        if (id != null) {
            return learnRequestRepository.findById(id);
        } else {
            log.warn("requested learnRequest with null id");
            return Optional.empty();
        }
    }

    public boolean deleteById(Long id) {
        if (id == null) {
            log.warn("deleting learnRequest with null id");
            return false;
        }
        boolean exists = learnRequestRepository.existsById(id);
        if (exists) {
            learnRequestRepository.deleteById(id);
        }
        return exists;
    }

    public List<LearnRequest> findByTeacherId(Long teacherId) {
        if (teacherId != null) {
            return learnRequestRepository.findByTeacherIdAndIsFinishedFalse(teacherId);
        } else {
            log.warn("requested learnRequest with null teacherId");
            return new ArrayList<>();
        }
    }

    public List<LearnRequest> findByStudentId(Long studentId) {
        if (studentId != null) {
            return learnRequestRepository.findByStudentIdAndIsFinishedFalse(studentId);
        } else {
            log.warn("requested learnRequest with null studentId");
            return new ArrayList<>();
        }
    }

    public List<LearnRequest> findActiveByTeacherId(Long teacherId) {
        if (teacherId != null) {
            return learnRequestRepository.findByTeacherIdAndHiddenForTeacherFalseAndIsFinishedFalse(teacherId);
        } else {
            log.warn("requested learnRequest with null teacherId");
            return new ArrayList<>();
        }
    }

    public List<LearnRequest> findActiveByStudentId(Long studentId) {
        if (studentId != null) {
            return learnRequestRepository.findByStudentIdAndHiddenForStudentFalseAndIsFinishedFalse(studentId);
        } else {
            log.warn("requested learnRequest with null studentId");
            return new ArrayList<>();
        }
    }

    public boolean create(LearnRequest learnRequest) {
        boolean exists = learnRequest.getId() != null && learnRequestRepository.existsById(learnRequest.getId());
        if (!exists) {
            learnRequestRepository.save(learnRequest);
        }
        return !exists;
    }

    public boolean update(LearnRequest learnRequest) throws IllegalStateException {
        if (learnRequest.getId() == null) {
            log.warn("updating learnRequest with null id");
            return false;
        }
        Optional<LearnRequest> oldLearnRequestOpt = findById(learnRequest.getId());
        if (oldLearnRequestOpt.isPresent()) {
            LearnRequest oldLearnRequest = oldLearnRequestOpt.get();

            Status oldStatus = oldLearnRequest.getStatus();
            Status newStatus = learnRequest.getStatus();

            if (ifCanChangeStatus(oldStatus, newStatus)) {
                log.debug(newStatus.getDescription());
                oldLearnRequest.setStatus(newStatus);

                oldLearnRequest.setHiddenForStudent(false);
                oldLearnRequest.setHiddenForTeacher(false);

                if (newStatus.isFinished()) {
                    oldLearnRequest.setIsFinished(true);
                }
            } else {
                oldLearnRequest.setHiddenForStudent(learnRequest.getHiddenForStudent());
                oldLearnRequest.setHiddenForTeacher(learnRequest.getHiddenForTeacher());
            }

            learnRequestRepository.save(oldLearnRequest);
        }

        return oldLearnRequestOpt.isPresent();
    }

    static private boolean ifCanChangeStatus(Status oldStatus, Status newStatus) throws IllegalStateException {
        if (oldStatus == newStatus) {
            return false;
        }

        switch (oldStatus) {
            case LESSON_REQUESTED:
                if (
                    newStatus == Status.MEETING_CANCELED ||
                    newStatus == Status.LESSON_REQUEST_REJECTED ||
                    newStatus == Status.LESSON_REQUEST_ACCEPTED
                ) {
                    return true;
                } else {
                    throw new IllegalStateException("Unexpected value: " + newStatus);
                }
            case LESSON_REQUEST_ACCEPTED:
                if (
                    newStatus == Status.MEETING_CONFIRMED ||
                    newStatus == Status.MEETING_DISPROVED ||
                    newStatus == Status.MEETING_CANCELED
                ) {
                    return true;
                } else {
                    throw new IllegalStateException("Unexpected value: " + newStatus);
                }
            case LESSON_REQUEST_REJECTED:
            case MEETING_CONFIRMED:
            case MEETING_CANCELED:
            case MEETING_DISPROVED:
            default:
                throw new IllegalStateException("Unexpected value: " + newStatus);
        }
    }

    public LearnRequest makeFromBody(LearnRequestBody body) {
        LearnRequest learnRequest = new LearnRequest();

        learnRequest.setId(body.getId());
        learnRequest.setHiddenForStudent(body.getHiddenForStudent());
        learnRequest.setHiddenForTeacher(body.getHiddenForTeacher());
        learnRequest.setStatus(body.getStatus());
        learnRequest.setIsFinished(body.getStatus().isFinished());

        Optional<User> student;
        Optional<Lesson> lesson;

        if (body.getStudentId() == null) {
            student = Optional.empty();
        } else {
            student = userService.findById(body.getStudentId());
        }

        if (body.getLessonId() == null) {
            lesson = Optional.empty();
        } else {
            lesson = lessonService.findById(body.getLessonId());
        }

        if (lesson.isPresent()) {
            learnRequest.setTeacher(lesson.get().getTeacher());
        } else {
            learnRequest.setTeacher(new User());
        }

        learnRequest.setStudent(student.orElse(new User()));
        learnRequest.setLesson(lesson.orElse(new Lesson()));

        return learnRequest;
    }


}
