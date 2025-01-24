package com.example.main.services.Users;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.main.domain.models.Users;
import com.example.main.domain.repositories.UsersRepository;
import com.example.main.error.CustomError;
import com.example.main.services.Users.dto.UserCreateDTO;


@Service
public class UserService {

    private final UsersRepository usersRepository;

    public UserService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public List<Users> getAll() {
        List<Users> users = usersRepository.findAll();
        if (users.isEmpty()) {
            throw new CustomError(4004, "No hay usuarios registrados");
        }
        return users;
    }

    public Users getById(long id) {
        return usersRepository.findById(id).
        orElseThrow(
            () -> new CustomError(
                4004, 
                "Usuario no encontrado"
            )
        );
    }

    public void create(UserCreateDTO user) {
        System.out.println("UserCreateDTO entra al caso de uso de registro de usuario: " + user.toString());
        System.out.println("Se procede a convertir el DTO a modelo de usuario");
        Users userModel = new Users(
            user.getEmail(),
            user.getPassword(),
            user.getFirstName(),
            user.getLastName(),
            user.getAddress(),
            user.getPhoneNumber()
            );
        System.out.println("Modelo de usuario creado: " + userModel.toString());
        System.out.println("Se procede a guardar el modelo de usuario en la base de datos");
        usersRepository.save(userModel);
    }
}
