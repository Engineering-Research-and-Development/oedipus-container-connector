package it.eng.oedipus.container.model;

public class Coordinate {
	private String latitude;
	private String longitude;

	public Coordinate() {};
	
	public Coordinate(String lat, String lon) {
		setLatitude(lat);
		setLongitude(lon);
	}

	public Coordinate(String coordinate) {
		setLatitude(coordinate.split(",")[0]);
		setLongitude(coordinate.split(",")[1]);
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
	};

}
