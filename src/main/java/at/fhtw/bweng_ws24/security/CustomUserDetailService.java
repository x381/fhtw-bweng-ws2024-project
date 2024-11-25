package at.fhtw.bweng_ws24.security;

import at.fhtw.bweng_ws24.model.User;
import at.fhtw.bweng_ws24.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailService implements UserDetailsService {

    private final UserService userService;

    public CustomUserDetailService(@Lazy UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            User user = userService.findUserByUsername(username);
            return new UserPrincipal(user.getId(), user.getUsername(), user.getPassword(), user.getRole());
        } catch (EntityNotFoundException e) {
            throw new UsernameNotFoundException(e.getMessage() + " for " + username);
        }
    }
}
