package com.marimbus.selenium.Tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.marimbus.selenium.SeleniumBase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Live365SearchTEST extends SeleniumBase {

	private  WebDriver dataDriver;
	private  WebDriver testDriver;
	private  final String MAIN_PAGE_TITLE = "Live365 Internet Radio Network - Listen to Free Music, Online Radio";
	private  final String EXPECTED_GENRE_PAGE_TITLE_TEMPLATE = "Listen to Free %s Music Online - Live365 Internet Radio";
	private  final String baseUrl= "http://www.live365.com/new/index.live";

/*	@Test
	public void testToFail(){	
		doGenreSearch("RANDOM STUFF");
	}*/
	@Test
	public void testGenres(){	
		List<String> extractedGenresList = this.getListOfGenres();		
		for (String genre : extractedGenresList){
			doGenreSearch(genre);
		}
	}
	
	private  List<String> getListOfGenres(){			

		List<String> listOfTopStations = new ArrayList<String>();
		dataDriver = getDriver();
		dataDriver.get(baseUrl);
		dataDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

		//						navigate to genre page				
		dataDriver.findElement(By.xpath(".//*[@class='headerlinks']//li[1]/a")).click();			
		dataDriver.switchTo().frame("contentFrame");
		dataDriver.switchTo().frame(dataDriver.findElement(By.cssSelector(".tabFrame")));		   

		//                  create station titles list
		List<WebElement> genreElementsList = dataDriver.findElements(By.xpath(".//*[@id='tagCloud']/a"));

		//         			 populate  listOfTopStations with genres
		for (WebElement elem : genreElementsList) {
			listOfTopStations.add(elem.getText());				
		}		
		for (String el : listOfTopStations){
			System.out.println("Extracted genre - "+ el.toUpperCase());		    
		}	
		System.out.println("==============================================");
		System.out.println("                                              ");
		return listOfTopStations;		
	}	
	
	
	public void doGenreSearch (String currentGenre) {

		System.out.println("Search and verify genre - "+ currentGenre.toUpperCase());
		testDriver = getDriver();
		testDriver.get(baseUrl);

		//                   assert home page title
		Assert.assertEquals(MAIN_PAGE_TITLE, testDriver.getTitle());
		
		//					navigate to search station field
		WebElement searchField = testDriver.findElement(By.name("query"));
		searchField.clear();
		searchField.sendKeys(currentGenre);		
		testDriver.findElement(By.xpath(".//*[@class='searchForm']/input")).click();

		//							assert search result page title
		String s = String.format(EXPECTED_GENRE_PAGE_TITLE_TEMPLATE, currentGenre).toLowerCase();
		Assert.assertEquals( testDriver.getTitle().toLowerCase(),s);

		//                  assert crumbhead title
		testDriver.switchTo().frame("contentFrame");
		WebElement searchResult = testDriver.findElement(By.id("crumbhead"));
		String expectedResult = String.format("top %s stations", currentGenre).toLowerCase();
		String actualResult = searchResult.getText().toLowerCase();
		Assert.assertEquals(actualResult, expectedResult); 
	}	
}