package org.example.shop.user;

import org.example.shop.common.ConflictException;
import org.example.shop.common.NotFoundException;
import org.example.shop.notification.Notifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)              // default for every method: read-only
public class UserService {

    private final UserRepository users;
    private final ApplicationEventPublisher events;

    public UserService(UserRepository users, ApplicationEventPublisher events) {
        this.users = users;
        this.events = events;
    }

    @Transactional
    public UserResponse create(CreateUserRequest request) {
        String email = normalize(request.email());
        if (users.existsByEmailIgnoreCase(email)) {
            throw new ConflictException("Email already in use: " + email);
        }
        User saved = users.save(new User(request.name().trim(), email, request.phone()));
        events.publishEvent(new UserRegisteredEvent(saved.getId(), saved.getName(), email,  request.phone()));
        return UserResponse.from(saved);
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
        return UserResponse.from(user);      // no save(): dirty checking writes the UPDATE at commit
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
