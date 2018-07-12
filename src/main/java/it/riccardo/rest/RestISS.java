package it.riccardo.rest;



import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.jayway.jsonpath.JsonPath;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;




@Component
public class RestISS {
	
	//return current ISS crew
	public List<String> getEquipaggio() {
			RestTemplate restTemplate = new RestTemplate(); 
			String response = restTemplate.getForObject("http://api.open-notify.org/astros.json", String.class);        
			List<String> name = JsonPath.read(response, "$..people[*].name");
			return name;
	}
	
	//return ISS pass times 
	public List<String> getPassaggi(String latitudine,String longitudine){
		String uri = "http://api.open-notify.org/iss-pass.json?lat=";
		String uri_2 = "&lon=";
		String numbers = "&n=8";
		RestTemplate restTemplate = new RestTemplate(); 
		try {
			String response = restTemplate.getForObject(uri+latitudine+uri_2+longitudine+numbers, String.class);        
			List<String> datesUnixTime = JsonPath.read(response, "$..response[*].risetime");
			List<String> dates = new ArrayList<>();
			for(int i = 0;i<datesUnixTime.size();i++) {
				String s = String.valueOf(datesUnixTime.get(i));
				final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
				final long unixTime = Long.parseLong(s);
				final String formattedDtm = Instant.ofEpochSecond(unixTime).atZone(ZoneId.of("GMT+2")).format(formatter);
				dates.add(formattedDtm);
			}
			return dates;
		}
		catch(Exception e) {
			return null;
		}
	}
	
	
}
