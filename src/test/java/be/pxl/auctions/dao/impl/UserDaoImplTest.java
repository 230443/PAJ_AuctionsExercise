package be.pxl.auctions.dao.impl;

import be.pxl.auctions.builder.UserBuilder;
import be.pxl.auctions.dao.UserRepository;
import be.pxl.auctions.model.User;
import be.pxl.auctions.rest.resource.UserCreateResource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static be.pxl.auctions.builder.UserBuilder.anUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Repository.class))
public class UserDaoImplTest {

	@Autowired
	private TestEntityManager entityManager;
	@Autowired
	private UserRepository userRepository;

	@Test
	public void userCanBeSavedAndRetrievedById() {
		User user = new User();
		user.setFirstName("Mark");
		user.setLastName("Zuckerberg");
		user.setDateOfBirth(LocalDate.of(1989, 5, 3));
		user.setEmail("mark@facebook.com");
		long newUserId = userRepository.save(user).getId();
		entityManager.flush();
		entityManager.clear();

		Optional<User> retrievedUser = userRepository.findUserById(newUserId);
		assertTrue(retrievedUser.isPresent());

		assertEquals(user.getFirstName(), retrievedUser.get().getFirstName());
		assertEquals(user.getLastName(), retrievedUser.get().getLastName());
		assertEquals(user.getEmail(), retrievedUser.get().getEmail());
		assertEquals(user.getDateOfBirth(), retrievedUser.get().getDateOfBirth());
	}
	@Test
	public void userCanBeSavedAndRetrievedByEmail() {
		User user = anUser()
				.withFirstName("Mark")
				.withLastName("Zuckerberg")
				.withEmail("mark@facebook.com")
				.withDateOfBirth(LocalDate.of(1989, 5, 3))
				.build();

		String email = userRepository.save(user).getEmail();
		entityManager.flush();
		entityManager.clear();

		Optional<User> retrievedUser = userRepository.findUserByEmail(email);
		assertTrue(retrievedUser.isPresent());

		assertEquals(user.getFirstName(), retrievedUser.get().getFirstName());
		assertEquals(user.getLastName(), retrievedUser.get().getLastName());
		assertEquals(user.getEmail(), retrievedUser.get().getEmail());
		assertEquals(user.getDateOfBirth(), retrievedUser.get().getDateOfBirth());

	}

	@Test
	public void returnsNullWhenNoUserFoundWithGivenEmail() {
		Optional<User> retrievedUser = userRepository.findUserByEmail("mark@facebook.com");
		assertFalse(retrievedUser.isPresent());
	}

	@Test
	public void allUsersCanBeRetrieved() {
		// create and save one user

		User user = anUser()
				.withFirstName("Mark")
				.withLastName("Zuckerberg")
				.withEmail("mark@facebook.com")
				.withDateOfBirth(LocalDate.of(1989, 5, 3))
				.build();

		String email = userRepository.save(user).getEmail();
		entityManager.flush();
		entityManager.clear();

		// retrieve all users
		List<User> users = userRepository.findAll();
		assertNotNull(users);

		// make sure there is at least 1 user in the list
		assertTrue(users.size() > 0);

		// make sure the newly created user is in the list (e.g. test if a user with this email address is in the list)
		assertThat(users.stream().map(User::getEmail).collect(Collectors.toList())).contains(email);
	}


}
