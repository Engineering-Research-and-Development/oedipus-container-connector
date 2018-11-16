package it.eng.oedipus.container.model;

public class Sector {
	private String id; 
	private Long travelMeanTime;
	private Long maxThreshold;
	private Long minThreshold;
	private Coordinate startPosition;
	private Coordinate endPosition;

	public Sector() {}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public Coordinate getStartPosition() {
		return startPosition;
	}

	public void setStartPosition(Coordinate startPosition) {
		this.startPosition = startPosition;
	}

	public Coordinate getEndPosition() {
		return endPosition;
	}

	public void setEndPosition(Coordinate endPosition) {
		this.endPosition = endPosition;
	}
	
	
}
