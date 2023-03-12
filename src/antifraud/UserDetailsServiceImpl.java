package antifraud;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl /*implements UserDetailsService*/ {
    @Autowired
    UserRepositoryImpl userRepositoryimpl;

 //   getting all user records
    public List<User> getAllUser()
    {
        List<User> users = new ArrayList<User>();
        userRepositoryimpl.findAll().forEach(user -> users.add(user));
        return users;
    }

    public boolean saveOrUpdate(User user)
    {
        userRepositoryimpl.save(user);
        return true;
    }

    //getting a specific record
    public User getUserById(int id)
    {
        return userRepositoryimpl.findById(id).get();
    }

        //deleting a specific record
    public void delete(int id)
    {
        userRepositoryimpl.deleteById(id);
    }
}


