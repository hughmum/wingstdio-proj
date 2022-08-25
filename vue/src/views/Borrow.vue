<template>
  <div>

    <div style="padding: 10px 0">
      <el-input style="width: 200px"  placeholder="请输入名称" suffix-icon="el-icon-search" v-model="deviceName"></el-input>
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
        <el-button style="float: right ; margin-right: 100px" type="primary" @click="borrowRecords">查看自己借用记录</el-button>
    </div>
    <el-table :data="tableData" border stripe :header-cell-class-name="headerBg" @selection-change="handleSelectionChange">
      <el-table-column
          type="selection"
          width="55">
      </el-table-column>
      <el-table-column prop="id" label="ID" width="50"></el-table-column>
      <el-table-column prop="deviceName" label="名称" width="140"></el-table-column>
      <el-table-column prop="price" label="价格" width="120"></el-table-column>
      <el-table-column prop="ownerName" label="拥有者"></el-table-column>
      <el-table-column prop="state" label="借用状态">
        <template slot-scope="scope">
          <div v-if="scope.row.state=='待借用'" style="color: #409EFF ; font-size: 16px">{{scope.row.state}}</div>
          <div v-if="scope.row.state=='已借出'" style="color: #67C23A ; font-size: 16px">{{scope.row.state}}</div>
        </template>
      </el-table-column>
      <el-table-column label="图片" align="center" width="265px">
        <template slot-scope="scope">
        <span v-for="(item,index) in scope.row.filesList" :key="index">
            <el-popover placement="left" trigger="click" width="300">
                <img :src="item.url" width="100%" />
                <img
                    slot="reference"
                    :src="item.url"
                    :alt="item"
                    style="max-height: 70px;max-width: 70px; padding: 5px"
                />
            </el-popover>
        </span>
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button type="success" @click="handleBorrow(scope.row)">借用<i class="el-icon-edit"></i></el-button>
          <el-popconfirm
              class="ml-5"
              confirm-button-text='确定'
              cancel-button-text='再想想'
              icon="el-icon-info"
              icon-color="red"
              title="确定删除吗？"
              @confirm="del(scope.row.id)"
          >
            <el-button type="danger" slot="reference">取消借用<i class="el-icon-remove-outline"></i></el-button>
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
        <el-form-item label="名称">
          <el-input v-model="form.deviceName" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="价格">
          <el-input v-model="form.price" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="拥有者">
          <el-input v-model="form.ownerName" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="借用原因">
          <el-input type="textarea" v-model="form.borrowReason" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="borrowDevice">确 定</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
export default {
  name: "Device",
  data(){
    return {
      tableData: [],
      total:0,
      pageNum:1,
      pageSize:6,
      form:{},
      fileList:[],//作为图片url的数组
      deviceName:"",

      dialogFormVisible:false,
      multipleSelection:[],
      headerBg:'headerBg',
      dialogVisible: false,
      user: localStorage.getItem("userInfo") ? JSON.parse(localStorage.getItem("userInfo")):{}
    }
  },
  created(){
    //请求 分页查询数据
    //将字符串转成一个json
    this.load();
    this.form.fileList= this.fileList
  },
  methods:{
    load(){
      this.request.get("/device/page/",{
        params:{
          pageNum:this.pageNum,
          pageSize:this.pageSize,
          deviceName:this.deviceName,
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
    uploadSuccess1(res){
      console.log(res)
      this.fileList.push(res)  //这里直接push会报错，但是改的话就不能把图片存成数组形式了，我还没有找到好的办法，所以只能先这样把功能实现了
      console.log(this.fileList)
      this.form.fileurl = this.fileList
      console.log(this.form.fileurl)
    },
    reset(){
      this.deviceName=""
      this.load()
    },
    //借用
    handleBorrow(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.form.deviceId = row.id
      console.log(this.user.username)
      this.form.id = null
      this.dialogFormVisible=true
    },
    borrowDevice() {
      this.form.borrowPersonId = this.user.id
      this.form.borrowPerson  = this.user.username
      console.log(this.form)
      if(this.form.state==='已借出'){
        this.$message.error("已被其他人借用")
        this.dialogFormVisible=false
      }
      else{
        // console.log(this.user)
        this.request.post("/borrow",this.form).then(res=>{
          if(res.code==='200'){
            this.$message.success("请求已发送");
            this.dialogFormVisible=false
            this.load()
          }
          else{
            this.$message.error("失败")
          }
        })
      }
    },
    del(id){
      this.request.delete("/device/"+id).then(res=>{
        if(res.data){
          this.$message.success("删除成功")
          this.load()
        }else{
          this.$message.error("删除失败")
        }
      })
    },
    delBatch(){
      let ids = this.multipleSelection.map(v=>v.id) //将数据扁平化处理，由对象转化为数组
      this.request.post("/device/del/batch",ids).then(res=>{
        if(res.data){
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
    handleRemove(file, fileList) {
      console.log(file, fileList);
    },
    handlePreview(file) {
      console.log(file);
    },
    //查看借用记录
    borrowRecords() {
      this.$router.push('/borrow/myborrow')
    }
  }
}
</script>

<style scoped>

</style>