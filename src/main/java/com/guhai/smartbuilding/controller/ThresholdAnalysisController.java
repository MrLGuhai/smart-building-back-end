package com.guhai.smartbuilding.controller;

import com.guhai.smartbuilding.common.ApiResponse;
import com.guhai.smartbuilding.entity.ThresholdAnalysis;
import com.guhai.smartbuilding.service.ThresholdAnalysisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/thresholds/analysis")
public class ThresholdAnalysisController {
    @Autowired
    private ThresholdAnalysisService thresholdAnalysisService;
    
    @GetMapping
    public ApiResponse analyzeThresholds(
            @RequestParam(required = false) Integer days,
            @RequestParam(required = false) Integer thresholdType) {
        try {
            List<ThresholdAnalysis> analysisResults = thresholdAnalysisService.analyzeThresholds(days, thresholdType);
            return ApiResponse.success("分析成功", analysisResults);
        } catch (Exception e) {
            log.error("阈值分析失败", e);
            return ApiResponse.error(500, "阈值分析失败：" + e.getMessage());
        }
    }
} 