package com.lazerycode.selenium.Tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.lazerycode.selenium.SeleniumBase;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Live365SearchTEST extends SeleniumBase {

	private  WebDriver dataDriver;
	private  WebDriver testDriver;
	private  final String MAIN_PAGE_TITLE = "Live365 Internet Radio Network - Listen to Free Music, Online Radio";
	private  final String EXPECTED_PAGE_TITLE_TEMPLATE = "Listen to Free %s Music Online - Live365 Internet Radio";
	private  final String baseUrl= "http://www.live365.com/new/index.live";
	private  List<String> listOfTopStations = new ArrayList<String>();

	@Test
	public void testGenres(){	
		List<String> extractedGenres = this.getListOfTopStations();
		for (String genre : extractedGenres){
			doGenreSearch(genre);
		}
	}
	private  List<String> getListOfTopStations(){			

		dataDriver = getDriver();
		dataDriver.get(baseUrl);
		dataDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		//						navigate to genre page				
		dataDriver.findElement(By.xpath(".//*[@class='headerlinks']//li[1]/a")).click();			
		dataDriver.switchTo().frame("contentFrame");
		dataDriver.switchTo().frame(dataDriver.findElement(By.cssSelector(".tabFrame")));		   
		//                  create station titles list
		List<WebElement> genreElementsList = dataDriver.findElements(By.xpath(".//*[@id='tagCloud']/a"));
		//         			 populate  listOfTopStations with
		for (WebElement elem : genreElementsList) {
			listOfTopStations.add(elem.getText());				
		}		
		for (String el : listOfTopStations){
			System.out.println("Extracted title of radio station - "+ el.toUpperCase());		    
		}	
		System.out.println("==============================================");
		return listOfTopStations;		
	}	

	public void doGenreSearch (String genre) {
		System.out.println("Search and verify top radio station - "+ genre.toUpperCase());
		testDriver = getDriver();
		testDriver.get(baseUrl);

		//                   assert page title
		Assert.assertEquals(MAIN_PAGE_TITLE, testDriver.getTitle());

		//					navigate to search station field
		WebElement searchField = testDriver.findElement(By.name("query"));
		searchField.clear();
		searchField.sendKeys(genre);		
		testDriver.findElement(By.xpath(".//*[@class='searchForm']/input")).click();

		//							assert search result page title
		String s = String.format(EXPECTED_PAGE_TITLE_TEMPLATE, genre).toLowerCase();
		Assert.assertEquals( testDriver.getTitle().toLowerCase(),s);

		//                  assert crumbhead title
		testDriver.switchTo().frame("contentFrame");
		WebElement searchResult = testDriver.findElement(By.id("crumbhead"));
		String expectedResult = String.format("top %s stations", genre).toLowerCase();
		String actualResult = searchResult.getText().toLowerCase();
		Assert.assertEquals(actualResult, expectedResult); 
	}	
}