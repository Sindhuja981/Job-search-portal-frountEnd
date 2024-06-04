package com.example.demo.Controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.entity.JobSekers;
import com.example.demo.entity.User1;
import com.example.demo.entity.Vacancies;
import com.exmaple.demo.service.UserService;
import com.example.demo.entity.Company;

import jakarta.servlet.http.HttpSession;



@Controller
@RequestMapping("/jobSeker")
public class JobSekerController {
	
	@Autowired
	UserService userService;
	
	/*@GetMapping("/JobSekerLogin")
	public ModelAndView JobsekerLogin(Model model) {
		JobSekers jobSekers = new JobSekers();
		model.addAttribute("JobSeker", jobSekers);
		ModelAndView view = new ModelAndView("JobSekerLogin");jobSeker/Jobseker
		return view;
	}*/
	@GetMapping("/Jobseker")//Jobseker home
	public ModelAndView JobsekerHome(Model model,HttpSession session, Authentication authentication, JobSekers jobSekers) {
		String Username = authentication.getName();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

		HttpEntity<JobSekers> entity = new HttpEntity<JobSekers>(jobSekers, headers);

		RestTemplate restTemplate = new RestTemplate();
		
		ResponseEntity<JobSekers> response = 
				restTemplate.getForEntity("http://3.108.252.162:8080/JobNexaProject/api/v1/JobSekerLogin/"+Username,
						JobSekers.class);
		
		JobSekers responseObject = response.getBody();
		
		System.out.println("responseFlag=" + responseObject);
		
		if (responseObject != null) { //login success
			
		
			System.out.println("sindu="+responseObject.getUserId());
			session.setAttribute("JobSekers", responseObject);
			
			JobSekers JobSekers1=(JobSekers)session.getAttribute("JobSekers");
			
			int UserId=JobSekers1.getUserId();
			
			
			model.addAttribute("userId",UserId);
			/////////////////////////////////////////////////////////////////////////////
			
			ResponseEntity<Vacancies[]> responseEntity1 = restTemplate
			.getForEntity("http://3.108.252.162:8080/JobNexaProject/api/v1/Allvacancies", Vacancies[].class);
			Vacancies[] responseObj = responseEntity1.getBody();
			List<Vacancies> vacancyList = Arrays.asList(responseObj);
			System.out.println("vacancyList" + vacancyList);
			model.addAttribute("vacancyList", vacancyList);
         ///////////////////////////////////////////////////////
			/////////////////
			
		///////////////////////////////////////////////////////
		for (Vacancies vacancy : vacancyList) {
		int vacancyId = vacancy.getVacancyId();
		ResponseEntity<Boolean> responseEntity = restTemplate.getForEntity(
		"http://3.108.252.162:8080/JobNexaProject/api/v1/check/" + vacancyId + "/" + UserId, Boolean.class);
		Boolean applied = responseEntity.getBody();
		session.setAttribute("applied_" + vacancyId, applied);
		}
			
				
			ModelAndView view = new ModelAndView("Jobseker");
			
			return view;
			
		} else {
			model.addAttribute("resp", "*Email/pass doesnot exists");
			
			ModelAndView view = new ModelAndView("JobSekerLogin");
			
			return view;
			
		}
	}
	
	@PostMapping("/ValidateJobSekerLogin")
	public ModelAndView ValidateJobSekerLogin(@ModelAttribute("JobSekers") JobSekers jobSekers, HttpSession session, Model model) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

		HttpEntity<JobSekers> entity = new HttpEntity<JobSekers>(jobSekers, headers);

		RestTemplate restTemplate = new RestTemplate();
		
		ResponseEntity<JobSekers> response = 
				restTemplate.exchange("http://3.108.252.162:8080/JobNexaProject/api/v1/JobSekerLogin",
				HttpMethod.POST, entity, JobSekers.class);
		
		JobSekers responseObject = response.getBody();
		
		System.out.println("responseFlag=" + responseObject);
		
		if (responseObject != null) { //login success
			session.setAttribute("JobSekers", responseObject);
			ModelAndView view = new ModelAndView("Jobseker");
			
			return view;
			
		} else {
			
			model.addAttribute("resp", "*Email/pass doesnot exists");
			ModelAndView view = new ModelAndView("login");		
			return view;
			
		}
	}
	@GetMapping("/getUserProfile")
	public ModelAndView getProfile(Model model,HttpSession session) {
		JobSekers jobSekers = (JobSekers) session.getAttribute("JobSekers");
		int cid = jobSekers.getUserId();
		
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<JobSekers> responseEntity = 
				restTemplate.getForEntity("http://3.108.252.162:8080/JobNexaProject/api/v1/getJobSekers/" +cid,JobSekers.class);
		JobSekers JobSekers1 = responseEntity.getBody();
		 
		System.out.println("JobSekers = " +JobSekers1.getFirstName());
		model.addAttribute("JobSekers",JobSekers1);
		ModelAndView view = new ModelAndView("JobSekerProfile");		
		return view;	
	}
	
	/// Edit profile code
///////for profile edit
	@GetMapping("/getUserProfileToEdit")
	public ModelAndView getProfileToEdit(Model model,HttpSession session, @RequestParam("profilepic1") MultipartFile logo1) {
		JobSekers JobSekers1=(JobSekers)session.getAttribute("JobSekers");

		int cid = JobSekers1.getUserId();
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<JobSekers> responseEntity = 
				restTemplate.getForEntity("http://3.108.252.162:8080/JobNexaProject/api/v1/getJobSekers/" +cid,JobSekers.class);
		JobSekers company1 = responseEntity.getBody();
	
		
		System.out.println("company = " +company1.getFirstName());
		model.addAttribute("company",company1);
		ModelAndView view = new ModelAndView("JobSekerProfile");		
		return view;
		
		
	}
	
	////update the company profile data
	@PostMapping("/updateJobseker")
	public ModelAndView AddCompany(@ModelAttribute("JobSekers") JobSekers jobSekers,
			@RequestParam("profilePic1") MultipartFile logo1, Model model) {
		// Set the product picture name and save it to a temporary location
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
      String encryptedPassword = passwordEncoder.encode(jobSekers.getPassword());

      // Get the existing user by email
      User1 existingUser = userService.getUserByEmail(jobSekers.getEmail());

      if (existingUser != null) {
          // Update the existing user entity with new information
          existingUser.setPassword(encryptedPassword);
          existingUser.setRole("ROLE_JOBSEKERS"); // Assuming role is also updated

          // Save the updated user entity
          userService.updateProfile(existingUser);
          System.out.println("User updated successfully");
      } else {
          System.out.println("User not found");
      }

		try {
			
			// Create the upload directory if not exists
	        Files.createDirectories(Path.of("./temp_uploads"));
	        
	        
			byte[] picBytes = logo1.getBytes();
			String companyPicName = "temp_" + System.currentTimeMillis() + "_" + logo1.getOriginalFilename();
			Files.write(Paths.get("./temp_uploads", companyPicName), picBytes);

			// Set the product picture name in the product object
			jobSekers.setProfilePic(companyPicName);
            
		
			
			
			
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			HttpEntity<JobSekers> entity = new HttpEntity<JobSekers>(jobSekers, headers);
			RestTemplate restTemplate = new RestTemplate();
			String url = "http://3.108.252.162:8080/JobNexaProject/api/v1/updateJobSekers/"+jobSekers.getUserId();
			
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);
			String responseString = response.getBody();
			// System.out.println("responseFlag=" + responseFlag);

			// Up to here product is adding

			//////////////////////// code to upload product picture
			HttpHeaders picHeaders = new HttpHeaders();
			picHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
			MultiValueMap<String, Object> picMap = new LinkedMultiValueMap<>();
			picMap.add("file", new FileSystemResource("./temp_uploads/" + companyPicName));
			
			
			
			picMap.add("filename", companyPicName);
			HttpEntity<MultiValueMap<String, Object>> picEntity = new HttpEntity<>(picMap, picHeaders);

			// Send a request to upload the product picture
			String uploadPicUrl = "http://3.108.252.162:8080/JobNexaProject/api/files/upload";
			ResponseEntity<String> picResponse = restTemplate.exchange(uploadPicUrl, HttpMethod.POST, picEntity,
					String.class);

			String picResponseString = picResponse.getBody();

			// Clean up: Delete the temporary product picture
			Files.deleteIfExists(Paths.get("./temp_uploads", companyPicName));

			///////////////////////

			jobSekers = new JobSekers();
			model.addAttribute("jobSekers", jobSekers);
			ModelAndView view = new ModelAndView("JobSekerProfile");
			model.addAttribute("responseString", responseString);

			return view;

		} catch (IOException e) {
			// Handle exceptions related to file operations or HTTP requests
			ModelAndView errorView = new ModelAndView("/Jobseker");
			model.addAttribute("responseString", e.getMessage());
			return errorView;
		}

	}
	

}
