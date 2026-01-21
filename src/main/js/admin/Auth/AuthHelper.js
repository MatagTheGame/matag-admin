const TOKEN = 'token'

export default class AuthHelper {
  static getToken() {
    return sessionStorage.getItem(TOKEN)
  }

  static setToken(token) {
    return sessionStorage.setItem(TOKEN, token)
  }

  static removeToken() {
    sessionStorage.removeItem(TOKEN)
  }

  static isLoggedIn(state) {
    return !!state.session?.profile?.username
  }

  static isNonGuest(state) {
    return AuthHelper.isLoggedIn(state) && state.session?.profile?.type !== 'GUEST'
  }
}