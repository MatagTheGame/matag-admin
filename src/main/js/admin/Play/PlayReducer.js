export default class PlayReducer {
  static getActions() {
    return ['ACTIVE_GAME_LOADING', 'ACTIVE_GAME_LOADED', 'ACTIVE_GAME_DELETING', 'ACTIVE_GAME_DELETED',
      'GAME_HISTORY_LOADING', 'GAME_HISTORY_LOADED']
  }

  static reduceAction(newState, action) {
    switch (action.type) {
    case 'ACTIVE_GAME_LOADING':
      newState.play.activeGame = {loading: true}
      break

    case 'ACTIVE_GAME_LOADED':
      newState.play.activeGame.loading = false
      newState.play.activeGame.value = action.value
      break

    case 'ACTIVE_GAME_DELETING':
      newState.play.activeGame.deleting = true
      break

    case 'ACTIVE_GAME_DELETED':
      newState.play.activeGame.deleting = false

      if (!action.response.error) {
        newState.play.activeGame.value = {}
      } else {
        newState.play.activeGame.deletingError = action.response.error
      }
      break

    case 'GAME_HISTORY_LOADING':
      newState.play.gameHistory = {loading: true}
      break

    case 'GAME_HISTORY_LOADED':
      newState.play.gameHistory.loading = false
      newState.play.gameHistory.value = action.value
      break
    }
  }
}