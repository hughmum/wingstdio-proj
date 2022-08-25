<template>
  <div>

    <div style="padding: 10px 0">
      <el-input style="width: 200px"  placeholder="请输入名称" suffix-icon="el-icon-search" v-model="name"></el-input>
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
    </div>

    <el-table :data="tableData" border stripe :header-cell-class-name="headerBg" @selection-change="handleSelectionChange">
      <el-table-column
          type="selection"
          width="55">
      </el-table-column>
      <el-table-column prop="id" label="ID" width="80px"></el-table-column>
      <el-table-column prop="name" label="名称" ></el-table-column>
      <el-table-column prop="flag" label="唯一标识" ></el-table-column>
      <el-table-column prop="description" label="描述" ></el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button type="info" @click="selectMenu(scope.row)">分配菜单<i class="el-icon-menu"></i></el-button>
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

    <el-dialog title="角色信息" :visible.sync="dialogFormVisible" width="30%">
      <el-form label-width="60px" size="small">
        <el-form-item label="名称">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="唯一标识">
          <el-input v-model="form.flag" autocomplete="off"></el-input>
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
      <el-tree
          :props="props"
          :data="menuData"
          show-checkbox
          node-key = "id"
          ref="tree"
          :default-expanded-keys="expends"
          :default-checked-keys="checks"
          >
         <span class="custom-tree-node" slot-scope="{ node, data }">
           <span><i :class="data.icon"></i>{{ data.name }}</span>
         </span>
      </el-tree>
      <div slot="footer" class="dialog-footer">
        <el-button @click="menuDialogVis = false">取 消</el-button>
        <el-button type="primary" @click="saveRoleMenu">确 定</el-button>
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
      menuData: [],
      props: {
        label: 'name',
      },
      expends: [],
      checks: [],
      roleId: 0,
      roleFlag: ''
    }
  },
  created(){
    //请求 分页查询数据
    //将字符串转成一个json
    this.load();
  },
  methods:{
    load(){
      this.request.get("/role/page/",{
        params:{
          pageNum:this.pageNum,
          pageSize:this.pageSize,
          name:this.name,
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

    },
    save(){
      this.request.post("/role",this.form).then(res=>{
        if(res.code==='200'){
          this.$message.success("保存成功")
          this.dialogFormVisible=false
          this.load()
        }else{
          this.$message.error("保存失败")
        }
      })
    },
    saveRoleMenu(){
      this.request.post("/role/roleMenu/"+this.roleId,this.$refs.tree.getCheckedKeys()).then(res =>{
          if (res.code==='200'){
            this.$message.success("绑定成功")
            this.menuDialogVis = false;
          }else{
            this.$message.error("绑定失败")
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
    },
    del(id){
      this.request.delete("/role/"+id).then(res=>{
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
      this.request.post("/role/del/batch",ids).then(res=>{
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
    selectMenu(role) {

      this.roleId = role.id
      this.roleFlag = role.flag
      //请求菜单数据
      this.request.get("/menu").then(res=>{
        console.log(res)
        this.menuData=res.data
        //把后台返回的菜单数据处理成id数组
        this.expends = this.menuData.map(v => v.id);
      })
      //请求已选中的数据
      this.request.get("/role/roleMenu/"+this.roleId).then(res=>{

        this.checks = res.data;
        //后端查询出所有菜单的id，然后跟我们本角色选中的id所比较
        this.request.get("/menu/ids").then(r =>{
          const ids  = r.data
          ids.forEach(id =>{
            if(!this.checks.includes(id)){
              //可能会报错
              this.$nextTick(()=>{
                this.$refs.tree.setChecked(id,false)//当当前的菜单项不是选中的就设为false
              })
            }
          })
          this.menuDialogVis = true;
        })
      })
    },
  }
}
</script>

<style>
.headerBg{
  background:#eee !important;
}
</style>