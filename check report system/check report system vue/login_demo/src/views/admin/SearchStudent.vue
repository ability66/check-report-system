<template>
  <el-card class="box-card">
    <div style="position: relative;height: 100%">

      <div>
        <h3 style="font-size: 40px">查询学生信息</h3>
      </div>
      <!-- 搜索栏 -->
      <el-form ref="form" :model="form" label-width="80px" :inline="true" style="text-align: left">
        <el-form-item label="学号: ">
          <el-input clearable v-model="form.stu_no"></el-input>
        </el-form-item>
        <el-form-item label="专业: ">
          <el-input clearable v-model="form.ter_no"></el-input>
        </el-form-item>
        <el-form-item label="姓名:">
          <el-input clearable v-model="form.stu_name"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="search">搜索</el-button>
          <el-button type="primary" @click="add">添加</el-button>
          <el-button type="primary" @click="reset">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 显示栏 -->
      <el-table :data="tableData" border max-height="500">
        <el-table-column fixed="left" type="selection"> </el-table-column>
        <el-table-column sortable prop="stu_no" label="学号" min-width="40%">
        </el-table-column>
        <el-table-column sortable prop="stu_name" label="姓名" min-width="40%">
        </el-table-column>
        <el-table-column sortable prop="stu_sex" label="性别" min-width="40%">
        </el-table-column>
        <el-table-column sortable prop="ter_no" label="专业" min-width="40%">
        </el-table-column>
        <el-table-column sortable label="入学时间" prop="stu_year" min-width="40%">
        </el-table-column>
        <el-table-column fixed="right" label="操作" min-width="40%">
          <template slot-scope="scope">
            <el-button @click="edit(scope.row)" type="text" size="small">修改</el-button>
            <el-button @click="handleDelete(scope.row)" type="text" size="small">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 新增弹窗 -->
      <el-dialog top="15vh" width="26%" title="添加学生" :visible.sync="zdydialog">
        <el-form :model="addform" label-width="auto">
          <el-form-item label="学号">
            <el-col :span="20" font-size="25">
              <el-input clearable size="small" placeholder="请输入学号" v-model="addform.stu_no"></el-input>
            </el-col>
          </el-form-item>
          <el-form-item label="姓名">
            <el-col :span="20">
              <el-input clearable size="small" placeholder="请输入姓名" v-model="addform.stu_name"></el-input>
            </el-col>
          </el-form-item>
          <el-form-item label="性别">
            <el-col :span="20" font-size="25">
              <el-radio v-model="addform.stu_sex" label="男">男</el-radio>
              <el-radio v-model="addform.stu_sex" label="女">女</el-radio>
            </el-col>
          </el-form-item>
          <el-form-item label="专业号">
            <el-col :span="20">
              <el-input clearable size="small" placeholder="请输入专业号" v-model="addform.ter_no"></el-input>
            </el-col>
          </el-form-item>
          <el-form-item label="入学时间">
            <el-col :span="20">
              <el-input clearable size="small" placeholder="请输入入学时间" v-model="addform.stu_year"></el-input>
            </el-col>
          </el-form-item>
        </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel">取 消</el-button>
        <el-button type="primary" @click="submit">确 定</el-button>
      </div>
      </el-dialog>
    </div>
  </el-card>
</template>


<script>
import axios from 'axios';

export default {
  data() {
    return {
      form: {
        stu_no: '',
        stu_name: '',
        ter_no: '',
      },
      addform: {
        stu_no: '',
        stu_name: '',
        stu_sex: '',
        stu_year: '',
        ter_no: '',
      },
      zdydialog: false,
      all: [],
      tableData: [],
    };
  },
  methods: {
    handleDelete(row) {
      console.log("delete");
      const stu_no = row.stu_no;
      console.log(stu_no)
      // 发送POST请求到服务器端API查询学生信息
      console.log(stu_no);
      axios.post('http://localhost:8181/admin/deleteStudent', stu_no, {
        headers: {
          'Content-Type': 'application/json;charset=UTF-8'
        }
      })
        .then((response) => {
          console.log("删除成功");
          this.getStudents();
        })
        .catch((error) => {
          // 处理错误
          console.error(error);
        });
    },
    getStudents() {
      // 发送GET请求到服务器端API获取所有学生信息
      axios
        .post('http://localhost:8181/admin/getStudents')
        .then((response) => {
          this.all = response.data;
          this.tableData = response.data;
          // response.data.forEach(element => {
          //   console.log(typeof (element))
          //   console.log(element)
          //   this.tableData.push(element);
          // });
          console.log(typeof (this.tableData))
          console.log(this.tableData)
          console.log(typeof (response.data))
          console.log(response.data)
        })
        .catch((error) => {
          // 处理错误
          console.error(error);
        });
    },
    selectStudent(stuNo) {
      const stu_no = stuNo;
      // 发送POST请求到服务器端API查询学生信息
      axios
        .post('http://localhost:8181/admin/selectStudent', stu_no, {
          headers: {
            'Content-Type': 'application/json;charset=UTF-8'
          }
        })
        .then((response) => {
          // 处理响应数据
          this.tableData = [];
          this.tableData[0] = response.data;

          console.log(response.data);
          console.log(this.tableData);
        })
        .catch((error) => {
          // 处理错误
          console.error(error);
        });
    },
    search() {
      console.log("search");
      const stu_no = this.form.stu_no;
      const stu_name = this.form.stu_name;
      const ter_no = this.form.ter_no;
      let result = [];

      if (stu_no) {
        console.log("stu_no");
        result = this.tableData.filter(item => item.stu_no == stu_no);
        console.log(result);
        this.tableData = result;
        return;
      }
      if (stu_name) {
        console.log("stu_name");
        result = this.tableData.filter(item => item.stu_name == stu_name);
        console.log(result);
        this.tableData = result;
        return;
      }
      if (ter_no) {
        console.log("ter_no");
        result = this.tableData.filter(item => item.ter_no == ter_no);
        console.log(result);
        this.tableData = result;
        return;
      }
      console.log("all");
      this.tableData = this.all;

    },
    reset() {
      this.form.stu_no = '';
      this.form.stu_name = '';
      this.form.ter_no = '';
      this.tableData = this.all;
    },
    add() {
      console.log("add");
      this.zdydialog = true;
      this.addform = {
        stu_no: '',
        stu_name: '',
        stu_sex: '',
        stu_year: '',
        ter_no: '',
      }
    },
    edit(row) {
      console.log("edit");
    },
    cancel() {
      this.zdydialog = false;
    },
    submit() {
      console.log("submit");
      axios.post('http://localhost:8181/admin/setStudent', this.addform)
        .then((response) => {
          console.log("添加成功");
          this.zdydialog = false;
          this.getStudents();
        })
        .catch((error) => {
          window.alert("添加失败");
          console.error(error);
        });
    }

  },
  created() {
    this.getStudents();
  },
};

</script>

<style scoped>
.demo-input-wrapper {
  display: flex;
  width: 70%;
}

.demo-input-wrapper span {
  /* border:1px solid rgba(253, 228, 3, 0.979);*/
  font-size: 25px;
  width: 40vw;
  height: 38px;
}
</style>