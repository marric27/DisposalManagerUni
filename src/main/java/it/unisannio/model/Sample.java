package it.unisannio.model;


import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;


@Document(collection = "samples")
@JsonPropertyOrder({"tagID", "timestamp", "truckID", "occurency", "latitude", "longitude"})
public class Sample  implements Serializable { 
	
	@Id
	String tagID;
	String truckID;
	Double latitude;
	Double longitude;
	int occurency;
	LocalDateTime timestamp;
	
	public Sample() {}
	
	public Sample(String tagID, String truckID, Double latitude, Double longitude, int occurency, LocalDateTime timestamp) {
		super();
		this.tagID = tagID;
		this.truckID = truckID;
		this.latitude = latitude;
		this.longitude = longitude;
		this.occurency = occurency;
		this.timestamp = timestamp;
	}



	@JsonProperty("tagID")
	public String getTagID() {
		return tagID;
	}
	@JsonProperty("tagID")
	public void setTagID(String tagID) {
		this.tagID = tagID;
	}
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")//2021-07-17 18:49:39
	@JsonProperty("timestamp")
	public LocalDateTime getTimestamp() {
		return timestamp;
	}
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonProperty("timestamp")
	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
	@JsonProperty("truckID")
	public String getTruckID() {
		return truckID;
	}
	@JsonProperty("truckID")
	public void setTruckID(String truckID) {
		this.truckID = truckID;
	}
	@JsonProperty("occurency")
	public int getOccurency() {
		return occurency;
	}
	@JsonProperty("occurency")
	public void setOccurency(int occurency) {
		this.occurency = occurency;
	}
	@JsonProperty("latitude")
	public Double getLatitude() {
		return latitude;
	}
	@JsonProperty("latitude")
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	@JsonProperty("longitude")
	public Double getLongitude() {
		return longitude;
	}
	@JsonProperty("longitude")
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}


	@Override
	public String toString() {
		return "Sample [tagID=" + tagID + ", timestamp=" + timestamp + ", truckID=" + truckID + ", occurency="
				+ occurency + ", latitude=" + latitude + ", longitude=" + longitude + "]";
	}
	
	
	

}
