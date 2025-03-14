package nl.fontys.s3.ticketmaster.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import nl.fontys.s3.ticketmaster.business.interfaces.userinterfaces.*;
import nl.fontys.s3.ticketmaster.domain.user.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:5173")
@AllArgsConstructor
public class UserController {
    private final GetUserUseCase getUserUseCase;
    private final GetAllUsersUseCase getAllUsersUseCase;
    private final DeleteUserUseCase deleteUserUseCase;
    private final CreateUserUseCase createUserUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final RegisterUserUseCase registerUserUseCase;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasPermission(#id, 'read')")
    public ResponseEntity<User> getUserById(@PathVariable("id") final long id,
                                            @AuthenticationPrincipal UserDetails userDetails) {
        final Optional<User> userResponseOptional = getUserUseCase.getUser(id);
        if (userResponseOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userResponseOptional.get());
    }



    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<GetAllUsersResponse> getAllUsers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Role role,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        GetAllUsersRequest request = GetAllUsersRequest.builder()
                .name(name)
                .email(email)
                .role(role)
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .build();
        GetAllUsersResponse response = getAllUsersUseCase.getAllUsers(request);
        return ResponseEntity.ok(response);
    }



    @DeleteMapping("{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable long userId) {
        deleteUserUseCase.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<CreateUserResponse> createUser(@RequestBody @Valid CreateUserRequest request) {
        CreateUserResponse response = createUserUseCase.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasPermission(#id, 'update')")
    public ResponseEntity<Void> updateUser(@PathVariable("id") long id,
                                           @RequestBody @Valid UpdateUserRequest request,
                                           @AuthenticationPrincipal UserDetails userDetails) {
        request.setId(id);
        updateUserUseCase.updateUser(request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/register")
    public ResponseEntity<CreateUserResponse> registerUser(@RequestBody @Valid RegisterUserRequest request) {
        CreateUserResponse response = registerUserUseCase.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
