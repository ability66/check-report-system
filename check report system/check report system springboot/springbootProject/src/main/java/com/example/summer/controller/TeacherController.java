package com.example.summer.controller;

import com.example.summer.SpringbootProjectApplication;
import com.example.summer.cosSimilar.CosineSimilarAlgorithm;
import com.example.summer.cosSimilar.DataVO;
import com.example.summer.entity.Grade;
import com.example.summer.entity.Report;
import com.example.summer.entity.Student;
import com.example.summer.entity.Teacher;
import com.example.summer.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/teacher")
@Slf4j
@CrossOrigin("*")
public class TeacherController {
    @Autowired
    StudentService studentService;
    @Autowired
    ClassTableService classTableService;
    @Autowired
    ReportService reportService;
    @Autowired
    GradeService gradeService;
    @Autowired
    TeacherService teacherService;
    @Autowired
    SubjectService subjectService;

    @RequestMapping(value = "/getMessage", method = RequestMethod.POST)
    public Teacher getMessage(@RequestBody int tea_no) {
        return teacherService.selectByTea_no(tea_no);
    }
    @RequestMapping(value = "/getReport", method = RequestMethod.POST)
    public List<Report> getReport(@RequestBody int tea_no) {
        System.out.println("getReport");
        List<Report>reports=reportService.selectByTea_no(tea_no);
        for (Report report:reports) {
            System.out.println(report);
        }
        return reports;
    }

    @RequestMapping(value = "/getReportByStu", method = RequestMethod.POST)
    public Report getReportByStu(@RequestBody Report report) {
        int stu_no=report.getStu_no();
        int tea_no=report.getTea_no();
        return reportService.getReportByStu_noAndTea_no(stu_no,tea_no);
    }
    @RequestMapping(value = "/checkReport", method = RequestMethod.POST)
    public List<DataVO> checkReport(@RequestBody Report reports) {
        int stu_no=reports.getStu_no();
        int tea_no=reports.getTea_no();
        System.out.println("checkReport"+reports);
        // 根据学生号和教师工号查询文件存储位置
        String filePath = reportService.selctByStu_noAndTea_no(stu_no, tea_no);

        // 获取数据库中存储的所有文件地址
        List<Report> allFilePaths = reportService.getAllFileLocations();

        // 存储结果
        List<DataVO> results = new ArrayList<>();

        // 遍历所有文件地址
        for (Report report : allFilePaths) {
            // 计算相似度
            double similarity = CosineSimilarAlgorithm.cosSimilarityByString(filePath, report.getRe_location(), new ArrayList<>());

            // 创建并添加结果
            DataVO dataVO = new DataVO();
            dataVO.setFileName(new File(report.getRe_location()).getName());
            dataVO.setStuNO(report.getStu_no() + "");
            dataVO.setResult(similarity);
            results.add(dataVO);
        }

        return results;
    }

    //返回所有学生的成绩
    @RequestMapping(value = "/getStudent", method = RequestMethod.POST)
    public List<Grade> getGrade(@RequestBody Report report) {
        int tea_no=report.getTea_no();
        List<Integer> sub_nos = subjectService.selectSub_noByTea_no(tea_no);
        Set<Integer> set = new HashSet<Integer>();
        for (int i = 0; i < sub_nos.size(); i++) {
            List<Integer> tmp = classTableService.selectClass_noBySub_no(sub_nos.get(i));
            set.addAll(tmp);
        }
//        System.out.println("Class_no");
//        for(Integer integer:set) {
//            System.out.println(integer);
//        }

        List<Integer> stu_nos = new ArrayList<Integer>();
        for (Integer integer:set) {
            List<Integer> tmp = studentService.selectStu_noByClass_no(integer);
            stu_nos.addAll(tmp);
        }
//        System.out.println("===================\nStu_no");
//        for(Integer integer:stu_nos) {
//            System.out.println(integer);
//        }
        List<Grade> res = new ArrayList<Grade>();
        for (int i = 0; i < stu_nos.size(); i++) {
            if (gradeService.selectByStu_no(stu_nos.get(i)) != null) {
                res.addAll(gradeService.selectByStu_no(stu_nos.get(i)));
            }
        }
//        int i = 0;
//        for (Grade grade:res) {
//            i++;
//            System.out.println(grade.toString());
//        }
//        System.out.println("共"+i+"条数据");
        return res;
    }

    @RequestMapping(value = "/setStudentGrade", method = RequestMethod.POST)
    public void setGrade(@RequestBody Grade grade) {
        gradeService.insertGrade(grade);
    }

//    @RequestMapping(value = "/getStudentGrades")
//    public List<Grade> getGrade(int tea_no,int sub_no) {
//
//    }

}
