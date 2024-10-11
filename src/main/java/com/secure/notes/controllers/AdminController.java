package com.secure.notes.controllers;

import com.secure.notes.dtos.UserDTO;
import com.secure.notes.exceptions.ResourceNotFoundException;
import com.secure.notes.models.User;
import com.secure.notes.response.ApiResponse;
import com.secure.notes.services.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {

    private final UserService userService;

    //    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/get-users")
//    public ResponseEntity<List<UserDTO>> getAllUsers() {
    public ResponseEntity<ApiResponse> getAllUsers() {
        try {
            return ResponseEntity.ok(new ApiResponse("success", userService.getAllUsers()));
//        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),null));
        }
    }

    //    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/update-role")
    public ResponseEntity<ApiResponse> updateUserRole(@RequestParam Long userId, @RequestParam String roleName) {
        try {
            userService.updateUserRole(userId, roleName);
            return ResponseEntity.ok(new ApiResponse("Successfully updated user role",null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),null));
        }
    }

    //    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/user/{id}")
    public ResponseEntity<ApiResponse> getUser(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(new ApiResponse("Success", userService.getUserById(id)));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),null));
        }
    }
}
