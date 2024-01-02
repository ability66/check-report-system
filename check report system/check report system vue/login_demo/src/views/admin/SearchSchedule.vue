<template>
  <el-card class="box-card" style="height: 79vh">
    <div style="position: relative;height: 100%">

      <div style="font-size: 35px">
        查询课表信息
      </div>
      <br>
      <br>
      <el-form>
        <el-col :span="8">
          <el-input tag="input"  placeholder="请输入班号" prefix-icon="el-icon-search" v-model="class_no"></el-input>
        </el-col>
        <el-button type="primary" @click="search">查询</el-button>
      </el-form>
      <br>
      <div class="output">
        <el-table :data="tableData" style="width: 100%;font-size: 20px;" max-height="800" border :header-cell-style="{color: '#ffffff',fontSize: '16px', backgroundColor: '#778899'}">
      <el-table-column prop="time" label="" align="center"></el-table-column>
      <el-table-column prop="monday" label="星期一" align="left" font-size:17px >
        <template slot-scope="scope">
          <div v-html="scope.row.monday"></div>
        </template>
      </el-table-column>
      <el-table-column prop="tuesday" label="星期二" align="left" font-size:17px >
        <template slot-scope="scope">
          <div v-html="scope.row.tuesday"></div>
        </template>
      </el-table-column>
      <el-table-column prop="wednesday" label="星期三" align="left" font-size:17px >
        <template slot-scope="scope">
          <div v-html="scope.row.wednesday"></div>
        </template>
      </el-table-column>
      <el-table-column prop="thursday" label="星期四" align="left" font-size:17px >
        <template slot-scope="scope">
          <div v-html="scope.row.thursday"></div>
        </template>
      </el-table-column>
      <el-table-column prop="friday" label="星期五" align="left" font-size:17px >
        <template slot-scope="scope">
          <div v-html="scope.row.friday"></div>
        </template>
      </el-table-column>
    </el-table>
      </div>



    </div>
  </el-card>
</template>




<script>
export default {
  name: "student_course",
  data() {
    return {
      class_no: '',
      tableData: []
    }
  },
  mounted() {
    this.getCourses(); // 在页面加载完成后自动调用获取成绩的方法
  },
  methods: {
    getCourses() {
      console.log("学生课表")
      const class_no = Cookies.get('class_no');; // 要请求的学生学号

      this.tableData = [];
      for (let i = 0; i < 11; i++) {
        let rowData = {
          time: i + 1 + '节',
          monday: '',
          tuesday: '',
          wednesday: '',
          thursday: '',
          friday: ''
        };
        this.tableData.push(rowData);
      }

      axios.post('http://localhost:8181/student/getClassTable', stu_no, {
        headers: {
          'Content-Type': 'application/json;charset=UTF-8'
        }
      })
        .then(response => {
          console.log(response.data);
          response.data.forEach(item => {
            let index = item.class_order - 1;
            let weekday = item.class_weekday;
            let prop = '';
            switch (weekday) {
              case '周一':
                prop = 'monday';
                break;
              case '周二':
                prop = 'tuesday';
                break;
              case '周三':
                prop = 'wednesday';
                break;
              case '周四':
                prop = 'thursday';
                break;
              case '周五':
                prop = 'friday';
                break;
            }
            this.tableData[index][prop] = `课程号: ${item.sub_no}<br>老师号: ${item.tea_no}`;
            // this.tableData[index][prop] = "课程号: " + item.sub_no + "<br>" + "老师号: " + item.tea_no;
          })
        })
        .catch(error => {
          console.error(error);
        });
    },
    search(class_no) {
      this.getCourses();
    }
  }
}
</script>

<style scoped>
.demo-input-wrapper {
  display: flex;
  width: 70%;
}
.demo-input-wrapper span {
  font-size: 25px;
  width:40vw;
  height: 38px;
}
</style>