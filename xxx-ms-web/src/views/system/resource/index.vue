<template>
  <div class="app-container">

    <!-- 头部表单 -->
    <el-form :inline="true" size="small">
      <el-form-item>
        <el-button type="success" @click="handleAddRootMenu">添加根菜单</el-button>
      </el-form-item>
    </el-form>

    <!-- 内容表格 -->
    <el-table v-loading="listLoading" :data="resourceTree" row-key="id"
              border fit highlight-current-row style="width: 100%"
    >
      <el-table-column label="资源名称">
        <template slot-scope="{row}">
          <span>{{ row.resName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="资源类型" width="110">
        <template slot-scope="{row}">
          <span>{{ resTypeMap[row.resType].desc }}</span>
        </template>
      </el-table-column>
      <el-table-column label="资源图标" width="90">
        <template slot-scope="{row}">
          <i v-if="row.resIcon.startsWith('el-icon-')" :class="row.resIcon"/>
          <svg-icon v-else-if="row.resIcon!==''" :icon-class="row.resIcon"/>
        </template>
      </el-table-column>
      <el-table-column label="请求方式" width="100">
        <template slot-scope="{row}">
          <span>{{ row.resReqMethod }}</span>
        </template>
      </el-table-column>
      <el-table-column label="资源url">
        <template slot-scope="{row}">
          <span>{{ row.resUrl }}</span>
        </template>
      </el-table-column>
      <el-table-column label="排序号" width="90">
        <template slot-scope="{row}">
          <span>{{ row.seq }}</span>
        </template>
      </el-table-column>
      <el-table-column label="资源状态" width="90">
        <template slot-scope="{row}">
          <el-tag :type="resStatusMap[row.status].tagType">
            {{ resStatusMap[row.status].desc }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="250">
        <template slot-scope="{row}">
          <el-button type="primary" size="small" @click="handleEdit(row)">
            编辑
          </el-button>
          <el-button type="warning" size="small" v-show="row.resType !== resTypeEnum.interface.resType"
                     @click="handleAddChild(row)"
          >
            添加子资源
          </el-button>
          <el-button type="danger" size="small" :disabled="row.children!==undefined" @click="handleDelete(row)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加、编辑表单 -->
    <el-dialog :title="isEdit ? '编辑系统资源' : '添加系统资源'" :visible.sync="saveResourceFormVisible">
      <el-form ref="saveResourceForm" :rules="saveResourceRules" :model="saveResourceForm" label-position="left"
               label-width="80px" style="width: 500px; margin-left:50px;"
      >
        <el-form-item label="资源名称" prop="resName">
          <el-input v-model="saveResourceForm.resName" clearable placeholder="2~100个字符"/>
        </el-form-item>
        <el-form-item label="资源类型" prop="resType">
          <el-select v-model="saveResourceForm.resType" clearable placeholder="请选择资源类型" :disabled="isEdit">
            <el-option v-for="item in saveFormResTypeOptions" :key="item.resType" :label="item.desc"
                       :value="item.resType"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="资源url" prop="resUrl">
          <el-input v-model="saveResourceForm.resUrl" clearable placeholder="盘符变量用*表示，2~100个字符"/>
        </el-form-item>
        <el-form-item label="请求方式" prop="resReqMethod"
                      v-show="saveResourceForm.resType === resTypeEnum.interface.resType"
        >
          <el-select v-model="saveResourceForm.resReqMethod" clearable placeholder="请选择请求方式">
            <el-option v-for="item in resReqMethodList" :key="item" :label="item" :value="item"/>
          </el-select>
        </el-form-item>
        <el-form-item v-show="saveResourceForm.resType === resTypeEnum.menu.resType" label="资源图标" prop="resIcon">
          <el-input v-model="saveResourceForm.resIcon" class="res-icon-input" clearable
                    placeholder="支持ElementUI图标、预置的svg图标"
          />
          <i v-if="saveResourceForm.resIcon.startsWith('el-icon-')" :class="saveResourceForm.resIcon"/>
          <svg-icon v-else-if="saveResourceForm.resIcon!==''" :icon-class="saveResourceForm.resIcon"/>
        </el-form-item>
        <el-form-item label="排序号" prop="seq">
          <el-input v-model="saveResourceForm.seq" type="number" clearable placeholder="数值越小 展示越靠前"/>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="saveResourceForm.status" clearable placeholder="请选择状态">
            <el-option v-for="item in resStatusEnum" :key="item.status" :label="item.desc" :value="item.status"/>
          </el-select>
        </el-form-item>
        <el-form-item label="父资源" prop="parentId">
          <el-cascader v-model="saveResourceForm.parentId" ref="parentResourceCascader" class="parent-resource-cascader"
                       :options="resourceTree" :props="parentResourceTreeProps" disabled clearable filterable
          >
          </el-cascader>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="saveResourceFormVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="isEdit ? updateResource() : addResource()">
          确认
        </el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import {
  listResource,
  addResource,
  updateResource,
  deleteResource,
  resTypeEnum,
  resReqMethodList,
  resStatusEnum
} from '@/api/system/resource'

import { resNameValidator, resUrlValidator } from '@/api/system/validator'

// 枚举转换为map，key是status
const resStatusMap = Object.values(resStatusEnum).reduce((acc, cur) => {
  acc[cur.status] = cur
  return acc
}, {})

const resTypeMap = Object.values(resTypeEnum).reduce((acc, cur) => {
  acc[cur.resType] = cur
  return acc
}, {})

export default {
  name: 'SystemResource',
  data() {
    return {
      resReqMethodList: resReqMethodList,
      resTypeEnum: resTypeEnum,
      resStatusEnum: resStatusEnum,
      resTypeMap: resTypeMap,
      resStatusMap: resStatusMap,
      isEdit: false,
      listLoading: true,
      resourceQuery: {
        resTypes: [],
        status: undefined
      },
      saveResourceForm: {
        id: undefined,
        resName: '',
        resType: undefined,
        resIcon: '',
        resUrl: '',
        resReqMethod: '',
        seq: 0,
        status: undefined,
        parentId: undefined
      },
      saveFormResTypeOptions: resTypeEnum, // 新增、编辑表单的资源类型选项
      resourceTree: [], // 资源树
      parentResourceTree: [], // 父资源树
      parentResourceTreeProps: {
        value: 'id',
        label: 'resName',
        children: 'children',
        checkStrictly: true, // 可直接选择任一级资源
        multiple: false, // 不允许多选
        emitPath: false // 选中节点改变时，不返回关联的各级节点组成的节点数组，只返回直接选中的节点
      },
      saveResourceFormVisible: false,
      saveResourceRules: {
        resName: [{ required: true, trigger: 'blur', validator: resNameValidator }],
        resType: [{ required: true, trigger: 'change', message: '请选择资源类型' }],
        resUrl: [{ required: true, trigger: 'blur', validator: resUrlValidator }],
        resReqMethod: [{ required: false, trigger: 'change', message: '请选择请求方式' }],
        status: [{ required: true, message: '请选择状态', trigger: 'change' }]
      }
    }
  },
  created() {
    this.getResourceList()
  },
  methods: {
    getResourceList() {
      this.listLoading = true
      listResource(this.resourceQuery).then(response => {
        // 按parentId分组
        const resourceList = response.data
        const parentResourceMap = resourceList.reduce((acc, cur) => {
          acc[cur.parentId] = acc[cur.parentId] || []
          acc[cur.parentId].push(cur)
          return acc
        }, {})
        // 从根资源（根菜单）开始，递归构建资源树
        this.resourceTree = resourceList.filter(resource => resource.parentId === 0)
        this.buildResourceTree(this.resourceTree, parentResourceMap)
        this.listLoading = false
      })
    },
    resetSaveResourceForm() {
      this.saveResourceForm = {
        id: undefined,
        resName: '',
        resType: undefined,
        resIcon: '',
        resUrl: '',
        resReqMethod: '',
        seq: undefined,
        status: undefined,
        parentId: undefined
      }
    },
    handleAddRootMenu() {
      this.isEdit = false
      this.resetSaveResourceForm()
      this.saveFormResTypeOptions = [resTypeEnum.menu]
      this.saveResourceFormVisible = true
      this.$nextTick(() => {
        this.$refs['saveResourceForm'].clearValidate()
      })
    },
    handleAddChild(row) {
      this.isEdit = false
      this.resetSaveResourceForm()
      this.saveResourceForm.parentId = row.id
      if (row.resType === resTypeEnum.menu.resType) {
        this.saveFormResTypeOptions = [resTypeEnum.menu, resTypeEnum.operation]
      } else if (row.resType === resTypeEnum.operation.resType) {
        this.saveFormResTypeOptions = [resTypeEnum.interface]
      }
      this.saveResourceFormVisible = true
      this.$nextTick(() => {
        this.$refs['saveResourceForm'].clearValidate()
      })
    },
    addResource() {
      if (Array.isArray(this.saveResourceForm.parentId)) {
        this.saveResourceForm.parentId = this.saveResourceForm.parentId.pop()
      }
      this.$refs['saveResourceForm'].validate((valid) => {
        if (valid) {
          addResource(this.saveResourceForm).then(() => {
            this.saveResourceFormVisible = false
            this.$message({
              type: 'success',
              message: '添加系统资源成功'
            })
            this.getResourceList()
          })
        }
      })
    },
    handleEdit(row) {
      this.isEdit = true
      this.saveResourceForm = Object.assign({}, row) // copy obj
      this.saveFormResTypeOptions = resTypeEnum
      this.saveResourceFormVisible = true
      this.$nextTick(() => {
        this.$refs['saveResourceForm'].clearValidate()
      })
    },
    updateResource() {
      this.$refs['saveResourceForm'].validate((valid) => {
        if (valid) {
          updateResource(this.saveResourceForm).then(() => {
            this.saveResourceFormVisible = false
            this.$message({
              type: 'success',
              message: '更新系统资源成功'
            })
            this.getResourceList()
          })
        }
      })
    },
    handleDelete(row) {
      this.$confirm(`是否删除系统资源【${row.resName}】？会一并删除对应的角色—资源关联关系。`, '操作提示', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async() => {
        await deleteResource(row.id)
        this.$message({
          type: 'success',
          message: '删除系统资源成功'
        })
        this.getResourceList()
      }).catch(err => {
        console.error(err)
      })
    },
    buildResourceTree(parentResourceList, parentResourceMap) {
      parentResourceList.forEach(parentResource => {
        const subResourceList = parentResourceMap[parentResource.id]
        if (subResourceList) {
          subResourceList.sort((res1, res2) => res1.seq - res2.seq)
          parentResource.children = subResourceList
          this.buildResourceTree(subResourceList, parentResourceMap)
        }
      })
    }
  }
}
</script>

<style scoped>
.parent-resource-cascader {
  width: 100%;
}

.res-icon-input {
  width: 80%;
  margin-right: 20px;
}
</style>
