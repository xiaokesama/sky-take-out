package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;

    /**
     * 获取营业额统计信息
     * @param begin 开始日期
     * @param end   结束日期
     * @return
     */
    @Override
    public TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = new ArrayList<>();
        List<Double> turnoverList = new ArrayList<>();
        for (LocalDate localDate = begin; !localDate.isAfter(end); localDate = localDate.plusDays(1)) {
            dateList.add(localDate);

            LocalDateTime beginTime = LocalDateTime.of(localDate, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(localDate, LocalTime.MAX);

            Map<String, Object> map = new HashMap<>();
            map.put("beginTime", beginTime);
            map.put("endTime", endTime);
            map.put("status", 5);
            Double turnover = orderMapper.sumByMap(map);
            turnover = turnover == null ? 0.0 : turnover;
            turnoverList.add(turnover);
        }
        return TurnoverReportVO.builder()
                .dateList(dateList.stream().map(LocalDate::toString).collect(Collectors.joining(",")))
                .turnoverList(turnoverList.stream().map(Object::toString).collect(Collectors.joining(",")))
                .build();
    }

    @Override
    public UserReportVO getUserStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = new ArrayList<>();
        List<Integer> totalUserList = new ArrayList<>();
        List<Integer> newUserList = new ArrayList<>();
        for (LocalDate localDate = begin; !localDate.isAfter(end); localDate = localDate.plusDays(1)){
            dateList.add(localDate);
        }
        for(LocalDate localDate:dateList){
            LocalDateTime beginTime = LocalDateTime.of(localDate, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(localDate, LocalTime.MAX);
            Map<String, Object> map = new HashMap<>();
            map.put("endTime", endTime);
            totalUserList.add(userMapper.countByMap(map));

            map.put("beginTime", beginTime);
            newUserList.add(userMapper.countByMap(map));
        }
        return UserReportVO.builder()
                .dateList(dateList.stream().map(LocalDate::toString).collect(Collectors.joining(",")))
                .totalUserList(totalUserList.stream().map(String::valueOf).collect(Collectors.joining(",")))
                .newUserList(newUserList.stream().map(String::valueOf).collect(Collectors.joining(",")))
                .build();
    }

    @Override
    public OrderReportVO getOrdersStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = new ArrayList<>();
        List<Integer> orderCountList = new ArrayList<>();
        ArrayList<Integer> validOrderCountList = new ArrayList<>();
        for (LocalDate localDate = begin; !localDate.isAfter(end); localDate = localDate.plusDays(1)){
            dateList.add(localDate);
        }
        for(LocalDate localDate:dateList){
            LocalDateTime beginTime = LocalDateTime.of(localDate, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(localDate, LocalTime.MAX);

            Integer orderCount = getOrderCount(beginTime, endTime, null);
            orderCountList.add(orderCount);

            Integer validOrderCount = getOrderCount(beginTime, endTime, 5);
            validOrderCountList.add(validOrderCount);
        }

        Integer totalOrderCount = orderCountList.stream().reduce(Integer::sum).get();
        Integer validOrderCount = validOrderCountList.stream().reduce(Integer::sum).get();
        Double orderCompletionRate = totalOrderCount == 0 ? 0.0 : validOrderCount.doubleValue() / totalOrderCount;

        return OrderReportVO.builder()
                .dateList(dateList.stream().map(LocalDate::toString).collect(Collectors.joining(",")))
                .orderCountList(orderCountList.stream().map(String::valueOf).collect(Collectors.joining(",")))
                .validOrderCountList(validOrderCountList.stream().map(String::valueOf).collect(Collectors.joining(",")))
                .totalOrderCount(totalOrderCount)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .build();
    }

    @Override
    public SalesTop10ReportVO getSalesTop10(LocalDate begin, LocalDate end) {
        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);
        List<GoodsSalesDTO> salesTop10 = orderMapper.getSalesTop10(beginTime, endTime);
        return SalesTop10ReportVO.builder()
                .nameList(salesTop10.stream().map(GoodsSalesDTO::getName).collect(Collectors.joining(",")))
                .numberList(salesTop10.stream().map(GoodsSalesDTO::getNumber).map(Object::toString).collect(Collectors.joining(",")))
                .build();
    }

    private Integer getOrderCount(LocalDateTime beginTime, LocalDateTime endTime, Integer status) {
        Map<String, Object> map = new HashMap<>();
        map.put("beginTime", beginTime);
        map.put("endTime", endTime);
        map.put("status", status);
        return orderMapper.countByMap(map);
    }

}
