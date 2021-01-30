import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Logger;

public class HumanUser extends User {
    private String password;
	public HumanUser() {
		// TODO Auto-generated constructor stub
	}

	public HumanUser(long userId, String name, String usertype, Double ConsistencyCheckProbability,String password) {
		super(userId, name, usertype, ConsistencyCheckProbability);
		this.password=password;
		// TODO Auto-generated constructor stub
	}
    
	//MATCH THE HUMAN User 
	public int find(ArrayList<User> listofuser,String name,String password) {
		for(int i=0;i<listofuser.size();i++) {
			if((listofuser.get(i).getName()).equals(name)) {
			HumanUser u=(HumanUser)listofuser.get(i);
			if((u.getPassword()).equals(password)) {
			return i;
			}}
		}
		return -3;
	}
	//CHOOSE LABELS FOR HUMAN User 
	public void chooseLabel(ArrayList<Instancee> listOfInstance,ArrayList<User>ListofUser, ArrayList<Label> listOfLabel,Dataset current,Logger logger,File file2,ArrayList<User>currentusers) throws IOException {
		 long endTime=0;
   	     long startTime=0;
		
		Scanner scannerUser = new Scanner (System.in);
		Print print=new Print();
		String listString = ""; 
        int i=0; 
		for (i=0; i<listOfLabel.size(); i++){
			listString+="Id: "+listOfLabel.get(i).getLabelid()+ " Text: "+listOfLabel.get(i).getText()+"\n";
		}
		if(!(this.getDataset().contains(current))) {
    		this.getDataset().add(current);
    	}
		if(this.getLabeled().size()==0) {
			i=0;
		}else {
		i=(int)this.getLabeled().get(this.getLabeled().size()-1).getInstanceid();
		}
		if(i==listOfInstance.get(listOfInstance.size()-1).getInstanceid()) {
			i=0;
		}
	   
		for(; i<listOfInstance.size(); i++) {
			System.out.println("Instancee id:"+listOfInstance.get(i).getInstanceid()+" Text: "+ listOfInstance.get(i).getText()+"\nLabels: \n" + listString+"\nChoose Labels: ");
			 startTime = System.nanoTime();
			//CHOOSE THE Label
			Long label;
	        while(true) {
	        label=scannerUser.nextLong();
	        //IF Label IS INVALID
	        if(0<label && label<listOfLabel.size()+1) {
	         System.out.println(label);	
	         break;
	        }	
	        //Label NOT INVALID
	        else {
	         System.out.println("Invalid label id,try again.");
	         continue;
	        }
	        }
	     
	        LabelAssignment labelAssignment= new LabelAssignment(this,listOfInstance.get(i),listOfLabel.get((int)(label-1)));
	        if(this.getLabeled().contains(listOfInstance.get(i))) {
   			 if(this.existbefore(labelAssignment)) {
   				 this.setCheckconsistency((long)-1);
   			 }else {
   			this.setLabeled2(labelAssignment);
   			 }
   		 }
	        else {
   		    this.addInstance(listOfInstance.get(i));
   		    this.setNumofuniqueins((long)-1,logger);
         	this.setLabeled2(labelAssignment);}
            this.setTotalNumberofInstance((long)-1,logger);
            this.setConsistency();

   		    //Dataset METRICS
         print.printDatasetMetrics(listOfInstance,listOfLabel,current.getId(),current.getNumofuser(),logger,currentusers,current);
		  

   		    
		    //WRITE User METRICS
		    FileWriter  file3 =new FileWriter("usermetrics.json");
			FileWriter file1 =new FileWriter(file2);
			file1.write(" ");	
		    print.printuserarray(file1,ListofUser);
	        file3.write(" ");	
	        print.printusermetrics(file3,ListofUser,logger);
	        
	        
   		   
	        //Instancee METRICS 
            listOfInstance.get(i).setLabelCountHU(Integer.valueOf(String.valueOf(label)));
	        listOfInstance.get(i).setLabelCount(Integer.valueOf(String.valueOf(label))); //counting label in the listOfInstance
   		    listOfInstance.get(i).calculateTotalNumberOfLabelAssignment(logger); //Instancee METRICS STEP 1: calculating Total Number Of Label Assignment
   		    Integer numberOfUniqueLabel=listOfInstance.get(i).calculateTotalNumberOfUniqueLabel(logger);//Instancee METRICS STEP 2:calculating Total Number Of Unique Label
   		    listOfInstance.get(i).checkUniqueUsers(this,logger);//Instancee METRICS STEP 3:adding user into uniqueUsers list  
   		    Label maxFrequentLabel = listOfInstance.get(i).findMaxFrequentLabel(listOfLabel,logger);// finding Max Frequent Label
   		    Integer maxFrequentLabelPercentage = listOfInstance.get(i).percentageOfMaxLabel(logger);//finding Max Frequent Label percentage
   		    listOfInstance.get(i).percentageOfLabels(logger);//calculating percentage of each labels into percentageOfLabel array
   		    double entropy=listOfInstance.get(i).entropy(logger);//calculating entropy
   		    listOfLabel.get((int)(label-1)).checkUniqueInstance(listOfInstance.get(i),logger);
   		    
   		    
   		    print.backup(listOfInstance,listOfLabel,current.getId());
		    print.printInstanceMetrics(listOfInstance,listOfLabel, current.getId(),logger);
		    
   		 endTime=System.nanoTime();
		 this.setTime(endTime - startTime);
		}
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
