import java.lang.Integer;
import java.util.ArrayList;
import java.util.logging.Logger;

import org.json.simple.JSONArray;
public class Dataset {
  private Long id;
  private Long max_label;
  private String name;
  private  JSONArray labels;
  private JSONArray instances;
  private Long numofuser;
  private double completeness;
	public Dataset() {
		// TODO Auto-generated constructor stub
	}
public Dataset(long id,long max_label,String name ,JSONArray label,JSONArray Instance,Long numberofuser) {
	setId(id);
	setMax_label(max_label);
	setName(name);
	setLabels(label);
	setInstances(Instance);
	setNumofuser(numberofuser);
}


	public void addDataset(ArrayList<User> currentusers, Dataset current) {
		 for(int k=0;k<currentusers.size();k++) {
	        	if(currentusers.get(k).getDataset().contains(current)) {
	        		continue;
	        	}else {
	        		currentusers.get(k).getDataset().add(current);
	        	}
	        }
	}
	
public Double classDistribution1(int isLabeled,int labelarrayelement,ArrayList<Instancee>instancee) {
	return (((double)(labelarrayelement-(instancee.size()-isLabeled))/(double)isLabeled))*100;
}

public Double classDistribution2(int isLabeled,int labelarrayelement) {
	return ((double)labelarrayelement/(double)isLabeled)*100;
}
	public void readDataset(Logger logger,Dataset current) {
        logger.info("Input file is read succesfully.\n");
        logger.info("Dataset id: "+ current.getId()+ " is created");
        logger.info("Dataset name: "+current.getName()+ " is created");
        logger.info("maximum number of labels per instance: "+current.getMax_label()+ " is created\n");	
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public JSONArray getLabels() {
		return labels;
	}
	public void setLabels(JSONArray labels) {
		this.labels = labels;
	}
	public JSONArray getInstances() {
		return instances;
	}
	public void setInstances(JSONArray instance) {
		this.instances = instance;
	}
	public long getMax_label() {
		return max_label;
	}
	public void setMax_label(long max_label) {
		this.max_label = max_label;
	}
	public long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getNumofuser() {
		return numofuser;
	}
	public void setNumofuser(Long numofuser) {
		this.numofuser = numofuser;
	}
	public double getCompleteness() {
		return completeness;
	}
	public void setCompleteness(double completeness) {
		this.completeness = completeness;
	}


	
	
}
