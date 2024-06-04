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


import com.example.demo.entity.Company;
import com.example.demo.entity.JobApply;
import com.example.demo.entity.User1;
import com.exmaple.demo.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/company")
public class companyController {
	@Autowired
	UserService userService;
	
	@GetMapping("/CompanyLogin")
	public ModelAndView CompanyLogin(Model model) {
		Company Company = new Company();
		model.addAttribute("company", Company);
		ModelAndView view = new ModelAndView("CompanyLogin");
		return view;
	}
	@GetMapping("/Company")//Company home
	public ModelAndView CompanyHome(Model model,HttpSession session, Authentication authentication, Company Company) {
		String Username = authentication.getName();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

		HttpEntity<Company> entity = new HttpEntity<Company>(Company, headers);

		RestTemplate restTemplate = new RestTemplate();
		
		ResponseEntity<Company> response = 
				restTemplate.getForEntity("http://3.108.252.162:8080/JobNexaProject/api/v1/CompanyLogin/"+Username,
				 Company.class);
		
		Company responseObject = response.getBody();
		
		System.out.println("responseFlag=" + responseObject);
		
		if (responseObject != null) { //login success
			
		
			System.out.println("sindu="+responseObject.getCompanyId());
			session.setAttribute("company", responseObject);
			
			Company company1=(Company)session.getAttribute("company");
			
			int companyid=company1.getCompanyId();
			
			
			model.addAttribute("companyId",companyid);
			
			
			ModelAndView view = new ModelAndView("Company");
			
			return view;
			
		} else {
			model.addAttribute("resp", "*Email/pass doesnot exists");
			
			ModelAndView view = new ModelAndView("companylogin");
			
			return view;
			
		}
	}
	
	/*@PostMapping("/ValidateCompanyLogin")
	public ModelAndView ValidateCompanyLogin(@ModelAttribute("company") Company Company, HttpSession session, Model model) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

		HttpEntity<Company> entity = new HttpEntity<Company>(Company, headers);

		RestTemplate restTemplate = new RestTemplate();
		
		ResponseEntity<Company> response = 
				restTemplate.exchange("http://localhost:8090/api/v1/loginValidate",
				HttpMethod.POST, entity, Company.class);
		
		Company responseObject = response.getBody();
		
		System.out.println("responseFlag=" + responseObject);
		
		if (responseObject != null) { //login success
			
		
			
			session.setAttribute("company", responseObject);
			ModelAndView view = new ModelAndView("companyhome");
			
			return view;
			
		} else {
			model.addAttribute("resp", "*Email/pass doesnot exists");
			
			ModelAndView view = new ModelAndView("companylogin");
			
			return view;
			
		}
	}*/
	

	
	@GetMapping("/getProfile")
	public ModelAndView getProfile(Model model,HttpSession session) {
		Company company = (Company) session.getAttribute("company");

		int cid = company.getCompanyId();
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<Company> responseEntity = 
				restTemplate.getForEntity("http://3.108.252.162:8080/JobNexaProject/api/v1/getCompany/" +cid,Company.class);
		Company company1 = responseEntity.getBody();
		
		System.out.println("company = " +company1.getCompanyName());
		model.addAttribute("company",company1);
		ModelAndView view = new ModelAndView("profile");		
		return view;
		
	}
	///////for profile edit
	@GetMapping("/getProfileToEdit")
	public ModelAndView getProfileToEdit(Model model,HttpSession session, @RequestParam("logo1") MultipartFile logo1) {
		Company company = (Company) session.getAttribute("company");

		int cid = company.getCompanyId();
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<Company> responseEntity = 
				restTemplate.getForEntity("http://3.108.252.162:8080/JobNexaProject/api/v1/getCompany/" +cid,Company.class);
		Company company1 = responseEntity.getBody();
	
		
		System.out.println("company = " +company1.getCompanyName());
		model.addAttribute("company",company1);
		ModelAndView view = new ModelAndView("profile");		
		return view;
		
		
	}
	
	////update the company profile data
	@PostMapping("/updateCompany")
	public ModelAndView AddCompany(@ModelAttribute("company") Company company,
			@RequestParam("logo1") MultipartFile logo1, Model model) {
		// Set the product picture name and save it to a temporary location
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encryptedPassword = passwordEncoder.encode(company.getPassword());

        // Get the existing user by email
        User1 existingUser = userService.getUserByEmail(company.getEmail());

        if (existingUser != null) {
            // Update the existing user entity with new information
            existingUser.setPassword(encryptedPassword);
            existingUser.setRole("ROLE_COMPANY"); // Assuming role is also updated

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
			company.setLogo(companyPicName);
              
		
			
			
			
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			HttpEntity<Company> entity = new HttpEntity<Company>(company, headers);
			RestTemplate restTemplate = new RestTemplate();
			String url = "http://3.108.252.162:8080/JobNexaProject/api/v1/updateCompany/"+company.getCompanyId();
			
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

			company = new Company();
			model.addAttribute("company", company);
			ModelAndView view = new ModelAndView("profile");
			model.addAttribute("responseString", responseString);

			return view;

		} catch (IOException e) {
			// Handle exceptions related to file operations or HTTP requests
			
			ModelAndView errorView = new ModelAndView("/Company");
			model.addAttribute("responseString", e.getMessage());
			return errorView;
		}

	}
	
	
	
	
	
	
	@GetMapping("/viewReports")
	public ModelAndView findByCompanyIdInJobApply(HttpSession session,Model model) {
		Company companys = (Company) session.getAttribute("company");
		int cid = companys.getCompanyId();	
		
		RestTemplate restTemplate = new RestTemplate();	
		ResponseEntity<JobApply[]> responseEntity = restTemplate.getForEntity("http://3.108.252.162:8080/JobNexaProject/api/v1/findByCompanyIdInJobApply/"+cid,
				JobApply[].class);
		JobApply[] responseBody = responseEntity.getBody();
		List<JobApply> jobapplyList = Arrays.asList(responseBody);

		System.out.println("jobapplyList" + jobapplyList);
		JobApply jobapply=new JobApply();
		model.addAttribute("jobapply", jobapply);
		model.addAttribute("jobapplyList", jobapplyList);
		ModelAndView view = new ModelAndView("Reports");
		return view;
		
	}
	@GetMapping("/ViewResults")
	public ModelAndView findByCompanyIdInJobApply1(HttpSession session,Model model) {
		Company companys = (Company) session.getAttribute("company");
		int cid = companys.getCompanyId();	
		
		RestTemplate restTemplate = new RestTemplate();	
		ResponseEntity<JobApply[]> responseEntity = restTemplate.getForEntity("http://3.108.252.162:8080/JobNexaProject/api/v1/findByCompanyIdInJobApply/"+cid,
				JobApply[].class);
		JobApply[] responseBody = responseEntity.getBody();
		List<JobApply> jobapplyList = Arrays.asList(responseBody);

		System.out.println("jobapplyList" + jobapplyList);
		JobApply jobapply=new JobApply();
		model.addAttribute("jobapply", jobapply);
		model.addAttribute("jobapplyList", jobapplyList);
		ModelAndView view = new ModelAndView("ResultsCompany");
		return view;
		
	}


}
