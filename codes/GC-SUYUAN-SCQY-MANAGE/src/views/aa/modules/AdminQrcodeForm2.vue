<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="归属溯源码" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="retrospectiveSourceCode">
              <a-input v-model="model.retrospectiveSourceCode" placeholder="请输入归属溯源码"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="打印数量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="dayinNum">
              <a-input-number v-model="model.dayinNum" placeholder="请输入打印数量"  ></a-input-number>
            </a-form-model-item>
          </a-col>
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="生成的二维码图片" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="qrcodePic">-->
<!--              <j-image-upload v-model="model.qrcodePic" disabled></j-image-upload>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="生成时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shengchengTime">-->
<!--              <j-date placeholder="请选择生成时间"  v-model="model.shengchengTime" :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" style="width: 100%" disabled/>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="操作员" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="createBy">-->
<!--              <a-input v-model="model.createBy" placeholder="请输入操作员" disabled ></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="创建时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="createTime">-->
<!--              <j-date placeholder="请选择创建时间"  v-model="model.createTime" :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" style="width: 100%" disabled/>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
        </a-row>
      </a-form-model>
    </j-form-container>
  </a-spin>
</template>

<script>

  import { httpAction, getAction } from '@/api/manage'
  import { validateDuplicateValue } from '@/utils/util'

  export default {
    name: 'AdminQrcodeForm2',
    components: {
    },
    props: {
      //表单禁用
      disabled: {
        type: Boolean,
        default: false,
        required: false
      }
    },
    data () {
      return {
        model:{
         },
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        confirmLoading: false,
        validatorRules: {
           retrospectiveSourceCode: [
              { required: true, message: '请输入溯源码!'},
           ],
        },
        url: {
          add: "/qrcode/adminQrcode/add2",
          edit: "/qrcode/adminQrcode/edit2",
          queryById: "/qrcode/adminQrcode/queryById"
        }
      }
    },
    computed: {
      formDisabled(){
        return this.disabled
      },
    },
    created () {
       //备份model原始值
      this.modelDefault = JSON.parse(JSON.stringify(this.model));
    },
    methods: {
      add () {
        this.edit(this.modelDefault);
      },
      edit (record) {
        this.model = Object.assign({}, record);
        this.visible = true;
      },
      submitForm () {
        const that = this;
        // 触发表单验证
        this.$refs.form.validate(valid => {
          if (valid) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = '';
            if(!this.model.id){
              httpurl+=this.url.add;
              method = 'post';
            }else{
              httpurl+=this.url.edit;
               method = 'put';
            }
            httpAction(httpurl,this.model,method).then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                that.$emit('ok');
              }else{
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
            })
          }

        })
      },
    }
  }
</script>