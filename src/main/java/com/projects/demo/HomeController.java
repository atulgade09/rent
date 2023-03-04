package com.projects.demo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.DialectOverride.Where;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class HomeController {

	@Autowired
	ownerSer ser;

	@Autowired
	postSer ser2;

	@Autowired

	postRepo repo;

	@Autowired
	FilesStorageService storageService;
	
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
		String date = new SimpleDateFormat("yyyy-MM-dd").format(userGlobal.getDate_Of_Birth());
		model.addAttribute("name", userGlobal.getName());
		model.addAttribute("email", userGlobal.getEmail());
		model.addAttribute("gender", userGlobal.getGender());
		model.addAttribute("contactNo", userGlobal.getContactNo());
		model.addAttribute("date_Of_Birth", date);
		model.addAttribute("type", userGlobal.getType());
		System.out.println("In");
		model.addAttribute("posts",filteredList);
		System.out.println("Out");
		return "FilteredCustomerScreen";
	}
	

	@GetMapping("/AdminFilteredList")
	public String AdminFilteredList(Model model) {
		System.out.println("In");
		model.addAttribute("posts",filteredList);
		System.out.println("Out");
		User admin=new User();
		admin.setEmail("admin@gmail.com");
		admin.setName("Admin");
		admin.setDate_Of_Birth(new Date("1997/08/18"));
		admin.setContactNo("9767701722");
		admin.setGender("Male");
		model.addAttribute("admin", admin);
		return "AdminFilteredList";
	}
	
	@GetMapping("/CustomerScreen")
	public String CustomerScreen(Model model) {
		List<post> posts = ser2.postList().stream().filter(e->e.getStatus()!=null&&!e.getStatus().equals("SOLD")).collect(Collectors.toList());

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
		model.addAttribute("type", postGlobal.getType());
		model.addAttribute("address", postGlobal.getAddress());
		model.addAttribute("price", postGlobal.getPrice());
		model.addAttribute("type",postGlobal.getType());
		model.addAttribute("userType",userGlobal.getType());
		model.addAttribute("address",postGlobal.getAddress());
		System.out.println("Address"+postGlobal.getAddress());
		model.addAttribute("price",postGlobal.getPrice());
		model.addAttribute("id", postGlobal.getId());

		System.out.println("s0");
		List<String> img = new ArrayList<>();
		if(postGlobal.getImage()!=null) {
			for (int i = 0; i < postGlobal.getImage().size(); i++) {
				img.add((String)postGlobal.getImage().get(i));
				System.out.println(postGlobal.getImage().get(i));
			}

		}
	
		model.addAttribute("img1", "");
		model.addAttribute("img2", "");
		model.addAttribute("img3", "");
		model.addAttribute("img4", "");
		if (img.size() > 0) {
			model.addAttribute("img1", img.get(0));
			if (img.size() > 1) {
				model.addAttribute("img2", img.get(1));
				if (img.size() > 2) {
					model.addAttribute("img3", img.get(2));
					if (img.size() > 3) {
						model.addAttribute("img4", img.get(3));
					}
				}
			}
		}
		return "propertyScreen";
	}


	@GetMapping("/addPost")
	public String addPost() {
		return "addPost";
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
		userGlobal = user;
		if (user.getType().equals("Owner"))
		userGlobal=user;
		if(user.getType().equals("Owner"))
			return "redirect:OwnerScreen";
		else
			return "redirect:CustomerScreen";
	}

	@PostMapping("/savePost")
	public String savePost(@RequestParam("type") String type, @RequestParam("give") String give,
			@RequestParam("file") MultipartFile[] file, @RequestParam("rooms") String rooms,
			@RequestParam("address") String address, @RequestParam("price") String price,
			@RequestParam("description") String description) throws IOException {

		List<post> postL = ser2.postList();

		String id = " " + (postL.size() + 1);

		String message = "";
		List<String> fileNames = new ArrayList<>();
		
		Arrays.asList(file).stream().forEach(files -> {
			int randomNumber = (int) (Math.random() * 9999);
			storageService.save(files, randomNumber);
			fileNames.add(randomNumber + "-" + files.getOriginalFilename());
		});
//List <?>fileList=new ArrayList<>(Arrays.asList(file));

		message = "Uploaded the files successfully: " + fileNames;

//		String fileName = new String(file.getOriginalFilename());
//		System.out.println(fileName);
//		if (fileName.contains("..")) {
//			System.out.println("not valid");
//		}
//
		post posts = new post(id, userGlobal.getEmail(), type, give, rooms, address, price, description);
//
		posts.setImage(fileNames);

//		posts = ser2.savePost(posts);

//		List<post> postL=ser2.postList();
//		
//		String id=" "+(postL.size()+1);
		
//		posts.setImage(fileList);
		double commission=Double.parseDouble(price);
		posts.setCommission(commission*0.1);
		posts.setStatus("Available");
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

	
	@PostMapping("/search")
	public String searchPostByName(@RequestParam String searchName, Model model){
		System.out.println(searchName);
		List<post>list= repo.findAll();
		filteredList= list.stream().filter(e->e.getType().equalsIgnoreCase(searchName) && e.getStatus()!=null && !e.getStatus().equals("SOLD")).collect(Collectors.toList());
		System.out.println(filteredList);
		System.out.println(list);
		return "redirect:/FilteredCustomerScreen";
	}
	
	@PostMapping("/adminSearch")
	public String adminSearch(@RequestParam String searchName, Model model){
		System.out.println(searchName);
		List<post>list= repo.findAll();
		filteredList= list.stream().filter(e->e.getType().equalsIgnoreCase(searchName)).collect(Collectors.toList());
		System.out.println("Admin Filtered List :"+ filteredList);
		System.out.println(list);
		return "redirect:/AdminFilteredList";
	}
	
	@SuppressWarnings("deprecation")
	@RequestMapping("/AdminScreen")
	public String admin(Model model) {
		User admin=new User();
		admin.setEmail("admin@gmail.com");
		admin.setName("Admin");
		admin.setDate_Of_Birth(new Date("1997/08/18"));
		admin.setContactNo("9767701722");
		admin.setGender("Male");
		model.addAttribute("admin", admin);
		List<post>list=repo.findAll();
		model.addAttribute("posts", list);
		return "AdminScreen";

	}

	
	
	@GetMapping("/EditPost")
	public String EditPost(Model model) {
		model.addAttribute("type", postGlobal.getType());
		model.addAttribute("address", postGlobal.getAddress());
		model.addAttribute("price", postGlobal.getPrice());
		model.addAttribute("description", postGlobal.getDescription());
		model.addAttribute("userType", userGlobal.getType());
		model.addAttribute("give", postGlobal.getGive());
		model.addAttribute("room", postGlobal.getRooms());
		model.addAttribute("id", postGlobal.getId());
		return "EditPost";
	}

	

	


	@GetMapping("/OwnerScreen")
	public String OwnerScreen(Model model) {
		List<post> posts = new ArrayList<>();

		for (int i = 0; i < ser2.postList().size(); i++) {
			if (ser2.postList().get(i).getOwnerUsername().equals(userGlobal.getEmail())) {
				posts.add(ser2.postList().get(i));
			}
		}

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


	@PostMapping("/loginCheck")
	public String loginCheck(@RequestParam("username") String username, @RequestParam("password") String password,
			org.springframework.ui.Model model) {
		if (username.equals("admin@gmail.com") && password.equals("Admin"))
			return "redirect:AdminScreen";
		User user = ser.findUser(username, password);
		userGlobal = user;
		userGlobal = user;
//		System.out.println(owner.getEmail()+" "+owner.getPassword());
		if (user != null) {
			if (user.getType().equals("Owner"))
				return "redirect:OwnerScreen";
			else
				return "redirect:CustomerScreen";

		} else
			return "register";
	}

	@RequestMapping("/deletePost/{id}")
	public String deletePost(@PathVariable String id) {

		ser2.deleteBypId(id);

		return "redirect:/OwnerScreen";
	}


	@RequestMapping("/editPost/{id}")
	public String editPost(@PathVariable String id) {
		post p = ser2.postById(id);

		System.out.println("ps" + p + " " + id);
		postGlobal = p;
		return "redirect:/EditPost";
	}
	
	@PostMapping("/saveEditPost")
	public String saveEditPost(@RequestParam("id") String id,@RequestParam("type") String type, @RequestParam("give") String give,
			@RequestParam("file") MultipartFile[] file, @RequestParam("rooms") String rooms,
			@RequestParam("address") String address, @RequestParam("price") String price,
			@RequestParam("description") String description) throws IOException {

		List<post> postL = ser2.postList();

		String message = "";
		List<String> fileNames = new ArrayList<>();

		Arrays.asList(file).stream().forEach(files -> {
			int randomNumber = (int) (Math.random() * 9999);
			storageService.save(files, randomNumber);
			fileNames.add(randomNumber + "-" + files.getOriginalFilename());
		});
//List <?>fileList=new ArrayList<>(Arrays.asList(file));

		message = "Uploaded the files successfully: " + fileNames;

//		String fileName = new String(file.getOriginalFilename());
//		System.out.println(fileName);
//		if (fileName.contains("..")) {
//			System.out.println("not valid");
//		}
//
		post posts = new post(id, userGlobal.getEmail(), type, give, rooms, address, price, description);
//
		posts.setImage(fileNames);

		posts = ser2.savePost(posts);

//		posts = ser2.savePost(posts);

		return "redirect:OwnerScreen";
//>>>>>>> branch 'master' of https://github.com/atulgade09/rent.git
	}
@RequestMapping("/bookProperty/{id}")
public String bookProperty(@PathVariable String id) {
	post bookPost=ser2.postById(id);
	System.out.println("---------------------------------------");
	System.out.println(bookPost);
	System.out.println("in bookProperty"+id);
	System.out.println("---------------------------------------");
	bookPost.setStatus("SOLD");
	ser2.savePost(bookPost);
	return "redirect:/CustomerScreen";
}
	
}
