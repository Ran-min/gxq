<template>
	<Layout>
		<Content>
			<Breadcrumb>
				<BreadcrumbItem>预警分析</BreadcrumbItem>
				<BreadcrumbItem>部门人员预警分析</BreadcrumbItem>
			</Breadcrumb>
			<Card style="margin-bottom: 20px;">
				<Form :model='formData' inline :label-width="100">
					<FormItem label="时间">
						<DatePicker v-model="defaultDate" type="year" placeholder="选择时间" :options="pickerOptions" style="width: 200px"></DatePicker>
					</FormItem>
					<FormItem label="单位">
						<Select
              v-model="formData.unitId"
              style="width:180px"
              @on-change="getDepartData">
							<Option
                v-for="(item, index) in unitList"
                :key="index"
                :value="item.id">
                {{item.name}}
              </Option>
						</Select>
					</FormItem>
					<FormItem label="部门">
						<Select v-model="formData.departmentId" style="width:180px">
							<Option v-for="(item, index) in departmentList" :key="index" :value="item.id">
								{{item.name}}
							</Option>
						</Select>
						<Button v-if="checkButton('supervise_Analysis_department_person_view')" type="primary" @click="getPersonData">查看分析</Button>
					</FormItem>
				</Form>
			</Card>
			<Layout style="background:#fff;">
				<carouseList :data='perList' :arrowType='arrowType'></carouseList>
				<Layout>
					<Col span="14"></Col>
					<Col span="9"></Col>
					<Row type="flex" justify="space-between">
						<Col style="width: 49%">
						<chart-card title="各人员预警状态">
							<perWarnState ref="perWarnState" />
						</chart-card>
						</Col>
						<Col style="width: 49%">
						<chart-card title="各人员预警类别情况">
							<per-category ref="perCategorye" />
						</chart-card>
						</Col>
					</Row>
					<Row type="flex" justify="space-between">
						<Col style="width: 49%">
						<chart-card title="各人员预警处理对比分析">
							<per-manage ref="perManageData" />
						</chart-card>
						</Col>
						<Col style="width: 49%">
						<chart-card title="各人员预警级别情况">
							<per-level ref="perLevel" />
						</chart-card>
						</Col>
					</Row>
				</Layout>
			</Layout>
		</Content>
	</Layout>
</template>

<script>
import api from '@/api/axiosApi'
import superviseApiList from '@/api/superviseApiList'

// echart图外层容器，包括卡片样式
import chartCard from './echarts/chartCard.vue'
import countUp from '@/components/hengyun/countUp.vue'
// 各人员预警状态
import perWarnState from './echarts/personnelWarnState.vue'
// 各人员预警类别情况
import perCategory from './echarts/personnelCategory.vue'
// 各人员预警处理对比分析
import perManage from './echarts/personnelManage.vue'
// 各人员预警级别情况
import perLevel from './echarts/personnelLevel.vue'
// 顶部滑动列表模块
import carouseList from "./modal/carouseList.vue"
import { mapState } from 'vuex'
// 保存所有人员的信息
const allPersonList = new Map()
export default {
  components: {
    'chart-card': chartCard,
    countUp,
    perWarnState,
    carouseList,
    'per-category': perCategory,
    'per-manage': perManage,
    'per-level': perLevel
  },
  data() {
    return{
      arrowType: 'never',
      // 所有单位和部门
      unitList: [], // 单位列表
      defaultDate: new Date(), // 获取今年年份
      empIds:[], // 初始化人员集合
      departmentList:[],//部门列表
      formData: {
        // 单位id
        unitId: '',
        // 部门id
        departmentId: ''
      },
      // 人员滑动列表
      perList: [],
      pickerOptions: {
        // disabledDate是一个函数,参数是当前选中的日期值,这个函数需要返回一个Boolean值,
        disabledDate: (year) => {
          var myYear = year.getFullYear()
          var myDate = new Date();
          // 如果函数处理比较简单,可以直接在这里写逻辑方法
          return !(myYear <= myDate.getFullYear() && myYear >= 1900)
        //return this.dealDisabledDate(time)
        }
      } // 日期设置对象
    }
  },
  computed: {
    year () {
      return this.defaultDate ? this.defaultDate.getFullYear().toString() : ''
    },
    ...mapState([
      'authButton'
    ])
  },
  mounted(){
    //初始化
    this.getUnitData()
  },
  methods:{
    systemCode () {
      for (let unit of this.unitList) {
        if (unit.id === this.formData.unitId) {
          return unit.systemCode
        }
      }
      return ''
   },
    // 获取所有单位
    getUnitData() {
      const vm = this;
      api(superviseApiList.findOrgByUser).then((res) => {
        if(res.data.errcode == 0) {
          const unitList = res.data.data;
          vm.formData.unitId = unitList[0].id
          vm.getDepartData();
          vm.unitList = unitList
        } else {
          this.$Message.error(res.data.errmsg);
        }
      }, (error) => {})
    },
    getDepartData(isOnChange){//获取所有部门
      const vm = this
      api(superviseApiList.findDpmByUserAndOrg,{orgId:vm.formData.unitId}).then((res) => {
          if(res.data.errcode == 0) {
            vm.departmentList = res.data.data;
            vm.formData.departmentId = vm.departmentList[0].id
            vm.$nextTick(() => {
            if (vm.formData.departmentId && !isOnChange) {
                vm.getPersonData()
              }
            })
          } else {
            vm.$Message.error(res.data.errmsg);
          }
      });
    },
    // 获取所有人员的信息
    getPersonData() {
      console.log(this.formData);
      const vm = this
      api(superviseApiList.findEmpByDpm, {
        dpmId: vm.formData.departmentId,
        systemCode: vm.systemCode()
      }).then((res) => {
        if(res.data.errcode == 0) {
          let empIds = []
          allPersonList.clear()
          res.data.data.map((data, idx) => {
            empIds.push(data.id)
            // 往map插入人员信息数据，后面获取数字之后有用
            allPersonList.set(data.id, data)
          })
          vm.empIds = empIds
          // 获取滑块的数据
          vm.getPerCount()
          // 获取人员预警状态
          vm.getFindPublicSuperviseStatus()
          // 各人员预警类别情况
          vm.getEmpTypeList()
          // 各人员预警级别情况
          vm.getEmpLevelList()
          // 各人员预警处理时长对比分析
          vm.getFindPublicHandle()
        } else {
          this.$Message.error(res.data.errmsg);
        }
      }, (error) => {})
    },
    // 获取每个应用对应的数字
    getPerCount() {
      const vm = this
      api(superviseApiList.findSurvey,{
        empIds: vm.empIds,
        empDpmId: vm.formData.departmentId,
        date: vm.year
      }).then((res) => {
        if(res.data.errcode == 0) {
          // 两层数组的里面一层，满8个就push一次
          let subArr = []
          const perListTemp = []
          // 遍历所有的应用信息
          res.data.data.map((data, idx) => {
            subArr.push({
              color: "#F7645D",
              idName: data.name,
              count: data.num,
              countSize: "#50",
              introText: data.name,
              link: `/personnelAnalyzeView/${data.id}/${vm.formData.departmentId}`
            })
            allPersonList.delete(data.id)
            if(subArr.length === 8) {
              perListTemp.push(subArr)
              subArr = []
            }
          })
          // 遍历剩余没有数字的人员
          let size = allPersonList.size
          allPersonList.forEach((value, key, map) => {
            subArr.push({
              color: "#F7645D",
              idName: value.name,
              count: 0,
              countSize: "#50",
              introText: value.name
            })
            size--
            if(subArr.length === 8 || size === 0) {
              perListTemp.push(subArr)
              subArr = []
            }
          })
          // 如果没有为0的就需要判断这个条件
          if (subArr.length > 0) {
            perListTemp.push(subArr)
          }
          vm.perList = perListTemp
        } else {
          this.$Message.error(res.data.errmsg);
        }
      }, (error) => {})
    },
    // 获取所有人员关联未处理预警的数量
    getFindSurvey(){
      let data = {},currData=[];
      api(superviseApiList.findSurvey, {
        empIds: vm.empIds,
        date: vm.year
      }).then((res) => {
        if(res.data.errcode == 0) {
          for(var i = 0;i<res.data.data.length;i++){
            //整理参数
            res.data.data[i].color = "#F7645D";
            res.data.data[i].idName = res.data.data[i].id;
            res.data.data[i].count = res.data.data[i].num;
            res.data.data[i].countSize = "#50";
            res.data.data[i].introText = res.data.data[i].name;
            currData.push(res.data.data[i]);
            if((i != 0 && (i + 1) % 8 == 0) || i == res.data.data.length - 1) {
              this.unitList.push(currData);
              currData = [];
            }
          }
        } else {
          this.$Message.error(res.data.errmsg);
        }
      }, (error) => {})
    },
    // 各人员预警状态
    getFindPublicSuperviseStatus() {
      const vm = this
      api(superviseApiList.findPublicSuperviseStatus, {
        empIds: vm.empIds,
        date: vm.year
      }).then((res) => {
        if(res.data.errcode == 0) {
          vm.$refs.perWarnState.refresh(res.data.data)
        }else{
          this.$Message.error(res.data.errmsg);
        }
      }, (error) => {})
    },
    // 各人员预警类别情况
    getEmpTypeList() {
      const vm = this
      api(superviseApiList.empTypeList, {
        depId: vm.formData.departmentId,
        ids: vm.empIds,
        date: vm.year
      }).then((res) => {
        if(res.data.errcode == 0) {
          vm.$refs.perCategorye.refresh(res.data.data)
        } else {
          this.$Message.error(res.data.errmsg);
        }
      }, (error) => {})
    },
    // 各人员预警处理对比分析
    getFindPublicHandle() {
      const vm = this
      api(superviseApiList.findPublicHandle, {
        empIds: vm.empIds,
        date: vm.year
      }).then((res) => {
        if(res.data.errcode == 0) {
          vm.$refs.perManageData.refresh(res.data.data)
        } else {
          this.$Message.error(res.data.errmsg);
        }
      }, (error) => {})
    },
    // 各人员预警级别情况
    getEmpLevelList() {
      const vm = this
      api(superviseApiList.empLevelList, {
        depId: vm.formData.departmentId,
        ids: vm.empIds,
        date: vm.year
      }).then((res) => {
        if(res.data.errcode == 0) {
          vm.$refs.perLevel.refresh(res.data.data)
        }else{
          this.$Message.error(res.data.errmsg);
        }
      }, (error) => {})
    },
    checkButton(code) {
			if(this.authButton.indexOf(code) >= 0) {
				return true;
			} else {
				return false;
			}
		},
  },
}
</script>

<style lang="less" scoped>
	.radioMod {
		width: 80%;
		height: 100%;
		padding: 10px 0;
		border: 1px solid #ccc;
		border-radius: 50%;
		margin: 30px auto;
	}

	.mf {
		margin-left: 2%;
	}

	.radioTxt {
		width: 100px;
		height: 100px;
		margin: 35px auto;
		font-size: 20px;
		text-align: center
	}

	.card-title {
		text-align: center;
		background-color: #888;
	}
</style>
