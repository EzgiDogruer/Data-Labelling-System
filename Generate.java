
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Logger;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Generate {
	private Logger logger;
	
	Generate(Logger logger){
		this.logger=logger;
	}

	public void readInstance(JSONArray jsonArrayInstance,ArrayList<Label> listOfLabel,ArrayList<Instancee >listOfInstance ) {      
	      Instancee addInstance;	         
	      for (int i=0;i<jsonArrayInstance.size();i++) {
	    	    JSONObject jsonArray1Object=(JSONObject) jsonArrayInstance.get(i);
	    	    addInstance =new Instancee((long) jsonArray1Object.get("id"),(String) jsonArray1Object.get("instance"));
	    	    addInstance.createListForLabelCount(listOfLabel.size());// creating countOfLabel list and assign 0 to countOfLabel list in the beginning
	    	    addInstance.createListForLabelPercentage(listOfLabel.size());//// creating percentageOfLabel list and assign 0 to percentageOfLabel list in the beginning
	    	    addInstance.createListForLabelCountHU(listOfLabel.size());// creating countOfLabel list and assign 0 to countOfLabel list in the beginning
		        listOfInstance.add(addInstance); 
	      } 

	}	
	public void readLabel(JSONArray jsonArray,ArrayList<Label> listOfLabel) {
		Label label1;
	      for (int i=0;i<jsonArray.size();i++) {
	    	  JSONObject jsonArrayObject=(JSONObject) jsonArray.get(i);
		      label1=new Label((long) jsonArrayObject.get("label id"),(String) jsonArrayObject.get("label text"));
		        listOfLabel.add(label1);
		        
	      }
	}
	//Read user
	public void readUser(JSONArray jsonArrayUser,ArrayList<User> listOfUser) {
	      User user;
	      HumanUser huser;
	      for(int i=0; i<jsonArrayUser.size(); i++) {
	    	  JSONObject jsonArrayUserObject=(JSONObject) jsonArrayUser.get(i);
	  	    if(((String) jsonArrayUserObject.get("user type")).equals("HumanUser")) {
	  	    	huser= new HumanUser((long) jsonArrayUserObject.get("user id"),(String) jsonArrayUserObject.get("user name"),(String) jsonArrayUserObject.get("user type"),(double) jsonArrayUserObject.get("ConsistencyCheckProbability"),(String) jsonArrayUserObject.get("password"));
	  	        listOfUser.add(huser);
	    	//    huser.printUser(this.logger);
	  	    }
	  	    else {
		        user = new User((long) jsonArrayUserObject.get("user id"),(String) jsonArrayUserObject.get("user name"),(String) jsonArrayUserObject.get("user type"),(double) jsonArrayUserObject.get("ConsistencyCheckProbability"));
	    	    listOfUser.add(user);
	    	//  user.printUser(this.logger);
	    	  }
	      }
	}
	//GET User 
	public int getUserInput(ArrayList<User>  listOfUser,int userIndex) {
		Scanner scannerUser = new Scanner (System.in);
		
		System.out.println("Please enter username");
        String username = scannerUser.nextLine();
        System.out.println("Please enter password");
        String password = scannerUser.nextLine();
        
        if(username.equals("") && password.equals(""))  {
        	System.out.println("bottt");
        }
        else if((username.equals("") || password.equals(""))) {
        	System.out.println("Username or password is invalid,try again.");
        	userIndex=-2;
        }
        else {
        	
        	HumanUser u=new HumanUser();
        	userIndex=u.find(listOfUser, username,password);
        	
        	if(userIndex<0) {//-3 returns,ask again
	        	System.out.println("Username or password is wrong,try again.");
	        } 	

        }//else
		return userIndex;
		
	}

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}
	
	
}



