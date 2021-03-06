<template>
  <Layout>
    <Content>
      <Breadcrumb>
        <BreadcrumbItem>运维文档管理</BreadcrumbItem>
        <BreadcrumbItem>文档分类管理</BreadcrumbItem>
      </Breadcrumb>
      <Card>
        <Row>
          <Col span="24">
            <Form
              ref="formValidate"
              inline
              :label-width="80"
              :model="searchData.name">
              <FormItem label="文档分类：">
                <Input
                  type="text"
                  v-model="searchData.name">
                </Input>
              </FormItem>
              <FormItem :label-width="20">
                <Button type="primary" @click="getSearchTree">查询</Button>
              </FormItem>
            </Form>
          </Col>
        </Row>
        <Row>
          <Col span="6">
            <Tree :data="ztreeData" :render="renderContent" id="docClassify"></Tree>
          </Col>
          <Col span="16">
          <Form ref="formValidate" :label-width="100">
            <FormItem label="分类名称：">
              <Row>
                <Col span="24">
                  <Input :value="currentNodeDetail.name" disabled></Input>
                </Col>
              </Row>
            </FormItem>
            <!-- <FormItem label="创建时间：">
              <Row>
                <Col span="24">
                  <DatePicker type="date" disabled></DatePicker>
                </Col>
              </Row>
            </FormItem> -->
            <FormItem label="分类描述：">
              <Row>
                <Col span="24">
                  <Input
                    type="textarea"
                    :rows="5"
                    disabled
                    :value="currentNodeDetail.description"
                    :maxlength="520"></Input>
                </Col>
              </Row>
            </FormItem>
          </Form>
          </Col>
        </Row>
      </Card>
      <Modal v-model="modal" :title="actTitle" :mask-closable="false">
        <Form ref="modalForm" inline :label-width="90" :model="modalFormData" :rules="ruleValidate">
          <FormItem label="分类名称" prop="name">
            <Input type="text" v-model="modalFormData.name" style="width:150px;"></Input>
          </FormItem>
          <FormItem label="新增类型" v-if="currentAct === actTypes.create">
            <Select v-model="modalFormData.type" style="width:150px;">
              <Option value="1">--新增子类--</Option>
              <Option value="2" :disabled="currentNodeInfo.parentId == '-1'">--新增类--</Option>
            </Select>
          </FormItem>
          <FormItem label="分类描述" style="width: 100%;">
            <Input
              type="textarea"
              v-model="modalFormData.description"
              :rows="5"
              :maxlength="520"
              :autosize="{minRows: 5, maxRows: 5}">
            </Input>
          </FormItem>
        </Form>
        <div slot="footer">
          <Button class="modalBtn" type="default" @click="modal = false" size="large">关闭</Button>
          <Button class="modalBtn" type="primary" @click="saveOrUpdate" size="large">确定</Button>
        </div>
      </Modal>
    </Content>
  </Layout>
</template>

<script>
import {mapState} from 'vuex'
import api from '@/api/axiosApi'
import {changeTreeData} from '@/assets/js/utils'
import operationApiList from '@/api/operationApiList'

// 操作类型
const actTypes = {
  edit: 'edit',
  create: 'create'
}

export default {
  data() {
    return {
      searchData: {
        name: ''
      },
      // 树的所有节点数组
      treeRoot: [],
      // 当前操作的节点对象，包含父节点的nodekey
      currentNode: {},
      actTypes,
      modal: false,
      // 当前操作的类型
      currentAct: 'edit',
      // 当前操作的节点的信息
      currentNodeInfo: {
        // 自生的id
        id: '',
        // 父节点的id，如果没有父节点就为-1
        parentId: ''
      },
      // 当前操作节点的详细信息
      currentNodeDetail: {
        name: '',
        description: ''
      },
      // 新增或者编辑时的表单
      modalFormData: {
        name: '',
        type: '1',
        description: ''
      },
      ruleValidate: {
        name: [{required: true, message: '不可为空', trigger: 'blur'}]
      },
      defaultMsg: '',
      config: {
        initialFrameWidth: null,
        initialFrameHeight: 350
      },
      displayType: "none",
      ztreeData: [],
      buttonProps: {
        type: 'ghost',
        size: 'small',
      }
    }
  },
  computed: {
    actTitle () {
      return this.currentAct === actTypes.edit ? '编辑分类' : '新增分类'
    },
    saveOrUpdateUrl() {
      return this.currentAct === actTypes.create ? 'classifySave' : 'classifyUpdate'
    },
    // 按钮权限列表
    ...mapState(['authButton'])
  },
  methods: {
    renderContent(h, { root, node, data }) {
      const vm = this
      // 添加按钮
      const createBtn = h('Button', {
        props: {
          icon: 'plus',
          ...this.buttonProps
        },
        style: {
          marginRight: '8px',
          color: 'green'
        },
        on: {
          click: () => {
            this.treeRoot = root
            this.currentNode = node
            this.modalFormData.name = ''
            this.modalFormData.description = ''
            this.currentNodeInfo = data
            this.currentAct = actTypes.create
            this.modal = true
          }
        }
      })
      // 编辑按钮
      const editBtn = h('Button', {
        props: {
          icon: 'edit',
          ...this.buttonProps
        },
        style: {
          marginRight: '8px',
          color: 'blue'
        },
        on: {
          click: () => {
            this.treeRoot = root
            this.currentNode = node
            this.currentNodeInfo = data
            this.currentAct = actTypes.edit
            this.modal = true
            this.getClassifyDetail()
          }
        }
      })
      // 删除按钮
      const deleteBtn = h('Button', {
        props: {
          icon: 'close',
          ...this.buttonProps
        },
        style: {
          marginRight: '8px',
          color: 'red'
        },
        on: {
          click: () => {
            vm.$Modal.confirm({
              title: '删除确认',
              content: '确认删除吗？',
              onOk: () => {
                api(operationApiList.isRelevanceDocument, {
                  classifyId: data.id
                }).then(res => {
                  if (res.data.errcode === 0 && !res.data.data) {
                    api(operationApiList.classifyDelete, {
                      id: data.id
                    }).then(res => {
                      if (res.data.errcode === 0) {
                        vm.$Message.success('删除成功！')
                        changeTreeData({
                          root,
                          node,
                          data,
                          type: 'delete'
                        })
                      }
                    }, error => {console.log(error)})
                  } else {
                    vm.$Message.info('有关联文档，无法删除！')
                  }
                }, error => {console.log(error)})
              }
            })
            this.currentNodeInfo = data
          }
        }
      })
      const btns = []
      if (vm.authButton.includes('ops_file_type_manage_add')) {
        btns.push(createBtn)
      }
      if (vm.authButton.includes('ops_file_type_manage_update')) {
        btns.push(editBtn)
      }
      // 如果有子节点，那就不能删除
      if (!data.children || data.children.length === 0 && data.parentId != '-1') {
        if (vm.authButton.includes('ops_file_type_manage_delete')) {
          btns.push(deleteBtn)
        }
      }
      // 有none这个字段就不显示
      if (data.none) {
        return h('span', {
          style: {
            display: 'none'
          }
        }, '')
      }
      return h('span', {
        attrs: {
          class: 'doc-tree-title'
        }
      }, [
        h('span', {
          on: {
            click: () => {
              // 点击文字也可以展开树结构
              Vue.set(data, 'expand', !data.expand)
              this.currentNodeInfo = data
              this.getClassifyDetail()
            }
          },
          attrs: {
            class: 'title'
          }
        }, data.title),
        h('span', {
          attrs: {
            class: 'act-btns-container'
          }
        }, btns)
      ])
    },
    // 关键字查询
    getSearchTree() {
      const vm = this
      api(operationApiList.getClassifyTree, {treeId: '751514420097', name: vm.searchData.name})
      .then(res => {
        if (res.data.errcode === 0) {
          res.data.data[0].expand = true
          vm.ztreeData = JSON.parse(JSON.stringify(res.data.data).replace(/name/g, 'title'))
        }
      }, error => {console.log(error)})
    },
    // 获取树结构,isAct代表是不是操作之后的刷新
    getTreeData(isAct, node) {
      // node: 新增的node的信息
      const vm = this
      api(operationApiList.getClassifyTree, {treeId: '751514420097', name: vm.searchData.name})
      .then(res => {
        if (res.data.errcode === 0) {
          res.data.data[0].expand = true
          // 操作--新增
          if (isAct) {
            const opts = {
              root: vm.treeRoot,
              node: vm.currentNode,
              data: vm.currentNodeInfo,
              nodeInfo: node
            }
            // 新增子类
            if (vm.modalFormData.type === '1') {
              opts.type = 'createChild'
            } else {
              opts.type = 'create'
            }
            changeTreeData(opts)
          } else {
            this.ztreeData = JSON.parse(JSON.stringify(res.data.data).replace(/name/g, 'title'))
          }
        }
      }, error => {console.log(error)})
    },
    // 保存或者更新节点信息
    saveOrUpdate() {
      // 此处缺少表单验证
      const vm = this
      vm.$refs.modalForm.validate(valid => {
        if (valid) {
          const {parentId, id} = {...vm.currentNodeInfo}
          let data = {
            ...vm.modalFormData
          }
          // 编辑
          if (vm.currentAct === actTypes.edit) {
            data.id = id
          } else {
            // 新增子类
            if (vm.modalFormData.type === '1') {
              data.parentId = id
            } else {
              data.parentId = parentId
            }
          }
          api(operationApiList[vm.saveOrUpdateUrl], data)
          .then(res => {
            if (res.data.errcode === 0) {
              // 编辑
              if (vm.currentAct === actTypes.edit) {
                changeTreeData({
                  root: vm.treeRoot,
                  node: vm.currentNode,
                  data: vm.currentNodeInfo,
                  nodeInfo: vm.modalFormData,
                  type: 'edit'
                })
              } else {
                vm.getTreeData(true, res.data.data)
              }
              vm.modal = false
            }
          }, error => {console.log(error)})
        } else {
          vm.$Message.warning('表单验证失败！')
        }
      })
    },
    // 获取文档分类的详细信息
    getClassifyDetail(node) {
      const vm = this
      api(operationApiList.getClassifyDetail, {
        id: vm.currentNodeInfo.id
      }).then(res => {
        if (res.data.errcode === 0) {
          vm.modalFormData.description = res.data.data.description
          vm.modalFormData.name = res.data.data.name
          vm.currentNodeDetail.description = res.data.data.description
          vm.currentNodeDetail.name = res.data.data.name
        }
      }, error => {console.log(error)})
    }
  },
  mounted() {
    this.getTreeData()
  }
}
</script>

<style type="text/css" scoped>
.w168 {
  width: 168px;
}
</style>

<style lang="less">
#docClassify{
  .doc-tree-title{
    display: inline-block;
    width: 100%;
    vertical-align: middle;
    .title{
      display: inline-block;
      height: 24px;
      float: left;
      line-height: 24px;
      cursor: pointer;
    }
    .act-btns-container{
      display: none;
      margin-left: 10px;
    }
    &:hover{
      .act-btns-container{
        display: inline-block;
      }
    }
  }
}

</style>
