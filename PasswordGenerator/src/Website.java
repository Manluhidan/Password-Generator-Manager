import java.util.*;

public class Website {

	public Data[] accounts;
	public String name;
	public int numOfData;
	
	public Website(String name) {
		
		accounts = new Data[100];
		this.name = name;
		numOfData = 0;
	}
	
	public Website(Data d, String name) {
		
		accounts = new Data[100];
		this.name = name;
		numOfData = 0;
		accounts[numOfData++] = d;
	}
	
	public void addAccount(Data d) {
		
		accounts[numOfData++] = d;
		
	}
	public int getNumOfData() {
		return numOfData;
	}
	
	
	
	
	
}
