import has from 'lodash/has'
import get from 'lodash/get'

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
    return has(state, 'session.profile.username')
  }

  static isNonGuest(state) {
    return AuthHelper.isLoggedIn(state) && get(state, 'session.profile.type') !== 'GUEST'
  }
}