import java.util.*;

import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


public class Instancee implements finder {
      private long id;
      private String instance;
      private Integer totalNumberOfLabelAssignment = 0;
      private ArrayList<User> uniqueUsers = new ArrayList<>(); // store of unique users who labeled for each instance
      private ArrayList<Integer> countOfLabel = new ArrayList<>();// store of each label counts for each instance
      private ArrayList<Integer> percentageOfLabel = new ArrayList<>();// store of label percantage for each instance
      private ArrayList<Integer> countOfLabelforHumanUsers = new ArrayList<>();

	public Instancee() {
  		// TODO Auto-generated constructor stub
  	}
  	
  	public Instancee(long id,String text ) {
  		this.setInstanceid(id);
  		this.instance=text;
  		
  	}
  	//calculating entropy
  	public double entropy( Logger logger) {
  		double result=0.0;
  		double divide;
  		double countOfLabel;
  		double totalNumberOfLabelAssignment;
  		
  		double uniqueLabel= calculateTotalNumberOfUniqueLabel(logger);
  		if(uniqueLabel!=0&&uniqueLabel!=1&& this.totalNumberOfLabelAssignment!=0) {
  		  for(int i=0; i<this.countOfLabel.size(); i++) {
  			
  			if(this.countOfLabel.get(i)!=0) {
  			countOfLabel=this.countOfLabel.get(i);
  		  	totalNumberOfLabelAssignment=this.totalNumberOfLabelAssignment;
  			divide=countOfLabel/totalNumberOfLabelAssignment;
  			result-= divide* (Math.log10(divide) / Math.log10(uniqueLabel)) ;
  			}
  		}
  		
  		}
  		//logger.info("instance id: "+this.getInstanceid()+ " entropy is calculated : "+result);
  		return result;
  			
  	}
 
  	// assign 0 to percentageOfLabel list in the beginning
  	public void createListForLabelPercentage( Integer numberOfLabel)
  	{
  		for(int i=0; i<numberOfLabel ;i++) {
  			this.percentageOfLabel.add(0);}
  	}
  	// calculating percentage of each labels into percentageOfLabel array
  	public void percentageOfLabels(Logger logger) {
  		if(this.totalNumberOfLabelAssignment!=0) {
  		Integer numberOfLabelled;
  		for(int i=0; i<this.percentageOfLabel.size() ; i++) {
  			numberOfLabelled = this.countOfLabel.get(i);
  	  		Integer percentage = (numberOfLabelled*100) / this.totalNumberOfLabelAssignment;
  	  		this.percentageOfLabel.set(i, percentage);
  		}	
  		}	
  		//logger.info("instance id: "+this.getInstanceid()+ " class labels and percentages are calculated " );
  	}	
  	
  	//finding Max Frequent Label
  	public Label findMaxFrequentLabel(ArrayList<Label> allLabels,Logger logger) {
  		Integer index = this.countOfLabel.indexOf(Collections.max(this.countOfLabel));
  		//logger.info("instance id: "+this.getInstanceid()+ "  most frequent class label  is calculated : " +allLabels.get(index).getLabelid() );
  		return allLabels.get(index);	
  	}
  	
  	
    //finding Max Frequent Label percentage
  	public Integer percentageOfMaxLabel(Logger logger) {
  		if(this.totalNumberOfLabelAssignment!=0) {
  		   Integer numberOfLabelled=Collections.max(this.countOfLabel);
  		   Integer percentage = (numberOfLabelled*100) / this.totalNumberOfLabelAssignment;
  		   //logger.info("instance id: "+this.getInstanceid()+ "  most frequent class label percentage is calculated\n"+percentage );
  		   return percentage;	
  		}
  		else {
  		
  			return 0;
  		}	
  	}
  	// adding unique user into uniqueUsers list 
  	public void checkUniqueUsers( User user , Logger logger) {
  		if(this.uniqueUsers.size()==0) {
  			this.uniqueUsers.add(user);
  		}
  		else if(!this.uniqueUsers.contains(user)) {
  				this.uniqueUsers.add(user);
  		}
  		//logger.info("instance id: "+this.getInstanceid()+ "  number of unique users is calculated " );
  	}
  	// calculating Total Number Of Unique Label
	public Integer calculateTotalNumberOfUniqueLabel( Logger logger) {
		
  		Integer count=0;
  		for(int i=0; i<this.countOfLabel.size(); i++)
  			if(this.countOfLabel.get(i)!=0)
  				count++;
  		//logger.info("instance id: "+this.getInstanceid()+ " total number of unique label is calculated "+count );
  		return count;
  	}
	
	
  	//calculating Total Number Of Label Assignment
  	public void calculateTotalNumberOfLabelAssignment( Logger logger) {
  		this.totalNumberOfLabelAssignment++;
  		//logger.info("instance id: "+this.getInstanceid()+ " total number of label assigmnet is calculated : " + this.totalNumberOfLabelAssignment);
  		
  		
  	}
 // assign 0 to countOfLabel list in the beginning
  	public void createListForLabelCount(Integer numberOfLabel)
  	{
  		for(int i=0; i<numberOfLabel ;i++) {
     		this.countOfLabel.add(0);}
  	}
  	
  	// increase count of label
	public void setLabelCount( Integer id) {
		this.countOfLabel.set(id-1, this.countOfLabel.get(id-1)+1);
		
	}
	 // assign 0 to countOfLabel list in the beginning
  	public void createListForLabelCountHU(Integer numberOfLabel)
  	{
  		for(int i=0; i<numberOfLabel ;i++) {
     		this.countOfLabelforHumanUsers.add(0);}
  	}
  	
  	// increase count of label
	public void setLabelCountHU( Integer id) {
		this.countOfLabelforHumanUsers.set(id-1, this.countOfLabelforHumanUsers.get(id-1)+1);
		
	}
	
   //finding Max Frequent Label
  	public Label findMaxFrequentLabelHU(ArrayList<Label> allLabels,Logger logger) {
  		Integer index = this.countOfLabelforHumanUsers.indexOf(Collections.max(this.countOfLabelforHumanUsers));
  		Integer label = Collections.max(this.countOfLabelforHumanUsers);
        ArrayList<Integer> gainLabelsIndex= new ArrayList<>();
  		int i=0; int random=0;
  		for(i=0; i<countOfLabelforHumanUsers.size(); i++) {
  			if(countOfLabelforHumanUsers.get(i)==label) {
  				gainLabelsIndex.add(i);
  			}
  		}
  		if(gainLabelsIndex.size()>1) {
  			random=(int)(Math.random()*(gainLabelsIndex.size()));
  			index=gainLabelsIndex.get(random);
  		}
 	   
  		logger.info("instance id: "+this.getInstanceid()+ "  most frequent class label  is calculated : " +allLabels.get(index).getLabelid() );
  		return allLabels.get(index);	
  	}
    

   //find instances according to their ids
	public int find(ArrayList<User>user,ArrayList<Label>list,ArrayList<Instancee>listofinstance,Long instanceid) {
		for(int c=0;c<listofinstance.size();c++) {
			if(instanceid==listofinstance.get(c).id) {
				return c;
			}
		}
		return -1;
	}
  	//////////////////////////////////////////////////////////// GETTERS & SETTERS
   
	public ArrayList<User> getUniqueUsers() {
		return uniqueUsers;
	}

	public void setUniqueUsers(ArrayList<User> uniqueUsers) {
		this.uniqueUsers = uniqueUsers;
	}
   
   public Integer getTotalNumberOfLabelAssignment() {
		return totalNumberOfLabelAssignment;
	}

	public void setTotalNumberOfLabelAssignment(Integer totalNumberOfLabelAssignment) {
		this.totalNumberOfLabelAssignment = totalNumberOfLabelAssignment;
	}
    
	
	public ArrayList<Integer> getCountOfLabel() {
		return countOfLabel;
	}

	public void setCountOfLabel(ArrayList<Integer> countOfLabel) {
		this.countOfLabel = countOfLabel;
	}
	

	public String getText() {
		return instance;
	}

	public void setText(String text) {
		this.instance = text;
	}

	public long getInstanceid() {
		return id;
	}

	public void setInstanceid(long id) {
		this.id = id;
	}


	public String getInstance() {
		return instance;
	}

	public void setInstance(String instance) {
		this.instance = instance;
	}

	public ArrayList<Integer> getPercentageOfLabel() {
		return percentageOfLabel;
	}

	public void setPercentageOfLabel(ArrayList<Integer> percentageOfLabel) {
		this.percentageOfLabel = percentageOfLabel;
	}

	public ArrayList<Integer> getCountOfLabelforHumanUsers() {
		return countOfLabelforHumanUsers;
	}

	public void setCountOfLabelforHumanUsers(ArrayList<Integer> countOfLabelforHumanUsers) {
		this.countOfLabelforHumanUsers = countOfLabelforHumanUsers;
	}
    
	

}
