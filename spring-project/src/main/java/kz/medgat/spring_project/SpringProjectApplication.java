package kz.medgat.spring_project;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@SpringBootApplication
public class SpringProjectApplication implements CommandLineRunner {

	private final ApplicationContext context;

	private final ProductRepository productRepository;

	@Autowired
	public SpringProjectApplication(ApplicationContext context, ProductRepository productRepository) {
		this.context = context;
		this.productRepository = productRepository;
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringProjectApplication.class, args);
	}

	@Override
	public void run(String... args) {
		Scanner scanner = new Scanner(System.in);
		Cart cart = context.getBean(Cart.class);

		while (true) {
			System.out.println("1. Show all products");
			System.out.println("2. Add product to cart");
			System.out.println("3. Remove product from cart");
			System.out.println("4. Show cart");
			System.out.println("5. Exit");

			int choice = scanner.nextInt();

			switch (choice) {
				case 1:
					productRepository.findAll().forEach(System.out::println);
					break;
				case 2:
					System.out.println("Enter product ID to add:");
					Long addId = scanner.nextLong();
					cart.addProduct(addId);
					break;
				case 3:
					System.out.println("Enter product ID to remove:");
					Long removeId = scanner.nextLong();
					cart.removeProduct(removeId);
					break;
				case 4:
					cart.getItems().forEach(System.out::println);
					break;
				case 5:
					return;
				default:
					System.out.println("Invalid choice. Please try again.");
			}
		}
	}
}
