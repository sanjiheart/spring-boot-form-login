package tw.sanjiheart.ctrl;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import tw.sanjiheart.model.ResourceCollection;
import tw.sanjiheart.model.User;
import tw.sanjiheart.svc.UserService;

@CrossOrigin({ "http://localhost:9095" })
@RestController
public class UserController {

  private UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @Secured({ "ROLE_ADMIN" })
  @PostMapping(ApiController.USERS)
  public ResponseEntity<User> create(@RequestBody User user) {
    return ResponseEntity.created(URI.create(ApiController.USERS)).body(userService.create(user));
  }

  @Secured({ "ROLE_ADMIN" })
  @GetMapping(ApiController.USERS)
  public ResponseEntity<ResourceCollection<User>> list() {
    return ResponseEntity.ok(userService.list());
  }

  @Secured({ "ROLE_ADMIN" })
  @GetMapping(ApiController.USERS_USERNAME)
  public ResponseEntity<User> get(@PathVariable String username) {
    return ResponseEntity.ok(userService.get(username));
  }

  @GetMapping(ApiController.USERS_ME)
  public ResponseEntity<User> getMe() {
    User me = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    return ResponseEntity.ok(userService.get(me.getUsername()));
  }

  @Secured({ "ROLE_ADMIN" })
  @PutMapping(ApiController.USERS_USERNAME)
  public ResponseEntity<User> update(@PathVariable String username, @RequestBody User user) {
    return ResponseEntity.ok(userService.update(username, user));
  }

  @Secured({ "ROLE_ADMIN" })
  @DeleteMapping(ApiController.USERS_USERNAME)
  public ResponseEntity<Void> delete(@PathVariable String username) {
    userService.delete(username);
    return ResponseEntity.noContent().build();
  }

}
