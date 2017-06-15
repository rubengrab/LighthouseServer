package eu.rubengrab.controllers;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * Created by Ruben on 23.05.2017.
 */
@RestController
@RequestMapping("/image")
public class ImageController {

    @RequestMapping(method = RequestMethod.GET, value = "/{imageName}")
    public void getImage(HttpServletResponse response, @PathVariable("imageName") String imageName) throws Exception {

        ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();

        try {
            BufferedImage img = null;
            try {
                System.out.println(imageName + ".jpg requested.");
                File pathToFile = new File("\\app\\build\\resources\\main\\static\\" + imageName + ".jpg");
                img = ImageIO.read(pathToFile);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            ImageIO.write(img, "jpeg", jpegOutputStream);
        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }

        byte[] imgByte = jpegOutputStream.toByteArray();

        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        ServletOutputStream responseOutputStream = response.getOutputStream();
        responseOutputStream.write(imgByte);
        responseOutputStream.flush();
        responseOutputStream.close();
    }
}
