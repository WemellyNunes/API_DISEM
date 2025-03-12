package com.disem.API.services;

import com.disem.API.enums.OrdersServices.RoleEnum;
import com.disem.API.models.UserModel;
import com.disem.API.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public Optional<UserModel> findByIdUsuario(Long idUsuario) {
        return userRepository.findByIdUsuario(idUsuario);
    }
    public UserModel saveUser(Long idUsuario, String nome, String email, RoleEnum papel) {
        Optional<UserModel> existingUser = findByIdUsuario(idUsuario);

        if (existingUser.isEmpty()) {
            UserModel user = new UserModel();
            user.setIdUsuario(idUsuario);
            user.setNome(nome);
            user.setEmail(email);
            user.setPapel(papel);
            return userRepository.save(user);
        }
        return existingUser.get();
    }
    public List<UserModel> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<UserModel> findById(Long id) {
        return userRepository.findById(id);
    }

    public UserModel save(UserModel user) {
        return userRepository.save(user);
    }

    public void deleteUser(UserModel user) {
        userRepository.delete(user);
    }
}
