package com.codingshuttle.project.airBnbApp.security;

import com.codingshuttle.project.airBnbApp.dto.LoginDTO;
import com.codingshuttle.project.airBnbApp.dto.SignUpRequestDTO;
import com.codingshuttle.project.airBnbApp.dto.UserDTO;
import com.codingshuttle.project.airBnbApp.entity.User;
import com.codingshuttle.project.airBnbApp.entity.enums.Role;
import com.codingshuttle.project.airBnbApp.exception.ResourceNotFoundException;
import com.codingshuttle.project.airBnbApp.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final ModelMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    public AuthService(UserRepository userRepository, ModelMapper mapper, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JWTService jwtService) {
        this.userRepository = userRepository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public UserDTO signUp(SignUpRequestDTO signUpRequestDTO){

        User user=userRepository.findByEmail(signUpRequestDTO.getEmail()).orElse(null);
        if(user!=null){
            throw new RuntimeException("User is already present with the same email ID");
        }

        User newUser=mapper.map(signUpRequestDTO,User.class);
        newUser.setRoles(Set.of(Role.GUEST));
        newUser.setPassword(passwordEncoder.encode(signUpRequestDTO.getPassword()));
        newUser=userRepository.save(newUser);

        return mapper.map(newUser,UserDTO.class);
    }

    public String[] login(LoginDTO loginDTO){
        Authentication authentication= authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDTO.getEmail(),loginDTO.getPassword()
        ));

        User user=(User) authentication.getPrincipal();

        String[] arr=new String[2];
        arr[0]=jwtService.generateAccessToken(user);
        arr[1]=jwtService.generateRefreshToken(user);

        return arr;
    }

    public String refreshToken(String refreshToken){
        long id= jwtService.getUserIdFromToken(refreshToken);
        User user=userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("User not found with id:"+id));

        return jwtService.generateAccessToken(user);
    }

}
