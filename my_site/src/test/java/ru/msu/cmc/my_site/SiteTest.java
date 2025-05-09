package ru.msu.cmc.my_site;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.assertTrue;


import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class SiteTest {
    private WebDriver driver;

    @BeforeEach
    public void setUp() throws InterruptedException {
        // Укажи путь к chromedriver, если он не прописан в переменной окружения PATH
        System.setProperty("webdriver.chrome.driver", "D:\\javaprack\\my_site_charging\\drivers\\chromedriver-win64\\chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        options.setBinary("D:\\Downloads\\chrome-win64\\chrome-win64\\chrome.exe");
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();

        // Авторизация
        driver.get("http://localhost:8080/login");

        driver.findElement(By.name("username")).sendKeys("Stacy");
        driver.findElement(By.name("password")).sendKeys("0000");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        Thread.sleep(3000); // подождать завершения редиректа
    }

    //сценарий №1 — Получение списка служащих с фильтрацией по имени, должности и стажу:
    //сценарий 8: чтение данных о служащем: главная -> список служащих -> страница служащего
    @Test
    public void testEmployeeListWithFilters() throws InterruptedException {
        // Перейти на главную страницу
        driver.get("http://localhost:8080");

        // Кликнуть на "Служащие"
        WebElement employeesLink = driver.findElement(By.linkText("Служащие"));
        employeesLink.click();
        Thread.sleep(1000);

        // Найти поле фильтра по имени и ввести часть ФИО
        WebElement nameInput = driver.findElement(By.name("namePart"));
        nameInput.clear();
        nameInput.sendKeys("а"); // например, чтобы найти Анну

        // Выбрать должность "Разработчик"
        WebElement postSelect = driver.findElement(By.name("postId"));
        List<WebElement> postOptions = postSelect.findElements(By.tagName("option"));
        for (WebElement option : postOptions) {
            if (option.getText().trim().equals("Разработчик")) {
                option.click();
                break;
            }
        }

        // Указать стаж
        WebElement experienceInput = driver.findElement(By.name("experience"));
        experienceInput.clear();
        experienceInput.sendKeys("1");

        // Нажать кнопку поиска
        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submitButton);
        Thread.sleep(1000);

        // Проверить, что отфильтрованная таблица содержит хотя бы одну строку
        List<WebElement> filteredRows = driver.findElements(By.cssSelector("table tbody tr"));
        assertTrue(filteredRows.size() > 0, "Фильтрация должна вернуть хотя бы одного сотрудника");
    }

    // Сценарий 2: Получение истории участия в проектах и карьерной истории для
    //служащего: главная -> список служащих (нажать на номер служащего) ->
    //страница служащего -> таблица должностей или таблица с проектами
    @Test
    public void testEmployeeDetailsPage() throws InterruptedException {
        // Шаг 1: Открыть главную страницу
        driver.get("http://localhost:8080");

        // Шаг 2: Перейти в раздел "Служащие"
        WebElement employeesLink = driver.findElement(By.linkText("Служащие"));
        employeesLink.click();
        Thread.sleep(1000);

        // Шаг 3: Нажать на первого сотрудника в списке
        WebElement firstLink = driver.findElement(By.cssSelector("table tbody tr td a"));
        String employeeName = firstLink.getText();
        firstLink.click();
        Thread.sleep(1000);

        // Шаг 4: Проверка — заголовок страницы содержит имя
        WebElement header = driver.findElement(By.tagName("h2"));
        assertTrue(header.getText().contains(employeeName), "Заголовок должен содержать имя сотрудника");

        // Шаг 5: Проверка — отображается таблица с должностями
        WebElement positionsTable = driver.findElement(By.xpath("//h5[contains(text(), 'История должностей')]/following-sibling::table"));
        assertTrue(positionsTable.isDisplayed(), "Таблица истории должностей должна отображаться");

        // Шаг 6: Проверка — отображается таблица с проектами
        WebElement projectsTable = driver.findElement(By.xpath("//h4[contains(text(), 'Проекты')]/following-sibling::table"));
        assertTrue(projectsTable.isDisplayed(), "Таблица проектов должна отображаться");

        Thread.sleep(2000);
    }

    //Сценарий №3 — Получение истории выплат для служащего: главная → история выплат → фильтр по номеру служащего.
    @Test
    public void testPaymentHistoryFilterByEmployeeId() throws InterruptedException {
        // Шаг 1: Открыть главную страницу
        driver.get("http://localhost:8080");

        // Шаг 2: Перейти в "История выплат"
        WebElement paymentsLink = driver.findElement(By.linkText("История выплат"));
        paymentsLink.click();
        Thread.sleep(1000);

        // Шаг 3: Ввести ID существующего сотрудника
        WebElement employeeIdInput = driver.findElement(By.name("employeeId"));
        employeeIdInput.sendKeys("1"); // предполагается, что сотрудник с ID = 1 существует

        // Шаг 4: Нажать кнопку фильтра
        WebElement filterButton = driver.findElement(By.cssSelector("button[type='submit']"));
        filterButton.click();
        Thread.sleep(1000);

        // Шаг 5: Проверить, что таблица не пуста (если выплаты есть)
        List<WebElement> rows = driver.findElements(By.cssSelector("table tbody tr"));
        assertTrue(rows.size() > 0, "Ожидается хотя бы одна запись в истории выплат для сотрудника");

        Thread.sleep(2000);
    }

    //Сценарий №4 — Назначение служащего на новую должность: главная → список служащих → карточка сотрудника → редактирование (смена должности)
    @Test
    public void testUpdateEmployeePost() throws InterruptedException {
        // Шаг 1: Открыть главную страницу
        driver.get("http://localhost:8080");

        // Шаг 2: Перейти в "Служащие"
        driver.findElement(By.linkText("Служащие")).click();
        Thread.sleep(1000);

        // Шаг 3: Открыть карточку первого служащего
        WebElement firstEmployee = driver.findElement(By.cssSelector("table tbody tr td a"));
        firstEmployee.click();
        Thread.sleep(1000);

        // Шаг 4: Нажать "Редактировать"
        driver.findElement(By.linkText("Редактировать")).click();
        Thread.sleep(1000);

        // Шаг 5: Выбрать другую должность
        WebElement postSelect = driver.findElement(By.id("postId"));
        postSelect.findElements(By.tagName("option")).get(1).click(); // выбираем вторую по списку

        // Шаг 6: Нажать "Сохранить"

        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebElement button = driver.findElement(By.cssSelector("button[type='submit']"));
        js.executeScript("arguments[0].click();", button);
        Thread.sleep(1500);

        // Шаг 7: Проверить, что вернулись на страницу списка
        assertTrue(driver.getCurrentUrl().contains("/employees"), "Должны вернуться на страницу служащих после сохранения");
    }


    //Сценарий 5: добавление и удаление участника проекта:
    @Test
    public void testAddAndRemoveParticipantFromProject() throws InterruptedException {
        // Перейти на главную страницу
        driver.get("http://localhost:8080");

        // Перейти в раздел "Проекты"
        WebElement projectsLink = driver.findElement(By.linkText("Проекты"));
        projectsLink.click();

        // Клик по первому проекту из списка
        WebElement firstProject = driver.findElement(By.cssSelector("table tbody tr td a"));
        firstProject.click();

        // Нажать "Добавить участника"
        WebElement addParticipantLink = driver.findElement(By.linkText("Добавить участника"));
        addParticipantLink.click();

        // Выбрать первого сотрудника
        WebElement employeeSelect = driver.findElement(By.name("employeeId"));
        List<WebElement> employees = employeeSelect.findElements(By.tagName("option"));
        if (employees.size() > 1) {
            employees.get(1).click(); // первый после пустого
        }

        // Выбрать первую роль
        WebElement roleSelect = driver.findElement(By.name("roleId"));
        List<WebElement> roles = roleSelect.findElements(By.tagName("option"));
        if (roles.size() > 0) {
            roles.get(0).click();
        }

        // Ввести доплату
        WebElement paymentInput = driver.findElement(By.name("payment"));
        paymentInput.clear();
        paymentInput.sendKeys("777");

        // Сохранить через JS
        WebElement saveBtn = driver.findElement(By.cssSelector("button[type='submit']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", saveBtn);

        Thread.sleep(1000);

        // Убедиться, что добавлен участник (ищем строку таблицы с доплатой 777)
        List<WebElement> rows = driver.findElements(By.cssSelector("table tbody tr"));
        boolean found = rows.stream().anyMatch(r -> r.getText().contains("777"));
        assertTrue(found, "Новый участник должен быть в таблице");

        // Теперь удалим этого участника
        WebElement deleteForm = rows.stream()
                .filter(r -> r.getText().contains("777"))
                .findFirst()
                .get()
                .findElement(By.tagName("form"));

        WebElement deleteBtn = deleteForm.findElement(By.cssSelector("button"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deleteBtn);

        // Подтверждение alert (если появится)
        try {
            Alert alert = driver.switchTo().alert();
            alert.accept();
        } catch (NoAlertPresentException ignored) {}

        Thread.sleep(1000);

        // Убедимся, что участник больше не отображается
        List<WebElement> updatedRows = driver.findElements(By.cssSelector("table tbody tr"));
        boolean stillExists = updatedRows.stream().anyMatch(r -> r.getText().contains("777"));
        assertTrue(!stillExists, "Участник должен быть удалён");
    }

    //Сценарий 6: Добавление служащего через Selenium:
    //Сценарий 7: Удаление служащего:
    @Test
    public void testAddDeleteNewEmployee() throws InterruptedException {
        driver.get("http://localhost:8080");

        // Перейти в раздел "Служащие"
        WebElement employeesLink = driver.findElement(By.linkText("Служащие"));
        employeesLink.click();

        // Нажать кнопку "Добавить нового сотрудника"
        WebElement addBtn = driver.findElement(By.cssSelector("a.btn.btn-success"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addBtn);

        // Заполнить форму добавления
        driver.findElement(By.id("name")).sendKeys("Новый Тест Тестович");
        driver.findElement(By.id("address")).sendKeys("Тестовый адрес");
        driver.findElement(By.id("birthDate")).sendKeys("1990-01-01");
        driver.findElement(By.id("education")).sendKeys("Тестовая");
        driver.findElement(By.id("experience")).sendKeys("3");

        // Выбрать должность из выпадающего списка
        WebElement postSelect = driver.findElement(By.id("postId"));
        List<WebElement> options = postSelect.findElements(By.tagName("option"));
        if (options.size() > 1) {
            options.get(1).click(); // выбираем первую доступную должность
        }

        // Сохранить
        WebElement saveBtn = driver.findElement(By.cssSelector("button[type='submit']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", saveBtn);

        Thread.sleep(1500);

        // Проверка: перешли на список сотрудников и новый сотрудник отображается
        List<WebElement> employeeLinks = driver.findElements(By.cssSelector("table tbody tr td a"));
        boolean exists = employeeLinks.stream().anyMatch(link -> link.getText().contains("Новый Тест Тестович"));
        assertTrue(exists, "Новый сотрудник должен появиться в списке");




        //удаление
        driver.get("http://localhost:8080");

        // Перейти в раздел "Служащие"
        WebElement employeesLink_del = driver.findElement(By.linkText("Служащие"));
        employeesLink_del.click();
        Thread.sleep(1000);

        // Найдём ссылку на сотрудника по имени
        List<WebElement> employeeLinks_del = driver.findElements(By.cssSelector("table tbody tr td a"));
        WebElement target = employeeLinks_del.stream()
                .filter(link -> link.getText().contains("Новый Тест Тестович"))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Сотрудник для удаления не найден"));

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", target);
        Thread.sleep(500);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", target);
        Thread.sleep(1000);

        // Нажать кнопку "Удалить"
        WebElement deleteBtn = driver.findElement(By.cssSelector("form button.btn.btn-danger"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deleteBtn);

        // Подтвердить alert, если он есть
        try {
            driver.switchTo().alert().accept();
        } catch (Exception e) {
            // alert не появился — ничего страшного
        }

        Thread.sleep(1000);

        // Проверим, что сотрудник исчез из списка
        List<WebElement> remaining = driver.findElements(By.cssSelector("table tbody tr td a"));
        boolean stillExists = remaining.stream()
                .anyMatch(link -> link.getText().contains("Новый Тест Тестович"));
        assertTrue(!stillExists, "Сотрудник должен быть удалён из списка");
    }

    //сценарий 9: Редактирование данных о служащем:
    //главная → список служащих → страница служащего → редактирование.(меняю адрес)
    @Test
    public void testEditEmployeeData() throws InterruptedException {
        // Шаг 1: Открыть главную страницу
        driver.get("http://localhost:8080");

        // Шаг 2: Перейти в раздел "Служащие"
        WebElement employeesLink = driver.findElement(By.linkText("Служащие"));
        employeesLink.click();
        Thread.sleep(1000);

        // Шаг 3: Клик по первому сотруднику
        WebElement firstEmployee = driver.findElement(By.cssSelector("table tbody tr td a"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", firstEmployee);
        Thread.sleep(1000);

        // Шаг 4: Нажать "Редактировать"
        WebElement editBtn = driver.findElement(By.linkText("Редактировать"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", editBtn);
        Thread.sleep(1000);

        // Шаг 5: Изменим адрес
        WebElement addressInput = driver.findElement(By.id("address"));
        addressInput.clear();
        addressInput.sendKeys("Новый тестовый адрес");

        // Шаг 6: Сохранить
        WebElement saveBtn = driver.findElement(By.cssSelector("button[type='submit']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", saveBtn);
        Thread.sleep(1500);

        // Шаг 7: Проверим, что нас перекинуло обратно на список
        WebElement header = driver.findElement(By.tagName("h2"));
        assertTrue(header.getText().contains("Список служащих"), "После редактирования должно вернуться на список");
    }

    //сценарии 10 и 11: Добавление проекта и удаление проекта
    @Test
    public void testAddNewProjectOnly() throws InterruptedException {
        // Шаг 1: Перейти на главную страницу
        driver.get("http://localhost:8080");

        // Шаг 2: Перейти в "Проекты"
        WebElement projectsLink = driver.findElement(By.linkText("Проекты"));
        projectsLink.click();
        Thread.sleep(1000);

        // Шаг 3: Нажать "Добавить проект"
        WebElement addBtn = driver.findElement(By.linkText("Новый проект"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addBtn);
        Thread.sleep(1000);

        // Шаг 4: Заполнить форму проекта
        driver.findElement(By.id("projectName")).sendKeys("Новый проект для теста");
        driver.findElement(By.id("startDate")).sendKeys("01-01-2025");

        WebElement statusSelect = driver.findElement(By.id("status"));
        List<WebElement> options = statusSelect.findElements(By.tagName("option"));
        if (options.size() > 1) {
            options.get(1).click(); // выбрать статус, например "в процессе"
        }

        // Шаг 5: Сохранить проект
        WebElement saveBtn = driver.findElement(By.cssSelector("button[type='submit']"));
        // Сохраняем
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", saveBtn);
        Thread.sleep(1000);

// Явно возвращаемся к списку
        driver.get("http://localhost:8080/projects");
        Thread.sleep(1000);

// Теперь проверяем наличие проекта
        List<WebElement> projectLinks = driver.findElements(By.cssSelector("table tbody tr td a"));
        boolean projectExists = projectLinks.stream()
                .anyMatch(link -> link.getText().contains("Новый проект для теста"));
        assertTrue(projectExists, "Добавленный проект должен появиться в списке проектов");
    }


    //сценарий 12: Чтение данных о проекте — переход на страницу проекта из списка:
    @Test
    public void testViewProjectDetails() throws InterruptedException {
        // Шаг 1: Перейти на главную
        driver.get("http://localhost:8080");

        // Шаг 2: Перейти в "Проекты"
        WebElement projectsLink = driver.findElement(By.linkText("Проекты"));
        projectsLink.click();
        Thread.sleep(1000);

        // Шаг 3: Кликнуть на первый проект в списке
        WebElement firstProjectLink = driver.findElement(By.cssSelector("table tbody tr td a"));
        String projectName = firstProjectLink.getText(); // Сохраняем имя проекта для проверки
        firstProjectLink.click();
        Thread.sleep(1000);

        // Шаг 4: Проверить, что открыта страница проекта с тем же именем
        WebElement header = driver.findElement(By.tagName("h2"));
        assertTrue(header.getText().contains(projectName), "Должна открыться карточка проекта с его названием");
    }

    //сценарий 13: Редактирование данных о проекте:
    @Test
    public void testEditProject() throws InterruptedException {
        driver.get("http://localhost:8080");

        // Перейти в "Проекты"
        driver.findElement(By.linkText("Проекты")).click();
        Thread.sleep(1000);

        // Клик по первому проекту
        WebElement firstProjectLink = driver.findElement(By.cssSelector("table tbody tr td a"));
        String projectName = firstProjectLink.getText(); // Сохраняем имя проекта
        firstProjectLink.click();
        Thread.sleep(1000);

        // Нажать "Редактировать проект"
        WebElement editButton = driver.findElement(By.xpath("//a[contains(text(),'Редактировать проект')]"));
        editButton.click();
        Thread.sleep(1000);

        // Изменить статус на "закрыт"
        WebElement statusSelect = driver.findElement(By.id("status"));
        List<WebElement> options = statusSelect.findElements(By.tagName("option"));
        for (WebElement option : options) {
            if (option.getText().equals("закрыт")) {
                option.click();
                break;
            }
        }

        // Сохранить изменения
        WebElement saveBtn = driver.findElement(By.cssSelector("button[type='submit']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", saveBtn);
        Thread.sleep(1000);

        // Вернуться на список проектов
        driver.get("http://localhost:8080/projects");
        Thread.sleep(1000);

        // Найти строку с нужным проектом
        List<WebElement> projectRows = driver.findElements(By.cssSelector("table tbody tr"));
        WebElement matchedRow = projectRows.stream()
                .filter(row -> row.getText().contains(projectName))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Не удалось найти отредактированный проект"));

        // Проверить, что статус обновлён
        WebElement statusCell = matchedRow.findElement(By.cssSelector("td:nth-child(4)"));
        assertTrue(statusCell.getText().equals("закрыт"), "Статус проекта должен быть обновлён на 'закрыт'");
    }

    //сценарий 14: добавление политики выплат (премии):
    @Test
    public void testAddAwardPolicy() throws InterruptedException {
        // Шаг 1: Перейти на главную
        driver.get("http://localhost:8080");

        // Шаг 2: Перейти в раздел "Политики выплат"
        driver.findElement(By.linkText("Политики выплат")).click();
        Thread.sleep(1000);

        // Шаг 3: Перейти в раздел "Премии"
        driver.findElement(By.linkText("Премии")).click(); // если в интерфейсе так названо
        Thread.sleep(1000);

        // Шаг 4: Нажать "Добавить"
        WebElement addButton = driver.findElement(By.linkText("Добавить премию"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addButton);
        Thread.sleep(1000);

        // Шаг 5: Заполнить форму
        driver.findElement(By.id("awardName")).sendKeys("Премия за успехи");
        driver.findElement(By.id("payment")).sendKeys("5000");
        driver.findElement(By.id("description")).sendKeys("За достижения и вклад в проект");

        // Шаг 6: Сохранить
        WebElement saveButton = driver.findElement(By.cssSelector("button[type='submit']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", saveButton);

        // Шаг 7: Убедиться, что премия добавлена
        Thread.sleep(1500);
        List<WebElement> rows = driver.findElements(By.cssSelector("table tbody tr"));
        boolean found = rows.stream().anyMatch(row -> row.getText().contains("Премия за успехи"));
        assertTrue(found, "Премия должна быть добавлена и отображаться в таблице");
    }

    //сценарии 14 и 15: добавление и удаление политики выплат:
    @Test
    public void testAddAndDeleteAwardPolicy() throws InterruptedException {
        driver.get("http://localhost:8080");

        // Перейти в "Политики выплат"
        driver.findElement(By.linkText("Политики выплат")).click();
        Thread.sleep(1000);

        // Перейти в "Премии"
        driver.findElement(By.linkText("Премии")).click();
        Thread.sleep(1000);

        // Нажать "Добавить премию"
        WebElement addButton = driver.findElement(By.linkText("Добавить премию"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addButton);
        Thread.sleep(1000);

        // Заполнить форму
        driver.findElement(By.id("awardName")).sendKeys("Тестовая премия");
        driver.findElement(By.id("payment")).sendKeys("7000");
        driver.findElement(By.id("description")).sendKeys("Для теста Selenium");

        // Сохранить
        WebElement saveButton = driver.findElement(By.cssSelector("button[type='submit']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", saveButton);
        Thread.sleep(1500);

        // Найти строку с "Тестовая премия"
        List<WebElement> rows = driver.findElements(By.cssSelector("table tbody tr"));
        WebElement targetRow = rows.stream()
                .filter(row -> row.getText().contains("Тестовая премия"))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Премия не найдена после добавления"));

        // Найти и нажать кнопку "Удалить" в этой строке
        WebElement deleteBtn = targetRow.findElement(By.cssSelector("form button.btn-danger"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deleteBtn);

        try {
            driver.switchTo().alert().accept(); // Подтвердить alert
        } catch (Exception e) {
            // alert не появился — ок
        }

        Thread.sleep(1000);

        // Проверка: премии больше нет в списке
        List<WebElement> remainingRows = driver.findElements(By.cssSelector("table tbody tr"));
        boolean stillExists = remainingRows.stream()
                .anyMatch(row -> row.getText().contains("Тестовая премия"));
        assertTrue(!stillExists, "Премия должна быть удалена из таблицы");
    }

    //сценария 16 — чтение политик выплат:
    @Test
    public void testReadAllPaymentPolicies() throws InterruptedException {
        driver.get("http://localhost:8080");

        // Переход в раздел "Политики выплат"
        driver.findElement(By.linkText("Политики выплат")).click();
        Thread.sleep(1000);

        // Проверка каждой вкладки по очереди
        String[] links = {
                "По должности",
                "По стажу",
                "Премии",
                "По проектам и ролям"
        };

        for (String linkText : links) {
            // Переход на страницу политики выплат
            driver.get("http://localhost:8080/payment-policies");
            Thread.sleep(1000);

            // Клик по нужной политике
            driver.findElement(By.linkText(linkText)).click();
            Thread.sleep(1000);

            // Проверка, что таблица не пуста
            List<WebElement> rows = driver.findElements(By.cssSelector("table tbody tr"));
            assertTrue(rows.size() > 0, "На странице \"" + linkText + "\" должна быть хотя бы одна строка");
        }
    }

    //Сценарий 17 — редактирование политик выплат:
    // Изменяем выплату по стажу
    // Обновляем название и описание премии
    // Повышаем выплату по должности
    // Добавляем роль к проекту (это сценарий 18: добавление и удаление и редактирование политик выплат по проекту и
    //ролям: надо менять таблицу ролей и выплат на странице проекта. Попасть
    //туда можно двумя способами:)

    @Test
    public void testEditPaymentPoliciesAndProjectRoles() throws InterruptedException {
        driver.get("http://localhost:8080");

// 1. Редактирование выплаты по стажу
        driver.findElement(By.linkText("Политики выплат")).click();
        Thread.sleep(500);
        driver.findElement(By.linkText("По стажу")).click();
        Thread.sleep(500);

// Получим значение стажа, которое редактируем
        WebElement editLink = driver.findElement(By.linkText("Редактировать"));
        WebElement experienceCell = editLink.findElement(By.xpath("./ancestor::tr/td[1]"));
        int experience = Integer.parseInt(experienceCell.getText());

// Редактирование выплаты
        editLink.click();
        WebElement paymentInput = driver.findElement(By.id("payment"));
        int oldValue = Integer.parseInt(paymentInput.getAttribute("value"));
        int newValue = oldValue + 1;
        paymentInput.clear();
        paymentInput.sendKeys(String.valueOf(newValue));

// Сохранение
        WebElement saveBtn = driver.findElement(By.cssSelector("button[type='submit']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", saveBtn);
        Thread.sleep(1000);

// Проверка — возвращаемся и ищем нужную строку
        driver.get("http://localhost:8080/experience-payments");
        Thread.sleep(1000);
        List<WebElement> rows = driver.findElements(By.cssSelector("table tbody tr"));
        WebElement targetRow = rows.stream()
                .filter(row -> row.findElement(By.cssSelector("td:nth-child(1)")).getText().equals(String.valueOf(experience)))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Не найдена строка с нужным стажем"));

        int displayedValue = Integer.parseInt(targetRow.findElement(By.cssSelector("td:nth-child(2)")).getText());
        assertTrue(displayedValue == newValue, "Значение выплаты должно быть обновлено");

        // 2. Редактирование премии
        driver.get("http://localhost:8080/payment-policies");
        driver.findElement(By.linkText("Премии")).click();
        Thread.sleep(500);
        driver.findElement(By.linkText("Редактировать")).click();

        WebElement nameField = driver.findElement(By.id("awardName"));
        nameField.clear();
        nameField.sendKeys("Супер премия");

        WebElement saveBtn2 = driver.findElement(By.cssSelector("button[type='submit']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", saveBtn2);
        Thread.sleep(1000);

        List<WebElement> cells = driver.findElements(By.cssSelector("table tbody tr td"));
        boolean found = cells.stream().anyMatch(cell -> cell.getText().contains("Супер премия"));
        assertTrue(found, "Ожидалось найти премию с названием 'Супер премия'");

        // 3. Редактирование выплаты по должности
        driver.get("http://localhost:8080/payment-policies");
        driver.findElement(By.linkText("По должности")).click();
        Thread.sleep(500);

// Находим строку и сохраняем название должности
        WebElement editAnchor = driver.findElement(By.xpath("//a[contains(text(),'Редактировать')]"));
        WebElement tableRow = editAnchor.findElement(By.xpath("./ancestor::tr"));
        String targetPostTitle = tableRow.findElement(By.cssSelector("td:nth-child(1)")).getText();

        editAnchor.click();
        Thread.sleep(500);

        WebElement salaryInput = driver.findElement(By.id("payment"));
        int previousSalary = Integer.parseInt(salaryInput.getAttribute("value"));
        int updatedSalary = previousSalary + 1;
        salaryInput.clear();
        salaryInput.sendKeys(String.valueOf(updatedSalary));

        WebElement confirmBtn = driver.findElement(By.cssSelector("button[type='submit']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", confirmBtn);
        Thread.sleep(1000);

// Снова ищем нужную строку по названию должности
        List<WebElement> allRows = driver.findElements(By.cssSelector("table tbody tr"));
        WebElement matchedRow = allRows.stream()
                .filter(r -> r.findElement(By.cssSelector("td:nth-child(1)")).getText().equals(targetPostTitle))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Редактированная строка не найдена"));

        String rawText = matchedRow.findElement(By.cssSelector("td:nth-child(2)")).getText();
        String numericOnly = rawText.replaceAll("[^\\d]", ""); // удаляет всё, кроме цифр
        int actualPayment = Integer.parseInt(numericOnly);
        assertTrue(actualPayment == updatedSalary, "Обновлённая сумма выплаты должна совпадать с введённой");

        // 4. Добавление роли в проект
        driver.get("http://localhost:8080/projects");
        Thread.sleep(500);
        driver.findElement(By.cssSelector("table tbody tr td a")).click(); // первый проект
        Thread.sleep(500);
        driver.findElement(By.linkText("Добавить участника")).click();
        Thread.sleep(500);

        new Select(driver.findElement(By.name("employeeId"))).selectByIndex(0);
        new Select(driver.findElement(By.name("roleId"))).selectByIndex(0);

        WebElement paymentInProject = driver.findElement(By.name("payment"));
        paymentInProject.clear();
        paymentInProject.sendKeys("1234");

        WebElement saveBtn4 = driver.findElement(By.cssSelector("button[type='submit']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", saveBtn4);
        Thread.sleep(1000);

        WebElement rolesTable = driver.findElement(By.cssSelector("table.table-bordered tbody"));
        assertTrue(rolesTable.getText().contains("1234"), "Участник с оплатой 1234 добавлен");

        // 5. Удаление этой роли
        // Найти строку с добавленным участником по сумме оплаты 1234
        List<WebElement> participantRows = rolesTable.findElements(By.tagName("tr"));
        for (WebElement participantRow : participantRows) {
            if (participantRow.getText().contains("1234")) {
                WebElement removeButton = participantRow.findElement(By.cssSelector("form button"));
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", removeButton);
                break;
            }
        }
        Thread.sleep(500);

// Подтвердить alert, если он появился
        try {
            driver.switchTo().alert().accept();
        } catch (Exception ignored) {}

        Thread.sleep(1000);

// Проверка: участник с оплатой 1234 должен исчезнуть
        WebElement refreshedRolesTable = driver.findElement(By.cssSelector("table.table-bordered tbody"));
        boolean participantStillExists = refreshedRolesTable.getText().contains("1234");
        assertTrue(!participantStillExists, "Участник с оплатой 1234 должен быть удалён из проекта");
    }



    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}