package Server.Control;

import java.util.Calendar;


@SuppressWarnings("serial")
public class HumanPlayerC implements Server.RMIInterface.Player{

	private String name;
	private String surname;
	private Calendar date;


	public HumanPlayerC(){
	}
	
	public HumanPlayerC(String n, String s, Calendar d){
		this.setName(n);
		this.setSurname(s);
		this.setDateBirth(d);
	}
	
	public String getName() {
		return this.name;
	}

	public String getSurname() {
		return this.surname;
	}

	public Calendar getDateBirth() {
		return this.date;
	}
	
	public void setName(String n){
		this.name = n;
	}

	public void setSurname(String s){
		this.surname = s;
	}
	
	public void setDateBirth(Calendar d){
		this.date = d;
	}
}
