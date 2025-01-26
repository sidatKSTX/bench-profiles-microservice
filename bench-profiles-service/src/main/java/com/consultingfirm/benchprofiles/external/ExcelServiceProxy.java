package com.consultingfirm.benchprofiles.external;

import com.consultingfirm.benchprofiles.model.BenchProfilesEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@FeignClient(name = "common-excel-service", url = "${excel.processor.url}")
public interface ExcelServiceProxy {

    @PostMapping(value = "/api/process/benchprofiles", consumes = {"multipart/form-data"})
    List<BenchProfilesEntity> processExcel(@RequestPart("file") MultipartFile file,
                                           @RequestParam("sheetIndex") int sheetIndex);
}
