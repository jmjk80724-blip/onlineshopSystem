package com.ecommerce.onlineshopsystem.user;

import com.ecommerce.onlineshopsystem.user.dto.LoginRequest;
import com.ecommerce.onlineshopsystem.user.dto.RegisterRequest;
import com.ecommerce.onlineshopsystem.user.dto.UserResponse;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {


    private final UserRepository userRepository;


    private  final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse register(RegisterRequest register) {
        if(userRepository.existsByUsername(register.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if(userRepository.existsByEmail(register.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        User user = new User();
        user.setUsername(register.getUsername());
        user.setPassword(passwordEncoder.encode(register.getPassword()));
        user.setEmail(register.getEmail());
        user.setPhone(register.getPhone());
        user.setFullName(register.getFullName());
        user.setRole("customer");

        User savedUser = userRepository.save(user);
        return toResponse(savedUser);
    }

    public UserResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new  UserNotFoundException("Invalid username or password"));
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UserNotFoundException("Invalid username or password");
        }
        return toResponse(user);
    }

    public UserResponse getUserById(Long Id){
        User user = userRepository.findById(Id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id" + Id));
        return toResponse(user);
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public UserResponse updateProfile(Long id, RegisterRequest register) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id" + id));
        user.setFullName(register.getFullName());
        user.setPhone(register.getPhone());

        User updatedUser = userRepository.save(user);
        return toResponse(updatedUser);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id" + id));
        userRepository.delete(user);

    }


    private UserResponse toResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        response.setFullName(user.getFullName());
        response.setRole(user.getRole());
        response.setCreatedAt(user.getCreatedAt());
        return response;

    }



}
