package com.example.myapp.controller;

import com.example.myapp.model.MyModel;
import com.example.myapp.service.MyService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class MyController {

    private final MyService myService;

    public MyController(MyService myService) {
        this.myService = myService;
    }

    @GetMapping("/models")
    public Flux<MyModel> getAllModels() {
        return myService.getAllModels();
    }

    private static final Logger logger = LoggerFactory.getLogger(MyController.class);

    // Existing code...

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<ResponseEntity<Void>> uploadFiles(@RequestPart("files") Flux<MultipartFile> files) {
        return files
                .flatMap(file -> {
                    // Process each file here
                    // Example: Save the file to a storage location
                    // file.transferTo(new File("path/to/save/" + file.getOriginalFilename()));

                    // Log the file name
                    logger.info("Processing file: {}", file.getOriginalFilename());
                    try {
                        file.transferTo(new File("path/to/save/" + file.getOriginalFilename()));
                        return Mono.just(ResponseEntity.ok().build());
                    } catch (IOException e) {
                        logger.error("Error processing file: {}", file.getOriginalFilename(), e);
                        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                    }
                })
                .then();
    }

}
