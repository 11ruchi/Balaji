package com.stackroute.keepnote.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.stackroute.keepnote.exception.UserAlreadyExistException;
import com.stackroute.keepnote.exception.UserNotFoundException;
import com.stackroute.keepnote.model.User;
import com.stackroute.keepnote.service.UserService;

/*
 * As in this assignment, we are working on creating RESTful web service, hence annotate
 * the class with @RestController annotation. A class annotated with the @Controller annotation
 * has handler methods which return a view. However, if we use @ResponseBody annotation along
 * with @Controller annotation, it will return the data directly in a serialized 
 * format. Starting from Spring 4 and above, we can use @RestController annotation which 
 * is equivalent to using @Controller and @ResposeBody annotation
 */
@RestController
public class UserController {

	/*
	 * Autowiring should be implemented for the UserService. (Use Constructor-based
	 * autowiring) Please note that we should not create an object using the new
	 * keyword
	 */
	@Autowired
	private UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	/*
	 * Define a handler method which will create a specific user by reading the
	 * Serialized object from request body and save the user details in a User table
	 * in the database. This handler method should return any one of the status
	 * messages basis on different situations:
	 * 
	 * 1. 201(CREATED) - If the user created successfully.
	 * 
	 * 2. 409(CONFLICT) - If the userId conflicts with any existing user
	 * 
	 * Note: ------ This method can be called without being logged in as well as
	 * when a new user will use the app, he will register himself first before
	 * login.
	 * 
	 * This handler method should map to the URL "/user/register" using HTTP POST
	 * method
	 */
	@PostMapping("/user/register")
	public ResponseEntity<?> registerUser(@RequestBody User user) {

		HttpStatus responseStatus = null;

		try {

			userService.registerUser(user);
			responseStatus = HttpStatus.CREATED;

		} catch (UserAlreadyExistException exception) {

			responseStatus = HttpStatus.CONFLICT;
		}

		return new ResponseEntity<>(responseStatus);
	}

	/*
	 * Define a handler method which will update a specific user by reading the
	 * Serialized object from request body and save the updated user details in a
	 * user table in database handle exception as well. This handler method should
	 * return any one of the status messages basis on different situations:
	 * 
	 * 1. 200(OK) - If the user updated successfully.
	 * 
	 * 2. 404(NOT FOUND) - If the user with specified userId is not found.
	 * 
	 * 3. 401(UNAUTHORIZED) - If the user trying to perform the action has not
	 * logged in.
	 * 
	 * This handler method should map to the URL "/user/{id}" using HTTP PUT method.
	 */
	@PutMapping("/user/{id}")
	public ResponseEntity<User> updateUser(@PathVariable("id") String userId, @RequestBody User user,
			HttpSession httpsession) {

		String validUserId = (String) httpsession.getAttribute("loggedInUserId");

		HttpStatus responseStatus = HttpStatus.UNAUTHORIZED;

		User updatedUser = null;
		
		if (!StringUtils.isEmpty(validUserId)) {

			try {

				updatedUser = userService.updateUser(user, userId);

				if (updatedUser != null) {
					
					responseStatus = HttpStatus.OK;
					
				} else {
					
					responseStatus = HttpStatus.NOT_FOUND;
					
				}

			} catch (Exception exception) {

				responseStatus = HttpStatus.NOT_FOUND;
			}
		}

		return new ResponseEntity<>(responseStatus);
	}

	/*
	 * Define a handler method which will delete a user from a database.
	 * 
	 * This handler method should return any one of the status messages basis on
	 * different situations:
	 * 
	 * 1. 200(OK) - If the user deleted successfully from database.
	 * 
	 * 2. 404(NOT FOUND) - If the user with specified userId is not found.
	 * 
	 * 3. 401(UNAUTHORIZED) - If the user trying to perform the action has not
	 * logged in.
	 * 
	 * This handler method should map to the URL "/user/{id}" using HTTP Delete
	 * method" where "id" should be replaced by a valid userId without {}
	 */
	@DeleteMapping("/user/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable("id") String userId, HttpSession httpsession) {

		String validUserId = (String) httpsession.getAttribute("loggedInUserId");

		HttpStatus responseStatus = HttpStatus.UNAUTHORIZED;

		if (!StringUtils.isEmpty(validUserId)) {

			if (userService.deleteUser(userId)) {

				responseStatus = HttpStatus.OK;

			} else {

				responseStatus = HttpStatus.NOT_FOUND;
			}
		}

		return new ResponseEntity<>(responseStatus);
	}

	/*
	 * Define a handler method which will show details of a specific user handle
	 * UserNotFoundException as well. This handler method should return any one of
	 * the status messages basis on different situations:
	 * 
	 * 1. 200(OK) - If the user found successfully.
	 * 
	 * 2. 401(UNAUTHORIZED) - If the user trying to perform the action has not
	 * logged in.
	 * 
	 * 3. 404(NOT FOUND) - If the user with specified userId is not found.
	 * 
	 * This handler method should map to the URL "/user/{id}" using HTTP GET method
	 * where "id" should be replaced by a valid userId without {}
	 */
	@GetMapping("/user/{id}")
	public ResponseEntity<User> getUserDetails(@PathVariable("id") String userId, HttpSession httpsession) {

		String validUserId = (String) httpsession.getAttribute("loggedInUserId");

		HttpStatus responseStatus = HttpStatus.UNAUTHORIZED;

		User userDetails = null;

		if (!StringUtils.isEmpty(validUserId)) {

			try {

				userDetails = userService.getUserById(userId);

				if (userDetails != null) {
					responseStatus = HttpStatus.OK;
				} else {
					responseStatus = HttpStatus.NOT_FOUND;
				}

			} catch (UserNotFoundException exception) {

				responseStatus = HttpStatus.NOT_FOUND;
			}
		}

		return new ResponseEntity<>(userDetails, responseStatus);
	}
}