export default class DecksReducer {
  static getActions() {
    return ['DECK_TYPE', 'DECK_SELECT_LOADING', 'DECK_SELECT_LOADED', 'DECK_START_LOADING', 'DECK_SET_ERROR',
      'DECK_SET_ACTIVE_GAME_ID', 'DECK_RANDOM']
  }

  static reduceAction(newState, action) {
    switch (action.type) {
    case 'DECK_TYPE':
      newState.decks.type = action.value
      break

    case 'DECK_SELECT_LOADING':
      newState.decks.select.loading = true
      break

    case 'DECK_SELECT_LOADED':
      newState.decks.select.loaded = false
      break

    case 'DECK_START_LOADING':
      newState.decks.start.loaded = false
      break

    case 'DECK_SET_ERROR':
      newState.decks.start.error = action.value
      break

    case 'DECK_SET_ACTIVE_GAME_ID':
      newState.decks.start.activeGameId = action.value
      break

    case 'DECK_RANDOM':
      newState.decks.random = action.value
      break
    }
  }
}