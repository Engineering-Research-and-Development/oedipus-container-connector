package it.eng.oedipus.container.model;

import java.util.Date;

public class Container {
	private Long id;
	private String content;
	private Long tripId;
	private Date requestTimeArrival;
	private String sectorId;
	private String latitude;
	private String longitude;
	private Long travelMeanTime;
	private Long maxThreshold;
	private Long minThreshold;
	private Date time;
	private Date extimatedTimeArrival;
	private Long requestTimeArrivalOffset;
	private Long tripTime;
	private Long maxTripTime;
	private Long minTripTime;

	
	public Container() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTripId() {
		return tripId;
	}

	public void setTripId(Long tripId) {
		this.tripId = tripId;
	}

	public Date getRequestTimeArrival() {
		return requestTimeArrival;
	}

	public void setRequestTimeArrival(Date requestTimeArrival) {
		this.requestTimeArrival = requestTimeArrival;
	}

	public String getSectorId() {
		return sectorId;
	}

	public void setSectorId(String sectorId) {
		this.sectorId = sectorId;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public Long getTravelMeanTime() {
		return travelMeanTime;
	}

	public void setTravelMeanTime(Long travelMeanTime) {
		this.travelMeanTime = travelMeanTime;
	}

	public Long getMaxThreshold() {
		return maxThreshold;
	}

	public void setMaxThreshold(Long maxThreshold) {
		this.maxThreshold = maxThreshold;
	}

	public Long getMinThreshold() {
		return minThreshold;
	}

	public void setMinThreshold(Long minThreshold) {
		this.minThreshold = minThreshold;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public Date getExtimatedTimeArrival() {
		return extimatedTimeArrival;
	}

	public void setExtimatedTimeArrival(Date extimatedTimeArrival) {
		this.extimatedTimeArrival = extimatedTimeArrival;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "Container [id=" + id + ", content=" + content + ", tripId=" + tripId + ", requestTimeArrival="
				+ requestTimeArrival + ", sectorId=" + sectorId + ", latitude=" + latitude + ", longitude=" + longitude
				+ ", travelMeanTime=" + travelMeanTime + ", maxThreshold=" + maxThreshold + ", minThreshold="
				+ minThreshold + ", time=" + time + ", extimatedTimeArrival=" + extimatedTimeArrival + "]";
	}

	public Long getRequestTimeArrivalOffset() {
		return requestTimeArrivalOffset;
	}

	public void setRequestTimeArrivalOffset(Long requestTimeArrivalOffset) {
		this.requestTimeArrivalOffset = requestTimeArrivalOffset;
	}

	public Long getTripTime() {
		return tripTime;
	}

	public void setTripTime(Long tripTime) {
		this.tripTime = tripTime;
	}

	public Long getMaxTripTime() {
		return maxTripTime;
	}

	public void setMaxTripTime(Long maxTripTime) {
		this.maxTripTime = maxTripTime;
	}

	public Long getMinTripTime() {
		return minTripTime;
	}

	public void setMinTripTime(Long minTripTime) {
		this.minTripTime = minTripTime;
	}
	
	
}
