<template>
  <el-card class="box-card" style="position: relative">
    <div class="container-SearchTeacher" style="text-align: left">
      <el-breadcrumb separator-class="el-icon-arrow-right" class="el-icon">
        <el-breadcrumb-item>首页</el-breadcrumb-item>
        <el-breadcrumb-item>教师查询</el-breadcrumb-item>
        <el-breadcrumb-item>成绩设置</el-breadcrumb-item>
      </el-breadcrumb>
      <!-- 搜索 -->
      <el-form ref="form" :model="form" label-width="80px" :inline="true" style="text-align: left">
        <el-form-item label="学号: ">
          <el-input v-model="form.stu_no"></el-input>
        </el-form-item>
        <el-form-item label="学科号: ">
          <el-input v-model="form.sub_no"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="search">搜索</el-button>
          <el-button type="primary" @click="addScore">添加</el-button>
        </el-form-item>
      </el-form>
    </div>
    <!-- 显示 -->
    <el-table :data="tableData" border size="medium" max-height="500">
      <el-table-column fixed="left" type="selection" > </el-table-column>
      <el-table-column sortable fixed prop="stu_no" label="学号"  min-width="40%"></el-table-column>
      <el-table-column sortable prop="sub_no" label="学科号"  aria-posinset="center" min-width="40%"> </el-table-column>
      <el-table-column sortable prop="grade" label="得分"  min-width="30%"> </el-table-column>
      <el-table-column fixed="right" label="操作"  min-width="40%">
        <template slot-scope="scope">
          <el-button @click="handleEdit(scope.row)" type="text" size="small">修改</el-button>
          <el-button @click="deleteScore(scope.row)" type="text" size="small">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- <div style="margin-top: 20px">
    <el-button @click="toggleSelection([tableData[1], tableData[2]])">切换第二、第三行的选中状态</el-button>
    <el-button @click="toggleSelection()">取消选择</el-button>
    </div> -->
    <!-- 新增弹窗 -->
    <el-dialog style="width: 1000px;height: 1000px;" title="添加学生成绩" :visible.sync="zdydialog">
      <!--:model="form"可能有问题  -->
      <el-form :model="form">
        <el-form-item label="学号" width="80px">
          <el-input class="dialog_input" v-model="addform.stu_no" ></el-input>
        </el-form-item>
        <el-form-item label="学科号" width="80px">
          <el-input class="dialog_input" v-model="addform.sub_no" ></el-input>
        </el-form-item>
        <el-form-item label="成绩" width="80px">
          <el-input class="dialog_input" v-model="addform.grade" ></el-input>
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel">取 消</el-button>
        <el-button type="primary" @click="submit">确 定</el-button>
      </div>
    </el-dialog>

  </el-card>
</template>
<script>
import axios from 'axios';
import Cookies from 'js-cookie';
export default {
  data() {
    return {
      form: {
        stu_no: '',
        sub_no: ''
      },
      tableData: [],// 显示的table数据
      allScores: [], // 存放所有学生成绩数据
      zdydialog: false,
      addform: {
        stu_no: '',
        sub_no: '',
        grade: ''
      }
    };
  },
  mounted() {
    this.getStudentInfo();
  },
  methods: {
    search() {
      console.log("search grade");

      // 根据用户输入的条件在allScores中进行搜索
      const stuNo = this.form.stu_no;
      const subNo = this.form.sub_no;
      let result = [];
      if (stuNo && subNo) {
        // 如果两个条件都有值
        for (let i = 0; i < this.allScores.length; i++) {
          if (this.allScores[i].stu_no == stuNo && this.allScores[i].sub_no == subNo) {
            // console.log(typeof(this.allScores[i]))
            result.push(this.allScores[i]);
          }
        }
        console.log(`No grade found for student ${stuNo} in subject ${subNo}`);
      } else if (stuNo) {
        // 如果只有学生编号有值
        // 在allScores中搜索该学生的所有成绩
        result.push(this.allScores.find(score => score.stu_no == stuNo));
        console.log(`Grades for student ${stuNo}:`, result);
      } else if (subNo) {
        // 如果只有科目编号有值
        // 在allScores中搜索该科目的所有成绩
        result.push(this.allScores.filter(score => score.sub_no == subNo));
        console.log(`Grades for subject ${subNo}:`, result);
      } else {
        getStudentInfo()
      }
      this.tableData = []
      this.tableData = result;
      // this.tableData.push(result);
    },
    getStudentInfo() {
      // 获取所有学生成绩
      const tea_no = Cookies.get('userId');
      axios.post('http://localhost:8181/teacher/getStudent', {
        tea_no: Cookies.get("userId")
      }, {
        headers: {
          "Content-Type": "application/json;charset=UTF-8"
        }
      })
        .then(res => {
          this.tableData = res.data;
          this.allScores = res.data;
          // console.log(res.data);
          // console.log(this.tableData);
        })
        .catch(err => {
          console.error(err);
        });
    },
    addScore() {
      // 添加学生成绩
      console.log("添加学生成绩");
      this.addform = {
        stu_no: '',
        sub_no: '',
        grade: ''
      }
      this.zdydialog = true
    },
    submit() {
      // 提交添加的学生成绩
      axios.post('http://localhost:8181/teacher/setStudentGrade',this.addform,{
        headers: {
          "Content-Type": "application/json;charset=UTF-8"
        }
      }).then(res => {
        console.log("添加学生成绩成功");
        getStudentInfo();
        this.zdydialog = false	//自定义对话框隐藏
      }).catch(err => {
        console.error(err);
      });
    },
    cancel() {
      // 取消添加学生成绩
      this.zdydialog = false	//自定义对话框隐藏
    },
    handleEdit(row) {
      // 修改学生成绩
      console.log("修改学生成绩", row);
    },
    deleteScore(row) {
      // 删除学生成绩
      console.log("删除学生成绩", row);
    },
  }
};
</script>
<style>
.container-SearchTeacher {
  height: 100%;
  background-color: #fff;
}

.el-icon {
  margin-top: 15px;
  margin-bottom: 15px;
  font-size: 15px;
}
</style>