<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="溯源码">
              <a-input placeholder="请输入归属溯源码" v-model="queryParam.ssretrospectiveSourceCode"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="10" :lg="11" :md="12" :sm="24">
            <a-form-item label="生成时间">
              <j-date
                :show-time="true"
                date-format="YYYY-MM-DD HH:mm:ss"
                placeholder="请选择开始时间"
                class="query-group-cust"
                v-model="queryParam.shengchengTime_begin"
              ></j-date>
              <span class="query-group-split-cust"></span>
              <j-date
                :show-time="true"
                date-format="YYYY-MM-DD HH:mm:ss"
                placeholder="请选择结束时间"
                class="query-group-cust"
                v-model="queryParam.shengchengTime_end"
              ></j-date>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left; overflow: hidden" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <!--      <a-button @click="DY" type="primary" icon="plus">打印</a-button>-->
      <!--      <a-button type="primary" icon="download" @click="handleExportXls('二维码管理')">导出</a-button>-->
      <!--      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">-->
      <!--        <a-button type="primary" icon="import">导入</a-button>-->
      <!--      </a-upload>-->
      <!-- 高级查询区域 -->
      <!--      <j-super-query :fieldList="superFieldList" ref="superQueryModal" @handleSuperQuery="handleSuperQuery"></j-super-query>-->

      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete" />删除</a-menu-item>
        </a-menu>
        <!--        <a-menu slot="overlay">-->
        <!--          <a-menu-item key="2" @click="batchDel"><a-icon type="primary"/>打印</a-menu-item>-->
        <!--        </a-menu>-->
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择
        <a style="font-weight: 600">{{ selectedRowKeys.length }}</a
        >项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        ref="table"
        size="middle"
        :scroll="{ x: true }"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
        class="j-table-force-nowrap"
        @change="handleTableChange"
      >
        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px; font-style: italic">无图片</span>
          <img
            v-else
            :src="getImgView(text)"
            height="25px"
            alt=""
            style="max-width: 80px; font-size: 12px; font-style: italic"
          />
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px; font-style: italic">无文件</span>
          <a-button v-else :ghost="true" type="primary" icon="download" size="small" @click="downloadFile(text)">
            下载
          </a-button>
        </template>

        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" />

          <!-- <a @click="print_usb(record)">打印</a>

          <a-divider type="vertical" /> -->
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>
      </a-table>
    </div>

    <admin-qrcode-modal2 ref="modalForm" @ok="modalFormOk"></admin-qrcode-modal2>
  </a-card>
</template>

<script>
import '@/assets/less/TableExpand.less'
import { mixinDevice } from '@/utils/mixin'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import AdminQrcodeModal2 from './modules/AdminQrcodeModal2'

export default {
  name: 'AdminQrcodeList2',
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    AdminQrcodeModal2,
  },
  data() {
    return {
      description: '溯源码二维码管理页面',
      // 表头
      columns: [
        {
          title: '#',
          dataIndex: '',
          key: 'rowIndex',
          width: 60,
          align: 'center',
          customRender: function (t, r, index) {
            return parseInt(index) + 1
          },
        },
        {
          title: '溯源码',
          align: 'center',
          dataIndex: 'retrospectiveSourceCode',
        },
        {
          title: '生成的二维码图片',
          align: 'center',
          dataIndex: 'qrcodePic',
          scopedSlots: { customRender: 'imgSlot' },
        },
        {
          title: '生成时间',
          align: 'center',
          dataIndex: 'shengchengTime',
        },
        {
          title: '打印数量',
          align: 'center',
          dataIndex: 'dayinNum',
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
          fixed: 'right',
          width: 147,
          scopedSlots: { customRender: 'action' },
        },
      ],
      url: {
        list: '/qrcode/adminQrcode/list2',
        delete: '/qrcode/adminQrcode/delete',
        deleteBatch: '/qrcode/adminQrcode/deleteBatch',
        exportXlsUrl: '/qrcode/adminQrcode/exportXls',
        importExcelUrl: 'qrcode/adminQrcode/importExcel',
      },
      dictOptions: {},
      superFieldList: [],
    }
  },
  created() {
    this.getSuperFieldList()
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`
    },
  },
  methods: {
    print_usb(record) {
      for (var i = 0; i < record.dayinNum; i++) {
        PTK_OpenUSBPort(255) //打开打印机USB端口
        this.PrintContent(record.qrcodePic) //打印内容
        PTK_CloseUSBPort() //关闭USB端口
        this.printing() //请求数据并打印
      }
    },

    PrintContent(qrcodePic) {
      var mm = 8
      PTK_ClearBuffer() //*清空缓存
      PTK_SetDarkness(10) //设置打印黑度 取值范围 0-20
      PTK_SetPrintSpeed(4) //设置打印速度
      PTK_SetDirection('B') //设置打印方向
      PTK_SetLabelHeight(30 * mm, 24, 0, false) //*设置标签高度、间隙及偏移
      PTK_SetLabelWidth(100 * mm) //*设置标签宽度，一定要准确，否则会导致打印内容位置不准确

      PTK_AnyGraphicsPrint(
        250,
        10,
        'picture',
        'http://101.200.162.45:9999/sys/common/static/' + qrcodePic,
        0,
        30 * mm,
        30 * mm,
        0
      ) //打印网络图片
      //PTK_AnyGraphicsPrint(20,20,"picture","D:\logo.jpg",1,0,0,0); //打印本地图片
      PTK_PrintLabel(1, 1) //打印，必须要执行这一行，否则不会打印
    },

    printing() {
      var url = 'http://127.0.0.1:888/postek/print'
      var data = {}
      data.reqParam = '1'
      data.printparams = JSON.stringify(printparamsJsonArray)
      jQuery.support.cors = true //适用于IE浏览器
      clean() //此函数必须使用
      $.ajax({
        type: 'POST',
        url: url,
        data: data,
        dataType: 'json',
        timeout: 5000, // in milliseconds
        success: function (result) {
          if (result.retval == '0') {
            // alert('运行成功！')
          } else {
            alert('存在错误，返回结果：' + result.msg)
          }
        },
        error: function (request, status, err) {
          alert('请求失败，请检查参数是否正确或服务是否开启')
        },
      })
    },
    initDictConfig() {},
    getSuperFieldList() {
      let fieldList = []
      fieldList.push({ type: 'string', value: 'retrospectiveSourceCode', text: '归属溯源码', dictCode: '' })
      fieldList.push({ type: 'string', value: 'qrcodePic', text: '生成的二维码图片', dictCode: '' })
      fieldList.push({ type: 'datetime', value: 'shengchengTime', text: '生成时间' })
      fieldList.push({ type: 'string', value: 'createBy', text: '操作员', dictCode: '' })
      fieldList.push({ type: 'datetime', value: 'createTime', text: '创建时间' })
      this.superFieldList = fieldList
    },
  },
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>
