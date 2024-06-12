package com.voting.system.userservice.repository;

import com.voting.system.userservice.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void UserRepository_CreateUser_ReturnSavedUser(){

        User user = User.builder()
                .userName("Sohaib Khan")
                .userCNIC("61101-8979377-9")
                .userConstituency("Islamabad")
                .build();

        User savedUser = userRepository.save(user);

        Assertions.assertNotNull(savedUser);
        Assertions.assertTrue(savedUser.getUserID() > 0);

    }

    @Test
    public void UserRepository_GetAllUser_ReturnMoreThanOneUser(){

        User user1 = User.builder()
                .userName("Sohaib Khan")
                .userCNIC("61101-8979377-9")
                .userConstituency("Islamabad")
                .build();

        User user2 = User.builder()
                .userName("Huzaifa Khan")
                .userCNIC("61101-1741742-5")
                .userConstituency("Rawalpindi")
                .build();

        userRepository.save(user1);
        userRepository.save(user2);

        List<User> userList = userRepository.findAll();

        Assertions.assertNotNull(userList);
        Assertions.assertEquals(2, userList.size());

    }

    @Test
    public void UserRepository_FindUserById_ReturnUser(){

        User user = User.builder()
                .userName("Sohaib Khan")
                .userCNIC("61101-8979377-9")
                .userConstituency("Islamabad")
                .build();

        userRepository.save(user);

        User userId = userRepository.findById(user.getUserID()).get();

        Assertions.assertNotNull(userId);

    }

    @Test
    public void UserRepository_FindUserByCNIC_ReturnUser(){

        User user = User.builder()
                .userName("Sohaib Khan")
                .userCNIC("61101-8979377-9")
                .userConstituency("Islamabad")
                .build();

        userRepository.save(user);

        String userCNIC = String.valueOf(userRepository.findByUserCNIC(user.getUserCNIC()).get(0));

        Assertions.assertNotNull(userCNIC);

    }

    @Test
    public void UserRepository_FindUserByUserName_ReturnUser(){

        User user = User.builder()
                .userName("Sohaib Khan")
                .userCNIC("61101-8979377-9")
                .userConstituency("Islamabad")
                .build();

        userRepository.save(user);

        String userName = String.valueOf(userRepository.findByUserName(user.getUserName()).get());

        Assertions.assertNotNull(userName);

    }
}
