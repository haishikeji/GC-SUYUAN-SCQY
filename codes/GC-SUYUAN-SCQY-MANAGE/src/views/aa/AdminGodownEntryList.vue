<template>
  <a-card class="j-inner-table-wrapper" :bordered="false">
    <!-- 查询区域 begin -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="采购订单号">
              <a-input v-model="queryParam.sspurchaseOrderNumber" placeholder="请输入采购订单号" />
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
          <a-col :xl="10" :lg="11" :md="12" :sm="24">
            <a-form-item label="入库时间">
              <j-date
                :show-time="true"
                date-format="YYYY-MM-DD HH:mm:ss"
                placeholder="请选择开始时间"
                class="query-group-cust"
                v-model="queryParam.sstime_begin"
              ></j-date>
              <span class="query-group-split-cust"></span>
              <j-date
                :show-time="true"
                date-format="YYYY-MM-DD HH:mm:ss"
                placeholder="请选择结束时间"
                class="query-group-cust"
                v-model="queryParam.sstime_end"
              ></j-date>
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
      <a-button type="primary" icon="download" @click="handleExportXls('采购单')">导出</a-button>
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
            <a-tab-pane tab="溯源码" key="adminRscode" forceRender>
              <admin-rscode-sub-table :record="record" />
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
          <a @click="handleDetail(record)">详情</a>
          <a-divider type="vertical" />
          <a @click="Edit3(record)">商品入库</a>
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
          <!--          <a-dropdown>-->
          <!--            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>-->
          <!--            <a-menu slot="overlay">-->
          <!--              <a-menu-item>-->
          <!--                <a-popconfirm title="确定删除吗?" @confirm="handleDelete(record.id)">-->
          <!--                  <a>删除</a>-->
          <!--                </a-popconfirm>-->
          <!--              </a-menu-item>-->
          <!--            </a-menu>-->
          <!--          </a-dropdown>-->
        </template>
      </a-table>
    </div>
    <!-- table区域 end -->

    <!-- 表单区域 -->
    <admin-godown-entry-modal ref="modalForm" @ok="modalFormOk" />

    <admin-godown-entry-modal2 ref="modalForm2" @ok="modalFormOk" />

    <admin-godown-entry-modal3 ref="modalForm3" @ok="modalFormOk" />
  </a-card>
</template>

<script>
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import AdminGodownEntryModal from './modules/AdminGodownEntryModal'
import AdminGodownEntryModal2 from './modules/AdminGodownEntryModal2'
import AdminGodownEntryModal3 from './modules/AdminGodownEntryModal3'
import AdminRscodeSubTable from './subTables/AdminRscodeSubTable'
import '@/assets/less/TableExpand.less'

export default {
  name: 'AdminGodownEntryList',
  mixins: [JeecgListMixin],
  components: {
    AdminGodownEntryModal,
    AdminGodownEntryModal2,
    AdminGodownEntryModal3,
    AdminRscodeSubTable,
  },
  data() {
    return {
      description: '采购单列表管理页面',
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
          title: '采购订单号',
          align: 'center',
          dataIndex: 'purchaseOrderNumber',
        },
        {
          title: '供应商',
          align: 'center',
          dataIndex: 'supplier',
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
          title: '采购单条码',
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
        list: '/admincasehistory/adminGodownEntry/list',
        delete: '/admincasehistory/adminGodownEntry/delete',
        deleteBatch: '/admincasehistory/adminGodownEntry/deleteBatch',
        exportXlsUrl: '/admincasehistory/adminGodownEntry/exportXls',
        importExcelUrl: '/admincasehistory/adminGodownEntry/importExcel',
        importExcelUrl22: '/admincasehistory/adminGodownEntry/importExcel2',
      },
      superFieldList: [],
    }
  },
  created() {
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
    Add1: function () {
      this.$refs.modalForm2.add()
      this.$refs.modalForm2.title = '新增采购单'
      this.$refs.modalForm2.disableSubmit = false
    },
    Edit2: function (record) {
      this.$refs.modalForm2.edit(record)
      this.$refs.modalForm2.title = '编辑采购单'
      this.$refs.modalForm2.disableSubmit = false
    },
    Edit3: function (record) {
      this.$refs.modalForm3.edit(record)
      this.$refs.modalForm3.title = '商品入库'
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
      fieldList.push({ type: 'string', value: 'purchaseOrderNumber', text: '采购订单号', dictCode: '' })
      fieldList.push({ type: 'string', value: 'supplier', text: '供应商', dictCode: '' })
      fieldList.push({ type: 'string', value: 'warehouseName', text: '仓库名称', dictCode: '' })
      fieldList.push({ type: 'string', value: 'orderType', text: '订单类型', dictCode: '' })
      fieldList.push({ type: 'string', value: 'barCode', text: '采购单条码', dictCode: '' })
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
