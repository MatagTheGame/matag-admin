export default class DecksReducer {
  static getActions() {
    return ['DECK_TYPE']
  }

  static reduceAction(newState, action) {
    switch (action.type) {
    case 'DECK_TYPE':
      newState.decks.type = action.value
      break
    }
  }
}