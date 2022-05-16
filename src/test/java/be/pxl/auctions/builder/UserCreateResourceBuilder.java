package be.pxl.auctions.builder;

import be.pxl.auctions.rest.resource.UserCreateResource;

public final class UserCreateResourceBuilder {
	private String firstName;
	private String lastName;
	private String email;
	private String dateOfBirth;

	private UserCreateResourceBuilder() {
	}

	public static UserCreateResourceBuilder anUserCreateResource() {
		return new UserCreateResourceBuilder();
	}

	public UserCreateResourceBuilder withFirstName(String firstName) {
		this.firstName = firstName;
		return this;
	}

	public UserCreateResourceBuilder withLastName(String lastName) {
		this.lastName = lastName;
		return this;
	}

	public UserCreateResourceBuilder withEmail(String email) {
		this.email = email;
		return this;
	}

	public UserCreateResourceBuilder withDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
		return this;
	}

	public UserCreateResource build() {
		UserCreateResource userCreateResource = new UserCreateResource();
		userCreateResource.setFirstName(firstName);
		userCreateResource.setLastName(lastName);
		userCreateResource.setEmail(email);
		userCreateResource.setDateOfBirth(dateOfBirth);
		return userCreateResource;
	}
}
