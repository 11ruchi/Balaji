package com.stackroute.keepnote.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.stackroute.keepnote.exception.NoteNotFoundException;
import com.stackroute.keepnote.model.Note;

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
public class NoteDAOImpl implements NoteDAO {

	/*
	 * Autowiring should be implemented for the SessionFactory.(Use
	 * constructor-based autowiring.
	 */
	@Autowired
	private SessionFactory sessionFactory;

	public NoteDAOImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	/*
	 * Create a new note
	 */
	public boolean createNote(Note note) {

		boolean operationFlag = true;

		try {

			getSession().save(note);

		} catch (ConstraintViolationException exception) {

			operationFlag = false;

		}

		return operationFlag;
	}

	/*
	 * Remove an existing note
	 */
	public boolean deleteNote(int noteId) {
		boolean operationFlag = false;

		List<Note> noteList = getSession().createQuery("from Note").list();

		for (Note tempNote : noteList) {

			if (tempNote.getNoteId() == noteId) {

				getSession().remove(tempNote);
				operationFlag = true;
				break;

			}
		}

		return operationFlag;
	}

	/*
	 * Retrieve details of all notes by userId
	 */
	public List<Note> getAllNotesByUserId(String userId) {
		return getSession().createQuery("from Note where NOTE_CREATOR = :userId").setParameter("userId", userId).list();
	}

	/*
	 * Retrieve details of a specific note
	 */
	public Note getNoteById(int noteId) throws NoteNotFoundException {

		Note noteRecord = getSession().find(Note.class, noteId);

		if (noteRecord == null) {

			throw new NoteNotFoundException("Note record not found");

		}

		return noteRecord;
	}

	/*
	 * Update an existing note
	 */

	public boolean UpdateNote(Note note) {

		boolean operationFlag = true;

		try {

			getSession().save(note);

		} catch (ConstraintViolationException exception) {

			operationFlag = false;

		}

		return operationFlag;
	}
}
