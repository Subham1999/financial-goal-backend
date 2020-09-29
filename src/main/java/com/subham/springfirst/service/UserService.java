package com.subham.springfirst.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.subham.springfirst.model.FinancialGoal;
import com.subham.springfirst.model.User;
import com.subham.springfirst.persistence.FinancialGoalRepository;
import com.subham.springfirst.persistence.UserRepository;

@RestController
public class UserService {
	
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private FinancialGoalRepository goalRepo;
	boolean userdone=false;
	@GetMapping("/users")
	public List<User> getAllUsers() {
		return userRepo.findAll();
	}
	
	@GetMapping("/users/{username}")
	public Optional<User> getUser(@PathVariable String username) {
		return userRepo.findById(username);
	}

	@PostMapping("/users")
	public User addNewUser(@RequestBody Map<String, String> body) {
		User user = new User();
		user.setFirstname(body.getOrDefault("firstname", null));
		user.setLastname(body.getOrDefault("lastname", null));
		user.setUsername(body.getOrDefault("username", null));
		user.setMail(body.getOrDefault("mail", null));
		user.setPassword(body.getOrDefault("password", user.getUsername() + "@123"));
		user.setWallet(Double.parseDouble(body.getOrDefault("wallet", "0.0")));
		Optional<User> alreadyUser = userRepo.findById(user.getUsername());
		if(alreadyUser.isPresent()) return null;
		return userRepo.save(user);
	}
	
	@PostMapping("/users/{username}/addmoney")
	public Optional<User> addMoneyToUserWallet(@PathVariable String username, @RequestBody Map<String, String> body) {
		double money = Double.parseDouble(body.getOrDefault("wallet", "0"));
		Optional<User> optionalUser = userRepo.findById(username);
		optionalUser.ifPresent(user -> {
			user.setWallet(user.getWallet() + money);
			userRepo.save(user);
		});
		return userRepo.findById(username);
	}
	
	@PostMapping("/users/{username}/deductmoney")
	public Optional<User> deductMoneyFromUserWallet(@PathVariable String username, @RequestBody Map<String, String> body) {
		double money = Double.parseDouble(body.getOrDefault("wallet", "0"));
		Optional<User> optionalUser = userRepo.findById(username);
		optionalUser.ifPresent(user -> {
			if(user.getWallet() + money >= 0) {
				user.setWallet(user.getWallet() + money);			
			}
			userRepo.save(user);
		});
		return userRepo.findById(username);
	}
	
	@DeleteMapping("/users/{username}")
	public String deleteUser(@PathVariable String username) {
		try{
			userRepo.deleteById(username);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return "successfull";
	}
	
	@PutMapping("/users/{username}")
	public User updateUser(@RequestBody User user) {
		return userRepo.save(user);
	}
	boolean done = false;
	
	@GetMapping("/users/{username}/goals")
	public List<FinancialGoal> getUserActiveGoals(@PathVariable String username) {
		if(!done) {
			goalRepo.saveAll(getDemoListOfFinancialGoal(username));
		}
		List<FinancialGoal> list = new ArrayList<FinancialGoal>();
		goalRepo.findByUsername(username)
				.stream()
				.filter(goal -> goal.isStatus() == false)
				.forEach(goal -> list.add(goal));
		return goalRepo.findByUsername(username);
	}
	
	@GetMapping("/users/{username}/goals/history")
	public List<FinancialGoal> getUserGoalsHistory(@PathVariable String username) {
		List<FinancialGoal> list = new ArrayList<FinancialGoal>();
		goalRepo.findByUsername(username)
				.stream()
				.filter(goal -> goal.isStatus() == true)
				.forEach(goal -> list.add(goal));
		return list;
	}
	
	@GetMapping("/users/{username}/goals/{id}")
	public Optional<FinancialGoal> getUserGoal(@PathVariable int id) {
		return goalRepo.findById(id);
	}
	
	@PostMapping("/users/{username}/goals")
	public FinancialGoal addUserGoal(@PathVariable String username, @RequestBody Map<String, String> rawGoal) {
		FinancialGoal newGoal = new FinancialGoal();
		newGoal.setName(rawGoal.getOrDefault("name", ""));
		newGoal.setUsername(username);
		newGoal.setAmount(Double.parseDouble(rawGoal.getOrDefault("amount", "0.0")));
		newGoal.setCreated(new Date());
		newGoal.setCompleted(null);
		newGoal.setStatus(false);
		return goalRepo.save(newGoal);
	}
	
	@PutMapping("/users/{username}/goals/{id}")
	public Optional<User> updateUserGoal(@PathVariable String username, @PathVariable int id) {
		System.out.println(username + ", " + id);
		Optional<FinancialGoal> goal = goalRepo.findById(id);
		goal.ifPresent(g -> {
			g.setStatus(true);
			g.setCompleted(new Date());
			goalRepo.save(g);
			Optional<User> u = userRepo.findById(username);
			u.ifPresent(u2 -> {
				u2.setWallet(u2.getWallet() - g.getAmount());
				userRepo.save(u2);
			});
		});
		return userRepo.findById(username);
	}
	
	@DeleteMapping("/users/{username}/goals/{id}")
	public void deleteUserGoal(@PathVariable String username, @PathVariable int id) {
		goalRepo.deleteById(id);
	}

	
	
	/* A Demo List of financial goals*/
	private List<FinancialGoal> getDemoListOfFinancialGoal(String username){
		FinancialGoal g1 = new FinancialGoal();
		g1.setUsername(username);
		g1.setName("Want to buy a bike :)");
		g1.setAmount(10000);
		g1.setCompleted(null);
		g1.setCreated(new Date());
		g1.setStatus(false);
		goalRepo.save(g1);
		
		FinancialGoal g2 = new FinancialGoal();
		g2.setUsername(username);
		g2.setName("Next semester college fees");
		g2.setAmount(65000);
		g2.setCompleted(new Date());
		g2.setCreated(new Date());
		g2.setStatus(true);
		goalRepo.save(g2);
		
		FinancialGoal g3 = new FinancialGoal();
		g3.setUsername(username);
		g3.setName("A phone for my mother");
		g3.setAmount(15000);
		g3.setCompleted(null);
		g3.setCreated(new Date());
		g3.setStatus(false);
		goalRepo.save(g2);
		
		
		done = true;
		
		return Arrays.asList(g1, g2, g3);
	}
}






