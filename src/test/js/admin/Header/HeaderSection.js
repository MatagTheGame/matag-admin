import {getNodeText} from '@testing-library/react'

export default class HeaderSection {
  constructor(element) {
    this.element = element
  }

  expectTitleToBeMatagTheGame() {
    expect(getNodeText(this.element.querySelector('h1'))).toBe('Matag: The Game')
  }

  expectMenuItems(expectedItemsText) {
    const items = this.element.querySelectorAll('nav')
    const itemsText = Array.from(items).map((item) => getNodeText(item.childNodes[0]))
    expect(itemsText).toEqual(expectedItemsText)
  }
}