export const usernameValidator = (rule, value, callback) => {
  const usernameRegex = /^\w{2,50}$/
  if (!usernameRegex.test(value)) {
    callback(new Error('用户名只能包含英文、数字、下划线，2~50个字符'))
  } else {
    callback()
  }
}

export const realNameValidator = (rule, value, callback) => {
  const realNameRegex = /^[A-Za-z\u4e00-\u9fa5][A-Za-z ·\u4e00-\u9fa5]{0,48}[A-Za-z\u4e00-\u9fa5]$/
  if (!realNameRegex.test(value)) {
    callback(new Error('请输入正确的真实姓名'))
  } else {
    callback()
  }
}

export const passwordValidator = (rule, value, callback) => {
  const passwordRegex = /^\w{4,20}$/
  if (!passwordRegex.test(value)) {
    callback(new Error('密码只能包含英文、数字、下划线，4~20个字符'))
  } else {
    callback()
  }
}

export const telValidator = (rule, value, callback) => {
  const telRegex = /(13\d|14[579]|15[^4\D]|17[^49\D]|18\d)\d{8}/
  if (!telRegex.test(value)) {
    callback(new Error('请输入正确的手机号'))
  } else {
    callback()
  }
}

export const roleNameValidator = (rule, value, callback) => {
  if (value.trim() === '' || value.length < 2 || value.length > 50) {
    callback(new Error('角色名不能全是空格，2~50个字符'))
  } else {
    callback()
  }
}

export const resNameValidator = (rule, value, callback) => {
  if (value.trim() === '' || value.length < 2 || value.length > 100) {
    callback(new Error('资源名称不能全是空格，2~100个字符'))
  } else {
    callback()
  }
}

export const resUrlValidator = (rule, value, callback) => {
  const resUrlRegex = /^[\w\/\-\\*]{1,100}$/
  if (!resUrlRegex.test(value)) {
    callback(new Error('请输入正确的资源url'))
  } else {
    callback()
  }
}
