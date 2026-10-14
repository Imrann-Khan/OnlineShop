package org.example.shop.user;

import org.example.shop.common.ConflictException;
import org.example.shop.common.NotFoundException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository users;
    private final ApplicationEventPublisher events;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository users, ApplicationEventPublisher events, PasswordEncoder passwordEncoder) {
        this.users = users;
        this.events = events;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse findById(Long id) {
        return UserResponse.from(getUser(id));
    }

    public Page<UserResponse> findAll(Pageable pageable) {
        return users.findAll(pageable).map(UserResponse::from);
    }

    @Transactional
    public UserResponse update(Long id, UpdateUserRequest request) {
        User user = getUser(id);
        String email = normalize(request.email());
        if (users.existsByEmailIgnoreCaseAndIdNot(email, id)) {
            throw new ConflictException("Email already in use: " + email);
        }
        user.rename(request.name().trim());
        user.changeEmail(email);
        return UserResponse.from(user);
    }

    @Transactional
    public UserResponse register(RegisterRequest request) {
        String email = normalize(request.email());
        if (users.existsByEmailIgnoreCase(email)) {
            throw new ConflictException("Email already in use: " + email);
        }
        User saved = users.save(new User(request.name().trim(), email, request.phone(),
                passwordEncoder.encode(request.password()), Role.CUSTOMER));
        events.publishEvent(new UserRegisteredEvent(saved.getId(), saved.getName(), email, saved.getPhone()));
        return UserResponse.from(saved);
    }

    public Optional<User> findEntityByEmail(String email) {
        return users.findByEmailIgnoreCase(normalize(email));
    }

    @Transactional
    public void delete(Long id) {
        users.delete(getUser(id));
    }

    private User getUser(Long id) {
        return users.findById(id)
                .orElseThrow(() -> new NotFoundException("User " + id + " not found"));
    }

    public User findEntity(Long id) {
        return getUser(id);
    }

    private static String normalize(String email) {
        return email.trim().toLowerCase();
    }
}
