package gui.items.statuses;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;

import javax.swing.JTextPane;

import org.mapdb.Fun.Tuple3;
import org.mapdb.Fun.Tuple4;

import core.item.statuses.StatusCls;
import database.ItemStatusMap;
import database.DBSet;
import gui.MainFrame;
import lang.Lang;

// Info for status
public class Status_Info extends JTextPane {
	private static final long serialVersionUID = 476307470457045006L;	
	
	public  Status_Info() {
	
		this.setContentType("text/html");
		this.setBackground(MainFrame.getFrames()[0].getBackground());
		
	}
	
	
	static String Get_HTML_Status_Info_001(StatusCls status)
	{
		 
		String message;
		
		if (status == null) return message = "Empty Status";
		
		if (!status.isConfirmed()) {
			message = Lang.getInstance().translate("Not confirmed");
		} else {
			message = "" + status.getKey();
		}
		message = "<div><b>" + message + "</b> : " + status.getName().toString() + "</div>";
		
		message += "<div>" + status.getDescription() + "</div>";

		String creator = status.getCreator() == null? "GENESIS": status.getCreator().asPerson_01(false);

		message += "<div> Creator: " + (creator.length()==0?status.getCreator().getAddress():creator) + "</div>";

		return message;
	}
	 	
	public void show_001(StatusCls status){
		
		setText("<html>" + Get_HTML_Status_Info_001(status) + "</html>");
		
		return;
	}	

}
