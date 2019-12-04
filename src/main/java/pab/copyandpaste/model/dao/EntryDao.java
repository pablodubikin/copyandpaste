package pab.copyandpaste.model.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.joda.time.DateTime;

import pab.copyandpaste.hibernate.HibernateUtil;
import pab.copyandpaste.model.Entry;

/**
 * @author https://examples.javacodegeeks.com/enterprise-java/jsf/jsf-hibernate-crud-example/
 */
public class EntryDao {

	private Log logger = LogFactory.getLog(EntryDao.class);
	private static Transaction transObj;
    private static Session sessionObj = HibernateUtil.getSessionFactory().openSession();
    
    /**
     * Finds an Entry by id in database.
     * 
     * @param id - the id of the entry you're looking for.
     * 
     * @returns the entry.
     */
    public Entry findOne(Integer id) {
    	
    	logger.info("About to get entry [" + id + "] from database.");
    	Entry entry = null;
    	try {
            
    		transObj = sessionObj.beginTransaction();
            entry = (Entry) sessionObj.find(Entry.class, id);
            
            if(entry!=null&&entry.getId()!=null)
            	logger.info("Entry with id [" + entry.getId() + "] successfully retrieved from database");
            
        } catch (Exception exceptionObj) {
            exceptionObj.printStackTrace();
            logger.info("Exception! Trying to retrieve entry with id [" + entry.getId() + "] from database");
        } finally {
            transObj.commit();
        }
    	return entry;
    }
 
    /**
     * Add new Entry in database.
     *  
     * @param entry - the entry to be added. The entry has to have id = null. After this method gets executed, 
     * the entry will have an id.
     */
    public void addEntry(Entry entry) {
    	
    	logger.info("About to add entry in database.");
        try {
        	entry.setAddDate(new DateTime());
        	
            transObj = sessionObj.beginTransaction();
            sessionObj.save(entry);
            
            logger.info("Entry with id [" + entry.getId() + "] successfully created in database");
 
        } catch (Exception exceptionObj) {
            exceptionObj.printStackTrace();
            logger.error("Exception! Trying to add entry with id [" + entry.getId() + "] in database");
        } finally {
            transObj.commit();
        }
    }
    
    /**
     * Updates the full entry object in database.
     * 
     * @param entry - the entry to be updated.
     */
    public void updateEntry(Entry entry) {
    	
    	logger.info("About to update entry with id [" + entry.getId() + "]in database.");
    	try {
        	entry.setModDate(new DateTime());
        	
            transObj = sessionObj.beginTransaction();
            sessionObj.update(entry);
            logger.info("Entry with id [" + entry.getId() + "] successfully updated in database");
 
        } catch (Exception exceptionObj) {
            exceptionObj.printStackTrace();
            logger.error("Exception! Trying to update entry with id [" + entry.getId() + "] in database");
        } finally {
            transObj.commit();
        }
    }
}
