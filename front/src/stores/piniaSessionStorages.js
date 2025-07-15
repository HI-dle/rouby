export function sessionStoragePlugin({ store }) {
  // 항상 로그인 하는 경우 로컬 스토리지로 적용 가능

  const saved = sessionStorage.getItem(store.$id)
  if (saved) {
    store.$patch(JSON.parse(saved))
  }

  store.$subscribe((_mutation, state) => {
    sessionStorage.setItem(store.$id, JSON.stringify(state))
  })
}
