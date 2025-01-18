package com.consultingfirm.benchprofiles.service;

import com.consultingfirm.benchprofiles.dto.BenchProfilesInfo;
import com.consultingfirm.benchprofiles.model.BenchProfilesEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface BenchProfilesService {

    void uploadUserDetails(MultipartFile file, int sheetIndex) throws Exception;

    BenchProfilesEntity createUserInfoDetails(BenchProfilesInfo benchProfilesInfo);

    void updateUserDetails(Long id, BenchProfilesEntity benchProfiles);

    Optional<List<BenchProfilesEntity>> getUserDetails();

    Optional<Optional<BenchProfilesEntity>> getUserDetailsByID(Long id);

    void deleteAllUserInfo();

    void deleteUserInfoById(long id);

}
