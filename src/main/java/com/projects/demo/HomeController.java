package com.projects.demo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
	
	private User userGlobal=new User();
	
	private post postGlobal=new post();
	
	private List<post>filteredList=null;

	@GetMapping("/")
	public String home() {
		return "index";
	}

	@GetMapping("/register")
	public String register() {
		return "register";
	}

	@RequestMapping("/FilteredCustomerScreen")
	public String FilteredCustomerScreen(Model model) {
		System.out.println("In");
		model.addAttribute("posts",filteredList);
		System.out.println("Out");
		return "FilteredCustomerScreen";
	}
	@GetMapping("/CustomerScreen")
	public String CustomerScreen(Model model) {
		List<post> posts=ser2.postList();
		System.out.println("hi");
		
		model.addAttribute("posts", posts);
		String date = new SimpleDateFormat("yyyy-MM-dd").format(userGlobal.getDate_Of_Birth());
		model.addAttribute("name", userGlobal.getName());
		model.addAttribute("email", userGlobal.getEmail());
		model.addAttribute("gender", userGlobal.getGender());
		model.addAttribute("contactNo", userGlobal.getContactNo());
		model.addAttribute("date_Of_Birth", date);
		model.addAttribute("type", userGlobal.getType());
		return "CustomerScreen";
	}

	@GetMapping("/propertyScreen")
	public String propertyScreen(Model model) {
		model.addAttribute("type",postGlobal.getType());
		model.addAttribute("userType",userGlobal.getType());
		model.addAttribute("address",postGlobal.getAddress());
		System.out.println("Address"+postGlobal.getAddress());
		model.addAttribute("price",postGlobal.getPrice());
		model.addAttribute("id", postGlobal.getId());
		return "propertyScreen";
	}

	@GetMapping("/OwnerScreen")
	public String OwnerScreen(Model model) {
		List<post> posts=ser2.postList();
	
		
		model.addAttribute("posts", posts);
		String date = new SimpleDateFormat("yyyy-MM-dd").format(userGlobal.getDate_Of_Birth());
		model.addAttribute("name", userGlobal.getName());
		model.addAttribute("email", userGlobal.getEmail());
		model.addAttribute("gender", userGlobal.getGender());
		model.addAttribute("contactNo", userGlobal.getContactNo());
		model.addAttribute("date_Of_Birth", date);
		model.addAttribute("type", userGlobal.getType());
		
		
		return "OwnerScreen";
	}

	@GetMapping("/addPost")
	public String addPost() {
		return "addPost";
	}

	@PostMapping("/loginCheck")
	public String loginCheck(@RequestParam("username") String username, @RequestParam("password") String password,
			org.springframework.ui.Model model) {
		if(username.equals("admin@gmail.com")&& password.equals("Admin")) return "redirect:AdminScreen";
		User user = ser.findUser(username, password);
		userGlobal=user;
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
		userGlobal=user;
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
		
		post posts=new post(id,userGlobal.getEmail(),type,give,rooms,address,price,description);
		
		posts=ser2.savePost(posts);
		
		return "redirect:OwnerScreen";
	}
	
	@RequestMapping("/checking/{id}")
	public String propertyScreen(@PathVariable String id) {
		post p=ser2.postById(id);
		
		System.out.println("ps"+p+" "+id);
//		model.addAttribute("type", userGlobal.getType());
		postGlobal=p;
		
		return "redirect:/propertyScreen";
	}
	
	@RequestMapping("/deletePost/{id}")
	public String deletePost(@PathVariable String id) {
		
		ser2.deleteBypId(id);
//		repo.deleteByValue(id);

	
		return "redirect:/OwnerScreen";
	}
	
	@PostMapping("/search")
	public String searchPostByName(@RequestParam String searchName, Model model){
		System.out.println(searchName);
		List<post>list= repo.findAll();
		filteredList= list.stream().filter(e->e.getType().equalsIgnoreCase(searchName)).collect(Collectors.toList());
		System.out.println(filteredList);
		System.out.println(list);
		return "redirect:/FilteredCustomerScreen";
	}

}
