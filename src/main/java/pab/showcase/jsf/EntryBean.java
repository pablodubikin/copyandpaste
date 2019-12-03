package pab.showcase.jsf;

import java.io.IOException;
import java.io.Serializable;
import java.util.Base64;

import javax.annotation.PostConstruct;
import javax.faces.annotation.FacesConfig;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.lifecycle.ClientWindow;
import javax.faces.push.Push;
import javax.faces.push.PushContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.richfaces.json.JSONException;
import org.richfaces.json.JSONObject;

import pab.showcase.jsf.ui.ShoulISave;
import pab.showcase.model.Entry;
import pab.showcase.model.dao.EntryDao;

@FacesConfig(version = FacesConfig.Version.JSF_2_3)
@Named
@ViewScoped
public class EntryBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Log logger = LogFactory.getLog(EntryBean.class);
	
	/**
	 * Current entry.
	 */
	private Entry entry;
	
	/**
	 * Entry's data access object.
	 */
	private EntryDao entryDao;
	
	/**
	 * A class to control when an update in db needs to be made.
	 */
	private ShoulISave shouldISave;
	
	/**
	 * Session id, to prevent updating the user's own session.
	 */
	private String sSId;
	
	/**
	 * Date when the document.ready is performed. This way, we can update entries that are
	 * on the same session, but in different tabs.
	 */
	private String tabId;
	
	@Inject
    @Push
    private PushContext entryChannel;
	
	public EntryBean() {
		
		logger.info("Creating Entry Bean.");
		this.shouldISave = new ShoulISave();
		this.entry = new Entry();
		this.entryDao = new EntryDao();
		
		this._enableClientWindow();
	}
	
	/**
	 * Gets the id of the entry from the url, sets this.sSId, and loads this.entry from database.
	 */
	@PostConstruct
	public void loadRecordFromView() {
		
		logger.info("About to load record from view.");
		
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
		        .getRequest();
		
		this.sSId = ((HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false)).getId();
		
		String idStr = request.getParameter("id");
		Integer id = null;
		
		try {
			id = Integer.parseInt(idStr);
		} catch(NumberFormatException e) {
			System.out.println(e.getMessage());
		}
		
		if(id!=null) {
			
			logger.info("Id retrieved from URL [" + id + "]");
			
			this._loadRecord(id);
		}
	}
	
	/**
	 * If an entry exists in the database with @param id, load it into this.entry.
	 * 
	 * If not, redirect to home, to create a new entry.
	 */
	private void _loadRecord(Integer id) {
		
		logger.info("About to retrieve entry with id [" + this.entry.getId() + "] from database");
		
		this.entry = this.entryDao.findOne(id);
		
		if(this.entry==null) {
			
			logger.error("Entry could NOT be retrieved from database. Will redirect to create new one.");
			
			ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
			try {
				context.redirect("/");
			} catch (IOException e2) {
				e2.printStackTrace();
				logger.error("Could NOT redirect for entry with id [" + this.entry.getId() + "]");
			}
		}
		
	}
	
	/**
	 * Updates this.entry.content, sends a message to all connected users, and, if this.shouldISave
	 * says so, update the object in database.
	 * 
	 * @param newContent: the updated content.
	 */
	public void updateContent(String newContent) {
		
		logger.info("About to update entry with id [" + this.entry.getId() + "]'s content, and send message");
		
		this.entry.setContent(newContent);
		
		this.sendMessage();
		
		logger.info("Message sent for entry with id [" + this.entry.getId() + "]");
		
		if(this.shouldISave.shouldISave(this.entry.getContent(), newContent)) {

			this.updateEntry();
			
			logger.info("Entry with id [" + this.entry.getId() + "] updated in database");
		}
	}
	
	/**
	 * Update the entry's text in database.
	 */
	public void updateEntry() {
		
		logger.info("About to update entry with id [" + this.entry.getId() + "] in db.");
		
		this.entryDao.updateEntry(this.entry);

	}

	/**
	 * Not used. Not sure if it's a good idea.
	 * 
	 * @returns this.entry.id encoded in base64
	 */
	public String getObscuredId() {
		try {
			return Base64.getEncoder().encodeToString(this.entry.getId().toString().getBytes());
		} catch(Exception e) {
			return null;
		}
	}

	/**
	 * Sends a message to all users connected.
	 */
	public void sendMessage() {
		
		logger.info("Sending message for entry with id [" + this.entry.getId() + "]");
        
		JSONObject jsonO = new JSONObject();
		try {
			jsonO.put("sessionId", this.getMySessionId() + this.getTabId());
			jsonO.put("newContent", this.entry.getContent());
			jsonO.put("entryId", this.entry.getId());
			
			this.sendPushMessage(jsonO.toString());
			
			logger.info("Message sent | " + jsonO.toString() + "| for entry with id [" + this.entry.getId() + "]");
			
		} catch (JSONException e) {
			e.printStackTrace();
			logger.error("Error sending message | " + jsonO.toString() + "| for entry with id [" + this.entry.getId() + "]");
		}
    }
	
	/**
	 * This sets this.tabId = clientWindow.id so that only the views which are NOT equal to it can be updated.
	 */
	public void _enableClientWindow() {
		
		logger.info("Enabling ClientWindow for entry with id [" + this.entry.getId() + "]");
		
		FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        ClientWindow clientWindow = externalContext.getClientWindow();

        if (clientWindow != null){
        	this.tabId = clientWindow.getId();
        	logger.info("client window id [" + clientWindow.getId() + "] for entry with id [" + this.entry.getId() + "]" );
        } 
	}
    
//	
//				GETTERS&&SETTERS!!	
//	
	
	public String getMySessionId() {
		return sSId;
	}

	public void setMySessionId(String sessionId) {
		this.sSId = sessionId;
	}
	
    private void sendPushMessage(String newContent) {
        entryChannel.send(newContent);
    }

	public String getsSId() {
		return sSId;
	}

	public void setsSId(String sSId) {
		this.sSId = sSId;
	}

	public Entry getEntry() {
		return entry;
	}

	public void setEntry(Entry currentEntry) {
		this.entry = currentEntry;
	}

	public String getTabId() {
		return tabId;
	}

	public void setTabId(String tabDate) {
		this.tabId = tabDate;
	}

	public PushContext getEntryChannel() {
		return entryChannel;
	}

	public void setEntryChannel(PushContext entryChannel) {
		this.entryChannel = entryChannel;
	}
}
