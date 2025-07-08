<template>
  <div class="chart-wrapper" ref="chartContainer">
    <BarChart 
      v-if="isChartDataValid"
      :chart-data="safeChartData" 
      :chart-options="chartOptions"
      ref="chart"
    />
    <div v-else class="chart-error">
      <p>图表数据无效，无法显示</p>
    </div>
  </div>
</template>

<script>
import { Bar as BarChart } from 'vue-chartjs'

export default {
  name: 'ClickableBarChart',
  components: {
    BarChart
  },
  props: {
    chartData: {
      type: Object,
      required: true,
      default: () => ({
        labels: [],
        datasets: []
      })
    },
    chartOptions: {
      type: Object,
      required: true,
      default: () => ({})
    },
    imageIds: {
      type: Array,
      required: true,
      default: () => []
    }
  },
  data() {
    return {
      safeChartData: {
        labels: [],
        datasets: []
      },
      isChartDataValid: false
    }
  },
  watch: {
    chartData: {
      handler(newData) {
        console.log('ClickableBarChart接收到新的chartData:', newData)
        // 确保数据安全
        if (newData && newData.labels && newData.datasets) {
          this.safeChartData = {
            labels: Array.isArray(newData.labels) ? newData.labels : [],
            datasets: Array.isArray(newData.datasets) ? newData.datasets : []
          }
          this.isChartDataValid = true
        } else {
          this.safeChartData = {
            labels: [],
            datasets: []
          }
          this.isChartDataValid = false
        }
        console.log('ClickableBarChart处理后的safeChartData:', this.safeChartData)
        console.log('isChartDataValid:', this.isChartDataValid)
      },
      immediate: true,
      deep: true
    }
  },
  mounted() {
    console.log('ClickableBarChart组件挂载')
    this.setupClickHandler()
  },
  methods: {
    setupClickHandler() {
      const chart = this.$refs.chart
      if (chart && chart.chart) {
        chart.chart.options.onClick = (event, elements) => {
          if (elements.length > 0) {
            const index = elements[0].index
            const imageId = this.imageIds && this.imageIds[index]
            if (imageId) {
              this.$emit('image-click', imageId)
            }
          }
        }
        chart.chart.update()
      }
    }
  }
}
</script>

<style scoped>
.chart-wrapper {
  position: relative;
  cursor: pointer;
}

.chart-error {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 400px;
  background: #f8f9fa;
  border-radius: 10px;
  color: #6c757d;
  font-size: 1.1rem;
}
</style> 