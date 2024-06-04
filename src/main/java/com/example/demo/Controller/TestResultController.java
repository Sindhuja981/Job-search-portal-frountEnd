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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.entity.Company;
import com.example.demo.entity.JobApply;
import com.example.demo.entity.JobSekers;
import com.example.demo.entity.Questions;
import com.example.demo.entity.TestResult;
import com.example.demo.entity.Vacancies;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller   
@RequestMapping("/jobSeker")
public class TestResultController {
	@GetMapping("/AddTest/{vacancyId}")
	public ModelAndView AddTest(@PathVariable("vacancyId") int vacancyId ,Model model,HttpSession session) {
		
		RestTemplate restTemplate = new RestTemplate();

		ResponseEntity<Questions[]> responseEntity = restTemplate
				.getForEntity("http://3.108.252.162:8080/JobNexaProject/api/v1/getVacancyQuestionById/"+vacancyId, Questions[].class);
		Questions[] responseBody = responseEntity.getBody();
		List<Questions> questionsList = Arrays.asList(responseBody);
		System.out.println("questionsList="+questionsList);

		model.addAttribute("questionsList", questionsList);
		session.setAttribute("questionsList", questionsList);
		
		
		
		ModelAndView view = new ModelAndView("AddExam");
		return view;
	}
	
	/*@PostMapping("/AddExaming")
	public  String AddTest(HttpServletRequest request,HttpSession session) {
		JobSekers jobSekers = (JobSekers) session.getAttribute("JobSekers");
		int job1 = jobSekers.getUserId();
		//////////////
		
		
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<TestResult> entity = new HttpEntity<TestResult>(testresult , headers);
		RestTemplate restTemplate = new RestTemplate();
		String url = "http://localhost:8090/api/v1/getVacancyQuestionById";
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
		String responseString = response.getBody();
		//System.out.println("responseFlag="+ );
		
		testresult = new TestResult();
		model.addAttribute("testresult", testresult);
		//ModelAndView view = new ModelAndView("Jobseker");
		
		model.addAttribute("responseString", responseString);
		return "redirect:/jobSeker/Jobseker";
	}*/
	@PostMapping("/AddExaming")
	public String submitAnswer(HttpServletRequest request, HttpSession session) {
		JobSekers jobSeeker = (JobSekers) session.getAttribute("JobSekers");
	
        int userId = jobSeeker.getUserId();
        System.err.println("User ID: " + userId);
        /////////
		List<Questions> questionsList = (List<Questions>) session.getAttribute("questionsList");
		int totalScore = 0; // I
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

		for (Questions quest : questionsList) {
			int qid = quest.getQuestionId();
            
            //////////////////////////
			Vacancies vacancy = quest.getVacancies();
			////////////////////////////
			System.err.println("vvvvvvvvvvvvvvvvv" + vacancy.getVacancyId());
			String selectedOption = request.getParameter("selectedOptions" + qid);
			System.out.println("qid: " + qid + " selectedoption=" + selectedOption);
			String correctOption = quest.getCorrect();

			System.out.println(
					"qid: " + qid + ", selectedOption: " + selectedOption + ", correctOption: " + correctOption);
            String score= "0";
			String result = "incorrect";
			if (selectedOption != null && selectedOption.equals(correctOption)) {
				result = "correct";
				score=quest.getScore();
	            totalScore += Integer.parseInt(quest.getScore());
	            
	            // Add score to totalScore

			}
			
			TestResult testResult = new TestResult();
			testResult.setJobseker(jobSeeker);
		    testResult.setQuestion(quest);
		    testResult.setVacancies(vacancy);
			testResult.setSelectedOption(selectedOption);
			testResult.setResult(result);
			// Assuming you calculate score somewhere else and set it here
			testResult.setScore(score);
////////////////////////////////////////////////////////////////////////////////////
			// Save the test result
			//setting test result object to end point
			

			HttpEntity<TestResult> entity = new HttpEntity<TestResult>(testResult, headers);


			String url = "http://3.108.252.162:8080/JobNexaProject/api/v1/AddTestResult";
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

			String responseString = response.getBody();
			System.out.println("responseString" + responseString);
			
			String status;
			if (totalScore >= 75) {
			    status = "qualified";
			} else {
			    status = "better luck next time";
			}
			
			System.out.println("totalscore"+totalScore);
			JobApply jobapply= new JobApply();
			jobapply.setStatus(String.valueOf(totalScore));
			
			
			HttpEntity<JobApply> entity1 = new HttpEntity<JobApply>(jobapply, headers);

			String url1 = "http://3.108.252.162:8080/JobNexaProject/api/v1/updatestat/"+userId+"/"+status+"/"+totalScore+"/" + vacancy.getVacancyId();
			ResponseEntity<String> response1 = restTemplate.exchange(url1, HttpMethod.PUT, entity1, String.class);

			String responseString1 = response1.getBody();
			System.out.println("responseString" + responseString1);			
/////////////////////////////////////////////////////////////////
		}
		
		
		
		return "redirect:/jobSeker/Jobseker";
	}
	
	@GetMapping("/viewTestresult")
	public ModelAndView viewTestres(Model model, HttpSession session) {
		JobSekers jobsek = (JobSekers) session.getAttribute("JobSekers");

		int userId = jobsek.getUserId();
		RestTemplate restTemplate = new RestTemplate();

		ResponseEntity<Object[]> responseEntity = restTemplate
				.getForEntity("http://3.108.252.162:8080/JobNexaProject/api/v1/getResultsOfUser/" + userId, Object[].class);
		Object[] responseBody = responseEntity.getBody();
		List<Object> testResultList = Arrays.asList(responseBody);
		System.out.println("testResultList=" + testResultList);

		model.addAttribute("testResultList", testResultList);

		//////////////////////////////////////
		
		ModelAndView view = new ModelAndView("ReportsJob");
		return view;
	}
}

