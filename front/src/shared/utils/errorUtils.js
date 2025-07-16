function extractErrorData(err) {
  return err.response?.data || {};
}

export function createApiError(err, {
  targetField,
  fieldMessages,
  fallbackMessage = '오류가 발생했습니다.'
} = {}) {
  const { errors: list = [], message: dataMsg } = extractErrorData(err);

  if (list.length > 0) {
    if (fieldMessages) {
      const fieldErrors = {};
      list.forEach(({ value, message }) => {
        fieldErrors[value] = fieldMessages?.[value] ?? message;
      });
      const e = new Error(dataMsg || fallbackMessage);
      e.fieldErrors = fieldErrors;
      return e;
    }

    if (targetField) {
      const fe = list.find(e => e.value === targetField);
      const msg = fe?.message || dataMsg || fallbackMessage;
      return new Error(msg);
    }
  }

  return new Error(dataMsg || err.message || fallbackMessage);
}

export function wrapApi(apiFn, options) {
  return async (...args) => {
    try {
      return await apiFn(...args);
    } catch (err) {
      throw createApiError(err, options);
    }
  }
}
