package com.ecommerce.onlineshopsystem.user;

import com.ecommerce.onlineshopsystem.user.dto.LoginRequest;
import com.ecommerce.onlineshopsystem.user.dto.RegisterRequest;
import com.ecommerce.onlineshopsystem.user.dto.UserResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody  RegisterRequest request){
        UserResponse newUser = userService.register(request);
        return ResponseEntity.ok(newUser);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@Valid @RequestBody LoginRequest request){
        UserResponse user = userService.login(request);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/id")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id){
        UserResponse user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PutMapping("/id")
    public ResponseEntity<UserResponse> updateProfile(
            @PathVariable Long id,
            @Valid @RequestBody RegisterRequest request){
        UserResponse user = userService.updateProfile(id, request);
        return ResponseEntity.ok(user);
    }
    @DeleteMapping("/id")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
         userService.deleteUser(id);
         return ResponseEntity.noContent().build();
    }

}
