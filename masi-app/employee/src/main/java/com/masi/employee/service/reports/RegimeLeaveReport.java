package com.masi.employee.service.reports;

import com.masi.employee.domain.enumeration.LeaveType;

import com.masi.employee.helper.ExcelCell;
import com.masi.employee.helper.SheetTitle;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@SheetTitle("Báo cáo số lượng NV đăng ký nghỉ chế độ")
public class RegimeLeaveReport implements Serializable {

    private LeaveType leaveType;
    @ExcelCell(name = "Số lượng", column = 1)
    private Integer count;
    @ExcelCell(name = "Loại nghỉ phép", column = 0)

    private String leaveTypeDisplay;

}
