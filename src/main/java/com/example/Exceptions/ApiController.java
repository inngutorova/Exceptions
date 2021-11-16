package com.example.Exceptions;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
@RestController
public class ApiController {

     private HashMap<String,User> users = new HashMap<>();

    // curl -X GET http://localhost:8080/users
    @GetMapping("users")
    public HashMap<String,User> getUsers() {
        return users;
    }

    // curl -X GET http://localhost:8080/users/jack
    @GetMapping("users/{name}")
    public User getUser(@PathVariable("name") String name) {
        User result = null;
        try{
            result = users.get(name);
            if(result == null) {
                throw new Exception404();
            }
        }
        catch (Exception404 e) {
            System.out.println("erroe 404");
        }
        return result;
    }

    // curl -X POST http://localhost:8080/users -H 'Content-Type:application/json' -d '{"username" : "Inna", "password" : "pass", "age" : "17"}'
    @PostMapping("users")
    public void addUser(@RequestBody User user) {
        try {
            for (int i = 0; i < user.getUsername().length(); i++) {
                char c = user.getUsername().charAt(i);
                if (c < 48 || (c > 57 && c < 65) || (c > 90 && c < 95) || c > 122) {
                    throw new Exception400();
                }
            }
                if(users.containsKey(user.getUsername())) {
                    throw new Exception409();
                }

            users.put(user.getUsername(),user);
        } catch (Exception400 e) {
            System.out.println("error400");
        }
        catch (Exception409 e) {
            System.out.println("error409");
        }
}

    // curl -X DELETE http://localhost:8080/users/jack
    @DeleteMapping("users/{name}")
    public void deleteUser(@PathVariable("name") String name) {
        try {
            String cut = name.substring(0, 5);
            if (!cut.equals("admin")) {
                throw new Exception403();
            }

            if(!users.containsKey(name)) {
                throw new Exception404();
            }
            users.remove(name);
        } catch (Exception403 e ) {
            System.out.println("error 404");
        }
    }

    // curl -X PUT http://localhost:8080/users/0 -H 'Content-Type: application/json' -d '13'
    @PutMapping("users/{name}")
    public void updateUser(@PathVariable("name")  String name, @RequestBody int age) {
        try {
            if(!users.containsKey(name)) {
                throw new Exception404();
            }
            String cut = name.substring(0, 6);
            System.out.println(cut);
            if (!cut.equals("update")) {
                throw new Exception403();
            }
            for (int i = 0; i < name.length(); i++) {
                char c = name.charAt(i);
                if (c < 48 || (c > 57 && c < 65) || (c > 90 && c < 95) || c > 122) {
                    throw new Exception400();
                }
            }
            users.get(name).setAge(age);
        } catch (Exception400 e) {
            System.out.println("error 400");
        }
        catch (Exception403 e) {
            System.out.println("error 403");
        }
        }



    class Exception400 extends ResponseStatusException {
        public Exception400() {
            super(HttpStatus.BAD_REQUEST, "bad request");
        }
    }

    class Exception403 extends ResponseStatusException {
        public Exception403() {
            super(HttpStatus.FORBIDDEN);
        }
    }

    class Exception404 extends ResponseStatusException {
    public Exception404() {
        super(HttpStatus.NOT_FOUND, "not found");
    }
}

    class Exception409 extends ResponseStatusException {
        public Exception409() {
            super(HttpStatus.CONFLICT, "conflict");
        }
    }





}