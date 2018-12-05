package com.stackroute.keepnote.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.stackroute.keepnote.exception.ReminderNotFoundException;
import com.stackroute.keepnote.model.Reminder;

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
public class ReminderDAOImpl implements ReminderDAO {

	/*
	 * Autowiring should be implemented for the SessionFactory.(Use
	 * constructor-based autowiring.
	 */
	@Autowired
	private SessionFactory sessionFactory;

	public ReminderDAOImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	/*
	 * Create a new reminder
	 */
	public boolean createReminder(Reminder reminder) {

		boolean operationFlag = true;

		try {

			getSession().save(reminder);

		} catch (ConstraintViolationException exception) {

			operationFlag = false;

		}

		return operationFlag;
	}

	/*
	 * Update an existing reminder
	 */
	public boolean updateReminder(Reminder reminder) {

		boolean operationFlag = true;

		try {

			getSession().save(reminder);

		} catch (ConstraintViolationException exception) {

			operationFlag = false;

		}

		return operationFlag;
	}

	/*
	 * Remove an existing reminder
	 */
	public boolean deleteReminder(int reminderId) {

		boolean operationFlag = false;

		List<Reminder> reminderList = getSession().createQuery("from Reminder").list();

		for (Reminder tempReminder : reminderList) {

			if (tempReminder.getReminderId() == reminderId) {

				getSession().remove(tempReminder);
				operationFlag = true;
				break;

			}
		}

		return operationFlag;
	}

	/*
	 * Retrieve details of a specific reminder
	 */
	public Reminder getReminderById(int reminderId) throws ReminderNotFoundException {

		Reminder reminderRecord = getSession().find(Reminder.class, reminderId);

		if (reminderRecord == null) {

			throw new ReminderNotFoundException("Reminder record not found");

		}

		return reminderRecord;
	}

	/*
	 * Retrieve details of all reminders by userId
	 */
	public List<Reminder> getAllReminderByUserId(String userId) {
		return getSession().createQuery("from Reminder where REMINDER_CREATOR = :userId").setParameter("userId", userId)
				.list();
	}
}
