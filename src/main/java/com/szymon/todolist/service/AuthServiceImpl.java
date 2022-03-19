package com.szymon.todolist.service;

import com.szymon.todolist.security.jwt.JwtUtils;
import com.szymon.todolist.exception.UserAlreadyExistsException;
import com.szymon.todolist.exception.NotFoundException;
import com.szymon.todolist.model.Role;
import com.szymon.todolist.model.User;
import com.szymon.todolist.payload.response.JwtResponse;
import com.szymon.todolist.payload.request.LoginRequest;
import com.szymon.todolist.payload.request.SignupRequest;
import com.szymon.todolist.reposotiry.RoleRepository;
import com.szymon.todolist.reposotiry.UserRepository;
import com.szymon.todolist.security.user.ERole;
import com.szymon.todolist.security.user.UserDetailsImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;

    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder encoder,
                           JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = setUserAuthentication(loginRequest);
        return prepareJwtResponse(authentication);
    }

    @Override
    public void registerUser(SignupRequest signUpRequest) {
        checkIfUsernameOrEmailAlreadyExists(signUpRequest);
        createUser(signUpRequest);
    }

    private void checkIfUsernameOrEmailAlreadyExists(SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new UserAlreadyExistsException("Error: Username is already taken!");
        }
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new UserAlreadyExistsException("Error: Email is already in use!");
        }
    }

    private void createUser(SignupRequest signUpRequest) {
        User user = User.builder()
                .username(signUpRequest.getUsername())
                .email(signUpRequest.getEmail())
                .password(encoder.encode(signUpRequest.getPassword()))
                .build();
        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new NotFoundException("Error: Role is not found."));
        user.getRoles().add(userRole);
        userRepository.save(user);
    }

    private JwtResponse prepareJwtResponse(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return JwtResponse.builder()
                .accessToken(jwtUtils.generateJwtToken(authentication))
                .id(userDetails.getId())
                .username(userDetails.getUsername())
                .email(userDetails.getEmail())
                .roles(prepareUserRoles(userDetails))
                .build();
    }

    private List<String> prepareUserRoles(UserDetailsImpl userDetails) {
        return userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }

    private Authentication setUserAuthentication(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }
}
