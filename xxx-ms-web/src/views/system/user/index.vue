<template>
  <div class="app-container">

    <!-- 搜索表单 -->
    <el-form :inline="true" :model="userQuery" size="small">
      <el-form-item label="用户名">
        <el-input v-model="userQuery.username" clearable/>
      </el-form-item>
      <el-form-item label="真实姓名">
        <el-input v-model="userQuery.realName" clearable/>
      </el-form-item>
      <el-form-item label="手机号">
        <el-input v-model="userQuery.tel" clearable/>
      </el-form-item>
      <el-form-item label="邮箱">
        <el-input v-model="userQuery.email" clearable style="width: 300px;"/>
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="userQuery.status" clearable>
          <el-option v-for="userStatus in userStatusList" :key="userStatus.status" :label="userStatus.desc"
                     :value="userStatus.status"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="角色">
        <el-select v-model="userQuery.roleId" class="dialog-role-select" clearable filterable>
          <el-option v-for="role in roleList" :key="role.id" :label="role.roleName" :value="role.id"/>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="search">查询</el-button>
        <el-button type="warning" @click="resetUserQuery">清空</el-button>
        <el-button type="success" @click="handleAdd">添加</el-button>
      </el-form-item>
    </el-form>

    <!-- 内容表格 -->
    <el-table v-loading="listLoading" :data="userPage.list" border fit highlight-current-row style="width: 100%">
      <el-table-column label="用户名" align="center" width="150">
        <template slot-scope="{row}">
          <span>{{ row.username }}</span>
        </template>
      </el-table-column>
      <el-table-column label="真实姓名" align="center" width="120">
        <template slot-scope="{row}">
          <span>{{ row.realName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="手机号" align="center" width="150">
        <template slot-scope="{row}">
          <span>{{ row.tel }}</span>
        </template>
      </el-table-column>
      <el-table-column label="邮箱" align="center" width="250">
        <template slot-scope="{row}">
          <span>{{ row.email }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" width="110">
        <template slot-scope="{row}">
          <el-tag :type="userStatusMap[row.status].tagType">
            {{ row.status | userStatusFilter }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="角色" align="center">
        <template slot-scope="{row}">
          <el-tag v-for="roleId of row.roleIds" :key="roleId">
            {{ roleMap[roleId] }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="250">
        <template slot-scope="{row}">
          <el-button type="primary" size="small" @click="handleEdit(row)">
            编辑
          </el-button>
          <el-button type="warning" size="small" @click="handleResetPassword(row)">
            重置密码
          </el-button>
          <el-button type="danger" size="small" @click="handleDelete(row)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
      :v-show="userPage.total > 0"
      :total="userPage.total"
      :current-page="userQuery.pageNum"
      :page-size="userQuery.pageSize"
      :page-sizes="[10, 20, 50, 100]"
      layout="total, sizes, prev, pager, next, jumper"
      @size-change="handlerSizeChange"
      @current-change="handleCurrentChange"
    />

    <!-- 添加、编辑表单 -->
    <el-dialog :title="isEdit ? '编辑系统用户' : '添加系统用户'" :visible.sync="saveUserFormVisible">
      <el-form ref="saveUserForm" :rules="saveUserRules" :model="saveUserForm" label-position="left" label-width="80px"
               style="width: 500px; margin-left:50px;"
      >
        <el-form-item label="用户名" prop="username">
          <el-input v-model="saveUserForm.username" :disabled="isEdit" clearable placeholder="只支持英文、数字、下划线，5~50字"/>
        </el-form-item>
        <el-form-item label="真实姓名" prop="realName">
          <el-input v-model="saveUserForm.realName" :disabled="isEdit" clearable/>
        </el-form-item>
        <el-form-item label="手机号" prop="tel">
          <el-input v-model="saveUserForm.tel" type="tel" clearable/>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="saveUserForm.email" type="email" clearable/>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="saveUserForm.status" clearable placeholder="请选择状态">
            <el-option v-for="userStatus in userStatusList" :key="userStatus.status" :label="userStatus.desc"
                       :value="userStatus.status"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="角色" prop="roleIds">
          <el-select v-model="saveUserForm.roleIds" class="dialog-role-select" clearable multiple filterable
                     placeholder="请选择一个或多个角色"
          >
            <el-option v-for="role in roleList" :key="role.id" :label="role.roleName" :value="role.id"/>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="saveUserFormVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="isEdit ? updateUser() : addUser()">
          确认
        </el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import { pageUser, addUser, updateUser, deleteUser, resetPassword } from '@/api/system/user'
import { pageRole } from '@/api/system/role'
import { usernameValidator, realNameValidator, telValidator, emailValidator } from '@/utils/validate'

// 状态对应关系统一维护在此list中
const userStatusList = [
  { status: 0, desc: '正常', tagType: 'success' },
  { status: 1, desc: '禁用', tagType: 'danger' }
]

// 将list转换为map
const userStatusMap = userStatusList.reduce((acc, cur) => {
  acc[cur.status] = cur
  return acc
}, {})

export default {
  name: 'SystemUser',
  filters: {
    userStatusFilter(status) {
      return userStatusMap[status].desc
    }
  },
  data() {
    return {
      userStatusList: userStatusList,
      userStatusMap: userStatusMap,
      isEdit: false,
      listLoading: true,
      userQuery: {
        username: '',
        realName: '',
        tel: '',
        email: '',
        status: undefined,
        roleId: undefined,
        pageNum: 1,
        pageSize: 10
      },
      roleQuery: {
        roleName: '',
        status: 0,
        pageNum: 1,
        pageSize: 10000
      },
      userPage: {
        total: 0,
        pages: 0,
        list: []
      },
      roleList: [],
      roleMap: {},
      saveUserForm: {
        id: undefined,
        username: '',
        realName: '',
        tel: '',
        email: '',
        status: undefined,
        roleIds: []
      },
      saveUserFormVisible: false,
      saveUserRules: {
        username: [{ required: true, trigger: 'blur', validator: usernameValidator }],
        realName: [{ required: true, trigger: 'blur', validator: realNameValidator }],
        tel: [{ required: true, trigger: 'blur', validator: telValidator }],
        email: [{ required: true, trigger: 'blur', validator: emailValidator }],
        status: [{ required: true, message: '请选择状态', trigger: 'blur' }],
        roleIds: [{ required: true, message: '请选择角色', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.getRoleList()
    this.getUserList()
  },
  methods: {
    getUserList() {
      this.listLoading = true
      pageUser(this.userQuery).then(response => {
        this.userPage = response.data
        this.listLoading = false
      })
    },
    getRoleList() {
      pageRole(this.roleQuery).then(response => {
        this.roleList = response.data.list
        this.roleMap = this.roleList.reduce((acc, cur) => {
          acc[cur.id] = cur.roleName
          return acc
        }, {})
      })
    },
    handlerSizeChange(currentPageSize) {
      this.userQuery.pageSize = currentPageSize
      this.getUserList()
    },
    handleCurrentChange(currentPageNum) {
      this.userQuery.pageNum = currentPageNum
      this.getUserList()
    },
    search() {
      this.userQuery.pageNum = 1
      this.getUserList()
    },
    resetUserQuery() {
      this.userQuery = {
        username: '',
        realName: '',
        tel: '',
        email: '',
        status: undefined,
        roleId: undefined,
        pageNum: 1,
        pageSize: 10
      }
    },
    resetSaveUserForm() {
      this.saveUserForm = {
        id: undefined,
        username: '',
        realName: '',
        tel: '',
        email: '',
        status: undefined,
        roleIds: []
      }
    },
    handleAdd() {
      this.resetSaveUserForm()
      this.isEdit = false
      this.saveUserFormVisible = true
      this.$nextTick(() => {
        this.$refs['saveUserForm'].clearValidate()
      })
    },
    addUser() {
      this.$refs['saveUserForm'].validate((valid) => {
        if (valid) {
          addUser(this.saveUserForm).then(() => {
            this.saveUserFormVisible = false
            this.$message({
              type: 'success',
              message: '添加系统用户成功'
            })
            this.getUserList()
          })
        }
      })
    },
    handleEdit(row) {
      this.saveUserForm = Object.assign({}, row) // copy obj
      this.isEdit = true
      this.saveUserFormVisible = true
      this.$nextTick(() => {
        this.$refs['saveUserForm'].clearValidate()
      })
    },
    updateUser() {
      this.$refs['saveUserForm'].validate((valid) => {
        if (valid) {
          updateUser(this.saveUserForm).then(() => {
            this.saveUserFormVisible = false
            this.$message({
              type: 'success',
              message: '更新系统用户成功'
            })
            this.getUserList()
          })
        }
      })
    },
    handleDelete(row) {
      this.$confirm(`是否删除用户【${row.username}_${row.realName}】?`, '操作提示', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async() => {
        await deleteUser(row.id)
        this.$message({
          type: 'success',
          message: '删除系统用户成功'
        })
        this.getUserList()
      }).catch(err => {
        console.error(err)
      })
    },
    handleResetPassword(row) {
      this.$confirm(`是否重置用户【${row.username}_${row.realName}】的密码?`, '操作提示', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async() => {
        await resetPassword(row.id)
        this.$message({
          type: 'success',
          message: '重置密码成功'
        })
      }).catch(err => {
        console.error(err)
      })
    }
  }
}
</script>

<style scoped>
.dialog-role-select {
  width: 100%;
}
</style>

