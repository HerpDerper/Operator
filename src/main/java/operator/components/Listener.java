package operator.components;

import operator.repositories.UserRepository;
import operator.seeders.UserSeeder;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class Listener {

    private final UserRepository userRepository;

    public Listener(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @EventListener
    public void handleContextRefresh(ContextRefreshedEvent event) {
        UserSeeder.seed(userRepository);
    }

}