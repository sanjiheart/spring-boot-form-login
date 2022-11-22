package tw.sanjiheart.repo;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import tw.sanjiheart.model.User;

public interface UserRepo extends MongoRepository<User, String> {

  public boolean existsByUsername(String username);

  public Optional<User> findByUsername(String username);

  public void deleteByUsername(String username);

}
