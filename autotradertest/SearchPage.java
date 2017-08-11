package autotradertest;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class SearchPage {
    @FindBy(css = "#js-known-search-advanced > div.known-search-container__quick-search-section > div.quick-search-section__dual-container.quick-search-section__location-container > input")
    private WebElement postcodeField;
    @FindBy(css = "#js-known-search-advanced > div.known-search-container__left-search-section > input")
    private WebElement keywordsField;
    @FindBy(css = "#js-known-search-advanced > div.known-search-container__quick-search-section > button")
    private WebElement searchButton;

    public void enterPostcode(String postcode) {
        postcodeField.sendKeys(postcode);
    }

    public void enterKeywords(String keywords) {
        keywordsField.sendKeys(keywords);
    }

    public void clickSearchButton() {
        searchButton.click();
    }
}