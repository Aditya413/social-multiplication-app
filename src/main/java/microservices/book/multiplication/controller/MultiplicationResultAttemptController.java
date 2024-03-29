package microservices.book.multiplication.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import microservices.book.multiplication.domain.MultiplicationResultAttempt;
import microservices.book.multiplication.service.MultiplicationService;

@RestController
@RequestMapping("/results")
final class MultiplicationResultAttemptController {
	private final MultiplicationService multiplicationService;
	
	@Autowired
	MultiplicationResultAttemptController(final MultiplicationService multiplicationService) {
		this.multiplicationService = multiplicationService;
	}
	
	@PostMapping
	ResponseEntity<MultiplicationResultAttempt> postResult(@RequestBody MultiplicationResultAttempt multiplicationResultAttempt) {
		boolean isCorrect = multiplicationService
				   .checkAttempt(multiplicationResultAttempt);
		MultiplicationResultAttempt attemptCopy = 
						new MultiplicationResultAttempt(
								multiplicationResultAttempt.getUser(),
								multiplicationResultAttempt.getMultiplication(),
								multiplicationResultAttempt.getResultAttempt(),
								isCorrect
								);
		return ResponseEntity.ok(attemptCopy);
	}
	
	@GetMapping
	ResponseEntity<List<MultiplicationResultAttempt>> getStatics(@RequestParam("alias") String alias) {
		return ResponseEntity.ok(multiplicationService.getStatsForUser(alias));
	}
}
