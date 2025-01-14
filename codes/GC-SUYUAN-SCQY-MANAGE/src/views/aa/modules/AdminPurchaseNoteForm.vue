<template>
   <a-spin :spinning="confirmLoading">
     <j-form-container :disabled="formDisabled">
       <!-- 主表单区域 -->
       <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
         <a-row>
          <a-col :xs="24" :sm="12">
            <a-form-model-item label="销售订单号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="marketOrderNumber">
              <a-input v-model="model.marketOrderNumber" placeholder="请输入销售订单号" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :xs="24" :sm="12">
            <a-form-model-item label="收件人" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="recipients">
              <a-input v-model="model.recipients" placeholder="请输入收件人" ></a-input>
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
            <a-form-model-item label="销售单条码" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="barCode">
              <a-input v-model="model.barCode" placeholder="请输入销售单条码" ></a-input>
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
        <a-tab-pane tab="出库溯源码" :key="refKeys[0]" :forceRender="true">
          <j-editable-table
            :zj="false"
            :ref="refKeys[0]"
            :loading="adminRscode2Table.loading"
            :columns="adminRscode2Table.columns"
            :dataSource="adminRscode2Table.dataSource"
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

  export default {
    name: 'AdminPurchaseNoteForm',
    mixins: [JEditableTableModelMixin],
    components: {
    },
    data() {
      return {
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
           marketOrderNumber: [
              { required: true, message: '请输入销售订单号!'},
              { validator: (rule, value, callback) => validateDuplicateValue('admin_purchase_note', 'market_order_number', value, this.model.id, callback)},
           ],
        },
        // 新增时子表默认添加几行空数据
        addDefaultRowNum: 1,
        refKeys: ['adminRscode2', ],
        tableKeys:['adminRscode2', ],
        activeKey: 'adminRscode2',
        // 出库溯源码
        adminRscode2Table: {
          loading: false,
          dataSource: [],
          columns: [
            {
              title: '商品名称',
              key: 'name',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '商品代码',
              width:"200px",
              key: 'code',
              type: FormTypes.input,
            },
            {
              title: '商品条码',
              width:"200px",
              key: 'barcode',
              type: FormTypes.input,
            },
            {
              title: '溯源码',
              key: 'retrospectiveSourceCode',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '出库时间',
              width:"200px",
              key: 'createTime',
              type: FormTypes.input,
            },
          ]
        },
        url: {
          add: "/adminpurchasenote/adminPurchaseNote/add",
          edit: "/adminpurchasenote/adminPurchaseNote/edit",
          adminRscode2: {
            list: '/adminpurchasenote/adminPurchaseNote/queryAdminRscode2ByMainId'
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
    },
    methods: {
     addBefore(){
            this.adminRscode2Table.dataSource=[]
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
          this.requestSubTableData(this.url.adminRscode2.list, params, this.adminRscode2Table)
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
          adminRscode2List: allValues.tablesValue[0].values,
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