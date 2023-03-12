package antifraud;

import org.springframework.data.repository.CrudRepository;

public interface UserRepositoryImpl extends CrudRepository<User, Integer> {
    User findUserByUsername(String username);
}
