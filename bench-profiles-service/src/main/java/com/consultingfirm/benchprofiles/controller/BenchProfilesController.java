package com.consultingfirm.benchprofiles.controller;

import com.consultingfirm.benchprofiles.service.BenchProfilesService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/user")
public class BenchProfilesController {

    private final BenchProfilesService benchProfilesService;
    @Value("${bench-profiles-excel-sheet-index}")
    private int benchProfilesSheetIndex;

    public BenchProfilesController(BenchProfilesService benchProfilesService) {
        this.benchProfilesService = benchProfilesService;
    }

    @PostMapping(value = "/excel/upload", consumes = {"multipart/form-data"})
    public ResponseEntity<String> uploadExcel(@RequestParam("file") MultipartFile file) throws Exception {
        benchProfilesService.uploadUserDetails(file, benchProfilesSheetIndex);
        return new ResponseEntity<>("Excel data uploaded and inserted into database successfully.", HttpStatus.CREATED);
    }
}
