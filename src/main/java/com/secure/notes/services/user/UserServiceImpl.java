package com.secure.notes.services.user;

import com.secure.notes.dtos.UserDTO;
import com.secure.notes.exceptions.AlreadyExistsException;
import com.secure.notes.exceptions.ResourceNotFoundException;
import com.secure.notes.models.AppRole;
import com.secure.notes.models.Role;
import com.secure.notes.models.User;
import com.secure.notes.repositories.RoleRepository;
import com.secure.notes.repositories.UserRepository;
import com.secure.notes.request.SignupRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder encoder;


    @Override
    public void updateUserRole(Long userId, String roleName) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found!"));
        AppRole appRole = AppRole.valueOf(roleName);
        Role role = roleRepository.findByRoleName(appRole).orElseThrow(() -> new ResourceNotFoundException("Role not found!"));

        user.setRole(role);
        userRepository.save(user);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertUserToDto)
                .toList();
    }

    @Override
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found!"));
        return convertUserToDto(user);
    }

    @Override
    public UserDTO registerUser(SignupRequest signUpRequest) {
        if (userRepository.existsByUserName(signUpRequest.getUsername())) {
            throw new AlreadyExistsException("Username is already taken!");
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new AlreadyExistsException("Email is already in use!");
        }

        User user = new User(signUpRequest.getUsername(),signUpRequest.getEmail(),encoder.encode(signUpRequest.getPassword()));
        user.setupDefaults(roleRepository);
        return convertUserToDto(userRepository.save(user));
    }

    @Override
    public UserDTO convertUserToDto(User user) {
        return new UserDTO(
                user.getUserId(),
                user.getUserName(),
                user.getEmail(),
                user.isAccountNonLocked(),
                user.isAccountNonExpired(),
                user.isCredentialsNonExpired(),
                user.isEnabled(),
                user.getCredentialsExpiryDate(),
                user.getAccountExpiryDate(),
                user.getTwoFactorSecret(),
                user.isTwoFactorEnabled(),
                user.getSignUpMethod(),
                user.getRole(),
                user.getCreatedDate(),
                user.getUpdatedDate()
        );
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUserName(username).orElseThrow(() -> new ResourceNotFoundException("User Not Found!"));
    }
}
