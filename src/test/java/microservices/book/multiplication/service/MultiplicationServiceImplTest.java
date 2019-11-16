package microservices.book.multiplication.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import microservices.book.multiplication.domain.Multiplication;
import microservices.book.multiplication.domain.MultiplicationResultAttempt;
import microservices.book.multiplication.domain.User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

public class MultiplicationServiceImplTest {
	
	private MultiplicationServiceImpl multiplicationServiceImpl;
	@Mock
	private RandomGenerateService randomGenerateService;
	
	@Before
	public void setUp() {
		//with this call to initMocks we tell Mockito 
		//to process the annotation
		MockitoAnnotations.initMocks(this);
		multiplicationServiceImpl = new MultiplicationServiceImpl(randomGenerateService);
	}
	
	@Test
	public void createRandomMultiplicationTest(){
		//given RandomGenerateService will return 50, 30
		given(randomGenerateService.generateRandomFactor()).willReturn(50, 30);
		
		//when
		Multiplication multiplication = multiplicationServiceImpl.createRandomMultiplication();
		
		//then
		assertThat(multiplication.getFactorA()).isEqualTo(50);
		assertThat(multiplication.getFactorB()).isEqualTo(30);
	}
	
	@Test
	public void checkCorrectAttemptTest() {
		//Given
		Multiplication multiplication = new Multiplication(50,60);
		User user = new User("Aditya");
		MultiplicationResultAttempt attempt = new MultiplicationResultAttempt(user, multiplication, 3000);
		
		//when 
		boolean attemptResult = multiplicationServiceImpl.checkAttempt(attempt);
		
		//assert
		assertThat(attemptResult).isTrue();
	}
	
	@Test
	public void checkWrongAttemptTest() {
		//Given 
		Multiplication multiplication = new Multiplication(50,60);
		User user = new User("Aditya");
		MultiplicationResultAttempt attempt = new MultiplicationResultAttempt(user, multiplication, 3010);
		
		//when 
		boolean attemptResult = multiplicationServiceImpl.checkAttempt(attempt);
		
		//assert
		assertThat(attemptResult).isFalse();
	}
}
