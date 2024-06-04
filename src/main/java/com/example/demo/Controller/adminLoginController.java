package com.example.demo.Controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.entity.Admin;
import com.example.demo.entity.Company;
import com.example.demo.entity.JobSekers;
import com.example.demo.entity.SelectedCandidates;
import com.example.demo.entity.Test;
import com.example.demo.entity.TestResult;
import com.example.demo.entity.Vacancies;

@Controller
@RequestMapping("/admin")
public class adminLoginController {
	
	
	@GetMapping("/AdminLogin")
	public ModelAndView adminLogin(Model model) {
		Admin admin = new Admin();
		model.addAttribute("Admin", admin);
		ModelAndView view = new ModelAndView("AdminLogin");
		return view;
	}
	/*@PostMapping("/ValidateAdminLogin")
	public ModelAndView validateAdminLogin(@ModelAttribute("admin") Admin admin, Model model) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		
		HttpEntity<Admin> entity = new HttpEntity<Admin>(admin,headers);
		
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<Boolean> response = restTemplate.exchange("http://localhost:8090/api/v1/adminLogin", HttpMethod.POST, entity, Boolean.class);
		
		boolean responseFlag=  response.getBody();
		System.out.println("responseFlag=="+responseFlag);
		
		if(responseFlag==true) {
			ModelAndView view = new ModelAndView("adminHome");
			return view;
		}else {
			model.addAttribute("responseText","Username/Password incorrect...");
			ModelAndView view = new ModelAndView("/AdminLogin");
			return view;
		}
	}*/
	@GetMapping("/adminHome")
	public ModelAndView adminHome() {
		ModelAndView view = new ModelAndView("adminHome");
		return view;
		
	}
	@GetMapping("/adminvacancy1")
	public ModelAndView viewvacancy(Model model) {
		/*company companys = (company) session.getAttribute("companys");
		int cid = companys.getCompanyId();*/
		
		String Vacancys = "";
		RestTemplate restTemplate = new RestTemplate();

		ResponseEntity<Vacancies[]> responseEntity = restTemplate.getForEntity("http://3.108.252.162:8080/JobNexaProject/api/v1/Allvacancies",
				Vacancies[].class);
		Vacancies[] responseBody = responseEntity.getBody();
		List<Vacancies> Vacancy = Arrays.asList(responseBody);

		System.out.println("VacancyList+++++++++++" + Vacancys);

		model.addAttribute("VacancyList", Vacancy);
		ModelAndView view = new ModelAndView("adminvacancy");
		return view;
	}
	@GetMapping("/adminJobseker")
	public ModelAndView adminJobseker(Model model) {
		/*company companys = (company) session.getAttribute("companys");
		int cid = companys.getCompanyId();*/
		
		String Vacancys = "";
		RestTemplate restTemplate = new RestTemplate();

		ResponseEntity<JobSekers[]> responseEntity = restTemplate.getForEntity("http://3.108.252.162:8080/JobNexaProject/api/v1/allJobSekers",
				JobSekers[].class);
		JobSekers[] responseBody = responseEntity.getBody();
		List<JobSekers> Vacancy = Arrays.asList(responseBody);

		System.out.println("VacancyList+++++++++++" + Vacancys);

		model.addAttribute("VacancyList", Vacancy);
		ModelAndView view = new ModelAndView("adminJobseker");
		return view;
	}
	@GetMapping("/admincompany")
	public ModelAndView admincompany(Model model) {
		/*company companys = (company) session.getAttribute("companys");
		int cid = companys.getCompanyId();*/
		
		String Vacancys = "";
		RestTemplate restTemplate = new RestTemplate();

		ResponseEntity<Company[]> responseEntity = restTemplate.getForEntity("http://3.108.252.162:8080/JobNexaProject/api/v1/allCompanys",
				Company[].class);
		Company[] responseBody = responseEntity.getBody();
		List<Company> Vacancy = Arrays.asList(responseBody);

		System.out.println("VacancyList+++++++++++" + Vacancys);

		model.addAttribute("VacancyList", Vacancy);
		ModelAndView view = new ModelAndView("admincompany");
		return view;
	}
	@GetMapping("/adminTest")
	public ModelAndView adminTest(Model model) {
		/*company companys = (company) session.getAttribute("companys");
		int cid = companys.getCompanyId();*/
		
		String Vacancys = "";
		RestTemplate restTemplate = new RestTemplate();

		ResponseEntity<Test[]> responseEntity = restTemplate.getForEntity("http://3.108.252.162:8080/JobNexaProject/api/v1/Alltest",
				Test[].class);
		Test[] responseBody = responseEntity.getBody();
		List<Test> Vacancy = Arrays.asList(responseBody);

		System.out.println("VacancyList+++++++++++" + Vacancys);

		model.addAttribute("VacancyList", Vacancy);
		ModelAndView view = new ModelAndView("adminTest");
		return view;
	}
	@GetMapping("/adminSelectedCandidates")
	public ModelAndView adminTest1(Model model) {
		/*company companys = (company) session.getAttribute("companys");
		int cid = companys.getCompanyId();*/
		
		String Vacancys = "";
		RestTemplate restTemplate = new RestTemplate();

		ResponseEntity<SelectedCandidates[]> responseEntity = restTemplate.getForEntity("http://3.108.252.162:8080/JobNexaProject/api/v1/AllselectedCandidates",
				SelectedCandidates[].class);
		SelectedCandidates[] responseBody = responseEntity.getBody();
		List<SelectedCandidates> Vacancy = Arrays.asList(responseBody);

		System.out.println("VacancyList+++++++++++" + Vacancys);

		model.addAttribute("VacancyList", Vacancy);
		ModelAndView view = new ModelAndView("adminSelectedCandidates");
		return view;
	}
	@GetMapping("/adminTestFResult")
	public ModelAndView adminTestFResult(Model model) {
		/*company companys = (company) session.getAttribute("companys");
		int cid = companys.getCompanyId();*/
		
		String Vacancys = "";
		RestTemplate restTemplate = new RestTemplate();

		ResponseEntity<TestResult[]> responseEntity = restTemplate.getForEntity("http://3.108.252.162:8080/JobNexaProject/api/v1/AllTestResult",
				TestResult[].class);
		TestResult[] responseBody = responseEntity.getBody();
		List<TestResult> Vacancy = Arrays.asList(responseBody);

		System.out.println("VacancyList+++++++++++" + Vacancys);

		model.addAttribute("VacancyList", Vacancy);
		ModelAndView view = new ModelAndView("adminTestFResult");
		return view;
	}
}


