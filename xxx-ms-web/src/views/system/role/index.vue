<template>
  <div class="app-container">

    <!-- 搜索表单 -->
    <el-form :inline="true" :model="roleQuery" size="small">
      <el-form-item label="角色名">
        <el-input v-model="roleQuery.roleName" clearable/>
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="roleQuery.status" clearable>
          <el-option v-for="item in roleStatusList" :key="item.value" :label="item.label" :value="item.value"/>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="search">查询</el-button>
        <el-button type="warning" @click="resetRoleQuery">清空</el-button>
        <el-button type="success" @click="handleAdd">添加</el-button>
      </el-form-item>
    </el-form>

    <!-- 内容表格 -->
    <el-table v-loading="listLoading" :data="rolePage.list" border fit highlight-current-row style="width: 100%">
      <el-table-column label="角色名称" align="center" width="200">
        <template slot-scope="{row}">
          <span>{{ row.roleName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" width="120">
        <template slot-scope="{row}">
          <el-tag>
            {{ row.status | statusFilter }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="包含用户" align="center">
        <template slot-scope="{row}">
          <el-tag v-if="row.sysUserList" v-for="sysUser of row.sysUserList" :key="sysUser.username">
            {{ sysUser.username + '_' + sysUser.realName }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="220">
        <template slot-scope="{row}">
          <el-button type="primary" size="small" @click="handleEdit(row)">
            编辑
          </el-button>
          <el-button type="warning" size="small" @click="handleGrantPermission(row)">
            授权
          </el-button>
          <el-button type="danger" size="small" @click="handleDelete(row)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
      :v-show="rolePage.total > 0"
      :total="rolePage.total"
      :current-page="roleQuery.pageNum"
      :page-size="roleQuery.pageSize"
      :page-sizes="[10, 20, 50, 100]"
      layout="total, sizes, prev, pager, next, jumper"
      @size-change="handlerSizeChange"
      @current-change="handleCurrentChange"
    />

    <!-- 添加、编辑表单 -->
    <el-dialog :title="isEditRole ? '编辑系统角色' : '添加系统角色'" :visible.sync="saveRoleFormVisible">
      <el-form ref="roleForm" :rules="saveRoleRules" :model="saveRoleForm" label-position="left" label-width="80px"
               style="width: 500px; margin-left:50px;"
      >
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="saveRoleForm.roleName" clearable placeholder="只支持英文、数字、下划线，5~50字"/>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="saveRoleForm.status" clearable placeholder="请选择状态">
            <el-option v-for="item in roleStatusList" :key="item.value" :label="item.label" :value="item.value"/>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="saveRoleFormVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="isEditRole ? updateRole() : addRole()">
          确认
        </el-button>
      </div>
    </el-dialog>

    <!-- 角色授权表单 -->
    <el-dialog title="系统角色授权" :visible.sync="saveRoleResourceFormVisible">
      <el-form ref="saveRoleResourceForm" :rules="saveRoleResourceRules" :model="saveRoleResourceForm"
               label-position="left" label-width="80px"
               style="width: 500px; margin-left:50px;"
      >
        <el-form-item prop="resIds">
          <el-tree ref="resourceTree"
                   :data="resourceTree"
                   :default-checked-keys="saveRoleResourceForm.resIds"
                   :props="resourceTreeProps"
                   node-key="id"
                   show-checkbox
          >
          </el-tree>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="saveRoleResourceFormVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="saveRoleResource">
          确认
        </el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import { pageRole, addRole, updateRole, deleteRole, listRoleResource, saveRoleResource } from '@/api/system/role'
import { listResource } from '@/api/system/resource'
import { roleNameValidator } from '@/utils/validate'

// 状态对应关系统一维护在此list中
const roleStatusList = [
  { value: 0, label: '正常' },
  { value: 1, label: '禁用' }
]

// 将list转换为map，{0:'正常', 1:'禁用'}
const roleStatusMap = roleStatusList.reduce((acc, cur) => {
  acc[cur.value] = cur.label
  return acc
}, {})

export default {
  name: 'SystemRole',
  filters: {
    statusFilter(status) {
      return roleStatusMap[status]
    }
  },
  data() {
    return {
      roleStatusList: roleStatusList,
      isEditRole: false,
      listLoading: true,
      roleQuery: {
        roleName: '',
        status: undefined,
        pageNum: 1,
        pageSize: 10
      },
      resourceQuery: {
        resTypes: [1, 2], // 菜单、按钮/操作 todo: 定义为常量
        status: undefined // 不过滤状态，允许分配非生效中的资源
      },
      roleResourceQuery: {
        roleId: undefined
      },
      saveRoleForm: {
        id: undefined,
        roleName: '',
        status: undefined
      },
      saveRoleResourceForm: {
        roleId: undefined,
        resIds: []
      },
      rolePage: {
        total: 0,
        pages: 0,
        list: []
      },
      resourceTree: [], // 资源树
      resourceTreeProps: {
        label: 'resName',
        children: 'children'
      },
      saveRoleFormVisible: false,
      saveRoleResourceFormVisible: false,
      saveRoleRules: {
        roleName: [{ required: true, trigger: 'blur', validator: roleNameValidator }],
        status: [{ required: true, message: '请选择状态', trigger: 'blur' }]
      },
      saveRoleResourceRules: {
        resIds: [{ required: true, message: '至少选择一个资源', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.getRoleList()
  },
  methods: {
    getRoleList() {
      this.listLoading = true
      pageRole(this.roleQuery).then(response => {
        this.rolePage = response.data
        this.listLoading = false
      })
    },
    handlerSizeChange(currentPageSize) {
      this.roleQuery.pageSize = currentPageSize
      this.getRoleList()
    },
    handleCurrentChange(currentPageNum) {
      this.roleQuery.pageNum = currentPageNum
      this.getRoleList()
    },
    search() {
      this.roleQuery.pageNum = 1
      this.getRoleList()
    },
    resetRoleQuery() {
      this.roleQuery = {
        roleName: '',
        status: undefined,
        pageNum: 1,
        pageSize: 10
      }
    },
    resetSaveRoleForm() {
      this.saveRoleForm = {
        id: undefined,
        roleName: '',
        status: undefined
      }
    },
    resetSaveRoleResourceForm() {
      this.saveRoleResourceForm = {
        roleId: undefined,
        resIds: []
      }
    },
    handleAdd() {
      this.resetSaveRoleForm()
      this.isEditRole = false
      this.saveRoleFormVisible = true
      this.$nextTick(() => {
        this.$refs['roleForm'].clearValidate()
      })
    },
    addRole() {
      this.$refs['roleForm'].validate((valid) => {
        if (valid) {
          addRole(this.saveRoleForm).then(() => {
            this.saveRoleFormVisible = false
            this.$message({
              type: 'success',
              message: '添加系统角色成功'
            })
            this.getRoleList()
          })
        }
      })
    },
    handleEdit(row) {
      this.saveRoleForm = Object.assign({}, row) // copy obj
      this.isEditRole = true
      this.saveRoleFormVisible = true
      this.$nextTick(() => {
        this.$refs['roleForm'].clearValidate()
      })
    },
    updateRole() {
      this.$refs['roleForm'].validate((valid) => {
        if (valid) {
          updateRole(this.saveRoleForm).then(() => {
            this.saveRoleFormVisible = false
            this.$message({
              type: 'success',
              message: '更新系统角色成功'
            })
            this.getRoleList()
          })
        }
      })
    },
    handleDelete(row) {
      this.$confirm(`是否删除系统角色【${row.roleName}】?`, '操作提示', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async() => {
        await deleteRole(row.id)
        this.$message({
          type: 'success',
          message: '删除系统角色成功'
        })
        this.getRoleList()
      }).catch(err => {
        console.error(err)
      })
    },
    handleGrantPermission(row) {
      this.resetSaveRoleResourceForm()
      this.saveRoleResourceForm.roleId = row.id

      // 加载资源列表
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
      })

      // 加载当前角色拥有的资源
      this.roleResourceQuery.roleId = row.id
      listRoleResource(this.roleResourceQuery).then(response => {
        const resourceList = response.data
        this.saveRoleResourceForm.resIds = resourceList.map((item, index, self) => item.id)
      })

      this.saveRoleResourceFormVisible = true
      this.$nextTick(() => {
        this.$refs['saveRoleResourceForm'].clearValidate()
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
    },
    saveRoleResource() {
      this.saveRoleResourceForm.resIds = this.$refs.resourceTree.getCheckedKeys()
      this.$refs['saveRoleResourceForm'].validate((valid) => {
        if (valid) {
          saveRoleResource(this.saveRoleResourceForm).then(() => {
            this.$message({
              type: 'success',
              message: '系统角色授权成功'
            })
          })
          this.saveRoleResourceFormVisible = false
        }
      })
    }
  }
}
</script>

<style scoped>

</style>

