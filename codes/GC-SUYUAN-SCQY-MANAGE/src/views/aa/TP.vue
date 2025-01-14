<template>
  <a-card :bordered="false" style="height:100%;padding-bottom:200px; ">

    <div class="table-page-search-wrapper">
      <a-form-model ref="form" layout="inline">


          <a-col :span="24">
            <a-form-model-item label="首页图片" >
              <j-image-upload v-model="tp" style="width: 100px;height: 120px"></j-image-upload>
            </a-form-model-item>
          </a-col>

        <br>
        <br>
        <br>
        <br>
        <br>

        <a-col :span="24">
          <a-form-model-item label="隐私协议" style="min-height: 300px" prop="jEditor">
            <j-editor v-model="jeditor"/>
          </a-form-model-item>
        </a-col>

        <a-col :span="24">
          <a-form-model-item label="业务员多少时间内没有更新跟进状态通知(分钟)" style="min-height: 100px">
            <a-input-number v-model="ywymygx" placeholder="设定时间(分钟)" style="width: 30%" />
          </a-form-model-item>
        </a-col>
        <a-button type="primary" @click='subButt()'>保存设置</a-button>

      </a-form-model>

    </div>

  </a-card>
</template>

<script>

  import { httpAction } from '@api/manage'
  import JEditor from '@/components/jeecg/JEditor'

  export default {
    name: 'tp',
    inject:['closeCurrent'],
    components: {
      JEditor
    },
    data() {
      return {

        selectList: [],
        selectedDepUsers: '',
        tp: '',
        jeditor: '',
        ywymygx: 0,
        num: 0,
        one: '/crm/sz/one',
        edit: '/crm/sz/edit',
        pid: 0,
      }
    },
    created() {
      var that = this;
      httpAction(that.one, {}, 'post').then((res) => {
        if (res.success) {
          that.tp = res.result.tp
          that.jeditor = res.result.jeditor
          that.ywymygx = res.result.ywymygx
          that.pid = res.result.id
        } else {
          that.$message.warning(res.message)
        }
      }).finally(() => {
      })
    },
    computed: {

    },
    methods: {
      subButt(){
        var that =this
        httpAction(that.edit, {id:that.pid, tp:that.tp,jeditor:that.jeditor,ywymygx:that.ywymygx}, 'post').then((res) => {
          if (res.success) {
            that.$message.success(res.message)
            that.$emit('ok')
            // that.loadData()
          } else {
            that.$message.warning(res.message)
          }
        }).finally(() => {
          // that.confirmLoading = false
        })
      },
      handleCloseCurrentPage() {
        // 注意：以下代码必须存在
        // inject:['closeCurrent'],
        this.closeCurrent()
      },

    }
  }
</script>
<style lang="less" scoped>
  .ant-card-body .table-operator {
    margin-bottom: 18px;
  }

  .ant-table-tbody .ant-table-row td {
    padding-top: 15px;
    padding-bottom: 15px;
  }

  .anty-row-operator button {
    margin: 0 5px
  }

  .ant-btn-danger {
    background-color: #ffffff
  }

  .ant-modal-cust-warp {
    height: 100%
  }

  .ant-modal-cust-warp .ant-modal-body {
    height: calc(100% - 110px) !important;
    overflow-y: auto
  }

  .ant-modal-cust-warp .ant-modal-content {
    height: 90% !important;
    overflow-y: hidden
  }
</style>