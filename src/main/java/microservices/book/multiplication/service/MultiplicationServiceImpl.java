package microservices.book.multiplication.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import microservices.book.multiplication.domain.Multiplication;
import microservices.book.multiplication.domain.MultiplicationResultAttempt;

@Service
public class MultiplicationServiceImpl implements MultiplicationService {
	
	private RandomGenerateService randomGenerateService;
	
	@Autowired
	public MultiplicationServiceImpl(RandomGenerateService randomGenerateService) {
		this.randomGenerateService = randomGenerateService;
	}
	
	@Override
	public Multiplication createRandomMultiplication() {
		int factorA = randomGenerateService.generateRandomFactor();
		int factorB = randomGenerateService.generateRandomFactor();
		
		return new Multiplication(factorA, factorB);
	}

	@Override
	public boolean checkAttempt(MultiplicationResultAttempt resultAttempt) {
		return resultAttempt.getResultAttempt() == resultAttempt.getMultiplication().getFactorA() * resultAttempt.getMultiplication().getFactorB();
	}

}
