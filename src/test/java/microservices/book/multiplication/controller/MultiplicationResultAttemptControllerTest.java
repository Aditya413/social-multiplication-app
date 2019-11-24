package microservices.book.multiplication.controller;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import microservices.book.multiplication.domain.Multiplication;
import microservices.book.multiplication.domain.MultiplicationResultAttempt;
import microservices.book.multiplication.domain.User;
import microservices.book.multiplication.service.MultiplicationService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.List;
@RunWith(SpringRunner.class)
@WebMvcTest(MultiplicationResultAttemptController.class)
public class MultiplicationResultAttemptControllerTest {
	@MockBean
	private MultiplicationService multiplicationService;
	
	@Autowired
	private MockMvc mvc;
	
	private JacksonTester<MultiplicationResultAttempt> jsonResultAttempt;
	private JacksonTester<List<MultiplicationResultAttempt>> jsonResultAttemptList;
	private JacksonTester<MultiplicationResultAttempt> jsonResponse;
	
	@Before
	public void setup() {
		JacksonTester.initFields(this, new ObjectMapper());
	}
	
	@Test
	public void postResultReturnCorrect() throws Exception {
		genericParametrizedTest(true);
	}
	
	@Test
	public void postResultReturnNotCorrect() throws Exception {
		genericParametrizedTest(false);
	}
	
	void genericParametrizedTest(final boolean correct) throws Exception {
		//given
		given(multiplicationService.checkAttempt(any(MultiplicationResultAttempt.class)))
						.willReturn(correct);
		
		User user = new User("John");
		Multiplication multiplication = new Multiplication(50, 70); 
		MultiplicationResultAttempt attempt = new MultiplicationResultAttempt(
				user, multiplication, 3500, false);
		
		// when
        MockHttpServletResponse response = mvc.perform(
                post("/results").contentType(MediaType.APPLICATION_JSON)
                        .content(jsonResultAttempt.write(attempt).getJson()))
                .andReturn().getResponse();
        
     // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(
        		jsonResponse.write(new MultiplicationResultAttempt(
        				attempt.getUser(),
        				attempt.getMultiplication(),
        				attempt.getResultAttempt(),
        				correct
        				)).getJson());
	}
	
	public void getUserStats() throws Exception {
		//given
		User user = new User("Aditya");
		Multiplication multiplication = new Multiplication(50, 70);
		MultiplicationResultAttempt attempt = new MultiplicationResultAttempt(
				user, multiplication, 3500, true);
		
		List<MultiplicationResultAttempt> recentAttempts =
				Lists.newArrayList(attempt, attempt);
		
		given(multiplicationService.getStatsForUser("Aditya")).willReturn(recentAttempts);
		
		//When
		MockHttpServletResponse response = mvc.perform(get("/results").param("alias", "Aditya"))
				.andReturn().getResponse();
		
		//then
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		assertThat(response.getContentAsString()).isEqualTo(jsonResultAttemptList.write(recentAttempts).getJson());
	
	}
}
