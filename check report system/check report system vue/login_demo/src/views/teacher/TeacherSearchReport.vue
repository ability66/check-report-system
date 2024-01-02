<template>
  <div class="box-card" style="height: 80vh">
    <div style="text-align: left">
      <el-breadcrumb separator-class="el-icon-arrow-right" class="el-icon">
        <el-breadcrumb-item>首页</el-breadcrumb-item>
        <el-breadcrumb-item>教师查询</el-breadcrumb-item>
        <el-breadcrumb-item>查询报告</el-breadcrumb-item>
      </el-breadcrumb>

      <el-form ref="form" :model="form" label-width="80px" :inline="true" style="text-align: left">
        <el-form-item label="学号: ">
          <el-input style="width: 250px" v-model="stu_no"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="search">搜索</el-button>
        </el-form-item>
      </el-form>
    </div>

    <el-table ref="singleTable" :data="tableData" highlight-current-row @current-change="handleCurrentChange" 
      height="50vh" max-width="70%">
      <el-table-column align="center" type="index" width="50">
      </el-table-column>
      <el-table-column align="center" property="stu_no" label="学生学号" width="270">
      </el-table-column>
      <el-table-column align="center" property="tea_no" label="教师工号" width="270">
      </el-table-column>
      <el-table-column align="center" property="re_location" label="文件地址" width="270">
      </el-table-column>
      <el-table-column align="center" property="upload_date" label="上传时间">
      </el-table-column>
    </el-table>
    <div style="margin-top: 20px;"></div>
      <el-button @click="setCurrent()" style="margin-right: 50vw">取消选择</el-button>
    <div>
    <h2>查重结果</h2>
    <ul>
      <li v-for="(item, index) in resultData" :key="index">
        <p>文件名：{{ item.fileName }}</p>
        <p>学号：{{ item.stuNO }}</p>
        <p>查重结果：{{ item.result }}</p>
      </li>
    </ul>
  </div>
</div>
</template>
<script>
import axios from 'axios';
import Cookies from 'js-cookie';

export default {
  name: "SearchTeacher",
  data() {
    return {
      data:[],//保持总数据:教师号对应的所有report
      form:[],
      stu_no: '',
      tableData: [],//显示的table数据
      currentRow: null,
      resultData: [],//查重结果及报告参数
      requestData: [],//封装查重需要的参数
      target:[],
    }
  },
  methods: {
    //根据stu_no查询报告
    search() {
      //console.log(this.stu_no)
      if (this.stu_no) {
        // 如果输入了学号，则调用selectStudent方法查询学生信息
        this.getReportByStu(this.stu_no);
      } else {
        // 否则调用getStudents方法获取所有学生信息
        this.getReport();
      }

    },
    getReportByStu(stu_no) {
      console.log("getReportByStu")
      this.tableData = []
      this.data.forEach(element => {
        console.log(this.data)
        console.log(element)
        if (element.stu_no == stu_no) {
          console.log("if语句")
          console.log(element)
          this.tableData.push(element)
        }
      });
      console.log(this.tableData)
    },

    // getReportByStu(stu_no) {
    //   console.log("getReportByStu")
    //   Cookies.get("userId")
    //   axios
    //     .post("http://localhost:8181/teacher/getReportByStu", {
    //       stu_no: stu_no,
    //       tea_no: Cookies.get("userId")
    //     }, {
    //       headers: {
    //         "Content-Type": "application/json;charset=UTF-8"
    //       }
    //     })
    //     .then(res => {
    //       this.tableData = res.data;

    //     })
    //     .catch(err => {
    //       console.log(err);
    //     });
    // },

    getReport() {
      console.log("getReport");
      axios
        .post("http://localhost:8181/teacher/getReport",
          Cookies.get("userId")
          ,
          {
            headers: {
              "Content-Type": "application/json;charset=UTF-8"
            }
          }
        )
        .then(res => {
          this.data = res.data
          this.tableData = res.data;
          console.log(this.tableData)
        })
        .catch(err => {
          console.log(err);
        });
    },

    setCurrent(row) {
      this.$refs.singleTable.setCurrentRow(row);
    },
    handleCurrentChange(val) {//该函数进行选择后的操作
      this.currentRow = val;
      //val传来了当前选择的行的数据
      // console.log("handleCurrentChange");
      console.log(this.currentRow);
      if (val != null) {
        this.$confirm('对该报告进行查重, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'info'
        }).then(() => {
          const requestData = {
            stu_no: this.currentRow.stu_no,
            tea_no: Cookies.get("userId")
          };
          // console.log(requestData); 
          axios
            .post("http://localhost:8181/teacher/checkReport",requestData, {
              headers: {
                "Content-Type": "application/json;charset=UTF-8"
              }
            })
            .then(res => {
              console.log("点击确定");
              //res.data;//res.data获取到的是所有报告的查重结果
              this.resultData = [];
              for (let i = 0; i < res.data.length; i++) {
                if(requestData.stu_no == res.data[i].stuNO)
                  this.resultData.push(res.data[i]);
              }
              console.log(this.resultData);
              // console.log("this.result: "+this.result);
            })
            .catch(err => {
              console.log(err);
            });
        }).catch(() => {

        });
      }
    },
  },
  created() {
    // 组件创建后调用getStudents方法获取所有学生信息
    this.getReport();
  },
};
</script>
<style>
.el-icon {
  margin-top: 15px;
  margin-bottom: 15px;
  font-size: 15px;
}
</style>