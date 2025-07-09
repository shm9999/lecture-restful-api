package com.restfulapi.myrestfulservice.controller;

import com.restfulapi.myrestfulservice.bean.Post;
import com.restfulapi.myrestfulservice.bean.User;
import com.restfulapi.myrestfulservice.bean.UserWithCount;
import com.restfulapi.myrestfulservice.exception.UserNotFoundException;
import com.restfulapi.myrestfulservice.repository.PostRepository;
import com.restfulapi.myrestfulservice.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/jpa")
public class UserJPAController {

    private UserRepository userRepository;
    private PostRepository postRepository;

    public UserJPAController(UserRepository userRepository,  PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    @GetMapping("/users")
    public UserWithCount retrieveAllUsers() {
        List<User> users = userRepository.findAll();

        UserWithCount userWithCount = new UserWithCount();
        userWithCount.setCount(users.size());
        userWithCount.setUsers(users);

        return userWithCount;
    }

    @GetMapping("/users/{id}")
    public ResponseEntity retrieveUserById(@PathVariable int id) {
        Optional<User> user = userRepository.findById(id);

        if (!user.isPresent()) {
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        }

        EntityModel entityModel = EntityModel.of(user.get());

        WebMvcLinkBuilder linTo = linkTo(methodOn(this.getClass()).retrieveAllUsers());
        entityModel.add(linTo.withRel("all-users")); // http://localhost:8088/users -> all-users

        return ResponseEntity.ok(entityModel);
    }

    @DeleteMapping("/users/{id}")
    public void deleteUserById(@PathVariable int id) {
        userRepository.deleteById(id);
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        User savedUser = userRepository.save(user);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/users/{id}/posts")
    public List<Post> retrieveAllPostsByUserId(@PathVariable int id) {
        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent()) {
            throw new UserNotFoundException("id-" + id);
        }

        return user.get().getPosts();
    }

    @PostMapping("/users/{id}/posts")
    public ResponseEntity createPost(@PathVariable int id, @RequestBody Post post) {
        Optional<User> userOptional = userRepository.findById(id);
        if (!userOptional.isPresent()) {
            throw new UserNotFoundException("id=" + id);
        }

        User user = userOptional.get();

        post.setUser(user);

        postRepository.save(post);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(post.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

}