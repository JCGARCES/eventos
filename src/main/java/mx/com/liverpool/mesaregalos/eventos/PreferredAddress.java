package mx.com.liverpool.mesaregalos.eventos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"addressId", "eventRecipientIndex", "nickName", "firstName", "lastNameOrFatherName", "maternalName", "country", "city", "stateId", "state", "delegationMunicipality", "delegationMunicipalityId", "building", "postalcode", "neighbourhood", "neighbourhoodId", "address1", "address2", "exteriorNumber", "interiorNumber", "particularPhoneCode", "phoneNumber", "cellular", "otherColony"})
public class PreferredAddress implements Cloneable {
  private String addressId;
  
  private String eventRecipientIndex;
  
  private String nickName;
  
  private String firstName;
  
  private String lastNameOrFatherName;
  
  private String maternalName;
  
  private String country;
  
  private String city;
  
  private String stateId;
  
  private String state;
  
  private String delegationMunicipality;
  
  private String delegationMunicipalityId;
  
  private String postalcode;
  
  private String neighbourhood;
  
  private String neighbourhoodId;
  
  private String address1;
  
  private String address2;
  
  private String exteriorNumber;
  
  private String interiorNumber;
  
  private String particularPhoneCode;
  
  private String phoneNumber;
  
  private String otherColony;
  
  private String building;
  
  private String cellular;
  
  public String getAddressId() {
    return this.addressId;
  }
  
  public void setAddressId(String addressId) {
    this.addressId = addressId;
  }
  
  public String getEventRecipientIndex() {
    return this.eventRecipientIndex;
  }
  
  public void setEventRecipientIndex(String eventRecipientIndex) {
    this.eventRecipientIndex = eventRecipientIndex;
  }
  
  public String getNickName() {
    return this.nickName;
  }
  
  public void setNickName(String nickName) {
    this.nickName = nickName;
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
  
  public String getMaternalName() {
    return this.maternalName;
  }
  
  public void setMaternalName(String maternalName) {
    this.maternalName = maternalName;
  }
  
  public String getCountry() {
    return this.country;
  }
  
  public void setCountry(String country) {
    this.country = country;
  }
  
  public String getCity() {
    return this.city;
  }
  
  public void setCity(String city) {
    this.city = city;
  }
  
  public String getStateId() {
    return this.stateId;
  }
  
  public void setStateId(String stateId) {
    this.stateId = stateId;
  }
  
  public String getState() {
    return this.state;
  }
  
  public void setState(String state) {
    this.state = state;
  }
  
  public String getDelegationMunicipality() {
    return this.delegationMunicipality;
  }
  
  public void setDelegationMunicipality(String delegationMunicipality) {
    this.delegationMunicipality = delegationMunicipality;
  }
  
  public String getDelegationMunicipalityId() {
    return this.delegationMunicipalityId;
  }
  
  public void setDelegationMunicipalityId(String delegationMunicipalityId) {
    this.delegationMunicipalityId = delegationMunicipalityId;
  }
  
  public String getPostalcode() {
    return this.postalcode;
  }
  
  public void setPostalcode(String postalcode) {
    this.postalcode = postalcode;
  }
  
  public String getNeighbourhood() {
    return this.neighbourhood;
  }
  
  public void setNeighbourhood(String neighbourhood) {
    this.neighbourhood = neighbourhood;
  }
  
  public String getNeighbourhoodId() {
    return this.neighbourhoodId;
  }
  
  public void setNeighbourhoodId(String neighbourhoodId) {
    this.neighbourhoodId = neighbourhoodId;
  }
  
  public String getAddress1() {
    return this.address1;
  }
  
  public void setAddress1(String address1) {
    this.address1 = address1;
  }
  
  public String getAddress2() {
    return this.address2;
  }
  
  public void setAddress2(String address2) {
    this.address2 = address2;
  }
  
  public String getExteriorNumber() {
    return this.exteriorNumber;
  }
  
  public void setExteriorNumber(String exteriorNumber) {
    this.exteriorNumber = exteriorNumber;
  }
  
  public String getParticularPhoneCode() {
    return this.particularPhoneCode;
  }
  
  public void setParticularPhoneCode(String particularPhoneCode) {
    this.particularPhoneCode = particularPhoneCode;
  }
  
  public String getPhoneNumber() {
    return this.phoneNumber;
  }
  
  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }
  
  public String getOtherColony() {
    return this.otherColony;
  }
  
  public void setOtherColony(String otherColony) {
    this.otherColony = otherColony;
  }
  
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }
  
  public String getInteriorNumber() {
    return this.interiorNumber;
  }
  
  public void setInteriorNumber(String interiorNumber) {
    this.interiorNumber = interiorNumber;
  }
  
  public String getBuilding() {
    return this.building;
  }
  
  public void setBuilding(String building) {
    this.building = building;
  }
  
  public String getCellular() {
    return this.cellular;
  }
  
  public void setCellular(String cellular) {
    this.cellular = cellular;
  }
}
