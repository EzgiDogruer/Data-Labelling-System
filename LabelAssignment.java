import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.Calendar;
import java.util.ArrayList;

public class LabelAssignment {
      private Instancee ınstance;
	  private User user;
      private Label labels;
      private String datetime ;
      
	public LabelAssignment() {
	}
 
		// TODO Auto-generated constructor stub
	
	public LabelAssignment (User user, Instancee instancee,Label labels) {
		this.setUser(user);
		this.setInstance(instancee);
		this.setLabels(labels);
		Date date = new Date();
   this.datetime =calculatedate(date);
	}
	
	public void printIds(Logger logger) {
		logger.info("instance id: "+this.getInstance().getInstanceid()+ " is labelled label id: "+this.getLabels().getLabelid());
	}
	
 
	public String calculatedate(Date date) {
		SimpleDateFormat dateFor = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
		String stringDate = dateFor.format(date);
		return stringDate;
	}
	
public void setAssignment(User user,LabelAssignment one) {
	if(user.existbefore(one)) {
		user.setCheckconsistency((long)-1);
	 }else {
		 user.setLabeled2(one);
	 }
}
	

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

	public Label getLabels() {
		return labels;
	}

	public void setLabels(Label labels) {
		this.labels = labels;
	}



	public Instancee getInstance() {
		return ınstance;
	}



	public void setInstance(Instancee instancee) {
		ınstance = instancee;
	}



	public User getUser() {
		return user;
	}



	public void setUser(User user) {
		this.user = user;
	}
	
}
