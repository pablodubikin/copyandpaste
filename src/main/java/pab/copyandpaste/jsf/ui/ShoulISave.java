package pab.copyandpaste.jsf.ui;

import org.joda.time.DateTime;

/**
 * This class has 3 properties,
 * 
 * 	- lastTimeSaved: the last time (DateTime) the string was updated in db.
 *  - numberOfChanges: the number of changes made to the string without updating the db.
 *  - lengthDifference: the difference between the lengths of the db string and the new string.
 *  
 * 
 * @author pab
 *
 */
public class ShoulISave {
	
	public static Integer MAX_SECONDS_BEFORE_SAVE = 3;
	public static Integer MAX_CHANGES_BEFORE_SAVE = 3;
	public static Integer MAX_LENGTH_BEFORE_SAVE = 3;
	
	private DateTime lastTimeSaved;
	
	private Integer numberOfChanges;
	
	public ShoulISave() {
		this._refresh();
	}

	public boolean shouldISave(String db, String memory) {
		
		if(lastTimeSaved.isBefore(new DateTime().minusSeconds(MAX_SECONDS_BEFORE_SAVE))) {
			this._refresh();
			return true;
		}
		
		if(numberOfChanges>MAX_CHANGES_BEFORE_SAVE) {
			this._refresh();
			return true;
		}
		
		if( db !=null && memory != null
				&& Math.abs( db.length() - memory.length()) > MAX_LENGTH_BEFORE_SAVE) {
			this._refresh();
			return true;
		}
		
		this.numberOfChanges++;
		
		return false;
	}
	
	private void _refresh() {
		this.lastTimeSaved = new DateTime();
		this.numberOfChanges = 0;
	}
}
