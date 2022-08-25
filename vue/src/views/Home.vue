<template>
  <div>
    <el-row :gutter="10" style="margin-bottom: 40px">
      <el-col :span="6">
          <el-card>
            <div style="color: #409EFF"><i class="el-icon-user-solid
">用户总数</i></div>
            <div style="padding: 10px 0;text-align: center; font-weight: bold">
                <el-tag type="primary" style="font-size: 20px">100</el-tag>
            </div>
          </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <div style="color: #409EFF">用户总数</div>
          <div style="padding: 10px 0;text-align: center; font-weight: bold">
            100
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <div style="color: #409EFF">用户总数</div>
          <div style="padding: 10px 0;text-align: center; font-weight: bold">
            100
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <div style="color: #409EFF">用户总数</div>
          <div style="padding: 10px 0;text-align: center; font-weight: bold">
            100
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="12">
        <div id="main" style="width: 500px ; height: 400px"></div>
      </el-col>
      <el-col :span="12">
        <div id="pai" style="width: 500px ; height: 400px"></div>
      </el-col>
    </el-row>

  </div>
</template>

<script>
import * as echarts from 'echarts';

export default {
  name: "Home",
  data() {
      return {

      }
  },
  mounted() { //页面元素渲染之后再触发
    var option = {
      title:{
        text:'个季度会员数量统计',
        subtext:'趋势图',
        left:'center'
      },
      tooltip: {
        trigger: 'item'
      },
      legend: {
        orient: 'vertical',
        left: 'left'
      },
      xAxis: {
        type: 'category',
        data: ["第一季度","第二季度","第三季度","第四季度"]
      },
      yAxis: {
        type: 'value'
      },
      series: [
        {
          name: "1",
          data: [],
          type: 'line'
        } ,
        {
          name: "2",
          data: [],
          type: 'bar'
        }
      ]
    };
    var chartDom = document.getElementById('main');
    var myChart = echarts.init(chartDom);
    //饼图
    var paioption = {
      legend: {
        top: 'bottom'
      },
      toolbox: {
        show: true,
        feature: {
          mark: { show: true },
          dataView: { show: true, readOnly: false },
          restore: { show: true },
          saveAsImage: { show: true }
        }
      },
      series: [
        {
          type: 'pie',
          radius: [0, 130],
          center: ['50%', '50%'],
          roseType: 'area',
          itemStyle: {
            borderRadius: 8
          },
          data: [],
          label:{
            normal:{
              show: true,
              position:'inner',
              textStyle:{
                fontSize: 16,
                color:"#fff"
              },
              formatter: '{d}%'
            }
          },
        }
      ]
    };
    var paichartDom = document.getElementById('pai');
    var paimyChart = echarts.init(paichartDom);

    //请求数据
    this.request.get("/echarts/members").then(res => {
      //填空
      // option.xAxis.data = res.data.x
      option.series[0].data = res.data
      option.series[1].data = res.data
      //必须再数据准备完毕之后set
      myChart.setOption(option);


      paioption.series[0].data=[
        {name:"第一季度",value:res.data[0]},
        {name:"第二季度",value:res.data[1]},
        {name:"第三季度",value:res.data[2]},
        {name:"第四季度",value:res.data[3]},
      ]
      paimyChart.setOption(paioption)
    })


  }
}
</script>

<style scoped>

</style>