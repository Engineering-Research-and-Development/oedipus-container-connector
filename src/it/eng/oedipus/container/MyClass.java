package it.eng.oedipus.container;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.glassfish.jersey.client.ClientConfig;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.eng.oedipus.container.model.Container;
import it.eng.oedipus.container.model.ngsi.Attribute;
import it.eng.oedipus.container.model.ngsi.Entity;


public class MyClass extends TimerTask {
	final static Logger logger = Logger.getLogger(MyClass.class);

	private  Timer timer = new Timer();

	private List<Container> containers=new ArrayList<Container>();
	private int index;
	private long intervalRate;
	private Map<Long, Date> tripRTA=new HashMap<Long, Date>();
	private Map<Long, Long> tripEffectiveTime=new HashMap<Long, Long>();


	public MyClass(List<Container> containers, int index, long intervalRate) {
		setContainers(containers);
		setIndex(index);
		setIntervalRate(intervalRate);
	}

	public MyClass(List<Container> containers, Map<Long, Date> tripRTA, Map<Long, Long> tripEffectiveTime,  int index, long intervalRate) {
		setContainers(containers);
		setIndex(index);
		setIntervalRate(intervalRate);
		setTripRTA(tripRTA);
		setTripEffectiveTime(tripEffectiveTime);
	}


	public void run() {

		//your code
		logger.debug(".........running........");
		try {
			FileInputStream input = new FileInputStream("config.properties");
			// load a properties file
			Properties prop = new Properties();
			prop.load(input);


			Container container=containers.get(index);

			String SERVER_ADDRESS = prop.getProperty("ocb-url");

			Client client = ClientBuilder.newClient(new ClientConfig());

			/*String entityString = client.target(SERVER_ADDRESS)
	            .path("entities/")
	            .request(MediaType.APPLICATION_JSON)
	            .header("service", "oedipus")
	            .header("servicePath", "/container")
	            .get(String.class);

		logger.debug("res="+entityString);*/

			Entity entity=new Entity();
			entity.setId("1");
			entity.setType("container");




			Attribute attribute=new Attribute();
			attribute.setValue(container.getContent());
			attribute.setType("String");
			if (entity.getAttributes()==null)
				entity.setAttributes(new HashMap<>());
			entity.getAttributes().put("content", attribute);




			attribute=new Attribute();
			attribute.setValue(container.getLatitude());
			attribute.setType("String");
			if (entity.getAttributes()==null)
				entity.setAttributes(new HashMap<>());
			entity.getAttributes().put("latitude", attribute);



			attribute=new Attribute();
			attribute.setValue(container.getLongitude());
			attribute.setType("String");
			if (entity.getAttributes()==null)
				entity.setAttributes(new HashMap<>());
			entity.getAttributes().put("longitude", attribute);







			attribute=new Attribute();
			attribute.setValue(container.getSectorId());
			attribute.setType("String");
			if (entity.getAttributes()==null)
				entity.setAttributes(new HashMap<>());
			entity.getAttributes().put("sectorId", attribute);









			attribute=new Attribute();
			attribute.setValue(container.getTripId());
			attribute.setType("String");
			if (entity.getAttributes()==null)
				entity.setAttributes(new HashMap<>());
			entity.getAttributes().put("tripId", attribute);









			if (intervalRate==-1) {

				attribute=new Attribute();
				attribute.setValue(container.getTime());
				attribute.setType("String");
				if (entity.getAttributes()==null)
					entity.setAttributes(new HashMap<>());
				entity.getAttributes().put("time", attribute);



				attribute=new Attribute();
				attribute.setValue(container.getRequestTimeArrival());
				attribute.setType("String");
				if (entity.getAttributes()==null)
					entity.setAttributes(new HashMap<>());
				entity.getAttributes().put("requestTimeArrival", attribute);


				attribute=new Attribute();
				attribute.setValue(container.getExtimatedTimeArrival());
				attribute.setType("String");
				if (entity.getAttributes()==null)
					entity.setAttributes(new HashMap<>());
				entity.getAttributes().put("extimatedTimeArrival", attribute);

				attribute=new Attribute();
				attribute.setValue(container.getTripTime());
				attribute.setType("String");
				if (entity.getAttributes()==null)
					entity.setAttributes(new HashMap<>());
				entity.getAttributes().put("tripTime", attribute);

				attribute=new Attribute();
				attribute.setValue(container.getMaxTripTime());
				attribute.setType("String");
				if (entity.getAttributes()==null)
					entity.setAttributes(new HashMap<>());
				entity.getAttributes().put("maxTripTime", attribute);


				attribute=new Attribute();
				attribute.setValue(container.getMinTripTime());
				attribute.setType("String");
				if (entity.getAttributes()==null)
					entity.setAttributes(new HashMap<>());
				entity.getAttributes().put("minTripTime", attribute);

				attribute=new Attribute();
				attribute.setValue(container.getTripTime());
				attribute.setType("String");
				if (entity.getAttributes()==null)
					entity.setAttributes(new HashMap<>());
				entity.getAttributes().put("tripTime", attribute);

				attribute=new Attribute();
				attribute.setValue(container.getMaxTripTime());
				attribute.setType("String");
				if (entity.getAttributes()==null)
					entity.setAttributes(new HashMap<>());
				entity.getAttributes().put("maxTripTime", attribute);


				attribute=new Attribute();
				attribute.setValue(container.getMinTripTime());
				attribute.setType("String");
				if (entity.getAttributes()==null)
					entity.setAttributes(new HashMap<>());
				entity.getAttributes().put("minTripTime", attribute);


			}else {

				Date d=new Date();

				attribute=new Attribute();
				attribute.setValue(d);
				attribute.setType("String");
				if (entity.getAttributes()==null)
					entity.setAttributes(new HashMap<>());
				entity.getAttributes().put("time", attribute);

				if (container.getSectorId().equalsIgnoreCase("1"))
					tripEffectiveTime.put(container.getTripId(), d.getTime());
				//container.setTime(d);

				long timeDifferenceOffset=container.getRequestTimeArrivalOffset();
				long timeDifferenceExtimatedOffset=container.getExtimatedTimeArrival().getTime()-container.getRequestTimeArrival().getTime();


				long newRTA=new Date().getTime()+timeDifferenceOffset;



				if (container.getSectorId().equals("1")) {
					tripRTA.put(container.getTripId(), new Date(newRTA));
				}


				container.setRequestTimeArrival(tripRTA.get(container.getTripId()));
				if (timeDifferenceExtimatedOffset==0)
					container.setExtimatedTimeArrival(tripRTA.get(container.getTripId()));
				else {
					long newETA=((tripRTA.get(container.getTripId()).getTime())+timeDifferenceExtimatedOffset/intervalRate);
					container.setExtimatedTimeArrival(new Date(newETA));
				}


				attribute=new Attribute();
				attribute.setValue(container.getRequestTimeArrival());
				attribute.setType("String");
				if (entity.getAttributes()==null)
					entity.setAttributes(new HashMap<>());
				entity.getAttributes().put("requestTimeArrival", attribute);


				attribute=new Attribute();
				attribute.setValue(container.getExtimatedTimeArrival());
				attribute.setType("String");
				if (entity.getAttributes()==null)
					entity.setAttributes(new HashMap<>());
				entity.getAttributes().put("extimatedTimeArrival", attribute);

				if (container.getSectorId()=="1") {
					attribute=new Attribute();
					attribute.setValue(0);
					attribute.setType("String");
					if (entity.getAttributes()==null)
						entity.setAttributes(new HashMap<>());
					entity.getAttributes().put("tripTime", attribute);
				}

				attribute=new Attribute();
				attribute.setValue(container.getTravelMeanTime()==null?0:container.getTravelMeanTime()/intervalRate);
				attribute.setType("String");
				if (entity.getAttributes()==null)
					entity.setAttributes(new HashMap<>());
				entity.getAttributes().put("travelMeanTime", attribute);



				attribute=new Attribute();
				attribute.setValue(container.getMaxThreshold()==null?0:container.getMaxThreshold()/intervalRate);
				attribute.setType("String");
				if (entity.getAttributes()==null)
					entity.setAttributes(new HashMap<>());
				entity.getAttributes().put("maxThreshold", attribute);


				attribute=new Attribute();
				attribute.setValue(container.getMinThreshold()==null?0:container.getMinThreshold()/intervalRate);
				attribute.setType("String");
				if (entity.getAttributes()==null)
					entity.setAttributes(new HashMap<>());
				entity.getAttributes().put("minThreshold", attribute);




				//

				if (index>0) {
					Container prec=containers.get(index-1);
					long diff=container.getTime().getTime()-prec.getTime().getTime();
					long lastTripTime=d.getTime()-tripEffectiveTime.get(container.getTripId());


					attribute=new Attribute();
					attribute.setValue(diff/1000);
					attribute.setValue(lastTripTime/1000);
					attribute.setType("String");
					if (entity.getAttributes()==null)
						entity.setAttributes(new HashMap<>());
					entity.getAttributes().put("tripTime", attribute);
				}else {
					attribute=new Attribute();
					attribute.setValue(0);
					attribute.setType("String");
					if (entity.getAttributes()==null)
						entity.setAttributes(new HashMap<>());
					entity.getAttributes().put("tripTime", attribute);
				}
				attribute=new Attribute();
				attribute.setValue(container.getMaxTripTime()/intervalRate);
				attribute.setType("String");
				if (entity.getAttributes()==null)
					entity.setAttributes(new HashMap<>());
				entity.getAttributes().put("maxTripTime", attribute);


				attribute=new Attribute();
				attribute.setValue(container.getMinTripTime()/intervalRate);
				attribute.setType("String");
				if (entity.getAttributes()==null)
					entity.setAttributes(new HashMap<>());
				entity.getAttributes().put("minTripTime", attribute);





			}





			ObjectMapper mapper = new ObjectMapper();



			//Object to JSON in String
			String jsonInString="";
			try {
				jsonInString = mapper.writeValueAsString(entity.getAttributes());

			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


			client.target(SERVER_ADDRESS)
			.path("entities/"+entity.getId()+"/attrs")
			.request(MediaType.APPLICATION_JSON)
			.header("fiware-service", prop.getProperty("fiware-service"))
			.header("fiware-servicepath", prop.getProperty("fiware-servicepath"))
			.post(javax.ws.rs.client.Entity.json(jsonInString));
			logger.debug("RTA="+container.getRequestTimeArrival());
			logger.debug("ETA="+container.getExtimatedTimeArrival());
			logger.debug("time="+entity.getAttributes().get("time").getValue());







			//schedule next task;
			schedule();
		}catch(Exception e ) {
			e.printStackTrace();
		}
	}


	public List<Container> getContainers() {
		return containers;
	}

	public void setContainers(List<Container> containers) {
		this.containers = containers;
	}

	public void schedule() {
		long interval;
		if (index==containers.size()-2)
			index=0;
		long timeDifference=containers.get(index+1).getTime().getTime()-containers.get(index).getTime().getTime();

		if (intervalRate==-1) {
			interval=1000;
		}else {
			float f =((float) timeDifference)/intervalRate;
			interval=Math.round(f);
		}
		if (interval<0)
			interval=0;
		timer.schedule(new MyClass(containers,tripRTA, tripEffectiveTime,index+1, intervalRate),interval);
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public long getIntervalRate() {
		return intervalRate;
	}

	public void setIntervalRate(long intervalRate) {
		this.intervalRate = intervalRate;
	}

	public Map<Long, Date> getTripRTA() {
		return tripRTA;
	}

	public void setTripRTA(Map<Long, Date> tripRTA) {
		this.tripRTA = tripRTA;
	}

	public Map<Long, Long> getTripEffectiveTime() {
		return tripEffectiveTime;
	}

	public void setTripEffectiveTime(Map<Long, Long> tripEffectiveTime) {
		this.tripEffectiveTime = tripEffectiveTime;
	}









}
