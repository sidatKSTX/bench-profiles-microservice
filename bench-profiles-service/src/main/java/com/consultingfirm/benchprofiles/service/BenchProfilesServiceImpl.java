package com.consultingfirm.benchprofiles.service;

import com.consultingfirm.benchprofiles.dto.BenchProfilesInfo;
import com.consultingfirm.benchprofiles.external.ExcelServiceProxy;
import com.consultingfirm.benchprofiles.model.BenchProfilesEntity;
import com.consultingfirm.benchprofiles.repository.BenchProfilesRepository;
import feign.FeignException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class BenchProfilesServiceImpl implements BenchProfilesService {

    private final ExcelServiceProxy excelServiceProxy;

    private final BenchProfilesRepository benchProfilesRepository;

    public BenchProfilesServiceImpl(ExcelServiceProxy excelServiceProxy,
                                    BenchProfilesRepository benchProfilesRepository) {
        this.excelServiceProxy = excelServiceProxy;
        this.benchProfilesRepository = benchProfilesRepository;
    }

    @Override
    public void uploadUserDetails(MultipartFile file, int sheetIndex) {
        if (!isExcelFile(file)) {
            System.out.println("Please upload an Excel file");
        }

        try {
            List<BenchProfilesEntity> benchProfilesEntities = excelServiceProxy.processExcel(file, sheetIndex);
            benchProfilesEntities.forEach(info -> System.out.println(info.getRecruiterName()));
            benchProfilesRepository.saveAll(benchProfilesEntities);
        } catch (FeignException e) {
            System.out.println("Error sending file to processor service: " + e);
            throw new RuntimeException("Failed to send file to processor service", e);
        }
    }

    @Override
    public BenchProfilesEntity createUserInfoDetails(BenchProfilesInfo benchProfilesInfo) {
        return null;
    }

    @Override
    public void updateUserDetails(Long id, BenchProfilesEntity benchProfiles) {

    }

    @Override
    public Optional<List<BenchProfilesEntity>> getUserDetails() {
        return Optional.empty();
    }

    @Override
    public Optional<Optional<BenchProfilesEntity>> getUserDetailsByID(Long id) {
        return Optional.empty();
    }

    @Override
    public void deleteAllUserInfo() {

    }

    @Override
    public void deleteUserInfoById(long id) {

    }

    private boolean isExcelFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && (
                contentType.equals("application/vnd.ms-excel") ||
                        contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
        );
    }
}
