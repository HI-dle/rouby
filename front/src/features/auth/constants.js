export const passwordRequirements = {
  minLength: 8,
  maxLength: 32,
  pattern: /^(?![A-Za-z]+$)(?!\d+$)(?![\W_]+$)[A-Za-z\d\W_]{8,32}$/,
}

export const timerConfig = {
  verificationTimeout: 300,
  resendCooldown: 60,
  maxResendAttempts: 5
}

export const errorMessages = {
  email: {
    required: '이메일을 입력해주세요.',
    invalid: '올바른 이메일 형식이 아닙니다.',
    duplicate: '이미 사용중인 이메일입니다.',
    verification: '이메일 인증이 필요합니다.',
  },
  password: {
    required: '비밀번호를 입력해주세요.',
    minLength: '비밀번호는 8자 이상이어야 합니다.',
    maxLength: '비밀번호는 32자 이하여야 합니다.',
    pattern: '영문 대·소문자/숫자/특수문자 중 2가지 이상 조합, 8자~32자',
    mismatch: '비밀번호가 일치하지 않습니다.',
  },
  verification: {
    required: '인증번호를 입력해주세요.',
    invalid: '인증번호가 올바르지 않습니다.',
    expired: '인증번호가 만료되었습니다. 다시 요청해주세요.',
  },
  general: {
    networkError: '네트워크 오류가 발생했습니다.',
    unexpectedError: '예상치 못한 오류가 발생했습니다.',
  }
}

export const successMessages = {
  verification: {
    sent: '인증번호를 이메일로 전송하였습니다.',
    completed: '이메일 인증이 완료되었습니다.',
  },
  signup: {
    completed: '회원가입이 완료되었습니다.',
  }
}
