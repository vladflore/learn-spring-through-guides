package tech.vladflore.spring.restwithspring;

public class OrderNotFoundException extends RuntimeException {
	public OrderNotFoundException(Long id) {
		super(Long.toString(id));
	}
}
