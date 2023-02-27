package com.projects.demo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.DialectOverride.Where;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



@Controller
public class HomeController {
	

	@Autowired
	ownerSer ser;
	
	@Autowired
	postSer ser2;
	
	@Autowired
	postRepo repo;
	
	private User ownerGlobal=new User();
	
	private post postGlobal=new post();
	


	@GetMapping("/")
	public String home() {
		return "index";
	}

	@GetMapping("/register")
	public String register() {
		return "register";
	}
	@GetMapping("/CustomerScreen")
	public String CustomerScreen(Model model) {
		List<post> posts=ser2.postList();
		System.out.println("hi");
		
		model.addAttribute("posts", posts);
		String date = new SimpleDateFormat("yyyy-MM-dd").format(ownerGlobal.getDate_Of_Birth());
		model.addAttribute("name", ownerGlobal.getName());
		model.addAttribute("email", ownerGlobal.getEmail());
		model.addAttribute("gender", ownerGlobal.getGender());
		model.addAttribute("contactNo", ownerGlobal.getContactNo());
		model.addAttribute("date_Of_Birth", date);
		model.addAttribute("type", ownerGlobal.getType());
		return "CustomerScreen";
	}

	@GetMapping("/propertyScreen")
	public String propertyScreen(Model model) {
		model.addAttribute("type",postGlobal.getType());
		model.addAttribute("address",postGlobal.getAddress());
		model.addAttribute("price",postGlobal.getPrice());
		model.addAttribute("id", postGlobal.getId());
		return "propertyScreen";
	}

	@GetMapping("/OwnerScreen")
	public String OwnerScreen(Model model) {
		List<post> posts=ser2.postList();
	
		
		model.addAttribute("posts", posts);
		String date = new SimpleDateFormat("yyyy-MM-dd").format(ownerGlobal.getDate_Of_Birth());
		model.addAttribute("name", ownerGlobal.getName());
		model.addAttribute("email", ownerGlobal.getEmail());
		model.addAttribute("gender", ownerGlobal.getGender());
		model.addAttribute("contactNo", ownerGlobal.getContactNo());
		model.addAttribute("date_Of_Birth", date);
		model.addAttribute("type", ownerGlobal.getType());
		
		
		return "OwnerScreen";
	}

	@GetMapping("/addPost")
	public String addPost() {
		return "addPost";
	}

	@PostMapping("/loginCheck")
	public String loginCheck(@RequestParam("username") String username, @RequestParam("password") String password,
			org.springframework.ui.Model model) {
		User user = ser.findUser(username, password);
		ownerGlobal=user;
//		System.out.println(owner.getEmail()+" "+owner.getPassword());
		if (user != null) {
			if(user.getType().equals("Owner"))
			return "redirect:OwnerScreen";
			else return "redirect:CustomerScreen";

		} else
			return "register";
	}

	@PostMapping("/save")
	public String saveRegister(@RequestParam("name") String name, @RequestParam("gender") String gender,
			@RequestParam("email") String email, @RequestParam("contactNo") String contactNo,
			@RequestParam("date_Of_Birth") String date_Of_Birth, @RequestParam("password") String password,
			@RequestParam("type") String type, org.springframework.ui.Model model) throws Exception {
		model.addAttribute("name", name);
		model.addAttribute("email", email);
		model.addAttribute("gender", gender);
		model.addAttribute("contactNo", contactNo);
		model.addAttribute("date_Of_Birth", date_Of_Birth);
		model.addAttribute("type", type);

		Date date = new SimpleDateFormat("yyyy-MM-dd").parse(date_Of_Birth);

		User user = new User(email, name, contactNo, gender, type, date, password);

		User o = ser.saveO(user);
		ownerGlobal=user;
		if(user.getType().equals("Owner"))
			return "redirect:OwnerScreen";
			else return "redirect:CustomerScreen";
	}

	@PostMapping("/savePost")
	public String savePost(@RequestParam("type") String type, @RequestParam("give") String give,
			@RequestParam("rooms") String rooms, @RequestParam("address") String address,
			@RequestParam("price") String price, @RequestParam("description") String description) {
		

		List<post> postL=ser2.postList();
		
		String id=" "+(postL.size()+1);
		
		post posts=new post(id,ownerGlobal.getEmail(),type,give,rooms,address,price,description);
		
		posts=ser2.savePost(posts);
		
		return "redirect:OwnerScreen";
	}
	
	@RequestMapping("/checking/{id}")
	public String propertyScreen(@PathVariable String id) {
		post p=ser2.postById(id);
		
		System.out.println("ps"+p+" "+id);
		
		postGlobal=p;
		
		return "redirect:/propertyScreen";
	}
	
	@RequestMapping("/deletePost/{id}")
	public String deletePost(@PathVariable String id) {
		
		ser2.deleteBypId(id);
//		repo.deleteByValue(id);

	
		return "redirect:/OwnerScreen";
	}

}
