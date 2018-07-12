package it.riccardo.controller;


import java.io.IOException;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import it.riccardo.rest.RestCity;
import it.riccardo.rest.RestISS;

@Controller
public class ControllerIss {

	@Autowired
	private RestISS restISS;

	@Autowired
	private RestCity restCity;


	//return index
	@RequestMapping("/")
	public String getIndex() {
		return "index";
	}


	//return current ISS position
	@RequestMapping("/mappa")
	public String getMappa() throws IOException {
		
		return "map";
	}

	//return current ISS position
	@RequestMapping("/citta")
	public String getFormCitta() {
		return "formCitta";
	}

	//return current ISS crew
	@RequestMapping("/equipaggio")
	public String getEquipaggio(Model model) {
		List<String> equipaggio = this.restISS.getEquipaggio();
		model.addAttribute("equipaggio", equipaggio);
		return "equipaggio";
	}


	@RequestMapping(value = "/ricercaCoordinate",method = RequestMethod.GET)
	public String getProssimiPassaggi(HttpServletRequest request, @RequestParam(value="nomecitta", required=true) String citta, Model model) {
		if(citta=="") {
			String errore = "Errore: Devi inserire il nome di una città!";
			model.addAttribute("errore", errore);
			return "formCitta";
		}
		else {
			List<String>coordinate = this.restCity.getLocationCity(citta);
			System.out.println(coordinate);
			if(coordinate==null) {
				String city = citta.substring(0, 1).toUpperCase() + citta.substring(1);
				String errore = "Errore: La città " + city+ " non esiste";
				model.addAttribute("errore", errore);
				return "formCitta";
			}
			else {
				List<String>prossimiPassaggi = this.restISS.getPassaggi(String.valueOf(coordinate.get(0)), String.valueOf(coordinate.get(1)));
				if(prossimiPassaggi==null) {
					return "errore";
				}
				else {
					String city = citta.substring(0, 1).toUpperCase() + citta.substring(1);
					model.addAttribute("citta",city);
					
					List<String>visibili = this.restCity.getPassaggiVisibili(prossimiPassaggi);
					String orarioTramonto = visibili.get(0);
					String orarioAlba = visibili.get(1);
					visibili.remove(0);
					visibili.remove(0);
					prossimiPassaggi.removeAll(visibili);
					model.addAttribute("passaggi",prossimiPassaggi);
					model.addAttribute("passaggiVisibili",visibili);
					model.addAttribute("orarioTramonto",orarioTramonto);
					model.addAttribute("orarioAlba",orarioAlba);
					return "prossimiPassaggi";
				}
			}
		}
	}



}
