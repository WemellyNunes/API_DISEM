package com.disem.API.repositories;

import com.disem.API.enums.OrdersServices.RoleEnum;
import com.disem.API.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {
    Optional<UserModel> findByIdUsuario(Long idUsuario);

    Optional<UserModel> findByPapel(RoleEnum papel);
}
