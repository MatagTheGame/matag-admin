import TestUtils from '../../utils/TestUtils'

export default class GameHistorySection {
  constructor(element) {
    this.element = element
  }

  getGameHistoryTable() {
    return this.element.querySelector('#game-history-table')
  }

  getGameHistoryTableRows() {
    return TestUtils.tableDataAsStrings(this.getGameHistoryTable())
  }
}