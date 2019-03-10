package com.bacefook.utilityTests;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.bacefook.utility.TimeConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TimeConverter.class)
public class TimeConverterTest {

	@Test
	public void justNowTest() {
		String expected = "just now";
		String actual = TimeConverter.convertTimeToString(LocalDateTime.of(LocalDate.now(), LocalTime.now()));
		
		Assert.assertEquals("Convertion failed", expected, actual);
	}
	
	@Test
	public void minuteTest() {
		int num = 1;
		String expected = num + " minutes ago";
		String actual = TimeConverter.convertTimeToString(LocalDateTime.of(LocalDate.now(), LocalTime.now().minusMinutes(num)));
		
		Assert.assertEquals("Convertion failed", expected, actual);
	}
	
	@Test
	public void hourTest() {
		int num = 1;
		String expected = num + " hours ago";
		String actual = TimeConverter.convertTimeToString(LocalDateTime.of(LocalDate.now(), LocalTime.now().minusHours(num)));
		
		Assert.assertEquals("Convertion failed", expected, actual);
	}
	
	@Test
	public void dayTest() {
		int num = 1;
		String expected = num + " days ago";
		String actual = TimeConverter.convertTimeToString(LocalDateTime.of(LocalDate.now().minusDays(num), LocalTime.now()));
		
		Assert.assertEquals("Convertion failed", expected, actual);
	}
	
	@Test
	public void monthTest() {
		int num = 1;
		String expected = num + " months ago";
		String actual = TimeConverter.convertTimeToString(LocalDateTime.of(LocalDate.now().minusMonths(num), LocalTime.now()));
		
		Assert.assertEquals("Convertion failed", expected, actual);
	}
	
	@Test
	public void yearTest() {
		int num = 1;
		String expected = num + " years ago";
		String actual = TimeConverter.convertTimeToString(LocalDateTime.of(LocalDate.now().minusYears(num), LocalTime.now()));
		
		Assert.assertEquals("Convertion failed", expected, actual);
	}
}
