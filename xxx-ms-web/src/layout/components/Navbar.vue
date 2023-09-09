<template>
  <div class="navbar">
    <hamburger :is-active="sidebar.opened" class="hamburger-container" @toggleClick="toggleSideBar"/>

    <breadcrumb class="breadcrumb-container"/>

    <!-- 右上角下拉菜单 -->
    <div class="right-menu">
      <el-dropdown class="avatar-container" trigger="click">
        <div class="avatar-wrapper">
          <img src="@/assets/images/avatar.gif" class="user-avatar">
          <i class="el-icon-caret-bottom"/>
        </div>
        <el-dropdown-menu slot="dropdown" class="user-dropdown">
          <router-link to="/">
            <el-dropdown-item>首页</el-dropdown-item>
          </router-link>
          <a target="_blank" href="https://github.com/chy1984/ms-template">
            <el-dropdown-item>Github</el-dropdown-item>
          </a>
          <el-dropdown-item @click.native="updatePasswordFormVisible = true">
            <span>修改密码</span>
          </el-dropdown-item>
          <el-dropdown-item @click.native="logout">
            <span style="display:block;">退出登录</span>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>

    <!-- 修改密码表单 -->
    <el-dialog title="修改密码" :visible.sync="updatePasswordFormVisible">
      <el-form ref="updatePasswordForm" :rules="updatePasswordRules" :model="updatePasswordForm"
               label-position="left" label-width="80px"
               style="width: 500px; margin-left:50px;"
      >
        <el-form-item label="原密码" prop="oldPassword">
          <el-input v-model="updatePasswordForm.oldPassword" show-password placeholder="只支持英文、数字、下划线"/>
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input v-model="updatePasswordForm.newPassword" show-password/>
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input v-model="updatePasswordForm.confirmPassword" show-password/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="updatePassword">
          确认
        </el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import Breadcrumb from '@/components/Breadcrumb'
import Hamburger from '@/components/Hamburger'
import { passwordValidator } from '@/utils/validate'

export default {
  components: {
    Breadcrumb,
    Hamburger
  },
  computed: {
    ...mapGetters([
      'sidebar'
    ])
  },
  data() {
    const newPasswordValidator = (rule, value, callback) => {
      if (value === this.updatePasswordForm.oldPassword) {
        callback(new Error('新旧密码不能相同'))
      } else {
        callback()
      }
    }
    const confirmPasswordValidator = (rule, value, callback) => {
      if (value !== this.updatePasswordForm.newPassword) {
        callback(new Error('两次输入的密码不一致'))
      } else {
        callback()
      }
    }
    return {
      updatePasswordFormVisible: false,
      updatePasswordForm: {
        oldPassword: '',
        newPassword: '',
        confirmPassword: ''
      },
      updatePasswordRules: {
        oldPassword: [{ required: true, trigger: 'blur', validator: passwordValidator }],
        newPassword: [
          { required: true, trigger: 'blur', validator: passwordValidator },
          { required: true, trigger: 'blur', validator: newPasswordValidator }
        ],
        confirmPassword: [{ required: true, trigger: 'blur', validator: confirmPasswordValidator }]
      }
    }
  },
  methods: {
    toggleSideBar() {
      this.$store.dispatch('app/toggleSideBar')
    },
    async updatePassword() {
      await this.$store.dispatch('user/updatePassword', this.updatePasswordForm)
      this.updatePasswordFormVisible = false
      this.$message({
        type: 'success',
        message: '密码修改成功，请重新登录'
      })
      this.$router.push(`/login?redirect=${this.$route.fullPath}`)
    },
    async logout() {
      await this.$store.dispatch('user/logout')
      this.$router.push(`/login?redirect=${this.$route.fullPath}`)
    }
  }
}
</script>

<style lang="scss" scoped>
.navbar {
  height: 50px;
  overflow: hidden;
  position: relative;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, .08);

  .hamburger-container {
    line-height: 46px;
    height: 100%;
    float: left;
    cursor: pointer;
    transition: background .3s;
    -webkit-tap-highlight-color: transparent;

    &:hover {
      background: rgba(0, 0, 0, .025)
    }
  }

  .breadcrumb-container {
    float: left;
  }

  .right-menu {
    float: right;
    height: 100%;
    line-height: 50px;

    &:focus {
      outline: none;
    }

    .right-menu-item {
      display: inline-block;
      padding: 0 8px;
      height: 100%;
      font-size: 18px;
      color: #5a5e66;
      vertical-align: text-bottom;

      &.hover-effect {
        cursor: pointer;
        transition: background .3s;

        &:hover {
          background: rgba(0, 0, 0, .025)
        }
      }
    }

    .avatar-container {
      margin-right: 30px;

      .avatar-wrapper {
        margin-top: 5px;
        position: relative;

        .user-avatar {
          cursor: pointer;
          width: 40px;
          height: 40px;
          border-radius: 10px;
        }

        .el-icon-caret-bottom {
          cursor: pointer;
          position: absolute;
          right: -20px;
          top: 25px;
          font-size: 12px;
        }
      }
    }
  }
}
</style>
