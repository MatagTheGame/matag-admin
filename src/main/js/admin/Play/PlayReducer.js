export default class PlayReducer {
  static getActions() {
    return ['ACTIVE_GAME_LOADING', 'ACTIVE_GAME_LOADED', 'ACTIVE_GAME_DELETING', 'ACTIVE_GAME_DELETED']
  }

  static reduceAction(newState, action) {
    switch (action.type) {
      case 'ACTIVE_GAME_LOADING':
        newState.activeGame = {loading: true}
        break

      case 'ACTIVE_GAME_LOADED':
        newState.activeGame.loading = false
        newState.activeGame.value = action.value
        break

      case 'ACTIVE_GAME_DELETING':
        newState.activeGame.deleting = true
        break

      case 'ACTIVE_GAME_DELETED':
        newState.activeGame.deleting = false

        if (!action.response.error) {
          newState.activeGame.value = {}
        } else {
          newState.activeGame.deletingError = action.response.error
        }
        break
    }
  }
}