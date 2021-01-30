import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.util.concurrent.TimeUnit;

import java.io.FileWriter;
import java.io.IOException;
import org.json.simple.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.logging.*;
public class Test {

	   public static void main(String args[]) throws FileNotFoundException, IOException, ParseException, SecurityException {
		    //LOGGER PART
		    Logger logger = Logger.getLogger("MyLog");
	        FileHandler fileHandler;
	        Integer IsPreviousRead=0;
		   try {
	             
	            // This block configure the logger with handler and formatter
			    fileHandler = new FileHandler("tracing.log");
	            logger.addHandler(fileHandler);
	            //logger.setLevel(Level.ALL);
	            SimpleFormatter formatter = new SimpleFormatter();
	            fileHandler.setFormatter(formatter);
	       
	             
	        } catch (SecurityException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }//********************
	     
		  ArrayList<Label>listOfLabel=new ArrayList<>();
		  ArrayList<Instancee>listOfInstance=new ArrayList<>();
	      JSONParser jsonParser = new JSONParser();
	      //READ INPUT FILE
	      JSONObject jsonUser = (JSONObject) jsonParser.parse(new FileReader("configuration.json"));
	      //READ Dataset
	      JSONArray jsonArraydataset = (JSONArray)(jsonUser.get("datasets"));
	      //Dataset ARRAYLIST
	      ArrayList<Dataset>dataset1=new ArrayList<>();
	      //SINGLETON
	      Singleton temp=Singleton.getInstance();
	      temp.createDatasets(jsonArraydataset, dataset1);

	      //Read from file
	      //CurrentDatasetId determined  which dataset will run.
	      Dataset current=dataset1.get(((Integer.valueOf(String.valueOf(jsonUser.get("current dataset id"))))-1));
     
	         //Take number of users in the dataset
	         Long nuser=current.getNumofuser();
	  
	         //Retrieving the array
	         JSONArray jsonArray = current.getLabels();

	         JSONArray jsonArrayInstance =current.getInstances();
	           
	         JSONArray jsonArrayUser = (JSONArray)(jsonUser.get("users"));
             
	          //User ARRAYLIST
			  ArrayList <User> listOfUser =new ArrayList<User>();
			  
			  //READ INPUTS
			  current.readDataset(logger, current);
	          Generate generate = new Generate(logger);  
	          generate.readLabel(jsonArray,listOfLabel);
	          generate.readInstance( jsonArrayInstance,listOfLabel, listOfInstance);
		      generate.readUser(jsonArrayUser, listOfUser);
		      //Print
              Print print=new Print();
          //**********************READ PREVIOUS INFORMATION*************
	        File file=new File("usermetrics.json"); 

		    for (int t=0;t<dataset1.size();t++) {
		    	File file1=new File("Secret"+dataset1.get(t).getId()+".json");
		    }
		     File file2 = new File("Secret"+current.getId()+".json"); 
		    temp.readUsers(file, listOfUser, listOfLabel, jsonParser, listOfInstance, print, logger, dataset1, current,file2);
		    
		
		   //**********************RANDOM ASSIGNMENT************************************************
		     ArrayList<User> currentusers=new ArrayList<User>();
		      int i=0;
		    	
		         
		      Integer numberOfUniqueLabel;
		      Label maxFrequentLabel= new Label();
		      Integer maxFrequentLabelPercentage;
		      double entropy;
		     
		         
		     //OKUMA KISMI
		    //-------------------------------------------------------------READING PREVIOUS VALUE---------------------------------
		      
		      IsPreviousRead=temp.readPreviousInstanceValue( listOfInstance, listOfLabel, listOfUser, currentusers, current, nuser, logger );
		      User bos=new User();
		     
		      //**************GET User*******************
		    
		     int userIndex=-1; int randomBotUser=(int)(Math.random()*(listOfUser.size()));
		     while(userIndex<0) {//human user isim alma
		    	userIndex=generate.getUserInput(listOfUser,userIndex);
		    	//IF user is human
		    	if(userIndex>0) {

		    	 if(!(currentusers.contains(listOfUser.get(userIndex)))) {
		    		 currentusers.add(listOfUser.get(userIndex));
		    	 }
		    	((HumanUser)listOfUser.get(userIndex)).chooseLabel(listOfInstance,listOfUser,listOfLabel,current,logger,file2,currentusers);
			
		    	}
		      	
		    	//BOT 
		    	else if(userIndex == -1) {//----------------------------------------BOTRANDOM-----------------------------------------------
		    	
		    				if(IsPreviousRead==0) {
		    					while(true) {
		    		        	   	 int random=(int)(Math.random()*(listOfUser.size()));
		    		        	     User cuser=listOfUser.get(random);
		    		        	     if(bos.find(currentusers,cuser)) {
		    		        	    	 continue;
		    		        	     }
		    		        	     else if(cuser.getUsertype().equals("HumanUser"))continue;
		    		        	     else {
		    		        	   	  currentusers.add(cuser) ;
		    		        	     }
		    		        	   if(currentusers.size()==nuser) {
		    		        		break;
		    		        	   }}	    					
		    					
		     				}
		    			
		    				//ADD Dataset TO User
		    				current.addDataset(currentusers,  current);
		    		
		    			          long endTime=0;
		    			    	  long startTime=0;
		    			        	    
		    			        	   //--------------------------FIRST ASSIGNMENT-----------------------------------------------------
		    			        	   for(int l=0;l<currentusers.size();l++) {
		    			        		 if(!currentusers.get(l).getUsertype().equals("HumanUser")){
		    			        		 startTime = System.nanoTime();
		    			        		 int random=(int)(Math.random()*(listOfInstance.size()));
		    			        		 int random1;
		    			        		 
		    			        		 //SPECIAL BOT
		    			        		 if(currentusers.get(l).getUsertype().equals("MachineLearningBot")){
		    			        			 random1=(int)listOfInstance.get(random).findMaxFrequentLabelHU(listOfLabel, logger).getLabelid()-1;
		    			        		 }
		    			        		 //RANDOM BOT
		    			        		 else {
		    			        			 random1=(int)(Math.random()*(listOfLabel.size()));
		    			        		 }
		    			        		 LabelAssignment one=new LabelAssignment(currentusers.get(l),listOfInstance.get(random),listOfLabel.get(random1));
		    			        		 
		    			        		 //This functions are related to Instancee METRICS
		    			        		
		    			        		 Integer intObj = new Integer((int)(long)listOfLabel.get(random1).getLabelid());// casting label id to Integer
		    			        		 listOfInstance.get(random).setLabelCount(intObj); //counting label in the listOfInstance
		    			        		 listOfInstance.get(random).calculateTotalNumberOfLabelAssignment(logger); //Instancee METRICS STEP 1: calculating Total Number Of Label Assignment
		    			        		 numberOfUniqueLabel=listOfInstance.get(random).calculateTotalNumberOfUniqueLabel(logger);//Instancee METRICS STEP 2:calculating Total Number Of Unique Label
		    			        		 listOfInstance.get(random).checkUniqueUsers( currentusers.get(l),logger);//Instancee METRICS STEP 3:adding user into uniqueUsers list  
		    			        		 maxFrequentLabel = listOfInstance.get(random).findMaxFrequentLabel(listOfLabel,logger);// finding Max Frequent Label
		    			        		 maxFrequentLabelPercentage = listOfInstance.get(random).percentageOfMaxLabel(logger);//finding Max Frequent Label percentage
		    			        		 listOfInstance.get(random).percentageOfLabels(logger);//calculating percentage of each labels into percentageOfLabel array
		    			        		 entropy=listOfInstance.get(random).entropy(logger);//calculating entropy
		    			        		 
		    			        		
		    			        		 listOfLabel.get(random1).checkUniqueInstance(listOfInstance.get(random),logger);
		    			        		 endTime=System.nanoTime();
		    			        		 currentusers.get(l).setTime(endTime - startTime);
		    			        		 if(currentusers.get(l).getLabeled().contains(listOfInstance.get(random))) {
		    			        			/* if(currentusers.get(l).existbefore(one)) {
		    			        				 currentusers.get(l).setCheckconsistency((long)-1);
		    			        			 }else {
		    			        				 currentusers.get(l).setLabeled2(one);
		    			        			 }*/
		    			        			 one.setAssignment(currentusers.get(l),one);
		    			        		 }
		    			        		 else {
		    			        		 currentusers.get(l).addInstance((listOfInstance.get(random)));
		    			        		 currentusers.get(l).setNumofuniqueins((long)-1,logger);
		    			        		 currentusers.get(l).setLabeled2(one);}
		    			                 currentusers.get(l).setTotalNumberofInstance((long)-1,logger);
		    			                 currentusers.get(l).setConsistency();
		    			                
		    			                 //Dataset METRICS
		    			        		 print.printDatasetMetrics(listOfInstance,listOfLabel,current.getId(),current.getNumofuser(),logger,currentusers,current);
		    						    
		    			        		 //User METRICS
		    							 FileWriter  file3 =new FileWriter("usermetrics.json");
		    					     	 FileWriter file1 =new FileWriter(file2);
		    						     file1.write(" ");	
		    						     print.printuserarray(file1,listOfUser);
		    						     file3.write(" ");	
		    						     print.printusermetrics(file3,listOfUser,logger);	
		    			                 
		    			                 //Instancee METRICS
		    			                 print.backup(listOfInstance,listOfLabel,current.getId());
		    			        		 print.printInstanceMetrics(listOfInstance,listOfLabel, current.getId(),logger);
		    			        		
		    			         }}
		    			    
		    			  i=0;
		    			  endTime=0;
		    			  startTime=0;
		    			  while(true) {
		    				 //Time starts 
		    				 startTime = System.nanoTime();

		    				     int randomuser=(int)(Math.random()*(currentusers.size()));
		    				     if(currentusers.get(randomuser).getUsertype().equals("HumanUser")) continue;
		    				     User currentu=currentusers.get(randomuser);
		    				     int checkprob=(int)(Math.random()*(100));
		    				     
		    			     //----------------------------IF CPP is lower----------------
		    				     if(checkprob<currentu.getConsistencyCheckProbability()*100) {
		    				    	
		    				    	int  random=(int)(Math.random()*(currentu.getLabeled().size()));
		    				 	    int random1;
	    			        		 
	    			        		 //SPECIAL BOT
	    			        		 if(currentu.getUsertype().equals("MachineLearningBot")){
	    			        			 random1=(int)currentu.getLabeled().get(random).findMaxFrequentLabelHU(listOfLabel, logger).getLabelid()-1;
	    			        			// System.out.println("SPECIAL "+ random1 + " " + currentu.getUserId() + "Instancee ID"+currentu.getLabeled().get(random).getInstanceid());
	    			        		 }
	    			        		 //RANDOM BOT
	    			        		 else {
	    			        			 random1=(int)(Math.random()*(listOfLabel.size()));
	    			        		 }
		    				 	
		    				 		 LabelAssignment two=new LabelAssignment(currentu,currentu.getLabeled().get(random),listOfLabel.get(random1));
		    				 		
		    			          /*    if(currentu.existbefore(two)) {
		    				          currentu.setCheckconsistency((long)-1);
		    			                }
		    			              else {	
		    				 	 	  currentu.setLabeled2(two);
		    				 	 	  }*/
		    				 		 two.setAssignment(currentu, two);
		    				 				
		    				 		  Integer intObj = new Integer((int)(long)two.getLabels().getLabelid());
		    				 		  currentu.getLabeled().get(random).setLabelCount(intObj);
		    				 		  //This functions are related to User METRICS
		    				 		  currentu.getLabeled().get(random).calculateTotalNumberOfLabelAssignment(logger);
		    				 		  numberOfUniqueLabel=currentu.getLabeled().get(random).calculateTotalNumberOfUniqueLabel(logger);
		    				 		  currentu.getLabeled().get(random).checkUniqueUsers( currentu,logger);
		    				 		  maxFrequentLabel = currentu.getLabeled().get(random).findMaxFrequentLabel(listOfLabel,logger);
		    						  maxFrequentLabelPercentage = currentu.getLabeled().get(random).percentageOfMaxLabel(logger);
		    						  currentu.getLabeled().get(random).percentageOfLabels(logger);
		    					    
		    						  entropy=currentu.getLabeled().get(random).entropy(logger);
		    						  listOfLabel.get(random1).checkUniqueInstance(currentu.getLabeled().get(random),logger);		 		
		    						  currentu.setTotalNumberofInstance((long)-1,logger);
		    					 	  currentu.setConsistency();
		    						  
		    					 	    //Dataset METRICS
		    			             	print.printDatasetMetrics(listOfInstance,listOfLabel,current.getId(),current.getNumofuser(),logger,currentusers,current);
		    						  
		    						    //WRITE User METRICS
		    						    FileWriter  file3 =new FileWriter("usermetrics.json");
		    							FileWriter file1 =new FileWriter(file2);
		    							file1.write(" ");	
		    						    print.printuserarray(file1,listOfUser);
		    					        file3.write(" ");	
		    					        print.printusermetrics(file3,listOfUser,logger);	
		    					        
		    					        //Instancee METRICS
		    					        print.backup(listOfInstance,listOfLabel,current.getId());
		    							print.printInstanceMetrics(listOfInstance,listOfLabel,current.getId(),logger);
		    							    				 	
		    				     }
		    				     
		    				    else {  //----------------------------IF CPP is higher----------------
		    				    	
		    					int random1=(int)(Math.random()*(listOfLabel.size()));
		    					
		    					Label currentlabel=listOfLabel.get(random1);
		    					
		    					while(true) {
		    						int  random=(int)(Math.random()*(listOfInstance.size()));
		    						Instancee cInstance=listOfInstance.get(random);
		
		    						if(currentu.getLabeled().contains(cInstance)) {
		    					    	 continue;
		    					     }
		    						else {
			 
		    			        		 //SPECIAL BOT
		    			        		 if(currentu.getUsertype().equals("MachineLearningBot")){
		    			        			 random1=(int)cInstance.findMaxFrequentLabelHU(listOfLabel, logger).getLabelid()-1;
		    			        		     currentlabel=listOfLabel.get(random1);
		    			        		 }
		    												
		    							LabelAssignment two=new LabelAssignment(currentu,cInstance,currentlabel);
		    						
		    							Integer intObj = new Integer((int)(long)two.getLabels().getLabelid());
		    							cInstance.setLabelCount(intObj);
		    							//This functions are related to User METRICS
		    							cInstance.calculateTotalNumberOfLabelAssignment(logger);
		    							numberOfUniqueLabel=cInstance.calculateTotalNumberOfUniqueLabel(logger);
		    							cInstance.checkUniqueUsers( currentu,logger);
		    							maxFrequentLabel = cInstance.findMaxFrequentLabel(listOfLabel,logger);
		    							maxFrequentLabelPercentage = cInstance.percentageOfMaxLabel(logger);
		    							cInstance.percentageOfLabels(logger);
		    						
		    							entropy=cInstance.entropy(logger);

		    							currentlabel.checkUniqueInstance(cInstance,logger);
		    						    currentu.getLabeled().add(cInstance);
		    							currentu.setNumofuniqueins((long)-1,logger);
		    							currentu.setLabeled2(two);
		    						    currentu.setTotalNumberofInstance((long)-1,logger);
		    						    currentu.setConsistency();
		    						   
		    						    //Dataset METRICS
		    			        		 print.printDatasetMetrics(listOfInstance,listOfLabel, current.getId(),current.getNumofuser(),logger,currentusers,current);
		    						    
		    						    //WRITE User METRICS
		    						    FileWriter  file3 =new FileWriter("usermetrics.json");
		    							FileWriter file1 =new FileWriter(file2);
		    							file1.write(" ");	
		    							print.printuserarray(file1,listOfUser);
		    					        file3.write(" ");	
		    					        print.printusermetrics(file3,listOfUser,logger);	  
		    					        
		    					    	     //Instancee METRICS
		    							print.backup(listOfInstance,listOfLabel,current.getId());
		    							print.printInstanceMetrics(listOfInstance,listOfLabel,current.getId(),logger);
		    					       break;  
		    						}
		    				     }//while
		    					
		    				    }//else
		    				    //Finish the timer
		    				    endTime = System.nanoTime();
		    				    currentu.setTime(endTime - startTime);
		    				     }
		    	}
		    	userIndex=-1;
		     }
	   }//main
	   
}//class
	   
