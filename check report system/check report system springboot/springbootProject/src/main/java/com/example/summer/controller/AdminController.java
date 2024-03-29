package com.example.summer.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.summer.entity.*;
import com.example.summer.service.*;
import com.example.summer.utils.MD5Util;
import com.fasterxml.jackson.databind.util.JSONPObject;
import jdk.internal.access.JavaNetHttpCookieAccess;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.xml.crypto.Data;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@Slf4j
@CrossOrigin("*")
public class AdminController {

    @Autowired
    StudentService studentService;
    @Autowired
    ClassTableService classTableService;
    @Autowired
    TeacherService teacherService;
    @Autowired
    UserService userService;
    @Autowired
    SubjectService subjectService;

    @RequestMapping(value = "/setTeacher", method = RequestMethod.POST)
    public void setTeacher(@RequestBody Teacher teacher) {
        teacherService.insertTeacher(teacher);
        User user = new User();
        user.setUsername(String.valueOf(teacher.getTea_no()));
        user.setPassword(MD5Util.MD5EncodeUtf8(String.valueOf(teacher.getTea_no())));
        user.setTea_no(teacher.getTea_no());
        user.setPower("teacher");
        userService.insertUser(user);
    }

    @RequestMapping(value = "/getTeachers", method = RequestMethod.POST)
    public List<Teacher> getTeachers() {
        return teacherService.selectAll();
    }

    @RequestMapping(value = "/setStudent", method = RequestMethod.POST)
    public void setStudent(@RequestBody Student student) {
        studentService.insertStudent(student);
        User user = new User();
        user.setUsername(String.valueOf(student.getStu_no()));
        user.setPassword(MD5Util.MD5EncodeUtf8(String.valueOf(student.getStu_no())));
        user.setStu_no(student.getStu_no());
        user.setPower("student");
        userService.insertUser(user);
    }

    @RequestMapping(value = "/selectStudent", method = RequestMethod.POST)
    public Student selectStudent(@RequestBody int stu_no) {
        return studentService.selectByStu_no(stu_no);
    }

    @RequestMapping(value = "/getStudents", method = RequestMethod.POST)
    public List<Student> getStudents() {
        return studentService.selectAll();
    }

    @RequestMapping(value = "/selectTeacher", method = RequestMethod.POST)
    public Teacher selectTeacher(@RequestBody int tea_no) {
        return teacherService.selectByTea_no(tea_no);
    }

//    @RequestParam("classTable") ClassTable classTable,@RequestParam("sub_name") String sub_name,@RequestParam("tea_no") int tea_no
    @RequestMapping(value = "/setClasstable", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public void setClassTable(@RequestBody String s) {
        System.out.println(s);
        JSONObject obj = JSON.parseObject(s);
        JSONObject classTableJson = obj.getJSONObject("classTable");
        ClassTable classTable = JSON.parseObject(classTableJson.toJSONString(),ClassTable.class);
        String sub_name = obj.getString("sub_name");
        int tea_no = obj.getIntValue("tea_no");

        System.out.println(classTable);
        System.out.println(sub_name);
        System.out.println(tea_no);

        classTable.setClass_no(subjectService.selectSub_noByPK(sub_name,tea_no));
        classTableService.insertClassTable(classTable);
    }

    @RequestMapping(value = "/addClass", method = RequestMethod.POST)
    public void addClass(@RequestBody int stu_no,@RequestBody int class_no) {
        studentService.insertStudentClass(stu_no,class_no);
    }

    @RequestMapping(value = "/deleteStudent", method = RequestMethod.POST)
    public void deleteStudent(@RequestBody int stu_no) {
        studentService.deleteStudentByStu_no(stu_no);
    }
}
