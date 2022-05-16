package be.pxl.auctions.service;

import be.pxl.auctions.dao.UserRepository;
import be.pxl.auctions.model.User;
import be.pxl.auctions.rest.resource.UserCreateResource;
import be.pxl.auctions.util.exception.DuplicateEmailException;
import be.pxl.auctions.util.exception.InvalidDateException;
import be.pxl.auctions.util.exception.InvalidEmailException;
import be.pxl.auctions.util.exception.RequiredFieldException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static be.pxl.auctions.builder.UserBuilder.anUser;
import static be.pxl.auctions.builder.UserCreateResourceBuilder.anUserCreateResource;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceCreateUserTest {


	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private UserService userService;

	private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/uuuu");

	private User mapToUser(UserCreateResource userCreateResource) {
		return anUser()
				.withFirstName(userCreateResource.getFirstName())
				.withLastName(userCreateResource.getLastName())
				.withEmail(userCreateResource.getEmail())
				.withDateOfBirth(LocalDate.parse(userCreateResource.getDateOfBirth(), DATE_FORMAT))
				.build();
	}


	@Test
	public void createUser_should_create_user_when_user_does_not_exist() {
		UserCreateResource user = anUserCreateResource()
				.withFirstName("John")
				.withLastName("Doe")
				.withEmail("john.doe@email.com")
				.withDateOfBirth("01/01/2000")
				.build();

		User userToReturn = mapToUser(user);

		when(userRepository.findUserByEmail(user.getEmail())).thenReturn(Optional.empty());
		when(userRepository.save(any(User.class))).thenReturn(userToReturn);
		//assert not throws exception
		var createdUser = userService.createUser(user);
		assertEquals(user.getEmail(), userService.createUser(user).getEmail());
	}

	@Test
	public void createUser_should_throw_exception_when_user_already_exists() {
		UserCreateResource user = anUserCreateResource()
				.withFirstName("John")
				.withLastName("Doe")
				.withEmail("john.doe@email.com")
				.withDateOfBirth("01/01/2000")
				.build();

		when(userRepository.findUserByEmail(user.getEmail())).thenReturn(Optional.of(anUser().build()));

		//assert throws exception
		assertThrows(DuplicateEmailException.class, () -> userService.createUser(user));
	}


	@Test
	public void createUser_should_throw_exception_when_user_firstName_is_null() {
		UserCreateResource user = anUserCreateResource()
				.withLastName("Doe")
				.withEmail("john.doe@email.com")
				.withDateOfBirth("01/01/2000")
				.build();

		assertThrows(RequiredFieldException.class, () -> userService.createUser(user));
	}

	@Test
	public void createUser_should_throw_exception_when_user_lastName_is_null() {
		UserCreateResource user = anUserCreateResource()
				.withFirstName("John")
				.withEmail("john.doe@email.com")
				.withDateOfBirth("01/01/2000")
				.build();

		assertThrows(RequiredFieldException.class, () -> userService.createUser(user));
	}

	@Test
	public void createUser_should_throw_exception_when_user_email_is_null() {
		UserCreateResource user = anUserCreateResource()
				.withFirstName("John")
				.withLastName("Doe")
				.withDateOfBirth("01/01/2000")
				.build();

		assertThrows(RequiredFieldException.class, () -> userService.createUser(user));
	}

	@Test
	public void createUser_should_throw_exception_when_user_dateOfBirth_is_null() {
		UserCreateResource user = anUserCreateResource()
				.withFirstName("John")
				.withLastName("Doe")
				.withEmail("john.doe@email.com")
				.build();

		assertThrows(RequiredFieldException.class, () -> userService.createUser(user));
	}

	@Test
	public void createUser_should_throw_exception_when_user_dateOfBirth_is_invalid() {
		UserCreateResource user = anUserCreateResource()
				.withFirstName("John")
				.withLastName("Doe")
				.withEmail("john.doe@email.com")
				.withDateOfBirth("01/01/2051")
				.build();

		when(userRepository.findUserByEmail(user.getEmail())).thenReturn(Optional.empty());

		assertThrows(InvalidDateException.class, () -> userService.createUser(user));
	}

	@Test
	public void createUser_should_throw_exception_when_user_email_is_invalid() {
		UserCreateResource user = anUserCreateResource()
				.withFirstName("John")
				.withLastName("Doe")
				.withEmail("john.doe")
				.withDateOfBirth("01/01/2000")
				.build();

		assertThrows(InvalidEmailException.class, () -> userService.createUser(user));
	}

	@Test
	public void createUser_should_throw_exception_when_user_email_is_invalid_2() {
		UserCreateResource user = anUserCreateResource()
				.withFirstName("John")
				.withLastName("Doe")
				.withEmail("john.doe@email")
				.withDateOfBirth("01/01/2000")
				.build();

		assertThrows(InvalidEmailException.class, () -> userService.createUser(user));
	}

	// invalid date of birth format
	@Test
	public void createUser_should_throw_exception_when_user_dateOfBirth_is_invalid_format() {
		UserCreateResource user = anUserCreateResource()
				.withFirstName("John")
				.withLastName("Doe")
				.withEmail("john.doe@email.com")
				.withDateOfBirth("01-01-2000")
				.build();

		assertThrows(InvalidDateException.class, () -> userService.createUser(user));
	}
}
