package org.pricetrackerbatchapp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    @Test
    public void productIdExtractionTest1() {
        assertEquals("B00QXT5T3U",
                JsoupScraper.getProductIdFromUrl("https://www.amazon.com/gp/product/B00QXT5T3U?pf_rd_r=KCDWFG5AM49DR8MBPAWH&pf_rd_p=1ab92b69-98d7-4842-a89b-ad387c54783f&pd_rd_r=d5d2119c-7294-4193-8c46-c8f78493be1a&pd_rd_w=W1EKM&pd_rd_wg=V1j21&ref_=pd_gw_unk"));
    }

    @Test
    public void productIdExtractionTest2() {
        assertEquals("B079JLY5M5",
                JsoupScraper.getProductIdFromUrl("https://www.amazon.com/Logitech-MK270-Wireless-Keyboard-Mouse/dp/B079JLY5M5/ref=sr_1_3?crid=2ZPLCETMTBMD3&keywords=wireless+keyboard&qid=1636947938&sprefix=wireless+ke%2Caps%2C175&sr=8-3"));
    }

    @Test
    public void scrapeTest() {
        Product testProduct = new Product("B087Z5WDJ2", "Logitech M510 Wireless Computer Mouse for PC with USB Unifying Receiver - Graphite", "$19.99", "https://www.amazon.com/Logitech-Wireless-Computer-Unifying-Receiver/dp/B087Z5WDJ2/ref=sxin_14_ac_d_mf_br?ac_md=1-0-TG9naXRlY2g%3D-ac_d_mf_br_br&cv_ct_cx=wireless%2Bmouse&keywords=wireless%2Bmouse&pd_rd_i=B087Z5WDJ2&pd_rd_r=2e79feb4-3dac-4c59-a80d-85b3d176bef5&pd_rd_w=R2SbG&pd_rd_wg=Kn7Z9&pf_rd_p=b0c493d8-5fdd-4188-b852-c552a4a3abdb&pf_rd_r=E17DYAW3SW9BSFZ2PKBR&qid=1636988574&sr=1-1-ed8a42d3-65f1-4884-a3a2-0dd6e83b6876&th=1");
        assertEquals(testProduct, JsoupScraper.scrapeProductFromUrl("B087Z5WDJ2","https://www.amazon.com/Logitech-Wireless-Computer-Unifying-Receiver/dp/B087Z5WDJ2/ref=sxin_14_ac_d_mf_br?ac_md=1-0-TG9naXRlY2g%3D-ac_d_mf_br_br&cv_ct_cx=wireless%2Bmouse&keywords=wireless%2Bmouse&pd_rd_i=B087Z5WDJ2&pd_rd_r=2e79feb4-3dac-4c59-a80d-85b3d176bef5&pd_rd_w=R2SbG&pd_rd_wg=Kn7Z9&pf_rd_p=b0c493d8-5fdd-4188-b852-c552a4a3abdb&pf_rd_r=E17DYAW3SW9BSFZ2PKBR&qid=1636988574&sr=1-1-ed8a42d3-65f1-4884-a3a2-0dd6e83b6876&th=1"));
    }

    @Test
    public void astraDbConnectionTest() {
        AstraDbConnector.buildBlockingStub();
    }

    @Test
    public void astraDbAllDataTest() {
        AstraDbUtil.getUserProductsMapFromDB();
    }

    @Test
    public void testEmailMessage() {
        List<Product> productList = Arrays.asList(new Product("ABCDE12345", "Logitech M510 Wireless Computer Mouse for PC with USB Unifying Receiver - Graphite", "$200.00", "tes-url"),
                new Product("PQRST54321", "Test Product 1 - Price and Name", "$15.99", "tes-url"),
                new Product("LMNOP10101", "Logitech M510 Wireless Computer Mouse for PC", "$1009.51", "tes-url"));

        String htmlMessage = MailSender.createHtmlMessage(productList);
        System.out.println(htmlMessage);
    }
}