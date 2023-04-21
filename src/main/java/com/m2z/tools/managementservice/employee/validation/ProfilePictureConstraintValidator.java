package com.m2z.tools.managementservice.employee.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@Slf4j
public class ProfilePictureConstraintValidator implements ConstraintValidator<ProfilePictureConstraint, MultipartFile> {

    private static final long maxSize = 2048;

    private boolean validateSize(byte[] bytes) {
        assert bytes != null;
        if (maxSize < bytes.length) {
            log.debug("Profile picture violates size constraint");
        }
        return true;
    }

    private boolean validateDimensions(BufferedImage image) {
        return image.getWidth() == 500 && image.getHeight() == 500;
    }

    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        try {
            if (value == null ||
                    value.getContentType() == null ||
                    !value.getContentType().equalsIgnoreCase("image/jpeg") ||
                    !validateSize(value.getBytes())
            ) return false;
            BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(value.getBytes()));
            return bufferedImage != null && validateDimensions(bufferedImage);

        } catch (IOException e) {
            log.error("Validating a file threw an exception msg: {}", e.getMessage());
            return false;
        }
    }
}
