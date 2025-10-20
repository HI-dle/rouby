export const MOODS = {
  great: {
    key: 'great',
    val: '최고야',
    day: '최고의 하루',
    placeholder:
      '정말 즐겁고 보람찬 하루였을 거 같아요. 루비에게 더 얘기해주세요.',
  },
  good: {
    key: 'good',
    val: '좋아',
    day: '기분 좋은 하루',
    placeholder: '좋은 하루를 보내셨군요. 저도 기분이 좋네요. 어떤 하루였나요?',
  },
  soso: {
    key: 'soso',
    val: '괜찮아',
    day: '무난한 하루',
    placeholder: '무탈한 하루를 보내신 것 같아 다행이에요. 어떤 하루였나요?',
  },
  bad: {
    key: 'bad',
    val: '별로야',
    day: '힘들었던 하루',
    placeholder: '피곤하신가요? 오늘 어땠는지 루비에게 알려주세요.',
  },
  terrible: {
    key: 'terrible',
    val: '우울해',
    day: '우울한 하루',
    placeholder: '오늘 힘든 일이 있으셨나요? 루비에게 얘기해주세요.',
  },
}

export const MAX_LEN = {
  userInput: 1000,
}

export const LIST_ERR_MESSAGES = {
  noContent: '해당 일자에는 피드백 정보가 존재하지 않습니다.',
  invalidRequest: '아직 해당 일자의 피드백을 요청할 수 없습니다.',
}
