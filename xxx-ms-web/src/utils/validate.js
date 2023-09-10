/**
 * 是否是外链
 *
 * @param {string} path
 * @returns {Boolean}
 */
export function isExternal(path) {
  const externalRegex = /^(https?:|mailto:|tel:)/
  return externalRegex.test(path)
}

