package microservices.book.multiplication.service;

import java.util.Random;

import org.springframework.stereotype.Service;

@Service
public class RandomGeneratorServiceImpl implements RandomGenerateService {
	final static int MINIMUM_FACTOR = 11;
	final static int MAXIMUM_FACTOR = 99;
	
	@Override
	public int generateRandomFactor() {
		// TODO Auto-generated method stub
		return new Random().nextInt((MAXIMUM_FACTOR - MINIMUM_FACTOR) + 1) + MINIMUM_FACTOR;
	}

}
