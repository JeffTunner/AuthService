package org.example.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.entities.UserInfo;
import org.example.model.UserInfoDto;
import org.example.repository.UserRepository;
import org.example.utils.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

@Component
@AllArgsConstructor
@Data
public class UserDetailsServiceImpl implements UserDetailsService {

   @Autowired
   private final UserRepository userRepository;

   @Autowired
   private final PasswordEncoder passwordEncoder;

   private static final Logger log = (Logger) LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserInfo user = userRepository.findByUsername(username);
        if(user == null) {
            log.error("Username not found" +username);
            throw new UsernameNotFoundException(username);
        }
        log.info("User Authenticated Succesfully!!!");
        return new CustomUserDetails(user);
    }

    public UserInfo checkIfUserAlreadyExist(UserInfoDto userInfoDto) {
        return userRepository.findByUsername(userInfoDto.getUsername());
    }

    public Boolean signupUser(UserInfoDto userInfoDto) {

            ValidationUtil.validateUserAttributes(userInfoDto);

            userInfoDto.setPassword(passwordEncoder.encode(userInfoDto.getPassword()));
            if (Objects.nonNull(checkIfUserAlreadyExist(userInfoDto))) {
                return false;
            }
            String userID = UUID.randomUUID().toString();
            userRepository.save(new UserInfo(userID, userInfoDto.getUsername(), userInfoDto.getPassword(), new HashSet<>()));
            return true;
    }
}
