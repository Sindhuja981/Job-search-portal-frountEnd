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
import com.example.demo.entity.Test;
import com.example.demo.entity.Vacancies;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/company")
public class TestController {
	
	@GetMapping("/addTest")
	public ModelAndView AddTest(Model model, HttpSession session) {
		Test test = new Test();
		model.addAttribute("Test", test);
		Company company = (Company) session.getAttribute("company");

		int cid = company.getCompanyId();
		System.out.println("companyId:::" + cid);

		RestTemplate restTemplate = new RestTemplate();

		ResponseEntity<Object[]> responseEntity = restTemplate
				.getForEntity("http://3.108.252.162:8080/JobNexaProject/api/v1/vacancyIdAndJobtitle/" + cid, Object[].class);
		Object[] responseBody = responseEntity.getBody();
		List<Object> vacancyList = Arrays.asList(responseBody);

		System.out.println("chand=" + vacancyList);
		model.addAttribute("vacancyList", vacancyList);
		ModelAndView view = new ModelAndView("AddTest");
		return view;
	}
	@PostMapping("/addingTest")
	public ModelAndView AddVacancy(@ModelAttribute("Test") Test test, Model model) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<Test> entity = new HttpEntity<Test>(test, headers);
		RestTemplate restTemplate = new RestTemplate();
		String url = "http://3.108.252.162:8080/JobNexaProject/api/v1/AddTest";
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
		String responseString = response.getBody();
		//System.out.println("responseFlag="+ );
		
		test = new Test();
		model.addAttribute("Test", test);
		ModelAndView view = new ModelAndView("Company");
		model.addAttribute("responseString", responseString);
		return view;
	}
	@GetMapping("/getTestByVacancyId")
	public ModelAndView getTestByVacancyId(HttpSession session,Model model) {
		Company companys = (Company) session.getAttribute("company");
		int cid = companys.getCompanyId();	
		
		RestTemplate restTemplate = new RestTemplate();	
		ResponseEntity<Test[]> responseEntity = restTemplate.getForEntity("http://3.108.252.162:8080/JobNexaProject/api/v1/getTestId/"+cid,
				Test[].class);
		
		Test[] responseBody = responseEntity.getBody();
		List<Test> TestList = Arrays.asList(responseBody);

		System.out.println("TestList" + TestList);
		
		
		model.addAttribute("TestList", TestList);
		ModelAndView view = new ModelAndView("viewTest");
		return view;
		
	}
	/////
	/*@GetMapping("/gettest/{testId}")
	public ModelAndView gettestById(@PathVariable("testId") int testId,Model model, HttpSession session) {
		Test test = new Test();
		test.getTestId();
		Test tests = (Test) session.getAttribute("test");
		int tid = test.getTestId();
		System.out.println("companyId:::" + tid);
		model.addAttribute("Test", test);
		Company company = (Company) session.getAttribute("company");

		int cid = company.getCompanyId();
		System.out.println("companyId:::" + cid);

		RestTemplate restTemplate = new RestTemplate();

		ResponseEntity<Object[]> responseEntity = restTemplate
				.getForEntity("http://localhost:8090/api/v1/gettests/" + tid, Object[].class);
		Object[] responseBody = responseEntity.getBody();
		List<Object> vacancyList = Arrays.asList(responseBody);

		System.out.println("chand=" + vacancyList);
		model.addAttribute("vacancyList", vacancyList);
		ModelAndView view = new ModelAndView("editTest");
		return view;
	}*/
	
	

	@GetMapping("/gettest/{testId}")
	public ModelAndView gettestById(@PathVariable("testId") int testId,Model model, HttpSession session) {
		Company companys = (Company) session.getAttribute("company");
		int cid = companys.getCompanyId();	
		
		RestTemplate restTemplate = new RestTemplate();	
		ResponseEntity<Test> responseEntity = restTemplate.getForEntity("http://3.108.252.162:8080/JobNexaProject/api/v1/gettests/" + testId,
				Test.class);
		Test test = responseEntity.getBody();
		System.out.println("JOBTITLE:" + test.getTestId());
		model.addAttribute("Test", test);
		///////////
		ResponseEntity<Object[]> responseEntity1 = restTemplate
				.getForEntity("http://3.108.252.162:8080/JobNexaProject/api/v1/vacancyIdAndJobtitle/" + cid, Object[].class);
		Object[] responseBody = responseEntity1.getBody();
		List<Object> vacancyList = Arrays.asList(responseBody);

		System.out.println("chand=" + vacancyList);
		model.addAttribute("vacancyList", vacancyList);
		
		ModelAndView view = new ModelAndView("editTest");
		return view;
	}
	@PostMapping("/updateTest")
	public String updateTest(@ModelAttribute("testId") Test test, Model model, HttpSession session) {
		/*Company company = (Company) session.getAttribute("company");
		Vacancy.setCompany(company);
		int cid = company.getCompanyId();
		System.out.println("companyId:::" + cid);*/
		
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

		HttpEntity<Test> entity = new HttpEntity<Test>(test, headers);

		RestTemplate restTemplate = new RestTemplate();

		String url = "http://3.108.252.162:8080/JobNexaProject/api/v1/updateTest/"+test.getTestId();
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);

		String responseString = response.getBody();
		System.out.println("responseString" + responseString);

		return "redirect:/company/getTestByVacancyId";		
	}
	@GetMapping("/deletetest/{testId}")
	public String deletevacancy(@PathVariable("testId") int testId) {
	
		Map<String, Integer> pathVar = new HashMap<>();
		pathVar.put("testId", testId);
		
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.delete("http://3.108.252.162:8080/JobNexaProject/api/v1/deleteTest/{testId}",pathVar);
		//ModelAndView view = new ModelAndView("viewProducts");
		//return view;
		return "redirect:/company/getTestByVacancyId";
		
	}
}
