package it.riccardo.rest;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.jayway.jsonpath.JsonPath;

@Component
public class RestCity {


	public List<String> getLocationCity(String city){
		String uri = "https://maps.googleapis.com/maps/api/geocode/json?&address=";
		String key = "&key=AIzaSyCiOSXdaWkN0mmmIICXt0sn9inD9-t6Lyc";

		RestTemplate restTemplate = new RestTemplate(); 
		String response = restTemplate.getForObject(uri+city+"&language=it"+key, String.class); 
		List<String> status = JsonPath.read(response, "$..status");
		String statusResponse = String.valueOf(status.get(0));
		if(!statusResponse.equals("OK")) {
			//return null if city doesn't exit or other problem

			return null;
		}
		else {
			//return lat and long
			List<String> location = JsonPath.read(response, "$..location..lat");
			List<String>location1 = JsonPath.read(response, "$..location..lng");
			location.addAll(location1);
			return location;
		}
		
	}

	public List<String> getPassaggiVisibili(List<String>passaggi){

		//prendi orario e tramonto +2 nell'ora perchè è con l'orario di ROMA
		RestTemplate restTemplate = new RestTemplate(); 
		String response = restTemplate.getForObject("https://api.sunrise-sunset.org/json?lat=42.1424700&lng=12.5451300&date=today", String.class);        
		List<String> alba = JsonPath.read(response, "$..results.sunrise");
		String orarioAlba = alba.get(0).substring(0, 7);
		String[] parts = orarioAlba.split(":");
		parts = orarioAlba.split(":");
		Calendar calendarioAlba = Calendar.getInstance();
		calendarioAlba.set(Calendar.HOUR_OF_DAY, Integer.parseInt(parts[0])+2);
		calendarioAlba.set(Calendar.MINUTE, Integer.parseInt(parts[1]));
		calendarioAlba.set(Calendar.SECOND, Integer.parseInt(parts[2]));
		

		List<String> tramonto = JsonPath.read(response, "$..results.sunset");

		String orarioTramonto = tramonto.get(0).substring(0, 7);
		parts = orarioTramonto.split(":");
		Calendar calendarioTramonto = Calendar.getInstance();
		calendarioTramonto.set(Calendar.HOUR_OF_DAY, Integer.parseInt(parts[0])+2+12);
		calendarioTramonto.set(Calendar.MINUTE, Integer.parseInt(parts[1]));
		calendarioTramonto.set(Calendar.SECOND, Integer.parseInt(parts[2]));

		List<String>visibili = new ArrayList<>();
		
		int oraTramonto = calendarioTramonto.get(Calendar.HOUR_OF_DAY);
		int oraAlba = calendarioAlba.get(Calendar.HOUR_OF_DAY);

		String tramontoRisultato = String.valueOf(calendarioTramonto.get(Calendar.HOUR_OF_DAY)+":"+calendarioTramonto.get(Calendar.MINUTE));
		String albaRisultato = String.valueOf(calendarioAlba.get(Calendar.HOUR_OF_DAY)+":"+calendarioAlba.get(Calendar.MINUTE));
		visibili.add(tramontoRisultato);
		visibili.add(albaRisultato);
		
		//questa parte va ripetuta per ogni orario
		for(int i=0;i<passaggi.size();i++) {
			String orario = passaggi.get(i).substring(11, 19);
			parts = orario.split(":");
			Calendar calendarioPassaggio = Calendar.getInstance();
			calendarioPassaggio.set(Calendar.HOUR_OF_DAY, Integer.parseInt(parts[0]));
			calendarioPassaggio.set(Calendar.MINUTE, Integer.parseInt(parts[1]));
			calendarioPassaggio.set(Calendar.SECOND, Integer.parseInt(parts[2]));

			

			if (calendarioPassaggio.get(Calendar.HOUR_OF_DAY)>oraTramonto) {
				visibili.add(passaggi.get(i));
			}
			else if(calendarioPassaggio.get(Calendar.HOUR_OF_DAY)<oraAlba){
				visibili.add(passaggi.get(i));
			}

		}
		return visibili;
	}


}
