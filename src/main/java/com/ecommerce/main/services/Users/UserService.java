// filepath: /home/giu/dev/javaCode/ecommerce-spring-api/spring-api/src/main/java/com/example/main/services/Users/UserService.java
package com.ecommerce.main.services.Users;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ecommerce.main.domain.models.Carts;
import com.ecommerce.main.domain.models.Users;
import com.ecommerce.main.domain.repositories.CartsRepository;
import com.ecommerce.main.domain.repositories.UsersRepository;
import com.ecommerce.main.error.CustomError;
import com.ecommerce.main.services.Users.dto.UserCreateDTO;
import com.ecommerce.main.services.Users.dto.UserGetDTO;
import com.ecommerce.main.services.Users.dto.UserLoginDTO;
import com.ecommerce.main.services.Users.dto.UserUpdateDTO;
import com.ecommerce.web.utils.JwtUtil;

import org.mindrot.jbcrypt.BCrypt;

/**
 * UserService
 * esta clase se encarga de manejar la lógica de negocio relacionada con los usuarios
 * contiene métodos para obtener, crear, actualizar y eliminar usuarios
 * también contiene métodos para autenticar usuarios y generar tokens JWT
 * implementa la interfaz UserDetailsService de Spring Security
 * para poder autenticar usuarios en la aplicación
 */

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private final UsersRepository usersRepository;

    @Autowired
    private final CartsRepository cartsRepository;

    @Autowired
    private JwtUtil jwtUtil;

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

    public List<Users> getAllEntity() {
        List<Users> users = usersRepository.findAll();
        if (users.isEmpty()) {
            throw new CustomError(4004, "No hay usuarios registrados");
        }
        return users;
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
        user.hashPassword();
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
        user.hashPassword();
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

    public String login(UserLoginDTO user) {
        Users userModel = usersRepository.findByEmail(user.getEmail());
        if (userModel == null || !BCrypt.checkpw(user.getPassword(), userModel.getPassword())) {
            throw new CustomError(4004, "Usuario o contraseña incorrectos");
        }
        if (!userModel.isActive()) {
            userModel.setActive(true);
            usersRepository.save(userModel);
        }
        return jwtUtil.generateToken(userModel.getEmail(), userModel.getId());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = usersRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }
        return new User(user.getEmail(), user.getPassword(), new ArrayList<>());
    }

    public void populateDatabaseWithUsers() {
        UserCreateDTO user1 = new UserCreateDTO(
            "user1@example.com",
            "Password1",
            "FirstName1",
            "LastName1",
            "Address1",
            "1234567890"
        );
        UserCreateDTO user2 = new UserCreateDTO(
            "user2@example.com",
            "Password2",
            "FirstName2",
            "LastName2",
            "Address2",
            "0987654321"
        );
        UserCreateDTO user3 = new UserCreateDTO(
            "user3@example.com",
            "Password3",
            "FirstName3",
            "LastName3",
            "Address3",
            "1122334455"
        );
    
        this.create(user1);
        this.create(user2);
        this.create(user3);
    }
}