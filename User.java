import java.io.IOException;

import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.*;

public class User implements finder{
      private Long userId;
      private String name;
      private String usertype;
      private Double ConsistencyCheckProbability;
      private Long totalNumberofInstance=(long)0;
      private ArrayList<Instancee>labeled=new ArrayList<Instancee>();
      private ArrayList<LabelAssignment>labeled2=new ArrayList<LabelAssignment>();
      private Long checkconsistency=(long)0;
      private ArrayList<Long>time=new ArrayList<Long>();
      private Long Numofuniqueins=(long)0;
      private double consistency=0.0;
      private double average;
      private ArrayList<Dataset>dataset=new ArrayList<Dataset>();
      
	public User() {
		// TODO Auto-generated constructor stub
	}

	public User(long userId,String name,String usertype,Double ConsistencyCheckProbability ) {
		this.setUserId(userId);
		this.setName(name);
		this.setUsertype(usertype);
		setConsistencyCheckProbability(ConsistencyCheckProbability);
	}
	
	//Print user
public void printUser(Logger logger) {
		logger.info("user id: "+this.getUserId()+ " username: "+this.getName()+" user type: "+ this.getUsertype()+" user ConsistencyCheckProbability :" +this.getConsistencyCheckProbability()+ " is created.");
	}
	
	public  boolean find(ArrayList<User>array,User user) {
		   if(array.contains(user)) {
			   return true;
		   }
		   else {
			   return false;
		   }
	}

	public void addInstance(Instancee x) {
		this.labeled.add(x);
	}
	
	
	public boolean 	existbefore(LabelAssignment two) {
		for(int c=0;c<this.getLabeled2().size();c++) {
			if(this.getLabeled2().get(c).getInstance().getInstanceid()==two.getInstance().getInstanceid()&&this.getLabeled2().get(c).getLabels().getLabelid()==two.getLabels().getLabelid()) {
			
				return true;
			}
	
		}
		return false;

	}
	
	public long average() {
		if(this.totalNumberofInstance==0) {
			return 0;
		}else {
			long sum=0;
			for(int i=0;i<this.time.size();i++) {
				sum=time.get(i);
			}
			return sum/this.totalNumberofInstance;
		}
	}

	public double stdeviation() {
		if(this.totalNumberofInstance==0 || this.totalNumberofInstance==1) {
			return 0;
		}
		
		else {
			long mean =this.average();
			double sum=0;
			for(int i=0;i<this.time.size();i++) {
				sum=Math.pow((time.get(i)-mean), 2);
			}
			double std=Math.sqrt((sum/(this.totalNumberofInstance-1)));
			return std;}
	}
	
	   //find instances according to their ids
		public int find(ArrayList<User>listOfuser,ArrayList<Label>listlabel,ArrayList<Instancee>listinstance, Long userid) {
			for(int c=0;c<listOfuser.size();c++) {
				if(userid==listOfuser.get(c).userId) {
					return c;
				}
			}
			return -1;
		}
	//////////////////////////////////////////////////////////// GETTERS & SETTERS
	   
	
	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUsertype() {
		return usertype;
	}

	public void setUsertype(String usertype) {
		this.usertype = usertype;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Instancee> getLabeled() {
		return labeled;
	}

	public void setLabeled(ArrayList<Instancee> labeled) {
		this.labeled = labeled;
	}


	public Double getConsistencyCheckProbability() {
		return ConsistencyCheckProbability;
	}

	public void setConsistencyCheckProbability(Double consistencyCheckProbability) {
		ConsistencyCheckProbability = consistencyCheckProbability;
	}

	

	public ArrayList<LabelAssignment> getLabeled2() {
		return labeled2;
	}

	public void setLabeled2(LabelAssignment labeled2) {
		this.labeled2.add(labeled2);
	}


	public ArrayList<Long> getTime() {
		return time;
	}

	public void setTime(Long  time) {
		this.time.add(time);
	}
	


	public Long getCheckconsistency() {
		return checkconsistency;
	}

	public void setCheckconsistency(Long checkconsistency) {
		if(checkconsistency==-1) {
			this.checkconsistency++;
		}else {
			this.checkconsistency = checkconsistency;
		}
	}

	public Long getTotalNumberofInstance() {
		return totalNumberofInstance;
	
	}

	public void setTotalNumberofInstance(Long totalNumberofInstance,Logger logger) {
		if(totalNumberofInstance==-1) {
			this.totalNumberofInstance++;
		}
		else {
			this.totalNumberofInstance = totalNumberofInstance;
		}
	//	logger.info("user id: " +this.userId + " number of total instance is calculated");
	}

	public Long getNumofuniqueins() {
		return Numofuniqueins;
	}

	public void setNumofuniqueins(Long numofuniqueins,Logger logger) {
		if(numofuniqueins==-1) {
			this.Numofuniqueins++;
		}
		else {
			this.Numofuniqueins=numofuniqueins;
		}		
	//	logger.info("user id: " + this.userId + " number of unique instance is calculated");
	}

	public double getConsistency() {
		return consistency;
	}

	public void setConsistency() {
		if(this.totalNumberofInstance==0) {
			this.consistency=0.0;
		}else {
			this.consistency=((double)this.checkconsistency/this.totalNumberofInstance)*100;
		}
	}

	public ArrayList<Dataset> getDataset() {
		return dataset;
	}

	public void setDataset(ArrayList<Dataset> dataset) {
		this.dataset = dataset;
	}

	public double getAverage() {

		return average;
	}

	public void setAverage(double average) {
		this.average = average;
	}


}//class
