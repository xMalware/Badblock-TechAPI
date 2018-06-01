 package fr.badblock.api.common.utils.time;

import lombok.Getter;

public enum Time {
	NANO_SECOND(1, "ns", "nanoseconde"),
	MICRO_SECOND(NANO_SECOND.getNano() * 1000L, "mcs", "microseconde"),
	MILLIS_SECOND(MICRO_SECOND.getNano() * 1000L, "ms", "milliseconde"),
	SECOND(MILLIS_SECOND.getNano() * 1000L, "s", "seconde"),
	MINUTE(SECOND.getNano() * 60L, "m", "minute"),
	HOUR(MINUTE.getNano() * 60L, "h", "heure"),
	DAY(HOUR.getNano() * 24L, "d", "jour"),
	MONTH(DAY.getNano() * 30L, "mo", "mois"),
	YEAR(DAY.getNano() * 365L, "y", "ann�e");
	
	private static Time[] inOrder = new Time[]{NANO_SECOND, MICRO_SECOND, MILLIS_SECOND, SECOND, MINUTE, HOUR, DAY, MONTH, YEAR};
	
	@Getter private final long nano;
	@Getter private final String recognizer, french;
	
	private Time(long inNano, String recognizer, String french){
		this.nano = inNano;
		this.recognizer = recognizer;
		this.french = french;
	}
	
	public long convert(long value, Time to){
		return (value * getNano()) / to.getNano();
	}
	
	private long[] getValues(long time, Time min, Time max){
		time *= getNano();
		
		long[] values = new long[inOrder.length];
		for(int i=inOrder.length - 1;i>=0;i--){
			Time u = inOrder[i];
			
			if(u.getNano() < min.getNano() || u.getNano() > max.getNano()) continue;
			
			values[i] = time / u.getNano();
			
			time = time % u.getNano();
			if(time == 0) break;
		}
		
		return values;
	}
	
	public String toFrench(long time, Time min, Time max){
		long[] values = getValues(time, min, max);
		
		String result = "";
		boolean first = true;
		
		for(int i=values.length -1;i>=0;i--){
			if(values[i] != 0){
				if(!first) result += " "; else first = false;
				result += values[i] + " " + inOrder[i].getFrench() + (values[i] == 1 || inOrder[i].getFrench().endsWith("s") ? "" : "s");
			}
		}
		
		if(result.isEmpty()) result = 1 + " " + min.getFrench();
		return result;
	}
	
	public String toFrench(long time){
		return toFrench(time, inOrder[0], inOrder[inOrder.length - 1]);
	}
	
	public String toShort(long time, Time min, Time max){
		long[] values = getValues(time, min, max);
		
		String result = "";
		boolean first = true;
		
		for(int i=values.length -1;i>=0;i--){
			if(values[i] != 0){
				if(!first) result += " "; else first = false;
				result += values[i] + inOrder[i].getRecognizer();
			}
		}
		
		if(result.isEmpty()) result = 0 + " " + min.getRecognizer();
		return result;
	}
	
	public String toShort(long time){
		return toShort(time, inOrder[0], inOrder[inOrder.length - 1]);
	}
	
	public long matchTime(String time){
		String valueNumber = "";
		String valueType = "";
		boolean isNumber = true;
		
		for(char c : time.toCharArray()){
			if(isNumber && Character.isDigit(c)){
				valueNumber += c;
			} else {
				isNumber = false;
				valueType += c;
			}
		}
		
		long value = 0;

		try {
			value = Long.parseLong(valueNumber);
		} catch(Exception e){}
		
		Time unit = null;
		for(Time type : values()){
			if(type.getRecognizer().equalsIgnoreCase(valueType)){
				unit = type; break;
			}
		}
		
		if(unit == null) unit = Time.SECOND;
		
		value *= unit.getNano();
		value /= getNano();
		
		return value;
	}
}
