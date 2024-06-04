package com.example.demo.Controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.aspectj.weaver.patterns.TypePatternQuestions.Question;
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
import com.example.demo.entity.Questions;
import com.example.demo.entity.Test;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/company")
public class questionController {
	
	@GetMapping("/addQuestion")
	public ModelAndView AddTest(Model model, HttpSession session) {
		Questions questions = new Questions();
		model.addAttribute("Questions", questions);
		Company company = (Company) session.getAttribute("company");

		int cid = company.getCompanyId();
		System.out.println("companyId:::" + cid);
		
///////////
		RestTemplate restTemplate = new RestTemplate();
		
		ResponseEntity<Object[]> responseEntity1 = restTemplate
				.getForEntity("http://3.108.252.162:8080/JobNexaProject/api/v1/vacancyIdAndJobtitle/" + cid, Object[].class);
		Object[] responseBody1 = responseEntity1.getBody();
		List<Object> vacancyList = Arrays.asList(responseBody1);

		System.out.println("****=" + vacancyList);
		model.addAttribute("vacancyList", vacancyList);
//////
		ResponseEntity<Test[]> responseEntity = restTemplate.getForEntity("http://3.108.252.162:8080/JobNexaProject/api/v1/getTestId/"+cid,
				Test[].class);
		Test[] responseBody = responseEntity.getBody();
		List<Test> TestList = Arrays.asList(responseBody);

		System.out.println("TestList" + TestList);

		model.addAttribute("TestList", TestList);
		

		/*ResponseEntity<List> responseEntity = restTemplate
			    .getForEntity("http://localhost:8090/api/v1/getTestId/" + cid, List.class);
			List<Integer> responseBody = responseEntity.getBody();
		List<List<Integer>> questionList = Arrays.asList(responseBody);

		System.out.println("chand=" + questionList);
		
		model.addAttribute("TestList", questionList);*/
		
		
		ModelAndView view = new ModelAndView("AddQuestion");
		return view;
	}
	@PostMapping("/AddingQuestion")
	public ModelAndView AddVacancy(@ModelAttribute("Questions") Questions questions, Model model) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<Questions> entity = new HttpEntity<Questions>(questions, headers);
		RestTemplate restTemplate = new RestTemplate();
		String url = "http://3.108.252.162:8080/JobNexaProject/api/v1/AddQuestion";
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
		String responseString = response.getBody();
		//System.out.println("responseFlag="+ );
		
		questions = new Questions();
		model.addAttribute("Questions", questions);
		ModelAndView view = new ModelAndView("Company");
		model.addAttribute("responseString", responseString);
		return view;
	}
	
	@GetMapping("/ViewQuestions")
	public ModelAndView getQuestionById(HttpSession session,Model model) {
		Company companys = (Company) session.getAttribute("company");
		int cid = companys.getCompanyId();
		
		
		RestTemplate restTemplate = new RestTemplate();	
		ResponseEntity<Questions[]> responseEntity = restTemplate.getForEntity("http://3.108.252.162:8080/JobNexaProject/api/v1/getQuestionById/"+cid,
				Questions[].class);
		Questions[] responseBody = responseEntity.getBody();
		List<Questions> QuestionsList = Arrays.asList(responseBody);

		System.out.println("QuestionsList" + QuestionsList);

		model.addAttribute("QuestionsList", QuestionsList);
		ModelAndView view = new ModelAndView("viewQuestions");
		return view;
		
	}
	/////for edit and delete
	@GetMapping("/getQuestionById/{questionId}")
	public ModelAndView gettestById(@PathVariable("questionId") int questionId,Model model, HttpSession session) {
		Company companys = (Company) session.getAttribute("company");
		int cid = companys.getCompanyId();	
		
		RestTemplate restTemplate = new RestTemplate();	
		ResponseEntity<Questions> responseEntity = restTemplate.getForEntity("http://3.108.252.162:8080/JobNexaProject/api/v1/getQuestions/" + questionId,
				Questions.class);
		Questions questions = responseEntity.getBody();
		System.out.println("JOBTITLE:" + questions.getQuestionId());
		model.addAttribute("Questions", questions);
		///////////

		ResponseEntity<Object[]> responseEntity1 = restTemplate
				.getForEntity("http://3.108.252.162:8080/JobNexaProject/api/v1/vacancyIdAndJobtitle/" + cid, Object[].class);
		Object[] responseBody1 = responseEntity1.getBody();
		List<Object> vacancyList = Arrays.asList(responseBody1);

		System.out.println("****=" + vacancyList);
		model.addAttribute("vacancyList", vacancyList);
//////
		ResponseEntity<Test[]> responseEntity2 = restTemplate.getForEntity("http://3.108.252.162:8080/JobNexaProject/api/v1/getCompanyId/"+cid,
				Test[].class);
		Test[] responseBody = responseEntity2.getBody();
		List<Test> TestList = Arrays.asList(responseBody);

		System.out.println("TestList" + TestList);

		model.addAttribute("TestList", TestList);
		
		ModelAndView view = new ModelAndView("editQuestion");
		return view;
	}@PostMapping("/updateQuestion")
	public String updateTest(@ModelAttribute("questionId") Questions question, Model model, HttpSession session) {
		/*Company company = (Company) session.getAttribute("company");
		Vacancy.setCompany(company);
		int cid = company.getCompanyId();
		System.out.println("companyId:::" + cid);*/
		
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

		HttpEntity<Questions> entity = new HttpEntity<Questions>(question, headers);

		RestTemplate restTemplate = new RestTemplate();

		String url = "http://3.108.252.162:8080/JobNexaProject/api/v1/updateQuestion/"+question.getQuestionId();
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);

		String responseString = response.getBody();
		System.out.println("responseString" + responseString);

		return "redirect:/company/ViewQuestions";		
	}
	@GetMapping("/deleteQuestion/{questionId}")
	public String deletevacancy(@PathVariable("questionId") int questionId) {
	
		Map<String, Integer> pathVar = new HashMap<>();
		pathVar.put("questionId", questionId);
		
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.delete("http://3.108.252.162:8080/JobNexaProject/api/v1/deleteQuestions/{questionId}",pathVar);
		//ModelAndView view = new ModelAndView("viewProducts");
		//return view;
		return "redirect:/company/ViewQuestions";
		
	}
	
	

}
