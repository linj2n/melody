<template>
  <div :class="className" :style="{ height: height, width: width }" />
</template>

<script>
import echarts from 'echarts'
require('echarts/theme/macarons') // echarts theme
import { debounce } from '@/utils'

export default {
  props: {
    className: {
      type: String,
      default: 'chart'
    },
    width: {
      type: String,
      default: '100%'
    },
    height: {
      type: String,
      default: '350px'
    },
    autoResize: {
      type: Boolean,
      default: true
    },
    chartData: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      chart: null,
      sidebarElm: null
    }
  },
  watch: {
    chartData: {
      deep: true,
      handler(val) {
        this.setOptions(val)
      }
    }
  },
  mounted() {
    this.initChart()
    if (this.autoResize) {
      this.__resizeHandler = debounce(() => {
        if (this.chart) {
          this.chart.resize()
        }
      }, 100)
      window.addEventListener('resize', this.__resizeHandler)
    }

    // 监听侧边栏的变化
    this.sidebarElm = document.getElementsByClassName('sidebar-container')[0]
    this.sidebarElm && this.sidebarElm.addEventListener('transitionend', this.sidebarResizeHandler)
  },
  beforeDestroy() {
    if (!this.chart) {
      return
    }
    if (this.autoResize) {
      window.removeEventListener('resize', this.__resizeHandler)
    }

    this.sidebarElm && this.sidebarElm.removeEventListener('transitionend', this.sidebarResizeHandler)

    this.chart.dispose()
    this.chart = null
  },
  methods: {
    sidebarResizeHandler(e) {
      if (e.propertyName === 'width') {
        this.__resizeHandler()
      }
    },
    setDefaultOptions() {
      this.chart.setOption({
        // 直角坐标系中横轴数组（详见xAxis），数组中每一项代表一条横轴坐标轴，标准（1.0）中规定最多同时存在2条横轴
        xAxis: {
          data: [],
          boundaryGap: false,
          axisTick: {
            show: false
          }
        },
        series: [],
        // 直角坐标系内绘图网格（详见grid）
        grid: {
          left: 10,
          right: 10,
          bottom: 20,
          top: 30,
          containLabel: true
        },
        // 提示框（详见tooltip），鼠标悬浮交互时的信息提示
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross'
          },
          padding: [5, 10]
        },
        // 直角坐标系中纵轴数组（详见yAxis），数组中每一项代表一条纵轴坐标轴，标准（1.0）中规定最多同时存在2条纵轴
        yAxis: [{
          type: 'value'
        }],

        // 图例（详见legend），每个图表最多仅有一个图例，混搭图表共享
        legend: {
          data: ['评论数', '访客数', '浏览次数', '文章数']
        }
      }, true)
    },
    setOptions(option) {
      this.setDefaultOptions()
      this.chart.setOption(option)
    },
    initChart() {
      this.chart = echarts.init(this.$el, 'macarons')
      this.setDefaultOptions()
      this.setOptions(this.chartData)
    }
  }
}
</script>

