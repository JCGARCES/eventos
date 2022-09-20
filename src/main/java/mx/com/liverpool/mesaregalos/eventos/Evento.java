/**
 * 
 */
package mx.com.liverpool.mesaregalos.eventos;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "event")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"eventId", "eventName", "actionType", "eventType", "eventCategory", "eventDate", "preferredStore", "status", "creationDate", "brandName", "eventOwnerDetails"})
public class Evento {
  private String eventId;
  
  private String eventName;
  
  private String actionType;
  
  private String eventType;
  
  private String eventCategory;
  
  private String eventDate;
  
  private String preferredStore;
  
  private String status;
  
  private String creationDate;
  
  private String brandName;
  
  @XmlElementWrapper
  @XmlElement(name = "owner")
  List<Owner> eventOwnerDetails;
  
  public String getEventId() {
    return this.eventId;
  }
  
  public void setEventId(String eventId) {
    this.eventId = eventId;
  }
  
  public String getEventName() {
    return this.eventName;
  }
  
  public void setEventName(String eventName) {
    this.eventName = eventName;
  }
  
  public String getActionType() {
    return this.actionType;
  }
  
  public void setActionType(String actionType) {
    this.actionType = actionType;
  }
  
  public String getEventType() {
    return this.eventType;
  }
  
  public void setEventType(String eventType) {
    this.eventType = eventType;
  }
  
  public String getEventCategory() {
    return this.eventCategory;
  }
  
  public void setEventCategory(String eventCategory) {
    this.eventCategory = eventCategory;
  }
  
  public String getEventDate() {
    return this.eventDate;
  }
  
  public void setEventDate(String eventDate) {
    this.eventDate = eventDate;
  }
  
  public String getPreferredStore() {
    return this.preferredStore;
  }
  
  public void setPreferredStore(String preferredStore) {
    this.preferredStore = preferredStore;
  }
  
  public String getStatus() {
    return this.status;
  }
  
  public void setStatus(String status) {
    this.status = status;
  }
  
  public String getCreationDate() {
    return this.creationDate;
  }
  
  public void setCreationDate(String creationDate) {
    this.creationDate = creationDate;
  }
  
  public String getBrandName() {
    return this.brandName;
  }
  
  public void setBrandName(String brandName) {
    this.brandName = brandName;
  }
  
  public List<Owner> getEventOwnerDetails() {
    return this.eventOwnerDetails;
  }
  
  public void setEventOwnerDetails(List<Owner> eventOwnerDetails) {
    this.eventOwnerDetails = eventOwnerDetails;
  }
}
