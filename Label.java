import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Label implements finder {
      private long labelid;
      private String text;
      ArrayList<Integer> uniqueInstance = new ArrayList<>();
	public Label() {
		// TODO Auto-generated constructor stub
	}
   public Label(long labelid , String text) {
	   this.setLabelid(labelid);
	   this.setText(text);
   }
   
//check the unique instance
public void checkUniqueInstance(Instancee instancee,Logger logger) {
	if(!this.uniqueInstance.contains(Integer.valueOf(String.valueOf(instancee.getInstanceid())))) {
		this.uniqueInstance.add(Integer.valueOf(String.valueOf(instancee.getInstanceid())));
	}
	//logger.info("label id: "+this.labelid + " unique instance is calculated");
}

public int find(ArrayList<User> list1,ArrayList<Label>listoflabel,ArrayList<Instancee>list,Long labelid) {
	for(int c=0;c<listoflabel.size();c++) {
		if(labelid==listoflabel.get(c).labelid) {
			return c;
		}
	}
	return -1;
}

//****************************GETTER SETTERS************************
public String getText() {
	return text;
}
public void setText(String text) {
	this.text = text;
}
public long getLabelid() {
	return labelid;
}
public void setLabelid(long labelid) {
	this.labelid = labelid;
}
public ArrayList<Integer> getUniqueInstance() {
	return uniqueInstance;
}
public void setUniqueInstance(ArrayList<Integer> uniqueInstance) {
	this.uniqueInstance = uniqueInstance;
}


}
