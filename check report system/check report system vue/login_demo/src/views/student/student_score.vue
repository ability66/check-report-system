<template>
  <el-card class="box-card" style="height: 79vh">
    <el-table :data="grades" height="522" border style="width: 100%;">
      <el-table-column sortable align="center" prop="course_no" label="课程号" width="300">
      </el-table-column>
      <el-table-column sortable align="center" prop="score" label="成绩">
      </el-table-column>
    </el-table>
  </el-card>
</template>

<script>
import axios from 'axios';
import Cookies from 'js-cookie';
export default {
  data() {
    return {
      grades: []
      // 用于存储学生成绩的数组
    };
  },
  mounted() {
    this.getGrades(); // 在页面加载完成后自动调用获取成绩的方法
  },
  methods: {
    getGrades() {
      console.log("学生成绩")
      const stu_no = Cookies.get('userId');; // 要请求的学生学号

      // 发送请求获取学生成绩
      axios.post('http://localhost:8181/student/getGrade', stu_no, {
        headers: {
          'Content-Type': 'application/json;charset=UTF-8'
        }
      })
        .then(response => {
          console.log(response.data);
          this.grades = response.data.map(
            item => (
              {
                course_no: item.sub_no,
                score: item.grade
              }
            )
          );
        })
        .catch(error => {
          console.error(error);
        });
      console.log(this.grades);
    }
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
</style>