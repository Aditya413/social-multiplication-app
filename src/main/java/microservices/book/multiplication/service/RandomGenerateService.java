package microservices.book.multiplication.service;

public interface RandomGenerateService {
	/**
	 * @return a randomly generated factor. It always a number between 11 to 99
	 */
	int generateRandomFactor();
}
