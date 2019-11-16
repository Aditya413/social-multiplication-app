package microservices.book.multiplication.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public final class Multiplication {
	//both factors
	private final int factorA;
	private final int factorB;
	
	// Empty constructor for JSON (de)serialization
	Multiplication() {
		this(0, 0);
	}
}
