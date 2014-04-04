import com.compuware.dog.Dog;
import com.compuware.service.ApplicationContext;


public class Main {
	
	public static void main(String[] args) {
		ApplicationContext context1 = new ApplicationContext("com.compuware.dog.australia");
		Dog dog = context1.getService(Dog.class);
		System.out.printf("[%s]: %s%n", dog.getClass().getName(), dog.bark());

		ApplicationContext context2 = new ApplicationContext("com.compuware.dog.america");
		dog = context2.getService(Dog.class);
		System.out.printf("[%s]: %s%n", dog.getClass().getName(), dog.bark());
	
	}

}
