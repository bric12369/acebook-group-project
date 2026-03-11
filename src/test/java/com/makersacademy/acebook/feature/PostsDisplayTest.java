package com.makersacademy.acebook.feature;

import com.github.javafaker.Faker;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class PostsDisplayTest {

    Playwright playwright;
    Browser browser;
    Page page;
    Faker faker;

    @BeforeEach
    public void setup() {
        faker = new Faker();
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(false)
        );
        page = browser.newPage();

        String email = faker.name().username() + "@email.com";

        page.navigate("http://localhost:8081/");
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Sign up")).click();
        page.locator("[name=email]").fill(email);
        page.locator("[name=password]").fill("P@55qw0rd");
        page.locator("[name=action]").click();
    }

    @AfterEach
    public void tearDown() {
        browser.close();
        playwright.close();
    }

    @Test
    public void mostRecentPostIsDisplayedFirst() {
        page.navigate("http://localhost:8081/posts");

        page.locator("[name='content']").fill("First post");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit")).click();

        page.locator("[name='content']").fill("Second post");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit")).click();

        page.locator("[name='content']").fill("Third post");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit")).click();

        assertThat(page.locator("li").nth(0)).hasText("Third post");
        assertThat(page.locator("li").nth(1)).hasText("Second post");
        assertThat(page.locator("li").nth(2)).hasText("First post");
    }
}
