package microservices.book.multiplication.service;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import microservices.book.multiplication.domain.Multiplication;
import microservices.book.multiplication.domain.MultiplicationResultAttempt;
import microservices.book.multiplication.domain.User;
import microservices.book.multiplication.repository.MultiplicationResultAttemptRepository;
import microservices.book.multiplication.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;

public class MultiplicationServiceImplTest {
	
	private MultiplicationServiceImpl multiplicationServiceImpl;
	@Mock
	private RandomGenerateService randomGenerateService;
	@Mock
	private MultiplicationResultAttemptRepository multiplicationResultAttemptRepository;
	@Mock
	private UserRepository userRepository;
	
	@Before
	public void setUp() {
		// With this call to initMocks we tell Mockito to
		// process the annotations
		MockitoAnnotations.initMocks(this);
		multiplicationServiceImpl = new MultiplicationServiceImpl(
				randomGenerateService, multiplicationResultAttemptRepository, userRepository);
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
		MultiplicationResultAttempt attempt = new MultiplicationResultAttempt(user, multiplication, 3000, false);
		MultiplicationResultAttempt verifyAttempt = new MultiplicationResultAttempt(user, multiplication, 3000, true);
		given(userRepository.findByAlias("Aditya")).willReturn(Optional.empty());
		
		//when 
		boolean attemptResult = multiplicationServiceImpl.checkAttempt(attempt);
		
		//assert
		assertThat(attemptResult).isTrue();
		verify(multiplicationResultAttemptRepository).save(verifyAttempt);
	}
	
	@Test
	public void checkWrongAttemptTest() {
		//Given 
		Multiplication multiplication = new Multiplication(50,60);
		User user = new User("Aditya");
		MultiplicationResultAttempt attempt = new MultiplicationResultAttempt(user, multiplication, 3010, false);
		given(userRepository.findByAlias("Aditya")).willReturn(Optional.empty());
		//when 
		boolean attemptResult = multiplicationServiceImpl.checkAttempt(attempt);
		
		//assert
		assertThat(attemptResult).isFalse();
		verify(multiplicationResultAttemptRepository).save(attempt);
	}
	
	public void retriveStatsTest() {
		//Given 
		Multiplication multiplication = new Multiplication(50,60);
		User user = new User("Aditya");
		
		MultiplicationResultAttempt attempt1 = new MultiplicationResultAttempt(user, multiplication, 3010, false);
		MultiplicationResultAttempt attempt2 = new MultiplicationResultAttempt(user, multiplication, 3018, false);
		
		List<MultiplicationResultAttempt> latestAttempts = Lists.newArrayList(attempt1, attempt2);
		
		given(userRepository.findByAlias("Aditya")).
		willReturn(Optional.empty());
		given(multiplicationResultAttemptRepository
				.findTop5ByUserAliasOrderByIdDesc("Aditya"))
		.willReturn(latestAttempts);
		
		// When 
		List<MultiplicationResultAttempt> statsForUser = multiplicationServiceImpl
															.getStatsForUser("Aditya");
		
		// then
		assertThat(statsForUser).isEqualTo(latestAttempts);
	}
	
}
