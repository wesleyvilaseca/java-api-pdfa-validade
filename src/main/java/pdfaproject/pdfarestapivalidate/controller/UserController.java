package pdfaproject.pdfarestapivalidate.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import pdfaproject.pdfarestapivalidate.model.UserModel;
import pdfaproject.pdfarestapivalidate.repository.UserRepository;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping(path = "/api/user/all")
    public List<UserModel> all() {
        return userRepository.findAll();
    }

    @PostMapping(path = "/api/user/save")
    @ResponseStatus(HttpStatus.CREATED)
    public UserModel save(@RequestBody UserModel user) {
        return userRepository.save(user);
    }

    @GetMapping(path = "/api/user/{id}")
    public UserModel find(@PathVariable Long id) {
        return userRepository.findById(id).get();
    }

    @DeleteMapping(path = "/api/user/{id}")
    public void delete(@PathVariable Long id) {
        userRepository.deleteById(id);
    }
}
