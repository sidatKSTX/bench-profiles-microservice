package com.consultingfirm.benchprofiles.controller;

import com.consultingfirm.benchprofiles.dto.BenchProfilesInfo;
import com.consultingfirm.benchprofiles.exception.UserNotFoundException;
import com.consultingfirm.benchprofiles.model.BenchProfilesEntity;
import com.consultingfirm.benchprofiles.service.BenchProfilesService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/bench-profiles")
public class BenchProfilesController {

    @Value("${bench-profiles-excel-sheet-index}")
    private int benchProfilesSheetIndex;

    private final BenchProfilesService benchProfilesService;

    public BenchProfilesController(BenchProfilesService benchProfilesService) {
        this.benchProfilesService = benchProfilesService;
    }

    @PostMapping(value = "/upload", consumes = {"multipart/form-data"})
    @Operation(summary = "Upload Archive Bench Profiles")
    public ResponseEntity<?> uploadExcel(@RequestParam("file") MultipartFile file) {
        try {
            // Validate file
            if (file.isEmpty() || !Objects.requireNonNull(file.getOriginalFilename()).endsWith(".xlsx")) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Invalid file. Please upload a non-empty Excel file."));
            }

            // Process file
            benchProfilesService.uploadUserDetails(file, benchProfilesSheetIndex);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "Excel data uploaded and inserted into database successfully."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to upload Excel data: " + e.getMessage()));
        }
    }

    @PostMapping()
    @Operation(summary = "Create Bench Profiles")
    public ResponseEntity<BenchProfilesEntity> createBenchProfileInfo(@Valid @RequestBody BenchProfilesInfo benchProfiles) {
        var newBenchProfileInfo = benchProfilesService.createUserInfoDetails(benchProfiles);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newBenchProfileInfo.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Bench profiles User Details")
    public ResponseEntity<String> updateBenchProfileInfo(@PathVariable Long id, @RequestBody BenchProfilesEntity updatedBenchProfiles) {
        benchProfilesService.updateUserDetails(id, updatedBenchProfiles);
        return new ResponseEntity<>("User details updated successfully.", HttpStatus.OK);
    }

    @GetMapping("/paginated")
    @Operation(summary = "Fetch Paginated Bench Profiles with Sorting")
    public ResponseEntity<List<BenchProfilesEntity>> fetchPaginatedBenchProfiles(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "id,asc") String sort) {

        List<BenchProfilesEntity> profiles = benchProfilesService.getPaginatedBenchProfiles(page, size, sort);
        return ResponseEntity.ok(profiles);
    }

    @GetMapping()
    @Operation(summary = "Fetch Bench profiles User Details")
    public ResponseEntity<List<BenchProfilesEntity>> fetchBenchProfileDetails() throws RuntimeException {
        Optional<List<BenchProfilesEntity>> users = benchProfilesService.getUserDetails();

        if (users.isEmpty() || users.get().isEmpty()) {
            throw new UserNotFoundException("No users found.");
        }

        return new ResponseEntity<>(users.get(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Fetch Bench profiles User Details by ID")
    public ResponseEntity<Optional<BenchProfilesEntity>> fetchBenchProfileDetailsByID(@PathVariable Long id) {
        Optional<Optional<BenchProfilesEntity>> users = benchProfilesService.getUserDetailsByID(id);

        return users.map(userDetails -> new ResponseEntity<>(userDetails, HttpStatus.OK))
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    @DeleteMapping()
    @Operation(summary = "Delete All Users")
    public ResponseEntity<HttpStatus> deleteAllUserInfo() {
        benchProfilesService.deleteAllUserInfo();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete User By Id")
    public ResponseEntity<HttpStatus> deleteUserInfoById(@PathVariable("id") long id) {
        benchProfilesService.deleteUserInfoById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
