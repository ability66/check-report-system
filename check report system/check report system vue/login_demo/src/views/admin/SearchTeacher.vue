<template>
  <el-card class="box-card" >
    <div style="position: relative;height: 100%">

      <div>
        <h3 style="font-size: 40px">教师信息</h3>
      </div>
      <!-- 搜索栏 -->
      <el-form  ref="form" :model="form" label-width="80px" :inline="true" style="text-align: left">
        <el-form-item label="工号: ">
          <el-input clearable v-model="form.tea_no"></el-input>
        </el-form-item>
        <el-form-item label="学院号: ">
          <el-input clearable v-model="form.tea_col_no"></el-input>
        </el-form-item>
        <el-form-item label="教师名:">
          <el-input clearable v-model="form.tea_name"></el-input>
        </el-form-item>
        <el-form-item >
          <el-button type="primary" @click="search">搜索</el-button>
          <el-button type="primary" @click="add">添加</el-button>
          <el-button type="primary" @click="reset">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 显示栏 -->
      <el-table :data="tableData" border max-height="500" size="medium">
        <el-table-column fixed="left" type="selection" > </el-table-column>
        <el-table-column sortable prop="tea_no" label="工号" min-width="40%">
        </el-table-column>
        <el-table-column sortable prop="tea_name" label="姓名" min-width="40%">
        </el-table-column>
        <el-table-column sortable prop="tea_level" label="职位" min-width="40%">
        </el-table-column>
        <el-table-column sortable prop="tea_col_no" label="学院号" min-width="40%">
        </el-table-column>
        <el-table-column fixed="right" label="操作"  min-width="40%">
        <template slot-scope="scope">
          <el-button @click="edit(scope.row)" type="text" size="small">修改</el-button>
          <el-button @click="delete_(scope.row)" type="text" size="small">删除</el-button>
        </template>
      </el-table-column>
      </el-table>

      <!-- 新增弹窗 -->
      <el-dialog top="15vh"  width="26%" title="添加教师" :visible.sync="zdydialog" >
      <el-form :model="addform" label-width="auto">
        <el-form-item  label="教师号" >
          <el-col :span="20" font-size="25">
            <el-input   clearable size="small"  placeholder="请输入教师号"  v-model="addform.tea_no" ></el-input>
          </el-col>
        </el-form-item>
        <el-form-item  label="教师名" >
          <el-col :span="20">
            <el-input  clearable size="small"  placeholder="请输入教师名" v-model="addform.tea_name" ></el-input>
          </el-col>
        </el-form-item>
        <el-form-item  label="学院号" >
          <el-col :span="20">
            <el-input  clearable size="small"  placeholder="请输入学院号" v-model="addform.tea_col_no" ></el-input>
          </el-col>
        </el-form-item>
        <el-form-item  label="职位" >
          <el-col :span="20">
            <el-input  clearable size="small"  placeholder="请输入职位" v-model="addform.tea_level" ></el-input>
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
        tea_no: '',
        tea_name: '',
        tea_col_no: '',
      },//搜索栏
      addform: {
        tea_no: '',
        tea_name: '',
        tea_col_no: '',
        tea_level: '',
      },//添加栏
      zdydialog: false,//添加弹窗
      all:[],//所有数据
      tableData: [],//显示出的数据
    };
  },
  methods: {
    getTeachers() {
      axios
        .post('http://localhost:8181/admin/getTeachers')
        .then((response) => {
          console.log("查询教师信息成功");
          console.log(response.data);
          this.all = response.data;
          this.tableData = response.data;
        })
        .catch((error) => {
          // 处理错误
          console.error(error);
        });
    },
    selectTeacher(teaNo) {
      console.log("selectTeacher");
      const tea_no = teaNo;

      axios
        .post('http://localhost:8181/admin/selectTeacher', tea_no, {
          headers: {
            'Content-Type': 'application/json;charset=UTF-8'
          }
        })
        .then((response) => {

          this.tableData = [];
          this.tableData[0] = response.data;
          console.log("查询教师信息成功");
          console.log(response.data);
          console.log(this.tableData);
        })
        .catch((error) => {

          console.error(error);
        });
    },
    search() {
      console.log("search");
      const tea_no = this.form.tea_no;
      const tea_name = this.form.tea_name;
      const tea_col_no = this.form.tea_col_no;
      console.log(tea_col_no)
      let result = [];
      // console.log(this.all);
      // console.log(tea_no);
      // console.log(this.all.filter(item => item.tea_no == tea_no));
      // var output = this.all.filter(item => item.tea_no == tea_no);
      // var output = this.all.find(item=>item.tea_no == tea_no) //返回了一个符合条件的对象
      // console.log(typeof (output))
      // console.log(output)
      if (tea_no) {
        console.log("tea_no");
        result = this.tableData.filter(item => item.tea_no == tea_no);
        console.log(result);
        this.tableData = result;
        return;
      }
      if (tea_name) {
        console.log("tea_name");
        result = this.tableData.filter(item => item.tea_name.indexOf(tea_name) != -1);
        console.log(result);
        this.tableData = result;
        return;
      }
      if (tea_col_no) {
        console.log("tea_col_no");
        result = this.tableData.filter(item => item.tea_col_no == tea_col_no);
        console.log(result);
        this.tableData = result;
        return;
      }
      console.log("all")
      this.tableData = this.all;



    },
    reset() {
      this.form.tea_no = '';
      this.form.tea_name = '';
      this.form.tea_col_no = '';
      this.tableData = this.all;
    },
    add() {
      console.log("add");
      this.zdydialog = true;
      this.addform = {
        tea_no: '',
        tea_name: '',
        tea_col_no: '',
        tea_level: '',
      }
    },
    cancel() {
      this.zdydialog = false;
    },
    submit() {
      console.log("submit");
      axios.post('http://localhost:8181/admin/setTeacher', this.addform)
        .then(response => {
          console.log('OKOK');
          this.zdydialog = false;
          this.getTeachers();
        })
        .catch(error => {
          window.alert("添加失败");
          console.error(error);
        });
    },
    edit(row) {
      console.log("handleEdit");
    },
    delete_(row) {
      console.log("delete");
    },
  },
  created() {
    this.getTeachers();
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