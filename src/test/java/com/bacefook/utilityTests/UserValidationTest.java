package com.bacefook.utilityTests;

import static com.bacefook.utility.UserValidation.validate;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import com.bacefook.dto.ChangePasswordDTO;
import com.bacefook.dto.SignUpDTO;
import com.bacefook.exception.InvalidUserCredentialsException;

/**	Testing User Validation is mainly to practice writing tests,
 * 	though it was still useful for finding a few mistakes.
 * @author Veradux
 */
@RunWith(SpringRunner.class)
public class UserValidationTest {
	
	private SignUpDTO signup;
	private ChangePasswordDTO passChange;
			
	@Before
	public void init() {
		LocalDate time = LocalDate.of(1990, 1, 1);
		signup = new SignUpDTO("male", "email@gmail.com", "first", "last", "password", "password", time);
		passChange = new ChangePasswordDTO("oldpassword", "newpassword", "newpassword");
	}
	
	@Test(expected = Test.None.class)
	public void correctInputTest() throws InvalidUserCredentialsException {
		validate(signup);
	}

	@Test(expected = InvalidUserCredentialsException.class)
	public void emptyNameTest() throws InvalidUserCredentialsException {
		signup.setFirstName("");
		validate(signup);
	}
	
	@Test(expected = InvalidUserCredentialsException.class)
	public void nameTooLongTest() throws InvalidUserCredentialsException {
		signup.setFirstName("Thisnameiswaytoolongohmygodbecky");
		validate(signup);
	}
	
	@Test(expected = InvalidUserCredentialsException.class)
	public void notMatchingPasswordsTest() throws InvalidUserCredentialsException {
		signup.setPasswordConfirmation("thisshouldnevermatch");
		validate(signup);
	}
	
	@Test(expected = InvalidUserCredentialsException.class)
	public void emailFormatTest() throws InvalidUserCredentialsException {
		signup.setEmail("thisformatshouldbeinvalid");
		validate(signup);
	}
	
	@Test(expected = InvalidUserCredentialsException.class)
	public void shortNameTest() throws InvalidUserCredentialsException {
		signup.setLastName("po");
		validate(signup);
	}
	
	@Test(expected = InvalidUserCredentialsException.class)
	public void tooYoungTest() throws InvalidUserCredentialsException {
		signup.setBirthday(LocalDate.now());
		validate(signup);
	}
	
	@Test(expected = InvalidUserCredentialsException.class)
	public void tooOldTest() throws InvalidUserCredentialsException {
		signup.setBirthday(LocalDate.of(1890, 1, 1));
		validate(signup);
	}
	
	@Test(expected = InvalidUserCredentialsException.class)
	public void passTooLongTest() throws InvalidUserCredentialsException {
		signup.setPassword("ihavedecidedtobethemostsecurepersonever!");
		validate(signup);
	}
	
	@Test(expected = InvalidUserCredentialsException.class)
	public void invalidPassSymbolsTest() throws InvalidUserCredentialsException {
		signup.setPassword("takethis!^*");
		validate(signup);
	}
	
	@Test(expected = InvalidUserCredentialsException.class)
	public void invalidPassConfirmationSymbolsTest() throws InvalidUserCredentialsException {
		signup.setPasswordConfirmation("%*thisisfun");
		validate(signup);
	}
	
	@Test(expected = Test.None.class)
	public void correctpassChangeTest() throws InvalidUserCredentialsException {
		validate(passChange);
	}
	
	@Test(expected = InvalidUserCredentialsException.class)
	public void differentPassTest() throws InvalidUserCredentialsException {
		passChange.setConfirmPassword("thispassisnotthesame");
		validate(passChange);
	}
	
	@Test(expected = InvalidUserCredentialsException.class)
	public void loginTest() throws InvalidUserCredentialsException {
		passChange.setConfirmPassword("thispassisnotthesame");
		validate(passChange);
	}
}
