package com.subham.springfirst.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.subham.springfirst.model.FinancialGoal;

public interface FinancialGoalRepository extends JpaRepository<FinancialGoal, Integer>{
	public List<FinancialGoal> findByUsername(String username);
}
