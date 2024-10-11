package com.secure.notes.services.user;

import com.secure.notes.dtos.UserDTO;
import com.secure.notes.models.User;
import com.secure.notes.request.SignupRequest;

import java.util.List;

public interface UserService {
    void updateUserRole(Long userId, String roleName);

    List<UserDTO> getAllUsers();

    UserDTO getUserById(Long id);

    UserDTO registerUser(SignupRequest signupRequest);

    UserDTO convertUserToDto(User user);

    User getUserByUsername(String username);
}
