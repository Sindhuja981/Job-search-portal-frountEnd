package com.example.demo.Controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.entity.Docunments;
import com.example.demo.entity.JobSekers;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/jobSeker")
public class DocunmentController {
	@GetMapping("/AddDocumnent")
	public ModelAndView AddJobApply(Model model,HttpSession session) {
		
		JobSekers jobSekers = (JobSekers) session.getAttribute("JobSekers");
		int job1 = jobSekers.getUserId();
		System.out.println("job1 obj:"+job1);
		Docunments docunment = new Docunments();
		
		
		model.addAttribute("userId", job1);
		model.addAttribute("documents", docunment);

		ModelAndView view = new ModelAndView("AddDocunments");
		return view;
	
	}
	
	@PostMapping("/addingDoc")
	public String postMethodName(@ModelAttribute("documents") Docunments documents,
			@RequestParam("doc1") MultipartFile doc1, Model model,HttpSession session) {
	//	JobSekers job1 = (JobSekers) session.getAttribute("JobSekers");
//		int userId = job1.getUserId();
//		
//		documents.setJobsekers(job1);
		
		try {

			// Create the upload directory if not exists
			Files.createDirectories(Path.of("./temp_uploads"));

			byte[] picBytes = doc1.getBytes();
			String documentFileName = "temp_" + System.currentTimeMillis() + "_" + doc1.getOriginalFilename();
			Files.write(Paths.get("./temp_uploads", documentFileName), picBytes);

			// Set the product picture name in the product object
			documents.setDocumentsFile(documentFileName);

			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

			HttpEntity<Docunments> entity = new HttpEntity<Docunments>(documents, headers);

			RestTemplate restTemplate = new RestTemplate();

			String url = "http://3.108.252.162:8080/JobNexaProject/api/v1/Adddocunments";
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

			String responseString = response.getBody();

			System.out.println("responseString" + responseString);

////////////////////////code to upload product picture
			HttpHeaders picHeaders = new HttpHeaders();
			picHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
			MultiValueMap<String, Object> picMap = new LinkedMultiValueMap<>();
			picMap.add("file", new FileSystemResource("./temp_uploads/" + documentFileName));
			picMap.add("filename", documentFileName);
			HttpEntity<MultiValueMap<String, Object>> picEntity = new HttpEntity<>(picMap, picHeaders);

// Send a request to upload the product picture
			String uploadPicUrl = "http://3.108.252.162:8080/JobNexaProject/api/files/upload";
			ResponseEntity<String> picResponse = restTemplate.exchange(uploadPicUrl, HttpMethod.POST, picEntity,
					String.class);

			String picResponseString = picResponse.getBody();

// Clean up: Delete the temporary product picture
			Files.deleteIfExists(Paths.get("./temp_uploads", documentFileName));

			documents = new Docunments();

			model.addAttribute("Documents", documents);

			model.addAttribute("responseString", responseString);
			model.addAttribute("job", "*ThankYou for Submiting");
			return "redirect:/jobSeker/AddDocumnent";
		} catch (IOException e) {
			// Handle exceptions related to file operations or HTTP requests

			model.addAttribute("responseString", e.getMessage());
			return "redirect:/AddDocunments";
		}
	
	}
	@GetMapping("/viewDoc")
	public ModelAndView Documentsss(Model model, HttpSession session) {

		JobSekers job1 = (JobSekers) session.getAttribute("JobSekers");

		RestTemplate restTemplate = new RestTemplate();

		ResponseEntity<Docunments[]> responseEntity = restTemplate
				.getForEntity("http://3.108.252.162:8080/JobNexaProject/api/v1/getDocuments/" + job1.getUserId(), Docunments[].class);
		Docunments[] responseBody = responseEntity.getBody();
		List<Docunments> documentsList = Arrays.asList(responseBody);

		model.addAttribute("documentsList", documentsList);
		ModelAndView view = new ModelAndView("viewDocuments");
		return view;
	}
	@GetMapping("/editDoc1/{documentsId}")
	public ModelAndView GetDoc(@PathVariable("documentsId")int documentsId,Model model, HttpSession session) {


		RestTemplate restTemplate = new RestTemplate();

		ResponseEntity<Docunments> responseEntity = restTemplate
				.getForEntity("http://3.108.252.162:8080/JobNexaProject/api/v1/getdocunment/" + documentsId, Docunments.class);
		Docunments responseBody = responseEntity.getBody();
System.out.println("ssssssssssss"+responseBody);
		model.addAttribute("documents", responseBody);
		ModelAndView view = new ModelAndView("/editDoc");
		return view;
	}
	
	@PostMapping("/updateDoc")
	public String addProducts(@ModelAttribute("documents") Docunments documents,
			@RequestParam("doc1") MultipartFile doc1, Model model,HttpSession session) {
		// Set the product picture name and save it to a temporary location
		
		
		try {

			// Create the upload directory if not exists
			Files.createDirectories(Path.of("./temp_uploads"));

			byte[] picBytes = doc1.getBytes();
			String docFileName = "temp_" + System.currentTimeMillis() + "_" + doc1.getOriginalFilename();
			Files.write(Paths.get("./temp_uploads", docFileName), picBytes);

			// Set the product picture name in the product object
			documents.setDocumentsFile(docFileName);

			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

			HttpEntity<Docunments> entity = new HttpEntity<Docunments>(documents, headers);

			RestTemplate restTemplate = new RestTemplate();

			String url = "http://3.108.252.162:8080/JobNexaProject/api/v1/updatedocunment/"+documents.getDocumentsId();
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);

			// System.out.println("responseFlag=" + responseFlag);

			// Up to here product is adding

			//////////////////////// code to upload product picture
			HttpHeaders picHeaders = new HttpHeaders();
			picHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
			MultiValueMap<String, Object> picMap = new LinkedMultiValueMap<>();
			picMap.add("file", new FileSystemResource("./temp_uploads/" + docFileName));
			picMap.add("filename", docFileName);
			HttpEntity<MultiValueMap<String, Object>> picEntity = new HttpEntity<>(picMap, picHeaders);

			// Send a request to upload the product picture
			String uploadPicUrl = "http://3.108.252.162:8080/JobNexaProject/api/files/upload";
			ResponseEntity<String> picResponse = restTemplate.exchange(uploadPicUrl, HttpMethod.POST, picEntity,
					String.class);

			String picResponseString = picResponse.getBody();

			// Clean up: Delete the temporary product picture
			Files.deleteIfExists(Paths.get("./temp_uploads", docFileName));

			///////////////////////

			
			

			return "redirect:/jobSeker/viewDoc";

		} catch (IOException e) {
			// Handle exceptions related to file operations or HTTP requests

			
			return null;
		}

	}
	
	
	@GetMapping("/deleteDocunment/{documentsId}")
	public String deletevacancy(@PathVariable("documentsId") int documentsId) {
	
		Map<String, Integer> pathVar = new HashMap<>();
		pathVar.put("documentsId", documentsId);
		
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.delete("http://3.108.252.162:8080/JobNexaProject/api/v1/deleteDocunment/{documentsId}",pathVar);
		//ModelAndView view = new ModelAndView("viewProducts");
		//return view;
		return "redirect:/jobSeker/viewDoc";
		
	}
	
	

}
