package be.pxl.auctions.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static be.pxl.auctions.builder.UserBuilder.anUser;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserGetAgeTest {


	@Test
	public void returnsCorrectAgeWhenHavingBirthdayToday() {
		User user = anUser()
				.withDateOfBirth(LocalDate.now().minusYears(20))
				.build();

		assertEquals(20, user.getAge());
	}

	@Test
	public void returnsCorrectAgeWhenHavingBirthdayTomorrow() {
		User user = anUser()
				.withDateOfBirth(LocalDate.now().minusYears(20).plusDays(1))
				.build();

		assertEquals(19, user.getAge());
	}

	@Test
	public void returnsCorrectAgeWhenBirthdayWasYesterday() {
		User user = anUser()
				.withDateOfBirth(LocalDate.now().minusYears(20).minusDays(1))
				.build();

		assertEquals(20, user.getAge());
	}

}
