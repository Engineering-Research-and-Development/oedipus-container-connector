package it.eng.oedipus.container;

import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.Timer;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import it.eng.oedipus.container.model.Container;
import it.eng.oedipus.container.model.Coordinate;
import it.eng.oedipus.container.model.Sector;
import it.eng.oedipus.container.model.Trip;

public class Connector {

    private static Timer timer = new Timer();
	final static Logger logger = Logger.getLogger(Connector.class);

	private static final String INTERVAL_RATE_KEY = "-i";


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<Container> containers=new ArrayList<Container>();
		Container container=new Container();
		Map<Integer, Coordinate> sectorCoordinates=new HashMap<Integer, Coordinate>();
		Map<Integer, Sector> sectors=new HashMap<Integer, Sector>();
		Map<Integer, Trip> trips=new HashMap<Integer, Trip>();

		long intervalRate=-1;
		for(int i=0; i<args.length; i+=2)
		{
			String key = args[i];
			String value = args[i+1];
			

			switch (key)
			{
				case INTERVAL_RATE_KEY : intervalRate = Long.parseLong(value); break;

			}
		}
		
		
		
		try {

			//XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream("/data.xlsx"));
			InputStream is = Connector.class.getClassLoader().getResourceAsStream("it/eng/oedipus/container/data.xlsx");
			//InputStream is=Connector.class.getResource("src/data.xlsx").openStream();
			XSSFWorkbook wb = new XSSFWorkbook(is);

			FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

			XSSFSheet sheetAtRest = wb.getSheetAt(1);
			XSSFRow row;
			XSSFCell cell;

			int rows; // No of rows
			rows = sheetAtRest.getPhysicalNumberOfRows();
			logger.debug("rows="+rows);
			int cols = 0; // No of columns
			int tmp = 0;

			// This trick ensures that we get the data properly even if it doesn't start from first few rows
			for(int i = 0; i < 10 || i < rows; i++) {
				row = sheetAtRest.getRow(i);
				if(row != null) {
					tmp = sheetAtRest.getRow(i).getPhysicalNumberOfCells();
					if(tmp > cols) cols = tmp;
				}
			}
			for(int r = 0; r < rows; r++) {
				logger.debug("-----------------------------");
				row = sheetAtRest.getRow(r);
				if(row != null) {
					for(int c = 0; c < cols; c++) {
						String content=null;
						cell = row.getCell((short)c);
						if(cell != null) {
							Object cellValue=getValue(cell);
							if (cellValue==null) {
								cellValue=readFormulaByType(evaluator, cell, CellType.NUMERIC);
								if (cellValue==null) 
									continue;
								content=cellValue.toString();
							}else {
								content=getValue(cell).toString();
							}



							try {
								if ((r>10)&&(c>0)) {


									DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
									java.util.Date d =  cell.getDateCellValue();
									String date = df.format(d);
									//logger.debug("date is :- "+ buy_date);
									logger.debug((r+1)+"."+(c+1)+" $content="+date);
									if (r>10) {
										Trip trip=trips.get(r);
										trip.setRequestTimeArrival(d);
									}
								}else {
									if ((r==5)&&(c>0)){
										try {
											DateFormat df = new SimpleDateFormat("HH:mm:ss");
											java.util.Date d =  cell.getDateCellValue();
											String buy_date = df.format(d);
											//df.setTimeZone(TimeZone.getTimeZone("UTC"));
											Date date = df.parse(buy_date);
											//Here you can do manually date.getHours()*3600+date.getMinutes*60+date.getSeconds();
											//It's deprecated to use Date class though.
											//Here it goes an original way to do it.
											Calendar time = new GregorianCalendar();
											time.setTime(date);
											time.setTimeZone(TimeZone.getTimeZone("UTC"));
											time.set(Calendar.YEAR,1970); //Epoc year
											time.set(Calendar.MONTH,Calendar.JANUARY); //Epoc month
											time.set(Calendar.DAY_OF_MONTH,1); //Epoc day of month
											long seconds = time.getTimeInMillis()/1000L;




											//logger.debug("date is :- "+ buy_date);
											logger.debug((r+1)+"."+(c+1)+" *content="+seconds);
											if (r==5) {
												Sector sector=sectors.get(c);
												sector.setTravelMeanTime(seconds*1000);
											}

										}
										catch (Exception e) {
											// TODO: handle exception
											//e.printStackTrace();
										}
									}
									else {
										if ((r>5)&&(r<8)&&(c>0)){

											try {
												//DateFormat df = new SimpleDateFormat("HH:mm:ss");
												//java.util.Date d =  cell.getDateCellValue();

												//df.setTimeZone(TimeZone.getTimeZone("UTC"));
												Date date= DateUtil.getJavaDate((double) Double.parseDouble(content));
												//Date date = df.parse(content);

												//Here you can do manually date.getHours()*3600+date.getMinutes*60+date.getSeconds();
												//It's deprecated to use Date class though.
												//Here it goes an original way to do it.
												Calendar time = new GregorianCalendar();
												time.setTime(date);
												time.setTimeZone(TimeZone.getTimeZone("UTC"));
												time.set(Calendar.YEAR,1970); //Epoc year
												time.set(Calendar.MONTH,Calendar.JANUARY); //Epoc month
												time.set(Calendar.DAY_OF_MONTH,1); //Epoc day of month
												long seconds = time.getTimeInMillis()/1000L;




												//logger.debug("date is :- "+ buy_date);
												logger.debug((r+1)+"."+(c+1)+" content="+seconds);
												
												if (r==6) {
													Sector sector=sectors.get(c);
													sector.setMaxThreshold(time.getTimeInMillis());
												}
												if (r==7) {
													Sector sector=sectors.get(c);
													sector.setMinThreshold(time.getTimeInMillis());
												}

											}
											catch (Exception e) {
												// TODO: handle exception
												e.printStackTrace();
												logger.debug("EXC on c="+c);
											}

										}	

										else {

											if (r==3) {
												Sector sector=sectors.get(c);
												sector.setStartPosition(new Coordinate(getValueByType(cell, CellType.STRING).toString()));
											}
											if (r==4) {
												Sector sector=sectors.get(c);
												sector.setEndPosition(new Coordinate(getValueByType(cell, CellType.STRING).toString()));
											}


											logger.debug((r+1)+"."+(c+1)+" +content="+getValueByType(cell, CellType.STRING).toString());
										}
									}
								}

							}
							catch (Exception e) {
								// TODO: handle exception
								logger.debug((r+1)+"."+(c+1)+" _content="+content);
								if (r==2) {
									Sector sector=new Sector();
									double d = Double.parseDouble(content);
									int i = (int) d;
									sector.setId(""+i);
									sectors.put(c, sector);
								}
								if (r>10) {
									logger.debug("put="+r);
									Trip trip=new Trip();
									double d = Double.parseDouble(content);
									long i = (long) d;
									trip.setId(i);
									trips.put(r, trip);
								}
								if ((r==2)&&(c<13)) {
									/*Coordinate coordinate=new Coordinate();
										coordinate.setLatitude(content.split(",")[0]);
										coordinate.setLongitude(content.split(",")[1]);
										sectorCoordinates.put(c, coordinate);*/
								}
							}
						}
					}
				}
			}

			//*************


			XSSFSheet sheetInMotion = wb.getSheetAt(0);
			XSSFRow rowInMotion;
			XSSFCell cellInMotion;


			rows = sheetInMotion.getPhysicalNumberOfRows();
			logger.debug("rows="+rows);
			cols = 0; // No of columns
			tmp = 0;

			// This trick ensures that we get the data properly even if it doesn't start from first few rows
			for(int i = 0; i < 10 || i < rows; i++) {
				rowInMotion = sheetInMotion.getRow(i);
				if(rowInMotion != null) {
					tmp = sheetInMotion.getRow(i).getPhysicalNumberOfCells();
					if(tmp > cols) cols = tmp;
				}
			}

			for(int r = 0; r < rows; r++) {
				logger.debug("-----------------------------");
				/*long tripTime=0;
				long maxTripTime=0;
				long minTripTime=0;*/
				long firstSectorTime=0;
				Set<String> sectorsInstance=new HashSet<String>();
				int sectorsInstanceDuplicateCount=0;
				rowInMotion = sheetInMotion.getRow(r);
				if(rowInMotion != null) {
					for(int c = 0; c < 13; c++) {
						boolean found=false;
						cellInMotion = rowInMotion.getCell((short)c);
						if(cellInMotion != null) {
							// Your code here

							Object cellValue=getValue(cellInMotion);
							if (cellValue==null)
								continue;
							String content=getValue(cellInMotion).toString();
							/*if ((r==0) && (c==0))
							{
								containerContent=content;
								
							}*/


							try {
								DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSSZ");
								java.util.Date d =  cellInMotion.getDateCellValue();
								String date = df.format(d);
								//logger.debug("date is :- "+ buy_date);
								logger.debug((r+1)+"."+(c+1)+" &content="+date);
								found=true;
								if ((r>2)&&(c<13)) {
									container=new Container();
									container.setId(1l);
									container.setContent("AIRBAG - TRW");
									//Integer sectorId=c+1;
									
									//Sector sector=sectors.get(sectorId);
									Sector sector=getSectorByCoordinates(sectors, sectorCoordinates.get(c).getLatitude(), sectorCoordinates.get(c).getLongitude());
									container.setSectorId(sector.getId());
									
								
									container.setTravelMeanTime(sector.getTravelMeanTime());
									container.setMaxThreshold(sector.getMaxThreshold());
									container.setMinThreshold(sector.getMinThreshold());
									container.setLatitude(sector.getStartPosition().getLatitude());
									container.setLongitude(sector.getStartPosition().getLongitude());
									container.setTime(d);
									
									/*if (!sector.getId().equals("1")) {
										Date previousTime=containers.get(containers.size()-1).getTime();
										Long maxThreshold=containers.get(containers.size()-1).getMaxThreshold();
										Long differenceMilliSeconds=(d.getTime()-previousTime.getTime());
										Date previousETA=containers.get(containers.size()-1).getExtimatedTimeArrival();
										System.out.println("differenceMilliSeconds="+differenceMilliSeconds);
										if (intervalRate!=-1) {
											differenceMilliSeconds=differenceMilliSeconds/1000;
										}
										if (maxThreshold!=null)
											System.out.println("sector.getMaxThreshold()="+maxThreshold);
										if (maxThreshold!=null) {
										if (differenceMilliSeconds>maxThreshold) {
											Long extimatedTime=previousETA.getTime()+differenceMilliSeconds;
											container.setExtimatedTimeArrival(new Date(extimatedTime));
											logger.debug("EXTIMATED TIME CHANGED="+new Date(extimatedTime));
										}
										else
											container.setExtimatedTimeArrival(previousETA);
										}else
											container.setExtimatedTimeArrival(previousETA);
										
									}*/
									long etaTime=d.getTime()+getResidualRTABySectorId(sectors,container.getSectorId());
									container.setExtimatedTimeArrival(new Date(etaTime));
									
									if (sector.getId().equals("1")) {
										container.setTripTime(0l);
										container.setMaxTripTime(0l);
										container.setMinTripTime(0l);
										firstSectorTime=d.getTime();
									}else {
										container.setTripTime(d.getTime()-firstSectorTime);
										
										if (sectorsInstance.contains(sector.getId())) {
											sectorsInstanceDuplicateCount++;
										}
											container.setMaxTripTime(getCumulatedMaxTripTimeBySectorId(sectors, sector.getId(),sectorsInstanceDuplicateCount));
											container.setMinTripTime(getCumulatedMinTripTimeBySectorId(sectors, sector.getId(),sectorsInstanceDuplicateCount));
										
										sectorsInstance.add(sector.getId());
									}
									
									
									
									
									
									if (intervalRate!=-1) {
										if (sector.getTravelMeanTime()!=null)
											container.setTravelMeanTime(sector.getTravelMeanTime()/intervalRate);
										if (sector.getMaxThreshold()!=null)
											container.setMaxThreshold(sector.getMaxThreshold()/intervalRate);
										if (sector.getMinThreshold()!=null)
											container.setMinThreshold(sector.getMinThreshold()/intervalRate);
										container.setTripTime(container.getTripTime()/intervalRate);
										container.setMaxTripTime(container.getMaxTripTime()/intervalRate);
										container.setMinTripTime(container.getMinTripTime()/intervalRate);
	
										//container.setTime(null);

									}
									
									
									cellInMotion = rowInMotion.getCell((short)13);
									if(cellInMotion != null) {
										// Your code here

										cellValue=getValue(cellInMotion);
										try {
											double cellValueDouble = Double.parseDouble(cellValue.toString());

											Long id= (long) cellValueDouble;
											Trip trip=getTripByID(trips, id);
											container.setTripId(trip.getId());
											container.setRequestTimeArrival(trip.getRequestTimeArrival());
											if (intervalRate!=-1) {
												//long timeDifference=trip.getRequestTimeArrival().getTime()-container.getTime().getTime();
												//long timeDifferenceOffset=timeDifference/intervalRate;
												//long newRTA=container.getTime().getTime()+timeDifferenceOffset;
												//container.setRequestTimeArrival(new Date(newRTA));
												//container.setExtimatedTimeArrival(new Date(newRTA));
												
												container.setRequestTimeArrivalOffset((container.getRequestTimeArrival().getTime()-container.getTime().getTime())/intervalRate);
												//trip.setRequestTimeArrival(new Date(newRTA));
						
											}
											if (sector.getId().equals("1")) {
												//container.setExtimatedTimeArrival(trip.getRequestTimeArrival());
												if (intervalRate!=-1) {
													long timeDifference=trip.getRequestTimeArrival().getTime()-container.getTime().getTime();
													long timeDifferenceOffset=timeDifference/intervalRate;
													//long newRTA=container.getTime().getTime()+timeDifferenceOffset;
													//container.setRequestTimeArrival(new Date(newRTA));
													//container.setExtimatedTimeArrival(new Date(newRTA));
													container.setRequestTimeArrivalOffset(timeDifferenceOffset);
													trip.setRequestTimeArrivalOffset(timeDifferenceOffset);
							
												}
											}
											
										}catch (Exception e) {
											// TODO: handle exception
											e.printStackTrace();
										}

									}
									
								}



							}
							catch (Exception e) {
								// TODO: handle exception
								if (r!=2)
									e.printStackTrace();
								logger.debug((r+1)+"."+(c+1)+" @content="+content);
								if ((r==2)&&(c<13)) {
									Coordinate coordinate=new Coordinate();
									coordinate.setLatitude(content.split(",")[0]);
									coordinate.setLongitude(content.split(",")[1]);
									sectorCoordinates.put(c, coordinate);
								}
							}
						}
						
						if ((container.getTripId()!=null)&&(found)) {
							System.out.println("registro trip="+container.getTripId() + " sector="+container.getSectorId());
							containers.add(container);
						}
					}
					

				}
			}
			wb.close();
		} catch(Exception ioe) {
			ioe.printStackTrace();
		}
		logger.debug("read "+containers.size() +" entries");
		timer.schedule(new MyClass(containers, 0, intervalRate),0);
		
		
		
	}

	
	private static long getCumulatedMaxTripTimeBySectorId(Map<Integer,Sector> sectors, String sectorId, int subSector) {
		long cumulatedMaxTripTime=0;
		int id=Integer.parseInt(sectorId);
		for (int i=1; i<(id+subSector); i++) {
			cumulatedMaxTripTime+=sectors.get(i).getMaxThreshold()==null?0:sectors.get(i).getMaxThreshold();
		}
		
		return cumulatedMaxTripTime;
	}
	
	private static long getCumulatedMinTripTimeBySectorId(Map<Integer,Sector> sectors, String sectorId, int subSector) {
		long cumulatedMinTripTime=0;
		int id=Integer.parseInt(sectorId);
		for (int i=1; i<(id+subSector); i++) {
			cumulatedMinTripTime+=sectors.get(i).getMinThreshold()==null?0:sectors.get(i).getMinThreshold();
		}
		return cumulatedMinTripTime;
	}
	
	private static long getResidualRTABySectorId(Map<Integer,Sector> sectors, String sectorId) {
		long residualRTA=0;
		int id=Integer.parseInt(sectorId);
		for (int i=id; i<=12; i++) {
			residualRTA+=sectors.get(i).getTravelMeanTime();
		}
			
		
		return residualRTA;
	}
	
	
	private static Sector getSectorByCoordinates(Map<Integer,Sector> sectors, String latitude, String longitude) {
		for (Integer key:sectors.keySet()) {
			Sector sector=sectors.get(key);
			if (sector.getStartPosition().getLatitude().equals(latitude))
				if (sector.getStartPosition().getLongitude().equals(longitude))
					return sector;
			if (key==12) {
				if (sector.getEndPosition().getLongitude().equals(longitude))
					return sector;
			}
		}
		return null;
	}
	

	private static Trip getTripByID(Map<Integer, Trip> trips, Long id) {
		
		for (Integer key:trips.keySet()) {
			Trip trip=trips.get(key);
			if (trip.getId()==id) {
				return trip;
			}
		}
		return null;
	}

	private static Object getValue(XSSFCell cell) {
		if (cell != null) {
			CellType cellType = cell.getCellType();

			switch (cellType) {
			case NUMERIC:
				return cell.getNumericCellValue();
			case BOOLEAN:
				return cell.getBooleanCellValue();
			case BLANK:
				return null;
			default:
				try {
					return cell.getStringCellValue();
				} catch (IllegalStateException mismatch) {
					return null;
				}
			}
		} else {
			return null;
		}
	}


	private static Object getValueByType(XSSFCell cell, CellType type) {
		if (cell != null) {
			switch (type) {
			case NUMERIC:
				return cell.getNumericCellValue();
			case BOOLEAN:
				return cell.getBooleanCellValue();
			case BLANK:
				return null;
			default:
				try {
					return cell.getStringCellValue();
				} catch (IllegalStateException mismatch) {
					return null;
				}
			}
		} else {
			return null;
		}
	}



	public static Object readFormula( FormulaEvaluator evaluator, Cell cell) {


		Object object = null;

		CellValue cellValue = evaluator.evaluate(cell);
		if (cellValue==null)
			return cellValue;
		switch (cellValue.getCellType()) {
		case BOOLEAN:
			object=cellValue.getBooleanValue();
			break;
		case NUMERIC:
			object=cellValue.getNumberValue();
			break;
		case STRING:
			object=cellValue.getStringValue();
			break;
		case BLANK:
			break;
		case ERROR:
			break;

			// CELL_TYPE_FORMULA will never happen
		case FORMULA:
			break;
		default:
			break;
		}
		return object;

	}

	public static Object readFormulaByType( FormulaEvaluator evaluator, Cell cell, CellType type) {


		Object object=null;

		CellValue cellValue = evaluator.evaluate(cell);
		if (cellValue==null)
			return cellValue;
		switch (type) {
		case BOOLEAN:
			object=cellValue.getBooleanValue();
			break;
		case NUMERIC:
			object=cellValue.getNumberValue();
			break;
		case STRING:
			object=cellValue.getStringValue();
			break;
		case BLANK:
			break;
		case ERROR:
			break;

			// CELL_TYPE_FORMULA will never happen
		case FORMULA:
			break;
		default:
			break;
		}
		return object;

	}

	
	
	
	
}
