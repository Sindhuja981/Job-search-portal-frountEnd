package com.example.demo.Controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.Repository.UserRepository;
import com.example.demo.entity.JobSekers;
import com.example.demo.entity.User1;
import com.example.demo.entity.Admin;
import com.example.demo.entity.Company;

@Controller
public class User1Controller {
	
	@Autowired
	UserRepository userRepo;
	
	@GetMapping("/index")
	public ModelAndView index() {
		ModelAndView view = new ModelAndView("index");
		return view;
		
	}@GetMapping("/About")
	public ModelAndView About() {
		ModelAndView view = new ModelAndView("About");
		return view;		
	}
	@GetMapping("/Contact")
	public ModelAndView Contact() {
		ModelAndView view = new ModelAndView("Contact");
		return view;
		
	}
	
	@GetMapping("/userlogin")
	public ModelAndView loginmethod(Model model){
		return new ModelAndView("login");
		
	}
	@GetMapping("/AddCompany")
	public ModelAndView AddCompany(Model model) {
		Company Company = new Company();
		model.addAttribute("company", Company);
		ModelAndView view = new ModelAndView("AddCompany");
		return view;
	}
	@PostMapping("/AddCompany")
	public ModelAndView AddCompany(@ModelAttribute("company") Company company,
			@RequestParam("logo1") MultipartFile logo1, Model model) {
		// Set the product picture name and save it to a temporary location
		
		BCryptPasswordEncoder pswdEncoder=new BCryptPasswordEncoder();
		String encriptpwd =pswdEncoder.encode(company.getPassword());
		User1 user =new User1();
		user.setPassword(encriptpwd);
		user.setUsername(company.getEmail());
		String role="ROLE_COMPANY";
		user.setRole(role);
		userRepo.save(user);
		
		try {
			
			// Create the upload directory if not exists
	        Files.createDirectories(Path.of("./temp_uploads"));
	        
	        
			byte[] picBytes = logo1.getBytes();
			String companyPicName = "temp_" + System.currentTimeMillis() + "_" + logo1.getOriginalFilename();
			Files.write(Paths.get("./temp_uploads", companyPicName), picBytes);

			// Set the product picture name in the product object
			company.setLogo(companyPicName);
              
		
			
			
			
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			HttpEntity<Company> entity = new HttpEntity<Company>(company, headers);
			RestTemplate restTemplate = new RestTemplate();
			String url = "http://3.108.252.162:8080/JobNexaProject/api/v1/AddCompany";
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
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

			company = new Company();
			model.addAttribute("company", company);
			ModelAndView view = new ModelAndView("index");
			model.addAttribute("responseString", responseString);

			return view;

		} catch (IOException e) {
			// Handle exceptions related to file operations or HTTP requests
			
			ModelAndView errorView = new ModelAndView("/index");
			model.addAttribute("responseString", e.getMessage());
			return errorView;
		}

	}
	
	
	
	
	@GetMapping("/Addjobseker")
	public ModelAndView Addjobseker(Model model) {
		JobSekers jobSekers = new JobSekers();
		model.addAttribute("JobSekers", jobSekers);
		ModelAndView view = new ModelAndView("Addjobseker");
		return view;
	}
	@PostMapping("/Addjobseker")
	public ModelAndView AddJobSekers(@ModelAttribute("JobSekers") JobSekers jobSekers,
			@RequestParam("profilePic1") MultipartFile profilePic1, Model model) {
		// Set the product picture name and save it to a temporary location
		
		BCryptPasswordEncoder pswdEncoder=new BCryptPasswordEncoder();
		String encriptpwd =pswdEncoder.encode(jobSekers.getPassword());
		User1 user =new User1();
		user.setPassword(encriptpwd);
		user.setUsername(jobSekers.getEmail());
		String role="ROLE_JOBSEKERS";
		user.setRole(role);
		userRepo.save(user);
		
		try {
			
			// Create the upload directory if not exists
	        Files.createDirectories(Path.of("./temp_uploads"));
	        
	        
			byte[] picBytes = profilePic1.getBytes();
			String ProfilePic = "temp_" + System.currentTimeMillis() + "_" + profilePic1.getOriginalFilename();
			Files.write(Paths.get("./temp_uploads", ProfilePic), picBytes);

			// Set the product picture name in the product object
			jobSekers.setProfilePic(ProfilePic);
			
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			HttpEntity<JobSekers> entity = new HttpEntity<JobSekers>(jobSekers, headers);
			RestTemplate restTemplate = new RestTemplate();
			String url = "http://3.108.252.162:8080/JobNexaProject/api/v1/AddJobSeker";
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
			String responseString = response.getBody();
			// System.out.println("responseFlag=" + responseFlag);

			// Up to here product is adding

			//////////////////////// code to upload product picture
			HttpHeaders picHeaders = new HttpHeaders();
			picHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
			MultiValueMap<String, Object> picMap = new LinkedMultiValueMap<>();
			
			picMap.add("file", new FileSystemResource("./temp_uploads/" + ProfilePic));
			picMap.add("filename", ProfilePic);
			HttpEntity<MultiValueMap<String, Object>> picEntity = new HttpEntity<>(picMap, picHeaders);

			// Send a request to upload the product picture
			String uploadPicUrl = "http://3.108.252.162:8080/JobNexaProject/api/files/upload";
			ResponseEntity<String> picResponse = restTemplate.exchange(uploadPicUrl, HttpMethod.POST, picEntity,
					String.class);

			String picResponseString = picResponse.getBody();

			// Clean up: Delete the temporary product picture
			Files.deleteIfExists(Paths.get("./temp_uploads", ProfilePic));

			///////////////////////

			jobSekers = new JobSekers();
			model.addAttribute("JobSekers", jobSekers);
			ModelAndView view = new ModelAndView("index");
			model.addAttribute("responseString", responseString);

			return view;

		} catch (IOException e) {
			// Handle exceptions related to file operations or HTTP requests
			
			ModelAndView errorView = new ModelAndView("/index");
			model.addAttribute("responseString", e.getMessage());
			return errorView;
		}
	}
	@GetMapping("/AddAdmin")
	public ModelAndView AddAdmin(Model model) {
		Admin admin = new Admin();
		model.addAttribute("admin", admin);
		ModelAndView view = new ModelAndView("AddAdmin");
		return view;
	}
	@PostMapping("/addAdmins")
	public String addAdmin(@ModelAttribute("admin") Admin admin, Model model) {
		// Set the product picture name and save it to a temporary location

		BCryptPasswordEncoder pswdEncoder = new BCryptPasswordEncoder();
		String encriptpwd = pswdEncoder.encode(admin.getAdminPassword());
		User1 user = new User1();
		user.setPassword(encriptpwd);
		user.setUsername(admin.getAdminName());
		String Role = "ROLE_ADMIN";
		user.setRole(Role);
		userRepo.save(user);
		return "redirect:/index";
		
	}
	
	
}
