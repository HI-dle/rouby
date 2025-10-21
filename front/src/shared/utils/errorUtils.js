function extractErrorData(err) {
  return err.response?.data || {}
}

export function createApiError(
  err,
  { targetField, fieldMessages, fallbackMessage = '오류가 발생했습니다.' } = {},
) {
  const {
    errors: list = [],
    message: dataMsg,
    code: code,
  } = extractErrorData(err)

  if (list.length > 0) {
    if (fieldMessages) {
      const fieldErrors = {}
      list.forEach(({ value, message }) => {
        fieldErrors[value] = fieldMessages?.[value] ?? message
      })
      const e = new Error(dataMsg || fallbackMessage)
      e.fieldErrors = fieldErrors
      e.code = code
      return e
    }

    if (targetField) {
      const fe = list.find((e) => e.value === targetField)
      const msg = fe?.message || dataMsg || fallbackMessage
      const e = new Error(msg)
      e.code = code
      return e
    }
  }

  const e = new Error(dataMsg || err.message || fallbackMessage)
  e.code = code
  return e
}

export function wrapApi(apiFn, options) {
  return async (...args) => {
    try {
      return await apiFn(...args)
    } catch (err) {
      throw createApiError(err, options)
    }
  }
}
