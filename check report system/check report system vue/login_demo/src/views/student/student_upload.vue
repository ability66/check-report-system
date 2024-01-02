<template>
  <el-card class="box-card" style="height: 80vh;">
    <div class="main">
      <i class="el-icon-warning" style="font-size: 15px;">响应净网行动，严禁利用管理系统上传、传播色情暴力等违法内容，一经查处将冻结账号！</i>
    </div>
    <div class="centered-container">
      <el-upload 
        class="upload-demo" 
        :ref="upload"
        :data=extraData
        action=http://localhost:8181/student/upload

        :on-preview="handlePreview"
        :on-remove="handleRemove" 
        :before-remove="beforeRemove" 
        :limit="1"
        :on-exceed="handleExceed" 
        :file-list="fileList" 
        :before-upload="beforeUpload">

        <div class="centered-content">
          <div class="select">
            <el-select 
              v-model="value" 
              filterable 
              remote 
              reserve-keyword 
              clearable 
              placeholder="请选择任课导师"

              :loading="loading">
              <el-option 
                v-for="teacher in list" 
                :key="teacher.tea_no" 
                :label="teacher.tea_name"
                :value="teacher.tea_no">
              </el-option>
            </el-select>
          </div>
          <el-button type="primary" @click="uploadFile">
            <i class="el-icon-upload" style="font-size: 25px;">点击上传报告</i>
          </el-button>
        </div>
        <div slot="tip" class="el-upload__tip" style="font-size: 15px;">只能上传word文件，且不超过20MB</div>
      </el-upload>
    </div>
  </el-card>
</template>



<script>
import axios from 'axios';
import Cookies from 'js-cookie';
export default {
  name: "student_upload",
  data() {
    return {

      fileList: [],//存放上传的文件
      uploadfile: null,//实际要上传的文件
      uploadUrl: 'localhost:8181/student/upload',
      option: [],//通过输入查找时的选项
      value: '',//el-select中option的value值
      loading: false,
      list: [],//教师姓名列表
      extraData: {
        stuNo:'',
        teaNo:''
      },

    };
  },
  mounted() {
      // 发送 HTTP 请求获取教师数据
      axios.post('http://localhost:8181/student/getTeachers')
        .then(response => {
          // 将返回的教师数据转换为包含教师姓名和工号的数组
          const teacherData = response.data.map(teacher => ({
            tea_no: teacher.tea_no,
            tea_name: teacher.tea_name,
          }));
          // 将教师姓名和工号的数组赋值给 list[]
          this.list = teacherData;
          console.log(this.list);
        })
        .catch(error => {
          // 处理请求失败的逻辑
        });
    },
  methods:{
    uploadFile() {
      this.$refs.upload.submit();
      console.log("上传结束")
    },
    handleRemove(file, fileList) {
      console.log(file, fileList);
    },
    handlePreview(file) {
      console.log(file);
    },
    handleExceed(files, fileList) {
      this.$message.warning(`当前限制选择 1 个文件`);
    },
    beforeRemove(file, fileList) {
      return this.$confirm(`确定移除 ${file.name}？`);
    },
    beforeUpload(file) {
      const isWord = file.name.endsWith('.doc') || file.name.endsWith('.docx');
      const isLt20M = file.size / 1024 / 1024 < 20;
      if (!isWord) {
        this.$message.error('上传文件只能是 word 格式!');
      }
      if (!isLt20M) {
        this.$message.error('上传文件大小不能超过 20MB!');
      }
      this.uploadfile = file;

      // const formData = new FormData();
      // formData.append('file', file);
      // formData.append('stuNo', Cookies.get('userId'));
      // formData.append('teaNo', this.value);
      // console.log("formData")
      // console.log(formData)
      this.extraData.teaNo = this.value;
      this.extraData.stuNo = Cookies.get('userId');
      console.log(typeof (this.extraData.stuNo));
      console.log(typeof (this.extraData.teaNo));
      console.log(this.extraData);
      // for (const pair of formData.entries()) {
      //   console.log(pair[0] + ': ' + pair[1]);
      // }
      // this.form = formData;

      return isWord && isLt20M;
    },
    // ------------------------------------
    handleSelect(value) {
      // 通过教师姓名获取对应的教师工号
      const selectedTeacher = this.list.find(teacher => teacher.tea_name === value);
      const number = selectedTeacher ? selectedTeacher.teaNo : '';
      console.log(number);
    },
  }
}
</script>

<style scoped>
.main {
  margin-bottom: 20vh;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.select {
  margin-bottom: 15vh;
}
</style>
<style>
.centered-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
}

.centered-content {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}
</style>
