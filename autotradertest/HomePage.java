package autotradertest;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class HomePage {
    @FindBy(css = "#js-header-nav > ul > li:nth-child(1) > a > span")
    private WebElement newCarButton;
    @FindBy(css = "#search > span")
    private WebElement searchButton;
    @FindBy(css = "#postcode")
    private WebElement postcodeField;
    @FindBy(css = "#postcode-lightbox-form > span > span > input[type=\"text\"]")
    private WebElement postcodePopUpField;
    @FindBy(css = "#postcode-lightbox-continue")
    private WebElement postcodePopUpButton;

    public void clickSearchButton() {
        searchButton.click();
    }

    public void enterPostcode(String postcode) {
        postcodeField.sendKeys(postcode);
    }

    public void enterPostcodePopUp(String postcode) {
        postcodePopUpField.sendKeys(postcode);
    }

    public void clickPostcodePopUpButton() {
        postcodePopUpButton.click();
    }
}