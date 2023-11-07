<template>
  <div>
    <el-row :gutter="10" style="margin-bottom: 40px">
      <el-col :span="6">
        <el-card style="color: #409EEF">
         <div> <i class="el-icon-user-solid"></i>用户总数</div>
          <div style="padding: 10px 0 ;text-align: center;font-weight: bold"   >
            100
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card style="color: #F56C6C">
          <div ><i class="el-icon-money"></i> 销售总量</div>
          <div style="padding: 10px 0 ;text-align: center;font-weight: bold"   >
            100
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card style="color: #67C23A">
          <div >  <i class="el-icon-bank-card"></i>收益总额</div>
          <div style="padding: 10px 0 ;text-align: center;font-weight: bold"   >
            100
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card style="color: #E6A23C">
          <div > <i class="el-icon-s-shop"></i> 门店总数</div>
          <div style="padding: 10px 0 ;text-align: center;font-weight: bold"   >
            100
          </div>
        </el-card>
      </el-col>

    </el-row>
    <el-row>
      <el-col :span="12">
        <div id="main" style="width: 500px;height: 400px;"></div>
      </el-col>
      <el-col :span="12">
        <div id="pai" style="width: 500px;height: 400px;"></div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts';
export default {
    name:"Home",
  data(){
      return{

      }
  },
  mounted() {
    var option = {
      tooltip: {
        trigger: 'item'
      },
      title: {
        text: '各季度会员数量统计',
        subtext: '趋势图',
        left: 'center'
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
          data: [],
          type: 'line'
        },
        {
          data: [],
          type: 'bar'
        }
      ]
    };
    var chartDom = document.getElementById('main');
    var myChart = echarts.init(chartDom);


   var paioption = {
      title: {
        text: '各季度会员数量统计',
        subtext: '比例图',
        left: 'center'
      },
      tooltip: {
        trigger: 'item'
      },
      legend: {
        orient: 'vertical',
        left: 'left'
      },
      series: [
        {
          type: 'pie',
          radius: '60%',
          label:{
          normal: {
            show: true,
            position: 'inner',
            textStyle: {
              fontWeight: 300,
              fontsize: 16
            },
            formatter: '{d}%'
          }
          },
          data: [

          ],
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        }
      ]
    };
    var paiDom = document.getElementById('pai');
    var paiChart = echarts.init(paiDom);
    this.request.get("/echarts/members").then(res=>{
      option.series[0].data=res.data
      option.series[1].data=res.data
      myChart.setOption(option);

      paioption.series[0].data=[
        {name:"第一季度",value:res.data[0]},
        {name:"第二季度",value:res.data[1]},
        {name:"第三季度",value:res.data[2]},
        {name:"第四季度",value:res.data[3]},
      ]
      paiChart.setOption(paioption)
    })

  }
};
</script>

<style>
</style>