package View;

import static org.junit.jupiter.api.Assertions.assertTrue;

import Controller.MariaResearch;
import Controller.MariaResearchLogin;
import org.junit.jupiter.api.Test;
import org.testfx.assertions.api.*;
import org.testfx.framework.junit5.ApplicationTest;

/**
 * AppTest for MariaDBApp is a testing suite that focuses of testing the
 *
 */
public class AppTest {

    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {
        assertTrue(true);
    }

    /**
     * MariaDB
     */
    public void testApplication(){
        //MariaResearch mariaResearch = new MariaResearch(1);
        ApplicationTest app = new ApplicationTest(MariaDBApp.class);
    }
}
