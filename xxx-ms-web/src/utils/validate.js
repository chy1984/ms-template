// 通用格式：英文字母、数字、下划线
const commonFormat = /^\w+$/

const telFormat = /(13\d|14[579]|15[^4\D]|17[^49\D]|18\d)\d{8}/

const emailFormat = /\w[-\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\.)+[A-Za-z]{2,14}/

// 外链格式
const externalFormat = /^(https?:|mailto:|tel:)/

/**
 * @param {string} path
 * @returns {Boolean}
 */
export function isExternal(path) {
  return externalFormat.test(path)
}

export function isCommonFormat(str) {
  return commonFormat.test(str)
}

export const usernameValidator = (rule, value, callback) => {
  if (!commonFormat.test(value)) {
    callback(new Error('用户名只能包含英文、数字、下划线，2~50个字符'))
  } else if (value.length < 2 || value.length > 50) {
    callback(new Error('用户名至少2个字符、至多50个字符'))
  } else {
    callback()
  }
}

export const realNameValidator = (rule, value, callback) => {
  if (value.length < 2 || value.length > 50) {
    callback(new Error('真实姓名至少2个字符、至多50个字符'))
  } else {
    callback()
  }
}

export const passwordValidator = (rule, value, callback) => {
  if (!commonFormat.test(value)) {
    callback(new Error('密码只能包含英文、数字、下划线'))
  } else if (value.length < 4 || value.length > 20) {
    callback(new Error('密码至少4个字符、至多20个字符'))
  } else {
    callback()
  }
}

export const telValidator = (rule, value, callback) => {
  if (!telFormat.test(value)) {
    callback(new Error('请输入正确的手机号'))
  } else {
    callback()
  }
}

export const emailValidator = (rule, value, callback) => {
  if (!emailFormat.test(value)) {
    callback(new Error('请输入正确的邮箱'))
  } else {
    callback()
  }
}
