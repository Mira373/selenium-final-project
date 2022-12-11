import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class SwoopProject {
    WebDriver driver;
    @BeforeMethod
    public void openBrLink(){
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.get("https://www.swoop.ge/");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5000));
    }

    @Test
    public void clickOnMovieButton(){
        try{
            WebElement movieButton = driver.findElement(By.xpath("//li[contains(@class,'cat-0 cat')]"));
            movieButton.click();
        }catch (TimeoutException e){
            System.out.println("TimeoutException Handling");
        }
    }
    @Test
    public void selectFirstMovieAndClickBuy(){
        clickOnMovieButton();

        //Select the first movie in the returned list and click on ‘ყიდვა’ button
        WebElement movieCard = driver.findElement(By.xpath(" //div[@class='container cinema_container']/div[@class='movies-deal'][1]"));
        JavascriptExecutor js = (JavascriptExecutor)driver;
        js.executeScript("arguments[0].scrollIntoView();",movieCard);
        Actions action = new Actions(driver);
        action.moveToElement(movieCard).perform();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(50000));
        WebElement buyButton = driver.findElement(By.xpath("(//p[contains(text(),'ყიდვა')])[1]"));
        buyButton.click();

        //AD  has been closed here
        WebElement ad = driver.findElement(By.xpath("//*[@class=\"bottom-header-content\"]/div[@class= 'banner-close-footer banner-close-all']/img"));
        js.executeScript("arguments[0].click();",ad);

    }

    @Test
    public void chooseCinemaKavea(){
        selectFirstMovieAndClickBuy();

        // Scroll vertically (if necessary), and horizontally and choose ‘კავეა ისთ ფოინთი’
        JavascriptExecutor js = (JavascriptExecutor)driver;
        List<WebElement> cinemaOptionsList = driver.findElements(By.xpath("//ul[@class='cinema-tabs ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all']/li/a")); //(dinamic) all options
        for(int k=1;k<cinemaOptionsList.size();k++){
            String randomCinema = cinemaOptionsList.get(k).getText();
            if(Objects.equals(randomCinema, "კავეა ისთ ფოინთი")){
                WebElement kaveaEastPoint = driver.findElement(By.xpath("//a[text()='კავეა ისთ ფოინთი']"));
                js.executeScript("arguments[0].scrollIntoView();", kaveaEastPoint);
                js.executeScript("arguments[0].click();", kaveaEastPoint);
                break;
            }else{
                System.out.println(randomCinema + " that is random cinema option");
            }
        }
    }

    public void monthNumberCalculator(String monthName, int monthNumber){

        switch (monthNumber) {
            case 1:
                monthName = "იანვარი";
                System.out.println("იანვარი");
                break;
            case 2:
                monthName = "თებერვალი";
                System.out.println("თებერვალი");
                break;
            case 3:
                System.out.println("მარტი");
                break;
            case 4:
                System.out.println("აპრილი");
                break;
            case 5:
                System.out.println("მაისი");
                break;
            case 6:
                System.out.println("ივნისი");
                break;
            case 7:
                System.out.println("ივლისი");
                break;
            case 8:
                System.out.println("აგვისტო");
                break;
            case 9:
                System.out.println("სექტემბერი");
                break;
            case 10:
                System.out.println("ოქტომბერი");
                break;
            case 11:
                System.out.println("ნოემბერი");
                break;
            case 12:
                System.out.println("დეკემბერი");
                break;
            default:
                System.out.println("Invalid month.");
                break;
        }

    }

    @Test
    public void makeValidations(){
        chooseCinemaKavea();

        // - Check that only ‘კავეა ისთ ფოინთი’ options are returned
        WebElement defaultDate = driver.findElement(By.xpath("//*[@id=\"all\"]/div/ul/li[1]"));
        String defaultDateStringDate = defaultDate.getAttribute("aria-controls");    //returns default date , it is linked to listed options, based on that have been selected div tags

        List<WebElement> visibleOptionsCinemaList = driver.findElements(By.xpath("//div[@id='"+defaultDateStringDate+"' and @aria-hidden='false' and @aria-expanded='true']/a/p[@class='cinema-title' and text()='კავეა ისთ ფოინთი']"));
        for(int j=1; j<visibleOptionsCinemaList.size(); j++){
            String cinemakaveaText = visibleOptionsCinemaList.get(j).getText();
            System.out.println(cinemakaveaText);
            //verified that returned sessions are only kavea related dates
        }
        //click on last option from the returned session for that default date
        visibleOptionsCinemaList.get(visibleOptionsCinemaList.size() - 1).click();

        // validations prepared for ModelView(pop up window)
        WebElement movieNameOnPage = driver.findElement(By.xpath(" //div[@class=\"movie-detailed\"]/div[1]/div[@class='info']/p"));
        String movieNameOnPageText = movieNameOnPage.getText();
        String cinemaNameForValidation = visibleOptionsCinemaList.get(visibleOptionsCinemaList.size() - 1).getText();
        List<WebElement> elementsToValidateTime = driver.findElements(By.xpath("//div[@id='"+defaultDateStringDate+"' and @aria-hidden='false' and @aria-expanded='true']/a/p[1]"));
        String timeValidation = elementsToValidateTime.get(elementsToValidateTime.size()-1).getText();
        System.out.println(defaultDateStringDate +" date For Validation");
        String[] date = defaultDateStringDate.split("[-.] ?");
        int monthNumber=Integer.parseInt(date[3]);

       // driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5000));
        try{
            Thread.sleep(5000);
            JavascriptExecutor js = (JavascriptExecutor)driver;
            WebElement forScrollRandomElement = driver.findElement(By.xpath("//*[@id=\"eventForm\"]/div/div[3]/div[4]/img"));
            js.executeScript("arguments[0].scrollIntoView();", forScrollRandomElement);
        }catch(InterruptedException e){
            System.out.println("InterruptedException");
        }

//        WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(5000)); // for explicit wait
//        wait.until(ExpectedConditions.visibilityOf(forScrollRandomElement));


        SwoopProject monthFunction = new SwoopProject();

        List<WebElement> modelViewElements = driver.findElements(By.xpath("//form[@id=\"eventForm\"]/div/div[@class='right-content']/div[1]/p"));
        for(int i=0; i<modelViewElements.size(); i++){
            String textFromList = modelViewElements.get(i).getText();
            System.out.println(textFromList);

            if(Objects.equals(textFromList, movieNameOnPageText)){
                System.out.println(textFromList +':'+ movieNameOnPageText);
            }else if (Objects.equals(textFromList, cinemaNameForValidation)) {
                System.out.println(textFromList +':'+ cinemaNameForValidation);
            }else{
                String[] result = textFromList.split(" ");
                System.out.println(result[0] +':'+ date[2]);
                System.out.println(result[2] +':'+ timeValidation);
                monthFunction.monthNumberCalculator(result[1],monthNumber);
                System.out.println(result[1]);

            }
        }


    }

    @Test
    public void chooseAnyAvailableSeat(){
        makeValidations();
        List<WebElement> freeSeats = driver.findElements(By.xpath("//div[@class=\"seance\"]/div[2]/div[@class='seat free']"));
        freeSeats.get(freeSeats.size() - 1).click();
    }
    @Test
    public void registration(){
        chooseAnyAvailableSeat();

        WebElement registrationButton = driver.findElement(By.xpath("//*[@class=\"authorization-active ui-tabs-anchor\"]/p[text()='რეგისტრაცია']"));
        registrationButton.click();
        WebElement vipButton = driver.findElement(By.xpath("//p[@class = 'profile-tabs__link' and text()='იურიდიული პირი']"));
        vipButton.click();
        WebElement optionDropdown = driver.findElement(By.xpath("//*[@id=\"lLegalForm\"]"));
        Select select = new Select(optionDropdown);
        String defaultOption = select.getFirstSelectedOption().getText();
        List<WebElement> listOfOptions = select.getOptions();
        for(int i = 0 ; i <= listOfOptions.size(); i++){
            String option = listOfOptions.get(i).getText();
            if(Objects.equals(defaultOption, option)){
                listOfOptions.get(i).click();
                continue;
            }else{
                listOfOptions.get(i).click();
                break;
            }
        }
    }

    @Test
    public void companyNameInput() {
        registration();
        String upperAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerAlphabet = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";

        String alphaNumeric = upperAlphabet + lowerAlphabet + numbers;
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        int length = 10;

        for(int i = 0; i < length; i++) {
            int index = random.nextInt(alphaNumeric.length());
            char randomChar = alphaNumeric.charAt(index);
            sb.append(randomChar);
        }
        String randomString = sb.toString();
        WebElement companyName = driver.findElement(By.id("lName"));
        companyName.sendKeys(randomString);
    }

    @Test
    public void identificationCodeInput(){
        companyNameInput();
        Random rand = new Random();
        int randomCompanyCode = 1000000 + rand.nextInt(900000);
        String codeIntoStringFormat = String.valueOf(randomCompanyCode);
        WebElement companyCode= driver.findElement(By.id("lTaxCode"));
        companyCode.sendKeys(codeIntoStringFormat);

    }


    @Test
    public void CompanyInfo(){
        identificationCodeInput();

        WebElement datePicker = driver.findElement(By.xpath("//fieldset[@class=\"birthday-picker mail-input\"]/input[@id='registred']"));
        datePicker.sendKeys("03232017");

        WebElement address = driver.findElement(By.id("lAddress"));
        address.sendKeys("virtual space, N 3");

        WebElement optionDropdown = driver.findElement(By.id("lCountryCode"));
        Select select = new Select(optionDropdown);
        String defaultOption = select.getFirstSelectedOption().getText();
        List<WebElement> listOfOptions = select.getOptions();
        for(int i = 0 ; i <= listOfOptions.size(); i++){
            String option = listOfOptions.get(i).getText();
            if(Objects.equals(defaultOption, option)){
                listOfOptions.get(i).click();
                continue;
            }else{
                listOfOptions.get(i).click();
                break;
            }
        }

        WebElement city = driver.findElement(By.id("lCity"));
        city.sendKeys("Tbilisi");

        Random rand = new Random();
        int randomCompanyCode = 1000 + rand.nextInt(9000);
        String codeIntoStringFormat = String.valueOf(randomCompanyCode);
        WebElement postalCode = driver.findElement(By.id("lPostalCode"));
        postalCode.sendKeys(codeIntoStringFormat);

        WebElement url = driver.findElement(By.id("lWebSite"));
        url.sendKeys(driver. getCurrentUrl());

        WebElement bankName = driver.findElement(By.id("lBank"));
        bankName.sendKeys("TBC");

        WebElement IBAN = driver.findElement(By.id("lBankAccount"));
        IBAN.sendKeys("TB245678900020202");

    }



    @Test
    public void personalInfo(){
        CompanyInfo();

        WebElement email = driver.findElement(By.id("lContactPersonEmail"));
        email.sendKeys("emailNotValidWithoutAt");
        WebElement password1 = driver.findElement(By.id("lContactPersonPassword"));
        password1.sendKeys("FakePassword123!");
        WebElement password2 = driver.findElement(By.id("lContactPersonConfirmPassword"));
        password2.sendKeys("FakePassword123!!");
        WebElement contactPersonName = driver.findElement(By.id("lContactPersonName"));
        contactPersonName.sendKeys("Jahn Doe");

        WebElement defaultGender = driver.findElement(By.id("lContactPersonGender"));
        Select select = new Select(defaultGender);
        String defaultOption = select.getFirstSelectedOption().getText();
        List<WebElement> listOfOptions = select.getOptions();
        for(int i = 0 ; i <= listOfOptions.size(); i++){
            String option = listOfOptions.get(i).getText();
            if(Objects.equals(defaultOption, option)){
                listOfOptions.get(i).click();
                continue;
            }else{
                listOfOptions.get(i).click();
                break;
            }
        };

        WebElement datePicker = driver.findElement(By.xpath("//fieldset[@class=\"birthday-picker mail-input\"]/input[@name='bday']"));
        datePicker.sendKeys("03232020");

        WebElement defaultCitizen = driver.findElement(By.id("lContactPersonCountryCode"));
        Select selectCitizen = new Select(defaultCitizen);
        String defaultOptionCitizen = selectCitizen.getFirstSelectedOption().getText();
        List<WebElement> listOfOptionsCitizens = selectCitizen.getOptions();
        for(int f = 0 ; f <= listOfOptionsCitizens.size(); f++){
            String option = listOfOptionsCitizens.get(f).getText();
            if(Objects.equals(defaultOptionCitizen, option)){
                listOfOptionsCitizens.get(f).click();
                continue;
            }else{
                listOfOptionsCitizens.get(f).click();
                break;
            }
        }
        WebElement personalID = driver.findElement(By.id("lContactPersonPersonalID"));
        personalID.sendKeys("12346789010");

        WebElement mobNum = driver.findElement(By.id("lContactPersonPhone"));
        mobNum.sendKeys("12346789010");
        WebElement registrationButton = driver.findElement(By.xpath("//*[@id=\"register-content-2\"]/div[2]/a/div/input"));
        registrationButton.click();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(90000));
        WebElement errorMessage = driver.findElement(By.id("legalInfoMassage"));
        WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(500));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("legalInfoMassage")));
        System.out.println(errorMessage.isDisplayed());
        JavascriptExecutor js = (JavascriptExecutor)driver;
        String errorMessageTextActual = (String)js.executeScript("return arguments[0].innerHTML;", errorMessage);
        // String errorMessageTextActual =  errorMessage.getText();
        System.out.println(errorMessageTextActual);
        String errorMessageTextExpected = "რეგისტრაციის დროს დაფიქსირდა შეცდომა!";
        Assert.assertEquals(errorMessageTextActual,errorMessageTextExpected);

    }

    @AfterMethod
    public void tearDown(){
        driver.quit();
    }
}
