package com.example.restourant.services.auth;


import com.example.restourant.dtos.SignupRequest;
import com.example.restourant.dtos.UserDto;
import com.example.restourant.entities.User;
import com.example.restourant.enums.UserRole;
import com.example.restourant.repositories.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImplimentation implements AuthService{
    private final UserRepository userRepository;


    public AuthServiceImplimentation(UserRepository userRepository) {
        this.userRepository = userRepository;

    }

    @Override
   public  UserDto createUser(SignupRequest signupRequest){

        User user=new User();
        user.setName(signupRequest.getName());
        user.setEmail(signupRequest.getEmail());
        user.setPassword(new BCryptPasswordEncoder().encode(signupRequest.getPassword()));
        user.setUserRole(UserRole.CUSTOMER);
        User createUser=userRepository.save(user);
        UserDto createdUserDto= new UserDto();
        createdUserDto.setId(createdUserDto.getId());
        createdUserDto.setName(createdUserDto.getName());
        createdUserDto.setEmail(createUser.getEmail());
        createdUserDto.setUserRole(createUser.getUserRole());


    return createdUserDto ;
}
}
