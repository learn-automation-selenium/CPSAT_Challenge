package com.cpsat.challenge.pages.nseindia;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import configreader.ObjectRepository;
import logger.LoggerHandler;
import wrappers.GenericHandlers;
import wrappers.WaitHandler;

public class NSEIndexPage {

	private static final Logger log = LoggerHandler.getLogger(NSEIndexPage.class);
	private GenericHandlers handlers;
	private WaitHandler waitHandler;
	WebDriver driver;
	
	@FindBy(how=How.XPATH, using="//ul[@class='advanceTab']/li")
	List<WebElement> marketWatchWindow;
	
	@FindBy(how=How.ID, using="keyword")
	WebElement searchBox;
	
	@FindBy(how=How.LINK_TEXT, using="Live Market")
	WebElement liveMarketTab;
	
	@FindBy(how=How.ID, using="main_liveany_ttg")
	WebElement top10GainersOrLosers;
	
	@FindBy(how=How.ID, using="ajax_response")
	WebElement companyResponse;
	
	public NSEIndexPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(this.driver, this);
		handlers = new GenericHandlers(this.driver);
		waitHandler = new WaitHandler(this.driver);
	}
	
	public Map<String, Integer> getMarketWatchDetails() {
		Map<String, Integer> marketWatchMap = new HashMap<>();
		waitHandler.waitForElementToBeVisible(marketWatchWindow.get(0), ObjectRepository.reader.getExplicitWait());
		int windowSize = marketWatchWindow.size();
		for(int i=0; i<windowSize; i++) {
			WebElement item = marketWatchWindow.get(i);
			String watchItem = handlers.getElementText(item.findElement(By.xpath("//ul[@class='advanceTab']/li["+(i+1)+"]/p")));
			Integer watchValue = Integer.valueOf(handlers.getElementText(item.findElement(By.xpath("//ul[@class='advanceTab']/li["+(i+1)+"]/span"))));
			marketWatchMap.put(watchItem, watchValue);
		}
		return marketWatchMap;
	}
	
	public CompanyDetailsPage searchCompanyDetails(String searchCompany) {
		log.info("Entering " +searchCompany+ " in the search box");
		waitHandler.waitForElementToBeClickable(searchBox, ObjectRepository.reader.getExplicitWait());
		handlers.enterData(searchBox, searchCompany);
		waitHandler.waitForElementToBeClickable(companyResponse, ObjectRepository.reader.getExplicitWait());
		handlers.performKeyOperation(searchBox, Keys.ENTER);
		return new CompanyDetailsPage(driver);
	}
	
	public NSEIndexPage hoverOverLiveMarket() {
		log.info("Hovering over live market tab");
		handlers.mouseOver(liveMarketTab);
		return this;
	}
	
	public TopGainersLosersPage clickTop10GainersOrLosers() {
		log.info("Selecting top 10 Gainers / Losers");
		waitHandler.waitForElementToBeVisible(top10GainersOrLosers, ObjectRepository.reader.getExplicitWait());
		handlers.clickElement(top10GainersOrLosers);
		return new TopGainersLosersPage(driver);
	}	
}