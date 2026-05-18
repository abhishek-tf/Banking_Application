package com.tandf.casestudy.banking;
import java.util.Objects;
public class CustomerAccount{
    private String customerId;
    private String name;
    private String email;
    private String phNumber;
    	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhNumber() {
		return phNumber;
	}
	public void setPhNumber(String phNumber) {
		this.phNumber = phNumber;
	}
	public CustomerAccount() {
		
	}
	public CustomerAccount(String customerId, String name, String email, String phNumber) throws InvalidEmailException, InvalidPhoneNumberException {
		super();
		this.customerId = customerId;
		this.name = name;
		if(!email.contains("@")) throw new InvalidEmailException("The Email is Invalid as it doesnot contain @");
		else this.email = email;
		int len = phNumber.length();
		if((len==10) || (phNumber.startsWith("+91") && len==13)) this.phNumber = phNumber;
		else throw new InvalidPhoneNumberException("The valid phone number should contain 10 digits");
	}
    
	@Override
	public String toString() {
		return "CustomerAccount [customerId=" + customerId + ", name=" + name + ", email=" + email + ", phNumber="
				+ phNumber + "]";
	}
    	@Override
	public int hashCode() {
		return Objects.hash(customerId, email, name, phNumber);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CustomerAccount other = (CustomerAccount) obj;
		return Objects.equals(customerId, other.customerId) && Objects.equals(email, other.email)
				&& Objects.equals(name, other.name) && Objects.equals(phNumber, other.phNumber);
	}
}
// •	customerId must be unique 
// •	email must contain @ 
// •	phone number must be 10 digits 
