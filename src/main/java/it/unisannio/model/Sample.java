package it.unisannio.model;


import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@Entity
@Table(name="records")
@JsonPropertyOrder({"TagID", "Timestamp", "TruckID", "Occurency", "Latitude", "Longitude"})
public class Sample  implements Serializable { 
	
	@Id
	//@GeneratedValue(generator="system-uuid")
	//@GenericGenerator(name="system-uuid", strategy = "uuid")
	String TagID;
	String TruckID;
	String Latitude;
	String Longitude;
	int Occurency;
	Date Timestamp;
	
	public Sample() {}
	
	
	
	public Sample(String tagID, String truckID, String latitude, String longitude, int occurency, Date timestamp) {
		super();
		TagID = tagID;
		TruckID = truckID;
		Latitude = latitude;
		Longitude = longitude;
		Occurency = occurency;
		Timestamp = timestamp;
	}



	@JsonProperty("TagID")
	public String getTagID() {
		return TagID;
	}
	@JsonProperty("TagID")
	public void setTagID(String tagID) {
		TagID = tagID;
	}
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")//2021-07-17 18:49:39
	@JsonProperty("Timestamp")
	public Date getTimestamp() {
		return Timestamp;
	}
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonProperty("Timestamp")
	public void setTimestamp(Date timestamp) {
		Timestamp = timestamp;
	}
	@JsonProperty("TruckID")
	public String getTruckID() {
		return TruckID;
	}
	@JsonProperty("TruckID")
	public void setTruckID(String truckID) {
		TruckID = truckID;
	}
	@JsonProperty("Occurency")
	public int getOccurency() {
		return Occurency;
	}
	@JsonProperty("Occurency")
	public void setOccurency(int occurency) {
		Occurency = occurency;
	}
	@JsonProperty("Latitude")
	public String getLatitude() {
		return Latitude;
	}
	@JsonProperty("Latitude")
	public void setLatitude(String latitude) {
		Latitude = latitude;
	}
	@JsonProperty("Longitude")
	public String getLongitude() {
		return Longitude;
	}
	@JsonProperty("Longitude")
	public void setLongitude(String longitude) {
		Longitude = longitude;
	}


	@Override
	public String toString() {
		return "Sample [TagID=" + TagID + ", Timestamp=" + Timestamp + ", TruckID=" + TruckID + ", Occurency="
				+ Occurency + ", Latitude=" + Latitude + ", Longitude=" + Longitude + "]";
	}
	
	
	

}
