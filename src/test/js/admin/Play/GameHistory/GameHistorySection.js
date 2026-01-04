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
      .map(it => it.slice(1)) // remove date as the format is different from server and local... can't be bother to fix it.
  }
}