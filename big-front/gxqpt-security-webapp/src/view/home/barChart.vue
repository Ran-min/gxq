<!-- 线图 -->
<template>
  <div>
    <slot></slot>
  </div>
</template>
<script>
export default {
  methods: {
    refresh (opts) {
      const vm = this
      const series = []
      if (opts.series) {
        opts.series.map(item => {
          series.push({
            type: 'bar',
            barWidth: '30%', //柱子宽度
            barGap: '30%', //柱子之间间距
            itemStyle: {
              normal: {
                opacity: 1
              }
            },
            ...item
          })
        })
      }
      const option = {
        color: opts.color || ['#F19A9A', '#9FDFA1', '#85C8F6'],
        title: {
          ...(opts.title || {})
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: { // 坐标轴指示器，坐标轴触发有效
            type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
          },
          ...(opts.tooltip || {})
        },
        legend: {
          align: 'right',
          right: 10,
          textStyle: {
            color: "#333"
          },
          itemWidth: 10,
          itemHeight: 10,
          itemGap: 35,
          ...(opts.legend || {})
        },
        grid: {
          left: '60px',
          right: '60px',
          bottom: '60px',
          containLabel: true,
          ...(opts.grid || {})
        },
        xAxis: {
          type: 'category',
          axisLine: {
            show: true,
            lineStyle: {
              width: 1,
              type: "solid"
            }
          },
          axisTick: {
            show: false,
          },
          axisLabel: {
            show: true,
            textStyle: {
              color: "#00c7ff",
            }
          },
          ...opts.xAxis
        },
        yAxis: {
          type: 'value',
          axisTick: {
            show: false,
          },
          axisLine: {
            show: true,
            lineStyle: {
              width: 1,
              type: "solid"
            }
          },
          ...(opts.yAxis || {})
        },
        series: series
      }
      const bar = vm.$echarts.init(document.getElementById(opts.el))
      bar.setOption(option)
    }
  }
}
</script>
