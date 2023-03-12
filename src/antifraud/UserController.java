package antifraud;

import java.util.*;

import org.h2.engine.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class UserController {

    @Autowired
    UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    UserServiceImpl userServiceImpl;

    //creating a get mapping that retrieves all the users detail from the database
    @GetMapping("/api/auth/user")
    private List<User> getAllUser() {
        return userDetailsServiceImpl.getAllUser();
    }

    @GetMapping("/api/auth/list")
    private ResponseEntity getAllUserList() {
        if (getAllUser().size() == 0) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        List<GetMappingResult> list = new ArrayList<GetMappingResult>();
        for (int i=0;i<getAllUser().size();i++) {
            list.add(new GetMappingResult((getAllUser().get(i).getId()), (getAllUser().get(i).getName()), (getAllUser().get(i).getUsername())));
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @DeleteMapping("/api/auth/user/{username}")
    private ResponseEntity<Map> deleteUserByUsername(@PathVariable("username") String username) {
        LinkedHashMap<String, Object> deleteMap = new LinkedHashMap<>();
        boolean usernameFound = false;
        int idToDelete = 0;
        for (int i = 0; i < getAllUser().size(); i++) {
            if (getAllUser().get(i).getUsername().equals(username)) {
                usernameFound = true;
                idToDelete = getAllUser().get(i).getId();
            }
        }
        if (usernameFound) {
            deleteMap.put("username", username);
            deleteMap.put("status", "Deleted successfully!");
            userDetailsServiceImpl.delete(idToDelete);
            return new ResponseEntity<>(deleteMap, HttpStatus.OK);
        } else return new ResponseEntity<>(deleteMap, HttpStatus.NOT_FOUND);

    }

    @Bean
    public PasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder();
    }

    //creating post mapping that post the user detail in the database
    @PostMapping("/api/auth/user")
    private ResponseEntity<Map> saveUser(@RequestBody User user) {
        LinkedHashMap<String, Object> userMap = new LinkedHashMap<>();
        boolean duplicate = false;

        for (int i = 0; i < getAllUser().size(); i++) {
            if (getAllUser().get(i).getUsername().equals(user.getUsername())) {
                duplicate = true;
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        }
        if (user.getUsername() == null || user.getName() == null || user == null || user.getPassword() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        user.setRole("User");
        userServiceImpl.save(user);
        user.setPassword(getEncoder().encode(user.getPassword()));

        userMap.put("id", user.getId());
        userMap.put("name", user.getName());
        userMap.put("username", user.getUsername());

        if (userDetailsServiceImpl.saveOrUpdate(user) || !duplicate) { // make it return boolean on success or failed
            return new ResponseEntity<>(userMap, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/actuator/shutdown")
    private void shutdown() {
        ConfigurableApplicationContext ctx = new SpringApplicationBuilder(AntiFraudApplication.class)
                .web(WebApplicationType.NONE).run();

        int exitCode = SpringApplication.exit(ctx, new ExitCodeGenerator() {
            @Override
            public int getExitCode() {
                // return the error code
                return 0;
            }
        });
        System.exit(exitCode);
    }
}

