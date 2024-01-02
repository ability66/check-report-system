<template>
  <el-card class="box-card" style="height: 79vh">
    <div id="stu_change">
       <el-form v-model="form" label-width="200px">
        <el-form-item prop="cla" label="班号">
          <el-input v-model="form.cla" style="width: 70%;" placeholder="请输入内容" clearable></el-input>
        </el-form-item>
        <el-form-item prop="week" label="时间">
          <el-select v-model="form.week" placeholder="请选择日期" style="width: 70%">
            <el-option label="星期一" value="周一"></el-option>
            <el-option label="星期二" value="周二"></el-option>
            <el-option label="星期三" value="周三"></el-option>
            <el-option label="星期四" value="周四"></el-option>
            <el-option label="星期五" value="周五"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item prop="time" label="节次">
          <el-select v-model="form.time" placeholder="请选择节次" style="width: 70%">
            <el-option label="第一节" value="1"></el-option>
            <el-option label="第二节" value="2"></el-option>
            <el-option label="第三节" value="3"></el-option>
            <el-option label="第四节" value="4"></el-option>
            <el-option label="第五节" value="5"></el-option>
            <el-option label="第六节" value="6"></el-option>
            <el-option label="第七节" value="7"></el-option>
            <el-option label="第八节" value="8"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="课程">
        <el-select v-model="selected_course" value-key="sub_no" filterable clearable placeholder="请选择课程"
          style="width:70%">
          <el-option v-for="item in courses" :key="item.sub_no" :label="item.sub_name" :value="item" style="width: 70%">
          </el-option>
        </el-select>
      </el-form-item>
      
      <el-form-item>
        <el-button type="primary" @click="onSubmit">确定编辑</el-button>
      </el-form-item> 
      </el-form> 


    </div>
  </el-card>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      form: {
        cla: '',//class_no班级号
        week: '',//class_weekday
        time: '',//class_order
        // sub_no: '',//sub_no课程号
        // tea_no: '',//tea_no教师号
      },//表单数据
      selected_course: {
        sub_no: '',
        tea_no: '',
        sub_name: ''
      },
      courses: [
        {
          sub_no: '2',
          tea_no: '111',
          sub_name: '复变'
        },
        {
          sub_no: '3',
          tea_no: '222',
          sub_name: 'c++'
        },
        {
          sub_no: '4',
          tea_no: '333',
          sub_name: '经济学'
        },
        {
          sub_no: '5',
          tea_no: '12312',
          sub_name: '英语'
        },
        {
          sub_no: '6',
          tea_no: '12313',
          sub_name: '算法'
        },
        {
          sub_no: '7',
          tea_no: '222',
          sub_name: 'python'
        }
      ],
    }
  },
  methods: {
    
    onSubmit() {
      console.log('submit!');
      this.form.sub_no = this.selected_course.sub_no;
      this.form.tea_no = this.selected_course.tea_no;
      console.log(this.form);
      const classTable = {
        class_no: this.form.cla,
        class_weekday: this.form.week,
        class_order: this.form.time,
        sub_no: this.form.sub_no,
        tea_no: this.form.tea_no
      };
      console.log(classTable)
      const sub_name = this.selected_course.sub_name;
      const tea_no = this.selected_course.tea_no;
      const data = {
        classTable,
        sub_name,
        tea_no
      }
      console.log(data)
      axios.post('http://localhost:8181/admin/setClasstable', data, {
          headers: {
            'Content-Type': 'application/json;charset=UTF-8'
          }
        })
        .then(response => {
          
        })
        .catch(error => {
          console.log(error);
        });

    },
  }
}
</script>

<style scoped>
.container {
  width: 100%;
  height: 100%;
  /*background-color: #42b983;*/
  /*flex弹性布局*/
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background-repeat: no-repeat;
  background-size: cover;
  background-color: blanchedalmond;
}

.output {
  line-height: 40px;
  align-items: center;
  width: 70%;
  height: 70%;
  border-radius: 30px;
  overflow: hidden;
}

.empty {
  margin-top: 60px;
}
</style>