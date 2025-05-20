package org.example.excel_edit.controller;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.excel_edit.service.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Controller
public class FileController {
    @Autowired
    private ExcelService excelService;

    @GetMapping("/")
    public String view() {
        return "view/index";
    }

    @PostMapping(value = "/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile multipartFile,
                                             @RequestParam("checkId") boolean checkId ) {
        try {
            InputStream inputStream = multipartFile.getInputStream();
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            List<Row> rows = new ArrayList<>();
            List<Row> dataRows = new ArrayList<>();
            List<Row> dataRowsChecked = new ArrayList<>();
            // 저장할 파일 경로
            String filePath = "C:\\excel\\test\\";

            // 정렬된 내용을 새로운 시트에 작성
            Workbook newWorkbook = new XSSFWorkbook();
            Sheet newSheet = newWorkbook.createSheet();

            //거꾸로 정렬하여 rows 리턴
            if(checkId) {
                dataRowsChecked = excelService.reverseRowChecked(sheet, rows);
                //row 별로 cell값 입력후 sheet return
                newSheet = excelService.setCellValues(newSheet, dataRowsChecked);
            }else {
                dataRows = excelService.reverseRow(sheet, rows);
                //row 별로 cell값 입력후 sheet return
                newSheet = excelService.setCellValues(newSheet, dataRows);
            }

            //헤더 생성
            excelService.setHeaderValues(newSheet, rows);

            // 파일 저장
            return excelService.saveFile(filePath, newWorkbook);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
