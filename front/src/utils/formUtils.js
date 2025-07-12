export function buildFieldValidator(errors) {
  return (validatorFn, field, ...validatorArgs) => {
    const errorMsg = validatorFn(...validatorArgs);
    if (errorMsg) errors[field] = errorMsg;
    else delete errors[field];
    return !errorMsg;
  }
}

export function buildErrorCleaner(errors, fieldGetters) {
  return (newState, oldState) => {
    fieldGetters.forEach(({ field, get }) => {
      if (get(newState) !== get(oldState) && errors[field]) {
        delete errors[field];
      }
    });
  }
}
