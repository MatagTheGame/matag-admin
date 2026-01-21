export default class DecksSelectorUtils {
  static getDeckType(state) {
    return state.deck?.type ?? 'random'
  }
}