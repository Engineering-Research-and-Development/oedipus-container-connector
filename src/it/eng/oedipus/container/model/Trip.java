package it.eng.oedipus.container.model;

import java.util.Date;

public class Trip {
	private Long id;
	private Date requestTimeArrival;
	private Long requestTimeArrivalOffset;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getRequestTimeArrival() {
		return requestTimeArrival;
	}

	public void setRequestTimeArrival(Date requestTimeArrival) {
		this.requestTimeArrival = requestTimeArrival;
	}

	public Trip() {}

	public Long getRequestTimeArrivalOffset() {
		return requestTimeArrivalOffset;
	}

	public void setRequestTimeArrivalOffset(Long requestTimeArrivalOffset) {
		this.requestTimeArrivalOffset = requestTimeArrivalOffset;
	};
	
}
