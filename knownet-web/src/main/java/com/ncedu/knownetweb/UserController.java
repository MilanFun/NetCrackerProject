package com.ncedu.knownetweb;

import com.ncedu.knownetimpl.model.entity.User;
import com.ncedu.knownetimpl.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> findAll() {
        log.debug("requested: users get    (all)");
        return ResponseEntity.ok().body(userService.findAll());
    }

    @GetMapping("byUsername/{username}")
    public ResponseEntity<User> findByUsername(@PathVariable(name = "username") String username) {
        log.debug("requested: user  get    (username = {})", username);
        return ResponseEntity.of(userService.findByUsername(username));
    }

    @GetMapping("byGroup/{group}")
    public ResponseEntity<List<User>> findByGroup(@PathVariable(name = "group") String group) {
        log.debug("requested: users get    (group = {})", group);
        return ResponseEntity.ok().body(userService.findByGroup(group));
    }

    @GetMapping(value = "byId/{id}")
    public ResponseEntity<User> findById(@PathVariable("id") Long id) {
        log.debug("requested: user  get    (id = {})", id);
        Optional<User> user = userService.findById(id);
        return ResponseEntity.of(user);
    }


    @DeleteMapping(value = "byUsername/{username}")
    public ResponseEntity<String> deleteByUsername(@PathVariable("username") String username) {
        log.debug("requested: user  delete (username = {})", username);
        boolean deleted = userService.deleteByUsername(username);
        if (deleted) {
            return ResponseEntity.ok().body("user with username = " + username + " was deleted");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("user with username = " + username + " does not exist");
        }
    }

    @DeleteMapping(value = "byId/{id}")
    public ResponseEntity<String> deleteById(@PathVariable("id") Long id) {
        log.debug("requested: user  delete (id = {})", id);
        boolean deleted = userService.deleteById(id);
        if (deleted) {
            return ResponseEntity.ok().body("user with id = " + id + " was deleted");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("user with id = " + id + " does not exist");
        }
    }

    @PostMapping(value = "user")
    public ResponseEntity<String> create(@RequestBody User user) {
        String username = user.getUsername();
        log.debug("requested: user  create (username = {})", username);
        boolean created = userService.create(user);
        if (created) {
            return ResponseEntity.ok().body("user with username = " + username + " was created");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("user with username = " + username + " already exists");
        }
    }

    @PutMapping(value = "user")
    public ResponseEntity<String> update(@RequestBody User user) {
        String username = user.getUsername();
        log.debug("requested: user  update (username = {})", username);
        boolean updated = userService.update(user);
        if (updated) {
            return ResponseEntity.ok().body("user with username = " + username + " was updated");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("user with username = " + username + " does not exist");
        }
    }
}
