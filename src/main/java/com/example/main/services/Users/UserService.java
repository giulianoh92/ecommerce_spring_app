package com.example.main.services.Users;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.main.domain.models.Carts;
import com.example.main.domain.models.Users;
import com.example.main.domain.repositories.CartsRepository;
import com.example.main.domain.repositories.UsersRepository;
import com.example.main.error.CustomError;
import com.example.main.services.Users.dto.UserCreateDTO;
import com.example.main.services.Users.dto.UserGetDTO;
import com.example.main.services.Users.dto.UserUpdateDTO;


@Service
public class UserService {

    @Autowired
    private final UsersRepository usersRepository;

    @Autowired
    private final CartsRepository cartsRepository;

    public UserService(
        UsersRepository usersRepository,
        CartsRepository cartsRepository
    ) {
        this.usersRepository = usersRepository;
        this.cartsRepository = cartsRepository;
    }

    public List<UserGetDTO> getAll() {
        List<Users> users = usersRepository.findAll();
        if (users.isEmpty()) {
            throw new CustomError(4004, "No hay usuarios registrados");
        }
        return users.stream().map(user -> UserGetDTO.mapToDto(user)).collect(Collectors.toList());
    }

    public UserGetDTO getById(long id) {
        return UserGetDTO.mapToDto(usersRepository.findById(id).
        orElseThrow(
            () -> new CustomError(
                4004, 
                "Usuario no encontrado"
            )
        ));
    }

    public void create(UserCreateDTO user) {
        if(usersRepository.findByEmail(user.getEmail()) != null) {
            throw new CustomError(4000, "El correo ya está registrado");
        }
        Users userModel = new Users(
            user.getEmail(),
            user.getPassword(),
            user.getFirstName(),
            user.getLastName(),
            user.getAddress(),
            user.getPhoneNumber()
            );
        Users newUser = usersRepository.save(userModel);
        cartsRepository.save(new Carts(newUser));
    }

    public void update(long id, UserUpdateDTO user) {
        Users userModel = usersRepository.findById(id).
        orElseThrow(
            () -> new CustomError(
                4004, 
                "Usuario no encontrado"
            )
        );
        if (user.getPassword() != null) {
            userModel.setPassword(user.getPassword());
        }
        if (user.getFirstName() != null) {
            userModel.setFirstName(user.getFirstName());
        }
        if (user.getLastName() != null) {
            userModel.setLastName(user.getLastName());
        }
        if (user.getAddress() != null) {
            userModel.setAddress(user.getAddress());
        }
        if (user.getPhoneNumber() != null) {
            userModel.setPhoneNumber(user.getPhoneNumber());
        }

        usersRepository.save(userModel);
    }

    public void delete(long id) {
        Users userModel = usersRepository.findById(id).
        orElseThrow(
            () -> new CustomError(
                4004, 
                "Usuario no encontrado"
            )
        );
        userModel.setActive(false);
        usersRepository.save(userModel);
    }

    public void login(String email, String password) {
        Users userModel = usersRepository.findByEmailAndPassword(email, password);
        if (userModel == null) {
            throw new CustomError(4004, "Usuario o contraseña incorrectos");
        }
        if (!userModel.isActive()) {
            userModel.setActive(true);
            usersRepository.save(userModel);
        }
    }
}
