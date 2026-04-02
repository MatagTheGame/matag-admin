export default class StatsSection {
  constructor(element) {
    this.element = element
  }

  validateStats(stats) {
    const lis = this.element.querySelectorAll('li')
    this.validateStat(lis[0], 'TOTAL USERS: ', stats.totalUsers.toString())
    this.validateStat(lis[1], 'ONLINE USERS: ', stats.onlineUsers.length + ' (' + stats.onlineUsers.join('') + ')')
    this.validateStat(lis[2], 'TOTAL CARDS: ', stats.totalCards.toString())
    this.validateStat(lis[3], 'TOTAL SETS: ', stats.totalSets + ' (from Magic Origins)')
  }

  validateStat(li, text, value) {
    expect(li.childNodes[0].childNodes[0].textContent).toBe(text)
    expect(li.childNodes[1].textContent).toBe(value)
  }
}