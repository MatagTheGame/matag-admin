import get from 'lodash/get'

export default class DecksSelectorUtils {
  static getDeckType(state) {
    return get(state, 'decks.type', 'random')
  }
}