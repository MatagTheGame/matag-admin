import AuthHelper from 'admin/Auth/AuthHelper'
import AuthReducer from 'admin/Auth/AuthReducer'
import PlayReducer from 'admin/Play/PlayReducer'

const clone = (object) => {
  if (!object) {
    object = {}
  }
  return JSON.parse(JSON.stringify(object))
}

export default (state, action) => {
  const newState = clone(state)

  if (!state) {
    return {
      session: {
        loading: true, token: AuthHelper.getToken()
      },
      play: {}
    }
  } else if (action.type.indexOf('@@') > -1) {
    // ignore all of these action types

  } else if (action.type === 'CONFIG_LOADED') {
    newState.config = action.value

  } else if (action.type === 'PROFILE_LOADED') {
    newState.session.loading = false
    newState.session.profile = action.value

  } else if (action.type === 'STATS_LOADING') {
    newState.stats = {loading: true}

  } else if (action.type === 'STATS_LOADED') {
    newState.stats.loading = false
    newState.stats.value = action.value

  } else if (AuthReducer.getActions().indexOf(action.type) >= 0) {
    AuthReducer.reduceAction(newState, action)

  } else if (PlayReducer.getActions().indexOf(action.type) >= 0) {
    PlayReducer.reduceAction(newState, action)

  } else {
    throw new Error(`Unknown action type ${action.type}`)
  }

  return newState
}