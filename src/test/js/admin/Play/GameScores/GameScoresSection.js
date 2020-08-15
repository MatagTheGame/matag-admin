import TestUtils from '../../utils/TestUtils'

export default class GameHistorySection {
  constructor(element) {
    this.element = element
  }

  getGameScoresTable() {
    return this.element.querySelector('#score-board-table')
  }

  getGameScoresTableRows() {
    return TestUtils.tableDataAsStrings(this.getGameScoresTable())
  }
}