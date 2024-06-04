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
import com.example.demo.entity.Docunments;
import com.example.demo.entity.JobApply;
import com.example.demo.entity.JobSekers;
import com.example.demo.entity.Test;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/jobSeker")
public class JobApplyController {
	@GetMapping("/AddJobApply/{vacancyId}")
	public ModelAndView AddJobApply(@PathVariable("vacancyId") int vacancyId ,Model model,HttpSession session) {
		
		JobSekers jobSekers = (JobSekers) session.getAttribute("JobSekers");
		int job1 = jobSekers.getUserId();
		System.out.println("job1 obj:"+job1);
		JobApply jobApply = new JobApply();
		model.addAttribute("vacancyId", vacancyId);
		model.addAttribute("userId", job1);
		model.addAttribute("JobApply", jobApply);

		ModelAndView view = new ModelAndView("AddJobApply");
		return view;
	}@PostMapping("/AddingJobApply")
	public  String AddJobApply(@ModelAttribute("JobApply") JobApply jobApply, Model model) {
		String status = "OnProgess";
		jobApply.setStatus(status);
		jobApply.setFinalscore("0");
	
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<JobApply> entity = new HttpEntity<JobApply>(jobApply, headers);
		RestTemplate restTemplate = new RestTemplate();
		String url = "http://3.108.252.162:8080/JobNexaProject/api/v1/AddJobApply";
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
		String responseString = response.getBody();
		//System.out.println("responseFlag="+ );
		
		jobApply = new JobApply();
		model.addAttribute("jobApply", jobApply);
		//ModelAndView view = new ModelAndView("Jobseker");
		model.addAttribute("responseString", responseString);
		return "redirect:/jobSeker/Jobseker";
	}
	@GetMapping("/viewApply")
	public ModelAndView viewVecncies(Model model,HttpSession session) {
		JobSekers jobsek = (JobSekers) session.getAttribute("JobSekers");


		
		int userId = jobsek.getUserId();
		RestTemplate restTemplate = new RestTemplate();

		ResponseEntity<JobApply[]> responseEntity = restTemplate
				.getForEntity("http://3.108.252.162:8080/JobNexaProject/api/v1/findByUserIdInJobApply/"+userId, JobApply[].class);
		JobApply[] responseBody = responseEntity.getBody();
		List<JobApply> jobapplyList = Arrays.asList(responseBody);
		System.out.println("jobapplyList="+jobapplyList);

		model.addAttribute("jobapplyList", jobapplyList);
		
		for (JobApply jooo : jobapplyList) {
			System.out.println("uuuuuuuuuuu" + jooo.getJobsekers().getUserId());

			ResponseEntity<Docunments[]> responseEntity1 = restTemplate.getForEntity(
					"http://3.108.252.162:8080/JobNexaProject/api/v1/getDocuments/" + jooo.getJobsekers().getUserId(), Docunments[].class);
			Docunments[] responseBody1 = responseEntity1.getBody();
			List<Docunments> documentsList = Arrays.asList(responseBody1);

			model.addAttribute("documentsList", documentsList);
			System.err.println("documentsList"+documentsList);
			/////////////////////////////////////////////

			//////////////////
			/*	int userid = jooo.getJobsekers().getUserId();
			ResponseEntity<Boolean> responseEntity2 = restTemplate
					.getForEntity("http://localhost:8090/api/v1/checkSlected/" + userid, Boolean.class);
			Boolean selected = responseEntity2.getBody();
			session.setAttribute("selected_" + userid, selected);*/

		}
		System.out.println("jobapplyList" + jobapplyList);
		////////////////////////////////////////////////////////

		//////////////////////////////////////
		
		ModelAndView view = new ModelAndView("viewJobapply");
		return view;
	}
	
	/*@GetMapping("/viewReports")
	public ModelAndView findByCompanyIdInJobApply(Model model,HttpSession session) {
		JobSekers jobsek = (JobSekers) session.getAttribute("JobSekers");


		
		int userId = jobsek.getUserId();
		RestTemplate restTemplate = new RestTemplate();

		ResponseEntity<JobApply[]> responseEntity = restTemplate
				.getForEntity("http://localhost:8090/api/v1/findByCompanyIdInJobApply/"+userId, JobApply[].class);
		JobApply[] responseBody = responseEntity.getBody();
		List<JobApply> jobapplyList = Arrays.asList(responseBody);
		System.out.println("jobapplyList="+jobapplyList);

		model.addAttribute("jobapplyList", jobapplyList);
			
		//////////////////////////////////////
		
		ModelAndView view = new ModelAndView("Reports");
		return view;
	}*/
	
	}


