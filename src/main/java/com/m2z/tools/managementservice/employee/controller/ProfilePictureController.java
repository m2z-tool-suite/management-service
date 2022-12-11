package com.m2z.tools.managementservice.employee.controller;

import com.m2z.tools.managementservice.employee.service.ProfilePictureStorage;
import com.m2z.tools.managementservice.employee.validation.EmployeeExists;
import com.m2z.tools.managementservice.generic.GenericResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Optional;


@Slf4j
@Validated
@Controller
@ResponseBody
@RequestMapping("/api/v1/employee/{userId}/profile/image")
@RequiredArgsConstructor
public class ProfilePictureController {

    private final ProfilePictureStorage profilePictureStorage;

    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<GenericResponseDTO> uploadProfilePicture(@RequestParam("image") MultipartFile file,
                                                                   @EmployeeExists @PathVariable String userId) throws IOException, URISyntaxException {

        profilePictureStorage.put(userId, file.getBytes());

        Optional<URL> url = profilePictureStorage.generateUrl(userId);
        if (url.isEmpty()) throw new RuntimeException("URL should not be empty after upload");

        return ResponseEntity.created(url.get().toURI()).body(GenericResponseDTO.created());
    }

    @DeleteMapping
    public GenericResponseDTO deleteProfilePicture(@PathVariable String userId) {
        profilePictureStorage.delete(userId);
        return GenericResponseDTO.ok();
    }

    @GetMapping
    // Does not check database this is intentional
    public Object generateUrl(@PathVariable String userId) {
        Optional<URL> url = profilePictureStorage.generateUrl(userId);
        if (url.isPresent()) {
            HashMap<String, String> response = new HashMap<>();
            response.put("profilePictureUrl", url.get().toString());
            return response;
        }

        return GenericResponseDTO.notFound();
    }
}
