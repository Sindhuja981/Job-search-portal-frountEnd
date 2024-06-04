package com.example.demo.Controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.entity.Company;
import com.example.demo.entity.Vacancies;

import jakarta.servlet.http.HttpSession;



@Controller
@RequestMapping("/company")
public class vancanyController {
	
	@GetMapping("/AddVacancy")
	public ModelAndView AddVacancy(Model model,  HttpSession session) {
		Vacancies Vacancy = new Vacancies();
		model.addAttribute("Vacancy",Vacancy);
		Company Company = (Company) session.getAttribute("company");

		int cid = Company.getCompanyId();
		System.out.println("companyId:::" + cid);
		
		model.addAttribute("companyId", Company.getCompanyId());

		ModelAndView view = new ModelAndView("AddVacancy");
		return view;
	}
	@PostMapping("/AddVacancy")
	public String AddVacancy(@ModelAttribute("Vacancy") Vacancies Vacancy, Model model) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<Vacancies> entity = new HttpEntity<Vacancies>(Vacancy, headers);
		RestTemplate restTemplate = new RestTemplate();
		String url = "http://3.108.252.162:8080/JobNexaProject/api/v1/AddVacancies";
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
		String responseString = response.getBody();
		//System.out.println("responseFlag="+ );
		
		Vacancy = new Vacancies();
		model.addAttribute("Vacancy", Vacancy);
		
		model.addAttribute("responseString", responseString);
		return "redirect:/company/Company";
	}
	@GetMapping("/viewvacancy")
	public ModelAndView viewProducts(Model model, HttpSession session) {
		/*company companys = (company) session.getAttribute("companys");
		int cid = companys.getCompanyId();*/
		
		String Vacancys = "";
		RestTemplate restTemplate = new RestTemplate();

		ResponseEntity<Vacancies[]> responseEntity = restTemplate.getForEntity("http://3.108.252.162:8080/JobNexaProject/api/v1/Allvacancies/",
				Vacancies[].class);
		Vacancies[] responseBody = responseEntity.getBody();
		List<Vacancies> Vacancy = Arrays.asList(responseBody);

		System.out.println("VacancyList" + Vacancys);

		model.addAttribute("VacancyList", Vacancys);
		ModelAndView view = new ModelAndView("viewvacancy");
		return view;
	}
	//////
	
	/////
	@GetMapping("/deletevacancy/{vacancyId}")
	public String deletevacancy(@PathVariable("vacancyId") int vacancyId) {
	
		Map<String, Integer> pathVar = new HashMap<>();
		pathVar.put("vacancyId", vacancyId);
		
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.delete("http://3.108.252.162:8080/JobNexaProject/api/v1/deletevacancy/{vacancyId}",pathVar);
		//ModelAndView view = new ModelAndView("viewProducts");
		//return view;
		return "redirect:/company/getVacanyByCid";
		
	}
	@GetMapping("/getvacancy/{vacancyId}")
	public ModelAndView getvacancy(@PathVariable("vacancyId") int vacancyId,Model model) {
		/*company companys = (company) session.getAttribute("companys");
		int cid = companys.getCompanyId();*/
		
		RestTemplate restTemplate = new RestTemplate();	
		ResponseEntity<Vacancies> responseEntity = restTemplate.getForEntity("http://3.108.252.162:8080/JobNexaProject/api/v1/getvacancy/" + vacancyId,
				Vacancies.class);
		Vacancies Vacancy = responseEntity.getBody();
		System.out.println("JOBTITLE:" + Vacancy.getJobTitle());
		model.addAttribute("Vacancy", Vacancy);
		ModelAndView view = new ModelAndView("editvacancy");
		return view;
	}
	@PostMapping("/updatevacancy")
	public String updatevacancy(@ModelAttribute("Vacancy") Vacancies Vacancy, Model model, HttpSession session) {
		Company company = (Company) session.getAttribute("company");
		Vacancy.setCompany(company);
		int cid = company.getCompanyId();
		System.out.println("companyId:::" + cid);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

		HttpEntity<Vacancies> entity = new HttpEntity<Vacancies>(Vacancy, headers);

		RestTemplate restTemplate = new RestTemplate();

		String url = "http://3.108.252.162:8080/JobNexaProject/api/v1/updatevacancy/"+Vacancy.getVacancyId();
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);

		String responseString = response.getBody();
		System.out.println("responseString" + responseString);

		return "redirect:/company/getVacanyByCid";		
	}
	@GetMapping("/getVacanyByCid")
	public ModelAndView getvacancyBycompanyId(HttpSession session,Model model) {
		Company companys = (Company) session.getAttribute("company");
		int cid = companys.getCompanyId();
		
		RestTemplate restTemplate = new RestTemplate();	
		ResponseEntity<Vacancies[]> responseEntity = restTemplate.getForEntity("http://3.108.252.162:8080/JobNexaProject/api/v1/getvacancyCid/"+cid,
				Vacancies[].class);
	Vacancies[] responseBody = responseEntity.getBody();
		List<Vacancies> VacancyList = Arrays.asList(responseBody);
		System.out.println("VacancyList" + VacancyList);
		model.addAttribute("VacancyList", VacancyList);
		ModelAndView view = new ModelAndView("/viewvacancy");
		return view;
		
	}
	

}
