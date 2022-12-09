package tw.sanjiheart.svc;

import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import com.google.common.collect.Sets;

import tw.sanjiheart.model.ResourceCollection;
import tw.sanjiheart.model.User;
import tw.sanjiheart.repo.UserRepo;

@Service
public class UserService implements UserDetailsService {

  private static final String MSG_404 = "User does not exist.";

  private static final String MSG_409 = "User already exists.";

  private UserRepo userRepo;

  public UserService(UserRepo userRepo) {
    this.userRepo = userRepo;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepo.findByUsername(username)
        .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, MSG_404));
  }

  @PostConstruct
  public void init() {
    createDefaultUser("admin", "高進", Sets.newHashSet("ROLE_ADMIN"));
    createDefaultUser("user", "陳小刀", Sets.newHashSet("ROLE_USER"));
  }

  private void createDefaultUser(String username, String name, Set<String> roles) {
    if (!userRepo.existsByUsername(username)) {
      User user = new User();
      user.setUsername(username);
      user.setPassword("pass");
      user.setName(name);
      user.setRoles(roles);
      user.setAccountNonExpired(true);
      user.setAccountNonLocked(true);
      user.setCredentialsNonExpired(true);
      user.setEnabled(true);
      create(user);
    }
  }

  public User create(User user) {
    if (userRepo.existsByUsername(user.getUsername())) {
      throw new HttpClientErrorException(HttpStatus.CONFLICT, MSG_409);
    }
    return userRepo.insert(user);
  }

  public ResourceCollection<User> list() {
    return new ResourceCollection<>(userRepo.count(), userRepo.findAll());
  }

  public User get(String username) {
    return userRepo.findByUsername(username)
        .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, MSG_404));
  }

  public User update(String username, User user) {
    var target = userRepo.findByUsername(username)
        .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, MSG_404));
    target.setRoles(user.getRoles());
    target.setAccountNonExpired(user.isAccountNonExpired());
    target.setAccountNonLocked(user.isAccountNonLocked());
    target.setCredentialsNonExpired(user.isCredentialsNonExpired());
    target.setEnabled(user.isEnabled());
    return userRepo.save(user);
  }

  public void delete(String username) {
    if (!userRepo.existsByUsername(username)) {
      throw new HttpClientErrorException(HttpStatus.NOT_FOUND, MSG_404);
    }
    userRepo.deleteByUsername(username);
  }

}
