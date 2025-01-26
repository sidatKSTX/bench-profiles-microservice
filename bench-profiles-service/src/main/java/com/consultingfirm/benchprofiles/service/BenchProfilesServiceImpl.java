package com.consultingfirm.benchprofiles.service;

import com.consultingfirm.benchprofiles.dto.BenchProfilesInfo;
import com.consultingfirm.benchprofiles.exception.UserNotFoundException;
import com.consultingfirm.benchprofiles.external.ExcelServiceProxy;
import com.consultingfirm.benchprofiles.model.BenchProfilesEntity;
import com.consultingfirm.benchprofiles.repository.BenchProfilesRepository;
import feign.FeignException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class BenchProfilesServiceImpl implements BenchProfilesService {

    private final ExcelServiceProxy excelServiceProxy;
    private final BenchProfilesRepository repository;

    public BenchProfilesServiceImpl(ExcelServiceProxy excelServiceProxy,
                                    BenchProfilesRepository repository) {
        this.excelServiceProxy = excelServiceProxy;
        this.repository = repository;
    }

    @Override
    public void uploadUserDetails(MultipartFile file, int sheetIndex) {
        if (!isExcelFile(file)) {
            System.out.println("Please upload an Excel file");
        }

        try {
            List<BenchProfilesEntity> benchProfilesEntities = excelServiceProxy.processExcel(file, sheetIndex);
            repository.saveAll(benchProfilesEntities);
        } catch (FeignException e) {
            System.out.println("Error sending file to processor service: " + e);
            throw new RuntimeException("Failed to send file to processor service", e);
        }
    }

    @Override
    public BenchProfilesEntity createUserInfoDetails(BenchProfilesInfo benchProfiles) {
        var benchProfilesInfo = BenchProfilesEntity.builder()
                .recruiterName(benchProfiles.recruiterName())
                .consultantName(benchProfiles.consultantName())
                .allocatedStatus(benchProfiles.allocatedStatus())
                .status(benchProfiles.status())
                .turboCheck(benchProfiles.turboCheck())
                .priority(benchProfiles.priority())
                .technology(benchProfiles.technology())
                .organization(benchProfiles.organization())
                .experience(benchProfiles.experience())
                .location(benchProfiles.location())
                .relocation(benchProfiles.relocation())
                .modeOfStaying(benchProfiles.modeOfStaying())
                .newOrExisting(benchProfiles.newOrExisting())
                .sourcedBy(benchProfiles.sourcedBy())
                .visaStatus(benchProfiles.visaStatus())
                .marketingVisaStatus(benchProfiles.marketingVisaStatus())
                .contactNumber(benchProfiles.contactNumber())
                .emailId(benchProfiles.emailId())
                .originalDob(benchProfiles.originalDob())
                .marketingDob(benchProfiles.marketingDob())
                .whatsappNumber(benchProfiles.whatsappNumber())
                .marketingStartDate(benchProfiles.marketingStartDate())
                .marketingEndDate(benchProfiles.marketingEndDate())
                .comments(benchProfiles.comments())
                .build();

        return repository.save(benchProfilesInfo);
    }

    @Override
    public void updateUserDetails(Long id, BenchProfilesEntity benchProfilesInfo) {
        var existingUser = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        // Update fields
        existingUser.setRecruiterName(benchProfilesInfo.getRecruiterName());
        existingUser.setConsultantName(benchProfilesInfo.getConsultantName());
        existingUser.setAllocatedStatus(benchProfilesInfo.getAllocatedStatus());
        existingUser.setStatus(benchProfilesInfo.getStatus());
        existingUser.setTurboCheck(benchProfilesInfo.getTurboCheck());
        existingUser.setPriority(benchProfilesInfo.getPriority());
        existingUser.setTechnology(benchProfilesInfo.getTechnology());
        existingUser.setOrganization(benchProfilesInfo.getOrganization());
        existingUser.setExperience(benchProfilesInfo.getExperience());
        existingUser.setLocation(benchProfilesInfo.getLocation());
        existingUser.setRelocation(benchProfilesInfo.getRelocation());
        existingUser.setModeOfStaying(benchProfilesInfo.getModeOfStaying());
        existingUser.setNewOrExisting(benchProfilesInfo.getNewOrExisting());
        existingUser.setSourcedBy(benchProfilesInfo.getSourcedBy());
        existingUser.setVisaStatus(benchProfilesInfo.getVisaStatus());
        existingUser.setMarketingVisaStatus(benchProfilesInfo.getMarketingVisaStatus());
        existingUser.setContactNumber(benchProfilesInfo.getContactNumber());
        existingUser.setEmailId(benchProfilesInfo.getEmailId());
        existingUser.setOriginalDob(benchProfilesInfo.getOriginalDob());
        existingUser.setMarketingDob(benchProfilesInfo.getMarketingDob());
        existingUser.setWhatsappNumber(benchProfilesInfo.getWhatsappNumber());
        existingUser.setMarketingStartDate(benchProfilesInfo.getMarketingStartDate());
        existingUser.setMarketingEndDate(benchProfilesInfo.getMarketingEndDate());
        existingUser.setComments(benchProfilesInfo.getComments());

        repository.save(existingUser);
    }

    public List<BenchProfilesEntity> getPaginatedBenchProfiles(int page, int size, String sort) {
        String[] sortParams = sort.split(",");
        Sort.Direction direction = Sort.Direction.fromString(sortParams[1]);
        String sortBy = sortParams[0];

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<BenchProfilesEntity> resultPage = repository.findAll(pageable);

        return resultPage.getContent();
    }

    @Override
    public Optional<List<BenchProfilesEntity>> getUserDetails() {
        return Optional.of(repository.findAll());
    }

    @Override
    public Optional<Optional<BenchProfilesEntity>> getUserDetailsByID(Long id) {
        return Optional.of(repository.findById(id));
    }

    @Override
    public void deleteAllUserInfo() {
        repository.deleteAll();
    }

    @Override
    public void deleteUserInfoById(long id) {
        repository.deleteById(id);
    }

    private boolean isExcelFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && (
                contentType.equals("application/vnd.ms-excel") ||
                        contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
        );
    }
}
