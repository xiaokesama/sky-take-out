package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@Slf4j
@RequestMapping("/admin/report")
@Api(tags = "报表管理")
public class ReportController {

    @Autowired
    private ReportService reportService;


    @RequestMapping("/turnoverStatistics")
    public Result<TurnoverReportVO> turnoverStatistics(
            @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam(value = "begin")
            LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam(value = "end")
            LocalDate end
    ) {
        // TODO: 统计营业额报表
        log.info("营业额统计数据：{}-{}", begin, end);
        return Result.success(reportService.getTurnoverStatistics(begin, end));
    }
}
