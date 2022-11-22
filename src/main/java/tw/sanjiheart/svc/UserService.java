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

  private UserRepo userRepo;

  public UserService(UserRepo userRepo) {
    this.userRepo = userRepo;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    if (!userRepo.existsByUsername(username)) {
      throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "User does not exist.");
    }
    return userRepo.findByUsername(username).get();
  }

  @PostConstruct
  public void init() {
    createDefaultUser("admin", Sets.newHashSet("ROLE_ADMIN"));
    createDefaultUser("user", Sets.newHashSet("ROLE_USER"));
  }

  private void createDefaultUser(String username, Set<String> roles) {
    if (!userRepo.existsByUsername(username)) {
      User user = new User();
      user.setUsername(username);
      user.setPassword("pass");
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
      throw new HttpClientErrorException(HttpStatus.CONFLICT, "User already exists.");
    }
    return userRepo.insert(user);
  }

  public ResourceCollection<User> list() {
    return new ResourceCollection<>(userRepo.count(), userRepo.findAll());
  }

  public User get(String username) {
    if (!userRepo.existsByUsername(username)) {
      throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "User does not exist.");
    }
    return userRepo.findByUsername(username).get();
  }

  public User update(String username, User user) {
    if (!userRepo.existsByUsername(username)) {
      throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "User does not exist.");
    }
    User targetUser = userRepo.findByUsername(username).get();
    user.setId(targetUser.getId());
    user.setPassword(targetUser.getPassword());
    return userRepo.save(user);
  }

  public void delete(String username) {
    if (!userRepo.existsByUsername(username)) {
      throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "User does not exist.");
    }
    userRepo.deleteByUsername(username);
  }

}
