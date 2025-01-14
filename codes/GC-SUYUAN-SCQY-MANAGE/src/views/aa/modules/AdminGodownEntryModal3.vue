<template>
  <j-modal
    :title="title"
    :width="1200"
    :visible="visible"
    :maskClosable="false"
    switchFullscreen
    @ok="handleOk"
    :okButtonProps="{ class:{'jee-hidden': disableSubmit} }"
    @cancel="handleCancel">
    <admin-godown-entry-form3 ref="realForm" @ok="submitCallback" :disabled="disableSubmit" />
  </j-modal>
</template>

<script>
import AdminGodownEntryForm3 from './AdminGodownEntryForm3'


export default {
  name: 'AdminGodownEntryModal3',
  components: {
    AdminGodownEntryForm3
  },
  data() {
    return {
      title: '',
      visible: false,
      disableSubmit: false,
      record: {},
      //采购单
      listUrl: "/adminpurchasenote/adminPurchaseNote/RSlist",
      //销售单
      listUrl2: "/adminpurchasenote/adminPurchaseNote/RS2list",
    }
  },
  methods: {
    add() {
      this.visible = true
      this.$nextTick(() => {
        this.$refs.realForm.add()
      })
    },
    edit(record) {
      this.visible = true
      this.record = record
      this.$nextTick(() => {
        this.$refs.realForm.edit(record)
      })
    },
    close() {
      this.$emit('close')
      this.visible = false
    },
    handleOk() {
      const list = this.$refs.realForm.test
      console.log(list)
      if (list.length == 0){
        this.$refs.realForm.handleOk()
      }else {
        let result = this.checkArrayObjectValueRepeat(list,"retrospectiveSourceCode","溯源码")
        if (result) {
          return alert(result)
        }else {
          //上面是未插入数据库的重复判断
          //下面是要判断未插入的和数据库的重复判断
          // console.log(this.record.id)
          // httpAction(this.listUrl+"/"+this.record.id, {  }, 'get').then((res) => {
          //   if (res.success) {
          //     console.log(res)
          //
          //   } else {
          //     this.$message.warning(res.message)
          //   }
          // }).finally(() => {
          // })
          this.$refs.realForm.handleOk()
        }
      }
    },
    checkArrayObjectValueRepeat(dataArr, key, keyCN) {
      let valArr = []
      let check = ''
      dataArr.some((item) => {
        if (valArr.includes(item[key])) return check = `${keyCN || key}不能重复`
        valArr.push(item[key])
        return false
      })
      return check
    },
    submitCallback() {
      this.$emit('ok')
      this.visible = false
    },
    handleCancel() {
      this.close()
    }
  }
}
</script>

<style scoped>
</style>