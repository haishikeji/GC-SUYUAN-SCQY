<template>
   <a-spin :spinning="confirmLoading">
     <j-form-container :disabled="formDisabled" v-show="false">
       <!-- 主表单区域 -->
       <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
         <a-row>
          <a-col :xs="24" :sm="12">
            <a-form-model-item label="采购订单号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="purchaseOrderNumber">
              <a-input v-model="model.purchaseOrderNumber" placeholder="请输入采购订单号" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :xs="24" :sm="12">
            <a-form-model-item label="供应商" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="supplier">
              <a-input v-model="model.supplier" placeholder="请输入供应商" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :xs="24" :sm="12">
            <a-form-model-item label="仓库名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="warehouseName">
              <a-input v-model="model.warehouseName" placeholder="请输入仓库名称" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :xs="24" :sm="12">
            <a-form-model-item label="订单类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="orderType">
              <a-input v-model="model.orderType" placeholder="请输入订单类型" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :xs="24" :sm="12">
            <a-form-model-item label="采购单条码" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="barCode">
              <a-input v-model="model.barCode" placeholder="请输入采购单条码" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :xs="24" :sm="12">
            <a-form-model-item label="操作员" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="createBy">
              <a-input v-model="model.createBy" placeholder="请输入操作员" disabled></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :xs="24" :sm="12">
            <a-form-model-item label="创建时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="createTime">
              <j-date placeholder="请选择创建时间" v-model="model.createTime" :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" style="width: 100%" disabled/>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
     </j-form-container>

      <!-- 子表单区域 -->
      <a-tabs v-model="activeKey" @change="handleChangeTabs">
        <a-tab-pane tab="溯源码" :key="refKeys[0]" :forceRender="true">
          <j-editable-table
            :alwaysEdit="false"
            @testClick="testClick"
            v-if="adminRscodeTable.columns[0].options.length!=0"
            :ref="refKeys[0]"
            :loading="adminRscodeTable.loading"
            :columns="adminRscodeTable.columns"
            :dataSource="adminRscodeTable.dataSource"
            :maxHeight="300"
            :disabled="formDisabled"
            :rowNumber="true"
            :rowSelection="true"
            :actionButton="true"/>
        </a-tab-pane>
      </a-tabs>
    </a-spin>
</template>

<script>

  import { FormTypes,getRefPromise,VALIDATE_NO_PASSED } from '@/utils/JEditableTableUtil'
  import { JEditableTableModelMixin } from '@/mixins/JEditableTableModelMixin'
  import { validateDuplicateValue } from '@/utils/util'
  import { httpAction } from '@api/manage'

  export default {
    name: 'AdminGodownEntryForm3',
    mixins: [JEditableTableModelMixin],
    components: {
    },
    data() {
      return {
        test:[],
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        model:{
        },
        validatorRules: {
           purchaseOrderNumber: [
              { required: true, message: '请输入采购订单号!'},
              { validator: (rule, value, callback) => validateDuplicateValue('admin_godown_entry', 'purchase_order_number', value, this.model.id, callback)},
           ],
        },
        // 新增时子表默认添加几行空数据
        addDefaultRowNum: 1,
        refKeys: ['adminRscode', ],
        tableKeys:['adminRscode', ],
        activeKey: 'adminRscode',
        // 溯源码
        // 溯源码
        adminRscodeTable: {
          loading: false,
          dataSource: [],
          columns: [
            {
              title: '商品名称',
              key: 'name',
              type: FormTypes.sel_search,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
              // options:[{id:1,value:"s",text:"1",commodityJson:"1"}]
              options:[]
            },
            {
              title: '商品代码',
              key: 'code',
              type: FormTypes.hidden,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '商品json',
              key: 'commodityJson',
              type: FormTypes.hidden,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '溯源码',
              key: 'retrospectiveSourceCode',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
          ]
        },
        // adminRscodeTable: {
        //   loading: false,
        //   dataSource: [],
        //   columns: [
        //     {
        //       title: '商品名称',
        //       key: 'name',
        //       type: FormTypes.input,
        //       width:"200px",
        //       placeholder: '请输入${title}',
        //       defaultValue:'',
        //     },
        //     {
        //       title: '商品代码',
        //       key: 'code',
        //       type: FormTypes.input,
        //       width:"200px",
        //       placeholder: '请输入${title}',
        //       defaultValue:'',
        //     },
        //     {
        //       title: '溯源码',
        //       key: 'retrospectiveSourceCode',
        //       type: FormTypes.input,
        //       width:"200px",
        //       placeholder: '请输入${title}',
        //       defaultValue:'',
        //     },
        //   ]
        // },
        url: {
          add: "/admincasehistory/adminGodownEntry/add",
          edit: "/admincasehistory/adminGodownEntry/edit",
          list: "/commodity/adminCommodity/list2",
          adminRscode: {
            list: '/admincasehistory/adminGodownEntry/queryAdminRscodeByMainId'
          },
        }
      }
    },
    props: {
      //表单禁用
      disabled: {
        type: Boolean,
        default: false,
        required: false
      }
    },
    computed: {
      formDisabled(){
        return this.disabled
      },
    },
    created () {
      httpAction(this.url.list, {  }, 'get').then((res) => {
        if (res.success) {
          this.adminRscodeTable.columns[0].options =res.result
          // this.$message.success(res.message)
          // this.$emit('ok')
          // this.loadData()
        } else {
          this.$message.warning(res.message)
        }
      })
    },
    methods: {
      testClick(e){
        this.test=e
      },
     addBefore(){
            this.adminRscodeTable.dataSource=[]
      },
      getAllTable() {
        let values = this.tableKeys.map(key => getRefPromise(this, key))
        return Promise.all(values)
      },
      /** 调用完edit()方法之后会自动调用此方法 */
      editAfter() {
        this.$nextTick(() => {
        })
        // 加载子表数据
        if (this.model.id) {
          let params = { id: this.model.id }
          this.requestSubTableData(this.url.adminRscode.list, params, this.adminRscodeTable)
        }
      },
      //校验所有一对一子表表单
    validateSubForm(allValues){
        return new Promise((resolve,reject)=>{
          Promise.all([
          ]).then(() => {
            resolve(allValues)
          }).catch(e => {
            if (e.error === VALIDATE_NO_PASSED) {
              // 如果有未通过表单验证的子表，就自动跳转到它所在的tab
              this.activeKey = e.index == null ? this.activeKey : this.refKeys[e.index]
            } else {
              console.error(e)
            }
          })
        })
    },
      /** 整理成formData */
      classifyIntoFormData(allValues) {
        let main = Object.assign(this.model, allValues.formValue)

        return {
          ...main, // 展开
          adminRscodeList: allValues.tablesValue[0].values,
        }
      },
      validateError(msg){
        this.$message.error(msg)
      },
     close() {
        this.visible = false
        this.$emit('close')
        this.$refs.form.clearValidate();
      },
    }
  }
</script>

<style scoped>
</style>