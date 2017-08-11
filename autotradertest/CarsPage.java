package autotradertest;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class CarsPage {
    @FindBy(className = "listing-main-image")
    private WebElement car1;
    @FindBy(css = "#main-content > div.js-search-results > div > nav > ul > li:nth-child(3)")
    private WebElement pageRight;
    @FindBy(css = "#main-content > div.js-search-results > div > header > div > label > div > div > select")
    private WebElement filter;
    @FindBy(css = "body > nav > ul > li:nth-child(3)")
    private WebElement breadcrumb;

    public void clickCar1() {
        car1.click();
    }

    public void clickPageRight() {
        pageRight.click();
    }

    public void clickFilter() {
        filter.click();
    }

    public String getBreadcrumbText() {
        return breadcrumb.getText();
    }
}