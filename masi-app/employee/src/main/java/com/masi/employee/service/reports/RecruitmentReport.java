package com.masi.employee.service.reports;

import com.masi.employee.domain.enumeration.Position;
import com.masi.employee.helper.ExcelCell;
import com.masi.employee.helper.SheetTitle;
import lombok.Data;

import java.io.Serializable;
//Báo cáo này sẽ hiển thị bảng so sánh giữa yêu cầu tuyển dụng và tình hình tuyển dụng hiện tại (VD: tổng số yêu cầu tuyển dụng trên từng vị trí, chuyên ngành so sánh với số lượng đã tuyển và đang được tuyển hiện tại).
@Data
@SheetTitle("Báo cáo tuyển dụng")
public class RecruitmentReport implements Serializable {

    @ExcelCell(name = "Chuyên ngành/vị trí", column = 0)
    private String position;
    @ExcelCell(name = "Tổng số yêu cầu", column = 1)
    private Integer totalRequest;
    @ExcelCell(name = "Tổng số đã tuyển", column = 3)
    private Integer totalRecruited;
    @ExcelCell(name = "Tổng số đang tuyển", column = 2)
    private Integer totalRecruiting;
}
