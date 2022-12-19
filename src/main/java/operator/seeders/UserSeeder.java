package operator.seeders;

import operator.models.Role;
import operator.models.User;
import operator.repositories.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserSeeder {

    private static final List<User> userList = new ArrayList<>();

    private static void init() {
        User user = new User();
        user.setUsername("ADMIN");
        user.setPassword(new BCryptPasswordEncoder().encode("12345678"));
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.ADMIN));
        userList.add(user);
    }

    public static void seed(UserRepository userRepository) {
        init();
        for (User user : userList)
            if (userRepository.findUserByUsername(user.getUsername()) == null)
                userRepository.save(user);
    }

}