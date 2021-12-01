package org.pricetrackerbatchapp;

import static org.junit.Assert.assertEquals;

import jakarta.mail.MessagingException;
import org.junit.Test;

import java.util.Arrays;

/**
 * Unit test for simple App.
 */
public class AppTest {

    @Test
    public void doBatchTest() throws InterruptedException {
        App.doBatch();
    }

    @Test
    public void productIdRegexTests() {
        assertEquals("B00QXT5T3U",
                ProductUtil.getProductIdFromUrl("https://www.amazon.com/gp/product/B00QXT5T3U?pf_rd_r=KCDWFG5AM49DR8MBPAWH&pf_rd_p=1ab92b69-98d7-4842-a89b-ad387c54783f&pd_rd_r=d5d2119c-7294-4193-8c46-c8f78493be1a&pd_rd_w=W1EKM&pd_rd_wg=V1j21&ref_=pd_gw_unk"));
        assertEquals("B079JLY5M5",
                ProductUtil.getProductIdFromUrl("https://www.amazon.com/Logitech-MK270-Wireless-Keyboard-Mouse/dp/B079JLY5M5/ref=sr_1_3?crid=2ZPLCETMTBMD3&keywords=wireless+keyboard&qid=1636947938&sprefix=wireless+ke%2Caps%2C175&sr=8-3"));
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
    public void testFreeMakerTemplate() {
        String html = MailSender.createHtmlMessage("bhandare.shreyas@gmail.com", Arrays.asList(new Product("B0031LKYMY","Logitech M510 Wireless Computer Mouse for PC","$222.70","https://www.amazon.com/Rocker-51396-Pedestal-Gaming-Wireless/dp/B0031LKYMY/?_encoding=UTF8&pf_rd_p=98b093b7-a806-403d-aa39-6ad3a4fa6d14&pd_rd_wg=e0m3j&pf_rd_r=FEQHB2XCZTS5XYQRGDTY&pd_rd_w=QFPUb&pd_rd_r=0c0bcbb5-5a54-4386-9fa8-d83e617750b0&ref_=pd_gw_unk","https://m.media-amazon.com/images/I/81P+F5lBRNL._AC_SX425_.jpg"),
                new Product("B08GKP7YXL", "Segway Ninebot ES1L Electric Kick Scooter", "$299.99", "https://www.amazon.com/Segway-ES1L-Electric-Lightweight-Inner-Support/dp/B08GKP7YXL/?_encoding=UTF8&pf_rd_p=41cfeace-dd87-4c72-a118-1f7af4c58577&pd_rd_wg=i8agC&pf_rd_r=28JBHAR8MKHHEEA6WJTJ&pd_rd_w=KPqdH&pd_rd_r=28a408c0-33d3-4bc4-b126-97bd16f046b5&ref_=pd_gw_unk", "https://m.media-amazon.com/images/I/61EoRZby6RL._AC_SX425_.jpg")));
        System.out.println(html);
    }

    @Test
    public void sendEmailTest() {
        String html = MailSender.createHtmlMessage("bhandare.shreyas@gmail.com", Arrays.asList(new Product("B0031LKYMY","Logitech M510 Wireless Computer Mouse for PC","$222.70","https://www.amazon.com/Rocker-51396-Pedestal-Gaming-Wireless/dp/B0031LKYMY/?_encoding=UTF8&pf_rd_p=98b093b7-a806-403d-aa39-6ad3a4fa6d14&pd_rd_wg=e0m3j&pf_rd_r=FEQHB2XCZTS5XYQRGDTY&pd_rd_w=QFPUb&pd_rd_r=0c0bcbb5-5a54-4386-9fa8-d83e617750b0&ref_=pd_gw_unk","https://m.media-amazon.com/images/I/81P+F5lBRNL._AC_SX425_.jpg"),
                new Product("B08GKP7YXL", "Segway Ninebot ES1L Electric Kick Scooter", "$299.99", "https://www.amazon.com/Segway-ES1L-Electric-Lightweight-Inner-Support/dp/B08GKP7YXL/?_encoding=UTF8&pf_rd_p=41cfeace-dd87-4c72-a118-1f7af4c58577&pd_rd_wg=i8agC&pf_rd_r=28JBHAR8MKHHEEA6WJTJ&pd_rd_w=KPqdH&pd_rd_r=28a408c0-33d3-4bc4-b126-97bd16f046b5&ref_=pd_gw_unk", "https://m.media-amazon.com/images/I/61EoRZby6RL._AC_SX425_.jpg")));

        try {
            MailSender.sendEmail("bhandare.shreyas@gmail.com", html);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void axessoProductApiTest() {
        Product product1 = ProductUtil.fetchProductFromUrl("B07XLTPC5L", "https://www.amazon.com/Gildan-T-Shirt-Multipack-Charcoal-X-Large/dp/B07XLTPC5L/ref=sr_1_4?keywords=t-shirt&qid=1637367374&sr=8-4");
        Product product2 = ProductUtil.fetchProductFromUrl("B08GKP7YXL", "https://www.amazon.com/Segway-ES1L-Electric-Lightweight-Inner-Support/dp/B08GKP7YXL/?_encoding=UTF8&pf_rd_p=41cfeace-dd87-4c72-a118-1f7af4c58577&pd_rd_wg=i8agC&pf_rd_r=28JBHAR8MKHHEEA6WJTJ&pd_rd_w=KPqdH&pd_rd_r=28a408c0-33d3-4bc4-b126-97bd16f046b5&ref_=pd_gw_unk");
    }
}