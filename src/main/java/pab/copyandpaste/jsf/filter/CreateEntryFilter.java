package pab.copyandpaste.jsf.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import pab.copyandpaste.model.Entry;
import pab.copyandpaste.model.dao.EntryDao;

/**
 * This class intercepts the requests, and if they end with /, it creates an Entry in database,
 * and redirects to /createdId.
 * 
 * From there, pretty-faces turns that /createdId into something like ?id=createdId.
 * 
 * https://stackoverflow.com/questions/8480100/how-implement-a-login-filter-in-jsf
 * 
 * @author pab
 */
@WebFilter("*")
public class CreateEntryFilter implements Filter {

	private Log logger = LogFactory.getLog(CreateEntryFilter.class);
	
	private EntryDao entryDao;

	@Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws ServletException, IOException {    
        
    	HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        
        String url = request.getRequestURL().toString();
        String queryString = request.getQueryString();
        
        if(url!=null&&url.lastIndexOf("/")>0&&
        		url.lastIndexOf("/")==url.length()-1&&
        		(queryString==null||queryString.isEmpty())) {
        	
        	logger.info("A request with no entry id was found. Will try to add entry in database");
        	
        	Entry newEntry = this.addEntry();
        	
        	logger.info("Entry with id [" + newEntry.getId() + "] created. Will redirect to /" + newEntry.getId());
        	
        	String idStr = "" + newEntry.getId();
        	
        	response.sendRedirect(idStr); // So, just perform standard synchronous redirect.
        	
        } else {
        	
        	chain.doFilter(request, response);
        }
    }

//	Not used for now. 
//    private String _obscureId(Integer id) {
//    	return Base64.getEncoder().encodeToString(id.toString().getBytes());
//    }

	/**
	 * Adds an entry to the database, and returns its id as a base64 string.
	 * 
	 * This is used as soon as the page is loaded, to add the id of the entry to the URL
	 * of the page. 
	 * 
	 * Note: maybe not the best way to do it.
	 * 
	 * @return
	 */
	public Entry addEntry() {
		Entry newEntry = new Entry();
		this.entryDao = new EntryDao();
		this.entryDao.addEntry(newEntry);
		return newEntry;
	}
	
    
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
}
