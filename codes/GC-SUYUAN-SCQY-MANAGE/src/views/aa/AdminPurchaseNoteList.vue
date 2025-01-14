<template>
  <a-card class="j-inner-table-wrapper" :bordered="false">
    <!-- 查询区域 begin -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="销售订单编号">
              <a-input v-model="queryParam.ssmarketOrderNumber" placeholder="请输入销售订单编号" />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="商品名称">
              <a-input v-model="queryParam.ssname" placeholder="请输入商品名称" />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="商品代码">
              <a-input v-model="queryParam.sscode" placeholder="请输入商品代码" />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="商品条码">
              <a-input v-model="queryParam.ssbarcode" placeholder="请输入商品条码" />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="溯源码">
              <a-input v-model="queryParam.ssretrospectiveSourceCode" placeholder="请输入溯源码" />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span class="table-page-search-submitButtons table-operator">
              <a-button type="primary" icon="search" @click="searchQuery">查询</a-button>
              <a-button type="primary" icon="reload" @click="searchReset">重置</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域 end -->

    <!-- 操作按钮区域 begin -->
    <div class="table-operator">
      <a-button type="primary" icon="plus" @click="Add1">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('销售单')">导出</a-button>
      <!--      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl"-->
      <!--                @change="handleImportExcel">-->
      <!--        <a-button type="primary" icon="import">导入</a-button>-->
      <!--      </a-upload>-->
      <!-- 高级查询区域 -->
      <j-super-query
        :fieldList="superFieldList"
        ref="superQueryModal"
        @handleSuperQuery="handleSuperQuery"
      ></j-super-query>

      <span style="margin-right: 8px">溯源码入库判断开关：<a-switch @click="switchI" v-model="fullscreen" /></span>

      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel">
            <a-icon type="delete" />
            <span>删除</span>
          </a-menu-item>
        </a-menu>
        <a-button>
          <span>批量操作</span>
          <a-icon type="down" />
        </a-button>
      </a-dropdown>
    </div>
    <!-- 操作按钮区域 end -->

    <!-- table区域 begin -->
    <div>
      <a-alert type="info" showIcon style="margin-bottom: 16px">
        <template slot="message">
          <span>已选择</span>
          <a style="font-weight: 600; padding: 0 4px">{{ selectedRowKeys.length }}</a>
          <span>项</span>
          <a style="margin-left: 24px" @click="onClearSelected">清空</a>
        </template>
      </a-alert>

      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        class="j-table-force-nowrap"
        :scroll="{ x: true }"
        :loading="loading"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :expandedRowKeys="expandedRowKeys"
        :rowSelection="{ selectedRowKeys, onChange: onSelectChange }"
        @expand="handleExpand"
        @change="handleTableChange"
      >
        <!-- 内嵌table区域 begin -->
        <template slot="expandedRowRender" slot-scope="record">
          <a-tabs tabPosition="top">
            <a-tab-pane tab="出库溯源码" key="adminRscode2" forceRender>
              <admin-rscode2-sub-table :record="record" />
            </a-tab-pane>
          </a-tabs>
        </template>
        <!-- 内嵌table区域 end -->

        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>

        <template slot="imgSlot" slot-scope="text">
          <div style="font-size: 12px; font-style: italic">
            <span v-if="!text">无图片</span>
            <img v-else :src="getImgView(text)" alt="" style="max-width: 80px; height: 25px" />
          </div>
        </template>

        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px; font-style: italic">无文件</span>
          <a-button v-else ghost type="primary" icon="download" size="small" @click="downloadFile(text)">
            <span>下载</span>
          </a-button>
        </template>

        <template slot="action" slot-scope="text, record">
          <a @click="Edit2(record)">编辑</a>
          <a-divider type="vertical" />
          <a @click="handle1(record)">详情</a>
          <a-divider type="vertical" />
          <a @click="Edit3(record)">商品出库</a>
          <!-- <a-divider type="vertical" /> -->
          <!-- <a @click="DCDYWJ(record)">导出打印文件</a> -->
          <a-divider type="vertical" />
          <a-upload
            name="file"
            :showUploadList="false"
            :multiple="false"
            :headers="tokenHeader"
            :action="importExcelUrl2 + '/' + record.id"
            @change="handleImportExcel"
          >
            <a>导入excel</a>
          </a-upload>
        </template>
      </a-table>
    </div>
    <!-- table区域 end -->

    <!-- 表单区域 -->
    <admin-purchase-note-modal ref="modalForm" @ok="modalFormOk" />

    <admin-purchase-note-modal2 ref="modalForm2" @ok="modalFormOk" />

    <admin-purchase-note-modal3 ref="modalForm3" @ok="modalFormOk" />
  </a-card>
</template>

<script>
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import AdminPurchaseNoteModal from './modules/AdminPurchaseNoteModal'
import AdminPurchaseNoteModal2 from './modules/AdminPurchaseNoteModal2'
import AdminPurchaseNoteModal3 from './modules/AdminPurchaseNoteModal3'
import AdminRscode2SubTable from './subTables/AdminRscode2SubTable'
import '@/assets/less/TableExpand.less'
import { httpAction } from '@api/manage'
import { axios } from '@/utils/request'

export default {
  name: 'AdminPurchaseNoteList',
  mixins: [JeecgListMixin],
  components: {
    AdminPurchaseNoteModal,
    AdminPurchaseNoteModal2,
    AdminPurchaseNoteModal3,
    AdminRscode2SubTable,
  },
  data() {
    return {
      fullscreen: true,
      pid: true,
      description: '销售单列表管理页面',
      // 表头
      columns: [
        {
          title: '#',
          key: 'rowIndex',
          width: 60,
          align: 'center',
          customRender: (t, r, index) => parseInt(index) + 1,
        },
        {
          title: '销售订单号',
          align: 'center',
          dataIndex: 'marketOrderNumber',
        },
        {
          title: '收件人',
          align: 'center',
          dataIndex: 'recipients',
        },
        {
          title: '仓库名称',
          align: 'center',
          dataIndex: 'warehouseName',
        },
        {
          title: '订单类型',
          align: 'center',
          dataIndex: 'orderType',
        },
        {
          title: '销售单条码',
          align: 'center',
          dataIndex: 'barCode',
        },
        {
          title: '操作员',
          align: 'center',
          dataIndex: 'createBy',
        },
        {
          title: '创建时间',
          align: 'center',
          dataIndex: 'createTime',
        },
        {
          title: '操作',
          dataIndex: 'action',
          align: 'center',
          width: 147,
          scopedSlots: { customRender: 'action' },
        },
      ],
      // 字典选项
      dictOptions: {},
      // 展开的行test
      expandedRowKeys: [],
      url: {
        list: '/adminpurchasenote/adminPurchaseNote/list',
        delete: '/adminpurchasenote/adminPurchaseNote/delete',
        deleteBatch: '/adminpurchasenote/adminPurchaseNote/deleteBatch',
        exportXlsUrl: '/adminpurchasenote/adminPurchaseNote/exportXls',
        importExcelUrl: '/adminpurchasenote/adminPurchaseNote/importExcel',
        importExcelUrl22: '/adminpurchasenote/adminPurchaseNote/importExcel2',
        one: '/sz/one',
        edit: '/sz/edit',
        exportPdf: '/adminpurchasenote/adminPurchaseNote/exportPdf',
      },
      superFieldList: [],
    }
  },
  created() {
    httpAction(this.url.one, {}, 'post')
      .then((res) => {
        if (res.success) {
          this.pid = res.result.id
          if (res.result.symcode == '1') {
            this.fullscreen = true
          }
          if (res.result.symcode == '2') {
            this.fullscreen = false
          }
        } else {
          this.$message.warning(res.message)
        }
      })
      .finally(() => {})
    this.getSuperFieldList()
  },
  computed: {
    importExcelUrl() {
      return window._CONFIG['domianURL'] + this.url.importExcelUrl
    },
    importExcelUrl2: function () {
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl22}`
    },
  },
  methods: {
    // DCDYWJ(record) {
    //   var that = this
    //   const timestamp = new Date().getTime()
    //   httpAction(that.url.exportPdf + '/' + record.id + '/' + timestamp, {}, 'post').then((res) => {
    //     window.open(
    //       'http://101.200.162.45:9999/sys/common/static/销售单_' + record.marketOrderNumber + '_' + timestamp + '.pdf'
    //     )
    //     // window.open("http://127.0.0.1:9999/sys/common/static/销售单_"+record.marketOrderNumber+"_"+timestamp+".pdf");
    //     // if (res.success) {
    //     //   // window.open(url);
    //     // } else {
    //     //   that.$message.warning(res.message)
    //     // }
    //   })
    // },

    switchI() {
      var that = this
      if (that.fullscreen) {
        var symcode = '1'
      } else {
        var symcode = '2'
      }
      httpAction(that.url.edit, { id: that.pid, symcode: symcode }, 'post').then((res) => {
        if (res.success) {
          that.$message.success(res.message)
          that.$emit('ok')
          // that.loadData()
        } else {
          that.$message.warning(res.message)
        }
      })
    },
    Add1: function () {
      this.$refs.modalForm2.add()
      this.$refs.modalForm2.title = '新增销售单'
      this.$refs.modalForm2.disableSubmit = false
    },
    handle1: function (record) {
      this.$refs.modalForm.edit(record)
      this.$refs.modalForm.title = '产品溯源单'
      this.$refs.modalForm.disableSubmit = true
    },
    Edit2: function (record) {
      this.$refs.modalForm2.edit(record)
      this.$refs.modalForm2.title = '编辑销售单'
      this.$refs.modalForm2.disableSubmit = false
    },
    Edit3: function (record) {
      this.$refs.modalForm3.edit(record)
      this.$refs.modalForm3.title = '商品出库'
      this.$refs.modalForm3.disableSubmit = false
    },

    initDictConfig() {},

    handleExpand(expanded, record) {
      this.expandedRowKeys = []
      if (expanded === true) {
        this.expandedRowKeys.push(record.id)
      }
    },
    getSuperFieldList() {
      let fieldList = []
      fieldList.push({ type: 'string', value: 'marketOrderNumber', text: '销售订单号', dictCode: '' })
      fieldList.push({ type: 'string', value: 'recipients', text: '收件人', dictCode: '' })
      fieldList.push({ type: 'string', value: 'warehouseName', text: '仓库名称', dictCode: '' })
      fieldList.push({ type: 'string', value: 'orderType', text: '订单类型', dictCode: '' })
      fieldList.push({ type: 'string', value: 'barCode', text: '销售单条码', dictCode: '' })
      fieldList.push({ type: 'string', value: 'createBy', text: '操作员', dictCode: '' })
      fieldList.push({ type: 'datetime', value: 'createTime', text: '创建时间' })
      this.superFieldList = fieldList
    },
  },
}
</script>
<style lang="less" scoped>
@import '~@assets/less/common.less';
</style>
