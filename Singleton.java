import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

// File Name: Singleton.java
public class Singleton {

   private static Singleton singleton = new Singleton( );

   
   private Singleton() { }

   public static Singleton getInstance( ) {
      return singleton;
   }
   protected static void createDatasets(JSONArray jsonArraydataset,ArrayList<Dataset>datasets) throws FileNotFoundException, IOException, ParseException{
	   JSONParser jsonParser = new JSONParser();
	   for (int i=0;i<jsonArraydataset.size();i++) {
		   JSONObject a=(JSONObject) jsonArraydataset.get(i);

		   JSONObject dataset= (JSONObject)jsonParser.parse(new FileReader(a.get("filepath").toString()));
		   JSONArray jsonArray = (JSONArray)(dataset.get("class labels"));
		   JSONArray jsonArray1 = (JSONArray)(dataset.get("instances"));

		   Dataset a1=new Dataset((long)dataset.get("dataset id"),(long)dataset.get("maximum number of labels per instance"),(String)dataset.get("dataset name"),jsonArray,jsonArray1,(Long)a.get("numberofuser"));
		   datasets.add(a1);
	   }
   }//createDatasets
  
   //Read previous value
   protected static Integer readPreviousInstanceValue( ArrayList<Instancee> listOfInstance,ArrayList<Label> listOfLabel,  ArrayList<User> listOfUser, ArrayList<User> currentusers, Dataset current, Long nuser,  Logger logger ) throws FileNotFoundException, IOException, ParseException {
	   User bos=new User();
	      Integer IsPreviousRead=0;
	     //**********Find the dataset whether it exists previous iteration*******************   
       File fileControl = new File("Secret"+current.getId()+"instancemetrics.json");

       if (fileControl.exists()) {
      	//Read previous file 
        
	      JSONParser jsonParserRead= new JSONParser();
	      JSONObject jsonRead = (JSONObject) jsonParserRead.parse(new FileReader("Secret"+current.getId()+"instancemetrics.json"));
	      
	      //COUNT OF Label READ 
	      JSONArray array = (JSONArray)(jsonRead.get("CountOfLabel"));
	      ArrayList<Integer> countOfLabelPrevious=new ArrayList<>();
	      String convert="";
	      for (int z=0;z<array.size();z++) {
	    	  JSONArray object=(JSONArray)array.get(z);
	    	  countOfLabelPrevious=new ArrayList<>();
	    	  for(int j=0; j<object.size(); j++) {
	    		  convert=String.valueOf(object.get(j));
	    		  countOfLabelPrevious.add(Integer.valueOf(convert));
	    		 
	    	  }
	    	  listOfInstance.get(z).setCountOfLabel(countOfLabelPrevious);
	    	  
	      } 
	      //COUNT OF Label READ for human 
	      JSONArray arrayHuman = (JSONArray)(jsonRead.get("HumanUsersCount"));
	      ArrayList<Integer> countOfLabelPreviousHuman=new ArrayList<>();
	      for (int z=0;z<arrayHuman.size();z++) {
	    	  JSONArray object=(JSONArray)arrayHuman.get(z);
	    	  countOfLabelPreviousHuman=new ArrayList<>();
	    	  for(int j=0; j<object.size(); j++) {
	    		  convert=String.valueOf(object.get(j));
	    		  countOfLabelPreviousHuman.add(Integer.valueOf(convert));
	    		 
	    	  }
	    	  listOfInstance.get(z).setCountOfLabelforHumanUsers(countOfLabelPreviousHuman);
	    	  
	      } 
	      
	      //TOTAL Label ASSIGNMENT READ
	      JSONArray totalArray = (JSONArray)(jsonRead.get("TotalNumberOfAssignment"));
	      for (int z=0;z<totalArray.size();z++) {
	    	  convert=String.valueOf(totalArray.get(z));
	    	  listOfInstance.get(z).setTotalNumberOfLabelAssignment(Integer.valueOf(convert));
	    	  listOfInstance.get(z).percentageOfLabels(logger);
	      }
	     

	      //UNIQUE ARRAY 
	      Integer numberOfHumanUser=0;
	      JSONArray uniqueArray = (JSONArray)(jsonRead.get("UniqueArray"));
	      for (int z=0;z<uniqueArray.size();z++) {
	    	  JSONArray object=(JSONArray)uniqueArray.get(z);
	    	  for(int j=0; j<object.size(); j++) {
	    		  convert=String.valueOf(object.get(j));
	    		   for(int k=0; k<listOfUser.size(); k++) {
	    		   if(listOfUser.get(k).getUserId()==(long)Integer.valueOf(convert)) {
	    			   listOfInstance.get(z).checkUniqueUsers(listOfUser.get(k),logger);
		    		   
		    		     User cuser=listOfUser.get(k);
		    		    
		        	     if(bos.find(currentusers,cuser)) {
		        	    	 
		        	    	 continue;
		        	     }
		        	     else if(cuser.getUsertype().equals("HumanUser")) {
		        	    	 currentusers.add(cuser);
		        	    	 numberOfHumanUser++;
		        	     }
		        	     else {
		        	   	  currentusers.add(cuser) ;
		        	     }
	    		   }
	    		   
	    		   
	    	       }    		  
	    	  } 
	      }
	      
	      while(currentusers.size()<nuser+numberOfHumanUser ) {
      	   	 int random=(int)(Math.random()*(listOfUser.size()));
      	     User cuser=listOfUser.get(random);
      	     if(bos.find(currentusers,cuser)) {
      	    	 continue;
      	     }
      	     else if(cuser.getUsertype().equals("HumanUser"))continue;
      	     else {
      	   	  currentusers.add(cuser) ;
      	     }
      	  }
	
	      
	      
	      
	      //UNIQUE Instancee FOR EACH Label READ 
	      JSONArray labelArray = (JSONArray)(jsonRead.get("LabelArray"));
	      ArrayList<Integer> labelArrayPrevious=new ArrayList<>();
	      for (int z=0;z<listOfLabel.size();z++) {
	    	  JSONArray object=(JSONArray)labelArray.get(z);
	    	  labelArrayPrevious=new ArrayList<>();
	    	  for(int j=0; j<object.size(); j++) {
	    		  convert=String.valueOf(object.get(j));
	    		  labelArrayPrevious.add(Integer.valueOf(convert));
	    		
	    	  }
	    	  listOfLabel.get(z).setUniqueInstance(labelArrayPrevious);	    	  
	      } 
	      IsPreviousRead=1;
       }
    
   
   
	  return IsPreviousRead; 
   }
   
   
   
 public static void readUsers(File file,ArrayList<User>listOfUser,ArrayList<Label>listOfLabel,JSONParser jsonParser,ArrayList<Instancee>listOfInstance,Print print,Logger logger,ArrayList<Dataset>dataset1,Dataset current,File file2) throws IOException, ParseException {
	 if(file.length()==0) { 
    	 FileWriter file1 = new FileWriter("usermetrics.json");
    	
     print.printusermetrics(file1,listOfUser,logger);

 	       }
     else {
       	
     JSONObject jsonUser1 = (JSONObject) jsonParser.parse(new FileReader("usermetrics.json"));
     JSONArray jsonArrayUser1 = (JSONArray)(jsonUser1.get("users"));
     for(int i=0; i<jsonArrayUser1.size(); i++) {
    	 JSONObject jsonArrayUser1Object=(JSONObject) jsonArrayUser1.get(i);
    	 long TotalNumberofInstance=(long)jsonArrayUser1Object.get("totalNumberofInstance");
    	 long uniq=(long)jsonArrayUser1Object.get("Number of unique instance");
     	 long checkconsistency=(long)jsonArrayUser1Object.get("consistency");
     	 double avr=(double)jsonArrayUser1Object.get("average time");
    	 listOfUser.get(i).setTotalNumberofInstance(TotalNumberofInstance,logger);
    	 listOfUser.get(i).setNumofuniqueins(uniq,logger);
    	 listOfUser.get(i).setCheckconsistency(checkconsistency);
    	 listOfUser.get(i).setConsistency();
    	 listOfUser.get(i).setAverage((double)avr);
    	  JSONArray jsondataset = (JSONArray)jsonArrayUser1Object.get("datasets");
     	    for(int c=0;c<jsondataset.size();c++) {
     	    	 JSONObject a=(JSONObject) jsondataset.get(c);
     	    	long iddataset=(long)a.get("dataset id");
     	    	double completeness=(double)a.get("completeness");
            
                 for(int h=0;h<dataset1.size();h++) {
             if(dataset1.get(h).getId()==iddataset) {
          	   listOfUser.get(i).getDataset().add(dataset1.get(h));
          	   dataset1.get(h).setCompleteness(completeness);
                      } }
            }
     }
     }


     if(file2.length()==0) { 
       FileWriter file1 = new FileWriter(file2);

        }
      else {
     
      	  JSONObject jsonUser1 = (JSONObject) jsonParser.parse(new FileReader(file2));
          JSONArray jsonArrayUser1 = (JSONArray)(jsonUser1.get("users"));
          for(int i=0; i<jsonArrayUser1.size(); i++) {
         	 JSONObject jsonArrayUser1_1=(JSONObject) jsonArrayUser1.get(i);
      
         	 long id=(long)jsonArrayUser1_1.get("user id");
      
         	    JSONArray jsonArrayUserlabeled = (JSONArray)jsonArrayUser1_1.get("labeled");
         	    for(int c=0;c<jsonArrayUserlabeled.size();c++) {
         	   	 JSONObject a211=(JSONObject) jsonArrayUserlabeled.get(c);
         	   long h1=(long)a211.get("instance id " );
         	   Instancee a=new Instancee();
         	   listOfUser.get(i).getLabeled().add(listOfInstance.get(a.find(null,null,listOfInstance, h1)));
         	
          }
         	    JSONArray jsonArrayUserlabeled2 = (JSONArray)jsonArrayUser1_1.get("labeled2");
         	   for(int c=0;c<jsonArrayUserlabeled2.size();c++) {
            	   JSONObject a211=(JSONObject) jsonArrayUserlabeled2.get(c);
            	   long h1=(long)a211.get("instance id");
            	   long h2=(long)a211.get("label id");
            	   Instancee bos=new Instancee();
            	   User bos1 =new User();
            	   User user1=listOfUser.get(bos1.find(listOfUser,null,null,id));
            	   Instancee a=listOfInstance.get(bos.find(null,listOfLabel, listOfInstance,h1));
            	   Label label1=new Label();
            	   LabelAssignment a1=new LabelAssignment(user1,a,listOfLabel.get(label1.find(null,listOfLabel,null, h2)));
            	   listOfUser.get(i).getLabeled2().add(a1);

         	    } 
         	    }
          }
 }
}
