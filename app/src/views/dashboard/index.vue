<template>
  <div class="dashboard-editor-container">
    <panel-group
      :site-total-views="siteTotalViews"
      :post-total-number="postTotalNumber"
      :comment-total-number="commentTotalNumber"
      @handleSetLineChartData="handleSetLineChartData"
    />
    <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">
      <line-chart :chart-data="lineChartData" />
    </el-row>
  </div>
</template>

<script>
import moment from 'moment'
import PanelGroup from './components/PanelGroup'
import LineChart from './components/LineChart'
import { fetchSiteTrafficData, fetchSiteTotalViews, fetchPostTotalNumber, fetchCommentTotalNumber, fetchCommentCountData } from '@/api/traffic'

export default {
  name: 'Dashboard',
  components: {
    PanelGroup,
    LineChart
  },
  data() {
    return {
      siteTotalViews: 0,
      postTotalNumber: 0,
      commentTotalNumber: 0,
      lineChartData: {}
    }
  },
  mounted() {

  },
  created() {
    this.getSiteTrafficData()
    this.getSiteTotalViews()
    this.getPostTotalNumber()
    this.getCommentTotalNumber()
    // this.showVisitcountData()
  },
  methods: {
    handleSetLineChartData(type) {
      // TODO: ugly design, need to refactor.
      if (type === 'visitCounts') {
        this.getSiteTrafficData()
      } else if (type === 'commentCounts') {
        this.showCommentCountData()
      } else {
        this.showPostCountData()
      }
    },
    getSiteTrafficData() {
      fetchSiteTrafficData().then(response => {
        const dates = response.data.map(item => moment(item.bucket).format('MM/DD')).reverse()
        const uniqueVisits = response.data.map(item => item.uniques).reverse()
        const views = response.data.map(item => item.count).reverse()
        this.lineChartData = {
          xAxis: {
            data: dates
          },
          series: [
            {
              name: '访客数',
              smooth: false,
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
              data: uniqueVisits,
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
              smooth: false,
              type: 'line',

              data: views,
              animationDuration: 2800,
              animationEasing: 'cubicInOut'
            }
          ],
          legend: {
            data: ['访客数', '浏览次数']
          }
        }
      })
    },
    getSiteTotalViews() {
      fetchSiteTotalViews().then(response => {
        this.siteTotalViews = response.data
      })
    },
    getPostTotalNumber() {
      fetchPostTotalNumber().then(response => {
        this.postTotalNumber = response.data
      })
    },
    getCommentTotalNumber() {
      fetchCommentTotalNumber().then(response => {
        this.commentTotalNumber = response.data
      })
    },
    showCommentCountData() {
      fetchCommentCountData().then(response => {
        const dates = response.data.map(item => moment(item.bucket).format('MM/DD')).reverse()
        const counts = response.data.map(item => item.count).reverse()
        this.lineChartData = {
          xAxis: {
            data: dates
          },
          series: [
            {
              name: '评论数', itemStyle: {
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
              smooth: false,
              type: 'line',
              yAxisIndex: 0,
              data: counts,
              animationDuration: 2800,
              animationEasing: 'cubicInOut'
            }
          ],
          legend: {
            data: ['评论数']
          }
        }
      })
    },
    showPostCountData() {

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
