package mx.com.liverpool.mesaregalos.eventos;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"ownerId", "ownerType", "firstName", "lastNameOrFatherName", "motherName", "nickName", "email", "dateOfBirth", "phone", "gender", "preferredAddresses"})
public class Owner {
  private String ownerId;
  
  private String ownerType;
  
  private String firstName;
  
  private String lastNameOrFatherName;
  
  private String motherName;
  
  private String nickName;
  
  private String email;
  
  private String dateOfBirth;
  
  private String phone;
  
  private String gender;
  
  @XmlElementWrapper(name = "preferredAddresses")
  @XmlElement(name = "preferredAddress")
  List<PreferredAddress> preferredAddresses;
  
  public String getOwnerId() {
    return this.ownerId;
  }
  
  public void setOwnerId(String ownerId) {
    this.ownerId = ownerId;
  }
  
  public String getOwnerType() {
    return this.ownerType;
  }
  
  public void setOwnerType(String ownerType) {
    this.ownerType = ownerType;
  }
  
  public String getFirstName() {
    return this.firstName;
  }
  
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }
  
  public String getLastNameOrFatherName() {
    return this.lastNameOrFatherName;
  }
  
  public void setLastNameOrFatherName(String lastNameOrFatherName) {
    this.lastNameOrFatherName = lastNameOrFatherName;
  }
  
  public String getMotherName() {
    return this.motherName;
  }
  
  public void setMotherName(String motherName) {
    this.motherName = motherName;
  }
  
  public String getNickName() {
    return this.nickName;
  }
  
  public void setNickName(String nickName) {
    this.nickName = nickName;
  }
  
  public String getEmail() {
    return this.email;
  }
  
  public void setEmail(String email) {
    this.email = email;
  }
  
  public String getDateOfBirth() {
    return this.dateOfBirth;
  }
  
  public void setDateOfBirth(String dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }
  
  public String getPhone() {
    return this.phone;
  }
  
  public void setPhone(String phone) {
    this.phone = phone;
  }
  
  public String getGender() {
    return this.gender;
  }
  
  public void setGender(String gender) {
    this.gender = gender;
  }
  
  public List<PreferredAddress> getPreferredAddresses() {
    return this.preferredAddresses;
  }
  
  public void setPreferredAddresses(List<PreferredAddress> preferredAddresses) {
    this.preferredAddresses = preferredAddresses;
  }
  
  public String toString() {
    return "Owner [ownerId=" + this.ownerId + ", ownerType=" + this.ownerType + ", firstName=" + this.firstName + 
      ", lastNameOrFatherName=" + this.lastNameOrFatherName + ", motherName=" + this.motherName + ", nickName=" + 
      this.nickName + ", email=" + this.email + ", dateOfBirth=" + this.dateOfBirth + ", phone=" + this.phone + ", gender=" + 
      this.gender + "]";
  }
}
