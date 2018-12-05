package com.stackroute.keepnote.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.stackroute.keepnote.exception.UserNotFoundException;
import com.stackroute.keepnote.model.User;

/*
 * This class is implementing the UserDAO interface. This class has to be annotated with 
 * @Repository annotation.
 * @Repository - is an annotation that marks the specific class as a Data Access Object, 
 * thus clarifying it's role.
 * @Transactional - The transactional annotation itself defines the scope of a single database 
 * 					transaction. The database transaction happens inside the scope of a persistence 
 * 					context.  
 * */
@Repository
@Transactional
public class UserDaoImpl implements UserDAO {

	/*
	 * Autowiring should be implemented for the SessionFactory.(Use
	 * constructor-based autowiring.
	 */
	@Autowired
	private SessionFactory sessionFactory;

	public UserDaoImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	/*
	 * Create a new user
	 */
	public boolean registerUser(User user) {

		boolean operationFlag = true;

		try {

			getSession().save(user);

		} catch (ConstraintViolationException exception) {

			operationFlag = false;

		}

		return operationFlag;
	}

	/*
	 * Update an existing user
	 */
	public boolean updateUser(User user) {

		boolean operationFlag = true;

		try {

			getSession().save(user);

		} catch (ConstraintViolationException exception) {

			operationFlag = false;

		}

		return operationFlag;
	}

	/*
	 * Retrieve details of a specific user
	 */
	public User getUserById(String UserId) {

		return getSession().find(User.class, UserId);

	}

	/*
	 * validate an user
	 */

	public boolean validateUser(String userId, String password) throws UserNotFoundException {

		User userRecord = getUserById(userId);

		boolean operationFlag = false;

		if (userRecord == null) {

			throw new UserNotFoundException("User record not found");

		} else if (userRecord.getUserPassword().equalsIgnoreCase(password)) {

			operationFlag = true;

		}

		return operationFlag;

	}

	/*
	 * Remove an existing user
	 */
	public boolean deleteUser(String userId) {

		boolean operationFlag = false;

		List<User> userList = getSession().createQuery("from User").list();

		for (User tempUser : userList) {

			if (tempUser.getUserId().equalsIgnoreCase(userId)) {

				getSession().remove(tempUser);
				operationFlag = true;
				break;

			}
		}

		return operationFlag;
	}

}
