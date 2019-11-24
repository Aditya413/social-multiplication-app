package microservices.book.multiplication.service;


import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import microservices.book.multiplication.domain.Multiplication;
import microservices.book.multiplication.domain.MultiplicationResultAttempt;
import microservices.book.multiplication.domain.User;
import microservices.book.multiplication.repository.MultiplicationResultAttemptRepository;
import microservices.book.multiplication.repository.UserRepository;

@Service
public class MultiplicationServiceImpl implements MultiplicationService {
	
	private RandomGenerateService randomGenerateService;
	private MultiplicationResultAttemptRepository multiplicationResultAttemptRepository;
	private UserRepository userRepository;
	
	@Autowired
	public MultiplicationServiceImpl(final RandomGenerateService randomGenerateService,
			final MultiplicationResultAttemptRepository multiplicationResultAttemptRepository,
			final UserRepository userRepository) {
		this.randomGenerateService = randomGenerateService;
		this.multiplicationResultAttemptRepository = multiplicationResultAttemptRepository;
		this.userRepository = userRepository;
	}
	
	@Override
	public Multiplication createRandomMultiplication() {
		int factorA = randomGenerateService.generateRandomFactor();
		int factorB = randomGenerateService.generateRandomFactor();
		
		return new Multiplication(factorA, factorB);
	}
	
	@Transactional
	@Override
	public boolean checkAttempt(MultiplicationResultAttempt resultAttempt) {
		// Check if the user already exists for this alias
		Optional<User> user = userRepository.findByAlias(resultAttempt.getUser().getAlias());
		
		// Avoid hack attempts
				Assert.isTrue(!resultAttempt.isCorrect(), "You can't send an attempt mark as correct");
		boolean correct = resultAttempt.getResultAttempt() == 
						  resultAttempt.getMultiplication().getFactorA() 
						  * resultAttempt.getMultiplication().getFactorB();
		
		// create copy, now setting the 'correct' field
		MultiplicationResultAttempt checkedAttempt = 
				new MultiplicationResultAttempt(
						user.orElse(resultAttempt.getUser()),
						resultAttempt.getMultiplication(), 
						resultAttempt.getResultAttempt(),
						correct);
		
		// Stores the attempt
		multiplicationResultAttemptRepository.save(checkedAttempt);
		// return the result
		return correct;
	}

	@Override
	public List<MultiplicationResultAttempt> getStatsForUser(String userAlias) {
		return multiplicationResultAttemptRepository.findTop5ByUserAliasOrderByIdDesc(userAlias);
	}
	
}
