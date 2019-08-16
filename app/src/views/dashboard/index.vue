<template>
  <div class="dashboard-editor-container">
    <panel-group @handleSetLineChartData="handleSetLineChartData" />
    <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">
      <line-chart :chart-data="lineChartData" />
    </el-row>
  </div>
</template>

<script>
import PanelGroup from './components/PanelGroup'
import TodoList from './components/TodoList'
import LineChart from './components/LineChart'
import { fetchSiteUniqueVisitorData, fetchSitePageViewData } from '@/api/traffic'

export default {
  name: 'Dashboard',
  components: {
    PanelGroup,
    LineChart,
    TodoList
  },
  data() {
    return {
      lineChartData: {}
    }
  },
  created() {
    this.showVisitcountData()
  },
  methods: {
    handleSetLineChartData(type) {
      // TODO: ugly design, need to refactor.
      if (type === 'visitCounts') {
        this.showVisitcountData()
      } else if (type === 'commentCounts') {
        this.showCommentCountData()
      } else {
        this.showPostCountData()
      }
    },
    showCommentCountData() {

    },
    showPostCountData() {

    },
    showVisitcountData() {
      fetchSiteUniqueVisitorData().then(response => {
        const dates = response.data.map(uv => uv.date)
        const uniqueVisitorData = response.data.map(uv => uv.count)
        console.log('uniqueVisitorArr: ', uniqueVisitorData)
        fetchSitePageViewData().then(response => {
          const pageViewData = response.data.map(pv => pv.count)
          console.log('pageViewArr: ', pageViewData)
          this.lineChartData = {
            xAxis: {
              data: dates
            },
            series: [
              {
                name: '访客数',
                smooth: true,
                type: 'line',
                itemStyle: {
                  normal: {
                    color: '#3888fa',
                    lineStyle: {
                      color: '#3888fa',
                      width: 2
                    },
                    areaStyle: {
                      color: '#f3f8ff'
                    }
                  }
                },
                yAxisIndex: 0,
                data: uniqueVisitorData,
                animationDuration: 2800,
                animationEasing: 'quadraticOut'
              },
              {
                name: '浏览次数', itemStyle: {
                  normal: {
                    color: '#FF005A',
                    lineStyle: {
                      color: '#FF005A',
                      width: 2
                    }
                  }
                },
                smooth: true,
                type: 'line',
                yAxisIndex: 1,
                data: pageViewData,
                animationDuration: 2800,
                animationEasing: 'cubicInOut'
              }
            ]
          }
        })
      })
    }
  }
}
</script>
<style lang="scss" scoped>
.dashboard-editor-container {
  padding: 32px;
  background-color: rgb(240, 242, 245);
  position: relative;

  .github-corner {
    position: absolute;
    top: 0px;
    border: 0;
    right: 0;
  }

  .chart-wrapper {
    background: #fff;
    padding: 16px 16px 0;
    margin-bottom: 32px;
  }
}
</style>
      return
