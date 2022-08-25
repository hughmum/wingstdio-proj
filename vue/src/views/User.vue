<template>
  <div>

    <div style="padding: 10px 0">
      <el-input style="width: 200px"  placeholder="请输入名称" suffix-icon="el-icon-search" v-model="username"></el-input>
      <el-input style="width: 200px"  placeholder="请输入邮箱" suffix-icon="el-icon-message" v-model="email"></el-input>
      <!--              <el-input style="width: 200px"  placeholder="请输入地址" suffix-icon="el-icon-position"></el-input>-->
      <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
      <el-button  type="warning" @click="reset">重置</el-button>
    </div>
    <div style="margin: 10px 0">
      <el-button type="primary" @click="handleAdd">新增<i class="el-icon-circle-plus-outline"></i></el-button>
      <el-popconfirm
          class="ml-5"
          confirm-button-text='确定'
          cancel-button-text='再想想'
          icon="el-icon-info"
          icon-color="red"
          title="确定删除吗？"
          @confirm="delBatch"
      >
        <el-button type="danger" slot="reference">批量删除<i class="el-icon-remove-outline"></i></el-button>
      </el-popconfirm>
      <el-upload
          :action="'http://'+serverIp+':9090/user/import'" :show-file-list="false" accept="xlsx" :on-success="handleExcelImportSuccess" style="display: inline-block">
      <el-button type="primary" class="ml-5">导入<i class="el-icon-bottom"></i></el-button>
      </el-upload>
      <el-button type="primary" @click="exp" class="ml-5">导出<i class="el-icon-top"></i></el-button>
    </div>
    <el-table :data="tableData" border stripe :header-cell-class-name="headerBg" @selection-change="handleSelectionChange">
      <el-table-column
          type="selection"
          width="55">
      </el-table-column>
      <el-table-column prop="id" label="ID" width="50"></el-table-column>
      <el-table-column prop="username" label="姓名" width="140"></el-table-column>
      <el-table-column prop="role" label="角色" width="140">
        <template slot-scope="scope">
          <span v-if="scope.row.role == 'user'">学生</span>
          <span v-if="scope.row.role == 'admin'">管理员</span>
          <span v-if="scope.row.role == 'teacher'">教师</span>
        </template>
      </el-table-column>
      <el-table-column prop="account" label="学工号" width="120"></el-table-column>
      <el-table-column prop="email" label="邮箱"></el-table-column>
      <el-table-column prop="phone" label="电话"></el-table-column>
      <el-table-column prop="address" label="地址"></el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button type="success" @click="handleEdit(scope.row)">编辑<i class="el-icon-edit"></i></el-button>
          <el-popconfirm
              class="ml-5"
              confirm-button-text='确定'
              cancel-button-text='再想想'
              icon="el-icon-info"
              icon-color="red"
              title="确定删除吗？"
              @confirm="del(scope.row.id)"
          >
            <el-button type="danger" slot="reference">删除<i class="el-icon-remove-outline"></i></el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pageNum"
        :page-sizes="[2, 5, 10, 20]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
    </el-pagination>

    <el-dialog title="用户信息" :visible.sync="dialogFormVisible" width="30%">
      <el-form label-width="60px" size="small">
        <el-form-item label="用户名">
          <el-input v-model="form.username" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="角色">
          <el-select clearable v-model="form.role" placeholder="请选择角色" style="width: 80%">
            <el-option v-for="item in roles" :key="item.name" :label="item.name" :value="item.flag">
              <i :class="item.value" /> {{item.name}}
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="学工号">
          <el-input v-model="form.account" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="form.email" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="电话">
          <el-input v-model="form.phone" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="地址">
          <el-input v-model="form.address" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import {serverIp} from "../../public/config";

export default {
  name: "User",
  data(){
    return {
      tableData: [],
      total:0,
      pageNum:1,
      pageSize:6,
      form:{},
      username:"",
      email:"",
      dialogFormVisible:false,
      multipleSelection:[],
      headerBg:'headerBg',
      roles: [],
      serverIp: serverIp
    }
  },
  created(){
    //请求 分页查询数据
    //将字符串转成一个json
    this.load();
  },
  methods:{
    load(){
      this.request.get("/user/page/",{
        params:{
          pageNum:this.pageNum,
          pageSize:this.pageSize,
          username:this.username,
          email:this.email
        }
      }).then(res=>{
        console.log(res)
        this.tableData=res.data.records
        this.total = res.data.total
      })
      // fetch("http://localhost:9090/user/page?pageNum="+this.pageNum+"&pageSize="+this.pageSize+"&username="+this.username+"&email="+this.email).then(res=>res.json()).then(res=>{
      //   console.log(res);
      //   this.tableData = res.data;
      //   this.total=res.total;
      // })
      this.request.get("/role").then(res=>{
        this.roles = res.data
      })
    },
    save(){
      this.request.post("/user",this.form).then(res=>{
        if(res.code==='200'){
          this.$message.success("保存成功")
          this.dialogFormVisible=false
          this.load()
        }else{
          this.$message.error("保存失败")
        }
      })
    },
    reset(){
      this.username=""
      this.email=""
      this.load()
    },
    //修改数据
    handleEdit(row){
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogFormVisible=true
    },
    del(id){
      this.request.delete("/user/"+id).then(res=>{
        if(res.code==='200'){
          this.$message.success("删除成功")
          this.load()
        }else{
          this.$message.error("删除失败")
        }
      })
    },
    delBatch(){
      let ids = this.multipleSelection.map(v=>v.id) //将数据扁平化处理，由对象转化为数组
      this.request.post("/user/del/batch",ids).then(res=>{
        if(res.code==='200'){
          this.$message.success("删除成功")
          this.load()
        }else{
          this.$message.error("删除失败")
        }
      })
    },
    handleSelectionChange(val){
      this.multipleSelection = val;
    },
    handleSizeChange(pageSize){
      console.log(pageSize);
      this.pageSize=pageSize;
      //当切换页码的时候，重新请求一下，由于参数已经更i性能，那就在加载时候直接变化
      this.load();
    },
    handleCurrentChange(pageNum){
      console.log(pageNum);
      this.pageNum=pageNum;
      this.load();
    },
    //新增数据
    handleAdd(){
      this.dialogFormVisible = true
      this.form = {}
    },
    //导出
    exp(){
      window.open(`http://${serverIp}:9090/user/export`);
    },
    //导入成功的提示
    handleExcelImportSuccess(){
      this.$message.success("导入成功");
      this.load();
    }
  }
}
</script>

<style>
.headerBg{
  background:#eee !important;
}
</style>