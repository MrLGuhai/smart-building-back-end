package com.guhai.smartbuilding.controller;

import com.guhai.smartbuilding.common.ApiResponse;
import com.guhai.smartbuilding.entity.Thresholds;
import com.guhai.smartbuilding.service.ThresholdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/thresholds")
public class ThresholdController {
    @Autowired
    private ThresholdService thresholdService;

    @GetMapping("/current")
    public ApiResponse getCurrentThresholds() {
        Thresholds thresholds = thresholdService.getCurrentThresholds();
        return ApiResponse.success("获取成功", thresholds);
    }

    @PostMapping("/update")
    public ApiResponse updateThresholds(@RequestBody Thresholds thresholds) {
        if (thresholdService.updateThresholds(thresholds)) {
            return ApiResponse.success("更新成功", null);
        }
        return ApiResponse.error(500, "更新失败");
    }
} 