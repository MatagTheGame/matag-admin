import {getNodeText} from '@testing-library/react'

export default class StatsSection {
  constructor(element) {
    this.element = element
  }

  validateStats(stats) {
    const lis = this.element.querySelectorAll('li')
    this.validateStat(lis[0], 'TOTAL USERS: ', '2')
    this.validateStat(lis[1], 'ONLINE USERS: ', '1')
    this.validateStat(lis[2], 'TOTAL CARDS: ', '300')
    this.validateStat(lis[3], 'TOTAL SETS: ', '10 (from Ixalan)')
  }

  validateStat(li, text, value) {
    expect(getNodeText(li.childNodes[0].childNodes[0])).toBe(text)
    expect(getNodeText(li.childNodes[1])).toBe(value)
  }
}