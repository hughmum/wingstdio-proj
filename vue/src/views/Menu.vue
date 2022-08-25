<template>
  <div>

    <div style="padding: 10px 0">
      <el-input style="width: 200px"  placeholder="请输入名称" suffix-icon="el-icon-search" v-model="name"></el-input>
      <!--              <el-input style="width: 200px"  placeholder="请输入地址" suffix-icon="el-icon-position"></el-input>-->
      <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
      <el-button  type="warning" @click="reset">重置</el-button>
    </div>
    <div style="margin: 10px 0">
      <el-button type="primary" @click="handleAdd()">新增<i class="el-icon-circle-plus-outline"></i></el-button>
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
    </div>
    <el-table :data="tableData" border stripe :header-cell-class-name="headerBg"  row-key="id" @selection-change="handleSelectionChange">
      <el-table-column
          type="selection"
          width="55">
      </el-table-column>
      <el-table-column prop="id" label="ID" width="80px"></el-table-column>
      <el-table-column prop="name" label="名称" ></el-table-column>
      <el-table-column prop="path" label="路径" ></el-table-column>
      <el-table-column prop="pagePath" label="页面路径" ></el-table-column>
      <el-table-column label="图标"  align="center" >
        <template slot-scope="scope">
          <i :class="scope.row.icon" style="font-size: 20px"/>
        </template>
      </el-table-column>
      <el-table-column prop="description" label="描述" ></el-table-column>
      <el-table-column label="操作" width="300px">
        <template slot-scope="scope">
          <el-button type="primary" @click="handleAdd(scope.row.id)" v-if="!scope.row.pid && !scope.row.path">新增子菜单<i class="el-icon-edit"></i></el-button>
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

    <el-dialog title="菜单信息" :visible.sync="dialogFormVisible" width="30%">
      <el-form label-width="60px" size="small">
        <el-form-item label="名称">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="路径">
          <el-input v-model="form.path" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="页面路径">
          <el-input v-model="form.pagePath" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="图标">
          <el-select clearable v-model="form.icon" placeholder="请选择" style="width: 80%">
            <el-option v-for="item in options" :key="item.name" :label="item.name" :value="item.value">
               <i :class="item.value" /> {{item.name}}
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" autocomplete="off"></el-input>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog title="菜单分配" :visible.sync="menuDialogVis" width="30%">
      <el-form label-width="60px" size="small">
        <el-form-item label="名称">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="路径">
          <el-input v-model="form.path" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="页面路径">
          <el-input v-model="form.pagePath" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="图标">
          <el-input v-model="form.icon" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="menuDialogVis = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
export default {
  name: "User",
  data(){
    return {
      tableData: [],
      total:0,
      pageNum:1,
      pageSize:6,
      form:{},
      name:"",
      dialogFormVisible:false,
      multipleSelection:[],
      menuDialogVis: false,
      headerBg:'headerBg',
      options: []//下拉菜单的数据
    }
  },
  created(){
    //请求 分页查询数据
    //将字符串转成一个json
    this.load();
  },
  methods:{
    load(){
      this.request.get("/menu",{
        params:{
          name:this.name,
        }
      }).then(res=>{
        console.log(res)
        this.tableData=res.data
      })
      // fetch("http://localhost:9090/user/page?pageNum="+this.pageNum+"&pageSize="+this.pageSize+"&username="+this.username+"&email="+this.email).then(res=>res.json()).then(res=>{
      //   console.log(res);
      //   this.tableData = res.data;
      //   this.total=res.total;
      // })
    },
    save(){
      this.request.post("/menu",this.form).then(res=>{
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
      this.name=""
      this.load()
    },
    //修改数据
    handleEdit(row){
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogFormVisible=true

      //请求图标的数据
      this.request.get("/menu/icons").then(res=>{
        console.log(res)
        this.options=res.data
      })
    },
    del(id){
      this.request.delete("/menu/"+id).then(res=>{
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
      this.request.post("/menu/del/batch",ids).then(res=>{
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
    handleAdd(pid){
      this.dialogFormVisible = true
      this.form = {}
      if (pid){
        this.form.pid = pid
      }
    },

  }
}
</script>

<style>
.headerBg{
  background:#eee !important;
}
.fontSize18{
  font-size: 18px;
}
.fontSize12{
  font-size: 12px;
}
</style>