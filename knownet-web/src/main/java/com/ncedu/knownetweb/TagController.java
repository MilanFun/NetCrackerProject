package com.ncedu.knownetweb;

import com.ncedu.knownetimpl.model.entity.Tag;
import com.ncedu.knownetimpl.service.TagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/tags")
public class TagController {
  private final TagService tagService;

  @Autowired
  public TagController(TagService tagService) {
    this.tagService = tagService;
  }

  @RolesAllowed("ADMIN")
  @GetMapping("/all")
  public ResponseEntity<List<Tag>> findAll() {
    log.debug("requested: tag get     (all)");
    return ResponseEntity.ok(tagService.findAll());
  }

  @GetMapping("byTitle/{title}")
  public ResponseEntity<List<Tag>> findByTitle(@PathVariable(name = "title") String title) {
    log.debug("requested: tag get    (title = {})", title);
    return ResponseEntity.ok().body(tagService.findByTitle(title));
  }

  @RolesAllowed("ADMIN")
  @GetMapping("byId/{id}")
  public ResponseEntity<Tag> findById(@PathVariable("id") Long id) {
    log.debug("requested: tag get      (id = {})", id);
    Optional<Tag> tag = tagService.findById(id);
    return ResponseEntity.of(tag);
  }

  @GetMapping("withParentsById/{id}")
  public ResponseEntity<List<Tag>> findWithParents(@PathVariable("id") Long id) {
    log.debug("requested: tag get with parents (id = {})", id);
    List<Tag> tags = tagService.findWithParents(id);
    return ResponseEntity.ok(tags);
  }

  @GetMapping("children/{id}")
  public ResponseEntity<List<Tag>> getChildren(@PathVariable("id") Long id) {
    log.debug("requested: tag get children (id = {})", id);
    List<Tag> tags = tagService.getChildren(id);
    return ResponseEntity.ok(tags);
  }

  @RolesAllowed("ADMIN")
  @DeleteMapping(value = "byId/{id}")
  public ResponseEntity<String> deleteById(@PathVariable("id") Long id) {
    log.debug("requested: tag  delete (id = {})", id);
    boolean deleted = tagService.deleteById(id);
    if (deleted) {
      return ResponseEntity.ok().body("tag with id = " + id + " was deleted");
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body("tag with id = " + id + " does not exist");
    }
  }

  @RolesAllowed("ADMIN")
  @PostMapping(value = "")
  public ResponseEntity<String> create(@RequestBody Tag tag) {
    log.debug("requested: tag  create (title = {}, parentId = {})", tag.getParentId(), tag.getParentId());
    boolean created = tagService.create(tag);
    if (created) {
      return ResponseEntity.ok().body("tag with id = " + tag.getId() + " was created");
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body("tag with id = " + tag.getId() + " already exists");
    }
  }

  @RolesAllowed("ADMIN")
  @PutMapping(value = "")
  public ResponseEntity<String> update(@RequestBody Tag tag) {
    Long id = tag.getId();
    log.debug("requested: tag  update (id = {})", id);
    boolean updated = tagService.update(tag);
    if (updated) {
      return ResponseEntity.ok().body("tag with id = " + id + " was updated");
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body("tag with id = " + id + " does not exist");
    }
  }
  @RolesAllowed("ADMIN")
  @GetMapping("/lastTagId")
  public ResponseEntity<Long> getLastId() {
    log.debug("requested: last tag id");
    return ResponseEntity.ok(tagService.getLastId());
  }
}
