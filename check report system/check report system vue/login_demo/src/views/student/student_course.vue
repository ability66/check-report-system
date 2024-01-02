<template>
  <!--  <div id="stu_course" class="container">
      <div class="output">-->
  <el-card class="box-card" >
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
  </el-card>
  <!--    </div>
    </div>-->
</template>

<script>
import axios from 'axios';
import Cookies from 'js-cookie';
export default {
  data() {
    return {
      tableData: []// 显示的table数据

    };
  },
  mounted() {
    this.getCourses(); // 在页面加载完成后自动调用获取成绩的方法
  },
  methods: {
    getCourses() {
      console.log("学生课表")
      const stu_no = Cookies.get('userId');; // 要请求的学生学号

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

  }
};

</script>

<style scoped>
.container {
  width: 100%;
  height: 100%;
  /*background-color: #42b983;*/
  /*flex弹性布局*/
  display: flex;
  justify-content: center;
  align-items: center;
  background-repeat: no-repeat;
  background-size: cover;
  background-color: blanchedalmond;
}

.output {
  align-items: center;
  width: 70vw;
  height: 70vh;
  border-radius: 30px;
  overflow: hidden;
  /*圆角*/
  background-color: #eeeeee;
}
.rowClass {
  background-color: #f2f2f2; /* 设置灰色背景色 */
}
</style>