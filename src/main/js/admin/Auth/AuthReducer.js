import AuthHelper from 'admin/Auth/AuthHelper'

export default class AuthReducer {
  static getActions() {
    return ['LOGIN_LOADING', 'LOGIN_RESPONSE', 'REGISTER_LOADING', 'REGISTER_RESPONSE', 'VERIFY_LOADING', 'VERIFY_LOADED',
      'CHANGE_PASSWORD_LOADING', 'CHANGE_PASSWORD_RESPONSE']
  }

  static reduceAction(newState, action) {
    switch (action.type) {
    case 'LOGIN_LOADING':
      newState.login = {loading: true}
      break

    case 'LOGIN_RESPONSE':
      newState.login = {loading: false}
      if (action.value.error) {
        newState.login.error = action.value.error
      } else {
        AuthHelper.setToken(action.value.token)
        newState.session = {
          loading: false,
          token: action.value.token,
          profile: action.value.profile
        }
      }
      break

    case 'REGISTER_LOADING':
      newState.register = {loading: true}
      break

    case 'REGISTER_RESPONSE':
      newState.register.loading = false
      newState.register.value = action.value
      break

    case 'VERIFY_LOADING':
      newState.verify = {loading: true}
      break

    case 'VERIFY_LOADED':
      newState.verify.loading = false
      newState.verify.value = action.value
      break

    case 'CHANGE_PASSWORD_LOADING':
      newState.changePassword = {loading: true}
      break

    case 'CHANGE_PASSWORD_RESPONSE':
      newState.changePassword.loading = false
      newState.changePassword.value = action.value
    }
  }
}