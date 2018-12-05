package com.stackroute.keepnote.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.stackroute.keepnote.exception.CategoryNotFoundException;
import com.stackroute.keepnote.model.Category;

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
public class CategoryDAOImpl implements CategoryDAO {

	/*
	 * Autowiring should be implemented for the SessionFactory.(Use
	 * constructor-based autowiring.
	 */
	@Autowired
	private SessionFactory sessionFactory;

	public CategoryDAOImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	/*
	 * Create a new category
	 */
	public boolean createCategory(Category category) {

		boolean operationFlag = true;

		try {

			getSession().save(category);

		} catch (ConstraintViolationException exception) {

			operationFlag = false;

		}

		return operationFlag;
	}

	/*
	 * Remove an existing category
	 */
	public boolean deleteCategory(int categoryId) {

		boolean operationFlag = false;

		List<Category> categoryList = getSession().createQuery("from Category").list();

		for (Category tempCategory : categoryList) {

			if (tempCategory.getCategoryId() == categoryId) {

				getSession().remove(tempCategory);
				operationFlag = true;
				break;

			}
		}

		return operationFlag;
	}

	/*
	 * Update an existing category
	 */
	public boolean updateCategory(Category category) {
		boolean operationFlag = true;

		try {

			getSession().save(category);

		} catch (ConstraintViolationException exception) {

			operationFlag = false;

		}

		return operationFlag;
	}

	/*
	 * Retrieve details of a specific category
	 */
	public Category getCategoryById(int categoryId) throws CategoryNotFoundException {

		Category categoryRecord = getSession().find(Category.class, categoryId);

		if (categoryRecord == null) {

			throw new CategoryNotFoundException("Category record not found");

		}

		return categoryRecord;
	}

	/*
	 * Retrieve details of all categories by userId
	 */
	public List<Category> getAllCategoryByUserId(String userId) {
		return getSession().createQuery("from Category where CATEGORY_CREATOR = :userId").setParameter("userId", userId)
				.list();
	}
}
