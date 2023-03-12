package antifraud;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserDetailsService {

    @Autowired
    UserRepositoryImpl userRepositoryImpl;

    public List<User> getAllUser()
    {
        List<User> users = new ArrayList<User>();
        userRepositoryImpl.findAll().forEach(user -> users.add(user));
        return users;
    }

    public User getUserById(int id)
    {
        return userRepositoryImpl.findById(id).get();
    }
    public void save(User user) {
        userRepositoryImpl.save(user);
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepositoryImpl.findUserByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("Not found: " + username);
        }

        return new UserDetailsImpl(user);
    }

    public User findUserByUsername(String username) {
        return userRepositoryImpl.findUserByUsername(username);
    }
}