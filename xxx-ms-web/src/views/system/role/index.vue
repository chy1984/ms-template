<template>
  <div class="app-container">

    <!-- 搜索表单 -->
    <el-form :inline="true" :model="roleQuery" size="small">
      <el-form-item label="角色名">
        <el-input v-model="roleQuery.roleName" clearable/>
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="roleQuery.status" clearable>
          <el-option
            v-for="item in roleStatusEnum"
            :key="item.status"
            :label="item.desc"
            :value="item.status"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="search">查询</el-button>
        <el-button type="warning" @click="resetRoleQuery">清空</el-button>
        <el-button v-permission="roleOperation.add" type="success" @click="handleAdd">添加</el-button>
      </el-form-item>
    </el-form>

    <!-- 内容表格 -->
    <el-table v-loading="listLoading" :data="rolePage.list" border fit highlight-current-row class="width-full">
      <el-table-column label="角色名称" align="center" width="200">
        <template slot-scope="{row}">
          <span>{{ row.roleName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" width="120">
        <template slot-scope="{row}">
          <el-tag :type="roleStatusMap[row.status].tagType">
            {{ roleStatusMap[row.status].desc }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="包含用户" align="center">
        <template slot-scope="{row}">
          <el-tag v-for="sysUser in row.sysUserList" :key="sysUser.username">
            {{ `${sysUser.username}_${sysUser.realName}` }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="220">
        <template slot-scope="{row}">
          <el-button v-permission="roleOperation.edit" type="primary" size="small" @click="handleEdit(row)">
            编辑
          </el-button>
          <el-button
            v-permission="roleOperation.grantPermission"
            type="warning"
            size="small"
            @click="handleGrantPermission(row)"
          >
            授权
          </el-button>
          <el-button v-permission="roleOperation.delete" type="danger" size="small" @click="handleDelete(row)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <div v-show="rolePage.total>0" class="pagination-container">
      <el-pagination
        :total="rolePage.total"
        :current-page="roleQuery.pageNum"
        :page-size="roleQuery.pageSize"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handlerSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>

    <!-- 添加、编辑表单 -->
    <el-dialog :title="isEditRole ? '编辑系统角色' : '添加系统角色'" :visible.sync="saveRoleFormVisible">
      <el-form
        ref="roleForm"
        :rules="saveRoleRules"
        :model="saveRoleForm"
        label-position="left"
        label-width="80px"
        class="edit-dialog"
      >
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="saveRoleForm.roleName" clearable placeholder="2~50个字符"/>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="saveRoleForm.status" clearable placeholder="请选择状态">
            <el-option
              v-for="item in roleStatusEnum"
              :key="item.status"
              :label="item.desc"
              :value="item.status"
            />
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
      <el-form
        ref="saveRoleResourceForm"
        :rules="saveRoleResourceRules"
        :model="saveRoleResourceForm"
        label-position="left"
        label-width="80px"
        class="edit-dialog"
      >
        <el-form-item prop="resIds">
          <el-tree
            ref="resourceTree"
            :data="resourceTree"
            :default-checked-keys="defaultCheckedLeafKeys"
            :props="resourceTreeProps"
            node-key="id"
            show-checkbox
          />
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
import {
  pageRole, addRole, updateRole, deleteRole, listRoleResource, saveRoleResource, roleStatusEnum
} from '@/api/system/role'
import { listResource, resTypeEnum, buildResourceTree } from '@/api/system/resource'
import { roleNameValidator } from '@/api/system/validator'
import { operationResUrls } from '@/directive/permission/operation-res-urls'

// 枚举转换为map，key是status
const roleStatusMap = Object.values(roleStatusEnum).reduce((acc, cur) => {
  acc[cur.status] = cur
  return acc
}, {})

export default {
  name: 'SystemRole',
  data() {
    return {
      roleStatusEnum: roleStatusEnum,
      roleStatusMap: roleStatusMap,
      roleOperation: operationResUrls.system.role,
      isEditRole: false,
      listLoading: true,
      roleQuery: {
        roleName: '',
        status: undefined,
        pageNum: 1,
        pageSize: 10
      },
      resourceQuery: {
        status: undefined, // 不过滤状态，允许分配非生效中的资源
        resTypes: [resTypeEnum.menu.resType, resTypeEnum.operation.resType] // 菜单、按钮/操作

      },
      roleResourceQuery: {
        roleId: undefined,
        resTypes: [resTypeEnum.menu.resType, resTypeEnum.operation.resType] // 菜单、按钮/操作
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
      defaultCheckedLeafKeys: [], // 默认选中的叶子资源key数组
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
      }).finally(() => {
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
    resetRoleResourceForm() {
      this.resourceTree = []
      this.defaultCheckedLeafKeys = []
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
      this.$confirm(`是否删除系统角色【${row.roleName}】？会一并删除对应的用户—角色、角色—资源关联关系。`, '操作提示', {
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
      })
    },
    handleGrantPermission(row) {
      this.resetRoleResourceForm()
      this.saveRoleResourceForm.roleId = row.id
      this.roleResourceQuery.roleId = row.id

      // 加载资源列表
      listResource(this.resourceQuery).then(response => {
        this.resourceTree = buildResourceTree(response.data)

        // 加载当前角色拥有的资源
        listRoleResource(this.roleResourceQuery).then(roleResResponse => {
          const resourceList = roleResResponse.data
          // 过滤出具有权限的叶子节点
          this.filterLeafResource(resourceList)
          this.$nextTick(() => {
            // default-checked-keys 默认选中数据变化后，不会刷新UI，需要手动设置下
            this.$refs.resourceTree.setCheckedKeys(this.defaultCheckedLeafKeys)
            this.$refs['saveRoleResourceForm'].clearValidate()
          })

          // 展示授权表单
          this.saveRoleResourceFormVisible = true
        })
      })
    },
    saveRoleResource() {
      // 选中的叶子节点、半选中的父节点都需要传给服务端存储
      this.saveRoleResourceForm.resIds = this.$refs.resourceTree.getHalfCheckedKeys().concat(this.$refs.resourceTree.getCheckedKeys())
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
    },
    filterLeafResource(resourceList) {
      // 按parentId分组
      const parentResourceMap = resourceList.reduce((acc, cur) => {
        acc[cur.parentId] = acc[cur.parentId] || []
        acc[cur.parentId].push(cur)
        return acc
      }, {})
      // 从根资源（根菜单）开始，递归筛选叶子节点
      const rootResourceList = resourceList.filter(resource => resource.parentId === 0)
      this.filterLeafNode(rootResourceList, parentResourceMap)
    },
    filterLeafNode(resourceList, resourceMap) {
      resourceList.forEach(resource => {
        const subResourceList = resourceMap[resource.id]
        if (subResourceList) {
          this.filterLeafNode(subResourceList, resourceMap)
        } else {
          this.defaultCheckedLeafKeys.push(resource.id)
        }
      })
    }
  }
}
</script>

<style scoped>
.width-full {
  width: 100%;
}

.edit-dialog {
  width: 500px;
  margin-left: 50px;
}
</style>

