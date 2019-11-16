package microservices.book.multiplication.service;


import microservices.book.multiplication.domain.Multiplication;
import microservices.book.multiplication.domain.MultiplicationResultAttempt;

public interface MultiplicationService {
	/**
	 * Create a Multiplication object with two randomly-generated
	 * factors
	 * between 11 to 99
	 * 
	 * @return a multiplication object with random factors
	 */
	
	Multiplication createRandomMultiplication();
	
	boolean checkAttempt(final MultiplicationResultAttempt resultAttempt);
}
